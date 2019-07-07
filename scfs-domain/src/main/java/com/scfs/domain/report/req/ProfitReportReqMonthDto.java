package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.CodeValue;

/**
 * 
 * @author Administrator 2017-06-05
 *
 */
@SuppressWarnings("serial")
public class ProfitReportReqMonthDto extends BaseReqDto {
	/** 期号 **/
	private String issue;
	/** 项目id **/
	private Integer projectId;
	/** 业务类型 **/
	private Integer bizType;
	/** 开始统计时间 **/
	private String startStatisticsDate;
	/** 结束统计时间 **/
	private String endStatisticsDate;
	/** 当前用户下的项目 **/
	private List<CodeValue> codeList;

	private Integer userId;
	/** 业务类型 **/
	private Integer type;
	private String typeName;

	/** 部门id **/
	private Integer departmentId;
	/** 业务专员 **/
	private Integer bizSpecialId;
	/** 商务专员 **/
	private Integer businessManagerId;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

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

	public String getStartStatisticsDate() {
		return startStatisticsDate;
	}

	public void setStartStatisticsDate(String startStatisticsDate) {
		this.startStatisticsDate = startStatisticsDate;
	}

	public String getEndStatisticsDate() {
		return endStatisticsDate;
	}

	public void setEndStatisticsDate(String endStatisticsDate) {
		this.endStatisticsDate = endStatisticsDate;
	}

	public List<CodeValue> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CodeValue> codeList) {
		this.codeList = codeList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getBizSpecialId() {
		return bizSpecialId;
	}

	public void setBizSpecialId(Integer bizSpecialId) {
		this.bizSpecialId = bizSpecialId;
	}

	public Integer getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(Integer businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

}
