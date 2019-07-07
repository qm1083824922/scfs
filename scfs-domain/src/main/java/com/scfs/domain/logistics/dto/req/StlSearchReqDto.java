package com.scfs.domain.logistics.dto.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.logistics.entity.Stl;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class StlSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5782288679689686534L;
	private Integer businessUnitId;
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
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
	private Integer currencyId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品型号
	 */
	private String goodsType;
	/**
	 * 商品条码
	 */
	private String goodsBarCode;
	/**
	 * 入库编号
	 */
	private String billInStoreNo;
	/**
	 * 入库附属编号
	 */
	private String affiliateNo;
	/**
	 * 开始收货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startReceiveDate;
	/**
	 * 结束收货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endReceiveDate;
	/**
	 * 开始入库日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startAcceptTime;
	/**
	 * 结束入库日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endAcceptTime;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 状态 1-正常 2-残次品
	 */
	private Integer goodsStatus;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 是否可用库存
	 */
	private Integer availableFlag;
	/**
	 * 库存大于零标识
	 */
	private Integer storeFlag;
	/**
	 * 复制日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date copyDate;
	/**
	 * 当前日期
	 */
	private Date currDate;
	/**
	 * 查询客户为空的库存
	 */
	private Integer isCustomerNullFlag;
	/**
	 * 查询来源 1-销售选择库存 2-出库选择库存 3-出库单拣货 4-出库单添加商品 5-销售单添加商品 6-销售单明细修改
	 */
	private Integer querySource;
	/**
	 * 关联单据ID
	 */
	private Integer refId;
	/**
	 * 未拣货数量
	 */
	private BigDecimal unPickNum;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 出库单明细ID
	 */
	private Integer billOutStoreDtlId;
	/**
	 * 销售单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 销售单明细ID
	 */
	private Integer billDeliveryDtlId;
	/**
	 * 是否付款
	 */
	private Integer isExistPay;
	/**
	 * 同客户下库存
	 */
	private Integer isSameCustomer;
	/**
	 * 按订单号精确查询
	 */
	private String preOrderNo;
	/**
	 * 库存列表
	 */
	private List<Stl> stlList;

	List<BaseUserSubject> userSubject;
	/**
	 * 开始付款日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startPayTime;
	/**
	 * 结束付款日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endPayTime;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	private Integer day;

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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getBillInStoreNo() {
		return billInStoreNo;
	}

	public void setBillInStoreNo(String billInStoreNo) {
		this.billInStoreNo = billInStoreNo;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
	}

	public Date getStartReceiveDate() {
		return startReceiveDate;
	}

	public void setStartReceiveDate(Date startReceiveDate) {
		this.startReceiveDate = startReceiveDate;
	}

	public Date getEndReceiveDate() {
		return endReceiveDate;
	}

	public void setEndReceiveDate(Date endReceiveDate) {
		this.endReceiveDate = endReceiveDate;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getAvailableFlag() {
		return availableFlag;
	}

	public void setAvailableFlag(Integer availableFlag) {
		this.availableFlag = availableFlag;
	}

	public Date getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(Date copyDate) {
		this.copyDate = copyDate;
	}

	public Date getCurrDate() {
		return currDate;
	}

	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getStartAcceptTime() {
		return startAcceptTime;
	}

	public void setStartAcceptTime(Date startAcceptTime) {
		this.startAcceptTime = startAcceptTime;
	}

	public Date getEndAcceptTime() {
		return endAcceptTime;
	}

	public void setEndAcceptTime(Date endAcceptTime) {
		this.endAcceptTime = endAcceptTime;
	}

	public Integer getIsCustomerNullFlag() {
		return isCustomerNullFlag;
	}

	public void setIsCustomerNullFlag(Integer isCustomerNullFlag) {
		this.isCustomerNullFlag = isCustomerNullFlag;
	}

	public Integer getQuerySource() {
		return querySource;
	}

	public void setQuerySource(Integer querySource) {
		this.querySource = querySource;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public BigDecimal getUnPickNum() {
		return unPickNum;
	}

	public void setUnPickNum(BigDecimal unPickNum) {
		this.unPickNum = unPickNum;
	}

	public Integer getStoreFlag() {
		return storeFlag;
	}

	public void setStoreFlag(Integer storeFlag) {
		this.storeFlag = storeFlag;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getBillOutStoreDtlId() {
		return billOutStoreDtlId;
	}

	public void setBillOutStoreDtlId(Integer billOutStoreDtlId) {
		this.billOutStoreDtlId = billOutStoreDtlId;
	}

	public Integer getBillDeliveryDtlId() {
		return billDeliveryDtlId;
	}

	public void setBillDeliveryDtlId(Integer billDeliveryDtlId) {
		this.billDeliveryDtlId = billDeliveryDtlId;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public Integer getIsExistPay() {
		return isExistPay;
	}

	public void setIsExistPay(Integer isExistPay) {
		this.isExistPay = isExistPay;
	}

	public Integer getIsSameCustomer() {
		return isSameCustomer;
	}

	public void setIsSameCustomer(Integer isSameCustomer) {
		this.isSameCustomer = isSameCustomer;
	}

	public String getPreOrderNo() {
		return preOrderNo;
	}

	public void setPreOrderNo(String preOrderNo) {
		this.preOrderNo = preOrderNo;
	}

	public List<Stl> getStlList() {
		return stlList;
	}

	public void setStlList(List<Stl> stlList) {
		this.stlList = stlList;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public List<BaseUserSubject> getUserSubject() {
		return userSubject;
	}

	public void setUserSubject(List<BaseUserSubject> userSubject) {
		this.userSubject = userSubject;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Date getStartPayTime() {
		return startPayTime;
	}

	public void setStartPayTime(Date startPayTime) {
		this.startPayTime = startPayTime;
	}

	public Date getEndPayTime() {
		return endPayTime;
	}

	public void setEndPayTime(Date endPayTime) {
		this.endPayTime = endPayTime;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
