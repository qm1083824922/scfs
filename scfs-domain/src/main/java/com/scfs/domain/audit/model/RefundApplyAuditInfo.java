package com.scfs.domain.audit.model;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.export.dto.resp.RefundApplyLineResDto;
import com.scfs.domain.export.dto.resp.RefundApplyResDto;

/**
 * <pre>
 * 
 *  File: RefundApplyAuditInfo.java
 *  Description: 
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日			Administrator
 *
 * </pre>
 */
public class RefundApplyAuditInfo {
	/** 退税基本信息 **/
	private RefundApplyResDto refundApply;

	/** 退税明细信息 **/
	private List<RefundApplyLineResDto> refundList = new ArrayList<RefundApplyLineResDto>();

	public RefundApplyResDto getRefundApply() {
		return refundApply;
	}

	public void setRefundApply(RefundApplyResDto refundApply) {
		this.refundApply = refundApply;
	}

	public List<RefundApplyLineResDto> getRefundList() {
		return refundList;
	}

	public void setRefundList(List<RefundApplyLineResDto> refundList) {
		this.refundList = refundList;
	}

}
