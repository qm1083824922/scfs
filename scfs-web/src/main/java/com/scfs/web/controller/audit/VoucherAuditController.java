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
import com.scfs.domain.audit.dto.req.VoucherAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.VoucherAuditService;
import com.scfs.service.fi.VoucherService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: VoucherAuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月5日				Administrator
 *
 * </pre>
 */

@RestController
public class VoucherAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(VoucherAuditController.class);

	@Autowired
	VoucherAuditService voucherAuditService;

	@Autowired
	VoucherService voucherService;

	@RequestMapping(value = AuditUrlConsts.VOUCHER_INFO_AUDIT, method = RequestMethod.POST)
	public Result<VoucherDetailResDto> queryPoAuditInfo(Integer poId) {
		Result<VoucherDetailResDto> result = new Result<VoucherDetailResDto>();
		try {
			result.setItems(voucherService.detailVoucherDetailById(poId));
		} catch (BaseException e) {
			LOGGER.error("查询凭证[{}]业务审核异常：,{}", poId, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询凭证[{}]业务审核异常：,{}", poId, e);
			result.setMsg("查询凭证审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.VOUCHER_FINANCE_PASS_AUDIT, method = RequestMethod.POST)
	public BaseResult passFinanceAudit(VoucherAuditReqDto voucherAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			voucherAuditService.passFinanceAudit(voucherAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("凭证审核异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("凭证审核异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg("凭证审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.VOUCHER_UNPASS_AUDIT, method = RequestMethod.POST)
	public BaseResult unpassAudit(VoucherAuditReqDto voucherAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			voucherAuditService.unPassAudit(voucherAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("凭证审核异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("凭证审核异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg("凭证审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.VOUCHER_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(VoucherAuditReqDto voucherAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			voucherAuditService.deliverAudit(voucherAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("凭证单转交不通过异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("凭证单转交不通过异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg("凭证单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.VOUCHER_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(VoucherAuditReqDto voucherAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			voucherAuditService.sighAudit(voucherAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("凭证单加签不通过异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("凭证单加签不通过异常：[{}],{}", JSONObject.toJSON(voucherAuditReqDto), e);
			result.setMsg("凭证单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询凭证审核流
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.VOUCHER_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> querFeeAuditFlowsResults(VoucherAuditReqDto voucherAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = voucherAuditService.queryAuditFlows(voucherAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(voucherAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(voucherAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}
}
