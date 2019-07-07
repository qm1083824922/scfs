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
import com.scfs.domain.audit.model.RefundApplyAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.RefundApplyAuditService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: RefundApplyAuditController.java
 *  Description:退税审核
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日				Administrator
 *
 * </pre>
 */
@RestController
public class RefundApplyAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(RefundApplyAuditController.class);

	@Autowired
	RefundApplyAuditService refundApplyAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.REFUND_APPLY_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<RefundApplyAuditInfo> queryCollectAuditInfo(Integer refundId) {
		Result<RefundApplyAuditInfo> result = new Result<RefundApplyAuditInfo>();
		try {
			result = refundApplyAuditService.queryRefundApplyAuditInfo(refundId);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(refundId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(refundId), e);
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
	@RequestMapping(value = AuditUrlConsts.REFUND_APPLY_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			refundApplyAuditService.passFinanceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出口退税财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出口退税财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("出口退税财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.REFUND_APPLY_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			refundApplyAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出口退税财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出口退税财务审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("出口退税财务审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.REFUND_APPLY_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			refundApplyAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出口退税转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出口退税转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("出口退税转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.REFUND_APPLY_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			refundApplyAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出口退税加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出口退税加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("出口退税加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.REFUND_APPLY_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = refundApplyAuditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
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
