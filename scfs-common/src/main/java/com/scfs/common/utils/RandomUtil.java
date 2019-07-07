package com.scfs.common.utils;

import java.util.Random;

/**
 * 随机类
 * @author Administrator
 *
 */
public class RandomUtil {
    
    public static int DEFALT_MIN_INT = 100000;
    public static int DEFALT_MAX_INT = 999999;
    
    public static String getFixedLenRandom(int len) {
        String str = "";
        Random random = new Random();
        long s = random.nextInt(DEFALT_MAX_INT) % (DEFALT_MAX_INT - DEFALT_MIN_INT + 1) + DEFALT_MIN_INT;
        str = toFixdLengthString(s , len) ;
        System.out.println(str);
        return str;
    }
    
    /** 
     * 根据数字生成一个定长的字符串，长度不够前面补0 
     *  
     * @param num 
     *            数字 
     * @param fixdlenth 
     *            字符串长度 
     * @return 定长的字符串 
     */  
    public static String toFixdLengthString(long num, int fixdlenth) {  
        StringBuffer sb = new StringBuffer();  
        String strNum = String.valueOf(num);  
        if (fixdlenth - strNum.length() >= 0) {  
            sb.append(generateZeroString(fixdlenth - strNum.length()));  
        } else {  
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth  
                    + "的字符串发生异常！");  
        }  
        sb.append(strNum);  
        return sb.toString();  
    }  
    
    /** 
     * 生成一个定长的纯0字符串 
     *  
     * @param length 
     *            字符串长度 
     * @return 纯0字符串 
     */  
    public static String generateZeroString(int length) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            sb.append('0');  
        }  
        return sb.toString();  
    }
    
}

