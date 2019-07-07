package com.scfs.domain.base.subject.dto.req;

import java.io.Serializable;

/**
 * <pre>
 * 
 *  File: AccountDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AddAccountDto implements Serializable {
	/** 主体id */
	private int subjectId;
	/** 账户类型 */
	private int accountType;
	/** 账户编号 */
	private String accountNo;
	/** 银行代码 */
	private String bankCode;
	/** 开户银行 */
	private String bankName;
	/** 开户地址 */
	private String bankAddress;
	/** 电话 */
	private String phoneNumber;
	/** 默认币种 */
	private int defaultCurrency;
	private String bankSimple;
	/** 状态 */
	private int state;
	/** 创建人 */
	private String creator;
	/** 开户姓名 */
	private String accountor;
	/** 银行IBAN */
	private String iban;
	/** 资金占用 **/
	private Integer capitalAccountType;
	/** id **/
	private Integer id;

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(int defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getAccountor() {
		return accountor;
	}

	public void setAccountor(String accountor) {
		this.accountor = accountor;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBankSimple() {
		return bankSimple;
	}

	public void setBankSimple(String bankSimple) {
		this.bankSimple = bankSimple;
	}

	public Integer getCapitalAccountType() {
		return capitalAccountType;
	}

	public void setCapitalAccountType(Integer capitalAccountType) {
		this.capitalAccountType = capitalAccountType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
