package com.scfs.domain.logistics.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.base.entity.BaseUserSubject;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillOutStoreSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 963686484905007326L;
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
	 * 出库编号
	 */
	private String billNo;
	/**
	 * 出库类型
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
	 * 开始出货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startSendDate;
	/**
	 * 结束出货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endSendDate;
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
	 * 销售单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 删除标记 0 : 有效 1 : 删除
	 */
	private Integer isDelete;
	/**
	 * 开始出库时间
	 */
	private String startDeliverTime;
	/**
	 * 结束出库时间
	 */
	private String endDeliverTime;
	/**
	 * 报关申请ID
	 */
	private Integer customsApplyId;
	/**
	 * 运输方式
	 */
	private Integer transferMode;
	/**
	 * 销售单号
	 */
	private String billDeliveryNo;
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

	public Date getStartSendDate() {
		return startSendDate;
	}

	public void setStartSendDate(Date startSendDate) {
		this.startSendDate = startSendDate;
	}

	public Date getEndSendDate() {
		return endSendDate;
	}

	public void setEndSendDate(Date endSendDate) {
		this.endSendDate = endSendDate;
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

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getStartDeliverTime() {
		return startDeliverTime;
	}

	public void setStartDeliverTime(String startDeliverTime) {
		this.startDeliverTime = startDeliverTime;
	}

	public String getEndDeliverTime() {
		return endDeliverTime;
	}

	public void setEndDeliverTime(String endDeliverTime) {
		this.endDeliverTime = endDeliverTime;
	}

	public Integer getCustomsApplyId() {
		return customsApplyId;
	}

	public void setCustomsApplyId(Integer customsApplyId) {
		this.customsApplyId = customsApplyId;
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

	public String getBillDeliveryNo() {
		return billDeliveryNo;
	}

	public void setBillDeliveryNo(String billDeliveryNo) {
		this.billDeliveryNo = billDeliveryNo;
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
