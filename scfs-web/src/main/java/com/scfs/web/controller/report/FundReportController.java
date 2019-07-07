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
import com.scfs.domain.report.req.FundCostBillSearchReqDto;
import com.scfs.domain.report.req.FundReportSearchReqDto;
import com.scfs.domain.report.resp.FundDtlResDto;
import com.scfs.domain.report.resp.FundReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.FundReportService;

/**
 * <pre>
 * 
 *  File: FundReportController.java
 *  Description:            资金统计报表
 *  TODO
 *  Date,					Who,				
 *  2017年3月30日				Administrator
 *
 * </pre>
 */
@Controller
public class FundReportController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FundReportController.class);

	@Autowired
	FundReportService fundReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_FUND_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundReportResDto> queryResultByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		PageResult<FundReportResDto> pageResult = new PageResult<FundReportResDto>();
		try {
			pageResult = fundReportService.queryResultsByCon(fundReportSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
			LOGGER.error("资金统计查询异常[{}]: {}", JSONObject.toJSON(fundReportSearchReqDto), e);
		} catch (Exception e) {
			pageResult.setMsg("资金统计报表查询异常,请稍后再试");
			LOGGER.error("资金统计查询异常[{}]: {}", JSONObject.toJSON(fundReportSearchReqDto), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_FUNDCOST_DTL_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundDtlResDto> queryFundCostDtlByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		PageResult<FundDtlResDto> pageResult = new PageResult<FundDtlResDto>();
		try {
			List<FundDtlResDto> fundDtls = fundReportService.queryFundCostDtlsByCon(fundReportSearchReqDto);
			pageResult.setItems(fundDtls);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
			LOGGER.error("资金成本明细查询异常[{}]: {}", JSONObject.toJSON(fundReportSearchReqDto), e);
		} catch (Exception e) {
			pageResult.setMsg("资金成本明细查询异常,请稍后再试");
			LOGGER.error("资金成本明细查询异常[{}]: {}", JSONObject.toJSON(fundReportSearchReqDto), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_FUND_DTL_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundDtlResDto> queryResultDetailByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		PageResult<FundDtlResDto> pageResult = new PageResult<FundDtlResDto>();
		try {
			List<FundDtlResDto> fundDtls = fundReportService.queryDtlsByCon(fundReportSearchReqDto);
			pageResult.setItems(fundDtls);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
			LOGGER.error("资金统计明细查询异常[{}]: {}", JSONObject.toJSON(fundReportSearchReqDto), e);
		} catch (Exception e) {
			pageResult.setMsg("资金统计明细查询异常,请稍后再试");
			LOGGER.error("资金统计明细查询异常[{}]: {}", JSONObject.toJSON(fundReportSearchReqDto), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_FUNDCOST_BILL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundDtlResDto> queryFundCostBillByCon(FundCostBillSearchReqDto fundCostBillDtlSearchReqDto) {
		PageResult<FundDtlResDto> pageResult = new PageResult<FundDtlResDto>();
		try {
			List<FundDtlResDto> fundDtls = fundReportService.queryFundCostBillDtlByCon(fundCostBillDtlSearchReqDto);
			pageResult.setItems(fundDtls);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
			LOGGER.error("资金成本单据查询异常[{}]: {}", JSONObject.toJSON(fundCostBillDtlSearchReqDto), e);
		} catch (Exception e) {
			pageResult.setMsg("资金成本单据查询异常,请稍后再试");
			LOGGER.error("资金成本单据查询异常[{}]: {}", JSONObject.toJSON(fundCostBillDtlSearchReqDto), e);
		}
		return pageResult;
	}

	/**
	 * 资金统计报表导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_STATISTIC_REPORT, method = RequestMethod.GET)
	public String exportFundReportExcel(ModelMap model, FundReportSearchReqDto req) {
		List<FundReportResDto> result = fundReportService.exportFundReportExcel(req);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("fundReportList", result);
		} else {
			model.addAttribute("fundReportList", new ArrayList<FundReportResDto>());
		}
		return "export/report/fund/fund_report_list";
	}

	/**
	 * 资金成本明细报表导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUNDCOST_DTL, method = RequestMethod.GET)
	public String exportFundCostDtlExcel(ModelMap model, FundReportSearchReqDto req) {
		List<FundDtlResDto> result = fundReportService.queryFundCostDtlsByCon(req);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("fundCostDtlList", result);
		} else {
			model.addAttribute("fundCostDtlList", new ArrayList<FundReportResDto>());
		}
		return "export/report/fund/fund_cost_dtl_list";
	}

	/**
	 * 资金成本单据导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUNDCOST_BILL_REPORT, method = RequestMethod.GET)
	public String exportFundCostBillExcel(ModelMap model, FundCostBillSearchReqDto fundCostBillDtlSearchReqDto) {
		List<FundDtlResDto> result = fundReportService.queryFundCostBillDtlByCon(fundCostBillDtlSearchReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("fundCostBillList", result);
		} else {
			model.addAttribute("fundCostBillList", new ArrayList<FundReportResDto>());
		}
		if (fundCostBillDtlSearchReqDto.getSearchType().equals(BaseConsts.ONE)) {
			return "export/report/fund/fund_cost_pay_list";
		} else {
			return "export/report/fund/fund_cost_receipt_list";
		}
	}
}
