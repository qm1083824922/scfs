package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年11月4日.
 */
public class BillOutStoreDtlSum implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8656897154696033393L;

	/**
	 * 发货总数量
	 */
	private BigDecimal sendNum;

	/**
	 * 发货总价格
	 */
	private BigDecimal sendAmount;
	/**
	 * 拣货总数量
	 */
	private BigDecimal pickupNum;
	/**
	 * 拣货总价格
	 */
	private BigDecimal pickupAmount;
	/**
	 * 成本总价格
	 */
	private BigDecimal costAmount;
	/**
	 * 订单总价格
	 */
	private BigDecimal poAmount;
	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public BigDecimal getSendNum() {
		return sendNum;
	}

	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
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

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
	}

	public BigDecimal getPickupAmount() {
		return pickupAmount;
	}

	public void setPickupAmount(BigDecimal pickupAmount) {
		this.pickupAmount = pickupAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
