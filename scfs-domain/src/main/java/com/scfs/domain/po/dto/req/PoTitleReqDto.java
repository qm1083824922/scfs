package com.scfs.domain.po.dto.req;

import java.math.BigDecimal;
import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/10/15.
 */
public class PoTitleReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9007002960216402705L;
	/** 系统编号 */
	private String sysNo;
	/** 附属编号 */
	private String appendNo;
	/** 订单编号 */
	private String orderNo;
	/** 经营单位ID */
	private Integer businessUnitId;
	/** 项目ID */
	private Integer projectId;
	/** 供应商ID */
	private Integer supplierId;
	/** 仓库ID */
	private Integer warehouseId;
	/** 客户ID */
	private Integer customerId;
	/** 币种ID */
	private Integer currencyId;
	/** 入库编号 */
	private String billOutStoreNo;
	/** 入库附属编号 */
	private String billOutStoreAffiliateNo;
	/** 开始订单日期 */
	private String startOrderTime;
	/** 结束订单日期 */
	private String endOrderTime;
	/** 开始收货日期 */
	private String startDeliveryTime;
	/** 结束收货日期 */
	private String endDeliveryTime;
	/** 开始预计到货日期 */
	private String startPerdictTime;
	/** 结束预计到货日期 */
	private String endPerdictTime;
	/** 状态 */
	private Integer state;
	/** 是否付款完成 0已付款完成，1未付款完成 **/
	private Integer isPayAll;
	/** 是否付款完成 0已收票完成，1未收票完成 **/
	private Integer isVatAll;
	private Integer orderType;
	/** 商品id **/
	private Integer goodsId;
	/** 商品编号 */
	private String goodsNo;
	/** 商品名称 */
	private String goodsName;
	/** 商品型号 */
	private String goodsType;
	/** 商品条码 */
	private String goodsBarCode;
	/** 理货数量 */
	private BigDecimal tallyNum;
	/** 批次 */
	private String batchNo;
	/** 是否需要收票 1 ：是 2 否 key:IS_NEED ***/
	private Integer needInvoice;
	/** 飞单表示 0-否 1-是 */
	private Integer flyOrderFlag;

	/** 部门 **/
	private List<Integer> departmentId;

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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getBillOutStoreNo() {
		return billOutStoreNo;
	}

	public void setBillOutStoreNo(String billOutStoreNo) {
		this.billOutStoreNo = billOutStoreNo;
	}

	public String getBillOutStoreAffiliateNo() {
		return billOutStoreAffiliateNo;
	}

	public void setBillOutStoreAffiliateNo(String billOutStoreAffiliateNo) {
		this.billOutStoreAffiliateNo = billOutStoreAffiliateNo;
	}

	public String getStartOrderTime() {
		return startOrderTime;
	}

	public void setStartOrderTime(String startOrderTime) {
		this.startOrderTime = startOrderTime;
	}

	public String getEndOrderTime() {
		return endOrderTime;
	}

	public void setEndOrderTime(String endOrderTime) {
		this.endOrderTime = endOrderTime;
	}

	public String getStartDeliveryTime() {
		return startDeliveryTime;
	}

	public void setStartDeliveryTime(String startDeliveryTime) {
		this.startDeliveryTime = startDeliveryTime;
	}

	public String getEndDeliveryTime() {
		return endDeliveryTime;
	}

	public void setEndDeliveryTime(String endDeliveryTime) {
		this.endDeliveryTime = endDeliveryTime;
	}

	public String getStartPerdictTime() {
		return startPerdictTime;
	}

	public void setStartPerdictTime(String startPerdictTime) {
		this.startPerdictTime = startPerdictTime;
	}

	public String getEndPerdictTime() {
		return endPerdictTime;
	}

	public void setEndPerdictTime(String endPerdictTime) {
		this.endPerdictTime = endPerdictTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsPayAll() {
		return isPayAll;
	}

	public void setIsPayAll(Integer isPayAll) {
		this.isPayAll = isPayAll;
	}

	public Integer getIsVatAll() {
		return isVatAll;
	}

	public void setIsVatAll(Integer isVatAll) {
		this.isVatAll = isVatAll;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getNeedInvoice() {
		return needInvoice;
	}

	public void setNeedInvoice(Integer needInvoice) {
		this.needInvoice = needInvoice;
	}

	public List<Integer> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
