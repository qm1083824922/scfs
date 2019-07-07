package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BillOutStore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1163180335307798305L;

	private Integer id;
	/**
	 * 采购退货单Id
	 */
	private Integer poReturnId;
	/**
	 * 出库编号
	 */
	private String billNo;
	/**
	 * 出库类型 1-销售出库 2-调拨出库
	 */
	private Integer billType;
	/**
	 * 出库附属编号
	 */
	private String affiliateNo;
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
	 * 接收仓库ID
	 */
	private Integer receiveWarehouseId;
	/**
	 * 状态 0-待提交 1-已发货
	 */
	private Integer status;
	/**
	 * 要求发货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requiredSendDate;
	/**
	 * 发货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date sendDate;
	/**
	 * 销售单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 发货数量
	 */
	private BigDecimal sendNum;
	/**
	 * 发货金额
	 */
	private BigDecimal sendAmount;
	/**
	 * 拣货数量
	 */
	private BigDecimal pickupNum;
	/**
	 * 拣货金额
	 */
	private BigDecimal pickupAmount;
	/**
	 * 成本金额
	 */
	private BigDecimal costAmount;
	/**
	 * 订单金额
	 */
	private BigDecimal poAmount;
	/**
	 * 出库客户地址ID
	 */
	private Integer customerAddressId;
	/**
	 * 运输方式 1-自提
	 */
	private Integer transferMode;
	/**
	 * 出库人ID
	 */
	private Integer deliverId;
	/**
	 * 出库人
	 */
	private String deliverer;
	/**
	 * 单据出库时间
	 */
	private Date deliverTime;
	/**
	 * 系统出库时间
	 */
	private Date systemDeliverTime;
	/**
	 * 币种 1-人民币 2-美元 3-港元
	 */
	private Integer currencyType;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
	/**
	 * 原因
	 */
	private Integer reasonType;
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
	 * 签收标准 0-身份证 1-公章 2-身份证和公章
	 */
	private Integer signStandard;
	/**
	 * 身份证号码
	 */
	private String certificateId;
	/**
	 * 姓名
	 */
	private String certificateName;
	/**
	 * 公章名
	 */
	private String officialSeal;
	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;
	/**
	 * 回款金额
	 */
	private BigDecimal receivedAmount;
	private BigDecimal fundBackAmount;
	/**
	 * 销售单号
	 */
	private String billDeliveryNo;
	/**
	 * 退货数量
	 */
	private BigDecimal returnNum;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

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

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public BigDecimal getSendNum() {
		return sendNum;
	}

	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
	}

	public BigDecimal getPickupAmount() {
		return pickupAmount;
	}

	public void setPickupAmount(BigDecimal pickupAmount) {
		this.pickupAmount = pickupAmount;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}

	public Integer getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(Integer customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public Integer getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Integer transferMode) {
		this.transferMode = transferMode;
	}

	public Integer getDeliverId() {
		return deliverId;
	}

	public void setDeliverId(Integer deliverId) {
		this.deliverId = deliverId;
	}

	public String getDeliverer() {
		return deliverer;
	}

	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer == null ? null : deliverer.trim();
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
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

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
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

	public Integer getReceiveWarehouseId() {
		return receiveWarehouseId;
	}

	public void setReceiveWarehouseId(Integer receiveWarehouseId) {
		this.receiveWarehouseId = receiveWarehouseId;
	}

	public Integer getReasonType() {
		return reasonType;
	}

	public void setReasonType(Integer reasonType) {
		this.reasonType = reasonType;
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

	public Integer getSignStandard() {
		return signStandard;
	}

	public void setSignStandard(Integer signStandard) {
		this.signStandard = signStandard;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getOfficialSeal() {
		return officialSeal;
	}

	public void setOfficialSeal(String officialSeal) {
		this.officialSeal = officialSeal;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public BigDecimal getFundBackAmount() {
		return fundBackAmount;
	}

	public void setFundBackAmount(BigDecimal fundBackAmount) {
		this.fundBackAmount = fundBackAmount;
	}

	public String getBillDeliveryNo() {
		return billDeliveryNo;
	}

	public void setBillDeliveryNo(String billDeliveryNo) {
		this.billDeliveryNo = billDeliveryNo;
	}

	public BigDecimal getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(BigDecimal returnNum) {
		this.returnNum = returnNum;
	}

	public Integer getPoReturnId() {
		return poReturnId;
	}

	public void setPoReturnId(Integer poReturnId) {
		this.poReturnId = poReturnId;
	}

	public Date getSystemDeliverTime() {
		return systemDeliverTime;
	}

	public void setSystemDeliverTime(Date systemDeliverTime) {
		this.systemDeliverTime = systemDeliverTime;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}