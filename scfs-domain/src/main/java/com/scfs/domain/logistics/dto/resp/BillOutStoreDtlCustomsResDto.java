package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * 可报关的出库明细对象 Created by Administrator on 2016年12月6日.
 */
public class BillOutStoreDtlCustomsResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7333815041114216769L;

	private Integer id;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 发货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal sendNum;
	/**
	 * 发货单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal sendPrice;
	/**
	 * 已报关数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal customsDeclareNum;
	/**
	 * 剩余报关数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal availableCustomsDeclareNum;
	/**
	 * 报关数量
	 */
	@SuppressWarnings("unused")
	private BigDecimal customsNum;
	/**
	 * 报关含税单价
	 */
	@SuppressWarnings("unused")
	private BigDecimal customsPrice;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品型号
	 */
	private String goodsType;
	/**
	 * 商品条码
	 */
	private String goodsBarCode;
	/**
	 * 商品税率
	 */
	private BigDecimal taxRate;
	/************** 出库单属性 ****************/
	/**
	 * 出库编号
	 */
	private String billNo;
	/**
	 * 出库附属编号
	 */
	private String affiliateNo;
	/**
	 * 仓库ID
	 */
	private Integer warehouseId;
	/**
	 * 仓库名称
	 */
	private String warehouseName;

	/**
	 * 出库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliverTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getSendNum() {
		return sendNum;
	}

	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}

	public BigDecimal getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(BigDecimal sendPrice) {
		this.sendPrice = sendPrice;
	}

	public BigDecimal getCustomsDeclareNum() {
		return customsDeclareNum;
	}

	public void setCustomsDeclareNum(BigDecimal customsDeclareNum) {
		this.customsDeclareNum = customsDeclareNum;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public BigDecimal getAvailableCustomsDeclareNum() {
		return DecimalUtil.subtract(null == this.getSendNum() ? BigDecimal.ZERO : this.getSendNum(),
				null == this.getCustomsDeclareNum() ? BigDecimal.ZERO : this.getCustomsDeclareNum());
	}

	public void setAvailableCustomsDeclareNum(BigDecimal availableCustomsDeclareNum) {
		this.availableCustomsDeclareNum = availableCustomsDeclareNum;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public BigDecimal getCustomsPrice() {
		return this.sendPrice;
	}

	public void setCustomsPrice(BigDecimal customsPrice) {
		this.customsPrice = customsPrice;
	}

	public BigDecimal getCustomsNum() {
		return this.getAvailableCustomsDeclareNum();
	}

	public void setCustomsNum(BigDecimal customsNum) {
		this.customsNum = customsNum;
	}

}
