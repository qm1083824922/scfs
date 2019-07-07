package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectPoolDtl {
	/** ID */
	private Integer id;
	/** 类型 1-资金明细 2-资产明细 */
	private Integer type;
	/** 单据编号 */
	private String billNo;
	/** 单据类型 1-收款 2-付款 */
	private Integer billSource;
	/** 项目ID */
	private Integer projectId;
	/** 客户ID */
	private Integer customerId;
	/** 供应商ID */
	private Integer supplierId;
	/** 记账日期 */
	private Date businessDate;
	/** 金额 */
	private BigDecimal amount;
	/** 金额(CNY) */
	private BigDecimal amountCny;
	/** 币种 1.人民币 2.美元 */
	private Integer currencyType;
	/** 汇率 */
	private BigDecimal exchangeRate;
	/** 备注 */
	private String remark;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountCny() {
		return amountCny;
	}

	public void setAmountCny(BigDecimal amountCny) {
		this.amountCny = amountCny;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
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
}