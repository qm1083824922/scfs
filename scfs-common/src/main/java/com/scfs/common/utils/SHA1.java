package com.scfs.common.utils;

import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 
 * @author Administrator
 *
 */
public class SHA1 {

    /**
     * 用SHA1算法生成安全签名
     * @param token 票据
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param encrypt 密文
     * @return 安全签名
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt){
        String[] array = new String[] { token, timestamp, nonce, encrypt };
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < 4; i++) {
            sb.append(array[i]);
        }
        return getSHA1Encrpt(sb.toString());
    }

    public static String getSHA1Encrpt(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException(ExcMsgEnum.ERROR_GENERAL,"消息加解密异常");
        }
    }

}
