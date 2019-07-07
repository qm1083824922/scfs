package com.scfs.rpc.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class SupplierInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952619566805065257L;

	/**
	 * 供应商编码
	 */
	private String provider_sn;
	/**
	 * 扣点
	 */
	private BigDecimal deduction_point;
	/**
	 * 添加时间
	 */
	private Date create_time;
	/**
	 * 状态
	 */
	private Integer status;

	public String getProvider_sn() {
		return provider_sn;
	}

	public void setProvider_sn(String provider_sn) {
		this.provider_sn = provider_sn;
	}

	public BigDecimal getDeduction_point() {
		return deduction_point;
	}

	public void setDeduction_point(BigDecimal deduction_point) {
		this.deduction_point = deduction_point;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
