package com.scfs.domain.logistics.entity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: BilOutStoreTaxGroupSum.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月24日			Administrator
 *
 * </pre>
 */
public class BillOutStoreDtlTaxGroupSum {
	private BigDecimal sendAmount;

	private BigDecimal receiveAmount; // 入库时的收货金额

	private BigDecimal taxRate;

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

}
