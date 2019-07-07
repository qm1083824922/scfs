package com.scfs.domain.logistics.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.base.entity.BaseUserSubject;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class StlHistorySearchReqDto extends BaseReqDto {

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
	 * 状态 1-正常 2-残次品
	 */
	private Integer goodsStatus;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 复制日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date copyDate;
	/**
	 * 库存大于零标识
	 */
	private Integer storeFlag;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	List<BaseUserSubject> userSubject;

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

	public Date getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(Date copyDate) {
		this.copyDate = copyDate;
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

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public List<BaseUserSubject> getUserSubject() {
		return userSubject;
	}

	public void setUserSubject(List<BaseUserSubject> userSubject) {
		this.userSubject = userSubject;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
