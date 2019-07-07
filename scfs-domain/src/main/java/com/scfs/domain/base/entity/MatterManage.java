package com.scfs.domain.base.entity;

/**
 * <pre>
 *  事项管理
 *  File: MatterManage.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class MatterManage extends BaseEntity {
	/** 事项编码,自动生成 **/
	private String matterNo;
	/** 客户维护id **/
	private Integer custMainId;
	/** 事项名称,1 项目导入表 2 项目监控 **/
	private Integer matterName;
	/** 事项类型, 1 客户事项 2 项目事项 **/
	private Integer matterType;
	/** 项目 **/
	private Integer projectId;
	/** 客户名称 **/
	private String customerName;
	/** 客户简称 **/
	private String customerAbbreviate;
	/** 所处阶段 1 意向阶段 2 合作阶段 3 已取消 **/
	private Integer stage;
	/** 香港公司全称 **/
	private String hkCompany;
	/** 企业主营业务 **/
	private String enterpriseBus;
	/** 事项描述 **/
	private String matterDescribe;
	/** 办公地址 **/
	private String officeAddress;
	/** 注册地址 **/
	private String regAddress;
	/** 联系人 **/
	private String contacts;
	/** 联系电话 **/
	private String contactsNumber;
	/** 铺货商品描述 **/
	private String disGoods;
	/** 状态 0待提交 **/
	private Integer state;
	/** 备注 **/
	private String remark;
	/** 业务 */
	private Integer bizManagerId;
	/** 商务 */
	private Integer businessManagerId;
	/** 部门主管 **/
	private Integer departmentId;
	/** 法务 **/
	private Integer justiceId;
	/** 财务主管 */
	private Integer financeManagerId;
	/** 风控主管 */
	private Integer riskManagerId;

	public String getMatterNo() {
		return matterNo;
	}

	public void setMatterNo(String matterNo) {
		this.matterNo = matterNo;
	}

	public Integer getCustMainId() {
		return custMainId;
	}

	public void setCustMainId(Integer custMainId) {
		this.custMainId = custMainId;
	}

	public Integer getMatterName() {
		return matterName;
	}

	public void setMatterName(Integer matterName) {
		this.matterName = matterName;
	}

	public Integer getMatterType() {
		return matterType;
	}

	public void setMatterType(Integer matterType) {
		this.matterType = matterType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAbbreviate() {
		return customerAbbreviate;
	}

	public void setCustomerAbbreviate(String customerAbbreviate) {
		this.customerAbbreviate = customerAbbreviate;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public String getHkCompany() {
		return hkCompany;
	}

	public void setHkCompany(String hkCompany) {
		this.hkCompany = hkCompany;
	}

	public String getEnterpriseBus() {
		return enterpriseBus;
	}

	public void setEnterpriseBus(String enterpriseBus) {
		this.enterpriseBus = enterpriseBus;
	}

	public String getMatterDescribe() {
		return matterDescribe;
	}

	public void setMatterDescribe(String matterDescribe) {
		this.matterDescribe = matterDescribe;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsNumber() {
		return contactsNumber;
	}

	public void setContactsNumber(String contactsNumber) {
		this.contactsNumber = contactsNumber;
	}

	public String getDisGoods() {
		return disGoods;
	}

	public void setDisGoods(String disGoods) {
		this.disGoods = disGoods;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(Integer businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getJusticeId() {
		return justiceId;
	}

	public void setJusticeId(Integer justiceId) {
		this.justiceId = justiceId;
	}

	public Integer getFinanceManagerId() {
		return financeManagerId;
	}

	public void setFinanceManagerId(Integer financeManagerId) {
		this.financeManagerId = financeManagerId;
	}

	public Integer getRiskManagerId() {
		return riskManagerId;
	}

	public void setRiskManagerId(Integer riskManagerId) {
		this.riskManagerId = riskManagerId;
	}

}
