package com.scfs.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.scfs.dao.base.entity.BaseDepartmentDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ImportParamValidate.java
 *  Description:下拉校验
 *  TODO
 *  Date,					Who,				
 *  2017年04月18日			Administrator
 *
 * </pre>
 */
@Service
public class CommonParamValidate {
	@Autowired
	BaseDepartmentDao baseDepartmentDao;
	@Autowired
	CommonService commonService;

	/**
	 * 判断项目名是否正确
	 * 
	 * @param projectName
	 * @return
	 */
	public String userProjectValidate(String projectName) {
		String projectId = null;
		List<CodeValue> listProject = commonService.getUserProject("USER_PROJECT",
				ServiceSupport.getUser().getId() + "");
		for (CodeValue code : listProject) {
			if (code.getValue().equals(projectName)) {
				projectId = code.getCode();
			}
		}
		return projectId;
	}

	/**
	 * 校验项目下项目下的经营单位 客户 供应商
	 * 
	 * @param name
	 * @param pid
	 * @return
	 */
	public String projectBcsValidate(String pid, String name) {
		String projectId = null;
		List<CodeValue> listProject = commonService.getAllOwnCv("PROJECT_BCS", pid);
		for (CodeValue code : listProject) {
			if (code.getValue().equals(name)) {
				projectId = code.getCode();
			}
		}
		return projectId;
	}

	/**
	 * 费用相关校验
	 * 
	 * @param key
	 * @param name
	 * @return
	 */
	public String feeValidate(String key, String name) {
		String projectId = null;
		List<CodeValue> listProject = commonService.getAllFee(key);
		for (CodeValue code : listProject) {
			if (code.getValue().equals(name)) {
				projectId = code.getCode();
			}
		}
		return projectId;
	}

	/**
	 * 常量类
	 * 
	 * @param key
	 * @param name
	 * @return
	 */
	public String cvListByBizCodeValidate(String key, String name) {
		String codeId = null;
		List<CodeValue> listProject = commonService.getBizConstant(key);
		for (CodeValue code : listProject) {
			if (code.getValue().equals(name)) {
				codeId = code.getCode();
			}
		}
		return codeId;
	}

	/***
	 * 校验关联父级下拉相关列表
	 * 
	 * @param key
	 * @param pId
	 * @param name
	 * @return
	 */
	public String getAllOwnCvValidate(String key, String pId, String name) {
		String codeId = null;
		List<CodeValue> listProject = commonService.getAllOwnCv(key, pId);
		for (CodeValue code : listProject) {
			if (code.getValue().equals(name)) {
				codeId = code.getCode();
			}
		}
		return codeId;
	}

	/**
	 * 校验所有下拉列表
	 * 
	 * @param key
	 * @param name
	 * @return
	 */
	public String getAllCdByKeyValidate(String key, String name) {
		String codeId = null;
		List<CodeValue> listProject = commonService.getAllCdByKey(key);
		for (CodeValue code : listProject) {
			if (code.getValue().equals(name)) {
				codeId = code.getCode();
			}
		}
		return codeId;
	}

	/**
	 * 获取部门信息
	 * 
	 * @param name
	 * @return
	 */
	public String getDepartmentDao(String name) {
		String result = null;
		BaseDepartment baseDepartment = new BaseDepartment();
		if (name.contains("-")) {
			baseDepartment.setNumber(name.split("-")[0].trim());
			name = name.split("-")[1];
		}
		baseDepartment.setName(name.trim());
		BaseDepartment entity = baseDepartmentDao.queryEntityParam(baseDepartment);
		if (entity != null) {
			result = entity.getId() + "";
		}
		return result;
	}

	/**
	 * 获取所有用户信息
	 * 
	 * @return
	 */
	public List<CodeValue> getAllUserList() {
		List<CodeValue> listProject = commonService.getAllCdByKey("USER");
		return listProject;
	}
}
