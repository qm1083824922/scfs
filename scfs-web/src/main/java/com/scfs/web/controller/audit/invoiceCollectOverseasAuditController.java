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
import com.scfs.domain.audit.model.InvoiceCollectOverseasAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.invoiceCollectOverseasAuditService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: invoiceCollectOverseasAuditController.java
 *  Description:Invoice收票审核
 *  TODO
 *  Date,					Who,				
 *  2017年08月03日				Administrator
 *
 * </pre>
 */
@RestController
public class invoiceCollectOverseasAuditController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(invoiceCollectOverseasAuditController.class);

	@Autowired
	private invoiceCollectOverseasAuditService invoiceCollectOverseasAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.INVOICE_COLLECT_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectOverseasAuditInfo> queryInvoiceCollectAuditInfo(Integer overId) {
		Result<InvoiceCollectOverseasAuditInfo> result = new Result<InvoiceCollectOverseasAuditInfo>();
		try {
			result = invoiceCollectOverseasAuditService.queryCollectOverseasAuditInfo(overId);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(overId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(overId), e);
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
	@RequestMapping(value = AuditUrlConsts.INVOICE_COLLECT_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasAuditService.passFinanceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("Invoice收票财务专员审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("Invoice收票财务专员审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("Invoice收票财务专员审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_COLLECT_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("Invoice收票财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("Invoice收票财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("Invoice收票财务审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_COLLECT_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("Invoice收票转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("Invoice收票转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("Invoice收票转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_COLLECT_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("Invoice收票加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("Invoice收票加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("Invoice收票加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_COLLECT_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = invoiceCollectOverseasAuditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
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
