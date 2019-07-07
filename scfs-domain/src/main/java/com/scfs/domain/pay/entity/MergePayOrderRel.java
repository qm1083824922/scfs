package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

public class MergePayOrderRel {
	private Integer id;

	private Integer mergePayId;

	private Integer payId;

	private BigDecimal payAmount;

	private String creator;

	private Integer creatorId;

	private Date createAt;

	private Date updateAt;

	/*-----------------------扩展字段---------------------------*/

	private String attachedNumbe;

	private Date requestPayTime;

	private Date innerPayDate;

	private String payNo;

	private Integer currencyType;

	private Integer state;

	private BigDecimal discountAmount;

	private BigDecimal inDiscountAmount;

	private Integer payWayType;

	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMergePayId() {
		return mergePayId;
	}

	public void setMergePayId(Integer mergePayId) {
		this.mergePayId = mergePayId;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
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

	public String getAttachedNumbe() {
		return attachedNumbe;
	}

	public void setAttachedNumbe(String attachedNumbe) {
		this.attachedNumbe = attachedNumbe;
	}

	public Date getRequestPayTime() {
		return requestPayTime;
	}

	public void setRequestPayTime(Date requestPayTime) {
		this.requestPayTime = requestPayTime;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getInnerPayDate() {
		return innerPayDate;
	}

	public void setInnerPayDate(Date innerPayDate) {
		this.innerPayDate = innerPayDate;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getInDiscountAmount() {
		return inDiscountAmount;
	}

	public void setInDiscountAmount(BigDecimal inDiscountAmount) {
		this.inDiscountAmount = inDiscountAmount;
	}

	public Integer getPayWayType() {
		return payWayType;
	}

	public void setPayWayType(Integer payWayType) {
		this.payWayType = payWayType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}