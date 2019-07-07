package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.fee.dto.resp.FeeSumModel;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.report.entity.SaleDtlSum;
import com.scfs.domain.report.entity.SaleReport;
import com.scfs.domain.report.entity.SaleReportSum;
import com.scfs.domain.report.req.SaleReportReqDto;

public interface SaleReportDao {

	List<SaleReport> querySaleReportResultsByCon(SaleReportReqDto saleReportReqDto, RowBounds rowBounds);

	List<SaleReport> querySaleReportResultsByCon(SaleReportReqDto saleReportReqDto);

	int querySaleReportCountByCon(SaleReportReqDto saleReportReqDto);

	List<SaleReportSum> querySumSaleReportResultsByCon(SaleReportReqDto saleReportReqDto);

	List<SaleDtlResult> querySaleDtlResultsByCon(SaleReportReqDto saleReportReqDto, RowBounds rowBounds);

	SaleDtlSum querySumSaleDtlResultsByCon(SaleReportReqDto saleReportReqDto);

	List<Fee> queryFeeDtlByCon(SaleReportReqDto saleReportReqDto, RowBounds rowBounds);

	List<FeeSumModel> queryFeeDtlSumByCon(SaleReportReqDto saleReportReqDto);

}
