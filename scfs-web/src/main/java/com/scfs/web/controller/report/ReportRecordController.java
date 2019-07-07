package com.scfs.web.controller.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.report.req.ReportRecordReqDto;
import com.scfs.service.report.ReportRecordService;

/**
 * Created by Administrator on 2017年4月13日.
 */
@Controller
public class ReportRecordController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReportRecordController.class);

	@Autowired
	private ReportRecordService reportRecordService;

	@RequestMapping(value = BusUrlConsts.EXEC_REPORT_RECORD, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult reExecReport(ReportRecordReqDto reportRecordReqDto) {
		BaseResult result = new BaseResult();
		try {
			reportRecordService.reExecReport(reportRecordReqDto);
		} catch (BaseException e) {
			LOGGER.error("重新生成绩效报表异常[{}]", reportRecordReqDto, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("重新生成绩效报表异常[{}]", reportRecordReqDto, e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}
}
