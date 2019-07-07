package com.scfs.dao.interceptor;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public final class ReflectionUtil {
	public static Object getFieldValue(Object target, String fieldName) {
		Field field = ReflectionUtils.findField(target.getClass(), fieldName);
		boolean accessible = field.isAccessible();
		ReflectionUtils.makeAccessible(field);
		Object value = ReflectionUtils.getField(field, target);
		field.setAccessible(accessible);
		return value;
	}
}