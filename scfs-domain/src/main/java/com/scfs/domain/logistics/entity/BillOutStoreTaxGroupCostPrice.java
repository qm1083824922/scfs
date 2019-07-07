package com.scfs.domain.logistics.entity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: BillOutStoreDtlTaxGroupCostPrice.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月26日			Administrator
 *
 * </pre>
 */
public class BillOutStoreTaxGroupCostPrice {

	private BigDecimal costPriceAmount; // 成本金额

	private BigDecimal taxRate;
	/**
	 * 发货单价
	 */
	private BigDecimal sendPrice;
	/**
	 * 成本单价
	 */
	private BigDecimal costPrice;

	/**
	 * 发货金额
	 */
	private BigDecimal sendPriceAmount;
	/**
	 * 拣货数量
	 */
	private BigDecimal pickupNum;

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getCostPriceAmount() {
		return costPriceAmount;
	}

	public void setCostPriceAmount(BigDecimal costPriceAmount) {
		this.costPriceAmount = costPriceAmount;
	}

	public BigDecimal getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(BigDecimal sendPrice) {
		this.sendPrice = sendPrice;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getSendPriceAmount() {
		return sendPriceAmount;
	}

	public void setSendPriceAmount(BigDecimal sendPriceAmount) {
		this.sendPriceAmount = sendPriceAmount;
	}

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
	}

}
