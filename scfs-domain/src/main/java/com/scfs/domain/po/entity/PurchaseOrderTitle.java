package com.scfs.domain.po.entity;

import com.scfs.domain.base.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/14. 订单头信息
 */
public class PurchaseOrderTitle extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 系统编号 */
	private String sysNo;
	/** 附属编号 */
	@NotNull(message = "appendNo:附属编号不能为空")
	private String appendNo;
	/** 订单编号 */
	private String orderNo;
	/** 经营单位ID */
	private Integer businessUnitId;
	/** 项目ID */
	@NotNull(message = "projectId:项目不能为空")
	private Integer projectId;
	/** 供应商ID */
	@NotNull(message = "supplierId:供应商不能为空")
	private Integer supplierId;
	/** 仓库ID */
	@NotNull(message = "warehouseId:仓库不能为空")
	private Integer warehouseId;
	/** 仓库地址ID */
	@NotNull(message = "wareAddrId:仓库地址不能为空")
	private Integer wareAddrId;
	/** 客户ID */
	private Integer customerId;
	/** 付款账号ID */
	private Integer accountId;
	/** 付款方式 */
	private Integer payWay;
	/** 订单类型 */
	private Integer orderType;
	/** 订单类型 */
	private Integer printNum;
	/** 订单日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "orderTime:订单日期不能为空")
	private Date orderTime;
	/** 预计到货日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date perdictTime;
	/** 要求付款时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requestPayTime;
	/** 客户银行流水 */
	private String cBankWater;
	/** 是否要求付款 1表示是，2表示否 */
	private Integer isRequestPay;
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
	private BigDecimal totalDiscountAmount;

	/** 币种 */
	@NotNull(message = "currencyId:币种不能为空")
	private Integer currencyId;
	/** 订单总数量 */
	private BigDecimal orderTotalNum;
	/** 订单总金额 */
	private BigDecimal orderTotalAmount;
	/** 状态 */
	private Integer state;
	/** 备注 */
	private String remark;
	/** 开立类型 */
	private Integer openType;
	// 预付金
	private BigDecimal perRecAmount;
	/** 运输方式 1-自提 */
	private Integer transferMode;
	/** 签收标准 0-身份证 1-公章 2-身份证和公章 */
	private Integer signStandard;
	/** 身份证号码 */
	private String certificateId;
	/** 姓名 */
	private String certificateName;
	/** 公章名 */
	private String officialSeal;
	/** 身份证号码 */
	private Integer supplierAddressId;
	/** 收货数量 */
	private BigDecimal takeDeliveryNum;
	/***************** 扩展 ******************/
	/** 铺货未请款数量 */
	private BigDecimal unDistributeNum;
	/** 铺货未请款金额 */
	private BigDecimal unDistributeAmount;
	/** 抵扣金额 **/
	private BigDecimal ductionMoney;
	/** 总退款金额 **/
	private BigDecimal totalRefundAmount;
	/**
	 * CNY汇率
	 */
	private BigDecimal cnyRate;
	/** PO单未到货金额=订单金额-折扣金额-抵扣金额-到货金额 **/
	private BigDecimal poAmount;
	/**
	 * 铺货订单可发货数量
	 */
	private BigDecimal totalRemainSendNum;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	public BigDecimal getDuctionMoney() {
		return ductionMoney;
	}

	public void setDuctionMoney(BigDecimal ductionMoney) {
		this.ductionMoney = ductionMoney;
	}

	public BigDecimal getPerRecAmount() {
		return perRecAmount;
	}

	public void setPerRecAmount(BigDecimal perRecAmount) {
		this.perRecAmount = perRecAmount;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
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

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
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

	public Integer getWareAddrId() {
		return wareAddrId;
	}

	public void setWareAddrId(Integer wareAddrId) {
		this.wareAddrId = wareAddrId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPerdictTime() {
		return perdictTime;
	}

	public void setPerdictTime(Date perdictTime) {
		this.perdictTime = perdictTime;
	}

	public Date getRequestPayTime() {
		return requestPayTime;
	}

	public void setRequestPayTime(Date requestPayTime) {
		this.requestPayTime = requestPayTime;
	}

	public String getcBankWater() {
		return cBankWater;
	}

	public void setcBankWater(String cBankWater) {
		this.cBankWater = cBankWater;
	}

	public Integer getIsRequestPay() {
		return isRequestPay;
	}

	public void setIsRequestPay(Integer isRequestPay) {
		this.isRequestPay = isRequestPay;
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

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public BigDecimal getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
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

	public BigDecimal getTakeDeliveryNum() {
		return takeDeliveryNum;
	}

	public void setTakeDeliveryNum(BigDecimal takeDeliveryNum) {
		this.takeDeliveryNum = takeDeliveryNum;
	}

	public BigDecimal getUnDistributeNum() {
		return unDistributeNum;
	}

	public void setUnDistributeNum(BigDecimal unDistributeNum) {
		this.unDistributeNum = unDistributeNum;
	}

	public BigDecimal getUnDistributeAmount() {
		return unDistributeAmount;
	}

	public void setUnDistributeAmount(BigDecimal unDistributeAmount) {
		this.unDistributeAmount = unDistributeAmount;
	}

	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public BigDecimal getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(BigDecimal cnyRate) {
		this.cnyRate = cnyRate;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}

	public BigDecimal getTotalRemainSendNum() {
		return totalRemainSendNum;
	}

	public void setTotalRemainSendNum(BigDecimal totalRemainSendNum) {
		this.totalRemainSendNum = totalRemainSendNum;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
