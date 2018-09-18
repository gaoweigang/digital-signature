package com.gwg.demo.test1.pfx;

import com.gwg.utils.Base64Utils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名操作 是发送方用私钥进行签名，接受方用发送方证书来验证签名；
 * 加密操作 则是用接受方的证书进行加密，接受方用自己的私钥进行解密
 */
public class CerTest {

    private static final Logger logger = LoggerFactory.getLogger(CerTest.class);

    /**
     * @author God
     * @cerPath Java读取Cer证书信息
     * @throws Exception
     * @return X509Cer对象
     */
    @Test
    public void getX509CerCate() throws Exception {
        String cerPath = "E:/test/baofu.cer";
        X509Certificate x509Certificate = null;
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        FileInputStream fileInputStream = new FileInputStream(cerPath);
        x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        fileInputStream.close();
        System.out.println("读取Cer证书信息...");
        System.out.println("x509Certificate_SerialNumber_序列号___:"+x509Certificate.getSerialNumber());
        System.out.println("x509Certificate_getIssuerDN_发布方标识名___:"+x509Certificate.getIssuerDN());
        System.out.println("x509Certificate_getSubjectDN_主体标识___:"+x509Certificate.getSubjectDN());
        System.out.println("x509Certificate_getSigAlgOID_证书算法OID字符串___:"+x509Certificate.getSigAlgOID());
        System.out.println("x509Certificate_getNotBefore_证书有效期___:"+x509Certificate.getNotAfter());
        System.out.println("x509Certificate_getSigAlgName_签名算法___:"+x509Certificate.getSigAlgName());
        System.out.println("x509Certificate_getVersion_版本号___:"+x509Certificate.getVersion());
        System.out.println("x509Certificate_getPublicKey_公钥___:"+x509Certificate.getPublicKey());
        PublicKey publicKey = x509Certificate.getPublicKey();

        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        System.out.println("公钥Base64字符串：" + publicKeyStr);
    }

    @Test
    public void testVerifySignature() throws Exception {
        String pubCerPath = "E:/test/baofu.cer";//

        Map<String,String> rDateArry = new TreeMap<String,String>();
        rDateArry.put("signature", "416ad69dd34d9f2351ff5e6c42629da7c64874ae4c349f12f22b9784c3678c456cc77541f71514275f0ca5383455b18e18f4ffa42127419b7ac880836c038a7a742cb40d8a7c3541aa6100d2ce4a0e62a96786da772cfe6a2142e05f180b46430f550a2df098373e1943f1fe5fba8b7e6448909817efb7a62b6d398be9c72d68");
        rDateArry.put("biz_resp_code", "0000");
        rDateArry.put("biz_resp_msg", "交易成功");
        rDateArry.put("member_id", "1222497");
        rDateArry.put("order_id", "2389181095");
        rDateArry.put("resp_code", "S");
        rDateArry.put("succ_amt", "217640");
        rDateArry.put("succ_time", "2018-09-17 18:55:23");
        rDateArry.put("terminal_id", "41474");
        rDateArry.put("trans_id", "201809171855210332193385");



        String expectSignature =rDateArry.get("signature");
        logger.info("[宝付协议代扣]返回的验签值："+expectSignature);
        rDateArry.remove("signature");//需要删除签名字段
        //biz_resp_code=0000&biz_resp_msg=交易成功&member_id=1222497&order_id=2389181095&resp_code=S&succ_amt=217640&succ_time=2018-09-17 18:55:23&terminal_id=41474&trans_id=201809171855210332193385
        String rSignVStr = coverMap2String(rDateArry);
        logger.info("[宝付协议代扣]返回SHA-1摘要字串："+rSignVStr);
        String rSignature = sha1X16(rSignVStr, "UTF-8");//生成摘要
        logger.info("[宝付协议代扣]返回SHA-1摘要结果："+rSignature);

        //获取公钥
        FileInputStream pubKeyStream = new FileInputStream(pubCerPath);
        byte[] reads = new byte[pubKeyStream.available()];
        pubKeyStream.read(reads);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        BufferedReader br = new BufferedReader(new StringReader(new String(reads)));
        String line = null;
        StringBuilder keyBuffer = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("-")) {
                keyBuffer.append(line);
            }
        }
        Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(Base64Utils.decoder(keyBuffer.toString())));
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("公钥Base64字符串：" + Base64.encodeBase64String(publicKey.getEncoded()));

        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(rSignature.getBytes("UTF-8"));
        System.out.println("=======> "+ new String(rSignature.getBytes("UTF-8")));
        boolean flag = signature.verify(hex2Bytes(expectSignature));
        logger.info("校验是否成立flag：{}", flag);

    }

    /**
     * TreeMa集合2String
     *
     * @param data TreeMa
     * @return String
     */
    public static String coverMap2String(Map<String, String> data) {
        StringBuilder sf = new StringBuilder();
        for (String key : data.keySet()) {
            if (!isBlank(data.get(key))) {
                sf.append(key).append("=").append(data.get(key).trim()).append("&");
            }
        }
        return sf.substring(0, sf.length() - 1);
    }

    /**
     * 空值判断
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将16进制字符串转为转换成字符串
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }

    /**
     * sha1计算后进行16进制转换
     *
     * @param data     待计算的数据
     * @param encoding 编码
     * @return 计算结果
     */
    public static String sha1X16(String data, String encoding) throws Exception {
        byte[] bytes = sha1(data.getBytes(encoding));
        return byte2Hex(bytes);
    }

    /**
     * sha1计算.
     *
     * @param data 待计算的数据
     * @return 计算结果
     */
    public static byte[] sha1(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

}
