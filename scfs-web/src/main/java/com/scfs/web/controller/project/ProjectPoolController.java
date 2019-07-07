package com.scfs.web.controller.project;

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
import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectPoolSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectPoolResDto;
import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.domain.project.entity.ProjectPoolDtl;
import com.scfs.domain.project.entity.ProjectPoolFund;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectPoolService;

@Controller
public class ProjectPoolController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectPoolController.class);

	@Autowired
	private ProjectPoolService projectService;

	/**
	 * 查询融资池信息
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectPoolResDto> queryProjectResultsByCon(ProjectPoolSearchReqDto projectPool) {
		PageResult<ProjectPoolResDto> pageResult = new PageResult<ProjectPoolResDto>();
		try {
			pageResult = projectService.queryProjectPoolResultsByCon(projectPool);

		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectPool), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectPool), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 浏览融资池一条记录
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOLBYID, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectPoolResDto> queryProjectResultsById(ProjectPoolSearchReqDto projectPool) {

		Result<ProjectPoolResDto> result = new Result<ProjectPoolResDto>();
		try {
			ProjectPoolResDto vo = projectService.detailProjectItemById(projectPool.getId());
			result.setItems(vo);
		} catch (BaseException e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(projectPool), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(projectPool), e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 浏览融资池一条记录
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOLBYPROJECTID, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectPoolResDto> queryProjectResultsByProjectId(Integer projectId) {

		Result<ProjectPoolResDto> result = new Result<ProjectPoolResDto>();
		try {
			ProjectPoolResDto vo = projectService.detailProjectItemByProjectId(projectId);
			result.setItems(vo);
		} catch (BaseException e) {
			LOGGER.error("查询融资池信息异常[{}]", JSONObject.toJSON(projectId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询融资池信息异常[{}]", JSONObject.toJSON(projectId), e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 查询资金明细
	 * 
	 * @param projectPoolDtl
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOLFUNDDTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectPoolFund> queryProjectDtlResultsByCon(ProjectPoolDtlSearchReqDto projectPoolDtl) {
		PageResult<ProjectPoolFund> pageResult = new PageResult<ProjectPoolFund>();
		try {
			pageResult = projectService.queryProjectPoolFundResultsByCon(projectPoolDtl);
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 查询资产明细
	 * 
	 * @param projectPoolDtl
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOLASSERTDTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectPoolAsset> queryProjectFundResultsByCon(ProjectPoolDtlSearchReqDto projectPoolDtl) {
		PageResult<ProjectPoolAsset> pageResult = new PageResult<ProjectPoolAsset>();
		try {
			pageResult = projectService.queryProjectPoolAssertResultsByCon(projectPoolDtl);

		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 新建资产明细
	 * 
	 * @param projectPoolDtl
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADDPROJECTPOOLASSERTDTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createProjectPoolAssertDtl(ProjectPoolDtl projectPoolDtl) {
		BaseResult br = new BaseResult();
		try {
			projectPoolDtl.setType(2);
		} catch (BaseException e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			br.setMsg("插入失败，请重试");
		}
		return br;
	}

	/**
	 * 新建资金明细
	 * 
	 * @param projectPoolDtl
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADDPROJECTPOOLFUNDDTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createProjectPoolFundDtl(ProjectPoolDtl projectPoolDtl) {
		BaseResult br = new BaseResult();
		try {
			projectPoolDtl.setType(1);
		} catch (BaseException e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(projectPoolDtl), e);
			br.setMsg("插入失败，请重试");
		}
		return br;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_REPORT, method = RequestMethod.GET)
	public String exportFund(ModelMap model, ProjectPoolDtlSearchReqDto projectPoolDtl) {
		List<ProjectPoolFund> result = projectService.queryFundResultsByCon(projectPoolDtl);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("projectPoolFundList", result);
		} else {
			model.addAttribute("projectPoolFundList", new ArrayList<SaleDtlResult>());
		}
		return "export/project/project_pool_fund_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_ASSERT_REPORT, method = RequestMethod.GET)
	public String exportAssert(ModelMap model, ProjectPoolDtlSearchReqDto projectPoolDtl) {
		List<ProjectPoolAsset> result = projectService.queryAssertResultsByCon(projectPoolDtl);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("projectPoolAssertList", result);
		} else {
			model.addAttribute("projectPoolAssertList", new ArrayList<SaleDtlResult>());
		}
		return "export/project/project_pool_assert_list";
	}
}
