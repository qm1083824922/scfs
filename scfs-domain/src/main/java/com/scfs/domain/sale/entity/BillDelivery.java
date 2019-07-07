package com.scfs.domain.sale.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

public class BillDelivery extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1938852371675463356L;

	/**
	 * 销售单ID
	 */
	private Integer id;

	/**
	 * 销售单编号
	 */
	private String billNo;

	/**
	 * 销售类型 1-销售销售 2-借货销售
	 */
	private Integer billType;

	/**
	 * 销售附属编号
	 */
	private String affiliateNo;

	/**
	 * 项目ID,关联tb_base_subject[id]
	 */
	private Integer projectId;

	/**
	 * 仓库ID,关联tb_base_subject[id]
	 */
	private Integer warehouseId;

	/**
	 * 客户ID,关联tb_base_subject[id]
	 */
	private Integer customerId;

	/**
	 * 状态 1-待提交 2-已提交
	 */
	private Integer status;

	/**
	 * 销售单日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date deliveryDate;

	/**
	 * 应发货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requiredSendDate;

	/**
	 * 应发货数量
	 */
	private BigDecimal requiredSendNum;

	/**
	 * 应发货金额
	 */
	private BigDecimal requiredSendAmount;

	/**
	 * 成本金额
	 */
	private BigDecimal costAmount;

	/**
	 * 订单金额
	 */
	private BigDecimal poAmount;

	/**
	 * 客户地址ID
	 */
	private Integer customerAddressId;

	/**
	 * 运输方式 1-自提
	 */
	private Integer transferMode;

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
	 * 回款时间
	 */
	private Date returnTime;
	/**
	 * 全部回款时间
	 */
	private Date wholeReturnTime;
	/**
	 * 提交人ID
	 */
	private Integer submitterId;
	/**
	 * 提交人
	 */
	private String submitter;
	/**
	 * 提交时间
	 */
	private Date submitTime;
	/**
	 * 是否改价 0-未改价 1-改价
	 */
	private Integer isChangePrice;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	// 接收项目
	private Integer receiveProjectId;
	// 接收项目下的仓库
	private Integer receiveWarehouseId;
	// 接收供应商
	private Integer receiveSupplierId;

	public Integer getReceiveSupplierId() {
		return receiveSupplierId;
	}

	public void setReceiveSupplierId(Integer receiveSupplierId) {
		this.receiveSupplierId = receiveSupplierId;
	}

	public Integer getReceiveProjectId() {
		return receiveProjectId;
	}

	public void setReceiveProjectId(Integer receiveProjectId) {
		this.receiveProjectId = receiveProjectId;
	}

	public Integer getReceiveWarehouseId() {
		return receiveWarehouseId;
	}

	public void setReceiveWarehouseId(Integer receiveWarehouseId) {
		this.receiveWarehouseId = receiveWarehouseId;
	}

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

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
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

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Integer getSubmitterId() {
		return submitterId;
	}

	public void setSubmitterId(Integer submitterId) {
		this.submitterId = submitterId;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getIsChangePrice() {
		return isChangePrice;
	}

	public void setIsChangePrice(Integer isChangePrice) {
		this.isChangePrice = isChangePrice;
	}

	public Date getWholeReturnTime() {
		return wholeReturnTime;
	}

	public void setWholeReturnTime(Date wholeReturnTime) {
		this.wholeReturnTime = wholeReturnTime;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}