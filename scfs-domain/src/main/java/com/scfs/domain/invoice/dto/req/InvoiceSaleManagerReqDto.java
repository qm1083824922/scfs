package com.scfs.domain.invoice.dto.req;

import com.scfs.domain.BaseReqDto;

public class InvoiceSaleManagerReqDto extends BaseReqDto {

	private static final long serialVersionUID = -9046694573325961893L;

	/** 自增ID */
	private Integer id;
	/** 发票申请ID */
	private Integer invoiceApplyId;
	/** 货物数量 */
	private String goodNum;
	/** 货物名称 */
	private String goodName;
	/** 单据ID, 销售单/退货单ID */
	private Integer billId;
	/** 单据编号, 销售单/退货单编号 */
	private String billNo;
	/** 单据明细ID, 销售单/退货单明细ID */
	private Integer billDtlId;
	/** 项目id */
	private Integer projectId;
	/** 客户id */
	private Integer customerId;
	/** 币种 **/
	private Integer currencyType;
	/** 销售单类型 **/
	private Integer billType;

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInvoiceApplyId() {
		return invoiceApplyId;
	}

	public void setInvoiceApplyId(Integer invoiceApplyId) {
		this.invoiceApplyId = invoiceApplyId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getBillDtlId() {
		return billDtlId;
	}

	public void setBillDtlId(Integer billDtlId) {
		this.billDtlId = billDtlId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

}