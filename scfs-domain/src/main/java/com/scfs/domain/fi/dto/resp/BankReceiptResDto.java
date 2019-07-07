
package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: BankReceiptResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */
public class BankReceiptResDto {
	/** 水单id **/
	private Integer id;
	/** 来源水单id **/
	private Integer pid;
	/** 水单编号 **/
	private String receiptNo;
	/** 银行水单号 **/
	private String bankReceiptNo;
	/** 收款账号 **/
	private Integer recAccountNo;
	private String recAccountNoName;

	/** 日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date receiptDate;
	/** 项目名称 **/
	private Integer projectId;
	private String projectName;
	/** 经营单位 **/
	private Integer busiUnit;
	private String busiUnitName;
	/** 客户名称 **/
	private Integer custId;
	private String cusName;
	/** 水单类型 **/
	private Integer receiptType;
	private String receiptTypeName;
	/** 币种 **/
	private Integer currencyType;
	private String currencyTypeName;
	/** 水单金额 **/
	private BigDecimal receiptAmount;
	/** 核销金额 **/
	private BigDecimal writeOffAmount;
	/** 预收金额 **/
	private BigDecimal preRecAmount;
	/** 尾差 **/
	private BigDecimal diffAmount;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 可付金额 **/
	private BigDecimal payableAmount;
	/** 状态 **/
	private Integer state;
	private String stateName;
	/** 摘要 **/
	private String summary;

	/** 收款方式 **/
	private Integer receiptWay;
	private String receiptWayName;
	/** 开立类型 **/
	private Integer openType;
	/** 开立日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date openDate;
	/** 余额 **/
	private BigDecimal receiptBlance;
	/**
	 * 余额
	 */
	private BigDecimal remainAmount;
	/**
	 * 销售单关联水单金额
	 */
	private BigDecimal verificationAdvanceAmount;
	private BigDecimal prePoAmount; // 预收货款总额
	private BigDecimal preDepositeAmount; // 预收定金总额
	/** 核完人 */
	private String writeOfftor;
	/** 核完时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date writeOffAt;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createAt;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	/** 付款单位 **/
	private String payUnit;
	/**
	 * 付款单位名称
	 */
	private String payUnitName;

	/** 实际水单金额 **/
	private BigDecimal actualReceiptAmount;
	/** 实际核销金额 **/
	private BigDecimal actualWriteOffAmount;
	/** 实际尾差（抹零金额） **/
	private BigDecimal actualDiffAmount;
	/** 实际预收金额（水单金额-核销金额+尾差） **/
	private BigDecimal actualPreRecAmount;
	/** 实际已付金额，关联tb_pay_advance_relation **/
	private BigDecimal actualPaidAmount;
	/** 实际币种 **/
	private Integer actualCurrencyType;
	/** 币种转换比率(应付水单金额/实际水单金额) **/
	private BigDecimal actualCurrencyRate;
	/** 实际币种 **/
	private String actualCurrencyTypeName;

	/****************** 扩展字段 **********************/
	/**
	 * 父级水单ID
	 */
	private Integer parentReceiptId;
	/**
	 * 父级水单编号
	 */
	private String parentReceiptNo;
	/**
	 * 父级水单日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date parentReceiptDate;
	/**
	 * 父级水单类型
	 */
	private Integer parentReceiptType;
	/**
	 * 父级水单类型名称
	 */
	private Integer parentReceiptTypeName;
	/**
	 * 父级水单摘要
	 */
	private String parentSummary;
	/**
	 * 父级水单金额
	 */
	private BigDecimal parentReceiptAmount;
	/**
	 * 可选择金额
	 */
	private BigDecimal availableAmount;
	/**
	 * 是否禁用勾选
	 */
	private boolean disabled = false;
	/**
	 * 是否勾选
	 */
	private boolean isChecked = false;

	/**
	 * 可付金额 退款添加
	 * 
	 */
	private BigDecimal canPaidAmount;

