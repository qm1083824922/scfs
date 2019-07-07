package com.scfs.domain.sale.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016年12月29日.
 */
public class BillDeliveryDtlExcel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1335962093404491339L;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 商品编号
	 */
	private String goodsNo;
	/**
	 * 销售数量
	 */
	private String requiredSendNum;
	/**
	 * 销售价
	 */
	private String requiredSendPrice;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 商品ID
	 */
	private Integer goodsId;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(String requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public String getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(String requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

}
