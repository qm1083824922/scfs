package com.scfs.domain.pay.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: AccountPoolReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年09月22日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountPoolReqDto extends BaseReqDto {
	/** 账户ID **/
	private Integer accountId;
	/** 经营单位 **/
	private Integer busiUnit;
	/** 币种 **/
	private Integer currencyType;

	private List<Integer> paramList;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public List<Integer> getParamList() {
		return paramList;
	}

	public void setParamList(List<Integer> paramList) {
		this.paramList = paramList;
	}

}
