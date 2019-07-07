package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: AccountPoolResDto.java
 *  Description:资金池
 *  TODO
 *  Date,					Who,				
 *  2017年09月22日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountPoolResDto extends BaseEntity {
	/** 账户ID **/
	private Integer accountId;
	/** 开户账号 **/
	private String accountNo;
	/** 经营单位ID **/
	private Integer busiUnit;
	private String busiUnitName;
	/** 币种 **/
	private Integer currencyType;
	private String currencyTypeName;
	/** 可用资金 **/
	private BigDecimal availableAmount = BigDecimal.ZERO;
	/** 账户余额=内部水单金额-付款单（订单和费用类型）实际付款金额-付款手续费+（回款、预收）类型水单金额 **/
	private BigDecimal accountBalanceAmount = BigDecimal.ZERO;
	/** 利润 **/
	private BigDecimal profitAmount = BigDecimal.ZERO;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_ACCOUNT_POOL);
		}
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getAccountBalanceAmount() {
		return accountBalanceAmount;
	}

	public void setAccountBalanceAmount(BigDecimal accountBalanceAmount) {
		this.accountBalanceAmount = accountBalanceAmount;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
