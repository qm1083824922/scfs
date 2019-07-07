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
import com.scfs.domain.report.req.ReceiveReportSearchReq;
import com.scfs.domain.report.resp.ReceiveLineReportResDto;
import com.scfs.domain.report.resp.ReceiveReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.ReceiveReportService;

/**
 * <pre>
 * 
 *  File: ReceiveReportController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月14日				Administrator
 *
 * </pre>
 */

@Controller
public class ReceiveReportController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReceiveReportController.class);

	@Autowired
	private ReceiveReportService receiveReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_RECEIVE_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveReportResDto> queryResultByCon(ReceiveReportSearchReq receiveReportSearchReq) {
		PageResult<ReceiveReportResDto> pageResult = new PageResult<ReceiveReportResDto>();
		try {
			pageResult = receiveReportService.queryResultByCon(receiveReportSearchReq);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			pageResult.setMsg("应收报表查询异常,请稍后再试");
			LOGGER.error("应收报表查询异常[{}]: {}", JSONObject.toJSON(receiveReportSearchReq), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_RECEIVE_DETAIL_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveLineReportResDto> queryResultDetailByCon(ReceiveReportSearchReq receiveReportSearchReq) {
		PageResult<ReceiveLineReportResDto> pageResult = new PageResult<ReceiveLineReportResDto>();
		try {
			pageResult = receiveReportService.queryResultDetailByCon(receiveReportSearchReq);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			pageResult.setMsg("应收报表查询异常,请稍后再试");
			LOGGER.error("应收报表查询异常[{}]: {}", JSONObject.toJSON(receiveReportSearchReq), e);
		}
		return pageResult;
	}

	/**
	 * 应收报表导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_RECEIVE_REPORT, method = RequestMethod.GET)
	public String exportRecFeeExcel(ModelMap model, ReceiveReportSearchReq req) {
		List<ReceiveReportResDto> result = receiveReportService.queryListByCon(req);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("receiveReportList", result);
		} else {
			model.addAttribute("receiveReportList", new ArrayList<ReceiveReportResDto>());
		}
		return "export/report/receive/receive_report_list";
	}

}
