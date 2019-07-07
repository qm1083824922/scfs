package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.entity.OrderProfitReport;
import com.scfs.domain.report.entity.OrderProfitReportSum;
import com.scfs.domain.report.req.OrderProfitReportReqDto;

public interface OrderProfitReportDao {
	List<OrderProfitReport> queryOrderProfitReportResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto,
			RowBounds rowBounds);

	List<OrderProfitReport> queryOrderProfitReportResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto);

	int queryOrderProfitReportCountByCon(OrderProfitReportReqDto orderProfitReportReqDto);

	List<OrderProfitReportSum> querySumOrderProfitReportByCon(OrderProfitReportReqDto orderProfitReportReqDto);

	/**
	 * 利润月结报表
	 * 
	 * @param orderProfitReportReqDto
	 * @param rowBounds
	 * @return
	 */
	List<MounthProfitReport> queryProfitReportMounthResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto,
			RowBounds rowBounds);

	List<MounthProfitReport> queryProfitReportMounthResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto);

	int queryMountProfitReportCountByCon(OrderProfitReportReqDto orderProfitReportReqDto);

	OrderProfitReport querySaleAmount(OrderProfitReportReqDto orderProfitReportReqDto);
}
