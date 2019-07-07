package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: AccountPoolFundResDto.java
 *  Description:资金池
 *  TODO
 *  Date,					Who,				
 *  2017年09月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountPoolFundResDto extends BaseEntity {
	/** 资金池头id **/
	private Integer poolId;
	/** 账户id **/
	private Integer accountId;
	/** 经营单位 **/
	private Integer busiUnit;
	private String busiUnitName;
	/** 项目 **/
	private Integer projectId;
	private String projectName;
	/** 客户 **/
	private Integer customerId;
	private String customerName;
	/** 供应商 **/
	private Integer supplieId;
	private String supplieName;
	/** 单据编号 **/
	private String billNo;
	/** 单据类型 **/
	private Integer billType;
	private String billTypeName;
	/** 单据币种 **/
	private Integer currencyType;
	private String currencyTypeName;
	/** 单据日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date billDate;
	/** 单据金额 **/
	private BigDecimal billAmount = BigDecimal.ZERO;
	/** 手续费 **/
	private BigDecimal billChargeAmount = BigDecimal.ZERO;
	/** 单据id，如水单id,付款id **/
	private Integer billThirdId;
	/** 备注 **/
	private String remark;

	public Integer getPoolId() {
		return poolId;
	}

	public void setPoolId(Integer poolId) {
		this.poolId = poolId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
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

	public Integer getSupplieId() {
		return supplieId;
	}

	public void setSupplieId(Integer supplieId) {
		this.supplieId = supplieId;
	}

	public String getSupplieName() {
		return supplieName;
	}

	public void setSupplieName(String supplieName) {
		this.supplieName = supplieName;
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

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
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

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getBillChargeAmount() {
		return billChargeAmount;
	}

	public void setBillChargeAmount(BigDecimal billChargeAmount) {
		this.billChargeAmount = billChargeAmount;
	}

	public Integer getBillThirdId() {
		return billThirdId;
	}

	public void setBillThirdId(Integer billThirdId) {
		this.billThirdId = billThirdId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
