package com.scfs.domain.report.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017年2月15日.
 */
public class SaleBillDtlResult {
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品条码
	 */
	private String goodsBarCode;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品型号
	 */
	private String goodsType;
	/**
	 * 发货数量
	 */
	private BigDecimal sendNum;
	/**
	 * 发货单价
	 */
	private BigDecimal sendPrice;
	/**
	 * 发货金额
	 */
	private BigDecimal sendAmount;
	/**
	 * 订单金额
	 */
	private BigDecimal poAmount;
	/**
	 * 成本金额
	 */
	private BigDecimal costAmount;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 库存状态
	 */
	private Integer goodsStatus;
	/**
	 * 库存状态名称
	 */
	private String goodsStatusName;

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
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

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

}
