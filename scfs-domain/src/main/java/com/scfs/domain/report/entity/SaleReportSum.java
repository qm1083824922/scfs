package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;

/**
 * Created by Administrator on 2017年2月17日.
 */
public class SaleReportSum {
	/**
	 * 外部销售金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalOuterSaleAmount;
	/**
	 * 内部销售金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalInnerSaleAmount;
	/**
	 * 销售金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalSaleAmount;
	/**
	 * 销售成本
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalCostAmount;
	/**
	 * 销售利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalProfitAmount;
	/**
	 * 费用利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalFeeProfitAmount;
	/**
	 * 合计利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalSumProfitAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public BigDecimal getTotalOuterSaleAmount() {
		return totalOuterSaleAmount;
	}

	public void setTotalOuterSaleAmount(BigDecimal totalOuterSaleAmount) {
		this.totalOuterSaleAmount = totalOuterSaleAmount;
	}

	public BigDecimal getTotalInnerSaleAmount() {
		return totalInnerSaleAmount;
	}

	public void setTotalInnerSaleAmount(BigDecimal totalInnerSaleAmount) {
		this.totalInnerSaleAmount = totalInnerSaleAmount;
	}

	public BigDecimal getTotalSaleAmount() {
		return totalSaleAmount;
	}

	public void setTotalSaleAmount(BigDecimal totalSaleAmount) {
		this.totalSaleAmount = totalSaleAmount;
	}

	public BigDecimal getTotalCostAmount() {
		return totalCostAmount;
	}

	public void setTotalCostAmount(BigDecimal totalCostAmount) {
		this.totalCostAmount = totalCostAmount;
	}

	public BigDecimal getTotalProfitAmount() {
		return totalProfitAmount;
	}

	public void setTotalProfitAmount(BigDecimal totalProfitAmount) {
		this.totalProfitAmount = totalProfitAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getTotalFeeProfitAmount() {
		return totalFeeProfitAmount;
	}

	public void setTotalFeeProfitAmount(BigDecimal totalFeeProfitAmount) {
		this.totalFeeProfitAmount = totalFeeProfitAmount;
	}

	public BigDecimal getTotalSumProfitAmount() {
		return totalSumProfitAmount;
	}

	public void setTotalSumProfitAmount(BigDecimal totalSumProfitAmount) {
		this.totalSumProfitAmount = totalSumProfitAmount;
	}

}
