package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceCollectOverseasPo {
	/**
	 * 收票采购ID自增长
	 */
	private Integer id;

	/**
	 * 境外收票id
	 */
	private Integer collectOverseasId;

	/**
	 * 采购单id
	 */
	private Integer poLineId;

	/**
	 * 收票金额
	 */
	private BigDecimal invoiceAmoun;

	/** 实际收票金额 **/
	private BigDecimal realInvoiceAmount;

	/** 汇率 **/
	private BigDecimal rate;
	/** 币种 **/
	private Integer currnecyType;

	/**
	 * 实际收票币种
	 */
	private Integer realCurrnecyType;

	/**
	 * 收票数量
	 */
	private BigDecimal invoiceNum;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 是否删除 0 否 1 是
	 */
	private Integer isDelete;

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
	/** 付款单汇率 **/
	private BigDecimal payRate;

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCollectOverseasId() {
		return collectOverseasId;
	}

	public void setCollectOverseasId(Integer collectOverseasId) {
		this.collectOverseasId = collectOverseasId;
	}

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public BigDecimal getInvoiceAmoun() {
		return invoiceAmoun;
	}

	public void setInvoiceAmoun(BigDecimal invoiceAmoun) {
		this.invoiceAmoun = invoiceAmoun;
	}

	public BigDecimal getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(BigDecimal invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public BigDecimal getRealInvoiceAmount() {
		return realInvoiceAmount;
	}

	public void setRealInvoiceAmount(BigDecimal realInvoiceAmount) {
		this.realInvoiceAmount = realInvoiceAmount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public Integer getRealCurrnecyType() {
		return realCurrnecyType;
	}

	public void setRealCurrnecyType(Integer realCurrnecyType) {
		this.realCurrnecyType = realCurrnecyType;
	}
}