package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

@SuppressWarnings("serial")
public class PayRefundRelation extends BaseEntity {
	/**
	 * 退款,付款，水单的关联id,自增长
	 */
	private Integer id;

	/**
	 * 付款项目的ID
	 */
	private Integer payId;

	/**
	 * 退款信息表的ID
	 */
	private Integer refundImId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 水单的ID
	 */
	private Integer receiptId;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 经营单位 关联tb_base_subject[id]
	 */
	private Integer busiUnit;

	/**
	 * 客户录入
	 */
	private Integer custId;

	/**
	 * 水单日期
	 */
	private Date receiptAt;

	/**
	 * 是否删除
	 */
	private Integer isDelete;

	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;

	/**
	 * 币种
	 * 
	 */
	private Integer currencyType;

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Date getReceiptAt() {
		return receiptAt;
	}

	public void setReceiptAt(Date receiptAt) {
		this.receiptAt = receiptAt;
	}

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

	public Integer getRefundImId() {
		return refundImId;
	}

	public void setRefundImId(Integer refundImId) {
		this.refundImId = refundImId;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}