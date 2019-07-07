package com.scfs.domain.base.subject.dto.resp;

/**
 * <pre>
 * 
 *  File: QueryAccountResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月30日				Administrator
 *
 * </pre>
 */
public class QueryAccountResDto {
	private Integer id;
	/** 主体ID */
	private Integer subjectId;
	/** 账户类型 */
	private Integer accountType;
	/** 账户类型 */
	private String accountTypeName;
	private String bankSimple;
	/** 银行代码 */
	private String bankCode;
	/** 开户银行 */
	private String bankName;
	/** 开户账号 */
	private String accountNo;
	/** 银行地址 */
	private String bankAddress;
	/** 电话 */
	private String phoneNumber;
	/** 默认币种 */
	private Integer defaultCurrency;
	/** 默认币种 */
	private String defaultCurrencyName;
	/** 状态 */
	private Integer state;
	/** 状态 */
	private String stateLabel;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private String creatAt;
	/** 开户人 **/
	private String subjectName;
	/** 开户姓名 */
	private String accountor;
	/** 银行IBAN */
	private String iban;
	/** 资金占用 **/
	private Integer capitalAccountType;
	private String capitalAccountName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(String creatAt) {
		this.creatAt = creatAt;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public Integer getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(Integer defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public String getDefaultCurrencyName() {
		return defaultCurrencyName;
	}

	public void setDefaultCurrencyName(String defaultCurrencyName) {
		this.defaultCurrencyName = defaultCurrencyName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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

	public String getCapitalAccountName() {
		return capitalAccountName;
	}

	public void setCapitalAccountName(String capitalAccountName) {
		this.capitalAccountName = capitalAccountName;
	}

}
