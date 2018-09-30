package com.gwg.demo.md5.test1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对MD5生成的签名加密解密
 */
public class HexStringUtils {

    private static final Logger logger = LoggerFactory.getLogger(HexStringUtils.class);

    private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    /**
     * 对MD5签名解密
     * 16进制解密
     */
    public static byte[] decrypt(String key) {
        char[] chars = key.toLowerCase().toCharArray();
        int j = chars.length / 2;
        byte[] ret = new byte[j];
        int k = 0;
        for (int i = 0; i < j; i++) {
            /**
             * 如果是阿拉伯数字，则减去48；如果是字母，则减去87
             * 阿拉伯数字0~9的unicode编码对应的code值是48~57
             * 大写字母A~Z的unicode编码对应的code值是65~90
             * 小写字母a~z的unicode编码对应的code值是97~122
             *
             * chars[k]小于65，则说明是阿拉伯数字
             *
             * char与int运算时是使用unicode编码的code值与int进行运算的
             */
            byte b1 = chars[k] < 65 ? (byte) (chars[k] - 48) : (byte) (chars[k] - 87);
            k++;
            byte b2 = chars[k] < 65 ? (byte) (chars[k] - 48) : (byte) (chars[k] - 87);
            k++;
            ret[i] = (byte) ((b1 << 4) | b2);//高4位与低4位 或运算
        }
        return ret;
    }

    /**
     * 对MD5签名进行加密，MD5生成的签名长度为16位
     * >>> : 无符号右移，忽略符号位，空位都以0补齐
     * & : 两个数都转为二进制，然后从高位开始比较，如果两个数都为1则为1，否则为0。
     *
     * 十进制 ：满10进一
     * 十六进制：满16进一
     *
     * 16进制加密
     */
    public static String encrypt(byte[] key) {
        int j = key.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = key[i];
            //将字节数组转成 16 进制的字符串来表示，每个字节采用两个字符表表示
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; //高4位运算 0xf 的二进制 00001111
            str[k++] = hexDigits[byte0 & 0xf];//低4位运算
        }
        logger.info("对MD5签名加密之后的长度：{}", str.length);
        return new String(str);
    }


    @Test
    public void testA() throws Exception{
        String businessNo = "10018";
        String businessName = "理财非存管借款人开通协议代扣鉴权";
        String productType = "5";
        String merchant_bank_acct_no = "";
        String str = businessNo + businessName + productType + merchant_bank_acct_no;
        //1.MD5生成签名
        byte[] digest = MD5Utils.encrypt(str.getBytes());
        System.out.println(digest);
        //2.MD5签名加密
        String encryptStr = HexStringUtils.encrypt(digest);
        System.out.println(encryptStr);
        //3.MD5签名解密
        byte[] decryptByte = HexStringUtils.decrypt(encryptStr);
        System.out.println(decryptByte);


    }


}
