package com.gwg.demo.sign.test2;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 公钥 私钥存在形式也可以是字符串
 * 參考：https://www.cnblogs.com/ppldev/p/5110667.html
 */
public class SignTest {
    private static final Logger logger = LoggerFactory.getLogger(SignTest.class);

    static final Map<DigestAlgEnum, String> SIGN_ALG_MAP = new HashMap();

    static {
        SIGN_ALG_MAP.put(DigestAlgEnum.SHA256, "SHA256withRSA");
        SIGN_ALG_MAP.put(DigestAlgEnum.SHA512, "SHA512withRSA");
    }


    @Test
    public void test() throws Exception{
        //私钥字符串
        String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJim4aF+C+UkeVnavDUCIdtYYc7EvLMRCxsB08VabsYxaFpUNqP6ejzV6zWfqVSTnUKGWFCgIj9v7J84OGdM8bymyOA51r+aWqMBq529ruXvqGkyf90/ZYmsravLYnw/RDgxwfMrBcYWhWquvZYXyaVW8qp044frjnyere5Ho5tpAgMBAAECgYA7A4kG3avLkiY3zpAkDgb4vVpSjUxGDbi1YCd2jFd213L4OMZG7naDPlAIEwlrYQXSyg0qXj3fOG3Jv/pONTjelODIWfizmLZDUQDCX418ekyqEFAUx/83cwOr9fkcaUMiTowlvNl0IUKwDX189e5BEVI3QBgoEF4Zt5WM3Et54QJBAMe6YzKz8j3wB23+QCVB0aMoEKZy55ERlfdlgRosmeKTeNB7xg31p7lHJt0a5G0+DjzMhIQkQTRkkRy3fFqd+EMCQQDDqRH58w3aGAkx087rxFa/B/DJixbnHRJST18OR5AMq3UTsIKLaLPMUg5OJORlKFYycl5Ww+HUca3a4xS0wijjAkBsjd2EnVE4YNFo8xuiJFfwucHy9djufARqZnz44OzyvPyZM+y9gZsblJSG8CrfRS8v1Gjh+WKHO3GzjfbEub9JAkEAkJ4Oo2h6/JB3e1TKxIjCdUAxQUaFDx31xNDVuYA7gKHaT0tPsisZehPMbwXlqr89ttvBzu/Jo3TCw97EFzbLcQJBAIE+Wjj/q7LUnmZ0NzGq7eTYPBdXzXMhF5CqhqoHXcQMPUfzqIfKbXQUheQiZk80nz0pHNst5T3NHocuG95aIcI=";
        //通过私钥字符串转对象
        byte[] decodeKey = Base64.decodeBase64(privateKeyStr); //将字符串Base64解码
        PKCS8EncodedKeySpec x509= new PKCS8EncodedKeySpec(decodeKey);//创建x509证书封装类
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");//指定RSA
        PrivateKey privateKey = keyFactory.generatePrivate(x509);//生成私钥

        String canonicalRequest = "gaoweigang";

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
}
