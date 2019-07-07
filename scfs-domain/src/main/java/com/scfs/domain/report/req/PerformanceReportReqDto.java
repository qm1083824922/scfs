package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年3月18日.
 */
public class PerformanceReportReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6242491866623223670L;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 业务员ID
	 */
	private Integer bizManagerId;
	/**
	 * 部门ID
	 */
	private List<Integer> departmentId;
	/**
	 * 开始期号
	 */
	private String startIssue;
	/**
	 * 结束期号
	 */
	private String endIssue;
	/**
	 * 期号
	 */
	private String issue;
	/**
	 * 业务类型
	 */
	private Integer bizType;
	/**
	 * 单据类型
	 */
	private Integer billType;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 经营单位
	 */
	private Integer businessUnitId;
	/**
	 * 报表记录表ID
	 */
	private Integer reportRecordId;
	/**
	 * 往期开始期号
	 */
	private String pastStartIssue;
	/**
	 * 往期结束期号
	 */
	private String pastEndIssue;
	/**
	 * 排除类型为资金成本的数据
	 */
	public Integer excludeFundCost;
	/**
	 * 期号类型 1-当前 2-往期
	 */
	public Integer periodType;
	/**
	 * 开始统计时间
	 */
	public String startStatisticsDate;
	/**
	 * 结束统计时间
	 */
	public String endStatisticsDate;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public List<Integer> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
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

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

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

	public Integer getExcludeFundCost() {
		return excludeFundCost;
	}

	public void setExcludeFundCost(Integer excludeFundCost) {
		this.excludeFundCost = excludeFundCost;
	}

	public String getPastStartIssue() {
		return pastStartIssue;
	}

	public void setPastStartIssue(String pastStartIssue) {
		this.pastStartIssue = pastStartIssue;
	}

	public String getPastEndIssue() {
		return pastEndIssue;
	}

	public void setPastEndIssue(String pastEndIssue) {
		this.pastEndIssue = pastEndIssue;
	}

	public Integer getPeriodType() {
		return periodType;
	}

	public void setPeriodType(Integer periodType) {
		this.periodType = periodType;
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

}
