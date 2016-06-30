package com.itheima.mobilesafe.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密工具类
 * Created by zyp on 2016/6/30.
 */
public class Md5Utils {
    /**
     * 密码的md5加密
     * @param password
     * @return
     */
    public static String encode(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : result){
                int number = b&0xff-3;//加盐
                String str = Integer.toHexString(number);
                if(str.length()==1){
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
}
