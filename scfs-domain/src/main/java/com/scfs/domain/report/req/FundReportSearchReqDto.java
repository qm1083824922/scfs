package com.scfs.domain.report.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: FundReportSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月28日				Administrator
 *
 * </pre>
 */
public class FundReportSearchReqDto extends BaseReqDto {
	private static final long serialVersionUID = 6204033276438295918L;
	/** 部门id */
	private String departmentId;
	/** 项目id */
	private Integer projectId;
	/** 账户id */
	private Integer accountId;
	/** 统计维度 */
	private Integer statisticsDimension;
	/** 币种 */
	private Integer currencyType;
	/** 本期开始日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/** 本期截止日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/** 业务员ID */
	private Integer bizManagerId;
	/** 经营单位 */
	private Integer businessUnitId;
	/** 所属部门 */
	private List<String> departmentList;
	/** 资金占用类型 */
	private Integer capitalAccountType;
	/** 当期和往期查询 */
	private Integer periodType;
	/** 开始期号 */
	private String startPeriod;
	/** 截止期号 */
	private String endPeriod;
	/** 报表记录表ID */
	private Integer reportRecordId;
	/** 期号 */
	private String issue;
	/**
	 * 角色
	 */
	private Integer roleId;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getStatisticsDimension() {
		return statisticsDimension;
	}

	public void setStatisticsDimension(Integer statisticsDimension) {
		this.statisticsDimension = statisticsDimension;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<String> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<String> departmentList) {
		this.departmentList = departmentList;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getCapitalAccountType() {
		return capitalAccountType;
	}

	public void setCapitalAccountType(Integer capitalAccountType) {
		this.capitalAccountType = capitalAccountType;
	}

	public Integer getPeriodType() {
		return periodType;
	}

	public void setPeriodType(Integer periodType) {
		this.periodType = periodType;
	}

	public String getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(String startPeriod) {
		this.startPeriod = startPeriod;
	}

	public String getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(String endPeriod) {
		this.endPeriod = endPeriod;
	}

	public Integer getReportRecordId() {
		return reportRecordId;
	}

	public void setReportRecordId(Integer reportRecordId) {
		this.reportRecordId = reportRecordId;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
