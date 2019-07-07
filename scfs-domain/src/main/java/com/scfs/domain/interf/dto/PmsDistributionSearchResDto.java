package com.scfs.domain.interf.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

public class PmsDistributionSearchResDto {
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
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	 * 状态 1：待处理 2：处理失败 3：处理成功
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/**
	 * 修改时间
	 */
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateAt;

	/** 操作集合 */
	private List<CodeValue> opertaList;
	/**
	 * 接口类型名称
	 */
	private String typeName;
	/**
	 * 状态
	 */
	private String stateName;
	/**
	 * 采购单单号
	 */
	private String purchaseSn;
	/** 处理状态 **/
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

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PMS_DISTRIBUTION_SEARCH);
			operMap.put(OperateConsts.REINVOKE, BusUrlConsts.RESET_PMS_DISTRIBUTION_SEARCH);
		}
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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
