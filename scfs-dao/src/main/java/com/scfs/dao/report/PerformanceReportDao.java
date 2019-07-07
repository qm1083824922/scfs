package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.report.entity.PerformanceReport;
import com.scfs.domain.report.entity.PerformanceReportSum;
import com.scfs.domain.report.req.PerformanceReportReqDto;

public interface PerformanceReportDao {
	int deleteById(Integer id);

	int insert(PerformanceReport performanceReport);

	PerformanceReport queryEntityById(Integer id);

	int updateById(PerformanceReport performanceReport);

	List<PerformanceReport> queryPerformanceReportResultsByCon(PerformanceReportReqDto performanceReportReqDto,
			RowBounds rowBounds);

	List<PerformanceReport> queryPerformanceReportResultsByCon(PerformanceReportReqDto performanceReportReqDto);

	int queryPerformanceReportCountByCon(PerformanceReportReqDto performanceReportReqDto);

	List<PerformanceReportSum> querySumPerformanceReportByCon(PerformanceReportReqDto performanceReportReqDto);

	int queryPerformanceReportCount(PerformanceReportReqDto performanceReportReqDto);

	int deletePerformanceReport(PerformanceReportReqDto performanceReportReqDto);

	List<PerformanceReportSum> querySumPerformanceReportByCon4Store(PerformanceReportReqDto performanceReportReqDto);
}