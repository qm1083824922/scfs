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
import com.scfs.domain.pay.model.PayAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.PayAuditService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: PayAuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月18日				Administrator
 *
 * </pre>
 */
@RestController
public class PayAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayAuditController.class);

	@Autowired
	PayAuditService payAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayAuditInfo> queryAuditByCond(Integer payId) {
		Result<PayAuditInfo> result = new Result<PayAuditInfo>();
		try {
			result = payAuditService.queryPayAuditInfo(payId);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(payId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(payId), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 法务主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_LAW_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passLawAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passLawAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单法务主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单法务主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单法务主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 风控主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_RISK_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passRiskAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单风控审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单风控审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单风控审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 商务审核
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_RISK_BUS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskBusinessAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passRiskBusinessAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单商务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单商务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单商务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 风控专员审核
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_RISK_SPECIAL_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskSpecialAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passRiskSpecialAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单风控专员审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单风控审专员核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单风控专员审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务专员审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passFinanceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passFinance2Audit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 部门主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_DEPT_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passDeptManageAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passDeptManageAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单部门审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单部门审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单部门审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 总经理审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_BOSS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passBossAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.passBossAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			payAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("付款单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询付款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PAY_ORDER_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = payAuditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
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
