package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.report.entity.AuditAgingReport;
import com.scfs.domain.report.req.AuditAgingReportReqDto;

public interface AuditAgingReportDao {
	List<AuditAgingReport> queryAuditAgingReportResultsByCon(AuditAgingReportReqDto auditAgingReportReqDto,
			RowBounds rowBounds);

	List<AuditAgingReport> queryAuditAgingReportResultsByCon(AuditAgingReportReqDto auditAgingReportReqDto);

	int queryAuditAgingReportCountByCon(AuditAgingReportReqDto auditAgingReportReqDto);

}
