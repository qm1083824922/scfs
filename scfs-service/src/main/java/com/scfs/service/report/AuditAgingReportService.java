package com.scfs.service.report;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.AuditAgingReportDao;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.report.entity.AuditAgingReport;
import com.scfs.domain.report.req.AuditAgingReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * @Description: 审核时效报表
 * @author Administrator
 * @date:2017年10月19日下午2:44:14
 * 
 */
@Service
public class AuditAgingReportService {
	@Autowired
	private ReportProjectService reportProjectService;
	@Autowired
	private AuditAgingReportDao auditAgingReportDao;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private CacheService cacheService;

	/**
	 * 查询审核时效报表
	 * 
	 * @param auditAgingReportReqDto
	 * @return
	 */
	public PageResult<AuditAgingReport> queryResultsByCon(AuditAgingReportReqDto auditAgingReportReqDto) {
		auditAgingReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_16));

		PageResult<AuditAgingReport> result = new PageResult<AuditAgingReport>();
		int offSet = PageUtil.getOffSet(auditAgingReportReqDto.getPage(), auditAgingReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, auditAgingReportReqDto.getPer_page());

		auditAgingReportReqDto.setUserId(ServiceSupport.getUser().getId());
		List<AuditAgingReport> auditAgingReportList = auditAgingReportDao
				.queryAuditAgingReportResultsByCon(auditAgingReportReqDto, rowBounds);
		List<AuditAgingReport> auditAgingReportResList = convertToResDto(auditAgingReportList);
		result.setItems(auditAgingReportResList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), auditAgingReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(auditAgingReportReqDto.getPage());
		result.setPer_page(auditAgingReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 查询审核时效报表(全部数据)
	 * 
	 * @param auditAgingReportReqDto
	 * @return
	 */
	public List<AuditAgingReport> queryAllResultsByCon(AuditAgingReportReqDto auditAgingReportReqDto) {
		if (null == auditAgingReportReqDto.getUserId()) {
			auditAgingReportReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<AuditAgingReport> auditAgingReportList = auditAgingReportDao
				.queryAuditAgingReportResultsByCon(auditAgingReportReqDto);
		List<AuditAgingReport> auditAgingReportResList = convertToResDto(auditAgingReportList);
		return auditAgingReportResList;
	}

	private List<AuditAgingReport> convertToResDto(List<AuditAgingReport> auditAgingReportList) {
		List<AuditAgingReport> auditAgingReportResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(auditAgingReportList)) {
			return auditAgingReportResList;
		}
		for (AuditAgingReport auditAgingReport : auditAgingReportList) {
			AuditAgingReport auditAgingReportRes = convertToResDto(auditAgingReport);
			auditAgingReportResList.add(auditAgingReportRes);
		}
		return auditAgingReportResList;
	}

	private AuditAgingReport convertToResDto(AuditAgingReport auditAgingReport) {
		AuditAgingReport auditAgingReportRes = new AuditAgingReport();
		BeanUtils.copyProperties(auditAgingReport, auditAgingReportRes);
		if (null != auditAgingReport.getBillType()) {
			auditAgingReportRes.setBillTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_POTYPE, auditAgingReport.getBillType() + ""));
		}
		if (null != auditAgingReport.getAuditorId()) {
			auditAgingReportRes.setAuditor(cacheService.getUserChineseNameByid(auditAgingReport.getAuditorId()));
		}
		auditAgingReportRes.setAuditDuration(DateFormatUtils.getBetweenTime(auditAgingReportRes.getArrivedDate(),
				auditAgingReportRes.getAuditPassDate()));
		auditAgingReportRes.setAuditAgeDuration(DateFormatUtils.getBetweenHours(auditAgingReportRes.getArrivedDate(),
				auditAgingReportRes.getAuditPassDate()));
		return auditAgingReportRes;
	}

	public boolean isOverAuditAgingReportMaxLine(AuditAgingReportReqDto auditAgingReportReqDto) {
		auditAgingReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = auditAgingReportDao.queryAuditAgingReportCountByCon(auditAgingReportReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("审核时效报表导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncAuditAgingReportExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/report/auditAging/auditAging_report_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_44);
			asyncExcelService.addAsyncExcel(auditAgingReportReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncAuditAgingReportExport(AuditAgingReportReqDto auditAgingReportReqDto) {
		auditAgingReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_16));

		Map<String, Object> model = Maps.newHashMap();
		List<AuditAgingReport> auditAgingReportList = queryAllResultsByCon(auditAgingReportReqDto);
		model.put("auditAgingReportList", auditAgingReportList);
		return model;
	}
}
