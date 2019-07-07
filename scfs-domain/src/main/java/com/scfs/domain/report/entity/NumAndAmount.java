package com.scfs.domain.report.entity;

import java.math.BigDecimal;

public class NumAndAmount {
	/** 金额 */
	private BigDecimal amount;
	/** 数量 */
	private BigDecimal num;

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
