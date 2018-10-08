package com.gwg.demo.md5.test1;

import com.sun.javafx.binding.StringFormatter;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 批量生成sign
 * 技巧：负数在计算机中是用补码表示的，如果获得一个负数的补码呢？负1~负127的数加256获取该负数的补码
 * -1：+256 -> 255 -> 0000 0000 1111 1111 -> 而负1的补码是 1111 1111
 * -127 ： +256 -> 129 ->  0000 0000 1000 0001 而负-127的补码是 1000 0001
 */
public class MD5Test {
    String str = "";




    @Test
    public void testMD5(){
        String[] strArr = str.split(",");
        System.out.println("数组大小"+strArr.length);
        for(int i = 0; i < strArr.length; i++){
            //System.out.println("源字符串："+strArr[i]);
            System.out.println(MD5(strArr[i]));
        }

    }

    @Test
    public void testA(){
        //转16进制
        byte b = -127;
        int a = (int )b;
        System.out.println(a);
        System.out.println(Integer.toBinaryString(-1&0xff));

    }




    /**
     * MD5 16进制
     * @param sourceStr
     * @return
     */
    private static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();//MD5生成签名，MD5生成签名有16位
            int i;
            StringBuffer buf = new StringBuffer("");
            /**
             * 255 = 0xff ->   1111 1111
             * 256  -> 1 0000 0000
             */
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0){
                    /**
                     * 负数在计算机中是用补码表示的，如果获得一个负数的补码呢？负数加256获取该负数的补码
                     * 十进制    二级制(省略前面16个0)     转byte
                     * 256  = 0000 0001 0000 0000
                     * 255  = 0000 0000 1111 1111      1111 1111  = -1在计算机中负数使用补码表示,十六进制表示ff
                     */
                    i += 256; //加256之后 就直接算出来byte负数的补码了，将补码存储在int中，可以理解为用int表示了byte补码,等价于i&0xff
                }
                //16进制用0~f表示0~15
                if (i < 16) {//小于16 恰好 0~f,前面补0
                    buf.append("0");
                }
                //否者低4位与高4位 在Integer.toHexString中处理
                buf.append(Integer.toHexString(i));//Integer.toHexString()转16进制(字符串表示，而不是整型表示)
            }
            result = buf.toString();
            //System.out.println("MD5(" + sourceStr + ",32) = " + result); 32位加密
            //System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));//16位加密
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

}
