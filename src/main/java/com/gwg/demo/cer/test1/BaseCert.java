package com.gwg.demo.cer.test1;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @Comments : 证书类
 * @Version : 1.0.0
 */

public class BaseCert {

    private static final Logger logger = LoggerFactory.getLogger(BaseCert.class);
    /**
     * BouncyCastleProvider
     */
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    /**
     *
     */
    protected static KeyPairGenerator kpg = null;

    /**
     *
     */
    public BaseCert() {
        try {
            // 采用 RSA 非对称算法加密
            kpg = KeyPairGenerator.getInstance("RSA");
            // 初始化为 1023 位
            kpg.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成 X509 证书
     * @param user
     * @return
     */
    public X509Certificate generateCert(String user) {
        X509Certificate cert = null;
        try {
            KeyPair keyPair = this.kpg.generateKeyPair();
            // 公钥
            PublicKey pubKey = keyPair.getPublic();
            logger.info("======>公钥Base64字符串:{}", Base64.encodeBase64String(pubKey.getEncoded()));

            // 私钥
            PrivateKey priKey = keyPair.getPrivate();
            logger.info("=====>私钥Base64字符串:{}", Base64.encodeBase64String(priKey.getEncoded()));
            logger.info("==========================================================================");
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            // 设置序列号
            certGen.setSerialNumber(this.getNextSerialNumber());//序列号，同一身份验证机构签发的证书序列号是唯一的，在这里我们使用时间戳
            // 设置颁发者
            certGen.setIssuerDN(new X500Principal(CAConfig.CA_ROOT_ISSUER));
            // 设置有效期
            Date startDate = new Date();
            certGen.setNotBefore(startDate);//有效开始时间
            certGen.setNotAfter(this.addTime(startDate, 1));//有效结束时间
            // 设置使用者
            certGen.setSubjectDN(new X500Principal(CAConfig.CA_DEFAULT_SUBJECT + user));
            // 公钥
            certGen.setPublicKey(pubKey);
            // 签名算法
            certGen.setSignatureAlgorithm(CAConfig.CA_SHA);
            cert = certGen.generateX509Certificate(priKey, "BC");
        } catch (Exception e) {
            System.out.println(e.getClass() + e.getMessage());
        }
        return cert;
    }

    //序列号，同一身份验证机构签发的证书序列号是唯一的，在这里我们使用时间戳
    private BigInteger getNextSerialNumber(){
        return new BigInteger(String.valueOf(System.currentTimeMillis()));
    }

    private Date addTime(Date date, int add) {
        if (null == date) {
            return null;
        }
        Calendar begin = Calendar.getInstance();
        begin.setTime(date);
        //begin.add(Calendar.YEAR, add);
        begin.add(Calendar.HOUR, add);//测试，1个小时之后无效
        return begin.getTime();
    }

    //测试
    public static void main(String[] args) {
        BaseCert baseCert = new BaseCert();
        System.out.println(baseCert.getNextSerialNumber());
        Date startDate = new Date();
        System.out.println("有效开始时间："+startDate+", 有效结束时间："+baseCert.addTime(startDate, 1));

    }
}