package com.scfs.domain.base.subject.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryAccountReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class QueryAccountReqDto extends BaseReqDto {
	private Integer subjectId;

	private Integer subjectType;
	private Integer accountType;
	private Integer currencyType;

	private Integer state;

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
