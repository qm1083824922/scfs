package com.scfs.domain.logistics.dto.req;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class StlHistorySummarySearchReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8270838949468370882L;

	private Integer projectFlag;
	private Integer supplierFlag;
	private Integer warehouseFlag;
	private Integer customerFlag;
	private Integer goodsFlag;
	private Integer batchNoFlag;
	private Integer goodsStatusFlag;
	private Integer currencyTypeFlag;
	/**
	 * 复制日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date copyDate;
	/**
	 * 库存大于零标识
	 */
	private Integer storeFlag;

	@SuppressWarnings("unused")
	private List<String> groupByCondition;

	public Integer getProjectFlag() {
		return projectFlag;
	}

	public void setProjectFlag(Integer projectFlag) {
		this.projectFlag = projectFlag;
	}

	public Integer getSupplierFlag() {
		return supplierFlag;
	}

	public void setSupplierFlag(Integer supplierFlag) {
		this.supplierFlag = supplierFlag;
	}

	public Integer getWarehouseFlag() {
		return warehouseFlag;
	}

	public void setWarehouseFlag(Integer warehouseFlag) {
		this.warehouseFlag = warehouseFlag;
	}

	public Integer getCustomerFlag() {
		return customerFlag;
	}

	public void setCustomerFlag(Integer customerFlag) {
		this.customerFlag = customerFlag;
	}

	public Integer getGoodsStatusFlag() {
		return goodsStatusFlag;
	}

	public void setGoodsStatusFlag(Integer goodsStatusFlag) {
		this.goodsStatusFlag = goodsStatusFlag;
	}

	public Integer getBatchNoFlag() {
		return batchNoFlag;
	}

	public void setBatchNoFlag(Integer batchNoFlag) {
		this.batchNoFlag = batchNoFlag;
	}

	public Integer getGoodsFlag() {
		return goodsFlag;
	}

	public void setGoodsFlag(Integer goodsFlag) {
		this.goodsFlag = goodsFlag;
	}

	public List<String> getGroupByCondition() {
		List<String> groupByConditionList = new ArrayList<String>(5);
		if (null != this.getProjectFlag() && this.getProjectFlag() == 1) {
			groupByConditionList.add("project_id");
		}
		if (null != this.getSupplierFlag() && this.getSupplierFlag() == 1) {
			groupByConditionList.add("supplier_id");
		}
		if (null != this.getWarehouseFlag() && this.getWarehouseFlag() == 1) {
			groupByConditionList.add("warehouse_id");
		}
		if (null != this.getCustomerFlag() && this.getCustomerFlag() == 1) {
			groupByConditionList.add("customer_id");
		}
		if (null != this.getGoodsFlag() && this.getGoodsFlag() == 1) {
			groupByConditionList.add("goods_id");
		}
		if (null != this.getBatchNoFlag() && this.getBatchNoFlag() == 1) {
			groupByConditionList.add("batch_no");
		}
		if (null != this.getGoodsStatusFlag() && this.getGoodsStatusFlag() == 1) {
			groupByConditionList.add("goods_status");
		}
		if (null != this.getCurrencyTypeFlag() && this.getCurrencyTypeFlag() == 1) {
			groupByConditionList.add("currency_type");
		}
		if (!CollectionUtils.isEmpty(groupByConditionList)) {
			return groupByConditionList;
		} else {
			return null;
		}
	}

	public void setGroupByCondition(List<String> groupByCondition) {
		this.groupByCondition = groupByCondition;
	}

	public Date getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(Date copyDate) {
		this.copyDate = copyDate;
	}

	public Integer getStoreFlag() {
		return storeFlag;
	}

	public void setStoreFlag(Integer storeFlag) {
		this.storeFlag = storeFlag;
	}

	public Integer getCurrencyTypeFlag() {
		return currencyTypeFlag;
	}

	public void setCurrencyTypeFlag(Integer currencyTypeFlag) {
		this.currencyTypeFlag = currencyTypeFlag;
	}

}
