package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年11月23日.
 */
public class BillInStoreTallyDtlSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -302287125587740748L;
	private BigDecimal tallyNum;

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
	}

}
