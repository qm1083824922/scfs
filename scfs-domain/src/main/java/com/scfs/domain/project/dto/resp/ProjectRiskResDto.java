package com.scfs.domain.project.dto.resp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: ProjectRiskResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日				Administrator
 *
 * </pre>
 */
public class ProjectRiskResDto {
	/** 主键id **/
	private Integer id;

	/** 项目id **/
	private Integer projectId;
	private String projectName;

	/** 类型 **/
	private String risktype;

	/** 事件标题 **/
	private String title;

	/** 事件内容 **/
	private String remarks;

	/** 事件状态 **/
	private Integer status;
	private String statusName;

	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_PROJECT_RISK);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PROJECT_RISK);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_PROJECT_RISK);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_PROJECT_RISK);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
