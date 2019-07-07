package com.scfs.domain.fee.dto.req;

import java.math.BigDecimal;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryFeeByCondReq.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class QueryFeeReqDto extends BaseReqDto {
	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	/** 项目 **/
	private Integer projectId;
	/** 经营单位 **/
	private Integer busiUnit;
	/** 应收客户 **/
	private Integer custPayer;
	/** 币种 **/
	private Integer currencyType;
	/** 费用类型 **/
	private Integer payFeeType;
	/** 应收费用科目 **/
	private Integer recFeeSpec;
	/** 应收辅助科目 **/
	private Integer recAssistFeeSpec;
	/** 应收方式 **/
	private Integer recType;
	/** 应收开始日期 **/
	private String startRecDate;
	/** 应收结束日期 **/
	private String endRecDate;
	/** 开票方式 **/
	private Integer provideInvoiceType;

	/** 应付客户 **/
	private Integer custReceiver;
	/** 应付费用科目 **/
	private Integer payFeeSpec;
	/** 应付辅助科目 **/
	private Integer payAssistFeeSpec;
	/** 支付方式 **/
	private Integer payType;
	/** 应付开始日期 **/
	private String startPayDate;
	/** 应付结束日期 **/
	private String endPayDate;
	/** 收票方式 **/
	private Integer acceptInvoiceType;
	/** 状态 **/
	private Integer state;
	/** 是否付款完成 0已付款完成，1未付款完成 2 收票金额小于应收金额 **/
	private Integer isPayAll;
	/** 收票税率 **/
	private BigDecimal acceptInvoiceTaxRate;
	
	public Integer getCustPayer() {
		return custPayer;
	}

	public void setCustPayer(Integer custPayer) {
		this.custPayer = custPayer;
	}

	public Integer getRecFeeSpec() {
		return recFeeSpec;
	}

	public void setRecFeeSpec(Integer recFeeSpec) {
		this.recFeeSpec = recFeeSpec;
	}

	public Integer getRecAssistFeeSpec() {
		return recAssistFeeSpec;
	}

	public void setRecAssistFeeSpec(Integer recAssistFeeSpec) {
		this.recAssistFeeSpec = recAssistFeeSpec;
	}

	public Integer getRecType() {
		return recType;
	}

	public void setRecType(Integer recType) {
		this.recType = recType;
	}

	public String getStartRecDate() {
		return startRecDate;
	}

	public void setStartRecDate(String startRecDate) {
		this.startRecDate = startRecDate;
	}

	public String getEndRecDate() {
		return endRecDate;
	}

	public void setEndRecDate(String endRecDate) {
		this.endRecDate = endRecDate;
	}

	public Integer getProvideInvoiceType() {
		return provideInvoiceType;
	}

	public void setProvideInvoiceType(Integer provideInvoiceType) {
		this.provideInvoiceType = provideInvoiceType;
	}

	public Integer getCustReceiver() {
		return custReceiver;
	}

	public void setCustReceiver(Integer custReceiver) {
		this.custReceiver = custReceiver;
	}

	public Integer getPayFeeSpec() {
		return payFeeSpec;
	}

	public void setPayFeeSpec(Integer payFeeSpec) {
		this.payFeeSpec = payFeeSpec;
	}

	public Integer getPayAssistFeeSpec() {
		return payAssistFeeSpec;
	}

	public void setPayAssistFeeSpec(Integer payAssistFeeSpec) {
		this.payAssistFeeSpec = payAssistFeeSpec;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getStartPayDate() {
		return startPayDate;
	}

	public void setStartPayDate(String startPayDate) {
		this.startPayDate = startPayDate;
	}

	public String getEndPayDate() {
		return endPayDate;
	}

	public void setEndPayDate(String endPayDate) {
		this.endPayDate = endPayDate;
	}

	public Integer getAcceptInvoiceType() {
		return acceptInvoiceType;
	}

	public void setAcceptInvoiceType(Integer acceptInvoiceType) {
		this.acceptInvoiceType = acceptInvoiceType;
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getIsPayAll() {
		return isPayAll;
	}

	public void setIsPayAll(Integer isPayAll) {
		this.isPayAll = isPayAll;
	}

	public BigDecimal getAcceptInvoiceTaxRate() {
		return acceptInvoiceTaxRate;
	}

	public void setAcceptInvoiceTaxRate(BigDecimal acceptInvoiceTaxRate) {
		this.acceptInvoiceTaxRate = acceptInvoiceTaxRate;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getPayFeeType() {
		return payFeeType;
	}

	public void setPayFeeType(Integer payFeeType) {
		this.payFeeType = payFeeType;
	}

}
