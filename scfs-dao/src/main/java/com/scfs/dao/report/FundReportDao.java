package com.scfs.dao.report;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.FundReport;
import com.scfs.domain.report.req.FundReportSearchReqDto;
import com.scfs.domain.report.resp.FundReportResDto;

@Repository
public interface FundReportDao {
	FundReport queryDataResultByCon(FundReportSearchReqDto fundReportSearchReqDto);

	List<FundReport> queryInitListByCon(FundReportSearchReqDto fundReportSearchReqDto);

	List<FundReport> queryInitListByCon(FundReportSearchReqDto fundReportSearchReqDto, RowBounds rowBounds);

	BigDecimal queryBeginBalanceByCon(FundReportSearchReqDto fundReportSearchReqDto);

	int insertFundReport(FundReportResDto fundReportResDto);

	int queryFundReportCount(FundReportSearchReqDto fundReportSearchReqDto);

	int deleteFundReport(FundReportSearchReqDto fundReportSearchReqDto);

	/**
	 * 
	 * @param fundReportSearchReqDto
	 * @return
	 */
	List<FundReport> queyrFundReportGroupBy(FundReportSearchReqDto fundReportSearchReqDto);

	List<Integer> queyrProjectBy(FundReportSearchReqDto fundReportSearchReqDto);

	/**
	 * 查询独立核算的
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundReport> queryInitListByConByTwo(FundReportSearchReqDto fundReportSearchReqDto, RowBounds rowBounds);

	/**
	 * 查询独立核算的
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundReport> queryInitListByConByTwo(FundReportSearchReqDto fundReportSearchReqDto);

	/**
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundReport> queryInitListByConByCon(FundReportSearchReqDto fundReportSearchReqDto, RowBounds rowBounds);

	/**
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundReport> queryInitListByConByCon(FundReportSearchReqDto fundReportSearchReqDto);

}
