package com.scfs.domain.fi.dto.req;

import java.math.BigDecimal;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: PrepaidReceiptRelReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 * 2017年10月31日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PrepaidReceiptRelReqDto extends BaseReqDto {

	/** 水单id **/
	private Integer receiptId;
	/**
	 * 项目id
	 */
	private Integer projectId;
	/**
	 * 客户id
	 */
	private Integer customerId;

	/**
	 * 经营单位
	 */
	private Integer busiUnitId;
	/**
	 * 币种
	 */
	private Integer currnecyType;
	/**
	 * 实际转预付金额
	 */
	private BigDecimal actualPrepaidAmount;
	/**
	 * 转预付类型
	 */
	private Integer prepaidType;

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public BigDecimal getActualPrepaidAmount() {
		return actualPrepaidAmount;
	}

	public void setActualPrepaidAmount(BigDecimal actualPrepaidAmount) {
		this.actualPrepaidAmount = actualPrepaidAmount;
	}

	public Integer getPrepaidType() {
		return prepaidType;
	}

	public void setPrepaidType(Integer prepaidType) {
		this.prepaidType = prepaidType;
	}

}
