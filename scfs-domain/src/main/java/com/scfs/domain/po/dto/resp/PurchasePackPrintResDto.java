package com.scfs.domain.po.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PurchasePackPrintResDto extends BaseEntity {
	/** 采购单明细id **/
	private Integer poLineId;
	/** 采购单编号 **/
	private String orderNo;
	/** 采购单日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date orderTime;
	/** 商品编号 **/
	private String goodsNo;
	/** 商品名称 **/
	private String goodsName;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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
