package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: RecLineSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RecLineSearchReqDto extends BaseReqDto {
	private Integer recId;
	// 1.未收 2.未核完 3.已核完 4.待核销(包含1,2) 5.已核销(包含2.3)
	private Integer searchType;
	private Integer billType;
	private Integer outStoreId;

	public Integer getRecId() {
		return recId;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getOutStoreId() {
		return outStoreId;
	}

	public void setOutStoreId(Integer outStoreId) {
		this.outStoreId = outStoreId;
	}

}
