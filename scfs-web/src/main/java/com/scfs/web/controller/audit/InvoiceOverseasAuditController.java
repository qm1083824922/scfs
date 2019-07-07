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
import com.scfs.domain.audit.model.InvoiceOverseasAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.InvoiceOverseasAuditService;

/**
 * <pre>
 * 
 *  File: InvoiceOverseasAuditController.java
 *  Description:境外开票审核
 *  TODO
 *  Date,					Who,				
 *  2017年03月31日				Administrator
 *
 * </pre>
 */
@RestController
public class InvoiceOverseasAuditController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceOverseasAuditController.class);

	@Autowired
	private InvoiceOverseasAuditService invoiceOverseasAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceOverseasAuditInfo> queryCollectAuditInfo(Integer overseasId) {
		Result<InvoiceOverseasAuditInfo> result = new Result<InvoiceOverseasAuditInfo>();
		try {
			result = invoiceOverseasAuditService.queryOverseasAuditInfo(overseasId);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(overseasId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(overseasId), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceOverseasAuditService.passFinanceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("收票财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("收票财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("收票财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceOverseasAuditService.passFinance2Audit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("收票财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("收票财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("收票财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceOverseasAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("收票财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("收票财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("收票财务审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceOverseasAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("收票转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("收票转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("收票转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceOverseasAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("收票加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("收票加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("收票加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_OVERSEAS_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = invoiceOverseasAuditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
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
