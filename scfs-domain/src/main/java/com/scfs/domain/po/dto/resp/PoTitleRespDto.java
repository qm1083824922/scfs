package com.scfs.domain.po.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/15.
 */
@JsonSerialize
public class PoTitleRespDto {

	/** 采购单ID */
	private Integer id;
	/** 系统编号 */
	private String sysNo;
	/** 附属编号 */
	private String appendNo;
	/** 订单编号 */
	private String orderNo;
	/** 经营单位 */
	private Integer businessUnitId;
	private String businessUnitName;
	/** 经营单位英文名字 **/
	private String businessEnglishName;
	/** 经营单位中文名字 **/
	private String businessChineseName;
	/** 项目 */
	private Integer projectId;
	private String projectName;
	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;
	/** 供应商 */
	private Integer supplierId;
	private String supplierName;
	/** 供应商地址 **/
	private String supplierAddress;
	/** 供应商中文名字 **/
	private String supplierChineseName;
	/** 供应商英文名字 **/
	private String supplierEnglishName;
	/** 仓库 */
	private Integer warehouseId;
	private String warehouseName;

	/** 仓库地址 */
	private Integer wareAddrId;
	private String wareAddrName;
	/** 客户银行流水 */
	private String cBankWater;
	/** 客户 */
	private Integer customerId;
	private String customerName;
	/** 订单状态 */
	private Integer stateId;
	private String stateName;
	/** 预计到货日期 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date perdictTime;
	/** 订单日期 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date orderTime;
	/** 要求付款时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requestPayTime;
	/** 打印时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date systemTime;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createAt;
	/** 创建人 */
	private String createUser;
	/**
	 * 是否要求付款
	 */
	private Integer isRequestPay;
	private String isRequestPayName;
	/** 到货数量 */
	private BigDecimal arrivalNum;
	/** 到货金额 */
	private BigDecimal arrivalAmount;
	/** 付款金额 */
	private BigDecimal payAmount;
	/** 收票数量 */
	private BigDecimal invoiceTotalNum;
	/** 收票金额 */
	private BigDecimal invoiceTotalAmount;
	/** 订单总数量 */
	private BigDecimal orderTotalNum;
	/** 订单总金额 */
	private BigDecimal orderTotalAmount;
	/** 折扣金额 */
	private BigDecimal titleDiscountAmount;
	/** 账户编号 */
	private Integer accountId;
	private String accountNo;
	private String accountNoPay;
	/** 付款方式 */
	private Integer payWayId;
	private String payWayName;
	/** 银行代码 */
	private String bankCode;
	/** 开户银行 */
	private String bankName;
	private String bankNamePay;
	/** 备注 */
	private String remark;
	/** 开立类型 */
	private Integer openType;
	/** 币种 */
	private Integer currencyId;
	private String currencyName;
	private String currency;
	/** 单位 **/
	private String unit;
	// 预付金
	private BigDecimal perRecAmount;
	/** 运输方式 1-自提 */
	private String transferModeName;
	/** 运输方式 1-自提 */
	private Integer transferMode;
	/** 签收标准 0-身份证 1-公章 2-身份证和公章 */
	private Integer signStandard;
	private String signStandardName;

	/** 身份证号码 */
	private String certificateId;
	/** 姓名 */
	private String certificateName;
	/** 公章名 */
	private String officialSeal;
	/** 身份证号码 */
	private Integer supplierAddressId;
	private String supplierAddressName;
	/** 操作集合 */
	private List<CodeValue> opertaList;
	/** 开户人 **/
	private String accountName;
	/** 抵扣金额 **/
	private BigDecimal ductionMoney;
	private String currencyTypeName;
	/** 电话 **/
	private String phone;
	/** 资金占用服务费 **/
	private BigDecimal totalOccupyServiceAmount;
	/** 总退款金额 **/
	private BigDecimal totalRefundAmount;

