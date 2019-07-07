package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: BankReceipt.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class BankReceipt extends BaseEntity {
	/** 水单id **/
	private Integer id;
	/** 来源水单id **/
	private Integer pid;
	/** 客户 **/
	private Integer custId;
	/** 经营单位 关联tb_base_subject[id] **/
	private Integer busiUnit;
	/** 项目 **/
	private Integer projectId;
	/** 系统生成的水单编号 **/
	private String receiptNo;
	/** 水单备注 **/
	private String receiptNote;
	/** 水单日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receiptDate;
	/** 银行水单号 **/
	private String bankReceiptNo;
	/** 银行水单备注 **/
	private String bankReceiptNote;
	/** 收款账号 **/
	private Integer recAccountNo;
	/** 币种 **/
	private Integer currencyType;
	/** 水单金额 **/
	private BigDecimal receiptAmount;
	/** 水单类型 1 回款 2 预收定金 3 预收货款 4融资 5内部 **/
	private Integer receiptType;
	/** 转水单预收id **/
	private Integer preRecId;
	/** 核销金额 **/
	private BigDecimal writeOffAmount;
	/** 尾差 **/
	private BigDecimal diffAmount;
	/** 预收金额 **/
	private BigDecimal preRecAmount;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 状态 **/
	private Integer state;
	/** 创建人 **/
	private String creator;
	/** 创建人id **/
	private Integer creatorId;
	/** 创建时间 **/
	private Date createAt;
	/** 修改时间 **/
	private Date updateAt;
	/** 摘要 **/
	private String summary;

	/** 收款方式 **/
	private Integer receiptWay;
	/** 开立类型 **/
	private Integer openType; 
	/** 开立日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date openDate;
	/**
	 * 余额
	 */
	private BigDecimal remainAmount;
	/**
	 * 销售单关联水单金额
	 */
	private BigDecimal verificationAdvanceAmount;

	/** 付款单位 **/
	private String payUnit;
	/** 核完人**/
	private Integer writeOfftorId;
	/** 核完时间**/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date writeOffAt;
	private Integer billOutStlId;
	
	/** 实际水单金额**/
	private BigDecimal actualReceiptAmount;
	/** 实际核销金额**/
	private BigDecimal actualWriteOffAmount;
	/** 实际尾差（抹零金额）**/
	private BigDecimal actualDiffAmount;
	/** 实际预收金额（水单金额-核销金额+尾差）**/
	private BigDecimal actualPreRecAmount;
	/** 实际已付金额，关联tb_pay_advance_relation**/
	private BigDecimal actualPaidAmount;
	/** 实际币种**/
	private Integer actualCurrencyType;
	/** 币种转换比率(应付水单金额/实际水单金额)**/
	private BigDecimal actualCurrencyRate;
	
	/**费用单ID**/
	private Integer feeId;
	/**出库单ID**/
	private Integer outStoreId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getReceiptNote() {
		return receiptNote;
	}

	public void setReceiptNote(String receiptNote) {
		this.receiptNote = receiptNote;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getBankReceiptNo() {
		return bankReceiptNo;
	}

	public void setBankReceiptNo(String bankReceiptNo) {
		this.bankReceiptNo = bankReceiptNo;
	}

	public String getBankReceiptNote() {
		return bankReceiptNote;
	}

	public void setBankReceiptNote(String bankReceiptNote) {
		this.bankReceiptNote = bankReceiptNote;
	}

	public Integer getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(Integer recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getPreRecId() {
		return preRecId;
	}

	public void setPreRecId(Integer preRecId) {
		this.preRecId = preRecId;
	}

	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
	}

	public BigDecimal getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}

	public BigDecimal getPreRecAmount() {
		return preRecAmount;
	}

	public void setPreRecAmount(BigDecimal preRecAmount) {
		this.preRecAmount = preRecAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getReceiptWay() {
		return receiptWay;
	}

	public void setReceiptWay(Integer receiptWay) {
		this.receiptWay = receiptWay;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public BigDecimal getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(BigDecimal remainAmount) {
		this.remainAmount = remainAmount;
	}

	public BigDecimal getVerificationAdvanceAmount() {
		return verificationAdvanceAmount;
	}

	public void setVerificationAdvanceAmount(BigDecimal verificationAdvanceAmount) {
		this.verificationAdvanceAmount = verificationAdvanceAmount;
	}

	public String getPayUnit() {
		return payUnit;
	}

	public void setPayUnit(String payUnit) {
		this.payUnit = payUnit;
	}

	public Integer getWriteOfftorId() {
		return writeOfftorId;
	}

	public void setWriteOfftorId(Integer writeOfftorId) {
		this.writeOfftorId = writeOfftorId;
	}

	public Date getWriteOffAt() {
		return writeOffAt;
	}

	public void setWriteOffAt(Date writeOffAt) {
		this.writeOffAt = writeOffAt;
	}

	public Integer getBillOutStlId() {
		return billOutStlId;
	}
	public void setBillOutStlId(Integer billOutStlId) {
		this.billOutStlId = billOutStlId;
	}

	public BigDecimal getActualReceiptAmount() {
		return actualReceiptAmount;
	}

	public void setActualReceiptAmount(BigDecimal actualReceiptAmount) {
		this.actualReceiptAmount = actualReceiptAmount;
	}

	public BigDecimal getActualWriteOffAmount() {
		return actualWriteOffAmount;
	}

	public void setActualWriteOffAmount(BigDecimal actualWriteOffAmount) {
		this.actualWriteOffAmount = actualWriteOffAmount;
	}

	public BigDecimal getActualDiffAmount() {
		return actualDiffAmount;
	}

	public void setActualDiffAmount(BigDecimal actualDiffAmount) {
		this.actualDiffAmount = actualDiffAmount;
	}

	public BigDecimal getActualPreRecAmount() {
		return actualPreRecAmount;
	}

	public void setActualPreRecAmount(BigDecimal actualPreRecAmount) {
		this.actualPreRecAmount = actualPreRecAmount;
	}

	public BigDecimal getActualPaidAmount() {
		return actualPaidAmount;
	}

	public void setActualPaidAmount(BigDecimal actualPaidAmount) {
		this.actualPaidAmount = actualPaidAmount;
	}

	public Integer getActualCurrencyType() {
		return actualCurrencyType;
	}

	public void setActualCurrencyType(Integer actualCurrencyType) {
		this.actualCurrencyType = actualCurrencyType;
	}

	public BigDecimal getActualCurrencyRate() {
		return actualCurrencyRate;
	}

	public void setActualCurrencyRate(BigDecimal actualCurrencyRate) {
		this.actualCurrencyRate = actualCurrencyRate;
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


	
}
