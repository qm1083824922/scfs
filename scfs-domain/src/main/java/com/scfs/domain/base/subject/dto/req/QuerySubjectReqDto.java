package com.scfs.domain.base.subject.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: SelectBusiUnitDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class QuerySubjectReqDto extends BaseReqDto {
	private Integer subjectType;
	private String subjectNo;
	private String abbreviation;
	private String chineseName;
	private String englishName;
	private Integer state;
	private Integer supplierType;
	private Integer warehouseType;
	private Integer custType;
	private Integer nation;
	private String businessUnitCode;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(Integer supplierType) {
		this.supplierType = supplierType;
	}

	public Integer getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(Integer warehouseType) {
		this.warehouseType = warehouseType;
	}

	public Integer getCustType() {
		return custType;
	}

	public void setCustType(Integer custType) {
		this.custType = custType;
	}

	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	public String getBusinessUnitCode() {
		return businessUnitCode;
	}

	public void setBusinessUnitCode(String businessUnitCode) {
		this.businessUnitCode = businessUnitCode;
	}

	public String getPmsSupplierCode() {
		return pmsSupplierCode;
	}

	public void setPmsSupplierCode(String pmsSupplierCode) {
		this.pmsSupplierCode = pmsSupplierCode;
	}
}
