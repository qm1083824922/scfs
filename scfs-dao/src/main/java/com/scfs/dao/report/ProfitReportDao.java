package com.scfs.dao.report;

import java.math.BigDecimal;

import com.scfs.domain.report.entity.ProfitReport;
import com.scfs.domain.report.req.ProfitReportReqDto;

public interface ProfitReportDao {
	int deleteById(Integer id);

	int insert(ProfitReport profitReport);

	ProfitReport queryEntityById(Integer id);

	int updateById(ProfitReport profitReport);

	int deleteProfitReport(ProfitReportReqDto profitReportReqDto);

	int queryProfitReportCount(ProfitReportReqDto profitReportReqDto);

	BigDecimal querySaleAmount(ProfitReportReqDto profitReportReqDto);
}