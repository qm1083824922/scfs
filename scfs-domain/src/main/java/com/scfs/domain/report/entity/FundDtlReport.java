package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 
 *  File: FundDtlReport.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月28日				Administrator
 *
 * </pre>
 */
public class FundDtlReport {
	private Integer projectId;
	private Integer accountId;
	private Integer currencyType;
	private BigDecimal payAmount;
	private BigDecimal receiptAmount;
	private BigDecimal curBalance;
	private String billNo;
	private Integer billType;
	private Date billDate;
	private Integer billId;
	private Integer receiptType;
	private Integer payType;
	private Integer payState;
	private Integer receiptState;
	private BigDecimal beginBalance;
	private Integer capitalAccountType;
	private Integer busiUnit;

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

	public void setCurBalance(BigDecimal curBalance) {
		this.curBalance = curBalance;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public Integer getReceiptState() {
		return receiptState;
	}

	public void setReceiptState(Integer receiptState) {
		this.receiptState = receiptState;
	}

	public Integer getCapitalAccountType() {
		return capitalAccountType;
	}

	public void setCapitalAccountType(Integer capitalAccountType) {
		this.capitalAccountType = capitalAccountType;
	}

	public BigDecimal getBeginBalance() {
		return beginBalance;
	}

	public void setBeginBalance(BigDecimal beginBalance) {
		this.beginBalance = beginBalance;
	}

}
