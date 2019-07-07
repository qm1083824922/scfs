package com.scfs.domain.base.entity;

/**
 * <pre>
 *  客户维护信息
 *  File: CustomerMaintain.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CustomerMaintain extends BaseEntity {
	/** 基本信息id **/
	private Integer subjectId;
	/** 客户编号 **/
	private String customerNo;
	/** 客户类型 1 客户 2 供应商 3 经营单位 **/
	private Integer customerType;
	/** 来源渠道,1 供应商系统申请 2 自主开发 3 采购事业部推荐 **/
	private Integer sourceChannel;
	/** 客户简称 **/
	private String abbreviation;
	/** 中文全称 **/
	private String chineseName;
	/** 英文名称 **/
	private String englishName;
	/** 注册地 **/
	private String regPlace;
	/** 注册号 **/
	private String regNo;
	/** 注册电话 **/
	private String regPhone;
	/** 办公地址 **/
	private String officeAddress;
	/** 维护人 **/
	private Integer guardian;
	/** 跟进人 **/
	private Integer fllow;
	/** 联系人 **/
	private String contacts;
	/** 联系电话 **/
	private String contactsNumber;
	/** 其他联系方式 **/
	private String contactsOtherNumber;
	/** 所处阶段 1 意向阶段 2 合同签署 3 合作阶段 4 已关闭 **/
	private Integer stage;
	/** 备注 **/
	private String remark;

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public Integer getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(Integer sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getRegPlace() {
		return regPlace;
	}

	public void setRegPlace(String regPlace) {
		this.regPlace = regPlace;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getRegPhone() {
		return regPhone;
	}

	public void setRegPhone(String regPhone) {
		this.regPhone = regPhone;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public Integer getGuardian() {
		return guardian;
	}

	public void setGuardian(Integer guardian) {
		this.guardian = guardian;
	}

	public Integer getFllow() {
		return fllow;
	}

	public void setFllow(Integer fllow) {
		this.fllow = fllow;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsNumber() {
		return contactsNumber;
	}

	public void setContactsNumber(String contactsNumber) {
		this.contactsNumber = contactsNumber;
	}

	public String getContactsOtherNumber() {
		return contactsOtherNumber;
	}

	public void setContactsOtherNumber(String contactsOtherNumber) {
		this.contactsOtherNumber = contactsOtherNumber;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
