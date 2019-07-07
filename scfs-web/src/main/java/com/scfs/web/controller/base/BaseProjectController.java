package com.scfs.web.controller.base;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.project.dto.req.ProjectSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectModelResDto;
import com.scfs.domain.project.dto.resp.ProjectResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectService;
import com.scfs.web.controller.BaseController;

/**
 * 项目Controller
 * 
 * @author 
 *
 */
@Controller
public class BaseProjectController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseProjectController.class);

	@Autowired
	private ProjectService projectService;

	/**
	 * 查询项目
	 * 
	 * @param projectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectResDto> queryProjectResultsByCon(ProjectSearchReqDto projectReqDto) {
		PageResult<ProjectResDto> pageResult = new PageResult<ProjectResDto>();
		try {
			pageResult = projectService.queryProjectResultsByCon(projectReqDto);
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 新建项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADDPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createProject(BaseProject baseProject) {
		Result<Integer> br = new Result<Integer>();
		try {
			Integer id = projectService.createProject(baseProject);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(baseProject), e);
			br.setSuccess(false);
			br.setMsg("插入失败，请重试");
		}
		return br;
	}

	/**
	 * 更新保存项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATEPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateProjectById(BaseProject baseProject) {
		BaseResult br = new BaseResult();
		try {
			projectService.updateProjectById(baseProject);
		} catch (Exception e) {
			LOGGER.error("更新信息异常[{}]", JSONObject.toJSON(baseProject), e);
			br.setSuccess(false);
			br.setMsg("更新失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAILPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectModelResDto> detailProjectById(BaseProject baseProject) {
		Result<ProjectModelResDto> result = new Result<ProjectModelResDto>();
		try {
			ProjectModelResDto vo = projectService.detailProjectById(baseProject);
			result.setItems(vo);
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(baseProject), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑预览项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDITPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectModelResDto> editProjectById(BaseProject baseProject) {
		Result<ProjectModelResDto> result = new Result<ProjectModelResDto>();
		try {
			ProjectModelResDto vo = projectService.editProjectById(baseProject);
			result.setItems(vo);
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(baseProject), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMITPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitProjectById(BaseProject baseProject) {
		BaseResult br = new BaseResult();
		try {
			projectService.submitProjectById(baseProject);
		} catch (Exception e) {
			LOGGER.error("提交信息异常[{}]", JSONObject.toJSON(baseProject), e);
			br.setSuccess(false);
			br.setMsg("提交失败，请重试");
		}
		return br;
	}

	/**
	 * 删除项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteProjectById(BaseProject baseProject) {
		BaseResult br = new BaseResult();
		try {
			projectService.deleteProjectById(baseProject);
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(baseProject), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 锁定项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.LOCKPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult lockProjectById(BaseProject baseProject) {
		BaseResult br = new BaseResult();
		try {
			projectService.lockProjectById(baseProject);
		} catch (Exception e) {
			LOGGER.error("锁定信息异常[{}]", JSONObject.toJSON(baseProject), e);
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/**
	 * 解锁项目
	 * 
	 * @param baseProject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UNLOCKPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unlockProjectById(BaseProject baseProject) {
		BaseResult br = new BaseResult();
		try {
			projectService.unlockProjectById(baseProject);
		} catch (Exception e) {
			LOGGER.error("解锁信息异常[{}]", JSONObject.toJSON(baseProject), e);
			br.setSuccess(false);
			br.setMsg("解锁失败，请重试");
		}
		return br;
	}

	/**
	 * 导出付款信息excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_PROJECT, method = RequestMethod.GET)
	public String exportProjectExcel(ModelMap model, ProjectSearchReqDto projectReqDto) {
		List<ProjectResDto> projectList = projectService.queryProjectResDtoResultsExcel(projectReqDto);
		if (!CollectionUtils.isEmpty(projectList) && projectList.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("projectList", projectList);
		} else {
			model.addAttribute("projectList", new ArrayList<PayOrderResDto>());
		}
		return "export/project/project_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PROJECT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportProjectByCount(ProjectSearchReqDto projectReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = projectService.isOverasyncMaxLine(projectReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", projectReqDto, e);
		}
		return result;
	}

}
