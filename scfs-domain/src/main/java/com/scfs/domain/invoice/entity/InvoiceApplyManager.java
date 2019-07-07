package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvoiceApplyManager {
	/** 自增ID */
	private Integer id;
	/** 申请编号 */
	private String applyNo;
	/** 申请类型 1-开票 2-收票 */
	private Integer applyType;
	/** 项目ID */
	private Integer projectId;
	/** 项目名称 */
	private String projectName;
	/** 申请金额 */
	private BigDecimal applyAmount;
	/** 客户ID */
	private Integer customerId;
	/** 客户ID */
	private String customerName;
	/** 经营单位ID */
	private Integer businessUnitId;
	private Integer printNum;
	/** 经营单位名称 */
	private String businessUnitName;
	private String customerNameAll;
	/** 经营单位名称 */
	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;
	/** 发票类型 1-增值税专用发票 2-增值税普通发票 */
	private Integer invoiceType;
	private String invoiceTypeName;
	/** 是否电子普通发票 0-否 1-是 */
	private Integer isElecInvoice;
	private String isElecInvoiceValue;
	/** 开票信息 关联tb_base_invoice */
	private Integer baseInvoiceId;
	/** 状态 1-待模拟 2-待提交 3-待财务审核 4-待确认 5-已完成 */
	private Integer status;
	/** 单据类别 1-货物 2-费用 */
	private Integer billType;
	/** * 单据类别 1-货物 2-费用 */
	private String billTypeName;
	/** 费用类型 1-服务费 2-操作费 3-仓储费 4运费 */
	private Integer feeType;
	private String feeTypeName;
	/** 1: 0 2: 0.06 3: 0.13 4: 0.17 */
	private BigDecimal invoiceTaxRate;
	/** 税收分类编码 */
	private String invoiceTaxRateValue;
	/** 发票编号 */
	private String invoiceNo;
	/** 发票号 */
	private String invoiceCode;
	/** 税收分类编码 */
	private String taxCateNo;
	/** 同品合并 0-否 1-是 */
	private Integer isGoodsMerge;
	/** 系统时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;

	private String isGoodsMergeValue;
	/** 显示折扣 0-否 1-是 */
	private Integer isDisplayDiscount;
	private String isDisplayDiscountValue;
	/** 固定折扣率 */
	private BigDecimal discount;
	/** 票据备注 */
	private String invoiceRemark;
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
	/** 作废人ID */
	private Integer deleterId;
	/** 作废人 */
	private String deleter;
	/** 删除标记 0 : 有效 1 : 删除 */
	private Integer isDelete;
	/** 删除时间 */
	private Date deleteAt;
	/** 开户银行 */
	private String bankName;
	/** 开户账号 */
	private String accountNo;
	/** 开票地址 */
	private String address;
	/** 开票电话 */
	private String phoneNumber;

	public String getInvoiceTaxRateValue() {
		return invoiceTaxRateValue;
	}

	public void setInvoiceTaxRateValue(String invoiceTaxRateValue) {
		this.invoiceTaxRateValue = invoiceTaxRateValue;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public String getCustomerNameAll() {
		return customerNameAll;
	}

	public void setCustomerNameAll(String customerNameAll) {
		this.customerNameAll = customerNameAll;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
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

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getInvoiceTypeName() {
		return invoiceTypeName;
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

	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public String getIsElecInvoiceValue() {
		return isElecInvoiceValue;
	}

	public void setIsElecInvoiceValue(String isElecInvoiceValue) {
		this.isElecInvoiceValue = isElecInvoiceValue;
	}

	public String getIsGoodsMergeValue() {
		return isGoodsMergeValue;
	}

	public void setIsGoodsMergeValue(String isGoodsMergeValue) {
		this.isGoodsMergeValue = isGoodsMergeValue;
	}

	public String getIsDisplayDiscountValue() {
		return isDisplayDiscountValue;
	}

	public void setIsDisplayDiscountValue(String isDisplayDiscountValue) {
		this.isDisplayDiscountValue = isDisplayDiscountValue;
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
		this.applyNo = applyNo == null ? null : applyNo.trim();
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
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

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getIsElecInvoice() {
		return isElecInvoice;
	}

	public void setIsElecInvoice(Integer isElecInvoice) {
		this.isElecInvoice = isElecInvoice;
	}

	public Integer getBaseInvoiceId() {
		return baseInvoiceId;
	}

	public void setBaseInvoiceId(Integer baseInvoiceId) {
		this.baseInvoiceId = baseInvoiceId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public BigDecimal getInvoiceTaxRate() {
		return invoiceTaxRate;
	}

	public void setInvoiceTaxRate(BigDecimal invoiceTaxRate) {
		this.invoiceTaxRate = invoiceTaxRate;
	}

	public String getTaxCateNo() {
		return taxCateNo;
	}

	public void setTaxCateNo(String taxCateNo) {
		this.taxCateNo = taxCateNo;
	}

	public Integer getIsGoodsMerge() {
		return isGoodsMerge;
	}

	public void setIsGoodsMerge(Integer isGoodsMerge) {
		this.isGoodsMerge = isGoodsMerge;
	}

	public Integer getIsDisplayDiscount() {
		return isDisplayDiscount;
	}

	public void setIsDisplayDiscount(Integer isDisplayDiscount) {
		this.isDisplayDiscount = isDisplayDiscount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark == null ? null : invoiceRemark.trim();
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

	public Integer getDeleterId() {
		return deleterId;
	}

	public void setDeleterId(Integer deleterId) {
		this.deleterId = deleterId;
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter == null ? null : deleter.trim();
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Date getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

}