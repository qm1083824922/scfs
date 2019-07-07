package com.scfs.web.controller.invoice;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceDtlInfo;
import com.scfs.domain.invoice.entity.InvoiceInfo;
import com.scfs.domain.invoice.entity.InvoiceInfoDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.service.invoice.InvoiceInfoService;
import com.scfs.web.controller.BaseController;

/**
 * 发票信息
 * 
 * @author Administrator
 *
 */
@Controller
public class InvoiceInfoController extends BaseController {

	@Autowired
	private InvoiceInfoService invoiceInfoService;
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceInfoController.class);

	/**
	 * 插入 模拟发票信息
	 */
	@RequestMapping(value = BusUrlConsts.INSERTSIMULATION, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceInfoDtl> insertSimulation(InvoiceApplyManagerReqDto invoiceManager) {
		PageResult<InvoiceInfoDtl> result = new PageResult<InvoiceInfoDtl>();
		try {
			result = invoiceInfoService.insertSimulationInvoiceInfo(invoiceManager.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 查询发票信息
	 */
	@RequestMapping(value = BusUrlConsts.QUERYSIMULATION, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceInfoDtl> querySimulationInvoiceInfo(Integer invoiceApplyId) {
		PageResult<InvoiceInfoDtl> result = new PageResult<InvoiceInfoDtl>();
		try {
			result = invoiceInfoService.queryInvoiceResultsByCon(invoiceApplyId);

		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERYPRINTSIMULATION, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceInfo> queryprintInvoiceInfo(Integer invoiceApplyId) {
		PageResult<InvoiceInfo> result = new PageResult<InvoiceInfo>();
		try {
			result = invoiceInfoService.printInvoiceResultsByCon(invoiceApplyId);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 删除模拟发票信息
	 */
	@RequestMapping(value = BusUrlConsts.DELETESIMULATEINFO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteSimulationInvoiceInfo(Integer invoiceApplyId) {
		PageResult<InvoiceInfoDtl> result = new PageResult<InvoiceInfoDtl>();
		try {
			invoiceInfoService.deleteSimulateInvoiceInfo(invoiceApplyId);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("删除异常，请稍后重试");
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_INFO_EXCEL, method = RequestMethod.GET)
	public String exportInvoiceApply(ModelMap model, Integer invoiceApplyId) {
		List<InvoiceDtlInfo> pageResult = invoiceInfoService.exportInvoiceByCon(invoiceApplyId);
		if (!CollectionUtils.isEmpty(pageResult) && pageResult.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("invoiceInfoList", pageResult);
		} else {
			model.addAttribute("invoiceInfoList", new ArrayList<InvoiceApplyManagerResDto>());
		}
		return "export/invoice/makeinvoice/makeinvoice_info_dtl_list";
	}

}
