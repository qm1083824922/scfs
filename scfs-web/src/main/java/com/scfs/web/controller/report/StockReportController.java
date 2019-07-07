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
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.report.req.StockReportReqDto;
import com.scfs.domain.report.resp.StockReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.StockReportService;

/**
 * <pre>
 * 
 *  File: StockReportController.java
 *  Description: 库存报表
 *  TODO
 *  Date,					Who,				
 *  2017年2月17日				Administrator
 *
 * </pre>
 */
@Controller
public class StockReportController {
	private final static Logger LOGGER = LoggerFactory.getLogger(StockReportController.class);

	@Autowired
	private StockReportService stockReportService;

	/**
	 * 库存报表列表
	 * 
	 * @param stockReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_STOCK_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StockReportResDto> queryResultByCon(StockReportReqDto stockReportReqDto) {
		PageResult<StockReportResDto> pageResult = new PageResult<StockReportResDto>();
		try {
			pageResult = stockReportService.queryResultByCon(stockReportReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			pageResult.setMsg("库存报表查询异常,请稍后再试");
			LOGGER.error("库存报表查询异常[{}]: {}", JSONObject.toJSON(stockReportReqDto), e);
		}
		return pageResult;
	}

	/**
	 * 库存商品列表
	 * 
	 * @param stockReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_STOCK_GOODS_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StockReportResDto> queryResultDetialByCon(StockReportReqDto stockReportReqDto) {
		PageResult<StockReportResDto> pageResult = new PageResult<StockReportResDto>();
		try {
			pageResult = stockReportService.queryResultDetialByCon(stockReportReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			pageResult.setMsg("库存报表查询异常,请稍后再试");
			LOGGER.error("库存报表查询异常[{}]: {}", JSONObject.toJSON(stockReportReqDto), e);
		}
		return pageResult;
	}

	/**
	 * 导出数据
	 * 
	 * @param model
	 * @param stockReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_STOCK_REPORT, method = RequestMethod.GET)
	public String exportPayOrderExcel(ModelMap model, StockReportReqDto stockReportReqDto) {
		List<StockReportResDto> result = stockReportService.queryResultByConExcel(stockReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("stockList", result);
		} else {
			model.addAttribute("stockList", new ArrayList<SaleDtlResult>());
		}
		return "export/report/stock/stock_report_list";
	}
}
