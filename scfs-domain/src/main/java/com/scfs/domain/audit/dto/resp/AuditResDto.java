package com.scfs.domain.audit.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class AuditResDto {

	private Integer id;

	private Integer poId;

	/** 单据类型，1表示采购单，2表示費用, 后续业务一次往后添加 */
	private Integer poType;

	/** 单据编号 */
	private String poNo;

	/** 单据日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date poDate;

	/** 申请人 */
	private Integer proposerId;

	/** 申请人 */
	private String proposer;

	/** 申请时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date proposerAt;

	/** 当前审核人 */
	private Integer auditorId;

	/** 当前审核人 */
	private String auditor;

	/** 币种 **/
	private Integer currencyId;
	private String currencyName;

	/** 金额 */
	private BigDecimal amount;

	/** 经营单位ID */
	private Integer businessUnitId;
	/** 项目ID */
	private Integer projectId;
	/** 供应商ID */
	private Integer supplierId;
	/** 客户ID */
	private Integer customerId;
	/** 状态 */
	private Integer state;

	/** 经营单位 */
	private String businessUnitName;

	/** 项目 */
	private String projectName;

	/** 供应商 */
	private String supplierName;

	/** 客户 */
	private String customerName;

	/** 单据类型 */
	private String poTypeName;

	/** 状态 */
	private String stateName;
	/** 截止到目前的申请时间（天数，小时数） */
	private String proposerDayTime;
	/** 是否标红 */
	private boolean isWarn;

	public boolean isWarn() {
		return isWarn;
	}

	public void setWarn(boolean warn) {
		isWarn = warn;
	}

	public String getProposerDayTime() {
		return proposerDayTime;
	}

	public void setProposerDayTime(String proposerDayTime) {
		this.proposerDayTime = proposerDayTime;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPoTypeName() {
		return poTypeName;
	}

	public void setPoTypeName(String poTypeName) {
		this.poTypeName = poTypeName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoType() {
		return poType;
	}

	public void setPoType(Integer poType) {
		this.poType = poType;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getProposerId() {
		return proposerId;
	}

	public void setProposerId(Integer proposerId) {
		this.proposerId = proposerId;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Date getProposerAt() {
		return proposerAt;
	}

	public void setProposerAt(Date proposerAt) {
		this.proposerAt = proposerAt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

}
