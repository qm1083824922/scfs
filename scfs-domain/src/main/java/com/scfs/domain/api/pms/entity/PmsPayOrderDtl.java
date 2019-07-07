package com.scfs.domain.api.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 
 *  File: PmsPayOrderDtl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月15日			Administrator
 *
 * </pre>
 */
public class PmsPayOrderDtl implements Serializable {
	private static final long serialVersionUID = 6965622530597637972L;
	private Integer id;
	private Integer pmsPayOrderTitleId;
	private String poNo;
	private String goodsNo;
	private String goodsName;
	private BigDecimal inQty;
	private BigDecimal inPrice;
	private String message;
	private Date createAt;
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPmsPayOrderTitleId() {
		return pmsPayOrderTitleId;
	}

	public void setPmsPayOrderTitleId(Integer pmsPayOrderTitleId) {
		this.pmsPayOrderTitleId = pmsPayOrderTitleId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
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

	public BigDecimal getInQty() {
		return inQty;
	}

	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}

	public BigDecimal getInPrice() {
		return inPrice;
	}

	public void setInPrice(BigDecimal inPrice) {
		this.inPrice = inPrice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
