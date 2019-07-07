package com.scfs.domain.export.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.export.entity.RefundApplyLine;

/**
 * <pre>
 *  
 *  File: RefundApplyLineReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RefundApplyLineReqDto extends BaseReqDto {
	/** 退税申请信息ID **/
	private Integer refundApplyId;

	private List<RefundApplyLine> refundList = new ArrayList<RefundApplyLine>();

	public Integer getRefundApplyId() {
		return refundApplyId;
	}

	public void setRefundApplyId(Integer refundApplyId) {
		this.refundApplyId = refundApplyId;
	}

	public List<RefundApplyLine> getRefundList() {
		return refundList;
	}

	public void setRefundList(List<RefundApplyLine> refundList) {
		this.refundList = refundList;
	}

}
