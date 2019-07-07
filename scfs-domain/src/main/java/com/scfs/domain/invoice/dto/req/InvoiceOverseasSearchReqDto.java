package com.scfs.domain.invoice.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 	境外发票基本信息
 *  File: InvoiceOverseasSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasSearchReqDto extends BaseReqDto {
	/** 经营单位 **/
	private Integer businessUnit;
	/** 客户id **/
	private Integer customerId;
	/** 项目id **/
	private Integer projectId;
	/** 状态 **/
	private Integer state;
	/** 单据类型 **/
	private Integer billType;
	/** 申请编号 **/
	private String applyNo;

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

}
