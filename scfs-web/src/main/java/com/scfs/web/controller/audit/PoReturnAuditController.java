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
import com.scfs.domain.audit.dto.req.PoAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.PoAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.PoReturnAuditService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2017年3月27日.
 */
@Controller
public class PoReturnAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PoReturnAuditController.class);
	@Autowired
	private PoReturnAuditService poReturnAuditService;

	@RequestMapping(value = AuditUrlConsts.PO_RETURN_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoAuditInfo> queryPoAuditInfo(Integer poId) {
		Result<PoAuditInfo> result = new Result<PoAuditInfo>();
		try {
			return poReturnAuditService.queryPoAuditInfoResultByPoId(poId);
		} catch (BaseException e) {
			LOGGER.error("查询采购退货单[{}]业务审核异常：,{}", poId, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询采购退货单[{}]业务审核异常：,{}", poId, e);
			result.setMsg("采购退货单业务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务专员审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PO_RETURN_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(PoAuditReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			poReturnAuditService.passFinanceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("采购退货单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("采购退货单财务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("采购退货单财务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PO_RETURN_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(PoAuditReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			poReturnAuditService.passFinance2Audit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("采购退货单财务主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("采购退货单财务主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("采购退货单财务主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PO_RETURN_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(PoAuditReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			poReturnAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("采购退货单审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("采购退货单审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("采购退货单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PO_RETURN_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(PoAuditReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			poReturnAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("采购退货单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("采购退货单转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("采购退货单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PO_RETURN_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(PoAuditReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			poReturnAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("采购退货单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("采购退货单加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("采购退货单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询订单审核记录
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.PO_RETURN_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(PoAuditReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = poReturnAuditService.queryAuditFlows(poAuditReqDto.getPoId());
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
