package com.scfs.domain.fi.dto.req;

import java.math.BigDecimal;
import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: BankReceiptSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class BankReceiptSearchReqDto extends BaseReqDto {
	private Integer custId;
	private Integer busiUnit;
	private Integer projectId;
	private Integer state;
	private String startReceiptDate;
	private String endReceiptDate;
	private String bankReceiptNo;
	private String receiptNo;
	private Integer receiptType;
	private String summary;
	private BigDecimal receiptAmount;
	private Integer isOver;
	private BigDecimal writeAmount;
	/** 收款方式 **/
	private Integer receiptWay;
	private Integer currencyType;
	private Integer pid;
	private Integer isPayOver; // 1.已付完 2:未付完
	private Integer orderType; // 1.按id降序排序 2.按水单日期排序
	/** 付款类型 **/
	private Integer payType;
	private List<Integer> receiptTypeList;
	/** 收款账号 **/
	private Integer recAccountNo;
	/** 实际币种 **/
	private Integer actualCurrencyType;

	public BigDecimal getWriteAmount() {
		return writeAmount;
	}

	public void setWriteAmount(BigDecimal writeAmount) {
		this.writeAmount = writeAmount;
	}

	public Integer getCustId() {
		return custId;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStartReceiptDate() {
		return startReceiptDate;
	}

	public void setStartReceiptDate(String startReceiptDate) {
		this.startReceiptDate = startReceiptDate;
	}

	public String getEndReceiptDate() {
		return endReceiptDate;
	}

	public void setEndReceiptDate(String endReceiptDate) {
		this.endReceiptDate = endReceiptDate;
	}

	public String getBankReceiptNo() {
		return bankReceiptNo;
	}

	public void setBankReceiptNo(String bankReceiptNo) {
		this.bankReceiptNo = bankReceiptNo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Integer getIsOver() {
		return isOver;
	}

	public void setIsOver(Integer isOver) {
		this.isOver = isOver;
	}

	public Integer getReceiptWay() {
		return receiptWay;
	}

	public void setReceiptWay(Integer receiptWay) {
		this.receiptWay = receiptWay;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getIsPayOver() {
		return isPayOver;
	}

	public void setIsPayOver(Integer isPayOver) {
		this.isPayOver = isPayOver;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public List<Integer> getReceiptTypeList() {
		return receiptTypeList;
	}

	public void setReceiptTypeList(List<Integer> receiptTypeList) {
		this.receiptTypeList = receiptTypeList;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(Integer recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public Integer getActualCurrencyType() {
		return actualCurrencyType;
	}

	public void setActualCurrencyType(Integer actualCurrencyType) {
		this.actualCurrencyType = actualCurrencyType;
	}

}
