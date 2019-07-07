package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 铺货出库库明细
 * 
 * @author 
 *
 */
public class GoodsOutReport {
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 采购单号
	 */
	private String purchaseSn;

	/**
	 * 币种
	 */
	private String currencyType;

	/**
	 * 币种名称
	 */
	private String currencyName;

	/**
	 * 销售数量
	 */
	private BigDecimal wmsOutStockin;

	/**
	 * 销售价格
	 */
	private BigDecimal purchasePrice;

	/**
	 * 销售日期
	 */
	/** 入库日期 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date salesDate;

	/**
	 * 退货单号
	 */
	private String orderNo;

	/**
	 * 出库金额
	 */
	private BigDecimal salesAmount;

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

	public String getPurchaseSn() {
		return purchaseSn;
	}

	public void setPurchaseSn(String purchaseSn) {
		this.purchaseSn = purchaseSn;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public BigDecimal getWmsOutStockin() {
		return wmsOutStockin;
	}

	public void setWmsOutStockin(BigDecimal wmsOutStockin) {
		this.wmsOutStockin = wmsOutStockin;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(BigDecimal salesAmount) {
		this.salesAmount = salesAmount;
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
