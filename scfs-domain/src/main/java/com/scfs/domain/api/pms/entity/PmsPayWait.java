package com.scfs.domain.api.pms.entity;

import java.util.List;

/**
 * <pre>
 * 
 *  File: PmsPayWait.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月06日			Administrator
 *
 * </pre>
 */
public class PmsPayWait {

	private PmsPay pms_pay;

	private List<PmsPayDtl> pms_pay_dtl;

	public PmsPay getPms_pay() {
		return pms_pay;
	}

	public void setPms_pay(PmsPay pms_pay) {
		this.pms_pay = pms_pay;
	}

	public List<PmsPayDtl> getPms_pay_dtl() {
		return pms_pay_dtl;
	}

	public void setPms_pay_dtl(List<PmsPayDtl> pms_pay_dtl) {
		this.pms_pay_dtl = pms_pay_dtl;
	}

}
