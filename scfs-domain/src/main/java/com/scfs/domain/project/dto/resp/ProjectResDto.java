package com.scfs.domain.project.dto.resp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseProject;
import com.google.common.collect.Maps;

public class ProjectResDto extends BaseProject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1806510961875030875L;

	/** 经营单位 */
	private String businessUnitName;

	/** 业务类别 */
	private String bizTypeName;
	/** 法务 */
	private String lawName;
	/** 业务专员 */
	private String bizSpecialName;

	/** 业务主管 */
	private String bizManagerName;

	/** 商务主管 */
	private String businessManagerName;

	/** 财务主管 */
	private String financeManagerName;
	private String financeSpecialName;

	private String industrialName;
	private String totalAmountValue;
	private String ammountUnitValue;
	/** 风控专员 */
	private String riskSpecialName;
	/** 风控主管 */
	private String riskManagerName;
	/** 状态 */
	private String statusName;
	private String projectNoTypeName;
	/** 部门 */
	private String departmentName;
	/** 部门主管 */
	private String departmentManagerName;
	/** 总经理 */
	private String bossName;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAILPROJECT);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDITPROJECT);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMITPROJECT);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETEPROJECT);
			operMap.put(OperateConsts.LOCK, BusUrlConsts.LOCKPROJECT);
			operMap.put(OperateConsts.UNLOCK, BusUrlConsts.UNLOCKPROJECT);
			operMap.put(OperateConsts.COPY, BusUrlConsts.COPYPROJECT);
		}
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getIndustrialName() {
		return industrialName;
	}

	public void setIndustrialName(String industrialName) {
		this.industrialName = industrialName;
	}

	public String getFinanceSpecialName() {
		return financeSpecialName;
	}

	public void setFinanceSpecialName(String financeSpecialName) {
		this.financeSpecialName = financeSpecialName;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public String getBizManagerName() {
		return bizManagerName;
	}

	public String getProjectNoTypeName() {
		return projectNoTypeName;
	}

	public void setProjectNoTypeName(String projectNoTypeName) {
		this.projectNoTypeName = projectNoTypeName;
	}

	public void setBizManagerName(String bizManagerName) {
		this.bizManagerName = bizManagerName;
	}

	public String getAmmountUnitValue() {
		return ammountUnitValue;
	}

	public void setAmmountUnitValue(String ammountUnitValue) {
		this.ammountUnitValue = ammountUnitValue;
	}

	public String getBusinessManagerName() {
		return businessManagerName;
	}

	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}

	public String getFinanceManagerName() {
		return financeManagerName;
	}

	public void setFinanceManagerName(String financeManagerName) {
		this.financeManagerName = financeManagerName;
	}

	public String getRiskManagerName() {
		return riskManagerName;
	}

	public String getTotalAmountValue() {
		return totalAmountValue;
	}

	public void setTotalAmountValue(String totalAmountValue) {
		this.totalAmountValue = totalAmountValue;
	}

	public void setRiskManagerName(String riskManagerName) {
		this.riskManagerName = riskManagerName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRiskSpecialName() {
		return riskSpecialName;
	}

	public void setRiskSpecialName(String riskSpecialName) {
		this.riskSpecialName = riskSpecialName;
	}

	public String getBizSpecialName() {
		return bizSpecialName;
	}

	public void setBizSpecialName(String bizSpecialName) {
		this.bizSpecialName = bizSpecialName;
	}

	public String getLawName() {
		return lawName;
	}

	public void setLawName(String lawName) {
		this.lawName = lawName;
	}

	public String getDepartmentManagerName() {
		return departmentManagerName;
	}

	public void setDepartmentManagerName(String departmentManagerName) {
		this.departmentManagerName = departmentManagerName;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

}
