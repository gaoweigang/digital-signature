package com.gwg.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
public class Base64Utils {

	/**
	 * BASE64解密
	 */
	public static String decoder(byte[] key) {
		return StringUtils.newStringUtf8(Base64.decodeBase64(key));
	}

	/**
	 * BASE64加密
	 */
	public static String encoder(String key) {
		return Base64.encodeBase64String(StringUtils.getBytesUtf8(key));
	}

	/**
	 * BASE64解密
	 */
	public static byte[] decoder(String key) {
		return Base64.decodeBase64(key);
	}

	/**
	 * BASE64加密
	 */
	public static String encoder(byte[] key) {
		return Base64.encodeBase64String(key);
	}

	/**
	 * BASE64安全加密
	 */
	public static String safeEncoder(String key) {
		return Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf8(key));
	}

}
