package com.scfs.domain.api.cms.dto.res;

import com.scfs.domain.api.pms.dto.res.BasePmsHttpResDto;

public class CmsPayConfirmHttpResDto extends BasePmsHttpResDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5080318616404494552L;
	private String pay_no;
	
	public String getPay_no() {
		return pay_no;
	}
	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
	}


}
