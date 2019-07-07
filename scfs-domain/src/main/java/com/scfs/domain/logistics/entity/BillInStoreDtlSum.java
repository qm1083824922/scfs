package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年11月4日.
 */
public class BillInStoreDtlSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8300088634843137333L;

	/**
	 * 收货数量
	 */
	private BigDecimal receiveNum;
	/**
	 * 收货金额
	 */
	private BigDecimal receiveAmount;
	/**
	 * 理货数量
	 */
	private BigDecimal tallyNum;
	/**
	 * 理货金额
	 */
	private BigDecimal tallyAmount;
	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public BigDecimal getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(BigDecimal receiveNum) {
		this.receiveNum = receiveNum;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
	}

	public BigDecimal getTallyAmount() {
		return tallyAmount;
	}

	public void setTallyAmount(BigDecimal tallyAmount) {
		this.tallyAmount = tallyAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
