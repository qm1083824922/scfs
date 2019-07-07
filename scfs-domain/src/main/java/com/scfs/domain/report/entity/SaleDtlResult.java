package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;

/**
 * Created by Administrator on 2017年2月15日.
 */
public class SaleDtlResult {
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 项目
	 */
	private String projectName;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 客户
	 */
	private String customerName;
	/**
	 * 单据id
	 */
	private String billId;
	/**
	 * 单据类型
	 */
	private String billType;
	/**
	 * 单据类型名称
	 */
	private String billTypeName;
	/**
	 * 单据编号
	 */
	private String billNo;
	/**
	 * 记账日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date accountDate;
	/**
	 * 销售金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal saleAmount;
	/**
	 * 销售成本
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal costAmount;
	/**
	 * 回款金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal returnedAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 币种名称
	 */
	private String currencyTypeName;

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
		this.projectName = projectName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(Date accountDate) {
		this.accountDate = accountDate;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getReturnedAmount() {
		return returnedAmount;
	}

	public void setReturnedAmount(BigDecimal returnedAmount) {
		this.returnedAmount = returnedAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

}
