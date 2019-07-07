package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: FundReportResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月28日				Administrator
 *
 * </pre>
 */
public class FundReportResDto {
	private String departmentName;
	private String projectName;
	private String accountNo;
	private String accountTypeName;
	private String currencyTypeName;
	private BigDecimal beginBalance;
	private BigDecimal payAmount;

	private BigDecimal receiptAmount;

	private BigDecimal balance; // 本期余额+期初余额
	private BigDecimal fundCost;
	private Integer projectId;
	private Integer departmentId;
	private Integer accountId;
	private Integer currencyType;
	private String capitalAccountTypeName;
	private Integer capitalAccountType;

	private Integer reportRecordId;
	/** 客户ID */
	private Integer customerId;
	/** 业务员ID */
	private Integer bizManagerId;
	/** 业务类型 */
	private Integer bizType;
	/** 经营单位ID */
	private Integer businessUnitId;
	/** 期号 */
	private String issue;
	/** 开始期号日期 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startIssueDate;
	/** 结束期号日期 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endIssueDate;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getBeginBalance() {
		return beginBalance;
	}

	public void setBeginBalance(BigDecimal beginBalance) {
		this.beginBalance = beginBalance;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
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

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getCapitalAccountTypeName() {
		return capitalAccountTypeName;
	}

	public void setCapitalAccountTypeName(String capitalAccountTypeName) {
		this.capitalAccountTypeName = capitalAccountTypeName;
	}

	public Integer getCapitalAccountType() {
		return capitalAccountType;
	}

	public void setCapitalAccountType(Integer capitalAccountType) {
		this.capitalAccountType = capitalAccountType;
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

	public Date getStartIssueDate() {
		return startIssueDate;
	}

	public void setStartIssueDate(Date startIssueDate) {
		this.startIssueDate = startIssueDate;
	}

	public Date getEndIssueDate() {
		return endIssueDate;
	}

	public void setEndIssueDate(Date endIssueDate) {
		this.endIssueDate = endIssueDate;
	}

}
