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

/**
 * <pre>
 *  铺货商品
 *  File: DistributionGoodsResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月02日				Administrator
 *
 * </pre>
 */
public class DistributionGoodsResDto {

	/** 商品ID */
	private Integer id;

	/** 编号 */
	private String number;

	/** 名称 */
	private String name;

	/** 部门 **/
	private Integer departmentId;
	private String departmentName;

	/** 供应商 **/
	private Integer supplierId;
	private String supplierName;

	/** 质押比例 **/
	private BigDecimal pledge;

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

	/** 体积 */
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
	/** 状态 */
	private Integer status;
	/** 状态 */
	private String statusName;

	private Integer purCurrencyType;
	private Integer saleCurrencyType;
	/**
	 * 采购指导价币种
	 */
	private String purCurrencyTypeName;
	/**
	 * 销售指导价币种
	 */
	private String saleCurrencyTypeName;

	/** 事业部 **/
	private Integer careerId;
	private String careerName;

	/** 采购部 **/
	private Integer purchaseId;
	private String purchaseName;

	/** 供应链小组 **/
	private Integer supplyChainGroupId;
	private String supplyChainGroupName;

	/** 供应链服务 **/
	private Integer supplyChainServiceId;
	private String supplyChainServiceName;

	/** 风控 **/
	private Integer riskId;
	private String riskName;

	private String creator;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creatorAt;

	/** 作废人 */
	private String deleter;

	/** 作废时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deleteAt;
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

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDIT_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.DELETE, BaseUrlConsts.DELETE_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.DETAIL, BaseUrlConsts.DETAIL_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.SUBMIT, BaseUrlConsts.SUBMIT_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.LOCK, BaseUrlConsts.LOCK_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.UNLOCK, BaseUrlConsts.UNLOCK_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.SAVE, BaseUrlConsts.ADD_DISTRIBUTE_GOODS);
			operMap.put(OperateConsts.COPY, BaseUrlConsts.COPE_DISTRIBUTE_GOODS);
		}

		public static Map<String, String> getOperMap() {
			return operMap;
		}

		public static void setOperMap(Map<String, String> operMap) {
			Operate.operMap = operMap;
		}

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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getTaxRateValue() {
		return taxRateValue;
	}

	public void setTaxRateValue(String taxRateValue) {
		this.taxRateValue = taxRateValue;
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

	public String getPurchasePriceValue() {
		return purchasePriceValue;
	}

	public void setPurchasePriceValue(String purchasePriceValue) {
		this.purchasePriceValue = purchasePriceValue;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getSalePriceValue() {
		return salePriceValue;
	}

	public void setSalePriceValue(String salePriceValue) {
		this.salePriceValue = salePriceValue;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public Integer getCareerId() {
		return careerId;
	}

	public void setCareerId(Integer careerId) {
		this.careerId = careerId;
	}

	public String getCareerName() {
		return careerName;
	}

	public void setCareerName(String careerName) {
		this.careerName = careerName;
	}

	public Integer getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}

	public Integer getSupplyChainGroupId() {
		return supplyChainGroupId;
	}

	public void setSupplyChainGroupId(Integer supplyChainGroupId) {
		this.supplyChainGroupId = supplyChainGroupId;
	}

	public String getSupplyChainGroupName() {
		return supplyChainGroupName;
	}

	public void setSupplyChainGroupName(String supplyChainGroupName) {
		this.supplyChainGroupName = supplyChainGroupName;
	}

	public Integer getSupplyChainServiceId() {
		return supplyChainServiceId;
	}

	public void setSupplyChainServiceId(Integer supplyChainServiceId) {
		this.supplyChainServiceId = supplyChainServiceId;
	}

	public String getSupplyChainServiceName() {
		return supplyChainServiceName;
	}

	public void setSupplyChainServiceName(String supplyChainServiceName) {
		this.supplyChainServiceName = supplyChainServiceName;
	}

	public Integer getRiskId() {
		return riskId;
	}

	public void setRiskId(Integer riskId) {
		this.riskId = riskId;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
