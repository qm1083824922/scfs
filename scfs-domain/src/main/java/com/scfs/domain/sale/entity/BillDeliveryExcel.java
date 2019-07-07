package com.scfs.domain.sale.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016年12月29日.
 */
public class BillDeliveryExcel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3259845634936939006L;

	/**
	 * 销售附属编号
	 */
	private String affiliateNo;
	/**
	 * 项目编号
	 */
	private String projectNo;
	/**
	 * 仓库编号
	 */
	private String warehouseNo;
	/**
	 * 客户编号
	 */
	private String customerNo;
	/**
	 * 运输方式
	 */
	private String transferModeName;
	/**
	 * 销售日期
	 */
	private Date requiredSendDate;
	/**
	 * 币种
	 */
	private String currencyTypeName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 商品编号
	 */
	private String goodsNo;
	/**
	 * 销售数量
	 */
	private String requiredSendNum;
	/**
	 * 销售价
	 */
	private String requiredSendPrice;
	/**
	 * 批次
	 */
	private String batchNo;

	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 仓库ID
	 */
	private Integer warehouseId;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 运输方式
	 */
	private Integer transferMode;
	/**
	 * 收货地址
	 */
	private Integer customerAddressId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getTransferModeName() {
		return transferModeName;
	}

	public void setTransferModeName(String transferModeName) {
		this.transferModeName = transferModeName;
	}

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(String requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public String getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(String requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Integer transferMode) {
		this.transferMode = transferMode;
	}

	public Integer getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(Integer customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((affiliateNo == null) ? 0 : affiliateNo.hashCode());
		result = prime * result + ((currencyTypeName == null) ? 0 : currencyTypeName.hashCode());
		result = prime * result + ((customerNo == null) ? 0 : customerNo.hashCode());
		result = prime * result + ((projectNo == null) ? 0 : projectNo.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((requiredSendDate == null) ? 0 : requiredSendDate.hashCode());
		result = prime * result + ((transferModeName == null) ? 0 : transferModeName.hashCode());
		result = prime * result + ((warehouseNo == null) ? 0 : warehouseNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillDeliveryExcel other = (BillDeliveryExcel) obj;
		if (affiliateNo == null) {
			if (other.affiliateNo != null)
				return false;
		} else if (!affiliateNo.equals(other.affiliateNo))
			return false;
		if (currencyTypeName == null) {
			if (other.currencyTypeName != null)
				return false;
		} else if (!currencyTypeName.equals(other.currencyTypeName))
			return false;
		if (customerNo == null) {
			if (other.customerNo != null)
				return false;
		} else if (!customerNo.equals(other.customerNo))
			return false;
		if (projectNo == null) {
			if (other.projectNo != null)
				return false;
		} else if (!projectNo.equals(other.projectNo))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (requiredSendDate == null) {
			if (other.requiredSendDate != null)
				return false;
		} else if (!requiredSendDate.equals(other.requiredSendDate))
			return false;
		if (transferModeName == null) {
			if (other.transferModeName != null)
				return false;
		} else if (!transferModeName.equals(other.transferModeName))
			return false;
		if (warehouseNo == null) {
			if (other.warehouseNo != null)
				return false;
		} else if (!warehouseNo.equals(other.warehouseNo))
			return false;
		return true;
	}

}
