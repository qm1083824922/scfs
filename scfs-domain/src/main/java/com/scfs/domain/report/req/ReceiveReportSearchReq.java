package com.scfs.domain.report.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: ReceiveReportSearchReq.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月13日				Administrator
 *
 * </pre>
 */
public class ReceiveReportSearchReq extends BaseReqDto {
	private static final long serialVersionUID = -8541412079901262001L;

	private Integer projectId;
	private Integer custId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startCheckDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endCheckDate;
	private Integer statisticsDimension; // 1:项目 2:客户 3：项目客户
	private String departmentId;
	private Integer bizManagerId;
	private Integer currencyType;
	private Integer searchType;
	List<String> departmentList;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Date getStartCheckDate() {
		return startCheckDate;
	}

	public void setStartCheckDate(Date startCheckDate) {
		this.startCheckDate = startCheckDate;
	}

	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}

	public Integer getStatisticsDimension() {
		return statisticsDimension;
	}

	public void setStatisticsDimension(Integer statisticsDimension) {
		this.statisticsDimension = statisticsDimension;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public List<String> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<String> departmentList) {
		this.departmentList = departmentList;
	}

}
