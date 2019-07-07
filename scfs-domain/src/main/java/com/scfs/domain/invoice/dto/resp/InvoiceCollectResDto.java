package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	收票信息
 *  File: InvoiceCollectResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectResDto extends BaseEntity {
	/** 收票id **/
	private Integer id;

	/** 申请编号 **/
	private String applyNo;

	/** 经营单位 **/
	private Integer businessUnit;
	private String businessUnitName;
	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;

	/** 项目id **/
	private Integer projectId;
	private String projectName;

	/** 供应商id **/
	private Integer supplierId;
	private String supplierName;

	/** 票据类型 1-增值税专用发票 2-增值税普通发票 **/
	private Integer invoiceType;
	private String invoiceTypeName;

	/** 单据类别 1-货物 2-费用 **/
	private Integer billType;
	private String billTypeName;

	/** 发票金额 **/
	private BigDecimal invoiceAmount;

	/** 发票号 **/
	private String invoiceNo;

	/** 发票日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date invoiceDate;

	/** 收票税率 **/
	private BigDecimal invoiceTaxRate;
	private String invoiceTaxRateValue;

	/** 票据备注 **/
	private String invoiceRemark;

	/** 状态 1 待提交 2 待财务审核 3 待认证 4 已完成 **/
	private Integer state;
	private String stateName;

	/** 单据备注 **/
	private String remark;

	/** 认证日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approveDate;

	/** 已认证金额 **/
	private BigDecimal approveAmount;

	/** 未认证金额 **/
	private BigDecimal unApproveAmount;

	/** 系统时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;

	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/** 当前认证日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date currApproveDate;
	/** 当前认证金额 **/
	private BigDecimal currApproveAmount;
	/** 当前认证备注 **/
	private String currApproveRemark;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_INVOICE_COLLECT);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_INVOICE_COLLECT);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_INVOICE_COLLECT);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_INVOICE_COLLECT);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_INVOICE_COLLECT);
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

	public String getBusinessUnitNameValue() {
		return businessUnitNameValue;
	}

	public void setBusinessUnitNameValue(String businessUnitNameValue) {
		this.businessUnitNameValue = businessUnitNameValue;
	}

	public String getBusinessUnitAddress() {
		return businessUnitAddress;
	}

	public void setBusinessUnitAddress(String businessUnitAddress) {
		this.businessUnitAddress = businessUnitAddress;
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

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTypeName() {
		return invoiceTypeName;
	}

	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
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

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public BigDecimal getInvoiceTaxRate() {
		return invoiceTaxRate;
	}

	public void setInvoiceTaxRate(BigDecimal invoiceTaxRate) {
		this.invoiceTaxRate = invoiceTaxRate;
	}

	public String getInvoiceTaxRateValue() {
		return invoiceTaxRateValue;
	}

	public void setInvoiceTaxRateValue(String invoiceTaxRateValue) {
		this.invoiceTaxRateValue = invoiceTaxRateValue;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public BigDecimal getApproveAmount() {
		return approveAmount;
	}

	public void setApproveAmount(BigDecimal approveAmount) {
		this.approveAmount = approveAmount;
	}

	public BigDecimal getUnApproveAmount() {
		return unApproveAmount;
	}

	public void setUnApproveAmount(BigDecimal unApproveAmount) {
		this.unApproveAmount = unApproveAmount;
	}

	public Date getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
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

	public Date getCurrApproveDate() {
		return currApproveDate;
	}

	public void setCurrApproveDate(Date currApproveDate) {
		this.currApproveDate = currApproveDate;
	}

	public BigDecimal getCurrApproveAmount() {
		return currApproveAmount;
	}

	public void setCurrApproveAmount(BigDecimal currApproveAmount) {
		this.currApproveAmount = currApproveAmount;
	}

	public String getCurrApproveRemark() {
		return currApproveRemark;
	}

	public void setCurrApproveRemark(String currApproveRemark) {
		this.currApproveRemark = currApproveRemark;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
