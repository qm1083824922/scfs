package com.scfs.web.controller.report;

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
import com.scfs.domain.report.entity.InvoicingListReport;
import com.scfs.domain.report.entity.InvoicingReport;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.report.req.InvoicingReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.report.InvoicingReportService;

@Controller
public class InvoicingReportController {

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoicingReportController.class);

	@Autowired
	private InvoicingReportService invoicingReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_INVOICING_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoicingReport> queryResultsByCon(InvoicingReportReqDto invoicingReportReqDto) {
		PageResult<InvoicingReport> result = new PageResult<InvoicingReport>();
		try {
			result = invoicingReportService.queryResultsByCon(invoicingReportReqDto);

		} catch (BaseException e) {
			LOGGER.error("查询进销存报表异常[{}]", JSONObject.toJSON(invoicingReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询进销存报表异常[{}]", JSONObject.toJSON(invoicingReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_INVOICING_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoicingListReport> queryResultsDtlByCon(InvoicingReportReqDto invoicingReportReqDto) {
		Result<InvoicingListReport> result = new Result<InvoicingListReport>();
		try {
			result = invoicingReportService.queryResultsDtlByCon(invoicingReportReqDto);

		} catch (BaseException e) {
			LOGGER.error("查询进销存报表异常[{}]", JSONObject.toJSON(invoicingReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询进销存报表异常[{}]", JSONObject.toJSON(invoicingReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICING_REPORT, method = RequestMethod.GET)
	public String exportSaleReport(ModelMap model, InvoicingReportReqDto invoicingReportReqDto) {
		List<InvoicingReport> result = invoicingReportService.queryExportResultsByCon(invoicingReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("invoicingReportList", result);
		} else {
			model.addAttribute("invoicingReportList", new ArrayList<SaleDtlResult>());
		}
		return "export/report/invoicing/invoicing_report_list";
	}
}
