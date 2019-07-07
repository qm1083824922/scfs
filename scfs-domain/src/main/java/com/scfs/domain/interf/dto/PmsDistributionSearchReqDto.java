package com.scfs.domain.interf.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.api.pms.entity.PmsSeries;

/**
 * 
 * @author 
 *
 */
@SuppressWarnings("serial")
public class PmsDistributionSearchReqDto extends BaseReqDto {

	/**
	 * PMS铺货的ID
	 */
	private Integer id;

	/**
	 * 接口类型 1-pms入库单接口 2-pms出库单接口 3-pms请款单接口
	 */
	private Integer type;

	/** PMS明细ID **/
	private Integer StoreId;

	private Integer state;

	/** 确认开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/** 确认结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	private List<Integer> StoreIds;

	// 付款单和退款信息的关联表
	private List<PmsSeries> pmsList = new ArrayList<PmsSeries>();

	public List<PmsSeries> getPmsList() {
		return pmsList;
	}

	public void setPmsList(List<PmsSeries> pmsList) {
		this.pmsList = pmsList;
	}

	public Integer getStoreId() {
		return StoreId;
	}

	public void setStoreId(Integer storeId) {
		StoreId = storeId;
	}

	public List<Integer> getStoreIds() {
		return StoreIds;
	}

	public void setStoreIds(List<Integer> storeIds) {
		StoreIds = storeIds;
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

}
