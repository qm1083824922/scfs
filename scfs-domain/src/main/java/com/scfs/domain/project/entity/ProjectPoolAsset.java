package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProjectPoolAsset {
	/** ID */
	private Integer id;
	/** 类型：入/出 */
	private Integer type;
	private String typeName;

	/** 单据编号 */
	private String billNo;
	/** 单据来源 类型为资金:1-收款 2-付款 类型为资产:1-收货 2-发货 */
	private Integer billSource;

	private String billSourceName;
	private String projectName;
	private String customerName;
	private String supplierName;
	private String billCurrencyTypeName;

	/** 项目ID */
	private Integer projectId;
	/** 客户ID */
	private Integer customerId;
	/** 供应商ID */
	private Integer supplierId;
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date businessDate;
	/** 单据占用金额 */
	private BigDecimal billAmount;
	/** 单据币种 */
	private Integer billCurrencyType;
	/** 单据币种对项目币种汇率 */
	private BigDecimal billProjectExchangeRate;
	/** * 单据币种对人民币的汇率 */
	private BigDecimal billCnyExchangeRate;
	/** 项目占用金额 */
	private BigDecimal projectAmount;
	private String projectAmountValue;
	/** 项目币种 */
	private Integer projectCurrencyType;
	/** 人民币占用金额 */
	private BigDecimal cnyAmount;
	/** 备注 */
	private String remark;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;
	/** 冗余金额 如果类型为入，则为入冗余金额，如果为出，则为出冗余金额 */
	private BigDecimal fundRedant;
	private String cnyAmountValue;
	private String billAmountValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public Integer getBillSource() {
		return billSource;
	}

	public void setBillSource(Integer billSource) {
		this.billSource = billSource;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public Integer getBillCurrencyType() {
		return billCurrencyType;
	}

	public void setBillCurrencyType(Integer billCurrencyType) {
		this.billCurrencyType = billCurrencyType;
	}

	public BigDecimal getBillProjectExchangeRate() {
		return billProjectExchangeRate;
	}

	public void setBillProjectExchangeRate(BigDecimal billProjectExchangeRate) {
		this.billProjectExchangeRate = billProjectExchangeRate;
	}

	public String getCnyAmountValue() {
		return cnyAmountValue;
	}

	public void setCnyAmountValue(String cnyAmountValue) {
		this.cnyAmountValue = cnyAmountValue;
	}

	public String getBillAmountValue() {
		return billAmountValue;
	}

	public void setBillAmountValue(String billAmountValue) {
		this.billAmountValue = billAmountValue;
	}

	public BigDecimal getBillCnyExchangeRate() {
		return billCnyExchangeRate;
	}

	public void setBillCnyExchangeRate(BigDecimal billCnyExchangeRate) {
		this.billCnyExchangeRate = billCnyExchangeRate;
	}

	public BigDecimal getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(BigDecimal projectAmount) {
		this.projectAmount = projectAmount;
	}

	public Integer getProjectCurrencyType() {
		return projectCurrencyType;
	}

	public void setProjectCurrencyType(Integer projectCurrencyType) {
		this.projectCurrencyType = projectCurrencyType;
	}

	public BigDecimal getCnyAmount() {
		return cnyAmount;
	}

	public void setCnyAmount(BigDecimal cnyAmount) {
		this.cnyAmount = cnyAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public BigDecimal getFundRedant() {
		return fundRedant;
	}

	public void setFundRedant(BigDecimal fundRedant) {
		this.fundRedant = fundRedant;
	}

	public String getBillSourceName() {
		return billSourceName;
	}

	public void setBillSourceName(String billSourceName) {
		this.billSourceName = billSourceName;
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBillCurrencyTypeName() {
		return billCurrencyTypeName;
	}

	public void setBillCurrencyTypeName(String billCurrencyTypeName) {
		this.billCurrencyTypeName = billCurrencyTypeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getProjectAmountValue() {
		return projectAmountValue;
	}

	public void setProjectAmountValue(String projectAmountValue) {
		this.projectAmountValue = projectAmountValue;
	}

}