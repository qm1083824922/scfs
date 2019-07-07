package com.scfs.domain.logistics.entity;

import java.math.BigDecimal;
import java.util.Date;

public class VerificationAdvance {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 销售单ID,关联tb_bill_delivery[id]
	 */
	private Integer billDeliveryId;

	/**
	 * 预收id
	 */
	private Integer advanceId;

	/**
	 * 水单id
	 */
	private Integer receiptId;

	/**
	 * 水单转预收生成的水单与预收关系id
	 */
	private Integer advanceReceiptRelId;

	/**
	 * 金额
	 */
	private BigDecimal amount;

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
	 * 修改时间
	 */
	private Date updateAt;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 逻辑删除标记
	 */
	private Integer isDelete;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 预收生成的水单id
	 */
	private Integer advanceReceiptId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(Integer advanceId) {
		this.advanceId = advanceId;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getAdvanceReceiptRelId() {
		return advanceReceiptRelId;
	}

	public void setAdvanceReceiptRelId(Integer advanceReceiptRelId) {
		this.advanceReceiptRelId = advanceReceiptRelId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAdvanceReceiptId() {
		return advanceReceiptId;
	}

	public void setAdvanceReceiptId(Integer advanceReceiptId) {
		this.advanceReceiptId = advanceReceiptId;
	}

}