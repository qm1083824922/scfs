package com.scfs.domain.api.pms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PmsReturn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * PMS退货单号
	 */
	private String refund_order_sn;

	/**
	 * 供应商编号
	 */
	private String provider_sn;

	/**
	 * 退货日期
	 */
	private Date submit_time;

	/**
	 * 事业部
	 */
	private String division_code;

	/**
	 * 处理结果 1-待处理 2-处理失败 3-处理成功
	 */
	private Integer dealFlag;

	/**
	 * 处理消息
	 */
	private String dealMsg;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	private List<PmsReturnDtl> details;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRefund_order_sn() {
		return refund_order_sn;
	}

	public void setRefund_order_sn(String refund_order_sn) {
		this.refund_order_sn = refund_order_sn == null ? null : refund_order_sn.trim();
	}

	public String getProvider_sn() {
		return provider_sn;
	}

	public void setProvider_sn(String provider_sn) {
		this.provider_sn = provider_sn == null ? null : provider_sn.trim();
	}

	public Date getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(Date submit_time) {
		this.submit_time = submit_time;
	}

	public String getDivision_code() {
		return division_code;
	}

	public void setDivision_code(String division_code) {
		this.division_code = division_code == null ? null : division_code.trim();
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getDealMsg() {
		return dealMsg;
	}

	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg == null ? null : dealMsg.trim();
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public List<PmsReturnDtl> getDetails() {
		return details;
	}

	public void setDetails(List<PmsReturnDtl> details) {
		this.details = details;
	}

}