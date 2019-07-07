package com.scfs.domain.base.entity;

/**
 * <pre>
 *
 *  File: BaseAccount.java
 *  Date,					Who,
 *  2016年9月22日				Administrator
 *  账户信息表
 * </pre>
 */
/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class BaseAccount extends BaseEntity {
	/**
	 * 主体ID
	 */
	private Integer subjectId;
	/**
	 * 账户类型
	 */
	private Integer accountType;
	/**
	 * 银行代码
	 */
	private String bankCode;
	/**
	 * 开户银行
	 */
	private String bankName;
	/**
	 * 开户账号
	 */
	private String accountNo;
	/**
	 * 电话
	 */
	private String phoneNumber;
	/**
	 * 银行地址
	 */
	private String bankAddress;
	/**
	 * 默认币种
	 */
	private Integer defaultCurrency;
	/**
	 * 开户姓名
	 */
	private String accountor;
	/**
	 * 银行IBAN
	 */
	private String iban;
	private Integer state;
	private String bankSimple;
	/** 资金占用 **/
	private Integer capitalAccountType;

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
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

	public Integer getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(Integer defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getShowValue() {
		String str = "";
		if (bankSimple != null) {
			str += bankSimple;
		}
		if (accountNo != null) {
			if (accountNo.length() > 4) {
				str += " " + this.accountNo.substring(accountNo.length() - 4);
			} else {
				str += " " + this.accountNo;
			}
		}
		return str;
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

}
