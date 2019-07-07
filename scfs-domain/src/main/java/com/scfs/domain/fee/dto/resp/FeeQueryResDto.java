package com.scfs.domain.fee.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: FeeResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator
 *
 * </pre>
 */
public class FeeQueryResDto {
	/** 费用id **/
	private Integer id;
	/** 费用编号 **/
	private String feeNo;
	/** 币种 **/
	private Integer currencyType;
	private String currencyTypeName;
	/** 费用类型 **/
	private Integer feeType;
	/** 项目 **/
	private Integer projectId;
	private String projectName;
	/** 应收客户 **/
	private Integer custPayer;
	private String custPayerName;
	/** 应收费用科目 **/
	private Integer recFeeSpec;
	private String recFeeSpecName;
	/** 应收辅助科目 **/
	private Integer recAssistFeeSpec;
	private String recAssistFeeSpecName;
	/** 应收方式 **/
	private Integer recType;
	private String recTypeName;
	/** 应收日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date recDate;
	/** 已收金额 **/
	private BigDecimal receivedAmount;
	/** 应收金额 **/
	private BigDecimal recAmount;
	/** 开票金额 **/
	private BigDecimal provideInvoiceAmount;
	/** 开票方式 **/
	private Integer provideInvoiceType;
	private String provideInvoiceTypeName;
	/** 开票税率 **/
	private BigDecimal provideInvoiceTaxRate;
	/** 开票税率 **/
	private String provideInvoiceTaxRateStr;
	/** 应收备注 **/
	private String recNote;

	/** 应付客户 **/
	private Integer custReceiver;
	private String custReceiverName;
	/** 应付费用科目 **/
	private Integer payFeeSpec;
	private String payFeeSpecName;
	/** 应付辅助科目 **/
	private Integer payAssistFeeSpec;
	private String payAssistFeeSpecName;
	/** 支付方式 **/
	private Integer payType;
	private String payTypeName;
	/** 应付日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date payDate;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 应付金额 **/
	private BigDecimal payAmount;
	/** 收票金额 **/
	private BigDecimal acceptInvoiceAmount;
	/** 收票方式 **/
	private Integer acceptInvoiceType;
	private String acceptInvoiceTypeName;
	/** 收票税率 **/
	private BigDecimal acceptInvoiceTaxRate;
	/** 收票税率 **/
	private String acceptInvoiceTaxRateStr; // 0.00传到前台会变成0,浏览时转成字符串显示
	/** 状态 **/
	private Integer state;
	private String stateName;
	/** 应付备注 **/
	private String payNote;

	/** 创建人 **/
	private String creator;
	/** 创建时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	private String busiUnitNameNo;
	private String busiUnitName;
	private String busiUnitAddress;
	private String feeTypeName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date auditAt;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date bookDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date expireDate;
	private String payFeeTypeName;
	/** 开票余额=应收-开票金额 **/
	private BigDecimal blanceInvoiceAmount;

