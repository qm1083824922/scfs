package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * 
 * <pre>
 * 
 *  File: Voucher.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class Voucher extends BaseEntity {
	/** 帐套id 关联tb_fi_account_book{id} **/
	private Integer accountBookId;

	/** 经营单位id 关联tb_base_subject{id} **/
	private Integer busiUnit;

	/** 凭证字 **/
	private Integer voucherWord;

	/** 凭证摘要 **/
	private String voucherSummary;

	/** 凭证编号 **/
	private String voucherNo;

	/** 贷方金额 **/
	private BigDecimal debitAmount;

	/** 贷方币种 **/
	private Integer debitCurrencyType;

	/** 借方金额 **/
	private BigDecimal creditAmount;

	/** 借方币种 **/
	private Integer creditCurrencyType;

	/** 费用单据id **/
	private Integer feeId;

	/** 出库单据id **/
	private Integer outStoreId;

	/** 入库单据id **/
	private Integer inStoreId;

	/** 付款单据id **/
	private Integer payId;

	/** 收票单据id **/
	private Integer acceptInvoiceId;

	/** 开票单据id **/
	private Integer provideInvoiceId;

	/** 水单单据id **/
	private Integer receiptId;

	/** 状态 **/
	private Integer state;

	/** 附单据数 **/
	private Integer attachmentNumber;

	/** 凭证日期，可能不是凭证创建日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date voucherDate;

	/** 分录数量 **/
	private Integer voucherLineNumber;

	private Integer billType;

	private String billNo;

	private Date billDate;

	public Integer getAccountBookId() {
		return accountBookId;
	}

	public void setAccountBookId(Integer accountBookId) {
		this.accountBookId = accountBookId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getVoucherWord() {
		return voucherWord;
	}

	public void setVoucherWord(Integer voucherWord) {
		this.voucherWord = voucherWord;
	}

	public String getVoucherSummary() {
		return voucherSummary;
	}

	public void setVoucherSummary(String voucherSummary) {
		this.voucherSummary = voucherSummary;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public Integer getOutStoreId() {
		return outStoreId;
	}

	public void setOutStoreId(Integer outStoreId) {
		this.outStoreId = outStoreId;
	}

	public Integer getInStoreId() {
		return inStoreId;
	}

	public void setInStoreId(Integer inStoreId) {
		this.inStoreId = inStoreId;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getAcceptInvoiceId() {
		return acceptInvoiceId;
	}

	public void setAcceptInvoiceId(Integer acceptInvoiceId) {
		this.acceptInvoiceId = acceptInvoiceId;
	}

	public Integer getProvideInvoiceId() {
		return provideInvoiceId;
	}

	public void setProvideInvoiceId(Integer provideInvoiceId) {
		this.provideInvoiceId = provideInvoiceId;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAttachmentNumber() {
		return attachmentNumber;
	}

	public void setAttachmentNumber(Integer attachmentNumber) {
		this.attachmentNumber = attachmentNumber;
	}

	public Integer getVoucherLineNumber() {
		return voucherLineNumber;
	}

	public void setVoucherLineNumber(Integer voucherLineNumber) {
		this.voucherLineNumber = voucherLineNumber;
	}

	public Date getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Integer getDebitCurrencyType() {
		return debitCurrencyType;
	}

	public void setDebitCurrencyType(Integer debitCurrencyType) {
		this.debitCurrencyType = debitCurrencyType;
	}

	public Integer getCreditCurrencyType() {
		return creditCurrencyType;
	}

	public void setCreditCurrencyType(Integer creditCurrencyType) {
		this.creditCurrencyType = creditCurrencyType;
	}

}
