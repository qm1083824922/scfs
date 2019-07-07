package com.scfs.domain.base.subject.dto.resp;

import java.util.Date;

/**
 * <pre>
 * 
 *  File: QueryInvoiceResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月30日				Administrator
 *
 * </pre>
 */
public class QueryInvoiceResDto {
	private Integer id;
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
	/** 状态 */
	private String stateLabel;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date creatAt;

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

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

}
