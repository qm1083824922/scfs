package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年9月27日.
 */
public class BaseUserReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971474657986876958L;

	/** ID */
	private Integer id;

	/** 工号 */
	private String employeeNumber;

	/** 用户名 */
	private String userName;

	/** 中文名 */
	private String chineseName;

	/** 英文名 */
	private String englishName;

	/** 手机 */
	private String mobilePhone;

	/** 状态 */
	private Integer status;

	/** 类型 */
	private Integer type;

	/** 角色id **/
	private Integer roleId;

	/** 项目id **/
	private Integer projectId;

	/** 主体id **/
	private Integer subjectId;

	/** 部门id **/
	private Integer departmentId;
	/** 角色名称 **/
	private String roleName;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

}
