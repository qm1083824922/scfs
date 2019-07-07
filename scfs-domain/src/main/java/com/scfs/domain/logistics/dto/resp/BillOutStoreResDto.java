package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillOutStoreResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6012543752249868153L;
	private Integer id;
	/**
	 * 出库编号
	 */
	private String billNo;
	/**
	 * 出库类型 1-销售出库 2-借货出库
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requiredSendDate;
	/**
	 * 发货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date sendDate;
	/**
	 * 销售单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 发货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal sendNum;
	/**
	 * 发货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal sendAmount;
	/**
	 * 拣货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal pickupNum;
	/**
	 * 拣货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal pickupAmount;
	/**
	 * 成本金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal costAmount;
	/**
	 * 订单金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
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
	 * 出库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliverTime;
	/**
	 * 币种 1-人民币 2-美元
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

	/**
	 * 销售单号
	 */
	private String billDeliveryNo;
	/**
	 * 退货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal returnNum;
	/**
	 * 采购退货单Id
	 */
	private Integer poReturnId;
	/**
	 * 采购退货单号
	 */
	private String poReturnNo;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	/*******************************************************/

	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 客户电话
	 */
	private String customerPhone;
	/**
	 * 收货地址
	 */
	private String customerAddress;
	/**
	 * 状态名称
	 */
	private String statusName;
	/**
	 * 出库类型 1-销售出库 2-借货出库
	 */
	private String billTypeName;
	/**
	 * 运输方式 1-自提
	 */
	private String transferModeName;
	/**
	 * 接收仓库名称
	 */
	private String receiveWarehouseName;
	/**
	 * 原因名称
	 */
	private String reasonName;
	/**
	 * 币种 1.人民币 2.美元 3.港币
	 */
	private String currencyTypeName;
	/**
	 * 发货金额(字符串)
	 */
	private String sendAmountStr;
	/**
	 * 拣货金额(字符串)
	 */
	private String pickupAmountStr;
	/**
	 * 经营单位
	 */
	private String businessUnitName;
	/**
	 * 经营单位地址
	 */
	private String businessUnitAddress;
	/**
	 * 经营单位中文名称
	 */
	private String businessChineseName;
	/**
	 * 经营单位ID
	 */
	private Integer businessId;
	/**
	 * 经营单位电话
	 */
	private String businessUnitPhone;
	/**
	 * 目的城市
	 */
	private String cityName;
	/**
	 * 联系人
	 */
	private String contactPerson;
	/**
	 * 联系人手机
	 */
	private String mobilePhone;
	/**
	 * 联系人电话
	 */
	private String telephone;
	/**
	 * 发货城市
	 */
	private String sendCityName;
	/**
	 * 发货仓库地址
	 */
	private String sendWarehouseAddressName;
	/**
	 * 签收标准名称
	 */
	private String signStandardName;
	/**
	 * 系统时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_BILL_OUT_STORE);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_BILL_OUT_STORE);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_BILL_OUT_STORE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_BILL_OUT_STORE);
			operMap.put(OperateConsts.SEND, BusUrlConsts.SEND_BILL_OUT_STORE);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_BILL_OUT_STORE);
			operMap.put(OperateConsts.PRINT_PICK, BusUrlConsts.PRINT_BILL_OUT_STORE_PICK);
		}

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
		this.billNo = billNo;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
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

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
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
		this.deliverer = deliverer;
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
		this.remark = remark;
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
		this.creator = creator;
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
		this.deleter = deleter;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public Integer getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Integer transferMode) {
		this.transferMode = transferMode;
	}

	public String getTransferModeName() {
		return transferModeName;
	}

	public void setTransferModeName(String transferModeName) {
		this.transferModeName = transferModeName;
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

	public String getReceiveWarehouseName() {
		return receiveWarehouseName;
	}

	public void setReceiveWarehouseName(String receiveWarehouseName) {
		this.receiveWarehouseName = receiveWarehouseName;
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
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

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getSendAmountStr() {
		return sendAmountStr;
	}

	public void setSendAmountStr(String sendAmountStr) {
		this.sendAmountStr = sendAmountStr;
	}

	public String getPickupAmountStr() {
		return pickupAmountStr;
	}

	public void setPickupAmountStr(String pickupAmountStr) {
		this.pickupAmountStr = pickupAmountStr;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getBusinessUnitAddress() {
		return businessUnitAddress;
	}

	public void setBusinessUnitAddress(String businessUnitAddress) {
		this.businessUnitAddress = businessUnitAddress;
	}

	public Date getSystemTime() {
		return new Date();
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}

	public String getBusinessUnitPhone() {
		return businessUnitPhone;
	}

	public void setBusinessUnitPhone(String businessUnitPhone) {
		this.businessUnitPhone = businessUnitPhone;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSendWarehouseAddressName() {
		return sendWarehouseAddressName;
	}

	public void setSendWarehouseAddressName(String sendWarehouseAddressName) {
		this.sendWarehouseAddressName = sendWarehouseAddressName;
	}

	public String getSendCityName() {
		return sendCityName;
	}

	public void setSendCityName(String sendCityName) {
		this.sendCityName = sendCityName;
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

	public String getSignStandardName() {
		return signStandardName;
	}

	public void setSignStandardName(String signStandardName) {
		this.signStandardName = signStandardName;
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

	public String getPoReturnNo() {
		return poReturnNo;
	}

	public void setPoReturnNo(String poReturnNo) {
		this.poReturnNo = poReturnNo;
	}

	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getBusinessChineseName() {
		return businessChineseName;
	}

	public void setBusinessChineseName(String businessChineseName) {
		this.businessChineseName = businessChineseName;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
