package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年12月12日.
 */
public class BillOutStoreSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8632642324051554412L;
	private BigDecimal totalSendNum;
	private BigDecimal totalPickupNum;
	private BigDecimal totalSendAmount;
	private BigDecimal totalPickupAmount;
	private Integer currencyType;

	public BigDecimal getTotalSendNum() {
		return totalSendNum;
	}

	public void setTotalSendNum(BigDecimal totalSendNum) {
		this.totalSendNum = totalSendNum;
	}

	public BigDecimal getTotalPickupNum() {
		return totalPickupNum;
	}

	public void setTotalPickupNum(BigDecimal totalPickupNum) {
		this.totalPickupNum = totalPickupNum;
	}

	public BigDecimal getTotalSendAmount() {
		return totalSendAmount;
	}

	public void setTotalSendAmount(BigDecimal totalSendAmount) {
		this.totalSendAmount = totalSendAmount;
	}

	public BigDecimal getTotalPickupAmount() {
		return totalPickupAmount;
	}

	public void setTotalPickupAmount(BigDecimal totalPickupAmount) {
		this.totalPickupAmount = totalPickupAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
