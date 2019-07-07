package com.scfs.domain.fee.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 	应收应付费用
 *  File: FeeRecPayReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年11月29日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeRecPayReqDto extends BaseReqDto {
	private Integer recFeeId;

	public Integer getRecFeeId() {
		return recFeeId;
	}

	public void setRecFeeId(Integer recFeeId) {
		this.recFeeId = recFeeId;
	}

}
