package com.scfs.web.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.web.base.BaseJUnitTest;

public class ProjectGoodsControllerTest extends BaseJUnitTest {
	
	//private String baseUrl = "http://10.40.6.237/scf";
	private String baseUrl = "http://localhost:8080/scf";

	@Test
    public void queryGoodsToProjectByCon(){
		String url = baseUrl + BusUrlConsts.QUERY_PROJECT_GOODS_NOTASSIGNED+"?projectId=10";
		String result = null;
		try {
			result = HttpInvoker.get(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
    public void queryProjectGoodsResultsByProjectId(){
		String url = baseUrl + BusUrlConsts.QUERY_PROJECT_GOODS+"?projectId=10";
		String result = null;
		try {
			result = HttpInvoker.get(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
	public void createProjectGoods(){
		String url = baseUrl + BusUrlConsts.DIVID_PROJECT_GOODS;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("goodsId", "6");
		parameter.put("projectId", "10");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
	public void createProjectGoods2(){
		String url = baseUrl + BusUrlConsts.DIVIDALL_PROJECT_GOODS;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("ids", "7,8,9,10");
		parameter.put("projectId", "10");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
    public void deleteProjectGoods(){
		String url = baseUrl + BusUrlConsts.DELETE_PROJECT_GOODS;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("id", "5");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
    public void deleteAllProjectGoods(){
		String url = baseUrl + BusUrlConsts.DELETEALL_PROJECT_GOODS;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("ids", "7,8");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
}
