package com.scfs.domain.api.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 应收保理 Created by Administrator on 2017年5月22日.
 */
public class PmsPayOrderConfirm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6616740562355230294L;

	/**
	 * 请款单号
	 */
	private String pay_no;
	/**
	 * 新的请款单号
	 */
	private String new_pay_no;
	/**
	 * 付款法人代码
	 */
	private String corporation_code;
	/**
	 * 付款法人名称
	 */
	private String corporation_name;

	/**
	 * 供应商编号
	 */
	private String provider_sn;
	/**
	 * 确认日期
	 */
	private Date confirm_date;
	/**
	 * 付款金额
	 */
	private BigDecimal pay_amount;
	/**
	 * 付款币种
	 */
	private String currency_type;
	/**
	 * 实际付款金额
	 */
	private BigDecimal real_pay_amount;
	/**
	 * 实际付款币种
	 */
	private String real_currency_type;
	/**
	 * 银行流水号
	 */
	private String bank_series_no;

	public String getPay_no() {
		return pay_no;
	}

	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
	}

	public String getCorporation_code() {
		return corporation_code;
	}

	public void setCorporation_code(String corporation_code) {
		this.corporation_code = corporation_code;
	}

	public String getCorporation_name() {
		return corporation_name;
	}

	public void setCorporation_name(String corporation_name) {
		this.corporation_name = corporation_name;
	}

	public String getProvider_sn() {
		return provider_sn;
	}

	public void setProvider_sn(String provider_sn) {
		this.provider_sn = provider_sn;
	}

	public Date getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}

	public BigDecimal getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(BigDecimal pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getCurrency_type() {
		return currency_type;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

	public BigDecimal getReal_pay_amount() {
		return real_pay_amount;
	}

	public void setReal_pay_amount(BigDecimal real_pay_amount) {
		this.real_pay_amount = real_pay_amount;
	}

	public String getReal_currency_type() {
		return real_currency_type;
	}

	public void setReal_currency_type(String real_currency_type) {
		this.real_currency_type = real_currency_type;
	}

	public String getBank_series_no() {
		return bank_series_no;
	}

	public void setBank_series_no(String bank_series_no) {
		this.bank_series_no = bank_series_no;
	}

	public String getNew_pay_no() {
		return new_pay_no;
	}

	public void setNew_pay_no(String new_pay_no) {
		this.new_pay_no = new_pay_no;
	}

}