	/** 可付金额=费用金额-已付款金额 **/
	private BigDecimal blanceFeeAmount;
    /**抵扣活动名称**/
    private String deductionTypeName;
    /**抵扣活动类型*/
    private Integer deductionType;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, Map<Integer, String>> operMap = new HashMap<String, Map<Integer, String>>();
		static {
			Map<Integer, String> editMap = new HashMap<Integer, String>();
			editMap.put(BaseConsts.ONE, BusUrlConsts.EDITRECFEE);
			editMap.put(BaseConsts.TWO, BusUrlConsts.EDITPAYFEE);
			editMap.put(BaseConsts.THREE, BusUrlConsts.EDITRECPAYFEE);
			editMap.put(BaseConsts.FOUR, BusUrlConsts.EDIT_REC_DEDUCTION_FEE);
			editMap.put(BaseConsts.FIVE, BusUrlConsts.EDIT_PAY_DEDUCTION_FEE);

			Map<Integer, String> deleteMap = new HashMap<Integer, String>();
			deleteMap.put(BaseConsts.ONE, BusUrlConsts.DELETERECFEE);
			deleteMap.put(BaseConsts.TWO, BusUrlConsts.DELETEPAYFEE);
			deleteMap.put(BaseConsts.THREE, BusUrlConsts.DELETERECPAYFEE);
			deleteMap.put(BaseConsts.FOUR, BusUrlConsts.DELETE_REC_DEDUCTION_FEE);
			deleteMap.put(BaseConsts.FIVE, BusUrlConsts.DELETE_PAY_DEDUCTION_FEE);

			Map<Integer, String> detailMap = new HashMap<Integer, String>(); // 浏览详情
			detailMap.put(BaseConsts.ONE, BusUrlConsts.DETAILRECFEE);
			detailMap.put(BaseConsts.TWO, BusUrlConsts.DETAILPAYFEE);
			detailMap.put(BaseConsts.THREE, BusUrlConsts.DETAILRECPAYFEE);
			detailMap.put(BaseConsts.FOUR, BusUrlConsts.DETAIL_REC_DEDUCTION_FEE);
			detailMap.put(BaseConsts.FIVE, BusUrlConsts.DETAIL_PAY_DEDUCTION_FEE);

			Map<Integer, String> submitMap = new HashMap<Integer, String>();
			submitMap.put(BaseConsts.ONE, BusUrlConsts.SUBMITRECFEE);
			submitMap.put(BaseConsts.TWO, BusUrlConsts.SUBMITPAYFEE);
			submitMap.put(BaseConsts.THREE, BusUrlConsts.SUBMITRECPAYFEE);
			submitMap.put(BaseConsts.FOUR, BusUrlConsts.SUBMIT_REC_DEDUCTION_FEE);
			submitMap.put(BaseConsts.FIVE, BusUrlConsts.SUBMIT_PAY_DEDUCTION_FEE);

			Map<Integer, String> printMap = new HashMap<Integer, String>();
			printMap.put(BaseConsts.ONE, BusUrlConsts.PRINTRECFEE);
			printMap.put(BaseConsts.TWO, BusUrlConsts.PRINTPAYFEE);
			printMap.put(BaseConsts.THREE, BusUrlConsts.PRINTRECPAYFEE);

			operMap.put(OperateConsts.EDIT, editMap);
			operMap.put(OperateConsts.DELETE, deleteMap);
			operMap.put(OperateConsts.DETAIL, detailMap);
			operMap.put(OperateConsts.SUBMIT, submitMap);
			operMap.put(OperateConsts.PRINT, printMap);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
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

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
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

	public Integer getCustPayer() {
		return custPayer;
	}

	public void setCustPayer(Integer custPayer) {
		this.custPayer = custPayer;
	}

	public String getCustPayerName() {
		return custPayerName;
	}

	public void setCustPayerName(String custPayerName) {
		this.custPayerName = custPayerName;
	}

	public Integer getRecFeeSpec() {
		return recFeeSpec;
	}

	public void setRecFeeSpec(Integer recFeeSpec) {
		this.recFeeSpec = recFeeSpec;
	}

	public String getRecFeeSpecName() {
		return recFeeSpecName;
	}

	public void setRecFeeSpecName(String recFeeSpecName) {
		this.recFeeSpecName = recFeeSpecName;
	}

	public Integer getRecAssistFeeSpec() {
		return recAssistFeeSpec;
	}

	public void setRecAssistFeeSpec(Integer recAssistFeeSpec) {
		this.recAssistFeeSpec = recAssistFeeSpec;
	}

	public String getRecAssistFeeSpecName() {
		return recAssistFeeSpecName;
	}

	public void setRecAssistFeeSpecName(String recAssistFeeSpecName) {
		this.recAssistFeeSpecName = recAssistFeeSpecName;
	}

	public Integer getRecType() {
		return recType;
	}

	public void setRecType(Integer recType) {
		this.recType = recType;
	}

	public String getRecTypeName() {
		return recTypeName;
	}

	public void setRecTypeName(String recTypeName) {
		this.recTypeName = recTypeName;
	}

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

	public BigDecimal getProvideInvoiceAmount() {
		return provideInvoiceAmount;
	}

	public void setProvideInvoiceAmount(BigDecimal provideInvoiceAmount) {
		this.provideInvoiceAmount = provideInvoiceAmount;
	}

	public Integer getProvideInvoiceType() {
		return provideInvoiceType;
	}

	public void setProvideInvoiceType(Integer provideInvoiceType) {
		this.provideInvoiceType = provideInvoiceType;
	}

	public String getProvideInvoiceTypeName() {
		return provideInvoiceTypeName;
	}

	public void setProvideInvoiceTypeName(String provideInvoiceTypeName) {
		this.provideInvoiceTypeName = provideInvoiceTypeName;
	}

	public BigDecimal getProvideInvoiceTaxRate() {
		return provideInvoiceTaxRate;
	}

	public void setProvideInvoiceTaxRate(BigDecimal provideInvoiceTaxRate) {
		this.provideInvoiceTaxRate = provideInvoiceTaxRate;
	}

	public String getProvideInvoiceTaxRateStr() {
		return provideInvoiceTaxRateStr;
	}

	public void setProvideInvoiceTaxRateStr(String provideInvoiceTaxRateStr) {
		this.provideInvoiceTaxRateStr = provideInvoiceTaxRateStr;
	}

	public String getRecNote() {
		return recNote;
	}

	public void setRecNote(String recNote) {
		this.recNote = recNote;
	}

	public Integer getCustReceiver() {
		return custReceiver;
	}

	public void setCustReceiver(Integer custReceiver) {
		this.custReceiver = custReceiver;
	}

	public String getCustReceiverName() {
		return custReceiverName;
	}

	public void setCustReceiverName(String custReceiverName) {
		this.custReceiverName = custReceiverName;
	}

	public Integer getPayFeeSpec() {
		return payFeeSpec;
	}

	public void setPayFeeSpec(Integer payFeeSpec) {
		this.payFeeSpec = payFeeSpec;
	}

	public String getPayFeeSpecName() {
		return payFeeSpecName;
	}

	public void setPayFeeSpecName(String payFeeSpecName) {
		this.payFeeSpecName = payFeeSpecName;
	}

	public Integer getPayAssistFeeSpec() {
		return payAssistFeeSpec;
	}

	public void setPayAssistFeeSpec(Integer payAssistFeeSpec) {
		this.payAssistFeeSpec = payAssistFeeSpec;
	}

	public String getPayAssistFeeSpecName() {
		return payAssistFeeSpecName;
	}

	public void setPayAssistFeeSpecName(String payAssistFeeSpecName) {
		this.payAssistFeeSpecName = payAssistFeeSpecName;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getAcceptInvoiceAmount() {
		return acceptInvoiceAmount;
	}

	public void setAcceptInvoiceAmount(BigDecimal acceptInvoiceAmount) {
		this.acceptInvoiceAmount = acceptInvoiceAmount;
	}

	public Integer getAcceptInvoiceType() {
		return acceptInvoiceType;
	}

	public void setAcceptInvoiceType(Integer acceptInvoiceType) {
		this.acceptInvoiceType = acceptInvoiceType;
	}

	public String getAcceptInvoiceTypeName() {
		return acceptInvoiceTypeName;
	}

	public void setAcceptInvoiceTypeName(String acceptInvoiceTypeName) {
		this.acceptInvoiceTypeName = acceptInvoiceTypeName;
	}

	public BigDecimal getAcceptInvoiceTaxRate() {
		return acceptInvoiceTaxRate;
	}

	public void setAcceptInvoiceTaxRate(BigDecimal acceptInvoiceTaxRate) {
		this.acceptInvoiceTaxRate = acceptInvoiceTaxRate;
	}

	public String getAcceptInvoiceTaxRateStr() {
		return acceptInvoiceTaxRateStr;
	}

	public void setAcceptInvoiceTaxRateStr(String acceptInvoiceTaxRateStr) {
		this.acceptInvoiceTaxRateStr = acceptInvoiceTaxRateStr;
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

	public String getPayNote() {
		return payNote;
	}

	public void setPayNote(String payNote) {
		this.payNote = payNote;
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

	public String getBusiUnitNameNo() {
		return busiUnitNameNo;
	}

	public void setBusiUnitNameNo(String busiUnitNameNo) {
		this.busiUnitNameNo = busiUnitNameNo;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public String getBusiUnitAddress() {
		return busiUnitAddress;
	}

	public void setBusiUnitAddress(String busiUnitAddress) {
		this.busiUnitAddress = busiUnitAddress;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public Date getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}

	public Date getAuditAt() {
		return auditAt;
	}

	public void setAuditAt(Date auditAt) {
		this.auditAt = auditAt;
	}

	public Date getBookDate() {
		return bookDate;
	}

	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}

	public BigDecimal getBlanceInvoiceAmount() {
		return blanceInvoiceAmount;
	}

	public void setBlanceInvoiceAmount(BigDecimal blanceInvoiceAmount) {
		this.blanceInvoiceAmount = blanceInvoiceAmount;
	}

	public BigDecimal getBlanceFeeAmount() {
		return blanceFeeAmount;
	}

	public void setBlanceFeeAmount(BigDecimal blanceFeeAmount) {
		this.blanceFeeAmount = blanceFeeAmount;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getPayFeeTypeName() {
		return payFeeTypeName;
	}

	public void setPayFeeTypeName(String payFeeTypeName) {
		this.payFeeTypeName = payFeeTypeName;
	} 
	public String getDeductionTypeName() {
		return deductionTypeName;
	}

	public void setDeductionTypeName(String deductionTypeName) {
		this.deductionTypeName = deductionTypeName;
	}

	public Integer getDeductionType() {
		return deductionType;
	}

	public void setDeductionType(Integer deductionType) {
		this.deductionType = deductionType;
	}

}
