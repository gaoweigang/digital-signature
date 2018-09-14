package com.gwg.utils;

import com.google.common.collect.Maps;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XmlUtils {
	private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);

	/**
	 * 根据Map组装xml消息体，值对象仅支持基本数据类型、String、BigInteger、BigDecimal，
	 * 以及包含元素为上述支持数据类型的Map
	 * 
	 * @param vo
	 * @param rootElement
	 * @return
	 */
	public static String maptoXml(Map<String, Object> vo, String rootElement) {
		Document doc = DocumentHelper.createDocument();
		Element body = DocumentHelper.createElement(rootElement);
		doc.add(body);
		_buildMaptoXml(body, vo);

		String xml = doc.asXML();
		xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		return xml;
	}

	@SuppressWarnings("unchecked")
	private static void _buildMaptoXml(Element body, Map<String, Object> vo) {
		if (vo != null) {
			Iterator<String> it = vo.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (StringUtils.isNotEmpty(key)) {
					Object obj = vo.get(key);
					Element element = DocumentHelper.createElement(key);
					if (obj != null) {
						if (obj instanceof String) {
							element.setText((String) obj);
						} else {
							if (obj instanceof Character || obj instanceof Boolean || obj instanceof Number
									|| obj instanceof java.math.BigInteger || obj instanceof java.math.BigDecimal) {
								org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", obj.getClass().getCanonicalName());
								element.add(attr);
								element.setText(String.valueOf(obj));
							} else if (obj instanceof Map) {
								org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", Map.class.getCanonicalName());
								element.add(attr);
								_buildMaptoXml(element, (Map<String, Object>) obj);
							} else {
							}
						}
					}
					body.add(element);
				}
			}
		}
	}

	/**
	 * 根据xml消息体转化为Map
	 *
	 * @param xml
	 * @param rootElement
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Object> xmltoMap(String xml, String rootElement) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element body = (Element) doc.selectSingleNode("/" + rootElement);
		Map<String, Object> vo = _buildXmltomap(body);
		return vo;
	}

	private static Map<String, Object> _buildXmltomap(Element body) {
		Map<String, Object> vo = Maps.newHashMap();
		if (body != null) {
			List<Element> elements = body.elements();
			for (Element element : elements) {
				String key = element.getName();
				if (StringUtils.isNotEmpty(key)) {
					String type = element.attributeValue("type", "java.lang.String");
					String text = element.getText().trim();
					Object value = null;
					if (String.class.getCanonicalName().equals(type)) {
						value = text;
					} else if (Character.class.getCanonicalName().equals(type)) {
						value = new Character(text.charAt(0));
					} else if (Boolean.class.getCanonicalName().equals(type)) {
						value = new Boolean(text);
					} else if (Short.class.getCanonicalName().equals(type)) {
						value = Short.parseShort(text);
					} else if (Integer.class.getCanonicalName().equals(type)) {
						value = Integer.parseInt(text);
					} else if (Long.class.getCanonicalName().equals(type)) {
						value = Long.parseLong(text);
					} else if (Float.class.getCanonicalName().equals(type)) {
						value = Float.parseFloat(text);
					} else if (Double.class.getCanonicalName().equals(type)) {
						value = Double.parseDouble(text);
					} else if (java.math.BigInteger.class.getCanonicalName().equals(type)) {
						value = new java.math.BigInteger(text);
					} else if (java.math.BigDecimal.class.getCanonicalName().equals(type)) {
						value = new java.math.BigDecimal(text);
					} else if (Map.class.getCanonicalName().equals(type)) {
						value = _buildXmltomap(element);
					} else {
					}
					vo.put(key, value);
				}
			}
		}
		return vo;
	}

	public static List<Map<String, Object>> xmltoList(String xml) {
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Document document = DocumentHelper.parseText(xml);
			Element nodesElement = document.getRootElement();
			List<Element> nodes = nodesElement.elements();
			for (Iterator<Element> its = nodes.iterator(); its.hasNext();) {
				Element nodeElement = (Element) its.next();
				Map<String, Object> map = xmltoMap(nodeElement.asXML(), "");
				list.add(map);
				map = null;
			}
			nodes = null;
			nodesElement = null;
			document = null;
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String listtoXml(List<?> list) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element nodesElement = document.addElement("nodes");
		int i = 0;
		for (Object o : list) {
			Element nodeElement = nodesElement.addElement("node");
			if (o instanceof Map) {
				for (Object obj : ((Map<?, ?>) o).keySet()) {
					Element keyElement = nodeElement.addElement("key");
					keyElement.addAttribute("label", String.valueOf(obj));
					keyElement.setText(String.valueOf(((Map<?, ?>) o).get(obj)));
				}
			} else {
				Element keyElement = nodeElement.addElement("key");
				keyElement.addAttribute("label", String.valueOf(i));
				keyElement.setText(String.valueOf(o));
			}
			i++;
		}
		return doctoString(document);
	}

	public static String doctoString(Document document) {
		String s = "";
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 使用UTF-8编码
			OutputFormat format = new OutputFormat("   ", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	/**
	 * 对象->xml
	 * default UTF-8
	 * @param xmlObj
	 * @return
	 */
	public static String beantoXml(Object xmlObj) {
		try {
			JAXBContext context = JAXBContext.newInstance(xmlObj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			StringWriter writer = new StringWriter();
			marshaller.marshal(xmlObj, writer);
			return writer.toString();
		} catch (JAXBException e) {
			logger.error("对象->xml异常" + e.getMessage());
		}
		return null;
	}
	/**
	 * 对象->xml  GBK
	 * 
	 * @param xmlObj
	 * @return
	 */
	public static String beantoXmlGBK(Object xmlObj) {
		try {
			JAXBContext context = JAXBContext.newInstance(xmlObj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			StringWriter writer = new StringWriter();
			marshaller.marshal(xmlObj, writer);
			return writer.toString();
		} catch (JAXBException e) {
			logger.error("对象->xml异常" + e.getMessage());
		}
		return null;
	}

	/**
	 * xml->对象
	 * 
	 * @param xml
	 * @param objType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmltoBean(String xml, Class<T> objType) {
		try {
			JAXBContext context = JAXBContext.newInstance(objType);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			logger.error("xml->对象异常" + e.getMessage());
		}
		return null;
	}

}