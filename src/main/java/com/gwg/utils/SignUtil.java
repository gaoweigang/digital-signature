package com.gwg.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignUtil {

	private static final String ENCODER_CHARTER = "utf-8";
	private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

	/**
	 * 生成签名串
	 * 
	 * @param reqObj
	 * @return
	 */
	public static String genSign(JSONObject reqObj, String prikeyvalue) {
		// // 生成待签名串生
		String sign_src = genSignData(reqObj);
		logger.info("原请求字符串: " + sign_src);
		return getSignRSA(sign_src, prikeyvalue);
	}

	/**
	 * 生成待签名串
	 * 
	 * @return
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			// sign 和ip_client 不参与签名
			if ("sign".equals(key)) {
				continue;
			}
			String value = (String) jsonObject.getString(key);
			// 空串不参与签名
			if (null == value) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}

	/**
	 * RSA签名验证
	 * 
	 * @return
	 */
	public static String getSignRSA(String sign_src, String prikeyvalue) {
		String str = sign(prikeyvalue, sign_src);
		logger.debug("RSA签名串---》" + str);
		return sign(prikeyvalue, sign_src);

	}

	/**
	 * 签名处理
	 * 
	 * @param prikeyvalue：私钥
	 * @param sign_str：签名源内容
	 * @return
	 */
	public static String sign(String prikeyvalue, String sign_str) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Utils.decoder(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(sign_str.getBytes(ENCODER_CHARTER));
			byte[] signed = signet.sign(); // 对信息的数字签名
			return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 签名验证
	 *
	 *
	 */
	public static boolean checksign(String pubkeyvalue, String oid_str, String signed_str) {
		try {
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64Utils.decoder(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			byte[] signed = Base64Utils.decoder(signed_str);// 这是SignatureData输出的数字签�?
			java.security.Signature signetcheck = java.security.Signature.getInstance("MD5withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(oid_str.getBytes("UTF-8"));
			return signetcheck.verify(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
