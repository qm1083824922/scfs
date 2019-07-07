package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年1月4日.
 */
public class BusinessAnalysisReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 317745847471948734L;
	/**
	 * 查询类型
	 */
	private Integer queryType;
	/**
	 * 经营单位ID
	 */
	private List<String> busiUnitIdList;
	/**
	 * 经营单位
	 */
	private List<String> busiUnitList;
	/**
	 * 年份
	 */
	private String year;
	/**
	 * 季节
	 */
	private String quarter;
	/**
	 * 月份
	 */
	private String month;
	/**
	 * 查询开始日期
	 */
	private String startDate;
	/**
	 * 查询结束日期
	 */
	private String endDate;
	/**
	 * 选择范围类型 0-年 1-季度 2-月份
	 */
	private Integer rangeType;
	/**
	 * 项目ID
	 */
	private Integer projectId;

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public List<String> getBusiUnitIdList() {
		return busiUnitIdList;
	}

	public void setBusiUnitIdList(List<String> busiUnitIdList) {
		this.busiUnitIdList = busiUnitIdList;
	}

	public List<String> getBusiUnitList() {
		return busiUnitList;
	}

	public void setBusiUnitList(List<String> busiUnitList) {
		this.busiUnitList = busiUnitList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public Integer getRangeType() {
		return rangeType;
	}

	public void setRangeType(Integer rangeType) {
		this.rangeType = rangeType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}