	/** 部门 **/
	private Integer departmentId;
	private String departmentName;
	/** PO单未到货金额=订单金额-折扣金额-抵扣金额-到货金额 **/
	private BigDecimal poAmount;
	/**
	 * CNY汇率
	 */
	private BigDecimal cnyRate;

	/** 抵扣金额 **/
	private BigDecimal deductionAmount;
	/**
	 * 铺货订单可发货数量
	 */
	private BigDecimal totalRemainSendNum;

	/**
	 * 临时字段 供应商 名称
	 */
	private String supplierNameStr;
	/**
	 * 临时字段 经营单位注册号码
	 */
	private String regNo;
	/**
	 * 有效期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startDate;
	/**
	 * 单据类型
	 */
	private Integer bizType;
	/**
	 * 飞单标识 0-否 1-是
	 */
	private Integer flyOrderFlag;
	private String flyOrderFlagName;
	

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getSupplierNameStr() {
		return supplierNameStr;
	}

	public void setSupplierNameStr(String supplierNameStr) {
		this.supplierNameStr = supplierNameStr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getDuctionMoney() {
		return ductionMoney;
	}

	public void setDuctionMoney(BigDecimal ductionMoney) {
		this.ductionMoney = ductionMoney;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_PO_TITLE);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PO_TITLE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_PO_TITLE);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_PO_TITLE);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_PO_TITLE);
			operMap.put(OperateConsts.RECEIVE, BusUrlConsts.RECEIVE_PO_TITLE);
			operMap.put(OperateConsts.FLY_ORDER, BusUrlConsts.FLY_ORDER_PO);
		}

	}

	public static class OperateReturn {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_PO_RETURN_TITLE);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PO_RETURN_TITLE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_PO_RETURN_TITLE);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_PO_RETURN_TITLE);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_PO_RETURN_TITLE);
		}

	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public BigDecimal getPerRecAmount() {
		return perRecAmount;
	}

	public void setPerRecAmount(BigDecimal perRecAmount) {
		this.perRecAmount = perRecAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusinessUnitNameValue() {
		return businessUnitNameValue;
	}

	public void setBusinessUnitNameValue(String businessUnitNameValue) {
		this.businessUnitNameValue = businessUnitNameValue;
	}

	public String getBusinessUnitAddress() {
		return businessUnitAddress;
	}

	public void setBusinessUnitAddress(String businessUnitAddress) {
		this.businessUnitAddress = businessUnitAddress;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
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

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getWareAddrId() {
		return wareAddrId;
	}

	public void setWareAddrId(Integer wareAddrId) {
		this.wareAddrId = wareAddrId;
	}

	public String getWareAddrName() {
		return wareAddrName;
	}

	public void setWareAddrName(String wareAddrName) {
		this.wareAddrName = wareAddrName;
	}

	public String getcBankWater() {
		return cBankWater;
	}

	public void setcBankWater(String cBankWater) {
		this.cBankWater = cBankWater;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Date getPerdictTime() {
		return perdictTime;
	}

	public void setPerdictTime(Date perdictTime) {
		this.perdictTime = perdictTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getRequestPayTime() {
		return requestPayTime;
	}

	public void setRequestPayTime(Date requestPayTime) {
		this.requestPayTime = requestPayTime;
	}

	public Integer getIsRequestPay() {
		return isRequestPay;
	}

	public void setIsRequestPay(Integer isRequestPay) {
		this.isRequestPay = isRequestPay;
	}

	public String getIsRequestPayName() {
		return isRequestPayName;
	}

	public void setIsRequestPayName(String isRequestPayName) {
		this.isRequestPayName = isRequestPayName;
	}

	public BigDecimal getArrivalNum() {
		return arrivalNum;
	}

	public void setArrivalNum(BigDecimal arrivalNum) {
		this.arrivalNum = arrivalNum;
	}

	public BigDecimal getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(BigDecimal arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getInvoiceTotalNum() {
		return invoiceTotalNum;
	}

	public void setInvoiceTotalNum(BigDecimal invoiceTotalNum) {
		this.invoiceTotalNum = invoiceTotalNum;
	}

	public BigDecimal getInvoiceTotalAmount() {
		return invoiceTotalAmount;
	}

	public void setInvoiceTotalAmount(BigDecimal invoiceTotalAmount) {
		this.invoiceTotalAmount = invoiceTotalAmount;
	}

	public BigDecimal getOrderTotalNum() {
		return orderTotalNum;
	}

	public void setOrderTotalNum(BigDecimal orderTotalNum) {
		this.orderTotalNum = orderTotalNum;
	}

	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getPayWayId() {
		return payWayId;
	}

	public void setPayWayId(Integer payWayId) {
		this.payWayId = payWayId;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public BigDecimal getTitleDiscountAmount() {
		return titleDiscountAmount;
	}

	public void setTitleDiscountAmount(BigDecimal titleDiscountAmount) {
		this.titleDiscountAmount = titleDiscountAmount;
	}

	public String getTransferModeName() {
		return transferModeName;
	}

	public void setTransferModeName(String transferModeName) {
		this.transferModeName = transferModeName;
	}

	public Integer getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Integer transferMode) {
		this.transferMode = transferMode;
	}

	public Integer getSignStandard() {
		return signStandard;
	}

	public void setSignStandard(Integer signStandard) {
		this.signStandard = signStandard;
	}

	public String getSignStandardName() {
		return signStandardName;
	}

	public void setSignStandardName(String signStandardName) {
		this.signStandardName = signStandardName;
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

	public Integer getSupplierAddressId() {
		return supplierAddressId;
	}

	public void setSupplierAddressId(Integer supplierAddressId) {
		this.supplierAddressId = supplierAddressId;
	}

	public String getSupplierAddressName() {
		return supplierAddressName;
	}

	public void setSupplierAddressName(String supplierAddressName) {
		this.supplierAddressName = supplierAddressName;
	}

	public String getSupplierChineseName() {
		return supplierChineseName;
	}

	public void setSupplierChineseName(String supplierChineseName) {
		this.supplierChineseName = supplierChineseName;
	}

	public String getSupplierEnglishName() {
		return supplierEnglishName;
	}

	public void setSupplierEnglishName(String supplierEnglishName) {
		this.supplierEnglishName = supplierEnglishName;
	}

	public String getBusinessEnglishName() {
		return businessEnglishName;
	}

	public void setBusinessEnglishName(String businessEnglishName) {
		this.businessEnglishName = businessEnglishName;
	}

	public String getBusinessChineseName() {
		return businessChineseName;
	}

	public void setBusinessChineseName(String businessChineseName) {
		this.businessChineseName = businessChineseName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAccountNoPay() {
		return accountNoPay;
	}

	public void setAccountNoPay(String accountNoPay) {
		this.accountNoPay = accountNoPay;
	}

	public String getBankNamePay() {
		return bankNamePay;
	}

	public void setBankNamePay(String bankNamePay) {
		this.bankNamePay = bankNamePay;
	}

	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public BigDecimal getTotalOccupyServiceAmount() {
		return totalOccupyServiceAmount;
	}

	public void setTotalOccupyServiceAmount(BigDecimal totalOccupyServiceAmount) {
		this.totalOccupyServiceAmount = totalOccupyServiceAmount;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}

	public BigDecimal getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(BigDecimal cnyRate) {
		this.cnyRate = cnyRate;
	}

	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public BigDecimal getTotalRemainSendNum() {
		return totalRemainSendNum;
	}

	public void setTotalRemainSendNum(BigDecimal totalRemainSendNum) {
		this.totalRemainSendNum = totalRemainSendNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getFlyOrderFlagName() {
		return flyOrderFlagName;
	}

	public void setFlyOrderFlagName(String flyOrderFlagName) {
		this.flyOrderFlagName = flyOrderFlagName;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
