package com.scfs.domain.audit.dto.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.scfs.domain.logistics.dto.req.BankReceiptAdvanceMentReqDto;
import com.scfs.domain.logistics.dto.req.VerificationAdvanceReqDto;

/**
 * Created by Administrator on 2016年11月2日.
 */
public class BillDeliveryAuditReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2519089524622132273L;
	private Integer id;
	// 审核ID
	private Integer auditId;
	// 销售单ID
	private Integer billDeliveryId;
	// 转交、加签人id
	private Integer pauditorId;
	// 转交、加签人id
	private String suggestion;
	/**
	 * 水单日期
	 */
	private String receiptDate;
	/**
	 * 回款日期
	 */
	private Date returnTime;
	/**
	 * 水单明细
	 */
	private List<VerificationAdvanceReqDto> verificationAdvanceList;

	/** 预收货款抵扣水单明显 **/
	private List<BankReceiptAdvanceMentReqDto> advanceMentList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getPauditorId() {
		return pauditorId;
	}

	public void setPauditorId(Integer pauditorId) {
		this.pauditorId = pauditorId;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public List<VerificationAdvanceReqDto> getVerificationAdvanceList() {
		return verificationAdvanceList;
	}

	public void setVerificationAdvanceList(List<VerificationAdvanceReqDto> verificationAdvanceList) {
		this.verificationAdvanceList = verificationAdvanceList;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public List<BankReceiptAdvanceMentReqDto> getAdvanceMentList() {
		return advanceMentList;
	}

	public void setAdvanceMentList(List<BankReceiptAdvanceMentReqDto> advanceMentList) {
		this.advanceMentList = advanceMentList;
	}

}
