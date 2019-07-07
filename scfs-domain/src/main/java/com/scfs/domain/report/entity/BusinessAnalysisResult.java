package com.scfs.domain.report.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017年1月9日.
 */
public class BusinessAnalysisResult {
	/**
	 * 开票金额
	 */
	private BigDecimal invoiceAmount;
	/**
	 * 未开票金额
	 */
	private BigDecimal unInvoiceAmount;
	/**
	 * 收票金额
	 */
	private BigDecimal collectInvoiceAmount;
	/**
	 * 未收票金额
	 */
	private BigDecimal unCollectInvoiceAmount;
	/**
	 * 销售单成本金额
	 */
	private BigDecimal deliveryCostAmount;
	/**
	 * 销售单销售金额
	 */
	private BigDecimal deliveryPickupAmount;

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getUnInvoiceAmount() {
		return unInvoiceAmount;
	}

	public void setUnInvoiceAmount(BigDecimal unInvoiceAmount) {
		this.unInvoiceAmount = unInvoiceAmount;
	}

	public BigDecimal getCollectInvoiceAmount() {
		return collectInvoiceAmount;
	}

	public void setCollectInvoiceAmount(BigDecimal collectInvoiceAmount) {
		this.collectInvoiceAmount = collectInvoiceAmount;
	}

	public BigDecimal getUnCollectInvoiceAmount() {
		return unCollectInvoiceAmount;
	}

	public void setUnCollectInvoiceAmount(BigDecimal unCollectInvoiceAmount) {
		this.unCollectInvoiceAmount = unCollectInvoiceAmount;
	}

	public BigDecimal getDeliveryCostAmount() {
		return deliveryCostAmount;
	}

	public void setDeliveryCostAmount(BigDecimal deliveryCostAmount) {
		this.deliveryCostAmount = deliveryCostAmount;
	}

	public BigDecimal getDeliveryPickupAmount() {
		return deliveryPickupAmount;
	}

	public void setDeliveryPickupAmount(BigDecimal deliveryPickupAmount) {
		this.deliveryPickupAmount = deliveryPickupAmount;
	}

}
