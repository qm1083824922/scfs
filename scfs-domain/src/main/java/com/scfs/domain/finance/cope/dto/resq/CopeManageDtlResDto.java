package com.scfs.domain.finance.cope.dto.resq;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  应付管理明细
 *  File: CopeDtl.java
 *  Description:
 *  TODO
 *  Date;					Who;				
 *  2017年10月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CopeManageDtlResDto extends BaseEntity {
	/** 管理id **/
	private Integer copeId;
	/** 单据id **/
	private Integer billId;
	/** 凭证明细id **/
	private Integer voucherLineId;
	/** 项目id **/
	private Integer projectId;
	private String projectName;
	/** 客户id **/
	private Integer customerId;
	private String customerName;
	/** 经营单位 **/
	private Integer busiUnitId;
	private String busiUnitName;
	/** 币种 **/
	private Integer currnecyType;
	private String currnecyTypeName;
	/** 单据编号 **/
	private String billNumber;
	/** 单据日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date billDate;
	/** 单据金额 **/
	private BigDecimal billAmount;
	/** 应付金额(核销金额) **/
	private BigDecimal copeAmount;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 未付金额 **/
	private BigDecimal unpaidAmount;

	public Integer getCopeId() {
		return copeId;
	}

	public void setCopeId(Integer copeId) {
		this.copeId = copeId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getVoucherLineId() {
		return voucherLineId;
	}

	public void setVoucherLineId(Integer voucherLineId) {
		this.voucherLineId = voucherLineId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getCurrnecyTypeName() {
		return currnecyTypeName;
	}

	public void setCurrnecyTypeName(String currnecyTypeName) {
		this.currnecyTypeName = currnecyTypeName;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getCopeAmount() {
		return copeAmount;
	}

	public void setCopeAmount(BigDecimal copeAmount) {
		this.copeAmount = copeAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

}
