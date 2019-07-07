package com.scfs.domain.base.dto.resp;

import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;
import com.google.common.collect.Maps;

/**
 * <pre>
 *  事项管理
 *  File: MatterManageResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class MatterManageResDto extends BaseEntity {
	/** 事项编码,自动生成 **/
	private String matterNo;
	/** 客户维护id **/
	private Integer custMainId;
	/** 事项名称,1 项目导入表 2 项目监控 **/
	private Integer matterName;
	private String matterNameValue;
	/** 事项类型, 1 客户事项 2 项目事项 **/
	private Integer matterType;
	private String matterTypeValue;
	/** 项目 **/
	private Integer projectId;
	private String projectName;
	/** 客户名称 **/
	private String customerName;
	/** 客户简称 **/
	private String customerAbbreviate;
	/** 所处阶段 1 意向阶段 2 合作阶段 3 已取消 **/
	private Integer stage;
	private String stageValue;
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
	/** 业务审核人 */
	private Integer bizManagerId;
	private String bizManagerName;
	/** 商务审核人 */
	private Integer businessManagerId;
	private String businessManagerName;
	/** 部门主管审核人 **/
	private Integer departmentId;
	private String departmentIdName;
	/** 法务审核人 **/
	private Integer justiceId;
	private String justiceName;
	/** 财务主管审核人 */
	private Integer financeManagerId;
	private String financeManagerName;
	/** 风控主管审核人 */
	private Integer riskManagerId;
	private String riskManagerName;
	/** 状态 0待提交 **/
	private Integer state;
	private String stateValue;
	/** 备注 **/
	private String remark;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_MATTER_MANAGE);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_MATTER_MANAGE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_MATTER_MANAGE);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_MATTER_MANAGE);
		}
	}

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

	public String getMatterNameValue() {
		return matterNameValue;
	}

	public void setMatterNameValue(String matterNameValue) {
		this.matterNameValue = matterNameValue;
	}

	public Integer getMatterType() {
		return matterType;
	}

	public void setMatterType(Integer matterType) {
		this.matterType = matterType;
	}

	public String getMatterTypeValue() {
		return matterTypeValue;
	}

	public void setMatterTypeValue(String matterTypeValue) {
		this.matterTypeValue = matterTypeValue;
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

	public String getStageValue() {
		return stageValue;
	}

	public void setStageValue(String stageValue) {
		this.stageValue = stageValue;
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

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public String getBizManagerName() {
		return bizManagerName;
	}

	public void setBizManagerName(String bizManagerName) {
		this.bizManagerName = bizManagerName;
	}

	public Integer getBusinessManagerId() {
		return businessManagerId;
	}

	public void setBusinessManagerId(Integer businessManagerId) {
		this.businessManagerId = businessManagerId;
	}

	public String getBusinessManagerName() {
		return businessManagerName;
	}

	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentIdName() {
		return departmentIdName;
	}

	public void setDepartmentIdName(String departmentIdName) {
		this.departmentIdName = departmentIdName;
	}

	public Integer getJusticeId() {
		return justiceId;
	}

	public void setJusticeId(Integer justiceId) {
		this.justiceId = justiceId;
	}

	public String getJusticeName() {
		return justiceName;
	}

	public void setJusticeName(String justiceName) {
		this.justiceName = justiceName;
	}

	public Integer getFinanceManagerId() {
		return financeManagerId;
	}

	public void setFinanceManagerId(Integer financeManagerId) {
		this.financeManagerId = financeManagerId;
	}

	public String getFinanceManagerName() {
		return financeManagerName;
	}

	public void setFinanceManagerName(String financeManagerName) {
		this.financeManagerName = financeManagerName;
	}

	public Integer getRiskManagerId() {
		return riskManagerId;
	}

	public void setRiskManagerId(Integer riskManagerId) {
		this.riskManagerId = riskManagerId;
	}

	public String getRiskManagerName() {
		return riskManagerName;
	}

	public void setRiskManagerName(String riskManagerName) {
		this.riskManagerName = riskManagerName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateValue() {
		return stateValue;
	}

	public void setStateValue(String stateValue) {
		this.stateValue = stateValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
