package com.scfs.domain.invoice.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasFee;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectOverseasFeeReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月15日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectOverseasFeeReqDto extends BaseReqDto {

	/** Invoice收票id **/
	private Integer collectOverseasId;

	List<InvoiceCollectOverseasFee> overseasFees = new ArrayList<InvoiceCollectOverseasFee>();

	public Integer getCollectOverseasId() {
		return collectOverseasId;
	}

	public void setCollectOverseasId(Integer collectOverseasId) {
		this.collectOverseasId = collectOverseasId;
	}

	public List<InvoiceCollectOverseasFee> getOverseasFees() {
		return overseasFees;
	}

	public void setOverseasFees(List<InvoiceCollectOverseasFee> overseasFees) {
		this.overseasFees = overseasFees;
	}
}
