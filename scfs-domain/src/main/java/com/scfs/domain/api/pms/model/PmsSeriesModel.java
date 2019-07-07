package com.scfs.domain.api.pms.model;

import java.util.Date;

public class PmsSeriesModel {

	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 接口类型 1-pms入库单接口 2-pms出库单接口 3-pms请款单接口
	 */
	private Integer type;

	/**
	 * 调用时间
	 */
	private Date invokeTime;

	/**
	 * 流水号
	 */
	private String seriesNo;

	/**
	 * 描述
	 */
	private String message;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;
	/** 采购单号（请款单号） **/
	private String purchaseSn;
	private Integer dealflag;

	public Integer getDealflag() {
		return dealflag;
	}

	public void setDealflag(Integer dealflag) {
		this.dealflag = dealflag;
	}

	public String getPurchaseSn() {
		return purchaseSn;
	}

	public void setPurchaseSn(String purchaseSn) {
		this.purchaseSn = purchaseSn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getInvokeTime() {
		return invokeTime;
	}

	public void setInvokeTime(Date invokeTime) {
		this.invokeTime = invokeTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message == null ? null : message.trim();
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

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
}
