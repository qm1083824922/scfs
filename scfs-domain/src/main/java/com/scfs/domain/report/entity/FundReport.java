package com.scfs.domain.report.entity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: FundReport.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月28日				Administrator
 *
 * </pre>
 */
public class FundReport {
	private Integer projectId;
	private Integer accountId;
	private Integer currencyType;
	private BigDecimal beginBalance;
	private BigDecimal payAmount;
	private BigDecimal receiptAmount;
	private BigDecimal curBalance; // 本期余额
	private BigDecimal fundCost;
	private Integer customerId; // 客户
	private Integer capitalAccountType;
	private String departmentId;

	private BigDecimal balance;// 往期余额
	private Integer busiUnit;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
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

	public BigDecimal getCurBalance() {
		return curBalance;
	}

	// public void setCueBalance(BigDecimal curBalance)
	// {
	// this.curBalance = curBalance;
	// }
	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCapitalAccountType() {
		return capitalAccountType;
	}

	public void setCapitalAccountType(Integer capitalAccountType) {
		this.capitalAccountType = capitalAccountType;
	}

	public void setCurBalance(BigDecimal curBalance) {
		this.curBalance = curBalance;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
