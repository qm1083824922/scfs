package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: ReceiveReportResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月14日				Administrator
 *
 * </pre>
 */
public class ReceiveReportResDto {
	private String departmentName;
	private String projectName;
	private String custName;
	private String bizManagerName;
	private String currencyTypeName;
	private BigDecimal balance;
	private BigDecimal expireRecAmount;
	private BigDecimal expireAmount1; // 超期1-7天
	private BigDecimal expireAmount2; // 超期8-15天
	private BigDecimal expireAmount3; // 超期超过16天及以上
	private BigDecimal adventAmount1; // 临期1-7天
	private BigDecimal adventAmount2; // 临期8-15天
	private BigDecimal adventAmount3; // 临期超过16天及以上
	private BigDecimal unCheckAmount; // 未对账金额

	private String billTypeName; // 单据类型
	private String billNo;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate; // 对账日期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date expireDate; // 应收到期日期
	private Integer expireDay; // 超期天数
	private Integer adventDay; // 临期天数

	private Integer projectId;
	private Integer custId;
	private Integer currencyType;
	private Integer billType;

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

	public BigDecimal getUnCheckAmount() {
		return unCheckAmount;
	}

	public void setUnCheckAmount(BigDecimal unCheckAmount) {
		this.unCheckAmount = unCheckAmount;
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

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}
}
