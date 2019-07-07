package com.scfs.rpc.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class Pay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2534611058782238661L;

	/**
	 * 请款单号
	 */
	private String pay_sn;
	/**
	 * 操作类型 1-驳回 2-付款成功
	 */
	private Integer type;
	/**
	 * 操作人
	 */
	private String create_user;
	/**
	 * 操作时间
	 */
	private Date create_time;
	/**
	 * 付款主体代码
	 */
	private String corporation_code;
	/**
	 * 付款主体名称
	 */
	private String corporation_name;
	/**
	 * 付款账户开户行
	 */
	private String pay_bank;
	/**
	 * 付款账号
	 */
	private String pay_no;
	/**
	 * 付款币种
	 */
	private String currency_type;
	/**
	 * 付款金额
	 */
	private BigDecimal currency_money;
	/**
	 * 付款汇率
	 */
	private BigDecimal currency_rate;

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
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

	public String getPay_bank() {
		return pay_bank;
	}

	public void setPay_bank(String pay_bank) {
		this.pay_bank = pay_bank;
	}

	public String getPay_no() {
		return pay_no;
	}

	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
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

	public BigDecimal getCurrency_rate() {
		return currency_rate;
	}

	public void setCurrency_rate(BigDecimal currency_rate) {
		this.currency_rate = currency_rate;
	}

}
