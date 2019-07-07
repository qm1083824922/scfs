package com.scfs.domain.pay.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.pay.entity.PayRefundRelation;

/**
 * <pre>
 * 
 *  File: PayRefundRelationReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月20日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayRefundRelationReqDto extends BaseReqDto {
	/** 关系id **/
	private Integer id;
	/** 付款id **/
	private Integer payId;
	private List<Integer> payIds;
	// 付款单和退款信息的关联表
	private List<PayRefundRelation> relList = new ArrayList<PayRefundRelation>();

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

	public List<Integer> getPayIds() {
		return payIds;
	}

	public void setPayIds(List<Integer> payIds) {
		this.payIds = payIds;
	}

	public List<PayRefundRelation> getRelList() {
		return relList;
	}

	public void setRelList(List<PayRefundRelation> relList) {
		this.relList = relList;
	}

}
