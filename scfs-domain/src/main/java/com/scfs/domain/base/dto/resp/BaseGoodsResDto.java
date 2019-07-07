package com.scfs.domain.base.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

public class BaseGoodsResDto {

	/** 商品ID */
	private Integer id;

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
	private String brand;

	/** 税收分类 */
	private String taxClassification;

	/** 国内税率 */
	private BigDecimal taxRate;
	/** 国内税率value */
	private String taxRateValue;

	/** 单位 */
	private String unit;

	/** 高 */
	private BigDecimal volume;

	/** 毛重 */
	private BigDecimal grossWeight;

	/** 净重 */
	private BigDecimal netWeight;

	/** 采购指导价 */
	private BigDecimal purchasePrice;
	private String purchasePriceValue;

	/** 销售指导价 */
	private BigDecimal salePrice;
	/** 销售指导价 */
	private String salePriceValue;
	private String creator;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creatorAt;
	/** 状态 */
	private Integer status;
	/** 状态 */
	private String statusName;
	/**
	 * 采购指导价币种
	 */
	private String purCurrencyTypeName;
	/**
	 * 销售指导价币种
	 */
	private String saleCurrencyTypeName;

	/** 宽 **/
	private BigDecimal broad;
	/** 长 **/
	private BigDecimal grow;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDITGOODS);
			operMap.put(OperateConsts.DELETE, BaseUrlConsts.DELETEGOODS);
			operMap.put(OperateConsts.DETAIL, BaseUrlConsts.DETAILGOODS);
			operMap.put(OperateConsts.SUBMIT, BaseUrlConsts.SUBMITGOODS);
			operMap.put(OperateConsts.LOCK, BaseUrlConsts.LOCKGOODS);
			operMap.put(OperateConsts.UNLOCK, BaseUrlConsts.UNLOCKGOODS);
		}

		public static Map<String, String> getOperMap() {
			return operMap;
		}

		public static void setOperMap(Map<String, String> operMap) {
			Operate.operMap = operMap;
		}

	}

	public String getStatusName() {
		return statusName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatorAt() {
		return creatorAt;
	}

	public void setCreatorAt(Date creatorAt) {
		this.creatorAt = creatorAt;
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTaxRateValue() {
		return taxRateValue;
	}

	public void setTaxRateValue(String taxRateValue) {
		this.taxRateValue = taxRateValue;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getPurchasePriceValue() {
		return purchasePriceValue;
	}

	public void setPurchasePriceValue(String purchasePriceValue) {
		this.purchasePriceValue = purchasePriceValue;
	}

	public String getSalePriceValue() {
		return salePriceValue;
	}

	public void setSalePriceValue(String salePriceValue) {
		this.salePriceValue = salePriceValue;
	}

	public String getPurCurrencyTypeName() {
		return purCurrencyTypeName;
	}

	public void setPurCurrencyTypeName(String purCurrencyTypeName) {
		this.purCurrencyTypeName = purCurrencyTypeName;
	}

	public String getSaleCurrencyTypeName() {
		return saleCurrencyTypeName;
	}

	public void setSaleCurrencyTypeName(String saleCurrencyTypeName) {
		this.saleCurrencyTypeName = saleCurrencyTypeName;
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
