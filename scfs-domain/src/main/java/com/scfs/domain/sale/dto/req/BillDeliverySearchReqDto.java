package com.scfs.domain.sale.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月27日.
 */
public class BillDeliverySearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1393150048326196967L;
	private Integer id;
	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;
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
	 * 销售单编号
	 */
	private String billNo;
	/**
	 * 销售单类型
	 */
	private Integer billType;
	/**
	 * 开始要求出货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startRequiredSendDate;
	/**
	 * 结束要求出货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endRequiredSendDate;
	/**
	 * 附属编号
	 */
	private String affiliateNo;
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
	 * 运输方式
	 */
	private Integer transferMode;
	/**
	 * 销售类型列表
	 */
	private List<Integer> billTypeList;
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

	public Date getStartRequiredSendDate() {
		return startRequiredSendDate;
	}

	public void setStartRequiredSendDate(Date startRequiredSendDate) {
		this.startRequiredSendDate = startRequiredSendDate;
	}

	public Date getEndRequiredSendDate() {
		return endRequiredSendDate;
	}

	public void setEndRequiredSendDate(Date endRequiredSendDate) {
		this.endRequiredSendDate = endRequiredSendDate;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
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

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public Integer getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Integer transferMode) {
		this.transferMode = transferMode;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public List<Integer> getBillTypeList() {
		return billTypeList;
	}

	public void setBillTypeList(List<Integer> billTypeList) {
		this.billTypeList = billTypeList;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
