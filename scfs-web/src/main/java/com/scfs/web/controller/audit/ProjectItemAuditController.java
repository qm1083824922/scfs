package com.scfs.web.controller.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.AuditUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectItemAuditModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.ProjectItemAuditService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: AuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月17日				cuichao
 *
 * </pre>
 */
@RestController
public class ProjectItemAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectItemAuditController.class);

	@Autowired
	private ProjectItemService projectItemService;

	@Autowired
	private ProjectItemAuditService auditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectItemAuditModel> queryAudit1ByCond(ProjectItemReqDto auditReqDto) {
		Result<ProjectItemAuditModel> result = new Result<ProjectItemAuditModel>();
		ProjectItem projectItem = new ProjectItem();
		projectItem.setId(auditReqDto.getProjectItemId());
		try {
			ProjectItemAuditModel item = projectItemService.detailProjectById(projectItem);
			result.setItems(item);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 业务审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_BUS_PASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passBusAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.passBusAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款业务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款业务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款业务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.passFinance2Audit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款财务专员审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款财务专员审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款财务专员审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 风控审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_RISK_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.passRiskAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款单风控审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款单风控审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款单风控审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 部门主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_DEPT_MANAGE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passDeptManageAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.passDeptManageAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款单部门主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款单部门主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款单部门主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款单审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款单审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			auditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("条款单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("条款单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("条款单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_ITEM_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = auditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(poAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(poAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}
}
