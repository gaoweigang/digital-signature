package com.gwg.utils;

public class HexStringUtils {

	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 16进制解密
	 */
	public static byte[] decrypt(String key) {
		char[] chars = key.toLowerCase().toCharArray();
		int j = chars.length / 2;
		byte[] ret = new byte[j];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte b1 = chars[k] < 65 ? (byte) (chars[k] - 48) : (byte) (chars[k] - 87);
			k++;
			byte b2 = chars[k] < 65 ? (byte) (chars[k] - 48) : (byte) (chars[k] - 87);
			k++;
			ret[i] = (byte) ((b1 << 4) | b2);
		}
		return ret;
	}

	/**
	 * 16进制加密
	 */
	public static String encrypt(byte[] key) {
		int j = key.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = key[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
}
