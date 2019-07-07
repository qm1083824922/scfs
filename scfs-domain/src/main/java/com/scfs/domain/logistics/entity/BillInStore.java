package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class BillInStore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8596143304817343888L;

	private Integer id;

	/**
	 * 入库编号
	 */
	private String billNo;
	/**
	 * 入库类型, 1-采购入库 2-调拨入库
	 */
	private Integer billType;
	/**
	 * 入库附属编号
	 */
	private String affiliateNo;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 供应商ID
	 */
	private Integer supplierId;
	/**
	 * 仓库ID
	 */
	private Integer warehouseId;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 状态 1-待提交 2-已收货
	 */
	private Integer status;
	/**
	 * 到货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receiveDate;
	/**
	 * 到货数量
	 */
	private BigDecimal receiveNum;
	/**
	 * 收货金额
	 */
	private BigDecimal receiveAmount;
	/**
	 * 理货数量
	 */
	private BigDecimal tallyNum;
	/**
	 * 理货金额
	 */
	private BigDecimal tallyAmount;
	/**
	 * 入库人ID
	 */
	private Integer acceptorId;
	/**
	 * 入库人
	 */
	private String acceptor;
	/**
	 * 入库时间
	 */
	private Date acceptTime;
	/**
	 * 关联出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 币种 1-人民币 2-美元
	 */
	private Integer currencyType;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
	/**
	 * 调用WMS接口 0-未调用 1-已调用
	 */
	private Integer wmsStatus;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 打印次数
	 */
	private Integer printNum;
	/**
	 * 创建人ID
	 */
	private Integer creatorId;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private Date createAt;
	/**
	 * 更新时间
	 */
	private Date updateAt;
	/**
	 * 作废人ID
	 */
	private Integer deleterId;
	/**
	 * 作废人
	 */
	private String deleter;
	/**
	 * 删除标记 0 : 有效 1 : 删除
	 */
	private Integer isDelete;
	/**
	 * 删除时间
	 */
	private Date deleteAt;
	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;
	/**
	 * 关联销售退货单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	/*************** 扩展字段 *******************/
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;

	private List<BillInStoreDtl> billInStoreDtlList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo == null ? null : affiliateNo.trim();
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public BigDecimal getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(BigDecimal receiveNum) {
		this.receiveNum = receiveNum;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
	}

	public BigDecimal getTallyAmount() {
		return tallyAmount;
	}

	public void setTallyAmount(BigDecimal tallyAmount) {
		this.tallyAmount = tallyAmount;
	}

	public Integer getAcceptorId() {
		return acceptorId;
	}

	public void setAcceptorId(Integer acceptorId) {
		this.acceptorId = acceptorId;
	}

	public String getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor == null ? null : acceptor.trim();
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
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

	public Integer getDeleterId() {
		return deleterId;
	}

	public void setDeleterId(Integer deleterId) {
		this.deleterId = deleterId;
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter == null ? null : deleter.trim();
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
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

	public List<BillInStoreDtl> getBillInStoreDtlList() {
		return billInStoreDtlList;
	}

	public void setBillInStoreDtlList(List<BillInStoreDtl> billInStoreDtlList) {
		this.billInStoreDtlList = billInStoreDtlList;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getWmsStatus() {
		return wmsStatus;
	}

	public void setWmsStatus(Integer wmsStatus) {
		this.wmsStatus = wmsStatus;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}