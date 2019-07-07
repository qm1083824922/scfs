package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class GoodsStlReport {

	/**
	 * 项目id
	 */
	private Integer projectId;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 供应商ID
	 */
	private Integer supplierId;
	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 商品编号
	 */
	private String goodsCode;
	/**
	 * 库存数量
	 */
	private BigDecimal stlNum;
	/**
	 * 成本单价
	 */
	private BigDecimal costPrice;
	/**
	 * 库存金额
	 */
	private BigDecimal stlAmount;
	/**
	 * 币种
	 */
	private Integer currencyId;
	/**
	 * 币种名称
	 */
	private String currencyName;
	/**
	 * 批次
	 */
	private String batchNum;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 入库日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date storeInDate;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public BigDecimal getStlNum() {
		return stlNum;
	}

	public void setStlNum(BigDecimal stlNum) {
		this.stlNum = stlNum;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getStlAmount() {
		return stlAmount;
	}

	public void setStlAmount(BigDecimal stlAmount) {
		this.stlAmount = stlAmount;
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

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getStoreInDate() {
		return storeInDate;
	}

	public void setStoreInDate(Date storeInDate) {
		this.storeInDate = storeInDate;
	}
}
