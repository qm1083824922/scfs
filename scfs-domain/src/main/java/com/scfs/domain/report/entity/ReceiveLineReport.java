package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 
 *  File: ReceiveLineReport.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月17日				Administrator
 *
 * </pre>
 */
public class ReceiveLineReport {
	private Integer billType; // 单据类型
	private String billNo;
	private Date billDate;
	private Integer feeId;
	private Integer outStoreId;
	private Date checkDate; // 对账日期
	private Date expireDate; // 应收到期日期
	private Integer expireDay; // 超期天数
	private Integer adventDay; // 临期天数
	private Integer projectId;
	private Integer custId;
	private BigDecimal balance;
	private Integer currencyType;

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public Integer getOutStoreId() {
		return outStoreId;
	}

	public void setOutStoreId(Integer outStoreId) {
		this.outStoreId = outStoreId;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(Integer expireDay) {
		this.expireDay = expireDay;
	}

	public Integer getAdventDay() {
		return adventDay;
	}

	public void setAdventDay(Integer adventDay) {
		this.adventDay = adventDay;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}
}
