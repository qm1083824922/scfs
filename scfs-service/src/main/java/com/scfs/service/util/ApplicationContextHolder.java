package com.scfs.service.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Administrator on 2016/10/7.
 */
public class ApplicationContextHolder implements ApplicationContextAware, DisposableBean {
	private static ApplicationContext context;

	public static ApplicationContext get() {
		return context;
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return (T) context.getBean(name, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return (T) context.getBean(requiredType);
	}

	public static Object getBean(String beanId) throws BeansException {
		return context.getBean(beanId);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		setContext(applicationContext);
	}

	private static void setContext(ApplicationContext context) {
		ApplicationContextHolder.context = context;
	}

	public void destroy() {
		setContext(null);
	}
}
