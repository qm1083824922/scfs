package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.StockReport;
import com.scfs.domain.report.req.StockReportReqDto;

@Repository
public interface StockReportDao {
	/**
	 * 查询库存报表信息
	 * 
	 * @param stockReportReqDto
	 * @param rowBounds
	 * @return
	 */
	public List<StockReport> queryResultsByCon(StockReportReqDto stockReportReqDto, RowBounds rowBounds);

	public List<StockReport> queryResultsByCon(StockReportReqDto stockReportReqDto);

	/**
	 * 查询库存商品信息
	 * 
	 * @param stockReportReqDto
	 * @param rowBounds
	 * @return
	 */
	public List<StockReport> queryResultDetialByCon(StockReportReqDto stockReportReqDto, RowBounds rowBounds);
}
