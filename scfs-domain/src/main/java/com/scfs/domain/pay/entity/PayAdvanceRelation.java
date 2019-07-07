package com.scfs.domain.pay.entity;

import com.scfs.domain.base.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <pre>
 *  付款预收订单关系
 *  File: PayAdvanceRelation.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayAdvanceRelation extends BaseEntity {
	/** 付款预收订单关系id **/
	private Integer id;
	/** 付款id **/
	private Integer payId;
	/** 预收id **/
	private Integer advanceId; // 预收定金类型的水单id
	/** 金额 **/
	private BigDecimal payAmount;
	/** 币种 **/
	private Integer currencyType;
	/** 项目 关联tb_base_project[id] **/
	private Integer projectId;
	/** 客户 关联tb_base_subject[id] **/
	private Integer custId;
	/** 经营单位 关联tb_base_subject[id] **/
	private Integer busiUnit;
	/** 已付款金额 */
	private BigDecimal paidAmount;
	/** 预收金额 */
	private BigDecimal preRecSum;
	private String payNo;
	/** 水单日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receiptDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(Integer advanceId) {
		this.advanceId = advanceId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getPreRecSum() {
		return preRecSum;
	}

	public void setPreRecSum(BigDecimal preRecSum) {
		this.preRecSum = preRecSum;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

}