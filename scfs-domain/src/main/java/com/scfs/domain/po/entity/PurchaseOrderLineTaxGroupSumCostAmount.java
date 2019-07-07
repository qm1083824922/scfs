package com.scfs.domain.po.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017年5月13日.
 */
public class PurchaseOrderLineTaxGroupSumCostAmount {
	private BigDecimal costAmount;
	private BigDecimal taxRate;

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

}
