package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/10/27.
 */
public class UserProjectReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5281241479631537365L;
	/**
	 * 编号
	 */
	private String projectNo;
	/** 经营单位 */
	private Integer businessUnit;
	/** 项目名称 */
	private String projectName;
	/** 用户id **/
	private Integer sighId;

	/** 项目id **/
	private Integer departmentId;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSighId() {
		return sighId;
	}

	public void setSighId(Integer sighId) {
		this.sighId = sighId;
	}

}
