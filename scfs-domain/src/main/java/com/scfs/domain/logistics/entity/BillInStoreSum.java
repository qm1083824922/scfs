package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年12月12日.
 */
public class BillInStoreSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 363955969340002101L;
	private BigDecimal totalReceiveNum;
	private BigDecimal totalTallyNum;
	private BigDecimal totalReceiveAmount;
	private BigDecimal totalTallyAmount;
	private Integer currencyType;

	public BigDecimal getTotalReceiveNum() {
		return totalReceiveNum;
	}

	public void setTotalReceiveNum(BigDecimal totalReceiveNum) {
		this.totalReceiveNum = totalReceiveNum;
	}

	public BigDecimal getTotalTallyNum() {
		return totalTallyNum;
	}

	public void setTotalTallyNum(BigDecimal totalTallyNum) {
		this.totalTallyNum = totalTallyNum;
	}

	public BigDecimal getTotalReceiveAmount() {
		return totalReceiveAmount;
	}

	public void setTotalReceiveAmount(BigDecimal totalReceiveAmount) {
		this.totalReceiveAmount = totalReceiveAmount;
	}

	public BigDecimal getTotalTallyAmount() {
		return totalTallyAmount;
	}

	public void setTotalTallyAmount(BigDecimal totalTallyAmount) {
		this.totalTallyAmount = totalTallyAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
