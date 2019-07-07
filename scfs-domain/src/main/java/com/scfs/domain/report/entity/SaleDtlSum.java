package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;

/**
 * Created by Administrator on 2017年2月18日.
 */
public class SaleDtlSum {
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
	 * 回款金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal totalReturnedAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;

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

	public BigDecimal getTotalReturnedAmount() {
		return totalReturnedAmount;
	}

	public void setTotalReturnedAmount(BigDecimal totalReturnedAmount) {
		this.totalReturnedAmount = totalReturnedAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
