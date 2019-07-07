package com.scfs.domain.api.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融通质押 Created by Administrator on 2017年5月6日.
 */
public class PmsPayConfirm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8443094826787525437L;

	/**
	 * 请款单号
	 */
	private String pay_sn;
	/**
	 * 采购单号
	 */
	private String purchase_sn;
	/**
	 * 确认时间
	 */
	private Date verify_time;
	/**
	 * 请款金额
	 */
	private BigDecimal pay_price;
	/**
	 * 请款币种
	 */
	private String currency_type;
	/**
	 * 实付金额
	 */
	private BigDecimal currency_money;
	/**
	 * 实付币种
	 */
	private String real_currency_type;

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public String getPurchase_sn() {
		return purchase_sn;
	}

	public void setPurchase_sn(String purchase_sn) {
		this.purchase_sn = purchase_sn;
	}

	public Date getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	public BigDecimal getPay_price() {
		return pay_price;
	}

	public void setPay_price(BigDecimal pay_price) {
		this.pay_price = pay_price;
	}

	public String getCurrency_type() {
		return currency_type;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

	public BigDecimal getCurrency_money() {
		return currency_money;
	}

	public void setCurrency_money(BigDecimal currency_money) {
		this.currency_money = currency_money;
	}

	public String getReal_currency_type() {
		return real_currency_type;
	}

	public void setReal_currency_type(String real_currency_type) {
		this.real_currency_type = real_currency_type;
	}

}
