package com.scfs.web.controller.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.common.dto.req.ReportProjectReqDto;
import com.scfs.domain.common.entity.ReportProject;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.ReportProjectService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2017年6月9日.
 */
@Controller
public class ReportProjectController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReportProjectController.class);
	@Autowired
	private ReportProjectService reportProjectService;

	@RequestMapping(value = BusUrlConsts.ADD_REPORT_FILTER_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> addReportProject(ReportProjectReqDto reportProjectReqDto) {
		Result<Object> result = new Result<Object>();
		try {
			reportProjectService.addReportProject(reportProjectReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增报表过滤信息异常[{}]", reportProjectReqDto, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增报表过滤信息异常[{}]", reportProjectReqDto, e);
			result.setSuccess(false);
			result.setMsg("新增报表过滤信息失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_REPORT_FILTER_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReportProject> queryReportProject(ReportProjectReqDto reportProjectReqDto) {
		PageResult<ReportProject> result = new PageResult<ReportProject>();
		try {
			List<ReportProject> reportProjectList = reportProjectService.queryReportProject(reportProjectReqDto);
			result.setItems(reportProjectList);
		} catch (BaseException e) {
			LOGGER.error("查询报表过滤信息异常[{}]", reportProjectReqDto, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询报表过滤信息异常[{}]", reportProjectReqDto, e);
			result.setSuccess(false);
			result.setMsg("查询报表过滤信息失败，请重试");
		}
		return result;
	}
}
