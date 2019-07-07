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
 * Created by Administrator on 2016年10月17日.
 */
public class BillInStoreResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1669414709979371319L;

	private Integer id;

	/**
	 * 入库编号
	 */
	private String billNo;
	/**
	 * 入库类型, 即收货类型
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
	 * 状态 0-待提交 1-已收货
	 */
	private Integer status;
	/**
	 * 到货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date receiveDate;
	/**
	 * 到货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal receiveNum;
	/**
	 * 收货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal receiveAmount;
	/**
	 * 理货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal tallyNum;
	/**
	 * 理货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

	/**********************************************/
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
	/**
	 * 状态名称
	 */
	private String statusName;

	/**
	 * 入库类型, 即收货类型
	 */
	private String billTypeName;
	/**
	 * 币种 1.人民币 2.美元 3.港币
	 */
	private String currencyTypeName;
	/**
	 * 收货金额(字符串)
	 */
	private String receiveAmountStr;
	/**
	 * 理货金额(字符串)
	 */
	private String tallyAmountStr;
	/**
	 * 经营单位
	 */
	private String businessUnitName;
	/**
	 * 经营单位地址
	 */
	private String businessUnitAddress;
	/**
	 * 经营单位电话
	 */
	private String businessUnitRegPhone;
	/**
	 * 系统时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;
	/**
	 * 供应商名称（全称）
	 */
	private String fullSupplierName;
	/**
	 * 仓库地址
	 */
	private String warehouseAddressName;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	/**
	 * 理货明细
	 */
	private List<BillInStoreTallyDtlResDto> billInStoreTallyDtlResDtoList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_BILL_IN_STORE);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_BILL_IN_STORE);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_BILL_IN_STORE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_BILL_IN_STORE);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_BILL_IN_STORE);
			operMap.put(OperateConsts.REJECT, BusUrlConsts.REJECT_BILL_IN_STORE);
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
		this.acceptor = acceptor;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public List<BillInStoreTallyDtlResDto> getBillInStoreTallyDtlResDtoList() {
		return billInStoreTallyDtlResDtoList;
	}

	public void setBillInStoreTallyDtlResDtoList(List<BillInStoreTallyDtlResDto> billInStoreTallyDtlResDtoList) {
		this.billInStoreTallyDtlResDtoList = billInStoreTallyDtlResDtoList;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getReceiveAmountStr() {
		return receiveAmountStr;
	}

	public void setReceiveAmountStr(String receiveAmountStr) {
		this.receiveAmountStr = receiveAmountStr;
	}

	public String getTallyAmountStr() {
		return tallyAmountStr;
	}

	public void setTallyAmountStr(String tallyAmountStr) {
		this.tallyAmountStr = tallyAmountStr;
	}

	public Date getSystemTime() {
		return new Date();
	}

	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
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

	public String getBusinessUnitRegPhone() {
		return businessUnitRegPhone;
	}

	public void setBusinessUnitRegPhone(String businessUnitRegPhone) {
		this.businessUnitRegPhone = businessUnitRegPhone;
	}

	public String getFullSupplierName() {
		return fullSupplierName;
	}

	public void setFullSupplierName(String fullSupplierName) {
		this.fullSupplierName = fullSupplierName;
	}

	public String getWarehouseAddressName() {
		return warehouseAddressName;
	}

	public void setWarehouseAddressName(String warehouseAddressName) {
		this.warehouseAddressName = warehouseAddressName;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
