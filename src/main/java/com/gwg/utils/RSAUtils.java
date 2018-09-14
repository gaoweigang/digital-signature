package com.gwg.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	// 1024->117,2048->245
	private static final int KEY_LEANGTH = 1024;
	private static final KeyFactory keyFactory = getKeyFactory();
	private static final KeyPairGenerator keyPairGen = getKeyPairGenerator();

	/**
	 * 获取钥匙工厂
	 */
	private static KeyFactory getKeyFactory() {
		try {
			return KeyFactory.getInstance(KEY_ALGORITHM);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取钥匙串生成器
	 */

	private static KeyPairGenerator getKeyPairGenerator() {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator
					.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(KEY_LEANGTH);
			return keyPairGen;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取Cipher
	 */

	public static Cipher getCipher(byte[] keyBytes, boolean isPublic,
			boolean isEncrypt) throws Exception {
		if (isPublic) {
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			Key publicKey = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
					publicKey);
			return cipher;
		} else {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
					privateKey);
			return cipher;
		}
	}

	/**
	 * 获取Signature
	 */

	public static Signature getSignature(byte[] keyBytes, boolean isPublic)
			throws Exception {
		if (isPublic) {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(pubKey);
			return signature;
		} else {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(priKey);
			return signature;
		}
	}

	/**
	 * 用私钥对信息生成数字签名
	 */

	public static byte[] sign(byte[] data, byte[] privateKeyBytes)
			throws Exception {
		Signature signature = getSignature(privateKeyBytes, false);
		signature.update(data);
		return signature.sign();
	}

	/**
	 * 校验数字签名
	 */

	public static boolean verify(byte[] data, byte[] publicKeyBytes,
			byte[] signBytes) throws Exception {
		Signature signature = getSignature(publicKeyBytes, true);
		signature.update(data);
		return signature.verify(signBytes);
	}

	/**
	 * 用私钥解密
	 */

	public static byte[] decryptByPrivateKey(byte[] data, byte[] keyBytes)
			throws Exception {
		return getCipher(keyBytes, false, false).doFinal(data);
	}

	public static String base64DecryptByPrivateKey(String base64Data,
			String base64Key) throws Exception {
		return new String(getCipher(Base64Utils.decoder(base64Key), false,
				false).doFinal(Base64Utils.decoder(base64Data)));
	}

	/**
	 * 用私钥加密
	 */

	public static byte[] encryptByPrivateKey(byte[] data, byte[] keyBytes)
			throws Exception {
		return getCipher(keyBytes, false, true).doFinal(data);
	}

	public static String base64EncryptByPrivateKey(String data, String base64Key)
			throws Exception {
		return Base64Utils.encoder(getCipher(Base64Utils.decoder(base64Key),
				false, true).doFinal(data.getBytes()));
	}

	/**
	 * 用公钥解密
	 */

	public static byte[] decryptByPublicKey(byte[] data, byte[] keyBytes)
			throws Exception {
		return getCipher(keyBytes, true, false).doFinal(data);
	}

	public static String base64DecryptByPublicKey(String base64Data,
			String base64Key) throws Exception {
		return new String(
				getCipher(Base64Utils.decoder(base64Key), true, false).doFinal(
						Base64Utils.decoder(base64Data)));
	}

	/**
	 * 用公钥加密
	 */

	public static byte[] encryptByPublicKey(byte[] data, byte[] keyBytes)
			throws Exception {
		return getCipher(keyBytes, true, true).doFinal(data);
	}

	public static String base64EncryptByPublicKey(String data, String base64Key)
			throws Exception {
		return Base64Utils.encoder(getCipher(Base64Utils.decoder(base64Key),
				true, true).doFinal(data.getBytes()));
	}

	/**
	 * 初始化公钥密钥
	 */

	public static KeyPair initKey() throws Exception {
		return keyPairGen.generateKeyPair();
	}

	public static byte[] getPublicKeyBytes(KeyPair keyPair) throws Exception {
		return keyPair.getPublic().getEncoded();
	}

	public static String getPublicKeyBase64(KeyPair keyPair) throws Exception {
		return Base64Utils.encoder(getPublicKeyBytes(keyPair));
	}

	public static byte[] getPrivateKeyBytes(KeyPair keyPair) throws Exception {
		return keyPair.getPrivate().getEncoded();
	}

	public static String getPrivateKeyBase64(KeyPair keyPair) throws Exception {
		return Base64Utils.encoder(getPrivateKeyBytes(keyPair));
	}
}
