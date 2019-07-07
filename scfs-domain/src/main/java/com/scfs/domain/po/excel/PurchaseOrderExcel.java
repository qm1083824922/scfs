package com.scfs.domain.po.excel;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * 
 *  File: PurchaseOrderExcel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月29日			Administrator
 *
 * </pre>
 */
public class PurchaseOrderExcel implements Serializable {
	private static final long serialVersionUID = -5873567308030462304L;

	/** 附属编号 */
	private String appendNo;
	/** 项目编号 */
	private String projectNo;
	/** 供应商编号 */
	private String supplierNo;
	/** 仓库编号 */
	private String warehouseNo;
	/** 客户编号 */
	private String customerNo;
	private String currencyName;
	/** 订单日期 */
	private Date orderTime;
	/** 预计到货日期 */
	private Date perdictTime;
	/** 备注 */
	private String remark;
	private String discountAmount;

	/******************************* 订单行 ******************************/

	/** 商品编号 */
	private String goodsNo;
	/** 商品数量 */
	private String goodsNum;
	/** 商品单价 */
	private String goodsPrice;
	/** 批次号 */
	private String batchNum;

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPerdictTime() {
		return perdictTime;
	}

	public void setPerdictTime(Date perdictTime) {
		this.perdictTime = perdictTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

}
