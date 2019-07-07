package com.scfs.domain.sale.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年12月12日.
 */
public class BillDeliverySum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1770573762004032201L;
	private BigDecimal totalRequiredSendNum;
	private BigDecimal totalRequiredSendAmount;
	private Integer currencyType;

	public BigDecimal getTotalRequiredSendNum() {
		return totalRequiredSendNum;
	}

	public void setTotalRequiredSendNum(BigDecimal totalRequiredSendNum) {
		this.totalRequiredSendNum = totalRequiredSendNum;
	}

	public BigDecimal getTotalRequiredSendAmount() {
		return totalRequiredSendAmount;
	}

	public void setTotalRequiredSendAmount(BigDecimal totalRequiredSendAmount) {
		this.totalRequiredSendAmount = totalRequiredSendAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
