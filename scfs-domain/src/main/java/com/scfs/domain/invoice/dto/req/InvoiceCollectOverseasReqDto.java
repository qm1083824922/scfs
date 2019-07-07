package com.scfs.domain.invoice.dto.req;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectOverseasReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月15日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectOverseasReqDto extends BaseReqDto {
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

	/**
	 * 发票开始日期
	 */
	private String startInvoiceDate;
	/**
	 * 发票结束日期
	 */
	private String endInvoiceDate;
	/**
	 * 币种
	 */
	private Integer currnecyType;

	/** 发票日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date invoiceDate;

	/**
	 * 状态 1 待提交 20 待财务专员审核 30 待财务主管审核 2 已完成
	 */
	private Integer state;

	/**
	 * 认证日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approveDate;

	/**
	 * 认证备注
	 * 
	 */
	private String approveRemark;

	/**
	 * 是否删除0 否 1 是
	 */
	private Integer isDelete;

	/**
	 * 是否需要合计
	 */
	private Integer needSum;

	/** 用戶ID **/
	private Integer userId;

	/**
	 * 单据备注
	 */
	private String remark;

	/**
	 * 票据备注
	 */
	private String invoiceRemark;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
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
		this.invoiceNo = invoiceNo;
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

	public String getStartInvoiceDate() {
		return startInvoiceDate;
	}

	public void setStartInvoiceDate(String startInvoiceDate) {
		this.startInvoiceDate = startInvoiceDate;
	}

	public String getEndInvoiceDate() {
		return endInvoiceDate;
	}

	public void setEndInvoiceDate(String endInvoiceDate) {
		this.endInvoiceDate = endInvoiceDate;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getNeedSum() {
		return needSum;
	}

	public void setNeedSum(Integer needSum) {
		this.needSum = needSum;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public String getApproveRemark() {
		return approveRemark;
	}

	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}
}
