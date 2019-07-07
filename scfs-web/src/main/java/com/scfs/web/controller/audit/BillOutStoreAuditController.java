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
import com.scfs.domain.audit.dto.req.BillOutStoreAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.BillOutStoreAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.BillOutStoreAuditService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年11月2日.
 */
@Controller
public class BillOutStoreAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillOutStoreAuditController.class);
	@Autowired
	private BillOutStoreAuditService billOutStoreAuditService;

	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStoreAuditInfo> queryBillOutStoreAuditInfo(Integer id) {
		Result<BillOutStoreAuditInfo> result = new Result<BillOutStoreAuditInfo>();
		try {
			return billOutStoreAuditService.queryBillOutStoreResultAuditInfo(id);
		} catch (BaseException e) {
			LOGGER.error("查询出库单异常：[{}]", id, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单异常：[{}]", id, e);
			result.setMsg("查询出库单信息异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(BillOutStoreAuditReqDto BillOutStoreAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreAuditService.passFinance2Audit(BillOutStoreAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出库单财务主管审核异常：[{}]", JSONObject.toJSON(BillOutStoreAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出库单财务主管审核异常：[{}]", JSONObject.toJSON(BillOutStoreAuditReqDto), e);
			result.setMsg("出库单财务主管审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(BillOutStoreAuditReqDto BillOutStoreAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreAuditService.passFinanceAudit(BillOutStoreAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出库单财务专员审核异常：[{}]", JSONObject.toJSON(BillOutStoreAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出库单财务专员审核异常：[{}]", JSONObject.toJSON(BillOutStoreAuditReqDto), e);
			result.setMsg("出库单财务专员审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(BillOutStoreAuditReqDto BillOutStoreAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreAuditService.unPassAudit(BillOutStoreAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出库单审核不通过异常：[{}]", JSONObject.toJSON(BillOutStoreAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出库单审核不通过异常：[{}]", JSONObject.toJSON(BillOutStoreAuditReqDto), e);
			result.setMsg("出库单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param billOutStoreAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreAuditService.deliverAudit(billOutStoreAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出库单转交不通过异常：【{}】,{}", JSONObject.toJSON(billOutStoreAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出库单转交不通过异常：【{}】,{}", JSONObject.toJSON(billOutStoreAuditReqDto), e);
			result.setMsg("出库单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param billOutStoreAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreAuditService.sighAudit(billOutStoreAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("出库单加签不通过异常：【{}】,{}", JSONObject.toJSON(billOutStoreAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出库单加签不通过异常：【{}】,{}", JSONObject.toJSON(billOutStoreAuditReqDto), e);
			result.setMsg("出库单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询出库单审核记录
	 * 
	 * @param billOutStoreAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_OUT_STORE_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryBillOutStoreAuditFlowsResults(
			BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = billOutStoreAuditService.queryAuditFlows(billOutStoreAuditReqDto.getBillOutStoreId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billOutStoreAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billOutStoreAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}
}
