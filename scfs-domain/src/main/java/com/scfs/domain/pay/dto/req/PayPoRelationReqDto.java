package com.scfs.domain.pay.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.pay.entity.PayPoRelation;

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
public class PayPoRelationReqDto extends BaseReqDto {
	/** 关系id **/
	private Integer id;
	/** 付款id **/
	private Integer payId;
	/** 付款id List **/
	private List<Integer> payIds;
	/** 是否核销 0-未核销 1-已核销 */
	private Integer writeOffFlag;

	private List<PayPoRelation> relList = new ArrayList<PayPoRelation>();

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

	public List<PayPoRelation> getRelList() {
		return relList;
	}

	public void setRelList(List<PayPoRelation> relList) {
		this.relList = relList;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

}
