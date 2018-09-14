package com.gwg.utils;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public final class HttpUtils {
	public static final String HTTP_PROTOCOL_PROTOBUF = "application/protobuf";// protobuf协议
	public static final String HTTP_PROTOCOL_JSON = "application/json";// json协议
	public static final String HTTP_PROTOCOL_DEFAULT = "application/json; charset=utf-8";// 默认
	public static final int BUF_MAX_LENTH = 1024 * 64; // buf最大长度
	public static final int DEFAULT_TIMEOUT = 60 * 1000; // 超时时间

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	private HttpUtils() {
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	// ***************************************HTTP***************************************
	/**
	 * 发送HTTP GET请求并获取结果
	 * 
	 * @param goUrl
	 * @return @
	 * @throws Exception
	 */
	public static String HttpGetGo(String goUrl) throws Exception {
		return HttpTextGo(goUrl, "GET", null, HTTP_PROTOCOL_DEFAULT, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	/**
	 * 发送HTTP GET请求并获取结果
	 * 
	 * @param goUrl
	 * @param connectTimeout
	 * @param readTimeout
	 * @return @
	 * @throws Exception
	 */
	public static String HttpGetGo(String goUrl, int connectTimeout, int readTimeout) throws Exception {
		return HttpTextGo(goUrl, "GET", null, HTTP_PROTOCOL_DEFAULT, connectTimeout, readTimeout);
	}

	/**
	 * 发送HTTP POST请求并返回结果
	 * 
	 * @param goURL
	 * @param body
	 * @return @
	 * @throws Exception
	 */
	public static String HttpPostGo(String goURL, String body) throws Exception {
		return HttpTextGo(goURL, "POST", body, HTTP_PROTOCOL_DEFAULT, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	/**
	 * 发送HTTP POST请求并返回结果
	 * 
	 * @param goURL
	 * @param body
	 * @param connectTimeout
	 * @param readTimeout
	 * @return @
	 * @throws Exception
	 */
	public static String HttpPostGo(String goURL, String body, int connectTimeout, int readTimeout) throws Exception {
		return HttpTextGo(goURL, "POST", body, HTTP_PROTOCOL_DEFAULT, connectTimeout, readTimeout);
	}

	/**
	 * 发送HTTP POST请求并返回结果
	 * 
	 * @param goURL
	 * @param body
	 * @return @
	 * @throws Exception
	 */
	public static String HttpPostGo(String goURL, String body, String contentType) throws Exception {
		return HttpTextGo(goURL, "POST", body, contentType, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	/**
	 * 发送HTTP POST请求并返回结果
	 * 
	 * @param goURL
	 * @param body
	 * @param contentType
	 * @param connectTimeout
	 * @param readTimeout
	 * @return @
	 * @throws Exception
	 */
	public static String HttpPostGo(String goURL, String body, String contentType, int connectTimeout, int readTimeout) throws Exception {
		return HttpTextGo(goURL, "POST", body, contentType, connectTimeout, readTimeout);
	}

	/**
	 * 文本请求,文本返回
	 * 
	 * @param goURL
	 * @param method
	 * @param body
	 * @param contentType
	 * @return @
	 */
	private static String HttpTextGo(String goURL, String method, String body, String contentType, int connectTimeout, int readTimeout)
			throws Exception {
		long start = System.currentTimeMillis();
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(goURL).openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			// connection.setRequestProperty("Accept", "*/*");
			if (contentType != null) {
				connection.setRequestProperty("Content-Type", contentType);
			}
			BufferedReader bufferedReader = null;
			DataOutputStream out = null;
			if (body != null) {
				byte[] buff = body.getBytes(StringUtils.DEFAULT_CHARSET);
				connection.setRequestProperty("Content-Length", String.valueOf(buff.length));
				try {
					out = new DataOutputStream(connection.getOutputStream());
					out.write(buff);
					out.flush();
				} finally {
					if (out != null)
						out.close();
				}
			}
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer temp = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					temp.append(line);
				}

				String ecod = connection.getContentEncoding();
				if (ecod == null) {
					return temp.toString();
				} else {
					return new String(temp.toString().getBytes(), ecod);
				}
			} finally {
				if (bufferedReader != null)
					bufferedReader.close();
			}

		} catch (Exception e) {
			logger.error("HttpGo url={}, 耗时ms={}, body={}, exception={}", goURL, (System.currentTimeMillis() - start), body, e);
			throw new Exception("网络请求异常", e);
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	// ***************************************HTTPEND***************************************
	// ***************************************HTTPS***************************************
	/**
	 * 发送HTTPS GET请求并返回结果
	 * 
	 * @param goURL
	 * @return @
	 * @throws Exception
	 * @throws Exception
	 */
	public static String HttpsGetGo(String goURL) throws Exception {
		return HttpsGo(goURL, "GET", null, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	/**
	 * 发送HTTPS GET请求并返回结果
	 * 
	 * @param goURL
	 * @param connectTimeout
	 * @param readTimeout
	 * @return @
	 * @throws Exception
	 */
	public static String HttpsGetGo(String goURL, int connectTimeout, int readTimeout) throws Exception {
		return HttpsGo(goURL, "GET", null, connectTimeout, readTimeout);
	}

	/**
	 * 发送HTTPS POST请求并返回结果
	 * 
	 * @param goURL
	 * @return @
	 * @throws Exception
	 * @throws Exception
	 */
	public static String HttpsPostGo(String goURL, String body) throws Exception {
		return HttpsGo(goURL, "POST", body, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	/**
	 * 发送HTTPS POST请求并返回结果
	 * 
	 * @param goURL
	 * @param body
	 * @param connectTimeout
	 * @param readTimeout
	 * @return @
	 * @throws Exception
	 */
	public static String HttpsPostGo(String goURL, String body, int connectTimeout, int readTimeout) throws Exception {
		return HttpsGo(goURL, "POST", body, connectTimeout, readTimeout);
	}

	private static String HttpsGo(String goURL, String method, String body, int connectTimeout, int readTimeout) throws Exception {
		long start = System.currentTimeMillis();
		HttpsURLConnection connection = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(goURL);
			connection = (HttpsURLConnection) console.openConnection();
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			connection.setRequestMethod(method);
			connection.setRequestProperty("content-type", HTTP_PROTOCOL_DEFAULT);
			// connection.setRequestProperty("Accept", "*/*");

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);

			BufferedReader bufferedReader = null;
			DataOutputStream out = null;
			if (body != null) {
				byte[] buff = body.getBytes(StringUtils.DEFAULT_CHARSET);
				connection.setRequestProperty("Content-Length", String.valueOf(buff.length));
				try {
					out = new DataOutputStream(connection.getOutputStream());
					out.write(buff);
					out.flush();
				} finally {
					if (out != null)
						out.close();
				}
			}
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer temp = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					temp.append(line);
				}

				String ecod = connection.getContentEncoding();
				if (ecod == null) {
					return temp.toString();
				} else {
					return new String(temp.toString().getBytes(), ecod);
				}
			} finally {
				if (bufferedReader != null)
					bufferedReader.close();
			}

		} catch (Exception e) {
			logger.error("HttpsGo url={}, 耗时ms={}, body={}, exception={}", goURL, (System.currentTimeMillis() - start), body, e);
			throw new Exception("网络请求异常", e);
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	// ***************************************HTTPSEND***************************************
	public static Map<String, String> getParamsMap(String s) {
		Map<String, String> parms = Maps.newHashMap();
		if (s == null || s.length() == 0) {
			return parms;
		}
		String[] strs = s.split("&");
		for (String str : strs) {
			int index = str.indexOf("=");
			String k = str.substring(0, index);
			String v = str.substring(index + 1, str.length());

			parms.put(k, v);
		}
		return parms;
	}

	public static String HttpGo(String goURL, String method, String body) throws Exception {
		long start = System.currentTimeMillis();
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(goURL).openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "close"); // "Keep-Alive"
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("contentType", "application/x-www-form-urlencoded;charset=UTF-8");
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(60000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			BufferedReader bufferedReader = null;
			DataOutputStream out = null;
			if (body != null) {
				byte[] buff = body.getBytes(StringUtils.DEFAULT_CHARSET);
				connection.setRequestProperty("Content-Length", String.valueOf(buff.length));
				try {
					out = new DataOutputStream(connection.getOutputStream());
					out.write(buff);
					out.flush();
				} finally {
					if (out != null)
						out.close();
				}
			}
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer temp = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					temp.append(line);
				}

				String ecod = connection.getContentEncoding();
				if (ecod == null) {
					return temp.toString();
				} else {
					return new String(temp.toString().getBytes(), ecod);
				}
			} finally {
				if (bufferedReader != null)
					bufferedReader.close();
			}

		} catch (Exception e) {
			logger.error("HttpGo url={}, 耗时ms={}, body={}, exception={}", goURL, (System.currentTimeMillis() - start), body, e);
			throw new Exception("网络请求异常", e);
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public static String HttpsGo(String goURL, String method, String body) throws Exception {
		long start = System.currentTimeMillis();
		HttpsURLConnection connection = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(goURL);
			connection = (HttpsURLConnection) console.openConnection();
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			connection.setRequestMethod(method);

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "close"); // "Keep-Alive"
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("contentType", "application/x-www-form-urlencoded;charset=UTF-8");
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(60000);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			BufferedReader bufferedReader = null;
			DataOutputStream out = null;
			if (body != null) {
				byte[] buff = body.getBytes(StringUtils.DEFAULT_CHARSET);
				connection.setRequestProperty("Content-Length", String.valueOf(buff.length));
				try {
					out = new DataOutputStream(connection.getOutputStream());
					out.write(buff);
					out.flush();
				} finally {
					if (out != null)
						out.close();
				}
			}
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer temp = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					temp.append(line);
				}

				String ecod = connection.getContentEncoding();
				if (ecod == null) {
					return temp.toString();
				} else {
					return new String(temp.toString().getBytes(), ecod);
				}
			} finally {
				if (bufferedReader != null)
					bufferedReader.close();
			}

		} catch (Exception e) {
			logger.error("HttpsGo url={}, 耗时ms={}, body={}, exception={}", goURL, (System.currentTimeMillis() - start), body, e);
			throw new Exception("网络请求异常", e);
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}
}
