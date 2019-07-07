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
import com.scfs.domain.BaseResult;
import com.scfs.domain.report.entity.PerformanceReport;
import com.scfs.domain.report.req.PerformanceReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.PerformanceReportService;

/**
 * Created by Administrator on 2017年4月12日.
 */
@Controller
public class PerformanceReportController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceReportController.class);

	@Autowired
	private PerformanceReportService performanceReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_PERFORMANCE_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PerformanceReport> queryResultsByCon(PerformanceReportReqDto performanceReportReqDto) {
		PageResult<PerformanceReport> result = new PageResult<PerformanceReport>();
		try {
			result = performanceReportService.queryResultsByCon(performanceReportReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询订单利润报表异常[{}]", JSONObject.toJSON(performanceReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询订单利润报表异常[{}]", JSONObject.toJSON(performanceReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PERFORMANCE_REPORT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPerformanceReportCount(PerformanceReportReqDto performanceReportReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = performanceReportService.isOverPerformanceReportMaxLine(performanceReportReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(performanceReportReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PERFORMANCE_REPORT, method = RequestMethod.GET)
	public String exportPerformanceReport(ModelMap model, PerformanceReportReqDto performanceReportReqDto) {
		List<PerformanceReport> result = performanceReportService.queryAllResultsByCon(performanceReportReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("performanceReportList", result);
		} else {
			model.addAttribute("performanceReportList", new ArrayList<PerformanceReport>());
		}
		return "export/report/performance/performance_report_list";
	}
}
