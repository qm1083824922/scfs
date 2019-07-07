package com.scfs.domain.pay.model;

import java.util.List;

import com.scfs.domain.pay.dto.resq.PayAdvanceRelationResDto;
import com.scfs.domain.pay.dto.resq.PayFeeRelationResDto;
import com.scfs.domain.pay.dto.resq.PayOrderFileResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;

/**
 * <pre>
 * 
 *  File: PayAuditInfo.java
 *  Description:付款审核信息
 *  TODO
 *  Date,					Who,				
 *  2016年11月18日			Administrator
 *
 * </pre>
 */
public class PayAuditInfo {

	/**
	 * 付款审核付款信息
	 */
	private PayOrderResDto payOrderResDto;
	/**
	 * 付款审核订单信息
	 */
	List<PayPoRelationResDto> payPoRelationResDto;

	/**
	 * 付款审核费用信息
	 */
	List<PayFeeRelationResDto> payFeeRelationResDto;

	/** 付款附件信息 **/
	List<PayOrderFileResDto> payOrderFileList;

	/** 付款预收信息 **/
	List<PayAdvanceRelationResDto> payAdvanceRelation;

	public PayOrderResDto getPayOrderResDto() {
		return payOrderResDto;
	}

	public void setPayOrderResDto(PayOrderResDto payOrderResDto) {
		this.payOrderResDto = payOrderResDto;
	}

	public List<PayPoRelationResDto> getPayPoRelationResDto() {
		return payPoRelationResDto;
	}

	public void setPayPoRelationResDto(List<PayPoRelationResDto> payPoRelationResDto) {
		this.payPoRelationResDto = payPoRelationResDto;
	}

	public List<PayFeeRelationResDto> getPayFeeRelationResDto() {
		return payFeeRelationResDto;
	}

	public void setPayFeeRelationResDto(List<PayFeeRelationResDto> payFeeRelationResDto) {
		this.payFeeRelationResDto = payFeeRelationResDto;
	}

	public List<PayOrderFileResDto> getPayOrderFileList() {
		return payOrderFileList;
	}

	public void setPayOrderFileList(List<PayOrderFileResDto> payOrderFileList) {
		this.payOrderFileList = payOrderFileList;
	}

	public List<PayAdvanceRelationResDto> getPayAdvanceRelation() {
		return payAdvanceRelation;
	}

	public void setPayAdvanceRelation(List<PayAdvanceRelationResDto> payAdvanceRelation) {
		this.payAdvanceRelation = payAdvanceRelation;
	}

}
