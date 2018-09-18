package com.gwg.demo.sign.test1;

import com.gwg.utils.Base64Utils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Enumeration;

/**
 * 签名test
 * 参考：https://blog.csdn.net/xiaxiaorui2003/article/details/3646917java.security
 * java.security.Signature类api: https://www.cnblogs.com/yelongsan/p/6343985.html
 */
public class SignTest {

    private static final Logger logger = LoggerFactory.getLogger(SignTest.class);

    public String msgTest = "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"yes\"?>       "+
            "<AIPG>                                                      "+
            "    <INFO>                                                  "+
            "        <DATA_TYPE>2</DATA_TYPE>                            "+
            "        <LEVEL>9</LEVEL>                                    "+
            "        <REQ_SN>1537254562340</REQ_SN>                      "+
            "        <TRX_CODE>200000</TRX_CODE>                         "+
            "        <USER_NAME>123455</USER_NAME>                       "+
            "        <USER_PASS>111111</USER_PASS>                       "+
            "        <VERSION>33</VERSION>                               "+
            "    </INFO>                                                 "+
            "    <VALIDR>                                                "+
            "        <ACCOUNT_NAME></ACCOUNT_NAME>                       "+
            "        <ACCOUNT_NO>222222222222222222222222</ACCOUNT_NO>   "+
            "        <ACCOUNT_PROP>00</ACCOUNT_PROP>                     "+
            "        <ACCOUNT_TYPE>0</ACCOUNT_TYPE>                      "+
            "        <BANK_CODE>CNCS</BANK_CODE>                         "+
            "        <ID>22244</ID>                                      "+
            "        <ID_TYPE>1</ID_TYPE>                                "+
            "        <MERCHANT_ID>30000</MERCHANT_ID>                    "+
            "        <SUBMIT_TIME>20180918150922</SUBMIT_TIME>           "+
            "        <TEL>13817191469</TEL>                              "+
            "    </VALIDR>                                               "+
            "</AIPG>                                                     ";


    public String obj2xml = ""; //数据

    /**
     * 生成请求报文，请求发的报文里面多了签名
     * @throws Exception
     */
    @Before
    public void testBefore() throws Exception{
        UserVerifyReq req = new UserVerifyReq();
        Info info = new Info();
        //info.setREQ_SN(String.valueOf(System.currentTimeMillis()));//注释掉，否者每个生成的请求消息不一样，导致请求端与接收端的消息体不一样
        info.setTRX_CODE("200000");
        info.setDATA_TYPE("2");
        info.setLEVEL("9");
        info.setUSER_NAME("123455");
        info.setUSER_PASS("111111");
        info.setVERSION("33");
        ValidReq valid = new ValidReq();
        valid.setACCOUNT_NAME("");
        valid.setACCOUNT_NO("222222222222222222222222");
        valid.setACCOUNT_PROP("00");//卡类型，默认银行卡
        valid.setACCOUNT_TYPE("0");//私有
        valid.setTEL("13817191469");
        valid.setBANK_CODE("CNCS");
        valid.setID_TYPE("1");
        valid.setID("22244");
        //提交时间,注释掉，否者每个生成的请求消息不一样，导致测试不准确
        //valid.setSUBMIT_TIME(DateUtil.dateFormat(Calendar.getInstance().getTime(), DateUtil.TIME_FORMAT_1));
        valid.setMERCHANT_ID("30000");//商户号
        req.setInfo(info);
        req.setVALIDR(valid);
        //对象转xml
        JAXBContext context = JAXBContext.newInstance(req.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
        StringWriter writer = new StringWriter();
        marshaller.marshal(req, writer);
        obj2xml =  writer.toString();
        logger.info("===>对象转xml:{}", obj2xml);
    }

    //请求方
    @Test
    public void testSign() throws Exception{
        String pathPfx = "E:/test/baofoo_fengjr_private.pfx";//pfx证书
        String password = "682519";//pfx证书密码
        String signedMsg = "";//请求的签名

        //生成签名
        String IDD_STR = "<SIGNED_MSG></SIGNED_MSG>";
        String strMsg = obj2xml.replaceAll(IDD_STR, "");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream fiKeyFile = new FileInputStream(pathPfx);
        ks.load(fiKeyFile,password.toCharArray());

        Enumeration myEnum = ks.aliases();
        String keyAlias = null;
        RSAPrivateCrtKey prikey = null;
        // keyAlias = (String) myEnum.nextElement();
		/* IBM JDK必须使用While循环取最后一个别名，才能得到个人私钥别名 */
        while (myEnum.hasMoreElements())
        {
            keyAlias = (String) myEnum.nextElement();
            // System.out.println("keyAlias==" + keyAlias);
            if (ks.isKeyEntry(keyAlias))
            {
                prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias, password.toCharArray());
                break;
            }
        }
        if (prikey == null)
        {
            throw new Exception("没有找到匹配私钥");
        }
        else
        {
            Signature sign =Signature.getInstance("SHA1withRSA");
            sign.initSign(prikey);
            logger.info("1111=========>{}",strMsg);
            sign.update(strMsg.getBytes("GBK"));
            byte signed[] = sign.sign();
            byte sign_asc[] = new byte[signed.length * 2];
            Hex2Ascii(signed.length, signed, sign_asc);
            signedMsg =  new String(sign_asc);
            System.out.println("====>"+signedMsg);
        }
        String strRnt = obj2xml.replaceAll(IDD_STR, "<SIGNED_MSG>" + signedMsg + "</SIGNED_MSG>");

        logger.info("====》带签名的消息：{}", strRnt);
    }

