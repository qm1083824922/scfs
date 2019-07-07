package com.scfs.domain.report.resp;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: GoodsReportResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
  *  2017年08月31日				Administrator
 *
 * </pre>
 */
public class GoodsReportResDto {

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
	/** 商品名称 **/
	private String goodsName;
	/** 商品条码 **/
	private String barCode;
	/** 型号 **/
	private String type;
	/** 国内税率 **/
	private BigDecimal taxRate;
	/** 质押比例 */
	private BigDecimal pledgeProportion;
	/** 规格 */
	private String specification;
	private String brand;

	/** 是否标红 */
	private boolean isWarn = false;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getPledgeProportion() {
		return pledgeProportion;
	}

	public void setPledgeProportion(BigDecimal pledgeProportion) {
		this.pledgeProportion = pledgeProportion;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

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

	public boolean isWarn() {
		return isWarn;
	}

	public void setWarn(boolean isWarn) {
		this.isWarn = isWarn;
	}

}
