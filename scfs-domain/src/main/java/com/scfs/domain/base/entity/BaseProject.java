package com.scfs.domain.base.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class BaseProject extends BaseEntity {

	private static final long serialVersionUID = -1145087772473005397L;
	private Integer id;
	/** 编号 */
	private String projectNo;
	private String projectNoType;
	/** 项目名称（简称） */
	private String projectName;
	/** 全称 */
	private String fullName;
	/** 财务专员 */
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
	/** 行业 */
	private Integer industrial;
	/** 状态 */
	private Integer status;
	/** 部门 **/
	private Integer departmentId;
	/** 业务专员 **/
	private Integer bizSpecialId;
	/** 部门主管 */
	private Integer departmentManagerId;
	/** 总经理 */
	private Integer bossId;

	/** 获取编码和简称 */
	public String getNoName() {
		if (StringUtils.isNotBlank(this.projectNo) && StringUtils.isNotBlank(this.projectName)) {
			return this.projectNo + "-" + this.projectName;
		} else if (StringUtils.isNotBlank(this.projectNo)) {
			return this.projectNo;
		} else if (StringUtils.isNotBlank(this.projectName)) {
			return this.projectName;
		} else {
			return "-";
		}
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

	public Integer getFinanceSpecialId() {
		return financeSpecialId;
	}

	public void setFinanceSpecialId(Integer financeSpecialId) {
		this.financeSpecialId = financeSpecialId;
	}

	public Integer getIndustrial() {
		return industrial;
	}

	public void setIndustrial(Integer industrial) {
		this.industrial = industrial;
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

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
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

	public Integer getBizSpecialId() {
		return bizSpecialId;
	}

	public void setBizSpecialId(Integer bizSpecialId) {
		this.bizSpecialId = bizSpecialId;
	}

	public Integer getLawId() {
		return lawId;
	}

	public void setLawId(Integer lawId) {
		this.lawId = lawId;
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