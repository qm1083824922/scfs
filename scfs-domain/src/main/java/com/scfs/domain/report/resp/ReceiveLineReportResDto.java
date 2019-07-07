package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: ReceiveLineReportResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月17日				Administrator
 *
 * </pre>
 */
public class ReceiveLineReportResDto {
	private String departmentName;
	private String projectName;
	private String custName;
	private String bizManagerName;
	private String currencyTypeName;
	private BigDecimal balance;

	private String billTypeName; // 单据类型
	private String billNo;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate; // 对账日期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date expireDate; // 应收到期日期
	private Integer expireDay; // 超期天数
	private Integer adventDay; // 临期天数
	private Integer billType;
	private Integer feeId;
	private Integer outStoreId;

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

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getBizManagerName() {
		return bizManagerName;
	}

	public void setBizManagerName(String bizManagerName) {
		this.bizManagerName = bizManagerName;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
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

}
