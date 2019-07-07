package com.scfs.domain.base.subject.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: QuerySubjectResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月29日				Administrator
 *
 * </pre>
 */
public class QuerySubjectResDto {
	/** 主键ID */
	private Integer id;
	/** 主体类型 */
	private Integer subjectType;
	/** 主体类型 */
	private String subjectTypeLabel;
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
	/** 供应商编号 */
	private String omsSupplierNo;
	/** 仓库编号 */
	private String warehouseNo;
	/** pms客户编号 */
	private String pmsCustNo;
	/** 仓库类型 */
	private Integer warehouseType;
	/** 仓库类型 */
	private String warehouseTypeLabel;
	/** 状态 */
	private Integer state;
	/** 状态 */
	private String stateLabel;
	/** 国家 */
	private Integer nation;
	/** 国家 */
	private String nationLabel;
	private String creator;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/** 开票限额类型 **/
	private BigDecimal invoiceQuotaType;
	/** 开票限额金额 **/
	private String invoiceQuotaAmount;
	/** 财务主管 */
	private Integer financeManagerId;
	/** 财务主管 */
	private String financeManagerName;
	/**
	 * 部门主管ID
	 */
	private Integer departmentManagerId;
	/**
	 * 部门主管
	 */
	private String departmentManagerName;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDITSUBJECT);
			operMap.put(OperateConsts.DETAIL, BaseUrlConsts.DETAILSUBJECT);
			operMap.put(OperateConsts.DELETE, BaseUrlConsts.DELETESUBJECT);
			operMap.put(OperateConsts.SUBMIT, BaseUrlConsts.SUBMITSUBJECT);
			operMap.put(OperateConsts.LOCK, BaseUrlConsts.LOCKSUBJECT);
			operMap.put(OperateConsts.UNLOCK, BaseUrlConsts.UNLOCKSUBJECT);
		}
	}

	public String getSubjectTypeLabel() {
		return subjectTypeLabel;
	}

	public void setSubjectTypeLabel(String subjectTypeLabel) {
		this.subjectTypeLabel = subjectTypeLabel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getOmsSupplierNo() {
		return omsSupplierNo;
	}

	public void setOmsSupplierNo(String omsSupplierNo) {
		this.omsSupplierNo = omsSupplierNo;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
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
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	public String getNationLabel() {
		return nationLabel;
	}

	public void setNationLabel(String nationLabel) {
		this.nationLabel = nationLabel;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Integer getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(Integer warehouseType) {
		this.warehouseType = warehouseType;
	}

	public String getWarehouseTypeLabel() {
		return warehouseTypeLabel;
	}

	public void setWarehouseTypeLabel(String warehouseTypeLabel) {
		this.warehouseTypeLabel = warehouseTypeLabel;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public BigDecimal getInvoiceQuotaType() {
		return invoiceQuotaType;
	}

	public void setInvoiceQuotaType(BigDecimal invoiceQuotaType) {
		this.invoiceQuotaType = invoiceQuotaType;
	}

	public String getInvoiceQuotaAmount() {
		return invoiceQuotaAmount;
	}

	public void setInvoiceQuotaAmount(String invoiceQuotaAmount) {
		this.invoiceQuotaAmount = invoiceQuotaAmount;
	}

	public Integer getFinanceManagerId() {
		return financeManagerId;
	}

	public void setFinanceManagerId(Integer financeManagerId) {
		this.financeManagerId = financeManagerId;
	}

	public String getFinanceManagerName() {
		return financeManagerName;
	}

	public void setFinanceManagerName(String financeManagerName) {
		this.financeManagerName = financeManagerName;
	}

	public Integer getDepartmentManagerId() {
		return departmentManagerId;
	}

	public void setDepartmentManagerId(Integer departmentManagerId) {
		this.departmentManagerId = departmentManagerId;
	}

	public String getDepartmentManagerName() {
		return departmentManagerName;
	}

	public void setDepartmentManagerName(String departmentManagerName) {
		this.departmentManagerName = departmentManagerName;
	}
}
