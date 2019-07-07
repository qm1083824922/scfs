package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectOverseasPoResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月15日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectOverseasPoResDto extends BaseEntity {
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

	/**
	 * 收票数量
	 */
	private BigDecimal invoiceNum;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date orderTime;
	/** 商品数量 */
	private BigDecimal goodsNum;
	/** 商品单价 */
	private BigDecimal goodsPrice;
	/** 商品编号 */
	private String goodsNo;
	/** 商品描述 */
	private String goodsDescribe;
	/** 实际收票金额 **/
	private BigDecimal realInvoiceAmount;
	/** 汇率 **/
	private BigDecimal rate;
	/** 收票币种 **/
	private Integer currnecyType;
	/** 收票币种名称 **/
	private String currnecyTypeName;
	/** 实际收票币种 **/
	private Integer realCurrnecyType;
	/** 实际收票币种名称 **/
	private String realCurrnecyTypeName;

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

	public String getCurrnecyTypeName() {
		return currnecyTypeName;
	}

	public void setCurrnecyTypeName(String currnecyTypeName) {
		this.currnecyTypeName = currnecyTypeName;
	}

	public Integer getRealCurrnecyType() {
		return realCurrnecyType;
	}

	public void setRealCurrnecyType(Integer realCurrnecyType) {
		this.realCurrnecyType = realCurrnecyType;
	}

	public String getRealCurrnecyTypeName() {
		return realCurrnecyTypeName;
	}

	public void setRealCurrnecyTypeName(String realCurrnecyTypeName) {
		this.realCurrnecyTypeName = realCurrnecyTypeName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsDescribe() {
		return goodsDescribe;
	}

	public void setGoodsDescribe(String goodsDescribe) {
		this.goodsDescribe = goodsDescribe;
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

}
