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
import com.scfs.domain.report.entity.AuditAgingReport;
import com.scfs.domain.report.req.AuditAgingReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.AuditAgingReportService;
import com.scfs.web.controller.BaseController;

/**
 * @Description: 审核时效
 * @author Administrator
 * @date:2017年10月19日下午2:29:34
 * 
 */
@Controller
public class AuditAgingReportController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuditAgingReportController.class);
	@Autowired
	private AuditAgingReportService auditAgingReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_AUDIT_AGING_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditAgingReport> queryResultsByCon(AuditAgingReportReqDto auditAgingReportReqDto) {
		PageResult<AuditAgingReport> result = new PageResult<AuditAgingReport>();
		try {
			result = auditAgingReportService.queryResultsByCon(auditAgingReportReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询审核时效异常[{}]", JSONObject.toJSON(auditAgingReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核时效异常[{}]", JSONObject.toJSON(auditAgingReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_AUDIT_AGING_REPORT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportAuditAgingReportCount(AuditAgingReportReqDto auditAgingReportReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = auditAgingReportService.isOverAuditAgingReportMaxLine(auditAgingReportReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(auditAgingReportReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_AUDIT_AGING_REPORT, method = RequestMethod.GET)
	public String exportAuditAgingReport(ModelMap model, AuditAgingReportReqDto auditAgingReportReqDto) {
		List<AuditAgingReport> result = auditAgingReportService.queryAllResultsByCon(auditAgingReportReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("auditAgingReportList", result);
		} else {
			model.addAttribute("auditAgingReportList", new ArrayList<AuditAgingReport>());
		}
		return "export/report/auditAging/auditAging_report_list";
	}
}
