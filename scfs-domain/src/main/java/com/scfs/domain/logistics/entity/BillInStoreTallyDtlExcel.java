package com.scfs.domain.logistics.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017年1月2日.
 */
public class BillInStoreTallyDtlExcel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3706541750334941827L;
	/**
	 * 收货明细ID
	 */
	private Integer id;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 收货数量
	 */
	private String receiveNum;
	/**
	 * 未理货数量
	 */
	private String unTallyNum;
	/**
	 * 理货数量
	 */
	private String tallyNum;
	/**
	 * 库存状态
	 */
	private String goodsStatusName;
	/**
	 * 商品批次
	 */
	private String batchNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(String receiveNum) {
		this.receiveNum = receiveNum;
	}

	public String getUnTallyNum() {
		return unTallyNum;
	}

	public void setUnTallyNum(String unTallyNum) {
		this.unTallyNum = unTallyNum;
	}

	public String getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(String tallyNum) {
		this.tallyNum = tallyNum;
	}

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

}
