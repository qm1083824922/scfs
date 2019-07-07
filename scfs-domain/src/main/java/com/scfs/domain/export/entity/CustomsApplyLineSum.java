package com.scfs.domain.export.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年12月6日.
 */
public class CustomsApplyLineSum implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8321848952577461975L;
    /**
     * 报关申请ID
     */ 
    private Integer customsApplyId;
    
    /**
     * 报关数量
     */ 
    private BigDecimal customsNum;
    
    /**
     * 报关含税金额
     */ 
    private BigDecimal customsAmount;

    
	public Integer getCustomsApplyId() {
		return customsApplyId;
	}

	public void setCustomsApplyId(Integer customsApplyId) {
		this.customsApplyId = customsApplyId;
	}

	public BigDecimal getCustomsNum() {
		return customsNum;
	}

	public void setCustomsNum(BigDecimal customsNum) {
		this.customsNum = customsNum;
	}

	public BigDecimal getCustomsAmount() {
		return customsAmount;
	}

	public void setCustomsAmount(BigDecimal customsAmount) {
		this.customsAmount = customsAmount;
	}
    
    
}

