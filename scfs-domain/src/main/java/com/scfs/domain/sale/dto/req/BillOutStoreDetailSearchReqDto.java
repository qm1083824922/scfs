package com.scfs.domain.sale.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年3月22日.
 */
public class BillOutStoreDetailSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1113742647189051719L;

	private Integer billOutStorePickDtlId;

	private Integer billDeliveryId;

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
	 * 销售编号
	 */
	private String billDeliveryNo;
	/**
	 * 销售附属编号
	 */
	private String billDeliveryAffiliateNo;
	/**
	 * 出库编号
	 */
	private String billOutStoreNo;
	/**
	 * 出库附属编号
	 */
	private String billOutStoreAffiliateNo;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品型号
	 */
	private String goodsType;
	/**
	 * 商品条码
	 */
	private String goodsBarCode;
	/**
	 * 开始收货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startRequiredSendDate;
	/**
	 * 结束收货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endRequiredSendDate;
	/**
	 * 开始入库日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDeliverTime;
	/**
	 * 结束入库日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDeliverTime;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 状态 1-正常 2-残次品
	 */
	private Integer goodsStatus;
	/**
	 * 商品ID
	 */
	private Integer goodsId;

	/**
	 * 出库类型
	 */
	private Integer billType;
	/***
	 * 供应商ID
	 */
	private Integer supplierId;

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getBillOutStorePickDtlId() {
		return billOutStorePickDtlId;
	}

	public void setBillOutStorePickDtlId(Integer billOutStorePickDtlId) {
		this.billOutStorePickDtlId = billOutStorePickDtlId;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
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

	public String getBillDeliveryNo() {
		return billDeliveryNo;
	}

	public void setBillDeliveryNo(String billDeliveryNo) {
		this.billDeliveryNo = billDeliveryNo;
	}

	public String getBillDeliveryAffiliateNo() {
		return billDeliveryAffiliateNo;
	}

	public void setBillDeliveryAffiliateNo(String billDeliveryAffiliateNo) {
		this.billDeliveryAffiliateNo = billDeliveryAffiliateNo;
	}

	public String getBillOutStoreNo() {
		return billOutStoreNo;
	}

	public void setBillOutStoreNo(String billOutStoreNo) {
		this.billOutStoreNo = billOutStoreNo;
	}

	public String getBillOutStoreAffiliateNo() {
		return billOutStoreAffiliateNo;
	}

	public void setBillOutStoreAffiliateNo(String billOutStoreAffiliateNo) {
		this.billOutStoreAffiliateNo = billOutStoreAffiliateNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public Date getStartRequiredSendDate() {
		return startRequiredSendDate;
	}

	public void setStartRequiredSendDate(Date startRequiredSendDate) {
		this.startRequiredSendDate = startRequiredSendDate;
	}

	public Date getEndRequiredSendDate() {
		return endRequiredSendDate;
	}

	public void setEndRequiredSendDate(Date endRequiredSendDate) {
		this.endRequiredSendDate = endRequiredSendDate;
	}

	public Date getStartDeliverTime() {
		return startDeliverTime;
	}

	public void setStartDeliverTime(Date startDeliverTime) {
		this.startDeliverTime = startDeliverTime;
	}

	public Date getEndDeliverTime() {
		return endDeliverTime;
	}

	public void setEndDeliverTime(Date endDeliverTime) {
		this.endDeliverTime = endDeliverTime;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}
}
