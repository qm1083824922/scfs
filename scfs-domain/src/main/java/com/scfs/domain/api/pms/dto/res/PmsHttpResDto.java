package com.scfs.domain.api.pms.dto.res;

/**
 * <pre>
 * 
 *  File: PmsHttpResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月15日			Administrator
 *
 * </pre>
 */

public class PmsHttpResDto extends BasePmsHttpResDto {
	private static final long serialVersionUID = 6983851370933160611L;

	private String payNo;

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

}
