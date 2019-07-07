package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	Invoice收票基本信息
 *  File: InvoiceCollectOverseas.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月25日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectOverseas extends BaseEntity {
	/**
	 * 境外收票id自增
	 */
	private Integer id;

	/**
	 * 境外申请编号自动生成
	 */
	private String applyNo;

	/**
	 * 经营单位
	 */
	private Integer businessUnit;

	/**
	 * 项目id
	 */
	private Integer projectId;

	/**
	 * 供应商ID
	 */
	private Integer supplierId;

	/**
	 * 发票号
	 */
	private String invoiceNo;

	/**
	 * 单据类型(1-货物 2-费用)
	 */
	private Integer billType;

	/**
	 * 收款账号ID
	 */
	private Integer accountId;

	/**
	 * 发票金额
	 */
	private BigDecimal invoiceAmount;

	/** 发票数量 */
	private BigDecimal invoiceNum;

	/** 发票日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date invoiceDate;

	/**
	 * 币种
	 */
	private Integer currnecyType;

	/**
	 * 状态 1 待提交 20 待财务专员审核 30 待财务主管审核 2 已完成
	 */
	private Integer state;

	/**
	 * 认证日期
	 */
	/** 发票日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approveDate;

	/** 认证备注 **/
	private String approveRemark;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 是否删除0 否 1 是
	 */
	private Integer isDelete;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 单据备注
	 */
	private String remark;

	/**
	 * 票据备注
	 */
	private String invoiceRemark;

	private String orderNo;

	public Integer getId() {
		return id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo == null ? null : applyNo.trim();
	}

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	public BigDecimal getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(BigDecimal invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getApproveRemark() {
		return approveRemark;
	}

	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}
}