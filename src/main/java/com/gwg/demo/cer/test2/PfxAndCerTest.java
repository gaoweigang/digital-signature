package com.gwg.demo.cer.test2;

import com.gwg.demo.test1.pfx.CerTest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.bc.BcX509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X509CertImpl;

import java.io.*;
import java.math.BigInteger;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

/**
 * org.bouncycastle.cert.bc.BcX509v3CertificateBuilder;   JCA hepler类允许BC轻量级对象用于构建第三版本证书。
 * org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder  JCA helper类，允许在构建第三版本证书时使用JCA对象。
 */
public class PfxAndCerTest {

    private static final Logger logger = LoggerFactory.getLogger(CerTest.class);

    /**
     * BouncyCastleProvider
     */
    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    @Test
    public void  testGenerateCer() throws Exception{
        String certPath = "E:/test/gaoweigang.cer";//cer证书保存路径
        String pfxPath = "E:/test/gaoweigang.pfx";//pfx证书保存路径

        String data = "gaoweigang";

        // 设置开始日期和结束日期
        long year = 360 * 24 * 60 * 60 * 1000;
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + year);

        // 设置颁发者和主题
        String issuerString = "CN=root,OU=单位,O=组织";
        X500Name issueDn = new X500Name(issuerString);
        X500Name subjectDn = new X500Name(issuerString);

        // 证书序列号
        BigInteger serail = BigInteger.probablePrime(32, new Random());


        /**
         *返回一个KeyPairGenerator对象，该对象为指定的算法生成公钥/私钥对。
         */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //生成一对公钥/私钥
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        PublicKey publicKey = keyPair.getPublic();
        logger.info("====>公钥算法：{}", publicKey.getAlgorithm());
        logger.info("====>公钥转字符串：{}", Base64.encodeBase64String(publicKey.getEncoded()));
        //私钥
        PrivateKey privateKey = keyPair.getPrivate();
        logger.info("====>私钥转字符串：{}", Base64.encodeBase64String(privateKey.getEncoded()));

        //与证书相关的公钥信息
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
                    .getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject());


        //证书的签名数据
        final byte[] signatureData ;
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());//
            signatureData = signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }

        //组装证书
        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                issueDn, //证书发行机构
                serail,  //证书序列号
                notBefore, //证书生效开始日期
                notAfter,  //证书生效结束日期
                subjectDn, //证书主题
                subjectPublicKeyInfo); //与此证书相关联的公钥的信息结构。

        //用于生成验证证书的签名的内容签名者。
        ContentSigner signer = new ContentSigner() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            @Override
            public byte[] getSignature() {
                try {
                    buf.write(signatureData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return signatureData;
            }

            @Override
            public OutputStream getOutputStream() {
                return buf;
            }

            @Override
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
            }
        };
        //给证书签名
        X509CertificateHolder holder = builder.build(signer);
        byte[] certBuf = holder.getEncoded();
        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(certBuf));
        logger.info("====>从证书中获取公钥转字符串：{}", Base64.encodeBase64String(certificate.getPublicKey().getEncoded()));
        FileOutputStream fos = new FileOutputStream(certPath);//将公钥输出到文件cer
        fos.write(certBuf);
        fos.close();


        //证书签名验证
        PublicKey useOfpublicKey = publicKey;//用户拥有公钥
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(useOfpublicKey);
        signature.update(data.getBytes());
        boolean flag = signature.verify(certificate.getSignature());
        logger.info("=========>{}", flag);


        //将公钥私钥保存为pfx,密码为123456
        String pfxPassword = "123456";
        Certificate[] certChain = {certificate};
        savePfx("test", privateKey, pfxPassword, certChain, pfxPath);


        //keyStore
        File pfxFile = new File(pfxPath);
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new ByteArrayInputStream(FileUtils.readFileToByteArray(pfxFile)), pfxPassword.toCharArray());

        //从pfx文件中获取私钥与公钥
        PrivateKey privateKeyOfpfx = null;//私钥
        PublicKey publicKeyOfpfx = null;//公钥
        X509Certificate x509Certificate = null;
        Enumeration<String> enums = keystore.aliases();
        String keyAlias = "";
        while (enums.hasMoreElements()) {
            keyAlias = enums.nextElement();
            logger.info("====>keyAlias:{}", keyAlias);
            if (keystore.isKeyEntry(keyAlias)) {
                privateKeyOfpfx = (PrivateKey) keystore.getKey(keyAlias, pfxPassword.toCharArray());
                x509Certificate = (X509Certificate) keystore.getCertificate(keyAlias);

            }
        }
        byte[] privateKeyByte = privateKeyOfpfx.getEncoded();
        logger.info("从pfx获取的私钥Base64字符串：{}", Base64.encodeBase64String(privateKeyByte));
        publicKeyOfpfx = x509Certificate.getPublicKey();
        logger.info("从pfx获取的公钥Base64字符串：{}", Base64.encodeBase64String(publicKeyOfpfx.getEncoded()));

    }

    /**
     * 保存pfx文件，里面包括公钥，私钥，证书链别名
     * @param alias
     * @param privKey
     * @param pwd
     * @param certChain
     * @param filepath
     * @throws Exception
     */
    public void savePfx(String alias, PrivateKey privKey, String pwd,
                        Certificate[] certChain, String filepath) throws Exception {
        FileOutputStream out = null;
        try {
            KeyStore outputKeyStore = KeyStore.getInstance("pkcs12");
            outputKeyStore.load(null, pwd.toCharArray());
            outputKeyStore.setKeyEntry(alias, privKey, pwd.toCharArray(), certChain);
            out = new FileOutputStream(filepath);
            outputKeyStore.store(out, pwd.toCharArray());
        } finally {
            if (out != null)
                out.close();
        }
    }
}
