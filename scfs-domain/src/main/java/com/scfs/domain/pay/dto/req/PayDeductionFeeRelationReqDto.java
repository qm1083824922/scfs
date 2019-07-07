package com.scfs.domain.pay.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.pay.entity.PayDeductionFeeRelation;

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
public class PayDeductionFeeRelationReqDto extends BaseReqDto {
	/** 付款id **/
	private Integer payId;
	/** 付款费用类型 **/
	private Integer payFeeType;
	/** 付款列表 **/
	private List<Integer> payIds;
	/** 费用id **/
	private Integer feeId;

	private List<PayDeductionFeeRelation> relList = new ArrayList<PayDeductionFeeRelation>();

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

	public List<PayDeductionFeeRelation> getRelList() {
		return relList;
	}

	public void setRelList(List<PayDeductionFeeRelation> relList) {
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

}
