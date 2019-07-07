package com.scfs.domain.base.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseExchangeRateResDto {

	/** 汇率ID */
	private Integer id;

	/** 银行名称 */
	private String bank;
	private String bankId;

	/** 币种 */
	private String currency;
	private String currencyId;

	/** 外币种 */
	private String foreignCurrency;
	private String foreignCurrencyId;

	/** 现钞卖出价 */
	private BigDecimal cashSellingPrice;

	/** 现钞买入价 */
	private BigDecimal cashBuyingPrice;

	/** 电汇卖出价 */
	private BigDecimal draftSellingPrice;

	/** 电汇买入价 */
	private BigDecimal draftBuyingPrice;

	/** 发布时间 */
	private Date publishAt;

	/** 备份人 */
	private String backupPerson;

	/** 创建人 */
	private String creator;

	/** 创建时间 */
	private Date createAt;
	/** 备份时间 */
	private Date backupAt;

	/** 异常标记 */
	private CodeValue isError;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDITEXCHANGERATE);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public BigDecimal getCashSellingPrice() {
		return cashSellingPrice;
	}

	public void setCashSellingPrice(BigDecimal cashSellingPrice) {
		this.cashSellingPrice = cashSellingPrice;
	}

	public BigDecimal getCashBuyingPrice() {
		return cashBuyingPrice;
	}

	public void setCashBuyingPrice(BigDecimal cashBuyingPrice) {
		this.cashBuyingPrice = cashBuyingPrice;
	}

	public BigDecimal getDraftSellingPrice() {
		return draftSellingPrice;
	}

	public void setDraftSellingPrice(BigDecimal draftSellingPrice) {
		this.draftSellingPrice = draftSellingPrice;
	}

	public BigDecimal getDraftBuyingPrice() {
		return draftBuyingPrice;
	}

	public void setDraftBuyingPrice(BigDecimal draftBuyingPrice) {
		this.draftBuyingPrice = draftBuyingPrice;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public String getBackupPerson() {
		return backupPerson;
	}

	public void setBackupPerson(String backupPerson) {
		this.backupPerson = backupPerson;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public CodeValue getIsError() {
		return isError;
	}

	public void setIsError(CodeValue isError) {
		this.isError = isError;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getBackupAt() {
		return backupAt;
	}

	public void setBackupAt(Date backupAt) {
		this.backupAt = backupAt;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(String foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}
}
