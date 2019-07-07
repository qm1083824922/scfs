package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderRelResDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayAdvanceRelationResDto;
import com.scfs.domain.pay.dto.resq.PayFeeRelationResDto;
import com.scfs.domain.pay.dto.resq.PayOrderFileResDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;

/**
 * <pre>
 * 
 *  File: MergePayOrderAuditInfo.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */

public class MergePayOrderAuditInfo {
	private MergePayOrderResDto MergePayOrderResDto;
	private List<MergePayOrderRelResDto> mergePayOrderRelResDtos;
	private List<AuditFlowsResDto> auditFlowsResDtos;

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

	public MergePayOrderResDto getMergePayOrderResDto() {
		return MergePayOrderResDto;
	}

	public void setMergePayOrderResDto(MergePayOrderResDto mergePayOrderResDto) {
		MergePayOrderResDto = mergePayOrderResDto;
	}

	public List<MergePayOrderRelResDto> getMergePayOrderRelResDtos() {
		return mergePayOrderRelResDtos;
	}

	public void setMergePayOrderRelResDtos(List<MergePayOrderRelResDto> mergePayOrderRelResDtos) {
		this.mergePayOrderRelResDtos = mergePayOrderRelResDtos;
	}

	public List<AuditFlowsResDto> getAuditFlowsResDtos() {
		return auditFlowsResDtos;
	}

	public void setAuditFlowsResDtos(List<AuditFlowsResDto> auditFlowsResDtos) {
		this.auditFlowsResDtos = auditFlowsResDtos;
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
