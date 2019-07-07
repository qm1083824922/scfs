package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 *  资金周转率信息
 *  File: CapitalTurnoverResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月06日				Administrator
 *
 * </pre>
 */
public class CapitalTurnoverResDto {
	/** 主键ID */
	private Integer id;
	/** 部门 **/
	private Integer departmentId;
	private String departmentName;
	/** 项目 **/
	private Integer projectId;
	private String projectName;
	/** 销售金额 **/
	private BigDecimal saleAmount;
	/** 期初金额 **/
	private BigDecimal beginAmount;
	/** 期末金额 **/
	private BigDecimal endAmount;
	/** 资金周转率 **/
	private BigDecimal turnoverRate;
	private String turnoverRateStr;
	/** 币种 **/
	private Integer currencyType;
	private String currencyName;
	/** 月份 **/
	private String issue;
	/** 创建人id */
	private Integer creatorId;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getBeginAmount() {
		return beginAmount;
	}

	public void setBeginAmount(BigDecimal beginAmount) {
		this.beginAmount = beginAmount;
	}

	public BigDecimal getEndAmount() {
		return endAmount;
	}

	public void setEndAmount(BigDecimal endAmount) {
		this.endAmount = endAmount;
	}

	public BigDecimal getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(BigDecimal turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public String getTurnoverRateStr() {
		return turnoverRateStr;
	}

	public void setTurnoverRateStr(String turnoverRateStr) {
		this.turnoverRateStr = turnoverRateStr;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
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

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
