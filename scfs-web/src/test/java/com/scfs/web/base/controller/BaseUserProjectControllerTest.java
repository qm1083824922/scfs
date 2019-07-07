package com.scfs.web.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.web.base.BaseJUnitTest;

public class BaseUserProjectControllerTest extends BaseJUnitTest {
	
	//private String baseUrl = "http://10.40.6.237/scf";
    private String baseUrl = "http://localhost:8081/scf-web";
    
    @Test
	public void testDeleteAll(){
    	String url = baseUrl + "/user/project/deleteAll";
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("ids", "5,6");
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
    }

}
