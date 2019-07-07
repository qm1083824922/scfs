package com.scfs.domain.po.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017年5月13日.
 */
public class PurchaseOrderLineTaxGroupSum {
	/**
	 * 未请款数量
	 */
	private BigDecimal unDistributeNum;
	/**
	 * 未请款金额
	 */
	private BigDecimal unDistributeAmount;
	/**
	 * 税率
	 */
	private BigDecimal taxRate;

	public BigDecimal getUnDistributeNum() {
		return unDistributeNum;
	}

	public void setUnDistributeNum(BigDecimal unDistributeNum) {
		this.unDistributeNum = unDistributeNum;
	}

	public BigDecimal getUnDistributeAmount() {
		return unDistributeAmount;
	}

	public void setUnDistributeAmount(BigDecimal unDistributeAmount) {
		this.unDistributeAmount = unDistributeAmount;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

}
