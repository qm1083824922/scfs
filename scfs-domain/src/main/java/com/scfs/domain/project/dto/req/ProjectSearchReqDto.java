package com.scfs.domain.project.dto.req;

import java.util.Date;
import java.util.List;

import com.scfs.domain.BaseReqDto;

public class ProjectSearchReqDto extends BaseReqDto {

	private static final long serialVersionUID = 7282909059311225067L;

	/** 主键 */
	private Integer id;

	/** 项目编号 */
	private String projectNo;

	/** 项目名称 */
	private String projectName;
	private List<Integer> departmentId;

	/** 全称 */
	private String fullName;

	/** 经营单位 */
	private Integer businessUnitId;

	/** 额度总额 */
	private Integer totalAmount;

	/** 额度总额单位 */
	private String amountUnit;

	/** 业务类别 */
	private Integer bizType;
	private String projectNoType;

	/** 业务专员 */
	private Integer bizSpecialId;

	/** 业务主管 */
	private Integer bizManagerId;

	/** 商务主管 */
	private Integer businessManagerId;

	/** 财务主管 */
	private Integer financeManagerId;

	/** 风控主管 */
	private Integer riskManagerId;

	/** 状态 */
	private Integer status;

	/** 创建人 */
	private String creator;

	/** 创建时间 */
	private Date createAt;

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

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(String amountUnit) {
		this.amountUnit = amountUnit;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getBizSpecialId() {
		return bizSpecialId;
	}

	public void setBizSpecialId(Integer bizSpecialId) {
		this.bizSpecialId = bizSpecialId;
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

	public String getProjectNoType() {
		return projectNoType;
	}

	public void setProjectNoType(String projectNoType) {
		this.projectNoType = projectNoType;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public List<Integer> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
	}

}
