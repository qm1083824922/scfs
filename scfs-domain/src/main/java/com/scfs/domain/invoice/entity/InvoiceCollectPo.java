package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	收票采购信息
 *  File: InvoiceCollectPo.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectPo extends BaseEntity {
	/** 收票采购id **/
	private Integer id;
	/** 收票id **/
	private Integer collectId;
	/** 采购id **/
	private Integer poId;
	/** 含税金额 **/
	private BigDecimal inRateAmount;
	/** 未税金额 **/
	private BigDecimal exRateAmount;
	/** 税额 **/
	private BigDecimal rateAmount;
	/** 折扣含税金额 **/
	private BigDecimal discountInRateAmount;
	/** 折扣未税金额 **/
	private BigDecimal discountExRateAmount;
	/** 折扣税额 **/
	private BigDecimal discountRateAmount;
	/** 实际发票金额 **/
	private BigDecimal actualInvoiceAmount;
	/** 税率 **/
	private BigDecimal taxRate;
	/** 录入数量 **/
	private BigDecimal poNum;
	/** 备注 **/
	private String remark;

	/** 商品id **/
	private Integer goodsId;
	/** 采购编号 **/
	private String orderNo;
	/** 订单日期 **/
	private Date orderTime;
	/** 商品数量 */
	private BigDecimal goodsNum;
	/** 商品单价 */
	private BigDecimal goodsPrice;
	/** 商品金额 (数量*单价） **/
	private BigDecimal amount;
	/** 收票数量 **/
	private BigDecimal invoiceNum;
	/** 收票金额 **/
	private BigDecimal invoiceAmount;
	/** 收票可用数量 **/
	private BigDecimal blanceNum;
	/** 收票可用金额 **/
	private BigDecimal blanceAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public BigDecimal getInRateAmount() {
		return inRateAmount;
	}

	public void setInRateAmount(BigDecimal inRateAmount) {
		this.inRateAmount = inRateAmount;
	}

	public BigDecimal getExRateAmount() {
		return exRateAmount;
	}

	public void setExRateAmount(BigDecimal exRateAmount) {
		this.exRateAmount = exRateAmount;
	}

	public BigDecimal getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(BigDecimal rateAmount) {
		this.rateAmount = rateAmount;
	}

	public BigDecimal getDiscountInRateAmount() {
		return discountInRateAmount;
	}

	public void setDiscountInRateAmount(BigDecimal discountInRateAmount) {
		this.discountInRateAmount = discountInRateAmount;
	}

	public BigDecimal getDiscountExRateAmount() {
		return discountExRateAmount;
	}

	public void setDiscountExRateAmount(BigDecimal discountExRateAmount) {
		this.discountExRateAmount = discountExRateAmount;
	}

	public BigDecimal getDiscountRateAmount() {
		return discountRateAmount;
	}

	public void setDiscountRateAmount(BigDecimal discountRateAmount) {
		this.discountRateAmount = discountRateAmount;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getPoNum() {
		return poNum;
	}

	public void setPoNum(BigDecimal poNum) {
		this.poNum = poNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
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

	public BigDecimal getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(BigDecimal goodsNum) {
		this.goodsNum = goodsNum;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(BigDecimal invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getBlanceNum() {
		return blanceNum;
	}

	public void setBlanceNum(BigDecimal blanceNum) {
		this.blanceNum = blanceNum;
	}

	public BigDecimal getBlanceAmount() {
		return blanceAmount;
	}

	public void setBlanceAmount(BigDecimal blanceAmount) {
		this.blanceAmount = blanceAmount;
	}

	public BigDecimal getActualInvoiceAmount() {
		return actualInvoiceAmount;
	}

	public void setActualInvoiceAmount(BigDecimal actualInvoiceAmount) {
		this.actualInvoiceAmount = actualInvoiceAmount;
	}

}
