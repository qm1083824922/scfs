package com.scfs.rpc.cms.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CmsPayOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2291113244527895088L;
	/**
	 * 付款编号
	 */
	private String document_no;
	/**
	 * 合并付款编号
	 */
	private String print_row;
	/**
	 * 附属编号
	 */
	private String supplier_code;
	/**
	 * 付款单位
	 */
	private String invoice_title;
	/**
	 * 项目
	 */
	private String project_name;
	/**
	 * 付款类型
	 */
	private String payment_type;
	/**
	 * 应付币种
	 */
	private String currency;
	/**
	 * 付款方式
	 */
	private String pay_type;
	/**
	 * 应付金额
	 */
	private BigDecimal amount;
	/**
	 * 收款单位
	 */
	private String p_account_name;
	/**
	 * 收款单位银行
	 */
	private String p_account_bank;
	/**
	 * 收款单位账号
	 */
	private String p_account;
	/**
	 * 收款银行所属地
	 */
	private String bank_city;
	/**
	 * 要求付款日期
	 */
	private String latest_payment_date;
	/**
	 * 创建人
	 */
	private String apply_user;
	/**
	 * 创建时间
	 */
	private String create_time;

	public String getDocument_no() {
		return document_no;
	}

	public void setDocument_no(String document_no) {
		this.document_no = document_no;
	}

	public String getPrint_row() {
		return print_row;
	}

	public void setPrint_row(String print_row) {
		this.print_row = print_row;
	}

	public String getSupplier_code() {
		return supplier_code;
	}

	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}

	public String getInvoice_title() {
		return invoice_title;
	}

	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getP_account_name() {
		return p_account_name;
	}

	public void setP_account_name(String p_account_name) {
		this.p_account_name = p_account_name;
	}

	public String getP_account_bank() {
		return p_account_bank;
	}

	public void setP_account_bank(String p_account_bank) {
		this.p_account_bank = p_account_bank;
	}

	public String getP_account() {
		return p_account;
	}

	public void setP_account(String p_account) {
		this.p_account = p_account;
	}

	public String getBank_city() {
		return bank_city;
	}

	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}

	public String getLatest_payment_date() {
		return latest_payment_date;
	}

	public void setLatest_payment_date(String latest_payment_date) {
		this.latest_payment_date = latest_payment_date;
	}

	public String getApply_user() {
		return apply_user;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
