package com.scfs.domain.logistics.dto.req;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年12月27日.
 */
public class VerificationAdvanceReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1219484789209674797L;

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
	 * 水单日期
	 */
	private String receiptDate;
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

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
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

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

}
