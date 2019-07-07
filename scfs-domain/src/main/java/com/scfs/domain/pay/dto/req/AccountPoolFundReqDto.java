package com.scfs.domain.pay.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: AccountPoolFundReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年09月26日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountPoolFundReqDto extends BaseReqDto {

	private Integer poolId;
	/** 账户ID **/
	private Integer accountId;
	/** 经营单位 **/
	private Integer busiUnit;
	/** 币种 **/
	private Integer currencyType;

	public Integer getPoolId() {
		return poolId;
	}

	public void setPoolId(Integer poolId) {
		this.poolId = poolId;
	}

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

}
