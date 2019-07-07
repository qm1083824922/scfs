package com.scfs.domain.api.cms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CmsPayConfirm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3702089590651392240L;
	
	/**
	 * 付款单号
	 */
	private String pay_no;
	/**
	 * 付款状态
	 */
	private Integer pay_status;
	/**
	 * 实际付款时间
	 */
	private Date real_pay_time;
	/**
	 * 实际付款币种
	 */
	private String real_currency;
	/**
	 * 实际付款金额
	 */
	private BigDecimal real_pay_amount;
	/**
	 * 付款比例
	 */
	private BigDecimal pay_rate;
	/**
	 * 银行手续费
	 */
	private BigDecimal bank_fee_amount;
	/**
	 * 银行账号
	 */
	private String bank_account;
	/**
	 * 付款人
	 */
	private String payer;
	/**
	 * 驳回原因
	 */
	private String reason;
	/**
	 * 驳回人
	 */
	private String rejecter;
	
	public String getPay_no() {
		return pay_no;
	}
	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}
	public Date getReal_pay_time() {
		return real_pay_time;
	}
	public void setReal_pay_time(Date real_pay_time) {
		this.real_pay_time = real_pay_time;
	}
	public String getReal_currency() {
		return real_currency;
	}
	public void setReal_currency(String real_currency) {
		this.real_currency = real_currency;
	}
	public BigDecimal getReal_pay_amount() {
		return real_pay_amount;
	}
	public void setReal_pay_amount(BigDecimal real_pay_amount) {
		this.real_pay_amount = real_pay_amount;
	}
	public BigDecimal getPay_rate() {
		return pay_rate;
	}
	public void setPay_rate(BigDecimal pay_rate) {
		this.pay_rate = pay_rate;
	}
	public BigDecimal getBank_fee_amount() {
		return bank_fee_amount;
	}
	public void setBank_fee_amount(BigDecimal bank_fee_amount) {
		this.bank_fee_amount = bank_fee_amount;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRejecter() {
		return rejecter;
	}
	public void setRejecter(String rejecter) {
		this.rejecter = rejecter;
	}
	public String getBank_account() {
		return bank_account;
	}
	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}


}

