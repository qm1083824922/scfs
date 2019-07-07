package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 * 
 *  File: PayOrderBatchConfirmResp.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月10日				Administrator
 *
 * </pre>
 */
public class PayOrderBatchConfirmResp {
	List<PayOrderResDto> payOrderResDto;
	BigDecimal sumPayAmount;

	public List<PayOrderResDto> getPayOrderResDto() {
		return payOrderResDto;
	}

	public void setPayOrderResDto(List<PayOrderResDto> payOrderResDto) {
		this.payOrderResDto = payOrderResDto;
	}

	public BigDecimal getSumPayAmount() {
		return sumPayAmount;
	}

	public void setSumPayAmount(BigDecimal sumPayAmount) {
		this.sumPayAmount = sumPayAmount;
	}

}
