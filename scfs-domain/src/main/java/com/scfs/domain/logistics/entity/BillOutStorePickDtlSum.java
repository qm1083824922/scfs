package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillOutStorePickDtlSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7615466683606406167L;
	/**
	 * 拣货数量
	 */
	private BigDecimal pickupNum;
	/**
	 * 成本金额
	 */
	private BigDecimal costAmount;
	/**
	 * 订单金额
	 */
	private BigDecimal poAmount;

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}

}