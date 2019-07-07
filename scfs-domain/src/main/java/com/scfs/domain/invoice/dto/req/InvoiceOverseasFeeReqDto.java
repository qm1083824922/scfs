package com.scfs.domain.invoice.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.invoice.entity.InvoiceOverseasFee;

/**
 * <pre>
 * 	境外发票费用信息
 *  File: InvoiceOverseasFeeResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasFeeReqDto extends BaseReqDto {
	/** 境外收票id **/
	private Integer overseasId;
	/** 费用编号 **/
	private String feeNo;

	private List<InvoiceOverseasFee> feeList;

	public Integer getOverseasId() {
		return overseasId;
	}

	public void setOverseasId(Integer overseasId) {
		this.overseasId = overseasId;
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}

	public List<InvoiceOverseasFee> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<InvoiceOverseasFee> feeList) {
		this.feeList = feeList;
	}

}
