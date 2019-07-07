package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

public class FundTotalReport {
	private Integer id;

	/**
	 * 报表记录表ID
	 */
	private Integer reportRecordId;

	/**
	 * 部门ID
	 */
	private Integer departmentId;

	/**
	 * 部门名称
	 */
	private String departmentName;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 账号ID
	 */
	private Integer accountId;

	/**
	 * 开户账号
	 */
	private String accountNo;

	/**
	 * 币种 1.人民币 2.美元 3.港币 4.欧元
	 */
	private Integer currencyType;

	/**
	 * 币种名称
	 */
	private String currencyName;

	/**
	 * 期初余额
	 */
	private BigDecimal beginBalance;

	/**
	 * 本期付款
	 */
	private BigDecimal payAmount;

	/**
	 * 本期收款
	 */
	private BigDecimal receiptAmount;

	/**
	 * 余额
	 */
	private BigDecimal balance;

	/**
	 * 资金成本
	 */
	private BigDecimal fundCost;

	/**
	 * 期号
	 */
	private String issue;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	private Integer busiUnit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReportRecordId() {
		return reportRecordId;
	}

	public void setReportRecordId(Integer reportRecordId) {
		this.reportRecordId = reportRecordId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName == null ? null : departmentName.trim();
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName == null ? null : projectName.trim();
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo == null ? null : accountNo.trim();
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName == null ? null : currencyName.trim();
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

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue == null ? null : issue.trim();
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}
}