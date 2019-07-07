package com.scfs.domain.logistics.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.base.entity.BaseUserSubject;

/**
 * Created by Administrator on 2016年10月17日.
 */
public class BillInStoreSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4502434912583965713L;
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
	 * 入库编号
	 */
	private String billNo;
	/**
	 * 入库类型, 即收货类型
	 */
	private Integer billType;
	/**
	 * 开始到货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startReceiveDate;
	/**
	 * 结束到货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endReceiveDate;
	/**
	 * 附属编号
	 */
	private String affiliateNo;

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
	 * 状态
	 */
	private Integer status;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 删除标记 0 : 有效 1 : 删除
	 */
	private Integer isDelete;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	private List<PoOrderDtlReqDto> poOrderDtlReqDtoList;

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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<PoOrderDtlReqDto> getPoOrderDtlReqDtoList() {
		return poOrderDtlReqDtoList;
	}

	public void setPoOrderDtlReqDtoList(List<PoOrderDtlReqDto> poOrderDtlReqDtoList) {
		this.poOrderDtlReqDtoList = poOrderDtlReqDtoList;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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
