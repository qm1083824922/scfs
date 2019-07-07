package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: InvoiceSumTaxGroup.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月9日				Administrator
 *
 * </pre>
 */
public class InvoiceSumTaxGroup {

	private BigDecimal taxRate;

	private BigDecimal rateAmount;

	private BigDecimal discountRateAmount;

	private BigDecimal exRateAmount;

	private BigDecimal discountExRateAmount;

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(BigDecimal rateAmount) {
		this.rateAmount = rateAmount;
	}

	public BigDecimal getDiscountRateAmount() {
		return discountRateAmount;
	}

	public void setDiscountRateAmount(BigDecimal discountRateAmount) {
		this.discountRateAmount = discountRateAmount;
	}

	public void setDiscountExRateAmount(BigDecimal discountExRateAmount) {
		this.discountExRateAmount = discountExRateAmount;
	}

	public BigDecimal getExRateAmount() {
		return exRateAmount;
	}

	public void setExRateAmount(BigDecimal exRateAmount) {
		this.exRateAmount = exRateAmount;
	}

	public BigDecimal getDiscountExRateAmount() {
		return discountExRateAmount;
	}

}
