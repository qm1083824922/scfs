package com.scfs.web.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.web.base.BaseJUnitTest;

public class BaseUserControllerTest extends BaseJUnitTest {
	
	//private String baseUrl = "http://10.40.6.237/scf";
	private String baseUrl = "http://localhost:8081/scf-web";
	
	@Test
	public void testUpdate(){
		String url = baseUrl + "/user/update";
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("id", "16");
		parameter.put("chineseName", "张三");
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
	public void testAdd(){
		String url = baseUrl + "/user/add";
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("userName", "7788");
		parameter.put("employeeNumber", "123");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}

}
