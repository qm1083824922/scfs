package com.scfs.domain.pay.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.pay.entity.PayFeeRelation;

/**
 * <pre>
 * 
 *  File: PayPoRelationReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月10日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayFeeRelationReqDto extends BaseReqDto {
	/** 付款id **/
	private Integer payId;
	/** 付款费用类型 **/
	private Integer payFeeType;
	/** 付款列表 **/
	private List<Integer> payIds;
	/** 费用id **/
	private Integer feeId;
	/** 是否核销 0-未核销 1-已核销 */
	private Integer writeOffFlag;
	
	private List<PayFeeRelation> relList = new ArrayList<PayFeeRelation>();

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public List<Integer> getPayIds() {
		return payIds;
	}

	public void setPayIds(List<Integer> payIds) {
		this.payIds = payIds;
	}

	public List<PayFeeRelation> getRelList() {
		return relList;
	}

	public void setRelList(List<PayFeeRelation> relList) {
		this.relList = relList;
	}

	public Integer getPayFeeType() {
		return payFeeType;
	}

	public void setPayFeeType(Integer payFeeType) {
		this.payFeeType = payFeeType;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

}
