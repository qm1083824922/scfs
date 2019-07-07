package com.scfs.domain.base.dto.resp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016年9月27日.
 */
public class BaseUserResDto {

	/** 主键ID */
	private int id;
	/** 工号 */
	private String employeeNumber;
	/** 角色名称 */
	private String roleName;
	/** 用户名 */
	private String userName;
	/** 密码 */
	private String password;
	/** 中文名 */
	private String chineseName;
	/** 英文名 */
	private String englishName;
	/** 手机 */
	private String mobilePhone;
	/** 邮箱 */
	private String email;
	/** 类型 */
	private Integer type;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	private String status;
	private String rtxCode;
	private Integer userProperty;
	private String userPropertyValue;
	private Integer departmentId;
	private String departmentName;
	private String operaterStr;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BaseUrlConsts.DETAILUSER);
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDITUSER);
			operMap.put(OperateConsts.SUBMIT, BaseUrlConsts.SUBMITUSER);
			operMap.put(OperateConsts.DELETE, BaseUrlConsts.DELETEUSER);
			operMap.put(OperateConsts.LOCK, BaseUrlConsts.LOCKUSER);
			operMap.put(OperateConsts.UNLOCK, BaseUrlConsts.UNLOCKUSER);
			operMap.put(OperateConsts.RESET, BaseUrlConsts.RESETPSW);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRtxCode() {
		return rtxCode;
	}

	public void setRtxCode(String rtxCode) {
		this.rtxCode = rtxCode;
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

	public Integer getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(Integer userProperty) {
		this.userProperty = userProperty;
	}

	public String getUserPropertyValue() {
		return userPropertyValue;
	}

	public void setUserPropertyValue(String userPropertyValue) {
		this.userPropertyValue = userPropertyValue;
	}

	public String getPassword() {
		return password;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public String getOperaterStr() {
		return operaterStr;
	}

	public void setOperaterStr(String operaterStr) {
		this.operaterStr = operaterStr;
	}

}
