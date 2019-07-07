package com.scfs.domain.base.entity;

/**
 * <pre>
 * 
 *  File: BaseInvoice.java
 *  Date,					Who,					
 *  2016年9月22日				Administrator				
 *  开票信息表
 * </pre>
 */

@SuppressWarnings("serial")
public class BaseInvoice extends BaseEntity {
	/** 主体ID */
	private Integer subjectId;
	/** 纳税人识别号 */
	private String taxPayer;
	/** 开户银行 */
	private String bankName;
	/** 开户账号 */
	private String accountNo;
	/** 开票地址 */
	private String address;
	/** 开票电话 */
	private String phoneNumber;
	/** 状态 */
	private Integer state;

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getTaxPayer() {
		return taxPayer;
	}

	public void setTaxPayer(String taxPayer) {
		this.taxPayer = taxPayer;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getShowValue() {

		return this.accountNo + this.bankName;
	}

}
