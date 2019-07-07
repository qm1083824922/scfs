package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	境外发票基本信息
 *  File: InvoiceOverseasResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasResDto extends BaseEntity {
	/** 境外开票id **/
	private Integer id;

	/** 申请编号自动生成 **/
	private String applyNo;

	/** 经营单位 **/
	private Integer businessUnit;
	private String businessUnitName;
	/** 中文名 **/
	private String businessChineseName;
	/** 英文名 **/
	private String englishName;
	/** 注册地址 **/
	private String address;
	/** 注册电话 **/
	private String regPhone;

	/** 项目id **/
	private Integer projectId;
	private String projectName;

	/** 客户id **/
	private Integer customerId;
	private String customerName;
	private String customerChineseName;

	/** 币种 **/
	private Integer currnecyType;
	private String currnecyTypeName;

	/** 收款账户id **/
	private Integer accountId;
	/** 收款账户 **/
	private String showName;
	/** 收款银行开户人 **/
	private String subjectName;
	/** 收款银行账号 **/
	private String accountNo;
	/** 开户行 **/
	private String bankName;

	/** 打印次数 **/
	private Integer printNum;
	/** 申请金额 **/
	private BigDecimal invoiceAmount;
	/** 结算开始日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date balanceStartDate;

	/** 结算结束日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date balanceEndDate;

	/** 单据类型 **/
	private Integer billType;
	private String billTypeName;

	/** 开票明细 **/
	private Integer feeType;
	private String feeTypeName;

	/** 同品合并 0 否 1 是 **/
	private Integer isMerge;

	/** 票据备注 **/
	private String invoiceRemark;

	/** 单据备注 **/
	private String remark;

	/** 销售单统计 **/
	private BigDecimal sumNum;
	private BigDecimal sumAmount;

	/** 销售单 **/
	private String billNo;
	/** 注册地 **/
	private String regPlace;

	/** 状态 1 待提交 20 待财务专员审核 30 待财务主管审核 2 已完成 **/
	private Integer state;
	private String stateName;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_INVOICE_OVERSEAS);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_INVOICE_OVERSEAS);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_INVOICE_OVERSEAS);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_INVOICE_OVERSEAS);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_INVOICE_OVERSEAS);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getBusinessChineseName() {
		return businessChineseName;
	}

	public void setBusinessChineseName(String businessChineseName) {
		this.businessChineseName = businessChineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegPhone() {
		return regPhone;
	}

	public void setRegPhone(String regPhone) {
		this.regPhone = regPhone;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerChineseName() {
		return customerChineseName;
	}

	public void setCustomerChineseName(String customerChineseName) {
		this.customerChineseName = customerChineseName;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getCurrnecyTypeName() {
		return currnecyTypeName;
	}

	public void setCurrnecyTypeName(String currnecyTypeName) {
		this.currnecyTypeName = currnecyTypeName;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getBalanceStartDate() {
		return balanceStartDate;
	}

	public void setBalanceStartDate(Date balanceStartDate) {
		this.balanceStartDate = balanceStartDate;
	}

	public Date getBalanceEndDate() {
		return balanceEndDate;
	}

	public void setBalanceEndDate(Date balanceEndDate) {
		this.balanceEndDate = balanceEndDate;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public Integer getIsMerge() {
		return isMerge;
	}

	public void setIsMerge(Integer isMerge) {
		this.isMerge = isMerge;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getSumNum() {
		return sumNum;
	}

	public void setSumNum(BigDecimal sumNum) {
		this.sumNum = sumNum;
	}

	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRegPlace() {
		return regPlace;
	}

	public void setRegPlace(String regPlace) {
		this.regPlace = regPlace;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
