package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: AdvanceSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AdvanceSearchReqDto extends BaseReqDto {
	private Integer id;
	/** 水单id **/
	private Integer receiptId;

	private Integer custId;
	private Integer busiUnit;
	private Integer projectId;

	private Integer advanceType;

	private Integer isNo;

	private Integer currencyType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
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

	public Integer getAdvanceType() {
		return advanceType;
	}

	public void setAdvanceType(Integer advanceType) {
		this.advanceType = advanceType;
	}

	public Integer getIsNo() {
		return isNo;
	}

	public void setIsNo(Integer isNo) {
		this.isNo = isNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
