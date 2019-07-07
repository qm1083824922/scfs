package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scfs.domain.report.entity.ReportRecord;
import com.scfs.domain.report.req.ReportRecordReqDto;

public interface ReportRecordDao {
	int deleteById(Integer id);

	int insert(ReportRecord reportRecord);

	ReportRecord queryEntityById(Integer id);

	int updateById(ReportRecord reportRecord);

	List<ReportRecord> queryResultsByCon(ReportRecordReqDto reportRecordReqDto);

	int queryReportRecordCount(ReportRecordReqDto reportRecordReqDto);

	ReportRecord queryReportRecordByIssue(@Param("reportType") Integer reportType, @Param("issue") String issue);

}