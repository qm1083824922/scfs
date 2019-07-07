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
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.dto.req.BillDeliveryAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.BillDeliveryAuditInfo;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.audit.BillDeliveryAuditService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年11月2日.
 */
@Controller
public class BillDeliveryAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillDeliveryAuditController.class);
	@Autowired
	private BillDeliveryAuditService billDeliveryAuditService;

	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_INFO_AUDIT, method = RequestMethod.POST)
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
			return billDeliveryAuditService.queryBillDeliveryResultAuditInfo(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("查询销售单异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售单异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("查询销售单信息异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.FI_BANK_RECEIPT_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceiptResDto> queryFiBankReceipt(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		PageResult<BankReceiptResDto> result = new PageResult<BankReceiptResDto>();
		try {
			return billDeliveryAuditService.queryFiBankReceipt(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询水单异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询水单异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("查询水单异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.FI_BANK_RECEIPTDATE_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CodeValue> queryFiBankReceiptDateList(Integer id) {
		PageResult<CodeValue> result = new PageResult<CodeValue>();
		try {
			return billDeliveryAuditService.queryFiBankReceiptDateList(id);
		} catch (BaseException e) {
			LOGGER.error("查询水单日期列表异常：[{}]", id, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询水单日期列表异常：[{}]", id, e);
			result.setMsg("查询水单日期列表异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_FINANCE2_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinance2Audit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryAuditService.passFinance2Audit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售单财务主管审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售单财务主管审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售单财务主管审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(@RequestBody BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryAuditService.passFinanceAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售单财务专员审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售单财务专员审核异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售单财务专员审核异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryAuditService.unPassAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售单审核不通过异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售单审核不通过异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售单审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryAuditService.deliverAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售单转交不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售单转交不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售单转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryAuditService.sighAudit(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("销售单加签不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("销售单加签不通过异常：【{}】,{}", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("销售单加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询销售单审核记录
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryBillDeliveryAuditFlowsResults(
			BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = billDeliveryAuditService.queryAuditFlows(billDeliveryAuditReqDto.getBillDeliveryId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询服务费
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.BILL_DELIVERY_SERVICE_AMOUNT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDeliveryResDto> queryServiceAmount(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		try {
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billDeliveryAuditReqDto.getBillDeliveryId());
			if (StringUtils.isNotBlank(billDeliveryAuditReqDto.getReceiptDate())) {
				billDelivery.setReturnTime(DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD_HH_MM_SS,
						billDeliveryAuditReqDto.getReceiptDate()));
			}
			BillDeliveryResDto billDeliveryResDto = billDeliveryAuditService.queryServiceAmount(billDelivery);
			result.setItems(billDeliveryResDto);
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询 水单 预收货款抵扣水单详情
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.FI_BANK_RECEIPT_ADVANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceiptResDto> queryFiBankReceiptAdvance(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		PageResult<BankReceiptResDto> result = new PageResult<BankReceiptResDto>();
		try {
			return billDeliveryAuditService.queryFiBankReceiptAdvance(billDeliveryAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询水单预收货款类型异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询水单预收货款类型异常：[{}]", JSONObject.toJSON(billDeliveryAuditReqDto), e);
			result.setMsg("查询水单预收货款类型异常，请稍后重试");
		}
		return result;
	}
}
