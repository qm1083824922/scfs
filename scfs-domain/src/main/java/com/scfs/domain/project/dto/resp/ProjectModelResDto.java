package com.scfs.domain.project.dto.resp;

import java.math.BigDecimal;

public class ProjectModelResDto {
	private Integer id;
	/** 编号 */
	private String projectNo;

	private String projectNoType;
	/** 项目名称（简称） */
	private String projectName;
	/** 全称 */
	private String fullName;
	private Integer financeSpecialId;
	/** 经营单位 */
	private Integer businessUnitId;
	/** 额度总额 */
	private BigDecimal totalAmount;
	/** 额度总额单位 */
	private Integer amountUnit;
	/** 业务类别 */
	private Integer bizType;
	/** 法务 */
	private Integer lawId;
	/** 业务专员 */
	private Integer bizSpecialId;
	/** 业务主管 */
	private Integer bizManagerId;
	/** 商务主管 */
	private Integer businessManagerId;
	/** 财务主管 */
	private Integer financeManagerId;
	/** 风控专员 */
	private Integer riskSpecialId;
	/** 风控主管 */
	private Integer riskManagerId;
	/** 部门主管 */
	private Integer departmentManagerId;
	/** 总经理 */
	private Integer bossId;
	/** 状态 */
	private Integer industrial;
	/** 状态 */
	private Integer status;
	private Integer departmentId;
	/** 经营单位 */
	private String businessUnitName;
	/** 业务类别 */
	private String bizTypeValue;
	/** 额度币种 */
	private String amountUnitValue;
	/** 项目编号类型 */
	private String projectNoTypeName;
	/** 法务 */
	private String lawName;
	/** 业务专员 */
	private String bizSpecialName;
	/** 业务主管 */
	private String bizManagerName;
	/** 财务专员 */
	private String financeSpecialName;
	/** 商务主管 */
	private String businessManagerName;
	/** 行业 */
	private String industrialName;
	/** 财务主管 */
	private String financeManagerName;
	/** 风控专员 */
	private String riskSpecialName;
	/** 风控主管 */
	private String riskManagerName;
	/** 状态 */
	private String statusValue;
	/** 部门 */
	private String departmentName;
	/** 部门主管 */
	private String departmentManagerName;
	/** 总经理 */
	private String bossName;

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getProjectNoTypeName() {
		return projectNoTypeName;
	}

	public void setProjectNoTypeName(String projectNoTypeName) {
		this.projectNoTypeName = projectNoTypeName;
	}

	public String getBizTypeValue() {
		return bizTypeValue;
	}

	public String getFinanceSpecialName() {
		return financeSpecialName;
	}

	public void setFinanceSpecialName(String financeSpecialName) {
		this.financeSpecialName = financeSpecialName;
	}

	public void setBizTypeValue(String bizTypeValue) {
		this.bizTypeValue = bizTypeValue;
	}

	public String getBizManagerName() {
		return bizManagerName;
	}

	public void setBizManagerName(String bizManagerName) {
		this.bizManagerName = bizManagerName;
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

	public void setRiskManagerName(String riskManagerName) {
		this.riskManagerName = riskManagerName;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getAmountUnitValue() {
		return amountUnitValue;
	}

	public void setAmountUnitValue(String amountUnitValue) {
		this.amountUnitValue = amountUnitValue;
	}

	public String getIndustrialName() {
		return industrialName;
	}

	public void setIndustrialName(String industrialName) {
		this.industrialName = industrialName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getProjectNoType() {
		return projectNoType;
	}

	public void setProjectNoType(String projectNoType) {
		this.projectNoType = projectNoType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getFinanceSpecialId() {
		return financeSpecialId;
	}

	public void setFinanceSpecialId(Integer financeSpecialId) {
		this.financeSpecialId = financeSpecialId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(Integer amountUnit) {
		this.amountUnit = amountUnit;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(Integer businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public Integer getFinanceManagerId() {
		return financeManagerId;
	}

	public void setFinanceManagerId(Integer financeManagerId) {
		this.financeManagerId = financeManagerId;
	}

	public Integer getRiskManagerId() {
		return riskManagerId;
	}

	public void setRiskManagerId(Integer riskManagerId) {
		this.riskManagerId = riskManagerId;
	}

	public Integer getIndustrial() {
		return industrial;
	}

	public void setIndustrial(Integer industrial) {
		this.industrial = industrial;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getRiskSpecialId() {
		return riskSpecialId;
	}

	public void setRiskSpecialId(Integer riskSpecialId) {
		this.riskSpecialId = riskSpecialId;
	}

	public String getRiskSpecialName() {
		return riskSpecialName;
	}

	public void setRiskSpecialName(String riskSpecialName) {
		this.riskSpecialName = riskSpecialName;
	}

	public Integer getBizSpecialId() {
		return bizSpecialId;
	}

	public void setBizSpecialId(Integer bizSpecialId) {
		this.bizSpecialId = bizSpecialId;
	}

	public String getBizSpecialName() {
		return bizSpecialName;
	}

	public void setBizSpecialName(String bizSpecialName) {
		this.bizSpecialName = bizSpecialName;
	}

	public Integer getLawId() {
		return lawId;
	}

	public void setLawId(Integer lawId) {
		this.lawId = lawId;
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

	public Integer getDepartmentManagerId() {
		return departmentManagerId;
	}

	public void setDepartmentManagerId(Integer departmentManagerId) {
		this.departmentManagerId = departmentManagerId;
	}

	public Integer getBossId() {
		return bossId;
	}

	public void setBossId(Integer bossId) {
		this.bossId = bossId;
	}

}
