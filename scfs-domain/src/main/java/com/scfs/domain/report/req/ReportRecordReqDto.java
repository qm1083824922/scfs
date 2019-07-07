package com.scfs.domain.report.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年4月13日.
 */
public class ReportRecordReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3876035667170625405L;

	/**
	 * 报表类型 1-绩效报表
	 */
	private Integer reportType;

	/**
	 * 期号
	 */
	private String issue;

	/**
	 * 是否存储成功 0-未成功 1-成功
	 */
	private Integer isSuccess;

	/**
	 * 是否强制生成报表 0-否 1-是
	 */
	private boolean forceExec = false;

	/**
	 * 开始期号
	 */
	private String startIssue;

	/**
	 * 结束期号
	 */
	private String endIssue;

	/**
	 * 是否当月
	 */
	private boolean isCurrentMonth = false;

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isForceExec() {
		return forceExec;
	}

	public void setForceExec(boolean forceExec) {
		this.forceExec = forceExec;
	}

	public String getStartIssue() {
		return startIssue;
	}

	public void setStartIssue(String startIssue) {
		this.startIssue = startIssue;
	}

	public String getEndIssue() {
		return endIssue;
	}

	public void setEndIssue(String endIssue) {
		this.endIssue = endIssue;
	}

	public boolean isCurrentMonth() {
		return isCurrentMonth;
	}

	public void setCurrentMonth(boolean isCurrentMonth) {
		this.isCurrentMonth = isCurrentMonth;
	}

}
