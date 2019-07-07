package com.scfs.web.controller.report;

import java.util.ArrayList;

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
import com.scfs.domain.report.req.DistributionBillsReportReqDto;
import com.scfs.domain.report.resp.DistributionBillsReportModel;
import com.scfs.domain.report.resp.DistributionBillsReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.report.DistributionBillsReportService;

/**
 * <pre>
 *  铺货对账单信息
 *  File: DistributionBillsReportController.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年09月12日         Administrator
 *
 * </pre>
 */
@Controller
public class DistributionBillsReportController {
	@Autowired
	private DistributionBillsReportService distributionBillsReportService;

	private final static Logger LOGGER = LoggerFactory.getLogger(DistributionBillsReportController.class);

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_DISTRIBUTION_BILLS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<DistributionBillsReportResDto> queryDistributionBillsReport(
			DistributionBillsReportReqDto reqDto) {
		PageResult<DistributionBillsReportResDto> result = new PageResult<DistributionBillsReportResDto>();
		try {
			result = distributionBillsReportService.queryDistributionBillsReportResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询铺货对账单信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 浏览铺货对账单
	 * 
	 * @param distributionBillsReport
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.PRINT_DISTRIBUTION_BILLS, method = RequestMethod.POST)
	@ResponseBody
	public Result<DistributionBillsReportModel> detailDistributionBillsReport(DistributionBillsReportReqDto reqDto) {
		Result<DistributionBillsReportModel> result = new Result<DistributionBillsReportModel>();
		try {
			result = distributionBillsReportService.queryDistributionBillsPrintResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览铺货对账单失败[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("浏览铺货对账单异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 预算对账单查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_BUDGET_DISTRIBUTION_BILLS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<DistributionBillsReportResDto> queryDistributionBudgetBillsReport(
			DistributionBillsReportReqDto reqDto) {
		PageResult<DistributionBillsReportResDto> result = new PageResult<DistributionBillsReportResDto>();
		try {
			result = distributionBillsReportService.queryDistributionBudgetBillsReportResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询铺货预算对账单信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 预算对账单浏览铺货对账单
	 * 
	 * @param distributionBillsReport
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.PRINT_BUDGET_DISTRIBUTION_BILLS, method = RequestMethod.POST)
	@ResponseBody
	public Result<DistributionBillsReportModel> detailDistributionBudgetBillsReport(
			DistributionBillsReportReqDto reqDto) {
		Result<DistributionBillsReportModel> result = new Result<DistributionBillsReportModel>();
		try {
			result = distributionBillsReportService.queryDistributionBudgetBillsPrintResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览铺货预算对账单失败[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("浏览铺货预算对账单异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 预算对账单导出数据
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXCEL_BUDGET_DISTRIBUTION_BILLS, method = RequestMethod.GET)
	public String exportPayOrderExcel(ModelMap model, DistributionBillsReportReqDto reqDto) {
		DistributionBillsReportModel billList = distributionBillsReportService
				.queryDistributionBudgetBillsPrintResultsByCon(reqDto).getItems();
		if (billList != null && !CollectionUtils.isEmpty(billList.getResDtoList())
				&& billList.getResDtoList().size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billModel", billList);
			model.addAttribute("billList", billList.getResDtoList());
		} else {
			model.addAttribute("billModel", billList);
			model.addAttribute("billList", new ArrayList<DistributionBillsReportResDto>());
		}
		return "export/report/distribution/distribution_budget_list";
	}
}