	/** 订金转退款 金额的改变 **/
	private BigDecimal depChangePaymentAmount;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_BANK_RECEIPT);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_BANK_RECEIPT);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_BANK_RECEIPT);
			operMap.put(OperateConsts.CHECK, BusUrlConsts.EDIT_CHECK_BANK_RECEIPT);
			operMap.put(OperateConsts.OVER, BusUrlConsts.OVER_BANK_RECEIPT);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_BANK_RECEIPT);
			operMap.put(OperateConsts.REJECT, BusUrlConsts.REJECT_BANK_RECEIPT);
		}
	}

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

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getBankReceiptNo() {
		return bankReceiptNo;
	}

	public void setBankReceiptNo(String bankReceiptNo) {
		this.bankReceiptNo = bankReceiptNo;
	}

	public Integer getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(Integer recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public String getRecAccountNoName() {
		return recAccountNoName;
	}

	public void setRecAccountNoName(String recAccountNoName) {
		this.recAccountNoName = recAccountNoName;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
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

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public String getReceiptTypeName() {
		return receiptTypeName;
	}

	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
	}

	public BigDecimal getPreRecAmount() {
		return preRecAmount;
	}

	public void setPreRecAmount(BigDecimal preRecAmount) {
		this.preRecAmount = preRecAmount;
	}

	public BigDecimal getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Integer getReceiptWay() {
		return receiptWay;
	}

	public void setReceiptWay(Integer receiptWay) {
		this.receiptWay = receiptWay;
	}

	public String getReceiptWayName() {
		return receiptWayName;
	}

	public void setReceiptWayName(String receiptWayName) {
		this.receiptWayName = receiptWayName;
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

	public BigDecimal getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(BigDecimal remainAmount) {
		this.remainAmount = remainAmount;
	}

	public BigDecimal getPrePoAmount() {
		return prePoAmount;
	}

	public void setPrePoAmount(BigDecimal prePoAmount) {
		this.prePoAmount = prePoAmount;
	}

	public BigDecimal getPreDepositeAmount() {
		return preDepositeAmount;
	}

	public void setPreDepositeAmount(BigDecimal preDepositeAmount) {
		this.preDepositeAmount = preDepositeAmount;
	}

	public BigDecimal getReceiptBlance() {
		return receiptBlance;
	}

	public void setReceiptBlance(BigDecimal receiptBlance) {
		this.receiptBlance = receiptBlance;
	}

	public Integer getParentReceiptId() {
		return parentReceiptId;
	}

	public void setParentReceiptId(Integer parentReceiptId) {
		this.parentReceiptId = parentReceiptId;
	}

	public String getParentReceiptNo() {
		return parentReceiptNo;
	}

	public void setParentReceiptNo(String parentReceiptNo) {
		this.parentReceiptNo = parentReceiptNo;
	}

	public Date getParentReceiptDate() {
		return parentReceiptDate;
	}

	public void setParentReceiptDate(Date parentReceiptDate) {
		this.parentReceiptDate = parentReceiptDate;
	}

	public Integer getParentReceiptType() {
		return parentReceiptType;
	}

	public void setParentReceiptType(Integer parentReceiptType) {
		this.parentReceiptType = parentReceiptType;
	}

	public String getParentSummary() {
		return parentSummary;
	}

	public void setParentSummary(String parentSummary) {
		this.parentSummary = parentSummary;
	}

	public BigDecimal getParentReceiptAmount() {
		return parentReceiptAmount;
	}

	public void setParentReceiptAmount(BigDecimal parentReceiptAmount) {
		this.parentReceiptAmount = parentReceiptAmount;
	}

	public Integer getParentReceiptTypeName() {
		return parentReceiptTypeName;
	}

	public void setParentReceiptTypeName(Integer parentReceiptTypeName) {
		this.parentReceiptTypeName = parentReceiptTypeName;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
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

	public String getWriteOfftor() {
		return writeOfftor;
	}

	public void setWriteOfftor(String writeOfftor) {
		this.writeOfftor = writeOfftor;
	}

	public Date getWriteOffAt() {
		return writeOffAt;
	}

	public void setWriteOffAt(Date writeOffAt) {
		this.writeOffAt = writeOffAt;
	}

	public BigDecimal getCanPaidAmount() {
		return canPaidAmount;
	}

	public void setCanPaidAmount(BigDecimal canPaidAmount) {
		this.canPaidAmount = canPaidAmount;
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

	public String getActualCurrencyTypeName() {
		return actualCurrencyTypeName;
	}

	public void setActualCurrencyTypeName(String actualCurrencyTypeName) {
		this.actualCurrencyTypeName = actualCurrencyTypeName;
	}

	public BigDecimal getDepChangePaymentAmount() {
		return depChangePaymentAmount;
	}

	public void setDepChangePaymentAmount(BigDecimal depChangePaymentAmount) {
		this.depChangePaymentAmount = depChangePaymentAmount;
	}

	public String getPayUnitName() {
		return payUnitName;
	}

	public void setPayUnitName(String payUnitName) {
		this.payUnitName = payUnitName;
	}
}
