package com.scfs.domain.base.subject.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryInvoiceReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class QueryInvoiceReqDto extends BaseReqDto {
	private int subjectId;

	private int subjectType;

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

}
