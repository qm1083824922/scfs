package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvoiceInfo {

	/** 自增ID */
	private Integer id;
	/** 发票编号 */
	private String invoiceNo;
	/** 发票号 */
	private String invoiceCode;
	private Integer invoiceApplyId;
	/** 发票日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date invoiceDate;
	/** 申请编号 */
	private String invoiceApplyNo;
	/** 状态 1-已完成 2-已红冲 3-已作废 */
	private Integer status;
	/** 状态 1-已完成 2-已红冲 3-已作废 */
	private String statusName;
	/** 客户ID */
	private Integer customerId;
	/** 客户ID */
	private String customerName;
	/** 开票信息 关联tb_base_invoice */
	private Integer baseInvoiceId;
	/** 税额 */
	private BigDecimal rateAmount;
	/** 含税金额 */
	private BigDecimal inRateAmount;
	/** 未税金额 */
	private BigDecimal exRateAmount;
	/** 折扣税额 */
	private BigDecimal discountRateAmount;
	/** 折扣含税金额 */
	private BigDecimal discountInRateAmount;
	/** 折扣未税金额 */
	private BigDecimal discountExRateAmount;
	private BigDecimal invoiceInAmount;
	private BigDecimal invoiceExAmount;
	private BigDecimal invoiceRateAmount;
	/** 折扣率 */
	private BigDecimal discount;
	/** 票据备注 */
	private String invoiceRemark;
	/** 票据备注 */
	private String bankName;
	/** 票据备注 */
	private String address;
	/** 票据备注 */
	private String accountNo;
	/** 票据备注 */
	private String phoneNumber;
	/** 纳税人识别号 */
	private String taxPay;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建人 */
	private String unit;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;

	public BigDecimal getInvoiceInAmount() {
		return invoiceInAmount;
	}

	public void setInvoiceInAmount(BigDecimal invoiceInAmount) {
		this.invoiceInAmount = invoiceInAmount;
	}

	public BigDecimal getInvoiceExAmount() {
		return invoiceExAmount;
	}

	public void setInvoiceExAmount(BigDecimal invoiceExAmount) {
		this.invoiceExAmount = invoiceExAmount;
	}

	public BigDecimal getInvoiceRateAmount() {
		return invoiceRateAmount;
	}

	public void setInvoiceRateAmount(BigDecimal invoiceRateAmount) {
		this.invoiceRateAmount = invoiceRateAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode == null ? null : invoiceCode.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceApplyNo() {
		return invoiceApplyNo;
	}

	public void setInvoiceApplyNo(String invoiceApplyNo) {
		this.invoiceApplyNo = invoiceApplyNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBaseInvoiceId() {
		return baseInvoiceId;
	}

	public void setBaseInvoiceId(Integer baseInvoiceId) {
		this.baseInvoiceId = baseInvoiceId;
	}

	public BigDecimal getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(BigDecimal rateAmount) {
		this.rateAmount = rateAmount;
	}

	public BigDecimal getInRateAmount() {
		return inRateAmount;
	}

	public void setInRateAmount(BigDecimal inRateAmount) {
		this.inRateAmount = inRateAmount;
	}

	public BigDecimal getExRateAmount() {
		return exRateAmount;
	}

	public void setExRateAmount(BigDecimal exRateAmount) {
		this.exRateAmount = exRateAmount;
	}

	public BigDecimal getDiscountRateAmount() {
		return discountRateAmount;
	}

	public void setDiscountRateAmount(BigDecimal discountRateAmount) {
		this.discountRateAmount = discountRateAmount;
	}

	public BigDecimal getDiscountInRateAmount() {
		return discountInRateAmount;
	}

	public void setDiscountInRateAmount(BigDecimal discountInRateAmount) {
		this.discountInRateAmount = discountInRateAmount;
	}

	public BigDecimal getDiscountExRateAmount() {
		return discountExRateAmount;
	}

	public void setDiscountExRateAmount(BigDecimal discountExRateAmount) {
		this.discountExRateAmount = discountExRateAmount;
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

	public String getTaxPay() {
		return taxPay;
	}

	public void setTaxPay(String taxPay) {
		this.taxPay = taxPay;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getInvoiceApplyId() {
		return invoiceApplyId;
	}

	public void setInvoiceApplyId(Integer invoiceApplyId) {
		this.invoiceApplyId = invoiceApplyId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}