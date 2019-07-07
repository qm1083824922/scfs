package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.ReceiveReport;
import com.scfs.domain.report.req.ReceiveReportSearchReq;

@Repository
public interface ReceiveReportDao {
	public List<ReceiveReport> queryResultsByCon(ReceiveReportSearchReq receiveReportSearchReq, RowBounds rowBounds);

	public List<ReceiveReport> queryResultsByCon(ReceiveReportSearchReq receiveReportSearchReq);

	public List<ReceiveReport> querySumByCon(ReceiveReportSearchReq receiveReportSearchReq);

}
