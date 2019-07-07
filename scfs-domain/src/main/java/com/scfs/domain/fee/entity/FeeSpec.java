package com.scfs.domain.fee.entity;

import java.io.Serializable;

/**
 * <pre>
 * 
 *  File: FeeItem.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator
 *
 * </pre>
 */
public class FeeSpec implements Serializable {
	private static final long serialVersionUID = 2310630383288144006L;
	private Integer id;
	private Integer feeType;
	private String feeSpecNo;
	private String feeSpecName;

	/** 管理一级名称 **/
	private Integer feeOneName;
	/** 管理二级名称 **/
	private Integer feeTwoName;
	/** 财务科目编码 **/
	private String financeCode;
	/** 备注 **/
	private String remark;

	public String getNameNo() {
		return this.feeSpecNo+"-" + this.feeSpecName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public String getFeeSpecNo() {
		return feeSpecNo;
	}

	public void setFeeSpecNo(String feeSpecNo) {
		this.feeSpecNo = feeSpecNo;
	}

	public String getFeeSpecName() {
		return feeSpecName;
	}

	public void setFeeSpecName(String feeSpecName) {
		this.feeSpecName = feeSpecName;
	}

	public Integer getFeeOneName() {
		return feeOneName;
	}

	public void setFeeOneName(Integer feeOneName) {
		this.feeOneName = feeOneName;
	}

	public Integer getFeeTwoName() {
		return feeTwoName;
	}

	public void setFeeTwoName(Integer feeTwoName) {
		this.feeTwoName = feeTwoName;
	}

	public String getFinanceCode() {
		return financeCode;
	}

	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
