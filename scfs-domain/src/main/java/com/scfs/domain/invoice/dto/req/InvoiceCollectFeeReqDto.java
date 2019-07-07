package com.scfs.domain.invoice.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectFee;

/**
 * <pre>
 * 
 *  File: InvoiceCollectFeeReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectFeeReqDto extends BaseReqDto {
	/** 收票id **/
	private Integer collectId;

	private List<InvoiceCollectFee> colRel = new ArrayList<InvoiceCollectFee>();

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	public List<InvoiceCollectFee> getColRel() {
		return colRel;
	}

	public void setColRel(List<InvoiceCollectFee> colRel) {
		this.colRel = colRel;
	}

}
