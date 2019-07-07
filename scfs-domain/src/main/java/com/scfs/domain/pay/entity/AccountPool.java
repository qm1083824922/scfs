package com.scfs.domain.pay.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: AccountPool.java
 *  Description:资金池
 *  TODO
 *  Date,					Who,				
 *  2017年09月22日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountPool extends BaseEntity {
	/** 账户ID **/
	private Integer accountId;
	/** 经营单位ID **/
	private Integer busiUnit;
	/** 币种 **/
	private Integer currencyType;
	/** 可用资金 **/
	private BigDecimal availableAmount = BigDecimal.ZERO;
	/** 账户余额=内部水单金额-付款单（订单和费用类型）实际付款金额-付款手续费+（回款、预收）类型水单金额 **/
	private BigDecimal accountBalanceAmount = BigDecimal.ZERO;
	/** 利润 **/
	private BigDecimal profitAmount = BigDecimal.ZERO;

	// 内部水单金额
	private BigDecimal receiptAmount;

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

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getAccountBalanceAmount() {
		return accountBalanceAmount;
	}

	public void setAccountBalanceAmount(BigDecimal accountBalanceAmount) {
		this.accountBalanceAmount = accountBalanceAmount;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

}
