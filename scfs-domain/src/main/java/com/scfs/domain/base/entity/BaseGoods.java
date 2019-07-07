package com.scfs.domain.base.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 * 
 * @author 
 * 
 */
public class BaseGoods extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 编号 */
	private String number;
	/** 名称 */
	private String name;
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
	/** 质押比例 */
	private BigDecimal pledgeProportion;
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
	private Integer purCurrencyType;
	private Integer saleCurrencyType;
	private String purCurrencyTypeValue;
	private String saleCurrencyTypeValue;
	private Integer goodType;
	/** 宽 **/
	private BigDecimal broad;
	/** 长 **/
	private BigDecimal grow;

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

	@Override
	public String getDeleter() {
		return deleter;
	}

	@Override
	public void setDeleter(String deleter) {
		this.deleter = deleter;
	}

	@Override
	public Date getDeleteAt() {
		return deleteAt;
	}

	@Override
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

	public String getPurCurrencyTypeValue() {
		return purCurrencyTypeValue;
	}

	public void setPurCurrencyTypeValue(String purCurrencyTypeValue) {
		this.purCurrencyTypeValue = purCurrencyTypeValue;
	}

	public String getSaleCurrencyTypeValue() {
		return saleCurrencyTypeValue;
	}

	public void setSaleCurrencyTypeValue(String saleCurrencyTypeValue) {
		this.saleCurrencyTypeValue = saleCurrencyTypeValue;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public BigDecimal getPledgeProportion() {
		return pledgeProportion;
	}

	public void setPledgeProportion(BigDecimal pledgeProportion) {
		this.pledgeProportion = pledgeProportion;
	}

	public Integer getGoodType() {
		return goodType;
	}

	public void setGoodType(Integer goodType) {
		this.goodType = goodType;
	}

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
}
