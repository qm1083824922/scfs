package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.GoodsInRepot;
import com.scfs.domain.report.entity.GoodsOutReport;
import com.scfs.domain.report.entity.GoodsPlReport;
import com.scfs.domain.report.entity.GoodsReport;
import com.scfs.domain.report.entity.GoodsRtReport;
import com.scfs.domain.report.entity.GoodsStlReport;
import com.scfs.domain.report.req.GoodsReportReqDto;

@Repository
public interface GoodsReportDto {

	/**
	 * 查询当前铺货进销存数据
	 * 
	 * @param reportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<GoodsReport> queryResultsByCon(GoodsReportReqDto reportReqDto, RowBounds rowBounds);

	/**
	 * 查询当前铺货进销存数据
	 * 
	 * @param reportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<GoodsReport> queryResultsByCon(GoodsReportReqDto reportReqDto);

	/**
	 * 根据SKU和项目等信息进行入库查询
	 * 
	 * @param reportReqDto
	 * @return
	 */
	List<GoodsInRepot> queryPmsInBySku(GoodsReportReqDto reportReqDto, RowBounds rowBounds);

	List<GoodsInRepot> queryPmsInBySku(GoodsReportReqDto reportReqDto);

	/**
	 * 根据SKU等信息查询PMS出库数据
	 * 
	 * @param reportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<GoodsOutReport> queryPmsOutReport(GoodsReportReqDto reportReqDto, RowBounds rowBounds);

	List<GoodsOutReport> queryPmsOutReport(GoodsReportReqDto reportReqDto);

	/**
	 * 根据Sku等其他条件查询请款数据
	 * 
	 * @param reportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<GoodsPlReport> queryPmsPlReport(GoodsReportReqDto reportReqDto, RowBounds rowBounds);

	List<GoodsPlReport> queryPmsPlReport(GoodsReportReqDto reportReqDto);

	/**
	 * 根据Sku等其他条件查询退货数据
	 * 
	 * @param reportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<GoodsRtReport> queryPmsRtReport(GoodsReportReqDto reportReqDto, RowBounds rowBounds);

	List<GoodsRtReport> queryPmsRtReport(GoodsReportReqDto reportReqDto);

	/**
	 * 根据Sku等其他条件查询库存数据
	 * 
	 * @param reportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<GoodsStlReport> queryStlReport(GoodsReportReqDto reportReqDto, RowBounds rowBounds);

}
