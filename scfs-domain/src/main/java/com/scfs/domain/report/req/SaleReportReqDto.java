package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年2月13日.
 */
public class SaleReportReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5945925516750959021L;

	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 业务类型
	 */
	private Integer bizType;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 日期开始时间
	 */
	private String startBusinessDate;
	/**
	 * 日期结束时间
	 */
	private String endBusinessDate;
	/**
	 * 业务员ID
	 */
	private Integer bizManagerId;
	/**
	 * 部门ID
	 */
	private List<Integer> departmentId;
	/**
	 * 统计维度类型
	 */
	private Integer statisticsDimensionType;
	/**
	 * 内外部金额类型标识
	 */
	private Integer amountTypeFlag;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getStartBusinessDate() {
		return startBusinessDate;
	}

	public void setStartBusinessDate(String startBusinessDate) {
		this.startBusinessDate = startBusinessDate;
	}

	public String getEndBusinessDate() {
		return endBusinessDate;
	}

	public void setEndBusinessDate(String endBusinessDate) {
		this.endBusinessDate = endBusinessDate;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public List<Integer> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getStatisticsDimensionType() {
		return statisticsDimensionType;
	}

	public void setStatisticsDimensionType(Integer statisticsDimensionType) {
		this.statisticsDimensionType = statisticsDimensionType;
	}

	public Integer getAmountTypeFlag() {
		return amountTypeFlag;
	}

	public void setAmountTypeFlag(Integer amountTypeFlag) {
		this.amountTypeFlag = amountTypeFlag;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
