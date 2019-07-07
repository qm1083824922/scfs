package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ReceiptPoolAssest {
	/**
	 * 资金池资产明细ID
	 */
	private Integer id;

	/**
	 * 类型：入/出
	 */
	private Integer type;

	/**
	 * 单据编号
	 */
	private String billNo;

	/**
	 * 单据来源 类型为资金:1-收款 2-付款 类型为资产:1-收货 2-发货
	 */
	private Integer billSource;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 客户ID
	 */
	private Integer customerId;

	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;

	/**
	 * '供应商ID
	 */
	private Integer supplierId;

	/**
	 * 记账日期
	 */
	private Date businessDate;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 单据占用金额
	 */
	private BigDecimal billAmount;

	/**
	 * 单据币种
	 */
	private Integer billCurrencyType;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	/**
	 * 单据ID
	 */
	private Integer billId;

	/**
	 * 单据类型
	 */
	private Integer billType;
	/** 汇率 **/
	private BigDecimal exchangeRate;
	/** 转换为人民币后的金额 **/
	private BigDecimal amountCny;

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getAmountCny() {
		return amountCny;
	}

	public void setAmountCny(BigDecimal amountCny) {
		this.amountCny = amountCny;
	}

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

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	
}