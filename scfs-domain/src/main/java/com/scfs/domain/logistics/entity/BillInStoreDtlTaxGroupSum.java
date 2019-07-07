package com.scfs.domain.logistics.entity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: BillInstoreDtlTaxGroupSum.java
 *  Description:
 *  TODO                    按商品税率分组计算到货明细金额
 *  Date,					Who,				
 *  2016年11月24日			Administrator
 *
 * </pre>
 */
public class BillInStoreDtlTaxGroupSum {

	private BigDecimal receiveAmount;
	private BigDecimal costPrice;

	private BigDecimal taxRate;

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

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
}
