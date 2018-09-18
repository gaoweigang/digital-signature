package com.gwg.demo.test1.pfx;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.util.Enumeration;

public class PfxTest {

    private static final Logger logger = LoggerFactory.getLogger(PfxTest.class);


    /** 编码 */
    public final static String ENCODE = "UTF-8";

    public final static String KEY_X509 = "X509";
    public final static String KEY_PKCS12 = "PKCS12";
    public final static String KEY_ALGORITHM = "RSA";
    public final static String CER_ALGORITHM = "MD5WithRSA";

    public final static String RSA_CHIPER = "RSA/ECB/PKCS1Padding";

    public final static int KEY_SIZE = 1024;
    /** 1024bit 加密块 大小 */
    public final static int ENCRYPT_KEYSIZE = 117;
    /** 1024bit 解密块 大小 */
    public final static int DECRYPT_KEYSIZE = 128;


    //利用bouncycastle 生成pfx公私钥文件
    @Test
    public void testPfx(){

        String pfxPath = "E:/test/009.pfx";
        String priKeyPass = "1";
        String src = "gaoweigang";

        //获取私钥
        PrivateKey privateKey = getPrivateKeyFromFile(pfxPath, priKeyPass);
        logger.info("私钥："+privateKey);
        if (privateKey == null) {
            return;
        }
        logger.info("加密之前：{}",src);
        String encryptSrc = encryptByPrivateKey(src, privateKey);//使用私钥对源数据进行加密
        logger.info("加密之后：{}", encryptSrc);

        //解密

    }

    //从PFX文件中获取私钥
    @Test
    public void testEncrypt() throws Exception{
        String pfxPath = "E:/test/009.pfx";
        String priKeyPass = "1";
        String src = "gaoweigang";//待加密数据
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
        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, charPriKeyPass);
        logger.info("私钥：{}", privateKey);

    }

    /**
     * 根据私钥路径读取私钥
     *
     * @param pfxPath
     * @param priKeyPass
     * @return
     */
    public PrivateKey getPrivateKeyFromFile(String pfxPath, String priKeyPass) {
        InputStream priKeyStream = null;
            try {
            priKeyStream = new FileInputStream(pfxPath);
            byte[] reads = new byte[priKeyStream.available()];
            priKeyStream.read(reads);
            return getPrivateKeyByStream(reads, priKeyPass);
        } catch (Exception e) {
            logger.error("解析文件，读取私钥失败:", e);
        } finally {
            if (priKeyStream != null) {
                try {
                    priKeyStream.close();
                } catch (Exception e) {
                    logger.error("解析文件，读取私钥失败:", e);
                }
            }
        }
        return null;
    }


    /**
     * 根据PFX私钥字节流读取私钥
     *
     * @param pfxBytes
     * @param priKeyPass
     * @return
     */
    public PrivateKey getPrivateKeyByStream(byte[] pfxBytes, String priKeyPass) {
        try {
            KeyStore ks = KeyStore.getInstance(KEY_PKCS12);
            char[] charPriKeyPass = priKeyPass.toCharArray();
            ks.load(new ByteArrayInputStream(pfxBytes), charPriKeyPass);
            Enumeration<String> aliasEnum = ks.aliases();
            String keyAlias = null;
            if (aliasEnum.hasMoreElements()) {
                keyAlias = (String) aliasEnum.nextElement();
            }
            return (PrivateKey) ks.getKey(keyAlias, charPriKeyPass);
        } catch (IOException e) {
            // 加密失败
            logger.error("解析文件，读取私钥失败:", e);
        } catch (KeyStoreException e) {
            logger.error("私钥存储异常:", e);

        } catch (NoSuchAlgorithmException e) {
            logger.error("不存在的解密算法:", e);
        } catch (Exception e) {
            logger.error("证书异常:", e);
        }
        return null;
    }


    /**
     * 根据私钥加密
     *
     * @param src
     * @param privateKey
     */
    public static String encryptByPrivateKey(String src, PrivateKey privateKey) {

        byte[] destBytes = rsaByPrivateKey(src.getBytes(), privateKey, Cipher.ENCRYPT_MODE);

        if (destBytes == null) {
            return null;
        }
        return byte2Hex(destBytes);

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
     * 私钥算法
     *
     * @param srcData
     *            源字节
     * @param privateKey
     *            私钥
     * @param mode
     *            加密 OR 解密
     * @return
     */
    public static byte[] rsaByPrivateKey(byte[] srcData, PrivateKey privateKey, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_CHIPER);
            cipher.init(mode, privateKey);
            // 分段加密
            int blockSize = (mode == Cipher.ENCRYPT_MODE) ? ENCRYPT_KEYSIZE : DECRYPT_KEYSIZE;
            byte[] decryptData = null;

            for (int i = 0; i < srcData.length; i += blockSize) {
                byte[] doFinal = cipher.doFinal(subarray(srcData, i, i + blockSize));

                decryptData = addAll(decryptData, doFinal);
            }
            return decryptData;
        } catch (NoSuchAlgorithmException e) {
//			//log.error("私钥算法-不存在的解密算法:", e);
        } catch (NoSuchPaddingException e) {
            //log.error("私钥算法-无效的补位算法:", e);
        } catch (IllegalBlockSizeException e) {
            //log.error("私钥算法-无效的块大小:", e);
        } catch (BadPaddingException e) {
            //log.error("私钥算法-补位算法异常:", e);
        } catch (InvalidKeyException e) {
            //log.error("私钥算法-无效的私钥:", e);
        }
        return null;
    }

    // /////////////==========================
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

}
