package com.gwg.demo.sign.test2;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 公钥 私钥存在形式也可以是字符串
 * 參考：https://www.cnblogs.com/ppldev/p/5110667.html
 */
public class SignTest {
    private static final Logger logger = LoggerFactory.getLogger(SignTest.class);

    static final Map<DigestAlgEnum, String> SIGN_ALG_MAP = new HashMap();

    //模拟 生成签名的数据
    String canonicalRequest = "gaoweigang";

    static {
        SIGN_ALG_MAP.put(DigestAlgEnum.SHA256, "SHA256withRSA");
        SIGN_ALG_MAP.put(DigestAlgEnum.SHA512, "SHA512withRSA");
    }

    @Test
    public void testRandomKey() throws Exception{

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);//生成大小 1024
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();//获取公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();//获取私钥
        logger.info("======>私钥转字符串：{}", Base64.encodeBase64String(privateKey.getEncoded()));
        logger.info("======>公钥转字符串：{}", Base64.encodeBase64String(publicKey.getEncoded()));
    }

    /**
     * 根据私钥字符串获取私钥, 生成签名
     * @throws Exception
     */
    @Test
    public void testPrivateKey() throws Exception{
        //私钥字符串
        String privateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJDwLXMAfu+vQljPEEAaEYaWDFhKwDYr0ypYudSoXmDkSMuEqw6fLEl0YLkRtMsONDNHn1wStJyx35KZIgsbuSxNBfrVl2NqrTdZ1HZqSO81z7D6oAFxW8kT3eGztTEzxiLourTM7cTpjX0ldaAWG6iYeBjiHRYVI59CtE/k97n5AgMBAAECgYBVJQNOH+hyHd84FMxTG/1XtyYhaChqqmwu4Fiq2Q44lWM+S/3BZTi7HicEEabH8twvk8/GYya/Nq9Ei7k4FV0+CLQ+cmCnvNU9zUuuLxsXbBvFUBQe+M39oN0QeiH17hRpNtlB1iiDxkWfSUaYC1t6ycmGKT39HvH1I7K2Lt/8QQJBAN01eLoJRhHcA7biDKhtaHkkI4ys8hrcQQCGk0rJw8P9iIGhb4IaPMJ9T7luJ72diB31xow1yglxS/glqYRoyWUCQQCnu9IzmuunMXd/B9PV9Oj3bd0W92bPgownDvPTDbzbMegtr2HBYcMMHFlDB7NhAcvdIVI8xJksBMAJeXB0JW8FAkAoZxXP1NXJeUfsZUkukh0c0y7zCLuHkGcG841/gW6SI4KFOrklKCmk1EJ1edskVNuBrVxAS6rFIr5fanGVz5XJAkBgHq3SjNDGOv6zeRTX40UtpXOxGY19FN6j81medJzpL6xRodNvMkrHwecVFnChApqE2fd6g18TbpVt2H2s0VbRAkAkjO9YEhEiiLXj++HRHIlox0jhUdQQ3L7grwB9jaxZw2ItZW0J7O7OiNxI26pgErv1m6ncXvHz/FT7tbF62tZO";
        //通过私钥字符串转对象
        byte[] decodeKey = Base64.decodeBase64(privateKeyStr); //将字符串Base64解码
        PKCS8EncodedKeySpec x509= new PKCS8EncodedKeySpec(decodeKey);//创建x509证书封装类
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");//指定RSA
        PrivateKey privateKey = keyFactory.generatePrivate(x509);//生成私钥

        //数字签名DTO
        DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();//数字签名DTO
        digitalSignatureDTO.setPlainText(canonicalRequest);
        digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
        digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
        //生成数字签名
        String source = digitalSignatureDTO.getPlainText();
        byte[] data = source.getBytes(StandardCharsets.UTF_8);
        DigestAlgEnum digestAlg = digitalSignatureDTO.getDigestAlg();

        Signature signature = Signature.getInstance((String)SIGN_ALG_MAP.get(digestAlg));
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign =  signature.sign();
        String signToBase64 = Base64.encodeBase64URLSafeString(sign);
        StringBuilder cipherText = new StringBuilder();
        cipherText.append(signToBase64);
        cipherText.append("$");
        cipherText.append(digestAlg.getValue());
        String result = cipherText.toString();

        logger.info("====>数字签名：{}", result);


    }

    /**
     * 根据公钥字符串获取公钥，校验签名
     * @throws Exception
     */
    @Test
    public  void testPublicKey() throws Exception{
        //待校验的数字签名
        String expectSign = "eod0p7SysKhv5pkeRuUfjylH5YA1j6WYPMmnsom5On9eiIoVc3sJrz1ju1dr8MviJNAouHy58LpGFAQUC0_mztcc6zuU8XVVBzPxaJ8o06nbBuPitb8T8Ql3ALUQZIVmn8WWEsPuLOCV5LLEQI8l-1aGs_fNh8d53d1WfDtjzeY$SHA256";
        //私钥字符串
        String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ8C1zAH7vr0JYzxBAGhGGlgxYSsA2K9MqWLnUqF5g5EjLhKsOnyxJdGC5EbTLDjQzR59cErScsd+SmSILG7ksTQX61Zdjaq03WdR2akjvNc+w+qABcVvJE93hs7UxM8Yi6Lq0zO3E6Y19JXWgFhuomHgY4h0WFSOfQrRP5Pe5+QIDAQAB";
        //通过公钥字符串获得公钥
        byte[] decodeKey = Base64.decodeBase64(publicKeyStr);//将字符串转byte[]
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);//创建x509证书封装类
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509);

        //数字签名DTO
        DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();//数字签名DTO
        digitalSignatureDTO.setPlainText(canonicalRequest);
        digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
        digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);

        //校验签名是否正确
        String source = digitalSignatureDTO.getPlainText();
        byte[] data = source.getBytes(StandardCharsets.UTF_8);
        DigestAlgEnum digestAlg = digitalSignatureDTO.getDigestAlg();

        Signature signature = Signature.getInstance((String)SIGN_ALG_MAP.get(digestAlg));
        signature.initVerify(publicKey);
        signature.update(data);

        StringBuilder cipherText = new StringBuilder();
        cipherText.append(expectSign);
        cipherText.replace(expectSign.indexOf("$"), expectSign.length(),"");
        logger.info("===》新字符串：{}", cipherText.toString());
        boolean flag = signature.verify(Base64.decodeBase64(cipherText.toString()));

        logger.info("====》数字签名是否正确：{}", flag);
    }


    /**
     * Base64加密解密测试
     * @throws Exception
     */
    @Test
    public void testBase64() throws Exception{
        String data = "gaoweigang";
        String encyptData = Base64.encodeBase64String(data.getBytes());
        String decyptData = new String(Base64.decodeBase64(encyptData));
        logger.info("=====>:{}", encyptData);
        logger.info("======>:{}", decyptData);

    }


}
