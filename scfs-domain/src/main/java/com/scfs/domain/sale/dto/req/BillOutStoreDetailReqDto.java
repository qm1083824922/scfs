package com.scfs.domain.sale.dto.req;

import java.math.BigDecimal;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年3月22日.
 */
public class BillOutStoreDetailReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870050041905270536L;

	private Integer billOutStorePickDtlId;

	private BigDecimal requiredSendNum;

	private BigDecimal requiredSendPrice;

	public Integer getBillOutStorePickDtlId() {
		return billOutStorePickDtlId;
	}

	public void setBillOutStorePickDtlId(Integer billOutStorePickDtlId) {
		this.billOutStorePickDtlId = billOutStorePickDtlId;
	}

	public BigDecimal getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(BigDecimal requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

}
