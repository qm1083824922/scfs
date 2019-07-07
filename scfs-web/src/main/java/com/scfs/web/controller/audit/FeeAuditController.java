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
import com.scfs.domain.audit.dto.req.FeeAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.FeeAuditService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: FeeAuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月5日				Administrator
 *
 * </pre>
 */
@RestController
public class FeeAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeAuditController.class);

	@Autowired
	FeeAuditService feeAuditService;

	@Autowired
	FeeServiceImpl feeService;

	@RequestMapping(value = AuditUrlConsts.FEE_INFO_AUDIT, method = RequestMethod.POST)
	public Result<FeeQueryResDto> queryFeeAuditInfo(Integer feeId) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		try {
			return feeService.detailEntityById(feeId);
		} catch (BaseException e) {
			LOGGER.error("查询费用单[{}]业务审核异常：,{}", feeId, e);
			result.setMsg(e.getMsg());
			return result;
		} catch (Exception e) {
			LOGGER.error("查询费用单[{}]业务审核异常：,{}", feeId, e);
			result.setMsg("查询费用单审核异常，请稍后重试");
			return result;
		}
	}

	@RequestMapping(value = AuditUrlConsts.FEE_FINANCE_PASS_AUDIT, method = RequestMethod.POST)
	public BaseResult passFinanceAudit(FeeAuditReqDto feeAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeAuditService.passFinanceAudit(feeAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("费用单业务审核异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("费用单业务审核异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg("费用单业务审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.FEE_FINANCE2_PASS_AUDIT, method = RequestMethod.POST)
	public BaseResult passFinance2Audit(FeeAuditReqDto feeAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeAuditService.passFinance2Audit(feeAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("费用单业务审核异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("费用单业务审核异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg("费用单业务审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.FEE_UNPASS_AUDIT, method = RequestMethod.POST)
	public BaseResult unpassAudit(FeeAuditReqDto feeAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeAuditService.unPassAudit(feeAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("费用单业务审核异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("费用单业务审核异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg("费用单业务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.FEE_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(FeeAuditReqDto feeAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeAuditService.deliverAudit(feeAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("费用单转交不通过异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("费用单转交不通过异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg("费用单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.FEE_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(FeeAuditReqDto feeAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeAuditService.sighAudit(feeAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("费用单加签不通过异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("费用单加签不通过异常：[{}],{}", JSONObject.toJSON(feeAuditReqDto), e);
			result.setMsg("费用单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询费用审核流
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.FEE_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> querFeeAuditFlowsResults(FeeAuditReqDto feeAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = feeAuditService.queryAuditFlows(feeAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(feeAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(feeAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

}
