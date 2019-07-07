package com.scfs.domain.base.subject.dto.req;

import com.scfs.domain.base.entity.BaseSubject;

/**
 * <pre>
 * 
 *  File: AddSubjectDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AddSubjectDto extends BaseSubject {
	/** 主体类型 */
	private Integer subjectType;
	/** 主体编号 */
	private String subjectNo;
	/** 简称 */
	private String abbreviation;
	/** 中文名 */
	private String chineseName;
	/** 英文名 */
	private String englishName;
	/** 注册地址 */
	private String regPlace;
	/** 注册号 */
	private String regNo;
	/** 注册电话 */
	private String regPhone;
	/** 办公地址 */
	private String officeAddress;
	/** 供应商类型 */
	private Integer supplierType;
	/** 供应商编号 */
	private String omsSupplierNo;
	/** 仓库国家 */
	private Integer nation;
	/** 仓库地址 */
	private Integer warehouseType;
	/** 仓库编号 */
	private String warehouseNo;
	/** 客户类型 */
	private Integer custType;
	/** pms客户编号 */
	private String pmsCustNo;
	/** 状态 */
	private Integer state;
	/** 创建人 */
	private String creator;
	/**
	 * 财务主管
	 */
	private Integer financeManagerId;
	/**
	 * pms结算对象(供应商编码)
	 */
	private String pmsSupplierCode;

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
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

	public Integer getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(Integer supplierType) {
		this.supplierType = supplierType;
	}

	public String getOmsSupplierNo() {
		return omsSupplierNo;
	}

	public void setOmsSupplierNo(String omsSupplierNo) {
		this.omsSupplierNo = omsSupplierNo;
	}

	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	public Integer getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(Integer warehouseType) {
		this.warehouseType = warehouseType;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public Integer getCustType() {
		return custType;
	}

	public void setCustType(Integer custType) {
		this.custType = custType;
	}

	public String getPmsCustNo() {
		return pmsCustNo;
	}

	public void setPmsCustNo(String pmsCustNo) {
		this.pmsCustNo = pmsCustNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
		this.pmsSupplierCode = null;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPmsSupplierCode() {
		return pmsSupplierCode;
	}

	public void setPmsSupplierCode(String pmsSupplierCode) {
		this.pmsSupplierCode = pmsSupplierCode;
	}

	public Integer getFinanceManagerId() {
		return financeManagerId;
	}

	public void setFinanceManagerId(Integer financeManagerId) {
		this.financeManagerId = financeManagerId;
	}

}
