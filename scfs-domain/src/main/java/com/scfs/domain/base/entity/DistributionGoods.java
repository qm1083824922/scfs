package com.scfs.domain.base.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 *  铺货商品
 *  File: DistributionGoods.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月02日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class DistributionGoods extends BaseEntity {

	/** 编号 */
	private String number;
	/** 名称 */
	private String name;

	/** 部门 **/
	private Integer departmentId;

	/** 供应商 **/
	private Integer supplierId;

	/** 质押比例 **/
	private BigDecimal pledge;

	/** 型号 */
	private String type;

	/** 条码 */
	private String barCode;

	/** 规格 */
	private String specification;

	/** 税收分类 */
	private String taxClassification;

	/** 国内税率 */
	private BigDecimal taxRate;

	/** 单位 */
	private String unit;
	private String brand;

	/** 体积 */
	private BigDecimal volume;

	/** 毛重 */
	private BigDecimal grossWeight;

	/** 净重 */
	private BigDecimal netWeight;

	/** 采购指导价 */
	private BigDecimal purchasePrice;

	/** 销售指导价 */
	private BigDecimal salePrice;

	/** 状态 */
	private Integer status;
	/** 锁定人 */
	private String locker;
	/** 删除人 */
	private String deleter;
	/** 删除时间 */
	private Date deleteAt;
	/** 锁定时间 */
	private Date lockAt;
	/** 采购指导价币种 **/
	private Integer purCurrencyType;
	/** 销售指导价币种 **/
	private Integer saleCurrencyType;
	/** 事业部 **/
	private Integer careerId;
	/** 采购部 **/
	private Integer purchaseId;
	/** 供应链小组 **/
	private Integer supplyChainGroupId;
	/** 供应链服务 **/
	private Integer supplyChainServiceId;
	/** 风控 **/
	private Integer riskId;

	/** 商品类型，0 普通商品 1 铺货商品 */
	private Integer goodType;

	/** 宽 **/
	private BigDecimal broad;
	/** 长 **/
	private BigDecimal grow;

	public BigDecimal getBroad() {
		return broad;
	}

	public void setBroad(BigDecimal broad) {
		this.broad = broad;
	}

	public BigDecimal getGrow() {
		return grow;
	}

	public void setGrow(BigDecimal grow) {
		this.grow = grow;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public BigDecimal getPledge() {
		return pledge;
	}

	public void setPledge(BigDecimal pledge) {
		this.pledge = pledge;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getTaxClassification() {
		return taxClassification;
	}

	public void setTaxClassification(String taxClassification) {
		this.taxClassification = taxClassification;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}

	public BigDecimal getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Date getLockAt() {
		return lockAt;
	}

	public void setLockAt(Date lockAt) {
		this.lockAt = lockAt;
	}

	public Integer getPurCurrencyType() {
		return purCurrencyType;
	}

	public void setPurCurrencyType(Integer purCurrencyType) {
		this.purCurrencyType = purCurrencyType;
	}

	public Integer getSaleCurrencyType() {
		return saleCurrencyType;
	}

	public void setSaleCurrencyType(Integer saleCurrencyType) {
		this.saleCurrencyType = saleCurrencyType;
	}

	public Integer getCareerId() {
		return careerId;
	}

	public void setCareerId(Integer careerId) {
		this.careerId = careerId;
	}

	public Integer getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Integer getSupplyChainGroupId() {
		return supplyChainGroupId;
	}

	public void setSupplyChainGroupId(Integer supplyChainGroupId) {
		this.supplyChainGroupId = supplyChainGroupId;
	}

	public Integer getSupplyChainServiceId() {
		return supplyChainServiceId;
	}

	public void setSupplyChainServiceId(Integer supplyChainServiceId) {
		this.supplyChainServiceId = supplyChainServiceId;
	}

	public Integer getRiskId() {
		return riskId;
	}

	public void setRiskId(Integer riskId) {
		this.riskId = riskId;
	}

	public Integer getGoodType() {
		return goodType;
	}

	public void setGoodType(Integer goodType) {
		this.goodType = goodType;
	}

}
