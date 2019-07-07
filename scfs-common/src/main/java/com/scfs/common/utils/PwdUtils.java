package com.scfs.common.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 
 * @author Administrator
 *
 */
public class PwdUtils {

    public final static String SALT_PWD = "c589b4a21e218c6ff4cc0c6e729b42ee";

    private static int hashIterations =2;

    /**
     * 加密，
     * @param password
     * @return
     */
    public static String encryptPassword(String password){
        return new Md5Hash(password, SALT_PWD, hashIterations).toHex();
    }


}
