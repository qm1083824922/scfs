package com.scfs.common.consts;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author Administrator
 *
 */
public class UrlToPermission {

    @SuppressWarnings("rawtypes")
	public static void main(String[] args) {
        try {
            AuditUrlConsts vo = new AuditUrlConsts();
            Class clazz = AuditUrlConsts.class;
            Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
            for (Field f : fields) {
                System.out.println(f.getName() + "-" + getFieldValueByName(f.getName(), vo));// 打印每个属性的类型名字
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter;
            char[] a = fieldName.toCharArray();
            for (int i = 1; i < a.length; i++) {
                String aa = String.valueOf(a[i]);
                if (aa.equals("_")) {
                    i++;
                    String aa2 = String.valueOf(a[i]);
                    getter = getter + aa2.toUpperCase();
                } else {
                    getter = getter + aa.toLowerCase();
                }
            }

            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
