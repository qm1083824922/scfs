package com.scfs.domain.report.entity;

/**
 * Created by Administrator on 2017年1月7日.
 */
public class FeeSearch {
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 查询开始日期
	 */
	private String startDate;
	/**
	 * 查询结束日期
	 */
	private String endDate;
	/**
	 * 费用科目
	 */
	private String recFeeSpec;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRecFeeSpec() {
		return recFeeSpec;
	}

	public void setRecFeeSpec(String recFeeSpec) {
		this.recFeeSpec = recFeeSpec;
	}

}
