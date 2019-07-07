package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: RateSum.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月5日				Administrator
 *
 * </pre>
 */
public class TaxRateSum {
	private BigDecimal taxRateSum;

	private BigDecimal taxRate;

	public BigDecimal getTaxRateSum() {
		return taxRateSum;
	}

	public void setTaxRateSum(BigDecimal taxRateSum) {
		this.taxRateSum = taxRateSum;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
}
