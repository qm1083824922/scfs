package com.scfs.domain.report.entity;

import java.math.BigDecimal;

public class GoodsReport {

	/**
	 * 项目Id
	 */
	private Integer projectId;

	/**
	 * 供应商ID
	 */
	private Integer supplierId;

	/**
	 * 客户ID
	 */
	private Integer customerId;

	/**
	 * 仓库ID
	 */
	private Integer warehouseId;

	/** 商品SKU **/
	private String sku;
	/** 币种 **/
	private Integer currencyId;
	/**
	 * 商品编号
	 */
	private String goodsCode;
	/** 期初数量 **/
	private BigDecimal beforeNumber;
	/** 入库数量 **/
	private BigDecimal inNumber;
	/** 销售数量 **/
	private BigDecimal saleNumber;
	/** 退货数量 **/
	private BigDecimal returnNumber;
	/** 期末数量 **/
	private BigDecimal afterNumber;
	/** 请款数量 **/
	private BigDecimal pleaseNumber;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public BigDecimal getBeforeNumber() {
		return beforeNumber;
	}

	public void setBeforeNumber(BigDecimal beforeNumber) {
		this.beforeNumber = beforeNumber;
	}

	public BigDecimal getInNumber() {
		return inNumber;
	}

	public void setInNumber(BigDecimal inNumber) {
		this.inNumber = inNumber;
	}

	public BigDecimal getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(BigDecimal saleNumber) {
		this.saleNumber = saleNumber;
	}

	public BigDecimal getReturnNumber() {
		return returnNumber;
	}

	public void setReturnNumber(BigDecimal returnNumber) {
		this.returnNumber = returnNumber;
	}

	public BigDecimal getAfterNumber() {
		return afterNumber;
	}

	public void setAfterNumber(BigDecimal afterNumber) {
		this.afterNumber = afterNumber;
	}

	public BigDecimal getPleaseNumber() {
		return pleaseNumber;
	}

	public void setPleaseNumber(BigDecimal pleaseNumber) {
		this.pleaseNumber = pleaseNumber;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

}
