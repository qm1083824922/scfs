package com.scfs.domain.sale.dto.resp;

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
import com.scfs.domain.sale.entity.BillDelivery;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016年10月27日.
 */
public class BillDeliveryResDto extends BillDelivery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9088345296900113976L;
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date deliveryDate;

	/**
	 * 应发货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requiredSendDate;

	/**
	 * 应发货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal requiredSendNum;

	/**
	 * 应发货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal requiredSendAmount;

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
	 * 回款时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date returnTime;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date submitTime;
	/**
	 * 是否改价 0-未改价 1-改价
	 */
	private Integer isChangePrice;

	/******************************************************/
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	/** 仓库地址 **/
	private String warehouseAddress;
	/**
	 * 客户名称
	 */
	private String customerName;
	private String customerChineseName;
	private String customerEnglishName;
	/**
	 * 客户地址
	 */
	private String customerAddress;
	/**
	 * 客户电话
	 */
	private String customerRegPhone;
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
	 * 币种 1.人民币 2.美元 3.港币
	 */
	private String currencyTypeName;
	/**
	 * 币种单位
	 */
	private String currencyTypeEnName;
	/**
	 * 应发货金额(字符串)
	 */
	private String requiredSendAmountStr;
	/**
	 * 应发货数量(字符串)
	 */
	private String requiredSendNumStr;
	/**
	 * 经营单位
	 */
	private String businessUnitName;
	private String businessUnitEnglishName;
	/**
	 * 经营单位地址
	 */
	private String businessUnitAddress;
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
	/**
	 * 预收金额
	 */
	private BigDecimal preRecAmount;
	/**
	 * 服务费金额
	 */
	private BigDecimal serviceAmount;
	/**
	 * 对应条款结算类型
	 */
	Integer settleType;

	/** 资金使用帐期 */
	private Integer fundAccountPeriod;

	// 接收项目
	private Integer receiveProjectId;
	private String receiveProjectName;
	// 接收项目下的长裤
	private Integer receiveWarehouseId;
	private String receiveWarehouseName;

	// 接收项目下的长裤
	private Integer receiveSupplierId;
	private String receiveSupplierName;

	/** 商品单位 **/
	private String goodUnit;

	/** 开户银行 **/
	private String bankName;
	/** 开户账号 **/
	private String accountNo;
	/** 电话 **/
	private String phoneNumber;
	/** 银行地址 **/
	private String bankAddress;
	/** 默认币种 **/
	private String defaultCurrency;
	/** 开户姓名 **/
	private String accountor;
	/** 银行开户人 **/
	private String subjectName;
	/** 银行代码 **/
	private String bankCode;
	/** 交货方式 项目客户结算方式 1 赊销 2 款到放货 **/
	private String settleName;
	/**
	 * 客户地址
	 */
	private String customerAfficeName;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_BILL_DELIVERY);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_BILL_DELIVERY);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_BILL_DELIVERY);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_BILL_DELIVERY);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_BILL_DELIVERY);
		}
	}

	@Override
	public Integer getReceiveSupplierId() {
		return receiveSupplierId;
	}

	@Override
	public void setReceiveSupplierId(Integer receiveSupplierId) {
		this.receiveSupplierId = receiveSupplierId;
	}

	public String getReceiveSupplierName() {
		return receiveSupplierName;
	}

	public void setReceiveSupplierName(String receiveSupplierName) {
		this.receiveSupplierName = receiveSupplierName;
	}

	@Override
	public Integer getReceiveProjectId() {
		return receiveProjectId;
	}

	@Override
	public void setReceiveProjectId(Integer receiveProjectId) {
		this.receiveProjectId = receiveProjectId;
	}

	public String getReceiveProjectName() {
		return receiveProjectName;
	}

	public void setReceiveProjectName(String receiveProjectName) {
		this.receiveProjectName = receiveProjectName;
	}

	@Override
	public Integer getReceiveWarehouseId() {
		return receiveWarehouseId;
	}

	@Override
	public void setReceiveWarehouseId(Integer receiveWarehouseId) {
		this.receiveWarehouseId = receiveWarehouseId;
	}

	public String getReceiveWarehouseName() {
		return receiveWarehouseName;
	}

	public void setReceiveWarehouseName(String receiveWarehouseName) {
		this.receiveWarehouseName = receiveWarehouseName;
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public String getTransferModeName() {
		return transferModeName;
	}

	public void setTransferModeName(String transferModeName) {
		this.transferModeName = transferModeName;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getRequiredSendAmountStr() {
		return requiredSendAmountStr;
	}

	public void setRequiredSendAmountStr(String requiredSendAmountStr) {
		this.requiredSendAmountStr = requiredSendAmountStr;
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

	public String getSignStandardName() {
		return signStandardName;
	}

	public void setSignStandardName(String signStandardName) {
		this.signStandardName = signStandardName;
	}

	public String getBusinessUnitPhone() {
		return businessUnitPhone;
	}

	public void setBusinessUnitPhone(String businessUnitPhone) {
		this.businessUnitPhone = businessUnitPhone;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public String getSendCityName() {
		return sendCityName;
	}

	public void setSendCityName(String sendCityName) {
		this.sendCityName = sendCityName;
	}

	public String getSendWarehouseAddressName() {
		return sendWarehouseAddressName;
	}

	public void setSendWarehouseAddressName(String sendWarehouseAddressName) {
		this.sendWarehouseAddressName = sendWarehouseAddressName;
	}

	public BigDecimal getPreRecAmount() {
		return preRecAmount;
	}

	public void setPreRecAmount(BigDecimal preRecAmount) {
		this.preRecAmount = preRecAmount;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
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

	public Integer getSettleType() {
		return settleType;
	}

	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}

	public Integer getIsChangePrice() {
		return isChangePrice;
	}

	public void setIsChangePrice(Integer isChangePrice) {
		this.isChangePrice = isChangePrice;
	}

	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	public String getCustomerChineseName() {
		return customerChineseName;
	}

	public void setCustomerChineseName(String customerChineseName) {
		this.customerChineseName = customerChineseName;
	}

	public Integer getFundAccountPeriod() {
		return fundAccountPeriod;
	}

	public void setFundAccountPeriod(Integer fundAccountPeriod) {
		this.fundAccountPeriod = fundAccountPeriod;
	}

	public String getRequiredSendNumStr() {
		return requiredSendNumStr;
	}

	public void setRequiredSendNumStr(String requiredSendNumStr) {
		this.requiredSendNumStr = requiredSendNumStr;
	}

	public String getBusinessUnitEnglishName() {
		return businessUnitEnglishName;
	}

	public void setBusinessUnitEnglishName(String businessUnitEnglishName) {
		this.businessUnitEnglishName = businessUnitEnglishName;
	}

	public String getCurrencyTypeEnName() {
		return currencyTypeEnName;
	}

	public void setCurrencyTypeEnName(String currencyTypeEnName) {
		this.currencyTypeEnName = currencyTypeEnName;
	}

	public String getGoodUnit() {
		return goodUnit;
	}

	public void setGoodUnit(String goodUnit) {
		this.goodUnit = goodUnit;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public String getAccountor() {
		return accountor;
	}

	public void setAccountor(String accountor) {
		this.accountor = accountor;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getSettleName() {
		return settleName;
	}

	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}

	public String getCustomerAfficeName() {
		return customerAfficeName;
	}

	public void setCustomerAfficeName(String customerAfficeName) {
		this.customerAfficeName = customerAfficeName;
	}

	public String getCustomerEnglishName() {
		return customerEnglishName;
	}

	public void setCustomerEnglishName(String customerEnglishName) {
		this.customerEnglishName = customerEnglishName;
	}

	public String getCustomerRegPhone() {
		return customerRegPhone;
	}

	public void setCustomerRegPhone(String customerRegPhone) {
		this.customerRegPhone = customerRegPhone;
	}

}
