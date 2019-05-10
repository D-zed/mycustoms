package com.jinaup.upcustoms.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	/**利用MD5进行加密*/
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
//        //确定计算方法
//        MessageDigest md5=MessageDigest.getInstance("MD5");
//        BASE64Encoder base64en = new BASE64Encoder();
//        //加密后的字符串
//        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
    
    /**判断用户密码是否正确
     *newpasswd  用户输入的密码
     *oldpasswd  正确密码*/
    public static boolean checkPassword(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(EncoderByMd5(newpasswd).equals(oldpasswd))
            return true;
        else
            return false;
    }
}
