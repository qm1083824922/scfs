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
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.entity.OrderProfitReport;
import com.scfs.domain.report.req.OrderProfitReportReqDto;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.OrderProfitReportService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2017年3月18日.
 */
@Controller
public class OrderProfitReportController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(OrderProfitReportController.class);

	@Autowired
	private OrderProfitReportService orderProfitReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_ORDER_PROFIT_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<OrderProfitReport> queryResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto) {
		PageResult<OrderProfitReport> result = new PageResult<OrderProfitReport>();
		try {
			result = orderProfitReportService.queryResultsByCon(orderProfitReportReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询订单利润报表异常[{}]", JSONObject.toJSON(orderProfitReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询订单利润报表异常[{}]", JSONObject.toJSON(orderProfitReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_ORDER_PROFIT_REPORT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportOrderProfitReportCount(OrderProfitReportReqDto orderProfitReportReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = orderProfitReportService.isOverOrderProfitReportMaxLine(orderProfitReportReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(orderProfitReportReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_ORDER_PROFIT_REPORT, method = RequestMethod.GET)
	public String exportOrderProfitReport(ModelMap model, OrderProfitReportReqDto orderProfitReportReqDto) {
		List<OrderProfitReport> result = orderProfitReportService.queryAllResultsByCon(orderProfitReportReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("orderProfitReportList", result);
		} else {
			model.addAttribute("orderProfitReportList", new ArrayList<OrderProfitReport>());
		}
		return "export/report/orderProfit/order_profit_report_list";
	}

	/**
	 * 月结利润信息查询
	 * 
	 * @param orderProfitReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MOUNTH_PROFIT_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MounthProfitReport> queryMounthResultsByCon(ProfitReportReqMonthDto profitReportReqMonthDto) {
		PageResult<MounthProfitReport> result = new PageResult<MounthProfitReport>();
		try {
			result = orderProfitReportService.queryMounthResultsByCon(profitReportReqMonthDto);
		} catch (BaseException e) {
			LOGGER.error("查询订单月利润报表异常[{}]", JSONObject.toJSON(profitReportReqMonthDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询订单月利润报表异常[{}]", JSONObject.toJSON(profitReportReqMonthDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_MOUNTH_PROFIT_REPORT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportMounthProfitReportCount(ProfitReportReqMonthDto profitReportReqMonthDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = orderProfitReportService.isOverMountProfitReportMaxLine(profitReportReqMonthDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(profitReportReqMonthDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_MOUNTH_PROFIT_REPORT, method = RequestMethod.GET)
	public String exportMounthProfitReport(ModelMap model, ProfitReportReqMonthDto profitReportReqMounthDto) {
		List<MounthProfitReport> result = orderProfitReportService.queryAllMountResultsByCon(profitReportReqMounthDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("mounthProfitReportList", result);
		} else {
			model.addAttribute("mounthProfitReportList", new ArrayList<OrderProfitReport>());
		}
		return "export/report/orderProfit/mounth_profit_report_list";
	}
}
