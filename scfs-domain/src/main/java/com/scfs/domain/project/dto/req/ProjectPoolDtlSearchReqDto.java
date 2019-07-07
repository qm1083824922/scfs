package com.scfs.domain.project.dto.req;

import java.util.Date;

import com.scfs.domain.BaseReqDto;

@SuppressWarnings("serial")
public class ProjectPoolDtlSearchReqDto extends BaseReqDto {

	/** ID */
	private Integer id;
	/** 类型 1-资金明细 2-资产明细 */
	private Integer type;
	/** 单据编号 */
	private String billNo;
	/** 单据类型 1-收款 2-付款 */
	private Integer billSource;
	/** 记账日期 */
	private Date businessDate;
	/** 项目ID */
	private Integer projectId;
	/** 客户ID */
	private Integer customerId;
	/** 供应商ID */
	private Integer supplierId;
	/**
	 * 账期
	 */
	private Integer fundAccountPeriod;
	private Integer fundClass;
	private Integer feeId;
	private Integer outStoreId;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public Integer getBillSource() {
		return billSource;
	}

	public void setBillSource(Integer billSource) {
		this.billSource = billSource;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public Integer getFundAccountPeriod() {
		return fundAccountPeriod;
	}

	public void setFundAccountPeriod(Integer fundAccountPeriod) {
		this.fundAccountPeriod = fundAccountPeriod;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getFundClass() {
		return fundClass;
	}

	public void setFundClass(Integer fundClass) {
		this.fundClass = fundClass;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public Integer getOutStoreId() {
		return outStoreId;
	}

	public void setOutStoreId(Integer outStoreId) {
		this.outStoreId = outStoreId;
	}

}
