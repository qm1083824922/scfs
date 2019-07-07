package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: AccountPoolFund.java
 *  Description:资金池明细
 *  TODO
 *  Date,					Who,				
 *  2017年09月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountPoolFund extends BaseEntity {
	/** 资金池头id **/
	private Integer poolId;
	/** 账户id **/
	private Integer accountId;
	/** 经营单位id **/
	private Integer busiUnit;
	/** 项目Id **/
	private Integer projectId;
	/** 客户id **/
	private Integer customerId;
	/** 供应商id **/
	private Integer supplieId;
	/** 单据编号 **/
	private String billNo;
	/** 单据类型 **/
	private Integer billType;
	/** 单据币种 **/
	private Integer currencyType;
	/** 单据日期 **/
	private Date billDate;
	/** 单据金额 **/
	private BigDecimal billAmount;
	/** 手续费 **/
	private BigDecimal billChargeAmount;
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

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSupplieId() {
		return supplieId;
	}

	public void setSupplieId(Integer supplieId) {
		this.supplieId = supplieId;
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

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
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
