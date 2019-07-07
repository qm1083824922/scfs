package com.scfs.domain.interf.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: PmsPayOrderTitleResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */
public class PmsPayOrderTitleResDto {
	private Integer id;
	private String payNo;
	private String corporation_code;
	private String corporation_name;
	private String vendorNo;
	private String supplierNo;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date payDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date innerPayDate;
	private BigDecimal payMoney;
	private String payCurrency;
	private String stateName;
	private String message;
	/** 抵扣金额 **/
	private BigDecimal deduction_money;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
	private Date createAt;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PMS_PAY_ORDER_DTL);
			operMap.put(OperateConsts.DEAL, BusUrlConsts.DEAL_PMS_PAY_ORDER);
		}
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public BigDecimal getDeduction_money() {
		return deduction_money;
	}

	public void setDeduction_money(BigDecimal deduction_money) {
		this.deduction_money = deduction_money;
	}
}
