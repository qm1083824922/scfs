package com.scfs.domain.pay.entity;

import com.scfs.domain.base.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <pre>
 * 
 *  File: PayOrder.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayOrder extends BaseEntity {
	/** 付款编号 **/
	private String payNo;
	/** 付款类型 1 订单货款 2 费用 3 借款 4 保证金 **/
	private Integer payType;
	/** 项目ID **/
	private Integer projectId;
	/** 经营单位 **/
	private Integer busiUnit;
	/** 付款单位 **/
	private Integer payer;
	/** 付款方式 **/
	private Integer payWay;
	/** 付款金额 **/
	private BigDecimal payAmount; // 折扣后金额
	/** 收款单位 **/
	private Integer payee;
	/** 收款账号ID **/
	private Integer payAccountId;
	/** 要求付款日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requestPayTime;
	/** 备注 **/
	private String remark;
	/** 状态状态 0 待提交 1待业务审核 2待财务审核 3待风控审核 4待确认 5待开立 6已完成 **/
	private Integer state;

	/** 确认人 **/
	private String confirmor;
	/** 确认人id **/
	private Integer confirmorId;
	/** 确认时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date confirmorAt;
	/** 收款余额 **/
	private BigDecimal blance;
	/** 付款订单总额 **/
	private BigDecimal poBlance;
	/** 付款费用总额 **/
	private BigDecimal feeBlance;
	/** 银行账号 **/
	private Integer paymentAccount;
	/** 银行手续费 **/
	private BigDecimal bankCharge;
	/** 币种 **/
	private Integer currnecyType;
	/** 开立类型 **/
	private Integer openType;
	/** 预收总额 **/
	private BigDecimal advanceAmount;
	/** 抵扣费用总额 **/
	private BigDecimal deductionFeeAmount;
	/** 附属编号 **/
	private String attachedNumbe;
	/** 预计内部打款日期 **/
	private Date innerPayDate;
	/** 实际付款金额 **/
	private BigDecimal realPayAmount;
	/** 实际付款币种 **/
	private Integer realCurrencyType;
	/** 汇率 **/
	private BigDecimal payRate;
	/** 打印次数 **/
	private Integer printNum;
	/** 批量确认标示符 **/
	private String unionOverIdentifier;
	/** 批量打印标示符 **/
	private String unionPrintIdentifier;
	/** 合并付款编号 **/
	private String mergePayNo;
	private BigDecimal discountAmount;
	private BigDecimal inDiscountAmount; // 折扣前金额
	/** 付款支付类型 0-全部 1-预付 2-尾款 **/
	private Integer payWayType;
	/** cms付款人 **/
	private String cmsPayer;
	/** cms驳回人 **/
	private String cmsRejecter;
	/** 原因 **/
	private String reason;

	/** 水单日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date memoTime;
	
	/** 是否无订单 0-否 1-是 */
	private Integer noneOrderFlag;
	/** 是否核销 0-未核销 1-已核销 */
	private Integer writeOffFlag;
	private BigDecimal checkAmount;

	private List<Integer> ids;

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getPayer() {
		return payer;
	}

	public void setPayer(Integer payer) {
		this.payer = payer;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getConfirmor() {
		return confirmor;
	}

	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}

	public Integer getConfirmorId() {
		return confirmorId;
	}

	public void setConfirmorId(Integer confirmorId) {
		this.confirmorId = confirmorId;
	}

	public Date getConfirmorAt() {
		return confirmorAt;
	}

	public void setConfirmorAt(Date confirmorAt) {
		this.confirmorAt = confirmorAt;
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

	public Integer getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(Integer paymentAccount) {
		this.paymentAccount = paymentAccount;
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

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public BigDecimal getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(BigDecimal advanceAmount) {
		this.advanceAmount = advanceAmount;
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

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
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

	public Integer getPayWayType() {
		return payWayType;
	}

	public void setPayWayType(Integer payWayType) {
		this.payWayType = payWayType;
	}

	public BigDecimal getDeductionFeeAmount() {
		return deductionFeeAmount;
	}

	public void setDeductionFeeAmount(BigDecimal deductionFeeAmount) {
		this.deductionFeeAmount = deductionFeeAmount;
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

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

	public BigDecimal getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}
	
}