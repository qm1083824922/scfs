package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: MergePayOrderRelResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */
public class MergePayOrderRelResDto {
	private Integer id;

	private Integer mergePayId;

	private Integer payId;

	private BigDecimal payAmount;

	private String creator;

	private Integer creatorId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
	private Date createAt;

	private Date updateAt;

	private String attachedNumbe;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requestPayTime;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date innerPayDate;

	private String payNo;

	private String currencyTypeName;

	private String stateName;

	private String sumProfit;

	private String discountRateStr;

	private Integer payWayType;
	private String payWayTypeName;

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
		this.creator = creator;
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

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Date getInnerPayDate() {
		return innerPayDate;
	}

	public void setInnerPayDate(Date innerPayDate) {
		this.innerPayDate = innerPayDate;
	}

	public String getSumProfit() {
		return sumProfit;
	}

	public void setSumProfit(String sumProfit) {
		this.sumProfit = sumProfit;
	}

	public String getDiscountRateStr() {
		return discountRateStr;
	}

	public void setDiscountRateStr(String discountRateStr) {
		this.discountRateStr = discountRateStr;
	}

	public Integer getPayWayType() {
		return payWayType;
	}

	public void setPayWayType(Integer payWayType) {
		this.payWayType = payWayType;
	}

	public String getPayWayTypeName() {
		return payWayTypeName;
	}

	public void setPayWayTypeName(String payWayTypeName) {
		this.payWayTypeName = payWayTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
