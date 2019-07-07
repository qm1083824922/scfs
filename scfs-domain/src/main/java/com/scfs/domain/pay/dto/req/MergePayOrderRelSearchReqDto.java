package com.scfs.domain.pay.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: MergePayOrderRelSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月3日				Administrator
 *
 * </pre>
 */
public class MergePayOrderRelSearchReqDto extends BaseReqDto {
	private static final long serialVersionUID = -1396305480421890295L;

	private Integer mergePayId;

	public Integer getMergePayId() {
		return mergePayId;
	}

	public void setMergePayId(Integer mergePayId) {
		this.mergePayId = mergePayId;
	}

}
