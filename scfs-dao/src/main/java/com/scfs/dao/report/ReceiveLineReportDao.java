package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.ReceiveLineReport;
import com.scfs.domain.report.req.ReceiveReportSearchReq;

@Repository
public interface ReceiveLineReportDao {
	public List<ReceiveLineReport> queryResultDetialByCon(ReceiveReportSearchReq receiveReportSearchReq,
			RowBounds rowBounds);

	public List<ReceiveLineReport> querySumByCon(ReceiveReportSearchReq receiveReportSearchReq);

}
