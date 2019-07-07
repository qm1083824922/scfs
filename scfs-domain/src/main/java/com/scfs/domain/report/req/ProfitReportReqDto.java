package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年4月21日.
 */
public class ProfitReportReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6795757686907917753L;
	/**
	 * 报表记录表ID
	 */
	private Integer reportRecordId;
	/**
	 * 期号
	 */
	private String issue;
	/**
	 * 开始统计时间
	 */
	private String startStatisticsDate;
	/**
	 * 结束统计时间
	 */
	/**
	 * 部门ID
	 */
	private Integer departmentId;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	private String endStatisticsDate;

	private List<Integer> projectList;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getReportRecordId() {
		return reportRecordId;
	}

	public void setReportRecordId(Integer reportRecordId) {
		this.reportRecordId = reportRecordId;
	}

	public String getStartStatisticsDate() {
		return startStatisticsDate;
	}

	public void setStartStatisticsDate(String startStatisticsDate) {
		this.startStatisticsDate = startStatisticsDate;
	}

	public String getEndStatisticsDate() {
		return endStatisticsDate;
	}

	public void setEndStatisticsDate(String endStatisticsDate) {
		this.endStatisticsDate = endStatisticsDate;
	}

	public List<Integer> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Integer> projectList) {
		this.projectList = projectList;
	}

}
