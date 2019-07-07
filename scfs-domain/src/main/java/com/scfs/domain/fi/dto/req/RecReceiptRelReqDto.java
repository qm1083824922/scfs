package com.scfs.domain.fi.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.common.consts.BaseConsts;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.fi.entity.RecReceiptRel;

/**
 * <pre>
 * 
 *  File: RecReceiptRelResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月4日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RecReceiptRelReqDto extends BaseReqDto {
	/** 水单id **/
	private Integer receiptId;
	
	/**是否校验核销金额和应收金额
	 *     1 不校验 0 校验
	 */
	private Integer  wtCheckWriteAmount;

	public Integer getWtCheckWriteAmount() {
		return wtCheckWriteAmount;
	}

	public void setWtCheckWriteAmount(Integer wtCheckWriteAmount) {
		this.wtCheckWriteAmount = wtCheckWriteAmount;
	}

	private List<RecReceiptRel> relList = new ArrayList<RecReceiptRel>();

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
        this.wtCheckWriteAmount = BaseConsts.ZERO;
		this.receiptId = receiptId;
	}

	public List<RecReceiptRel> getRelList() {
		return relList;
	}

	public void setRelList(List<RecReceiptRel> relList) {
		this.relList = relList;
	}

}
