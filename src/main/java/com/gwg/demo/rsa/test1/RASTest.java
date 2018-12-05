package com.gwg.demo.rsa.test1;

import com.gwg.utils.Base64Utils;
import com.gwg.utils.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * RSA生成签名以及验证签名
 * 參考：https://www.cnblogs.com/linjiqin/p/6005626.html
 */
public class RASTest {

    private static final String ENCODER_CHARTER = "utf-8";
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final Logger logger = LoggerFactory.getLogger(RASTest.class);

    public static String rsaPrivate="";
    public static String rsaPublic = "";

    public static void main(String[] args) throws Exception{

        String data = "gaoweigang";

        //私钥进行加密
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data.getBytes(ENCODER_CHARTER), Base64Utils.decoder(rsaPrivate));

        String sign = getSignRSA(encodedData, rsaPrivate);
        boolean flag = verify(encodedData, rsaPublic, sign);

        System.out.println(flag);

    }

    /**
     * 对信息用私钥生成签名
     * @param data 已加密数据
     * @param prikeyvalue 私钥(Base64编码)
     * @return
     */
    public static String getSignRSA(byte[] data, String prikeyvalue) throws Exception{
        byte[] keyBytes = Base64Utils.decoder(prikeyvalue);
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
        // 用私钥对信息生成数字签名
        Signature signet = Signature.getInstance(SIGNATURE_ALGORITHM);
        signet.initSign(myprikey);
        signet.update(data);
        byte[] signed = signet.sign(); // 对信息的数字签名
        logger.debug("RSA签名串---》" + new String(Base64.encodeBase64(signed)));
        return new String(Base64.encodeBase64(signed));
    }

    /**
     * <p>
     * 用公钥对签名进行校验
     * </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decoder(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decoder(sign));
    }



}
