package com.scfs.dao.report;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.report.entity.FundTotalReport;
import com.scfs.domain.report.req.FundReportSearchReqDto;

public interface FundTotalReportDao {
	int deleteById(Integer id);

	int insert(FundTotalReport record);

	FundTotalReport selectById(Integer id);

	List<FundTotalReport> queryResultsByCon(FundReportSearchReqDto fundReportSearchReqDto, RowBounds rowBounds);

	List<FundTotalReport> queryResultsByCon(FundReportSearchReqDto fundReportSearchReqDto);

	int batchInsert(List<FundTotalReport> items);

	int updateById(FundTotalReport record);

	/**
	 * 查询维度为2 类型为2
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundTotalReport> queryResultsByStaTwo(FundReportSearchReqDto fundReportSearchReqDto, RowBounds rowBounds);

	/**
	 * 查询维度为2 类型为2
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundTotalReport> queryResultsByStaTwo(FundReportSearchReqDto fundReportSearchReqDto);

	/**
	 * 占用类型为空
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundTotalReport> queryResultsBySta(FundReportSearchReqDto fundReportSearchReqDto, RowBounds rowBounds);

	/**
	 * 占用类型为空
	 * 
	 * @param fundReportSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<FundTotalReport> queryResultsBySta(FundReportSearchReqDto fundReportSearchReqDto);
}