package com.scfs.dao.report;

import java.util.List;
import com.scfs.domain.report.entity.InvoicingDTlResult;
import com.scfs.domain.report.entity.InvoicingReport;
import com.scfs.domain.report.entity.NumAndAmount;
import com.scfs.domain.report.req.InvoicingReportReqDto;

public interface InvoicingReportDao {
	NumAndAmount queryStartOutByCon(InvoicingReportReqDto invoicingReportReqDto);

	NumAndAmount queryStartInByCon(InvoicingReportReqDto invoicingReportReqDto);

	NumAndAmount queryOutByCon(InvoicingReportReqDto invoicingReportReqDto);

	NumAndAmount queryInByCon(InvoicingReportReqDto invoicingReportReqDto);

	List<InvoicingReport> queryByCon(InvoicingReportReqDto invoicingReportReqDto);

	List<InvoicingReportReqDto> queryGoodsIdByCon(InvoicingReportReqDto invoicingReportReqDto);

	List<InvoicingDTlResult> queryInfoDtl(InvoicingReportReqDto invoicingReportReqDto);

	Integer querySumNumber(InvoicingReportReqDto invoicingReportReqDto);
}
