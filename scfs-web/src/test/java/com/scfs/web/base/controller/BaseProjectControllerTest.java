package com.scfs.web.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.web.base.BaseJUnitTest;

public class BaseProjectControllerTest extends BaseJUnitTest {
	
	    //private String baseUrl = "http://10.40.6.237/scf";
		private String baseUrl = "http://localhost:8080/scf";
		
		@Test
	    public void queryProjectResultsByCon(){
			String url = baseUrl + BusUrlConsts.QUERYPROJECT;
			
			String result = null;
			try {
				result = HttpInvoker.get(url);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
	    public void createProject(){
			String url = baseUrl + BusUrlConsts.ADDPROJECT;
			
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("projectNo", "P00001");
			parameter.put("projectName", "小米");
			parameter.put("fullName", "香港小米");
			parameter.put("businessUnitId", "1");
			parameter.put("totalAmount", "3000");
			parameter.put("amountUnit", "1");
			parameter.put("bizType", "1");
			parameter.put("bizManagerId", "4");
			parameter.put("businessManagerId", "4");
			parameter.put("financeManagerId", "4");
			parameter.put("riskManagerId", "4");
			String result = null;
			try {
				result = HttpInvoker.post(url, parameter);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
		public void detailProjectById(){
			String url = baseUrl + BusUrlConsts.DETAILPROJECT;
			String result = null;
			try {
				result = HttpInvoker.get(url+"?id=10");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
		public void editProjectById(){
			String url = baseUrl + BusUrlConsts.EDITPROJECT;
			String result = null;
			try {
				result = HttpInvoker.get(url+"?id=10");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
		public void deleteProjectById(){
			String url = baseUrl + BusUrlConsts.DELETEPROJECT;
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("id", "10");
			String result = null;
			try {
				result = HttpInvoker.post(url, parameter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
	    public void updateProjectById(){
			String url = baseUrl + BusUrlConsts.UPDATEPROJECT;
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("id", "10");
			parameter.put("projectName", "小米");
			parameter.put("fullName", "香港小米");
			parameter.put("totalAmount", "3000");
			parameter.put("amountUnit", "1");
			parameter.put("bizType", "2");
			parameter.put("bizManagerId", "5");
			parameter.put("businessManagerId", "5");
			parameter.put("financeManagerId", "5");
			parameter.put("riskManagerId", "5");
			String result = null;
			try {
				result = HttpInvoker.post(url, parameter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
		public void lockProjectById(){
			String url = baseUrl + BusUrlConsts.LOCKPROJECT;
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("id", "7");
			String result = null;
			try {
				result = HttpInvoker.post(url, parameter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
		public void unlockProjectById(){
			String url = baseUrl + BusUrlConsts.UNLOCKPROJECT;
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("id", "10");
			String result = null;
			try {
				result = HttpInvoker.post(url, parameter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
		@Test
		public void submitProjectById(){
			String url = baseUrl + BusUrlConsts.SUBMITPROJECT;
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("id", "7");
			String result = null;
			try {
				result = HttpInvoker.post(url, parameter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info(result);
		}
		
}
