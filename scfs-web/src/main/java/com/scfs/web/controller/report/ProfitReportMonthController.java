package com.scfs.web.controller.report;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.report.resp.MounthProfitReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.report.ProfitReportMonthService;

/**
 * <pre>
 * 
 *  File: ProfitReportMonthController.java
 *  Description: 月报表相关
 *  TODO
 *  Date,					Who,				
 *  2017年06月07日				Administrator
 *
 * </pre>
 */
@Controller
public class ProfitReportMonthController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProfitReportMonthController.class);

	@Autowired
	private ProfitReportMonthService profitReportMonthService;

	/**
	 * 获取信息
	 * 
	 * @param mounthDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MONTH, method = RequestMethod.POST)
	@ResponseBody
	public Result<MounthProfitReport> detailMounthProfitReport(ProfitReportReqMonthDto mounthDto) {
		Result<MounthProfitReport> result = new Result<MounthProfitReport>();
		try {
			result = profitReportMonthService.queryMounthIndex(mounthDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取信息失败[{}]", JSONObject.toJSON(mounthDto), e);
			result.setMsg("获取信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取类型分组
	 * 
	 * @param mounthDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MONTH_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public List<MounthProfitReport> queryMounthProfit(ProfitReportReqMonthDto mounthDto) {
		return profitReportMonthService.queryMounthProfit(mounthDto);
	}

	/**
	 * 获取进一年数据
	 * 
	 * @param mounthDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MONTH_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public MounthProfitReportResDto queryMounthYear(ProfitReportReqMonthDto mounthDto) {
		return profitReportMonthService.queryMounthYearByCon(mounthDto);
	}

	/**
	 * 获取当月相关数据
	 * 
	 * @param mounthDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MONTH_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public Result<MounthProfitReport> detailMounthNow(ProfitReportReqMonthDto mounthDto) {
		Result<MounthProfitReport> result = new Result<MounthProfitReport>();
		try {
			result = profitReportMonthService.queryMounthByCom(mounthDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取信息失败[{}]", JSONObject.toJSON(mounthDto), e);
			result.setMsg("获取信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取相关利润占比数据
	 * 
	 * @param mounthDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MONTH_PIE, method = RequestMethod.POST)
	@ResponseBody
	public Result<MounthProfitReport> detailProfitPro(ProfitReportReqMonthDto mounthDto) {
		Result<MounthProfitReport> result = new Result<MounthProfitReport>();
		try {
			result = profitReportMonthService.detailProfitPro(mounthDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取信息失败[{}]", JSONObject.toJSON(mounthDto), e);
			result.setMsg("获取信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取项目指标
	 * 
	 * @param mounthDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GROUP_MONTH_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MounthProfitReport> queryPrijectMounthProfit(ProfitReportReqMonthDto mounthDto) {
		PageResult<MounthProfitReport> pageResult = new PageResult<MounthProfitReport>();
		try {
			// 获取当前访问的微信渠道标识
			pageResult = profitReportMonthService.queryProjectMonth(mounthDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(mounthDto), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}
}
