package com.scfs.domain.pay.dto.resq;

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
 *  File: MergePayOrderResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */
public class MergePayOrderResDto {
	/** 付款id **/
	private Integer id;
	/** 付款编号 **/
	private String mergePayNo;
	/** 经营单位id **/
	private Integer busiUnitId;
	/** 经营单位 **/
	private String busiUnit;
	/** 项目 **/
	private String projectName;
	private Integer projectId;
	/** 业务类别 */
	private Integer bizType;
	private String bizTypeName;
	/** 付款单位 **/
	private Integer payer;
	private String payerName;
	/** 付款类型 **/
	private String payTypeName;
	private Integer payType;
	/** 付款方式 **/
	private String payWayName;
	private Integer payWay;
	/** 付款金额 **/
	private BigDecimal payAmount;
	/** 收款单位 **/
	private String payeeName;
	private Integer payee;
	/** 收款账号ID **/
	private Integer payAccountId;
	/** 要求付款日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requestPayTime;
	/** 状态 **/
	private String state;
	/** 状态 **/
	private Integer stateInt;
	/** 付款备注 **/
	private String remark;

	/** 银行默认币种 **/
	private String defaultCurrency;
	/** 开户行 **/
	private String bankName;
	/** 收款银行地址 **/
	private String bankAddress;
	/** 收款银行开户人 **/
	private String subjectName;
	/** 收款银行账号 **/
	private String accountNo;
	/** 收款银行代码 **/
	private String bankCode;
	/** 银行iban */
	private String iban;
	/** 地址电话 **/
	private String phoneNumber;
	/** 操作集合 */
	private List<CodeValue> opertaList;
	/** 币种 **/
	private Integer currencyType;
	private String currencyTypeName;

	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;
	/** 系统时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	private Integer printNum;
	/** 批量打印标示符 **/
	private String unionPrintIdentifier;

	/** 销售价 **/
	private BigDecimal sumSendPrice;
	/** 总利润率 **/
	private String sumProfit;

	/** 项目额度 **/
	private BigDecimal projectTotalAmount;
	private BigDecimal projectBalanceAmount;
	private Integer projectAmountUnit;
	private String projectAmountUnitTypeName;

	/** 付款后项目余额 **/
	private BigDecimal payProjectBalanceAmount;

	/** 预收金额 **/
	private BigDecimal advanceAmount;
	/** 实际占用 **/
	private BigDecimal payAdvanceAmount;
	/** 占用比例 **/
	private BigDecimal payAdvanceAmountRate;
	/** 占用比例 **/
	private String payAdvanceAmountRateName;
	/** 预收比例 **/
	private BigDecimal advanceAmountRate;
	/** 预收比例 **/
	private String advanceAmountRateName;
	private String discountRateStr;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_MERGE_PAY_ORDER);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_MERGE_PAY_ORDER);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_MERGE_PAY_ORDER);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_MERGE_PAY_ORDER);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_MERGE_PAY_ORDER);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMergePayNo() {
		return mergePayNo;
	}

	public void setMergePayNo(String mergePayNo) {
		this.mergePayNo = mergePayNo;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public String getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(String busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public Integer getPayer() {
		return payer;
	}

	public void setPayer(Integer payer) {
		this.payer = payer;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Integer getPayee() {
		return payee;
	}

	public void setPayee(Integer payee) {
		this.payee = payee;
	}

	public Integer getPayAccountId() {
		return payAccountId;
	}

	public void setPayAccountId(Integer payAccountId) {
		this.payAccountId = payAccountId;
	}

	public Date getRequestPayTime() {
		return requestPayTime;
	}

	public void setRequestPayTime(Date requestPayTime) {
		this.requestPayTime = requestPayTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getStateInt() {
		return stateInt;
	}

	public void setStateInt(Integer stateInt) {
		this.stateInt = stateInt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
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

	public Date getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
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

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public String getUnionPrintIdentifier() {
		return unionPrintIdentifier;
	}

	public void setUnionPrintIdentifier(String unionPrintIdentifier) {
		this.unionPrintIdentifier = unionPrintIdentifier;
	}

	public BigDecimal getSumSendPrice() {
		return sumSendPrice;
	}

	public void setSumSendPrice(BigDecimal sumSendPrice) {
		this.sumSendPrice = sumSendPrice;
	}

	public String getSumProfit() {
		return sumProfit;
	}

	public void setSumProfit(String sumProfit) {
		this.sumProfit = sumProfit;
	}

	public BigDecimal getProjectTotalAmount() {
		return projectTotalAmount;
	}

	public void setProjectTotalAmount(BigDecimal projectTotalAmount) {
		this.projectTotalAmount = projectTotalAmount;
	}

	public BigDecimal getProjectBalanceAmount() {
		return projectBalanceAmount;
	}

	public void setProjectBalanceAmount(BigDecimal projectBalanceAmount) {
		this.projectBalanceAmount = projectBalanceAmount;
	}

	public Integer getProjectAmountUnit() {
		return projectAmountUnit;
	}

	public void setProjectAmountUnit(Integer projectAmountUnit) {
		this.projectAmountUnit = projectAmountUnit;
	}

	public String getProjectAmountUnitTypeName() {
		return projectAmountUnitTypeName;
	}

	public void setProjectAmountUnitTypeName(String projectAmountUnitTypeName) {
		this.projectAmountUnitTypeName = projectAmountUnitTypeName;
	}

	public BigDecimal getPayProjectBalanceAmount() {
		return payProjectBalanceAmount;
	}

	public void setPayProjectBalanceAmount(BigDecimal payProjectBalanceAmount) {
		this.payProjectBalanceAmount = payProjectBalanceAmount;
	}

	public BigDecimal getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(BigDecimal advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public BigDecimal getPayAdvanceAmount() {
		return payAdvanceAmount;
	}

	public void setPayAdvanceAmount(BigDecimal payAdvanceAmount) {
		this.payAdvanceAmount = payAdvanceAmount;
	}

	public BigDecimal getPayAdvanceAmountRate() {
		return payAdvanceAmountRate;
	}

	public void setPayAdvanceAmountRate(BigDecimal payAdvanceAmountRate) {
		this.payAdvanceAmountRate = payAdvanceAmountRate;
	}

	public String getPayAdvanceAmountRateName() {
		return payAdvanceAmountRateName;
	}

	public void setPayAdvanceAmountRateName(String payAdvanceAmountRateName) {
		this.payAdvanceAmountRateName = payAdvanceAmountRateName;
	}

	public BigDecimal getAdvanceAmountRate() {
		return advanceAmountRate;
	}

	public void setAdvanceAmountRate(BigDecimal advanceAmountRate) {
		this.advanceAmountRate = advanceAmountRate;
	}

	public String getAdvanceAmountRateName() {
		return advanceAmountRateName;
	}

	public void setAdvanceAmountRateName(String advanceAmountRateName) {
		this.advanceAmountRateName = advanceAmountRateName;
	}

	public String getDiscountRateStr() {
		return discountRateStr;
	}

	public void setDiscountRateStr(String discountRateStr) {
		this.discountRateStr = discountRateStr;
	}

}
