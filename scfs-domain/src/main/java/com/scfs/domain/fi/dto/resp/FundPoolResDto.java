package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: PayFundPoolResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月08日				Administrator
 *
 * </pre>
 */
public class FundPoolResDto {
	/**
	 * 资金池的主键ID
	 */
	private Integer id;

	/**
	 * 水单ID
	 */
	private Integer receiptId;

	/**
	 * 资金额度
	 */
	private BigDecimal countRecriptAmount;

	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;

	/** 经营单位名称 **/
	private String businessUnitName;

	/**
	 * 币种
	 */
	private Integer currencyType;

	/** 币种名称 **/
	private String currencyName;

	/**
	 * 已使用资金额度
	 */
	private BigDecimal usedFundAmount;

	/**
	 * 资金余额
	 */
	private BigDecimal remainFundAmount;

	/**
	 * 资产余额
	 */
	private BigDecimal remainAssetAmount;

	/**
	 * 资金额度（CNY）
	 */
	private BigDecimal countRecriptAmountCny;

	/**
	 * 已使用资金额度（CNY）
	 */
	private BigDecimal usedFundAmountCny;

	/**
	 * 资金余额（CNY）
	 */
	private BigDecimal remainFundAmountCny;

	/**
	 * 资产余额（CNY）
	 */
	private BigDecimal remainAssetAmountCny;

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
	 * 单据金额
	 */
	private BigDecimal billAmount;
	/** 尾差 **/
	private BigDecimal diffAmount;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
	/**
	 * 单据币种人民币
	 */
	private BigDecimal billAmountCny;
	/**
	 * 记账日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date businessDate;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createAt;
	/** 出入类型 **/
	private Integer type;
	/** 出入名称 **/
	private String typeName;
	/** 单据来源 类型为资金:1-收款 2-付款 类型为资产:1-收货 2-发货 **/
	private Integer billSource;
	/** 单据来源 类型为资金:1-收款 2-付款 类型为资产:1-收货 2-发货 **/
	private String billSourceName;
	/** 项目名称 **/
	private String projectName;
	/** 项目id **/
	private Integer project;
	/** 客户ID **/
	private Integer customerId;
	/** 客户名称 **/
	private String customerName;
	/** 供应商id **/
	private Integer supplierId;
	/** 供应商名称 **/
	private String supplieName;
	private String remark;
	/** 单据编号 **/
	private String billNo;
	/** 单据类型 **/
	private Integer billType;
	/** 单据类型 名称 **/
	private String billTypeName;
	/**
     * 预付款金额
     */
     private BigDecimal advancePayAmount;
     /**
      * 付货款金额
      */
     private BigDecimal paymentAmount;
     
     /**
      * 库存金额
      */
     private BigDecimal stlAmount;
     
     /**
      * 应收金额
      */
     private BigDecimal recAmount;
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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.QUERYPROJECTPOOLBYID);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public BigDecimal getCountRecriptAmount() {
		return countRecriptAmount;
	}

	public void setCountRecriptAmount(BigDecimal countRecriptAmount) {
		this.countRecriptAmount = countRecriptAmount;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getUsedFundAmount() {
		return usedFundAmount;
	}

	public void setUsedFundAmount(BigDecimal usedFundAmount) {
		this.usedFundAmount = usedFundAmount;
	}

	public BigDecimal getRemainFundAmount() {
		return remainFundAmount;
	}

	public void setRemainFundAmount(BigDecimal remainFundAmount) {
		this.remainFundAmount = remainFundAmount;
	}

	public BigDecimal getRemainAssetAmount() {
		return remainAssetAmount;
	}

	public void setRemainAssetAmount(BigDecimal remainAssetAmount) {
		this.remainAssetAmount = remainAssetAmount;
	}

	public BigDecimal getCountRecriptAmountCny() {
		return countRecriptAmountCny;
	}

	public void setCountRecriptAmountCny(BigDecimal countRecriptAmountCny) {
		this.countRecriptAmountCny = countRecriptAmountCny;
	}

	public BigDecimal getUsedFundAmountCny() {
		return usedFundAmountCny;
	}

	public void setUsedFundAmountCny(BigDecimal usedFundAmountCny) {
		this.usedFundAmountCny = usedFundAmountCny;
	}

	public BigDecimal getRemainFundAmountCny() {
		return remainFundAmountCny;
	}

	public void setRemainFundAmountCny(BigDecimal remainFundAmountCny) {
		this.remainFundAmountCny = remainFundAmountCny;
	}

	public BigDecimal getRemainAssetAmountCny() {
		return remainAssetAmountCny;
	}

	public void setRemainAssetAmountCny(BigDecimal remainAssetAmountCny) {
		this.remainAssetAmountCny = remainAssetAmountCny;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getBillAmountCny() {
		return billAmountCny;
	}

	public void setBillAmountCny(BigDecimal billAmountCny) {
		this.billAmountCny = billAmountCny;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getBillSource() {
		return billSource;
	}

	public void setBillSource(Integer billSource) {
		this.billSource = billSource;
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

	public Integer getProject() {
		return project;
	}

	public void setProject(Integer project) {
		this.project = project;
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

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplieName() {
		return supplieName;
	}

	public void setSupplieName(String supplieName) {
		this.supplieName = supplieName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}

	public BigDecimal getAdvancePayAmount() {
		return advancePayAmount;
	}

	public void setAdvancePayAmount(BigDecimal advancePayAmount) {
		this.advancePayAmount = advancePayAmount;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getStlAmount() {
		return stlAmount;
	}

	public void setStlAmount(BigDecimal stlAmount) {
		this.stlAmount = stlAmount;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

}
