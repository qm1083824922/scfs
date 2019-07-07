package com.scfs.domain.project.dto.resp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

public class GoodsResDto {

	/** 主键 */
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

	/** 销售指导价 */
	private BigDecimal salePrice;
	private String purCurrencyTypeValue;
	private String saleCurrencyTypeValue;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DIVIDE, BusUrlConsts.DIVID_PROJECT_GOODS);
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
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

}
