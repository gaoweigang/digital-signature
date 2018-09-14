package com.gwg.utils;

import java.security.MessageDigest;

public class MD5Utils {

	public static final String KEY_MD5 = "MD5";

	public static byte[] encrypt(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
	}

	/**
	 * md5 base64
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptToBase64(String data) throws Exception {
		return Base64Utils.encoder(encrypt(data.getBytes()));
	}

	/**
	 * md5 16进制
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String encryptToHex(String data) throws Exception {
		return HexStringUtils.encrypt(encrypt(data.getBytes()));
	}
}
