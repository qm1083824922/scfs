package com.scfs.domain.sale.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年10月27日.
 */
public class BillDeliveryDtlSum implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5844664862305845942L;

	/**
	 * 应发货总数量
	 */
	private BigDecimal requiredSendNum;

	/**
	 * 应发货总价格
	 */
	private BigDecimal requiredSendAmount;

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
	 * 服务费
	 */
	private BigDecimal serviceAmount;

	public BigDecimal getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(BigDecimal requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public BigDecimal getRequiredSendAmount() {
		return requiredSendAmount;
	}

	public void setRequiredSendAmount(BigDecimal requiredSendAmount) {
		this.requiredSendAmount = requiredSendAmount;
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

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

}
