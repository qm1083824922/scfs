package com.scfs.domain.invoice.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectPo;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectPo.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectPoReqDto extends BaseReqDto {
	/** 收票id **/
	private Integer collectId;

	List<InvoiceCollectPo> colRel = new ArrayList<InvoiceCollectPo>();

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	public List<InvoiceCollectPo> getColRel() {
		return colRel;
	}

	public void setColRel(List<InvoiceCollectPo> colRel) {
		this.colRel = colRel;
	}

}
