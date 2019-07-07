package com.scfs.domain.po.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: PurchasePackPrint.java
 *  Description:采购单装箱打印信息
 *  TODO
 *  Date,					Who,				
 *  2017年12月19日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PurchasePackPrint extends BaseEntity {
	/** 采购单明细id **/
	private Integer poLineId;
	/** 包装件数 **/
	private BigDecimal packages;
	/** 净重 **/
	private BigDecimal netWeight;
	/** 毛重 **/
	private BigDecimal grossWeight;
	/** 体积 **/
	private BigDecimal volume;

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public BigDecimal getPackages() {
		return packages;
	}

	public void setPackages(BigDecimal packages) {
		this.packages = packages;
	}

	public BigDecimal getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}

	public BigDecimal getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
}
