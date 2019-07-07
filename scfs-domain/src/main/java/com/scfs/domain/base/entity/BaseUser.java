package com.scfs.domain.base.entity;

/**
 * 用户
 * 
 * @author 
 *
 */
public class BaseUser extends BaseEntity {

	private static final long serialVersionUID = -4192245372402251382L;

	/** 工号 */
	private String employeeNumber;

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

	/** 状态 */
	private Integer status;

	/** 类型 */
	private Integer type;
	private String rtxCode;
	/** 用户属性 0为内置员工 1为外部员工 */
	private Integer userProperty;
	private Integer departmentId;
	/** 仓管操作;0 否 1 是 **/
	private Integer operater;

	public String getRtxCode() {
		return rtxCode;
	}

	public Integer getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(Integer userProperty) {
		this.userProperty = userProperty;
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

	public String getPassword() {
		return password;
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

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getOperater() {
		return operater;
	}

	public void setOperater(Integer operater) {
		this.operater = operater;
	}
}
