package com.scfs.web.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.web.base.BaseJUnitTest;

public class BaseExchangeRateControllerTest extends BaseJUnitTest {
	
	//private String baseUrl = "http://10.40.6.237/scf";
	private String baseUrl = "http://localhost:8081/scf-web";
	
	@Test
    public void testAdd(){
		String url = baseUrl + "/exchangeRate/add";
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("bank", "汇丰银行");
		parameter.put("currency", "港币");
		parameter.put("foreignCurrency", "人民币");
		parameter.put("cashSellingPrice", "210");
		parameter.put("cashBuyingPrice", "34");
		parameter.put("draftSellingPrice", "34");
		parameter.put("draftBuyingPrice", "242");
		parameter.put("publishAt", "2016-10-19 20:56:00");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
    public void testUpdate(){
		String url = baseUrl + "/exchangeRate/update";
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("id", "880");
		parameter.put("bank", "1");
		parameter.put("currency", "3");
		parameter.put("foreignCurrency", "2");
		parameter.put("cashSellingPrice", "210");
		parameter.put("cashBuyingPrice", "34");
		parameter.put("draftSellingPrice", "34");
		parameter.put("draftBuyingPrice", "242");
		parameter.put("publishAt", "2016-10-19 20:59:00");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}

}
