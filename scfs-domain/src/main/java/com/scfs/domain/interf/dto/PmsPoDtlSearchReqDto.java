package com.scfs.domain.interf.dto;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: PmsPoDtlSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */
public class PmsPoDtlSearchReqDto extends BaseReqDto {
	private static final long serialVersionUID = 7555508157007679751L;
	private Integer titleId;

	public Integer getTitleId() {
		return titleId;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

}
