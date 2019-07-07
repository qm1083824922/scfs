package com.scfs.domain.invoice.dto.req;

import java.util.ArrayList;
import java.util.List;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasPo;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectOverseasPoReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月15日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectOverseasPoReqDto extends BaseReqDto {

	/** Invoice收票id **/
	private Integer collectOverseasId;

	List<InvoiceCollectOverseasPo> overseasPos = new ArrayList<InvoiceCollectOverseasPo>();

	public Integer getCollectOverseasId() {
		return collectOverseasId;
	}

	public void setCollectOverseasId(Integer collectOverseasId) {
		this.collectOverseasId = collectOverseasId;
	}

	public List<InvoiceCollectOverseasPo> getOverseasPos() {
		return overseasPos;
	}

	public void setOverseasPos(List<InvoiceCollectOverseasPo> overseasPos) {
		this.overseasPos = overseasPos;
	}

}
