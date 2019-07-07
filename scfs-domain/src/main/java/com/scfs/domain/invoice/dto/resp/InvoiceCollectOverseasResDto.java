package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;

@SuppressWarnings("serial")
public class InvoiceCollectOverseasResDto extends BaseEntity {

	/** 境外收票id自增 **/
	private Integer id;

	/** 境外申请编号自动生成 **/
	private String applyNo;

	/** 经营单位 **/
	private Integer businessUnit;
	private String businessUnitName;

	/** 项目id **/
	private Integer projectId;
	private String projectName;

	/** 供应商id **/
	private Integer supplierId;
	private String supplierName;

	/** 发票号 **/
	private String invoiceNo;

	/** 单据类别 1-货物 2-费用 **/
	private Integer billType;
	private String billTypeName;

	/**
	 * 收款账号ID
	 */
	private Integer accountId;

	/**
	 * 发票金额
	 */
	private BigDecimal invoiceAmount;

	/** 发票日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date invoiceDate;

	/** 币种 **/
	private Integer currnecyType;

	/** 币种 **/
	private String currnecyTypeName;

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
	/** 认证备注 **/
	private String approveRemark;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createAt;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_INVOICE_COLLECT_OVER);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_INVOICE_COLLECT_OVER);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_INVOICE_COLLECT_OVER);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_INVOICE_COLLECT_OVER);
			operMap.put(OperateConsts.APPROVE, BusUrlConsts.APPROVE_INVOICE_COLLECT_OVER);
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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public String getApproveRemark() {
		return approveRemark;
	}

	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}
}
