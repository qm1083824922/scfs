package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvoiceCollectApproveResDto {
	/**
	 * 收票认证id
	 */
	private Integer id;

	/**
	 * 收票id
	 */
	private Integer invoiceCollectId;

	/**
	 * 认证日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approveDate;

	/**
	 * 认证金额
	 */
	private BigDecimal approveAmount;

	/**
	 * 认证备注
	 */
	private String approveRemark;

	/**
	 * 认证人ID
	 */
	private Integer approverId;

	/**
	 * 认证人
	 */
	private String approver;

	/**
	 * 认证时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approverAt;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	/**
	 * 是否删除 0 否 1 是
	 */
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInvoiceCollectId() {
		return invoiceCollectId;
	}

	public void setInvoiceCollectId(Integer invoiceCollectId) {
		this.invoiceCollectId = invoiceCollectId;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public BigDecimal getApproveAmount() {
		return approveAmount;
	}

	public void setApproveAmount(BigDecimal approveAmount) {
		this.approveAmount = approveAmount;
	}

	public String getApproveRemark() {
		return approveRemark;
	}

	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark == null ? null : approveRemark.trim();
	}

	public Integer getApproverId() {
		return approverId;
	}

	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver == null ? null : approver.trim();
	}

	public Date getApproverAt() {
		return approverAt;
	}

	public void setApproverAt(Date approverAt) {
		this.approverAt = approverAt;
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
		this.creator = creator == null ? null : creator.trim();
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}