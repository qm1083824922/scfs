package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

@SuppressWarnings("serial")
public class RefundInformation extends BaseEntity {
	/**
	 * 退款信息ID，自增长
	 */
	private Integer id;

	/**
	 * 客户
	 */
	private Integer custId;

	/**
	 * 经营单位
	 */
	private Integer busiUnit;

	/**
	 * 项目
	 */
	private Integer projectId;

	/**
	 * 币种
	 */
	private Integer currencyType;

	/**
	 * 水单日期
	 */
	private Date receiptDate;

	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;

	/**
	 * 是否删除
	 */
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}