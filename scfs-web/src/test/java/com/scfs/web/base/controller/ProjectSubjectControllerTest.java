package com.scfs.web.base.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.web.base.BaseJUnitTest;

public class ProjectSubjectControllerTest extends BaseJUnitTest {
	
	//private String baseUrl = "http://10.40.6.237/scf";
	private String baseUrl = "http://localhost:8080/scf";
	
	@Test
    public void querySubjectWToProjectByCon(){
		String url = baseUrl + BusUrlConsts.QUERY_PROJECT_SUBJECTW_NOTASSIGNED+"?projectId=10";
		String result = null;
		try {
			result = HttpInvoker.get(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
    public void queryProjectSubjectWByProjectId(){
		String url = baseUrl + BusUrlConsts.QUERY_PROJECT_SUBJECTW+"?projectId=10";
		String result = null;
		try {
			result = HttpInvoker.get(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
	public void createProjectSubjectW(){
		String url = baseUrl + BusUrlConsts.DIVID_PROJECT_SUBJECTW;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("subjectId", "35");
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
	public void createProjectSubjectW2(){
		String url = baseUrl + BusUrlConsts.DIVIDALL_PROJECT_SUBJECTW;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("ids", "36,37,121");
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
    public void deleteProjectSubjectWById(){
		String url = baseUrl + BusUrlConsts.DELETE_PROJECT_SUBJECTW;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("id", "17");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}
	
	@Test
    public void deleteProjectSubjectWByIds(){
		String url = baseUrl + BusUrlConsts.DELETEALL_PROJECT_SUBJECTW;
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("ids", "18,19");
		
		String result = null;
		try {
			result = HttpInvoker.post(url, parameter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info(result);
	}

}
