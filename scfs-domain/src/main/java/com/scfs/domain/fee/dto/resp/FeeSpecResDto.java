package com.scfs.domain.fee.dto.resp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: FeeSpecResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月2日			Administrator
 *
 * </pre>
 */
public class FeeSpecResDto {
	private Integer id;
	private Integer feeType;
	private String feeTypeName;
	private String feeSpecNo;
	private String feeSpecName;

	/** 管理一级名称 **/
	private Integer feeOneName;
	private String feeOneNameValue;
	/** 管理二级名称 **/
	private Integer feeTwoName;
	private String feeTwoNameValue;
	/** 财务科目编码 **/
	private String financeCode;
	/** 备注 **/
	private String remark;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDITFEESPEC);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAILFEESPEC);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETEFEESPEC);
		}
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

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
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

	public String getFeeOneNameValue() {
		return feeOneNameValue;
	}

	public void setFeeOneNameValue(String feeOneNameValue) {
		this.feeOneNameValue = feeOneNameValue;
	}

	public Integer getFeeTwoName() {
		return feeTwoName;
	}

	public void setFeeTwoName(Integer feeTwoName) {
		this.feeTwoName = feeTwoName;
	}

	public String getFeeTwoNameValue() {
		return feeTwoNameValue;
	}

	public void setFeeTwoNameValue(String feeTwoNameValue) {
		this.feeTwoNameValue = feeTwoNameValue;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
