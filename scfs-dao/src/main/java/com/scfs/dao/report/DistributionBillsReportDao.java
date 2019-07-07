package com.scfs.dao.report;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.DistributionBillsReport;
import com.scfs.domain.report.req.DistributionBillsReportReqDto;

@Repository
public interface DistributionBillsReportDao {
	List<DistributionBillsReport> queryResultsByCon(DistributionBillsReportReqDto reqDto, RowBounds rowBounds);

	List<DistributionBillsReport> queryResultsByCon(DistributionBillsReportReqDto reqDto);

	DistributionBillsReport queryEntityById(Integer id);

	Date queryOldPayDate(@Param("payTime") String payTime);

	/**
	 * 铺货预算对账单
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<DistributionBillsReport> queryBudgetResultsByCon(DistributionBillsReportReqDto reqDto, RowBounds rowBounds);

	List<DistributionBillsReport> queryBudgetResultsByCon(DistributionBillsReportReqDto reqDto);

}
