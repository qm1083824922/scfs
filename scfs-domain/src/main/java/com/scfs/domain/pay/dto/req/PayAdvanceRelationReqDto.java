package com.scfs.domain.pay.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.pay.entity.PayAdvanceRelation;

/**
 * <pre>
 * 
 *  File: PayAdvanceRelationReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayAdvanceRelationReqDto extends BaseReqDto {
	/** 关系id **/
	private Integer id;
	/** 付款id **/
	private Integer payId;
	private List<Integer> payIds;

	private List<PayAdvanceRelation> relList = new ArrayList<PayAdvanceRelation>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public List<PayAdvanceRelation> getRelList() {
		return relList;
	}

	public void setRelList(List<PayAdvanceRelation> relList) {
		this.relList = relList;
	}

	public List<Integer> getPayIds() {
		return payIds;
	}

	public void setPayIds(List<Integer> payIds) {
		this.payIds = payIds;
	}

}
