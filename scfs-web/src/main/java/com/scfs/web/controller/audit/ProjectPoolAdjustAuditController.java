package com.scfs.web.controller.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.AuditUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.audit.dto.req.BaseAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.ProjectPoolAdjustAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.ProjectPoolAdjustAuditService;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustAuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月23日				Administrator
 *
 * </pre>
 */
@Controller
public class ProjectPoolAdjustAuditController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectPoolAdjustAuditController.class);

	@Autowired
	ProjectPoolAdjustAuditService projectPoolAdjustAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectPoolAdjustAuditInfo> queryAudit1ByCond(BaseAuditReqDto auditReqDto) {
		Result<ProjectPoolAdjustAuditInfo> result = new Result<ProjectPoolAdjustAuditInfo>();
		try {
			result = projectPoolAdjustAuditService.queryProjectPoolAdjustAuditInfo(auditReqDto.getPoId());
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
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_BUS_PASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passBusAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.passBusAudit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请业务审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请业务审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请业务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.passFinance2Audit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请财务专员审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请财务专员审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请财务专员审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 风控审核通过
	 * 
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_RISK_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.passRiskAudit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请单风控审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请单风控审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请单风控审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 部门主管审核通过
	 * 
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_DEPT_MANAGE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passDeptManageAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.passDeptManageAudit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请单部门主管审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请单部门主管审核异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请单部门主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.unPassAudit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请单审核不通过异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请单审核不通过异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.deliverAudit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请单转交不通过异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请单转交不通过异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param auditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(BaseAuditReqDto auditReqDto) {
		BaseResult result = new BaseResult();
		try {
			projectPoolAdjustAuditService.sighAudit(auditReqDto);
		} catch (BaseException e) {
			LOGGER.error("临时额度申请单加签不通过异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("临时额度申请单加签不通过异常：【{}】,{}", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("临时额度申请单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询临时额度申请
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PROJECT_POOL_ADJUST_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(BaseAuditReqDto auditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = projectPoolAdjustAuditService.queryAuditFlows(auditReqDto.getPoId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(auditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(auditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

}
