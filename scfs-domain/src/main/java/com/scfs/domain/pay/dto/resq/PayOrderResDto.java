package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: PayOrderResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日			Administrator
 *
 * </pre>
 */
public class PayOrderResDto {
	/** 付款id **/
	private Integer id;
	/** 付款编号 **/
	private String payNo;
	/** 经营单位id **/
	private Integer busiUnitId;
	/** 经营单位 **/
	private String busiUnit;
	/** 项目 **/
	private String projectName;
	private Integer projectId;
	/** 项目业务类别 */
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
	private BigDecimal payAmount; // 折扣后金额
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
	/** 确认时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date confirmorAt;
	/** 确认人 **/
	private String confirmor;
	/** 付款备注 **/
	private String remark;

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
	/** 收款余额 **/
	private BigDecimal blance;
	/** 付款订单总额 **/
	private BigDecimal poBlance;
	/** 付款费用总额 **/
	private BigDecimal feeBlance;
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
	/** 银行默认币种 **/
	private String defaultCurrency;
	/** 银行账号 **/
	private Integer paymentAccount;
	private String paymentAccountName;
	/** 银行手续费 **/
	private BigDecimal bankCharge;

	/** 项目额度 **/
	private BigDecimal projectTotalAmount;
	private BigDecimal projectBalanceAmount;
	private Integer projectAmountUnit;
	private String projectAmountUnitTypeName;

	/** 付款后项目余额 **/
	private BigDecimal payProjectBalanceAmount;

	/** 币种 **/
	private Integer currnecyType;
	private String currnecyTypeName;
	/** 开立类型 **/
	private Integer openType;

	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;
	/** 系统时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;
	/** 附属编号 **/
	private String attachedNumbe;
	/** 预计内部打款日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date innerPayDate;
	/** 销售价 **/
	private BigDecimal sumSendPrice;
	/** 总利润率 **/
	private String sumProfit;
	/** 实际付款金额 **/
	private BigDecimal realPayAmount;
	/** 实际付款币种 **/
	private Integer realCurrencyType;
	/** 实际付款币种 **/
	private String realCurrencyTypeName;
	/** 汇率 **/
	private BigDecimal payRate;

