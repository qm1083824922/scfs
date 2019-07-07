package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: ReceiptOutRelResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月03日				Administrator
 *
 * </pre>
 */
public class ReceiptOutRelResDto {
	/**
	 * 水单和出库单关联ID
	 */
	private Integer id;
	/** 水单id **/
	private Integer receiptId;
	/** 出库单的ID **/
	private Integer outStroeId;
	/** 项目名称 **/
	private String projectName;
	/** 经营单位 **/
	private String busiUnit;
	/** 客户名称 **/
	private String custName;
	/** 币种 **/
	private String currencyTypeName;
	/** 核销金额 **/
	private BigDecimal writeOffAmount;
	/** 回款金额 **/
	private BigDecimal receivedAmount;
	/** 创建日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createAt;
	/**单据类型**/
	private  Integer  billType;
	/**单据名称**/
	private String billTypeName;
	/**单据日期**/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date billTime;
	public Date getBillTime() {
		return billTime;
	}
	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}
	public Integer getOutStroeId() {
		return outStroeId;
	}
	public void setOutStroeId(Integer outStroeId) {
		this.outStroeId = outStroeId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getBusiUnit() {
		return busiUnit;
	}
	public void setBusiUnit(String busiUnit) {
		this.busiUnit = busiUnit;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCurrencyTypeName() {
		return currencyTypeName;
	}
	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}
	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}
	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
	}
	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
}
