package com.scfs.web.controller.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.project.dto.req.ProjectRiskSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectRiskResDto;
import com.scfs.domain.project.entity.ProjectRisk;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectRiskService;

/**
 * <pre>
 *  项目事件
 *  File: ProjectRiskController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日			Administrator
 *
 * </pre>
 */
@Controller
public class ProjectRiskController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectRiskController.class);

	@Autowired
	private ProjectRiskService projectRiskService;

	/**
	 * 新建
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createProjectRisk(ProjectRisk projectRisk) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = projectRiskService.createProjectRisk(projectRisk);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加异常[{}]", JSONObject.toJSON(projectRisk), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览项目事件
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectRiskResDto> detailProjectRisk(ProjectRisk projectRisk) {
		Result<ProjectRiskResDto> result = new Result<ProjectRiskResDto>();
		try {
			result = projectRiskService.editProjectRiskById(projectRisk);
		} catch (Exception e) {
			LOGGER.error("浏览事件失败[{}]", JSONObject.toJSON(projectRisk), e);
			result.setMsg("浏览事件异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑项目事件
	 * 
	 * @param projectRisk
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectRiskResDto> editProjectRiskById(ProjectRisk projectRisk) {
		Result<ProjectRiskResDto> result = new Result<ProjectRiskResDto>();
		try {
			result = projectRiskService.editProjectRiskById(projectRisk);
		} catch (Exception e) {
			LOGGER.error("编辑事件失败[{}]", JSONObject.toJSON(projectRisk), e);
			result.setMsg("编辑事件异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新项目事件
	 * 
	 * @param projectRisk
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateProjectRiskById(ProjectRisk projectRisk) {
		BaseResult result = new BaseResult();
		try {
			result = projectRiskService.updateProjectRiskById(projectRisk);
		} catch (Exception e) {
			LOGGER.error("更新付款失败[{}]", JSONObject.toJSON(projectRisk), e);
			result.setMsg("更新付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除项目事件
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteProjectRiskById(ProjectRisk projectRisk) {
		BaseResult result = new BaseResult();
		try {
			result = projectRiskService.deleteProjectRisk(projectRisk);
		} catch (Exception e) {
			LOGGER.error("删除付款失败[{}]", JSONObject.toJSON(projectRisk), e);
			result.setMsg("删除付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param projectRisk
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitProjectRiskById(ProjectRisk projectRisk) {
		BaseResult result = new BaseResult();
		try {
			result = projectRiskService.submitProjectRisk(projectRisk);
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(projectRisk), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_RISK, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectRiskResDto> queryPayOrderResultsByCon(ProjectRiskSearchReqDto req) {
		PageResult<ProjectRiskResDto> pageResult = new PageResult<ProjectRiskResDto>();
		try {
			pageResult = projectRiskService.queryPayOrderResultsByCon(req);
		} catch (Exception e) {
			LOGGER.error("查询项目事件失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询项目事件异常，请稍后重试");
		}
		return pageResult;
	}
}
