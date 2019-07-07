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
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.InvoiceAuditInfo;
import com.scfs.domain.invoice.entity.InvoiceAuditModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.InvoiceAuditService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: InvoiceAuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月5日				Administrator
 *
 * </pre>
 */

@RestController
public class InvoiceAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceAuditController.class);

	@Autowired
	InvoiceAuditService invoiceAuditService;

	@RequestMapping(value = AuditUrlConsts.INVOICE_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceAuditModel> queryInvoiceAuditInfo(Integer poId) {
		Result<InvoiceAuditModel> result = new Result<InvoiceAuditModel>();
		try {
			result = invoiceAuditService.queryData(poId);
		} catch (BaseException e) {
			LOGGER.error("查询发票[{}]财务审核异常：,{}", poId, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询发票[{}]财务审核异常：,{}", poId, e);
			result.setMsg("查詢发票财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务审核通过
	 * 
	 * @param invoiceAuditInfo
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_FINANCE_PASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(InvoiceAuditInfo invoiceAuditInfo) {
		BaseResult result = new BaseResult();
		try {
			invoiceAuditService.passFinanceAudit(invoiceAuditInfo);
		} catch (BaseException e) {
			LOGGER.error("开票财务审核异常：【{}】,{}", JSONObject.toJSON(invoiceAuditInfo), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("开票财务审核异常：【{}】,{}", JSONObject.toJSON(invoiceAuditInfo), e);
			result.setMsg("开票财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param invoiceAuditInfo
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_FINANCE_PASS2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(InvoiceAuditInfo invoiceAuditInfo) {
		BaseResult result = new BaseResult();
		try {
			invoiceAuditService.passFinance2Audit(invoiceAuditInfo);
		} catch (BaseException e) {
			LOGGER.error("开票财务审核异常：【{}】,{}", JSONObject.toJSON(invoiceAuditInfo), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("开票财务审核异常：【{}】,{}", JSONObject.toJSON(invoiceAuditInfo), e);
			result.setMsg("开票财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_FINANCE_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(InvoiceAuditInfo poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("开票财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("开票财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("开票财务审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(InvoiceAuditInfo poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("开票单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("开票单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("开票单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_SIGN_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(InvoiceAuditInfo poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("开票单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("开票单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("开票单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 发票查询
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.INVOICE_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(InvoiceAuditInfo invoiceAuditInfo) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = invoiceAuditService.queryAuditFlows(invoiceAuditInfo.getInvoiceApplyId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(invoiceAuditInfo), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(invoiceAuditInfo), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}
}
