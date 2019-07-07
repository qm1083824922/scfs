package com.scfs.domain.fee.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: FeeSpecSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月19日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeSpecSearchReqDto extends BaseReqDto {

	private Integer feeType;

	private String feeSpecNo;
	private String feeSpecName;

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public String getFeeSpecNo() {
		return feeSpecNo;
	}

	public void setFeeSpecNo(String feeSpecNo) {
		this.feeSpecNo = feeSpecNo;
	}

	public String getFeeSpecName() {
		return feeSpecName;
	}

	public void setFeeSpecName(String feeSpecName) {
		this.feeSpecName = feeSpecName;
	}

}
