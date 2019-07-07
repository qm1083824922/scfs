package com.scfs.domain.logistics.dto.req;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * Created by Administrator on 2017年07月12日.
 *
 */
public class BankReceiptAdvanceMentReqDto implements Serializable {

	private static final long serialVersionUID = 4147529065226518074L;

	/**
	 * 销售单ID,关联tb_bill_delivery[id]
	 */
	private Integer billDeliveryId;

	/**
	 * 预收id
	 */
	private Integer advanceId;

	/**
	 * 水单id
	 */
	private Integer receiptId;

	/**
	 * 金额
	 */
	private BigDecimal availableAmount;

	/**
	 * 水单号
	 */
	private String receiptNo;

	/**
	 * 水单类型
	 */
	private Integer receiptType;
	/**
	 * 到账金额
	 */
	private BigDecimal receiptAmount;
	/**
	 * 余额
	 */
	private BigDecimal writeOffAmount;

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(Integer advanceId) {
		this.advanceId = advanceId;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
	}
}
