package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GoodsRtReport {

	/**
	 * 退货ID
	 */
	private Integer id;
	/**
	 * 采购ID
	 */
	private Integer poId;
	/**
	 * 采购状态
	 */
	private Integer poState;

	/**
	 * 退货编号
	 */
	private String returnOrder;
	/**
	 * 退货附属编号
	 */
	private String returnAppenNo;

	/**
	 * 单据类型名称
	 */
	private String orderTypeName;

	/**
	 * 单据类型
	 */
	private Integer orderType;

	/**
	 * 退货数量
	 */
	private BigDecimal returnNumber;
	/**
	 * 退货金额
	 */
	private BigDecimal returnAmount;
	/**
	 * 退货金额
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date returnDate;

	/**
	 * 币种
	 */
	private Integer currencyId;

	/**
	 * 币种名称
	 */
	private String currencyName;

	/** 项目名称 **/
	private String projectName;
	/** 项目Id **/
	private Integer projectId;
	/** 供应商 **/
	private String supplierName;
	/** 供应商Id **/
	private Integer supplierId;
	/** 客户名称 **/
	private String customerName;
	/** 客户Id **/
	private Integer customerId;
	/** 商品编号 **/
	private String goodsCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReturnOrder() {
		return returnOrder;
	}

	public void setReturnOrder(String returnOrder) {
		this.returnOrder = returnOrder;
	}

	public String getReturnAppenNo() {
		return returnAppenNo;
	}

	public void setReturnAppenNo(String returnAppenNo) {
		this.returnAppenNo = returnAppenNo;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public BigDecimal getReturnNumber() {
		return returnNumber;
	}

	public void setReturnNumber(BigDecimal returnNumber) {
		this.returnNumber = returnNumber;
	}

	public BigDecimal getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoState() {
		return poState;
	}

	public void setPoState(Integer poState) {
		this.poState = poState;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

}
