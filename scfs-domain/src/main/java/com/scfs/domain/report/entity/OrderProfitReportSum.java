package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * Created by Administrator on 2017年3月21日.
 */
public class OrderProfitReportSum {
	/**
	 * 数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal num;
	/**
	 * 销售总价
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal saleAmount = BigDecimal.ZERO;
	/**
	 * 服务收入
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal serviceAmount = BigDecimal.ZERO;
	/**
	 * 综合税金
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal compositeTaxAmount = BigDecimal.ZERO;
	/**
	 * 采购成本
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal purchaseCost = BigDecimal.ZERO;
	/**
	 * 资金成本
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal fundCost = BigDecimal.ZERO;
	/**
	 * 仓储物流费
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal warehouseAmount = BigDecimal.ZERO;
	/**
	 * 市场费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal marketAmount = BigDecimal.ZERO;
	/**
	 * 财务费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal financeAmount = BigDecimal.ZERO;
	/**
	 * 管理费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal manageAmount = BigDecimal.ZERO;
	/**
	 * 人工费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal manualAmount = BigDecimal.ZERO;
	/**
	 * 利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal profitAmount = BigDecimal.ZERO;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public BigDecimal getCompositeTaxAmount() {
		return compositeTaxAmount;
	}

	public void setCompositeTaxAmount(BigDecimal compositeTaxAmount) {
		this.compositeTaxAmount = compositeTaxAmount;
	}

	public BigDecimal getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(BigDecimal purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
	}

	public BigDecimal getWarehouseAmount() {
		return warehouseAmount;
	}

	public void setWarehouseAmount(BigDecimal warehouseAmount) {
		this.warehouseAmount = warehouseAmount;
	}

	public BigDecimal getMarketAmount() {
		return marketAmount;
	}

	public void setMarketAmount(BigDecimal marketAmount) {
		this.marketAmount = marketAmount;
	}

	public BigDecimal getFinanceAmount() {
		return financeAmount;
	}

	public void setFinanceAmount(BigDecimal financeAmount) {
		this.financeAmount = financeAmount;
	}

	public BigDecimal getManageAmount() {
		return manageAmount;
	}

	public void setManageAmount(BigDecimal manageAmount) {
		this.manageAmount = manageAmount;
	}

	public BigDecimal getManualAmount() {
		return manualAmount;
	}

	public void setManualAmount(BigDecimal manualAmount) {
		this.manualAmount = manualAmount;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
