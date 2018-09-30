package com.gwg.demo.md5.test1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
/**
 * MD5是一种签名算法，是不可逆，所谓的MD5加密解密指的是对MD5生成的签名加密解密，而不是对MD5签名的解密
 *
 */
public class MD5Utils {

    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
	
	public static final String KEY_MD5 = "MD5";

	/**
	 *使用MD5生成签名，MD5生成的签名字节长度为16位
	 */
	public static byte[] encrypt(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		logger.info("MD5签名：{}， MD5签名长度：{}", md5.digest(), md5.digest().length);
        byte[] bytes = md5.digest();
		return md5.digest();
	}

}

 