    /**
     * 接受带签名的消息=====》验证签名是否正确
     * 接收方接受到请求方的数据，
     * 在这里假设已经将签名请求方的数据中提取出来的了
     */
    @Test
    public void testVerifySign() throws Exception{
        String pubCerPath = "E:/test/baofu_credential.cer";//cer证书，只有公钥

        //String expectSignature = signedMsg;//期望的签名，在这里假设签名已经从请求方的数据中提取出来的了
        //String strMsg  = obj2xml; //请求方的数据，已经删除了签名部分
        String expectSignature = "c44ebea04ed273774e0bcc2f78618283a8dc0b0b66a4235972bb59de6de56cdc10c9ddb91def4e04c8b17db02c8f509af0760c07743ec85f8bd1738dfde84937426ec2af1a6ba9580be70b35cd283f8b7e599e1d3c4c83b91551b3a255e599196e622e73695d758705a9563e6eb87f60e0ac6bc98692b78e90bc646195af3bb5";//期望的签名，在这里假设签名已经从请求方的数据中提取出来的了
        String IDD_STR = "<SIGNED_MSG></SIGNED_MSG>";
        String strMsg = obj2xml.replaceAll(IDD_STR, "");//请求方的数据(不带签名)，这里假设已经将签名从请求数据抽离出来了
        logger.info("[请求方]返回的验签值：{}，有效数据：{}", expectSignature, strMsg);

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
        PublicKey publicKey = certificate.getPublicKey();//从证书中获取公钥
        System.out.println("公钥Base64字符串：" + Base64.encodeBase64String(publicKey.getEncoded()));

        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        logger.info("222222=========>{}",strMsg);
        signature.update(strMsg.getBytes("GBK"));
        //ASCii转16进制
        byte signed[] = expectSignature.getBytes();
        byte sign_hex[] = new byte[signed.length / 2];
        Ascii2Hex(signed.length, signed, sign_hex);
        //System.out.println(new String(sign_hex));
        boolean flag = signature.verify(sign_hex);
        logger.info("校验签名是否正确flag：{}", flag);

    }



    /**
     * 将十六进制数据转换成ASCII字符串
     *
     * @param len
     *            十六进制数据长度
     * @param data_in
     *            待转换的十六进制数据
     * @param data_out
     *            已转换的ASCII字符串
     */
    private static void Hex2Ascii(int len, byte data_in[], byte data_out[])
    {
        byte temp1[] = new byte[1];
        byte temp2[] = new byte[1];
        for (int i = 0, j = 0; i < len; i++)
        {
            temp1[0] = data_in[i];
            temp1[0] = (byte) (temp1[0] >>> 4);
            temp1[0] = (byte) (temp1[0] & 0x0f);
            temp2[0] = data_in[i];
            temp2[0] = (byte) (temp2[0] & 0x0f);
            if (temp1[0] >= 0x00 && temp1[0] <= 0x09)
            {
                (data_out[j]) = (byte) (temp1[0] + '0');
            }
            else if (temp1[0] >= 0x0a && temp1[0] <= 0x0f)
            {
                (data_out[j]) = (byte) (temp1[0] + 0x57);
            }

            if (temp2[0] >= 0x00 && temp2[0] <= 0x09)
            {
                (data_out[j + 1]) = (byte) (temp2[0] + '0');
            }
            else if (temp2[0] >= 0x0a && temp2[0] <= 0x0f)
            {
                (data_out[j + 1]) = (byte) (temp2[0] + 0x57);
            }
            j += 2;
        }
    }


    /**
     * 将ASCII字符串转换成十六进制数据
     *
     * @param len
     *            ASCII字符串长度
     * @param data_in
     *            待转换的ASCII字符串
     * @param data_out
     *            已转换的十六进制数据
     */
    private static void Ascii2Hex(int len, byte data_in[], byte data_out[])
    {
        byte temp1[] = new byte[1];
        byte temp2[] = new byte[1];
        for (int i = 0, j = 0; i < len; j++)
        {
            temp1[0] = data_in[i];
            temp2[0] = data_in[i + 1];
            if (temp1[0] >= '0' && temp1[0] <= '9')
            {
                temp1[0] -= '0';
                temp1[0] = (byte) (temp1[0] << 4);

                temp1[0] = (byte) (temp1[0] & 0xf0);

            }
            else if (temp1[0] >= 'a' && temp1[0] <= 'f')
            {
                temp1[0] -= 0x57;
                temp1[0] = (byte) (temp1[0] << 4);
                temp1[0] = (byte) (temp1[0] & 0xf0);
            }

            if (temp2[0] >= '0' && temp2[0] <= '9')
            {
                temp2[0] -= '0';

                temp2[0] = (byte) (temp2[0] & 0x0f);

            }
            else if (temp2[0] >= 'a' && temp2[0] <= 'f')
            {
                temp2[0] -= 0x57;

                temp2[0] = (byte) (temp2[0] & 0x0f);
            }
            data_out[j] = (byte) (temp1[0] | temp2[0]);

            i += 2;
        }

    }


}
