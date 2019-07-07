package com.scfs.domain.base.subject.dto.resp;

import java.util.Date;

/**
 * <pre>
 * 
 *  File: QueryAddressResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月30日				Administrator
 *
 * </pre>
 */
public class QueryAddressResDto {
	private int id;
	/** 主体ID */
	private int subjectId;
	/** 地址类型 */
	private Integer addressType;
	/** 地址类型 */
	private String addressTypeName;
	/** 国家 */
	private Integer nationId;
	/** 国家名称 */
	private String nationName;
	/** 省 */
	private Integer provinceId;
	/** 省名称 */
	private String provinceName;
	/** 市 */
	private Integer cityId;
	/** 市名称 */
	private String cityName;
	/** 县 */
	private Integer countyId;
	/** 县名称 */
	private String countyName;
	/** 地址详情 */
	private String addressDetail;
	/** 联系人 */
	private String contactPerson;
	/** 手机 */
	private String mobilePhone;
	/** 电话 */
	private String telephone;
	/** 传真 */
	private String fax;
	/** 备注 */
	private String note;
	/** 状态 */
	private Integer state;
	/** 状态 */
	private String stateLabel;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date creatAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getAddressType() {
		return addressType;
	}

	public void setAddressType(Integer addressType) {
		this.addressType = addressType;
	}

	public String getAddressTypeName() {
		return addressTypeName;
	}

	public void setAddressTypeName(String addressTypeName) {
		this.addressTypeName = addressTypeName;
	}

	public Integer getNationId() {
		return nationId;
	}

	public void setNationId(Integer nationId) {
		this.nationId = nationId;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public Date getCreatAt() {
		return creatAt;
	}

}
