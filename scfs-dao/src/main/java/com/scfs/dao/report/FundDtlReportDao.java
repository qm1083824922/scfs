package com.scfs.dao.report;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.FundDtlReport;
import com.scfs.domain.report.req.FundCostBillSearchReqDto;
import com.scfs.domain.report.req.FundReportSearchReqDto;

@Repository
public interface FundDtlReportDao {
	public List<FundDtlReport> queryFundDtlsByCon(FundReportSearchReqDto fundReportSearchReqDto);

	/**
	 * 废弃
	 * 
	 * @param fundReportSearchReqDto
	 * @return
	 */
	public List<FundDtlReport> queryFundDtlsGroupByDate(FundReportSearchReqDto fundReportSearchReqDto);

	// 根据日期分组查询对应的详细信息
	public List<FundDtlReport> queryFundDtlListByDate(FundReportSearchReqDto fundReportSearchReqDto);

	public List<FundDtlReport> queryFundCostPayByCon(FundCostBillSearchReqDto fundCostBillDtlSearchReqDto);

	public List<FundDtlReport> queryFundCostReceiptByCon(FundCostBillSearchReqDto fundCostBillDtlSearchReqDto);

}
