package com.scfs.domain.pay.dto.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 * 
 *  File: PayOrderBatchComfirmReq.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月10日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayOrderBatchConfirmReq implements Serializable {
	private List<Integer> ids;
	/** 银行账号 **/
	private Integer paymentAccount;
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String confirmorAt;
	/** 实际付款币种 **/
	private Integer realCurrencyType;
	/** 实际付款金额 **/
	private BigDecimal realPayAmount;
	/** 汇率 **/
	private BigDecimal payRate;
	/** 银行手续费 **/
	private BigDecimal bankCharge;

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public Integer getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(Integer paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getConfirmorAt() {
		return confirmorAt;
	}

	public void setConfirmorAt(String confirmorAt) {
		this.confirmorAt = confirmorAt;
	}

	public Integer getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(Integer realCurrencyType) {
		this.realCurrencyType = realCurrencyType;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public BigDecimal getBankCharge() {
		return bankCharge;
	}

	public void setBankCharge(BigDecimal bankCharge) {
		this.bankCharge = bankCharge;
	}

	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
	}

}
