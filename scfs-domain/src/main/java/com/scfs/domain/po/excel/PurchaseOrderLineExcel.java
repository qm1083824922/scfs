package com.scfs.domain.po.excel;

/**
 * <pre>
 * 
 *  File: PurchaseOrderLineExcel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月29日				Administrator
 *
 * </pre>
 */
public class PurchaseOrderLineExcel {
	/** 商品编号 */
	private String goodsNo;
	/** 商品数量 */
	private String goodsNum;
	/** 商品单价 */
	private String goodsPrice;
	/** 批次号 */
	private String batchNum;
	private String discountAmount;

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
