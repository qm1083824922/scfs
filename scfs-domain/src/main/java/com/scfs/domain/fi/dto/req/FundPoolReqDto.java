package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: PayFundPoolReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月08日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FundPoolReqDto extends BaseReqDto {

	/** 水单的ID **/
	private Integer receiptID;

	/** 币种类型 **/
	private Integer currencyType;
	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;
	/** 1 :水单类型   2 付款单类型**/
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getReceiptID() {
		return receiptID;
	}

	public void setReceiptID(Integer receiptID) {
		this.receiptID = receiptID;
	}

}