	/** 利润 **/
	private BigDecimal profit;

	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	private Integer printNum;
	/** 批量确认标示符 **/
	private String unionOverIdentifier;
	/** 批量打印标示符 **/
	private String unionPrintIdentifier;
	private String mergePayNo;
	private BigDecimal discountAmount;
	private BigDecimal inDiscountAmount; // 折扣前金额
	private BigDecimal discountRate;
	private String discountRateStr;
	/** 付款支付类型 0-全部 1-预付 2-尾款 **/
	private Integer payWayType;
	private String payWayTypeName;
	/** cms付款人 **/
	private String cmsPayer;
	/** cms驳回人 **/
	private String cmsRejecter;
	/** 原因 **/
	private String reason;
	/** 水单时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date memoTime;
	/** 是否无订单 0-否 1-是 */
	private Integer noneOrderFlag;
	private String noneOrderFlagName;
	/** 是否核销 0-未核销 1-已核销 */
	private Integer writeOffFlag;
	private String writeOffFlagName;
	private BigDecimal checkAmount;
	/** 可核销金额*/
	private BigDecimal leftCheckAmount;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_PAY_ORDER);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PAY_ORDER);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_PAY_ORDER);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_PAY_ORDER);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_PAY_ORDER_OPEN);
			operMap.put(OperateConsts.CHECK, BusUrlConsts.CHECK_PAY_ORDER);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
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

	public Date getConfirmorAt() {
		return confirmorAt;
	}

	public void setConfirmorAt(Date confirmorAt) {
		this.confirmorAt = confirmorAt;
	}

	public String getConfirmor() {
		return confirmor;
	}

	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

	public BigDecimal getPoBlance() {
		return poBlance;
	}

	public void setPoBlance(BigDecimal poBlance) {
		this.poBlance = poBlance;
	}

	public BigDecimal getFeeBlance() {
		return feeBlance;
	}

	public void setFeeBlance(BigDecimal feeBlance) {
		this.feeBlance = feeBlance;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public Integer getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(Integer paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getPaymentAccountName() {
		return paymentAccountName;
	}

	public void setPaymentAccountName(String paymentAccountName) {
		this.paymentAccountName = paymentAccountName;
	}

	public BigDecimal getBankCharge() {
		return bankCharge;
	}

	public void setBankCharge(BigDecimal bankCharge) {
		this.bankCharge = bankCharge;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getCurrnecyTypeName() {
		return currnecyTypeName;
	}

	public void setCurrnecyTypeName(String currnecyTypeName) {
		this.currnecyTypeName = currnecyTypeName;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
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

	public BigDecimal getPayProjectBalanceAmount() {
		return payProjectBalanceAmount;
	}

	public void setPayProjectBalanceAmount(BigDecimal payProjectBalanceAmount) {
		this.payProjectBalanceAmount = payProjectBalanceAmount;
	}

	public String getAttachedNumbe() {
		return attachedNumbe;
	}

	public void setAttachedNumbe(String attachedNumbe) {
		this.attachedNumbe = attachedNumbe;
	}

	public Date getInnerPayDate() {
		return innerPayDate;
	}

	public void setInnerPayDate(Date innerPayDate) {
		this.innerPayDate = innerPayDate;
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

	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
	}

	public Integer getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(Integer realCurrencyType) {
		this.realCurrencyType = realCurrencyType;
	}

	public String getRealCurrencyTypeName() {
		return realCurrencyTypeName;
	}

	public void setRealCurrencyTypeName(String realCurrencyTypeName) {
		this.realCurrencyTypeName = realCurrencyTypeName;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public String getUnionOverIdentifier() {
		return unionOverIdentifier;
	}

	public void setUnionOverIdentifier(String unionOverIdentifier) {
		this.unionOverIdentifier = unionOverIdentifier;
	}

	public String getUnionPrintIdentifier() {
		return unionPrintIdentifier;
	}

	public void setUnionPrintIdentifier(String unionPrintIdentifier) {
		this.unionPrintIdentifier = unionPrintIdentifier;
	}

	public Integer getStateInt() {
		return stateInt;
	}

	public void setStateInt(Integer stateInt) {
		this.stateInt = stateInt;
	}

	public String getMergePayNo() {
		return mergePayNo;
	}

	public void setMergePayNo(String mergePayNo) {
		this.mergePayNo = mergePayNo;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getInDiscountAmount() {
		return inDiscountAmount;
	}

	public void setInDiscountAmount(BigDecimal inDiscountAmount) {
		this.inDiscountAmount = inDiscountAmount;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountRateStr() {
		return discountRateStr;
	}

	public void setDiscountRateStr(String discountRateStr) {
		this.discountRateStr = discountRateStr;
	}

	public Integer getPayWayType() {
		return payWayType;
	}

	public void setPayWayType(Integer payWayType) {
		this.payWayType = payWayType;
	}

	public String getPayWayTypeName() {
		return payWayTypeName;
	}

	public void setPayWayTypeName(String payWayTypeName) {
		this.payWayTypeName = payWayTypeName;
	}

	public String getCmsPayer() {
		return cmsPayer;
	}

	public void setCmsPayer(String cmsPayer) {
		this.cmsPayer = cmsPayer;
	}

	public String getCmsRejecter() {
		return cmsRejecter;
	}

	public void setCmsRejecter(String cmsRejecter) {
		this.cmsRejecter = cmsRejecter;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getMemoTime() {
		return memoTime;
	}

	public void setMemoTime(Date memoTime) {
		this.memoTime = memoTime;
	}

	public Integer getNoneOrderFlag() {
		return noneOrderFlag;
	}

	public void setNoneOrderFlag(Integer noneOrderFlag) {
		this.noneOrderFlag = noneOrderFlag;
	}

	public String getNoneOrderFlagName() {
		return noneOrderFlagName;
	}

	public void setNoneOrderFlagName(String noneOrderFlagName) {
		this.noneOrderFlagName = noneOrderFlagName;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

	public String getWriteOffFlagName() {
		return writeOffFlagName;
	}

	public void setWriteOffFlagName(String writeOffFlagName) {
		this.writeOffFlagName = writeOffFlagName;
	}

	public BigDecimal getLeftCheckAmount() {
		return DecimalUtil.subtract(null == this.payAmount ? BigDecimal.ZERO : this.payAmount, 
				null == this.checkAmount ? BigDecimal.ZERO : this.checkAmount);
	}

	public void setLeftCheckAmount(BigDecimal leftCheckAmount) {
		this.leftCheckAmount = leftCheckAmount;
	}

	public BigDecimal getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}

}
