package com.scfs.domain.api.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;

/**
 * <pre>
 * 
 *  File: PmsPayOrder.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2016年12月5号                                       Administrator
 *
 * </pre>
 */
public class PmsPayOrderTitle implements Serializable {
	private static final long serialVersionUID = -2545332626148870005L;

	private Integer id;
	private String payNo;
	private String corporation_code;
	private String corporation_name;
	private String vendorNo;
	private String supplierNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date payDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date innerPayDate;
	private BigDecimal payMoney;
	private String payCurrency;
	private Integer currencyType;
	private Integer state;
	private String message;
	private Date createAt;
	private Date updateAt;
	/** 抵扣金额 **/
	private BigDecimal deduction_money;

	public BigDecimal getDeduction_money() {
		return deduction_money;
	}

	public void setDeduction_money(BigDecimal deduction_money) {
		this.deduction_money = deduction_money;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
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

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Date getInnerPayDate() {
		return innerPayDate;
	}

	public void setInnerPayDate(Date innerPayDate) {
		this.innerPayDate = innerPayDate;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public static Integer convertCurrencyType(String currencyCode) {
		if (StringUtils.isEmpty(currencyCode)) {
			return null;
		}
		if (currencyCode.equalsIgnoreCase(BaseConsts.CURRENCY_CNY)) {
			return BaseConsts.ONE;
		} else if (currencyCode.equalsIgnoreCase(BaseConsts.CURRENCY_USD)) {
			return BaseConsts.TWO;
		} else if (currencyCode.equalsIgnoreCase(BaseConsts.CURRENCY_HKD)) {
			return BaseConsts.THREE;
		} else {
			return null;
		}
	}
}
