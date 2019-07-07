package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 
 *  File: ReceiveReport.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月14日				Administrator
 *
 * </pre>
 */
public class ReceiveReport {
	private Integer departmentId;
	private Integer projectId;
	private Integer custId;
	private Integer busiManagerId;
	private Integer currencyType;
	private BigDecimal balance;
	private BigDecimal expireRecAmount;
	private BigDecimal expireAmount1; // 超期1-7天
	private BigDecimal expireAmount2; // 超期8-15天
	private BigDecimal expireAmount3; // 超期超过16天及以上
	private BigDecimal adventAmount1; // 临期1-7天
	private BigDecimal adventAmount2; // 临期8-15天
	private BigDecimal adventAmount3; // 临期超过16天及以上

	private Integer billType; // 单据类型
	private String billNo;
	private Date billDate;
	private Integer feeId;
	private Integer outStoreId;
	private Date checkDate; // 对账日期
	private Date expireDate; // 应收到期日期
	private Integer expireDay; // 超期天数
	private Integer adventDay; // 临期天数

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public Integer getBusiManagerId() {
		return busiManagerId;
	}

	public void setBusiManagerId(Integer busiManagerId) {
		this.busiManagerId = busiManagerId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getExpireRecAmount() {
		return expireRecAmount;
	}

	public void setExpireRecAmount(BigDecimal expireRecAmount) {
		this.expireRecAmount = expireRecAmount;
	}

	public BigDecimal getExpireAmount1() {
		return expireAmount1;
	}

	public void setExpireAmount1(BigDecimal expireAmount1) {
		this.expireAmount1 = expireAmount1;
	}

	public BigDecimal getExpireAmount2() {
		return expireAmount2;
	}

	public void setExpireAmount2(BigDecimal expireAmount2) {
		this.expireAmount2 = expireAmount2;
	}

	public BigDecimal getExpireAmount3() {
		return expireAmount3;
	}

	public void setExpireAmount3(BigDecimal expireAmount3) {
		this.expireAmount3 = expireAmount3;
	}

	public BigDecimal getAdventAmount1() {
		return adventAmount1;
	}

	public void setAdventAmount1(BigDecimal adventAmount1) {
		this.adventAmount1 = adventAmount1;
	}

	public BigDecimal getAdventAmount2() {
		return adventAmount2;
	}

	public void setAdventAmount2(BigDecimal adventAmount2) {
		this.adventAmount2 = adventAmount2;
	}

	public BigDecimal getAdventAmount3() {
		return adventAmount3;
	}

	public void setAdventAmount3(BigDecimal adventAmount3) {
		this.adventAmount3 = adventAmount3;
	}

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
}
