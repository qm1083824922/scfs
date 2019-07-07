package com.scfs.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.dao.report.ProfitReportDao;
import com.scfs.domain.report.entity.ProfitReport;
import com.scfs.domain.report.req.ProfitReportReqDto;

/**
 * Created by Administrator on 2017年4月20日.
 */
@Service
public class ProfitReportService {
	@Autowired
	private ProfitReportDao profitReportDao;

	public void deleteProfitReport(ProfitReportReqDto profitReportReqDto) {
		profitReportDao.deleteProfitReport(profitReportReqDto);
	}

	public void insert(ProfitReport profitReport) {
		profitReportDao.insert(profitReport);
	}

	public int queryProfitReportCount(ProfitReportReqDto profitReportReqDto) {
		return profitReportDao.queryProfitReportCount(profitReportReqDto);
	}

}
