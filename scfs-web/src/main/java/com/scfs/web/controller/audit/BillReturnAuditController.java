package com.scfs.web.controller.audit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.AuditUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.domain.BaseResult;
import com.scfs.domain.audit.dto.req.BillDeliveryAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.BillDeliveryAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.audit.BillReturnAuditService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年11月2日.
 */
@Controller
public class BillReturnAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillReturnAuditController.class);
	@Autowired
	private BillReturnAuditService billReturnAuditService;

	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDeliveryAuditInfo> queryBillDeliveryAuditInfo(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		Result<BillDeliveryAuditInfo> result = new Result<BillDeliveryAuditInfo>();
		try {
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billDeliveryAuditReqDto.getId());
			if (StringUtils.isNotBlank(billDeliveryAuditReqDto.getReceiptDate())) {
				billDelivery.setReturnTime(
						DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, billDeliveryAuditReqDto.getReceiptDate()));
			}
			return billReturnAuditService.queryBillDeliveryResultAuditInfo(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("查询销售退货单信息异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billReturnAuditService.passFinance2Audit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售退货单财务主管审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售退货单财务主管审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售退货单财务主管审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(@RequestBody BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billReturnAuditService.passFinanceAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售退货单财务专员审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售退货单财务专员审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售退货单财务专员审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billReturnAuditService.unPassAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售退货单审核不通过异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售退货单审核不通过异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售退货单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billReturnAuditService.deliverAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售退货单转交不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售退货单转交不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售退货单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billReturnAuditService.sighAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售退货单加签不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售退货单加签不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售退货单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询销售退货单审核记录
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_RETURN_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryBillDeliveryAuditFlowsResults(
			BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = billReturnAuditService.queryAuditFlows(billDeliveryAuditReqDto.getBillDeliveryId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

}
