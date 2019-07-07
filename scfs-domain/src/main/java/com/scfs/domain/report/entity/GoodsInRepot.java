package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 铺货入库明细
 * 
 * @author 
 *
 */
public class GoodsInRepot {

	/**
	 * PMS入库ID
	 */
	private Integer id;
	/** 采购单号 */
	private String purchaseSn;
	/** 入库数量 */
	private BigDecimal stockinNum;
	/** 入库价格 */
	private BigDecimal purchasePrice;
	/** 入库日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date stockinTime;
	/** 送货单号 */
	private String purchaseDeliverySn;
	/** 入库金额 **/
	private BigDecimal stockinAmount;
	/**
	 * 采购单ID
	 */
	private Integer poId;
	/**
	 * 采购单状态
	 */
	private Integer poState;
	/**
	 * 代销订单号
	 */
	private String poOrderNo;

	/** 项目名称 **/
	private String projectName;
	/** 项目Id **/
	private Integer projectId;
	/** 供应商 **/
	private String supplierName;
	/** 供应商Id **/
	private Integer supplierId;
	/** 客户名称 **/
	private String customerName;
	/** 客户Id **/
	private Integer customerId;
	/** 商品编号 **/
	private String goodsCode;
	/** 出库销售数量 **/
	private BigDecimal wmsOutStockin;
	/** 可发货数量 **/
	private BigDecimal remainSendNum;
	/** 库龄 **/
	private Integer stlAge;

	public String getPurchaseSn() {
		return purchaseSn;
	}

	public void setPurchaseSn(String purchaseSn) {
		this.purchaseSn = purchaseSn;
	}

	public BigDecimal getStockinNum() {
		return stockinNum;
	}

	public void setStockinNum(BigDecimal stockinNum) {
		this.stockinNum = stockinNum;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Date getStockinTime() {
		return stockinTime;
	}

	public void setStockinTime(Date stockinTime) {
		this.stockinTime = stockinTime;
	}

	public String getPurchaseDeliverySn() {
		return purchaseDeliverySn;
	}

	public void setPurchaseDeliverySn(String purchaseDeliverySn) {
		this.purchaseDeliverySn = purchaseDeliverySn;
	}

	public BigDecimal getStockinAmount() {
		return stockinAmount;
	}

	public void setStockinAmount(BigDecimal stockinAmount) {
		this.stockinAmount = stockinAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoState() {
		return poState;
	}

	public void setPoState(Integer poState) {
		this.poState = poState;
	}

	public String getPoOrderNo() {
		return poOrderNo;
	}

	public void setPoOrderNo(String poOrderNo) {
		this.poOrderNo = poOrderNo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public BigDecimal getWmsOutStockin() {
		return wmsOutStockin;
	}

	public void setWmsOutStockin(BigDecimal wmsOutStockin) {
		this.wmsOutStockin = wmsOutStockin;
	}

	public BigDecimal getRemainSendNum() {
		return remainSendNum;
	}

	public void setRemainSendNum(BigDecimal remainSendNum) {
		this.remainSendNum = remainSendNum;
	}

	public Integer getStlAge() {
		return stlAge;
	}

	public void setStlAge(Integer stlAge) {
		this.stlAge = stlAge;
	}

}
