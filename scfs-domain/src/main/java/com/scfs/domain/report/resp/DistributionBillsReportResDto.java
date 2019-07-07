package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;
import com.google.common.collect.Maps;

/**
 * <pre>
 * 
 *  File: DistributionBillsReportResDto.java
 *  Description: 铺货对账单
 *  TODO
 *  Date,					Who,				
 *  2017年09月12日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class DistributionBillsReportResDto extends BaseEntity {
	/** PMS同步请款单 **/
	private Integer payId;
	/** 采购单 **/
	private Integer poId;
	/** 采购单明细 **/
	private Integer lineId;
	/** 商品信息 **/
	private Integer goodsId;
	/** 销售单 **/
	private Integer deliveryId;
	/** 销售单明细 **/
	private Integer deliveryDtlId;
	/** 项目 **/
	private Integer projectId;
	private String projectName;
	/** 经营单位 **/
	private Integer businessUnitId;
	private String businessUnitName;
	/** 供应商 **/
	private Integer supplierId;
	private String supplierName;
	private String supplierChineseName;
	/** 订单编号 **/
	private String orderNo;
	/** 订单附属编号 **/
	private String appendNo;
	/** 代销订单号 **/
	private String consignmentOrderNo;
	/** 商品编号 **/
	private String number;
	/** 单价 **/
	private BigDecimal goodsPrice;
	/** 币种 **/
	private Integer currencyId;
	private String currencyName;
	/** 批次 **/
	private String batchNum;
	/** 销售数量 **/
	private BigDecimal requiredSendNum;
	/** 销售金额 **/
	private BigDecimal requiredSendAmount;
	/** 预付比例 **/
	private BigDecimal pledge;
	/** 预付日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date payTime;
	/** 预付金额 **/
	private BigDecimal budgetAmount;
	/** 结算日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date payCreateTime;
	/** 占用天数 **/
	private Integer occupyDay;
	/** 服务费 **/
	private BigDecimal discountAmount;
	/** 已付尾款 **/
	private BigDecimal payAmount;
	/** 应付尾款 **/
	private BigDecimal retainageAmount;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_DISTRIBUTION_BILLS);
		}
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Integer getDeliveryDtlId() {
		return deliveryDtlId;
	}

	public void setDeliveryDtlId(Integer deliveryDtlId) {
		this.deliveryDtlId = deliveryDtlId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierChineseName() {
		return supplierChineseName;
	}

	public void setSupplierChineseName(String supplierChineseName) {
		this.supplierChineseName = supplierChineseName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public String getConsignmentOrderNo() {
		return consignmentOrderNo;
	}

	public void setConsignmentOrderNo(String consignmentOrderNo) {
		this.consignmentOrderNo = consignmentOrderNo;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public BigDecimal getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(BigDecimal requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public BigDecimal getRequiredSendAmount() {
		return requiredSendAmount;
	}

	public void setRequiredSendAmount(BigDecimal requiredSendAmount) {
		this.requiredSendAmount = requiredSendAmount;
	}

	public BigDecimal getPledge() {
		return pledge;
	}

	public void setPledge(BigDecimal pledge) {
		this.pledge = pledge;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getBudgetAmount() {
		return budgetAmount;
	}

	public void setBudgetAmount(BigDecimal budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

	public Date getPayCreateTime() {
		return payCreateTime;
	}

	public void setPayCreateTime(Date payCreateTime) {
		this.payCreateTime = payCreateTime;
	}

	public Integer getOccupyDay() {
		return occupyDay;
	}

	public void setOccupyDay(Integer occupyDay) {
		this.occupyDay = occupyDay;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getRetainageAmount() {
		return retainageAmount;
	}

	public void setRetainageAmount(BigDecimal retainageAmount) {
		this.retainageAmount = retainageAmount;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
