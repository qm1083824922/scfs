package com.scfs.domain.common.dto.req;

import java.util.List;

/**
 * Created by Administrator on 2017年6月10日.
 */
public class ReportProjectReqDto {
	
	private Integer projectId;
	
	private List<Integer> reportTypeList;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public List<Integer> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<Integer> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}	

}

