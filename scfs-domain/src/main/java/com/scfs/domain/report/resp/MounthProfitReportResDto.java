package com.scfs.domain.report.resp;

import java.util.List;

import com.scfs.domain.report.entity.MounthProfitReport;

/**
 * <pre>
 * 
 *  File: MounthProfitReportResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年6月10日				Administrator
 *
 * </pre>
 */
public class MounthProfitReportResDto {

	private List<MounthProfitReport> mounthData;

	public List<String> monthList;

	public List<MounthProfitReport> getMounthData() {
		return mounthData;
	}

	public void setMounthData(List<MounthProfitReport> mounthData) {
		this.mounthData = mounthData;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

}
