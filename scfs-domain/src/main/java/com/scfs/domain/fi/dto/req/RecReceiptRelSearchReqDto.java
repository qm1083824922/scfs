package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: RecReceiptRelSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RecReceiptRelSearchReqDto extends BaseReqDto {
	/** 水单id **/
	private Integer receiptId;
	/**应收id **/
	private Integer recId;

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

    public Integer getRecId()
    {
        return recId;
    }

    public void setRecId(Integer recId)
    {
        this.recId = recId;
    }

}
