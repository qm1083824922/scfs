package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator Date: 2017年10月13日
 */
public class PmsPaySum {
	/** 总数量 **/
	private BigDecimal totalPayQuantity;
	/** 总金额 **/
	private BigDecimal totalDealAmount;

	public BigDecimal getTotalPayQuantity() {
		return totalPayQuantity;
	}

	public void setTotalPayQuantity(BigDecimal totalPayQuantity) {
		this.totalPayQuantity = totalPayQuantity;
	}

	public BigDecimal getTotalDealAmount() {
		return totalDealAmount;
	}

	public void setTotalDealAmount(BigDecimal totalDealAmount) {
		this.totalDealAmount = totalDealAmount;
	}

}
