package com.gwg.demo.encryptanddecrypt.test2;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * 实现数据安全性的的两种方式，一种方式是签名，另一种方式是加密
 **************************************************************
 * 加密又有两种场景：
 * 第一种 私钥加密，公钥解密
 * 第二种 公钥加密，私钥解密
 * ************************************************************
 *
 * 测试 第二种 公钥加密，私钥解密
 */
public class EncryptAndDecryptTest {

    private static final Logger logger = LoggerFactory.getLogger(EncryptAndDecryptTest.class);

    /**
     * 公钥加密
     * @return
     */
    @Test
    public void testEncypt() throws Exception{
        String pubCerPath = "E:/test/baofu_credential.cer";

        //需要加密的数据
        TransReq req = new TransReq();
        req.setName("gaoweigang");
        req.setAge(22);
        String reqJson = JSON.toJSONString(req);
        logger.info("代扣 加密前data_content[{}]",reqJson);
        //原始数据
        byte[] srcData = reqJson.getBytes();
        //获取公钥
        X509Certificate x509Certificate = null;
        CertificateFactory certificateFactoryX509 = CertificateFactory.getInstance("X.509");
        FileInputStream fileInputStream = new FileInputStream(pubCerPath);
        x509Certificate = (X509Certificate) certificateFactoryX509.generateCertificate(fileInputStream);
        fileInputStream.close();
        PublicKey publicKey = x509Certificate.getPublicKey();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        System.out.println("公钥Base64字符串：" + publicKeyStr);
        //使用公钥解密
        int mode = Cipher.ENCRYPT_MODE;
        Cipher cipher = Cipher.getInstance(RsaConst.RSA_CHIPER);
        cipher.init(mode, publicKey);
        // 分段加密 1024bit 解密块 大小
        int blockSize = (mode == Cipher.ENCRYPT_MODE) ? RsaConst.ENCRYPT_KEYSIZE : RsaConst.DECRYPT_KEYSIZE;
        byte[] encryptedData = null;
        for (int i = 0; i < srcData.length; i += blockSize) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(subarray(srcData, i, i + blockSize));
            encryptedData = addAll(encryptedData, doFinal);
        }
        String result = byte2Hex(encryptedData);
        logger.info("=====》公钥加密之后的数据：{}", result);

    }

    /**
     * 私钥解密
     * @throws Exception
     */
    @Test
    public void testDecrypt() throws Exception{
        String pfxPath = "E:/test/baofoo_fengjr_private.pfx";//pfx证书路径
        String priKeyPass = "682519";//pfx证书密码

        //原始加密数据
        String encryptData = "a1e92e86eaf6ed423b9867dd87a49c6a52cfc7628f55abac21de65702567f64dbeeef33a1ad42b1edf0a43f53d509d5e8eb4f0a9fef16507432e71e25ef61fcb465ff0a493dadd1afe743d81c8684874fb23ae4228889269fc50e66a2bc602e1cf8850f691d4a17cc75317e535673f1a511dd9c62b702c488c36fb8b652491cf";
        //16进制转byte[]数组
        byte[] encBytes = hex2Bytes(encryptData);

        //用私钥对数据进行解密
        //获取私钥
        InputStream priKeyStream = new FileInputStream(pfxPath);
        byte[] reads = new byte[priKeyStream.available()];
        priKeyStream.read(reads);
        KeyStore ks = KeyStore.getInstance("PKCS12");
        char[] charPriKeyPass = priKeyPass.toCharArray();
        ks.load(new ByteArrayInputStream(reads), charPriKeyPass);
        Enumeration<String> aliasEnum = ks.aliases();
        String keyAlias = null;
        if (aliasEnum.hasMoreElements()) {
            keyAlias = (String) aliasEnum.nextElement();
        }
        PrivateKey privateKey =  (PrivateKey) ks.getKey(keyAlias, charPriKeyPass);
        //用私钥进行数据加密
        int mode = Cipher.DECRYPT_MODE; //解密
        Cipher cipher = Cipher.getInstance(RsaConst.RSA_CHIPER);
        cipher.init(mode, privateKey);
        // 分段加密 1024bit 加密块 大小
        int blockSize = (mode == Cipher.ENCRYPT_MODE) ? RsaConst.ENCRYPT_KEYSIZE : RsaConst.DECRYPT_KEYSIZE;
        byte[] decryptData = null;

        for (int i = 0; i < encBytes.length; i += blockSize) {
            byte[] doFinal = cipher.doFinal(subarray(encBytes, i, i + blockSize));
            decryptData = addAll(decryptData, doFinal);
        }
        logger.info("====》使用私钥进行解密之后的数据[{}]", new String(decryptData, "UTF-8"));
    }



    public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;

        if (newSize <= 0) {
            return new byte[0];
        }

        byte[] subarray = new byte[newSize];

        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);

        return subarray;
    }

    public static byte[] addAll(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static byte[] clone(byte[] array) {
        if (array == null) {
            return null;
        }
        return (byte[]) array.clone();
    }

    /**
     * 将byte[] 转换成字符串
     */
    public static String byte2Hex(byte[] srcBytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * 将16进制字符串转为转换成byte[]
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }
}
