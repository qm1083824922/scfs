package com.scfs.domain.export.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  出口退税申请信息
 *  File: RefundApply.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月06日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RefundApply extends BaseEntity {
	/** 出口退税申请id **/
	private Integer id;
	/** 退税申请编号 **/
	private String refundApplyNo;
	/** 退税附属编号 **/
	private String refundAttachNo;
	/** 项目id **/
	private Integer projectId;
	private Integer printNum;

	/** 客户id **/
	private Integer custId;
	/** 退税数量 **/
	private BigDecimal refundApplyNum;
	/** 退税金额 **/
	private BigDecimal refundApplyAmount;
	/** 可退税额 **/
	private BigDecimal refundApplyTax;
	/** 退税申请日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date refundApplyDate;
	/** 核销金额 **/
	private BigDecimal verifyAmount;
	/** 核销日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date verifyDate;
	/** 核销 **/
	private String verify;
	/** 状态 1:待提交 2.待财务审核 3,已完成 **/
	private Integer state;
	/** 备注 **/
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRefundApplyNo() {
		return refundApplyNo;
	}

	public void setRefundApplyNo(String refundApplyNo) {
		this.refundApplyNo = refundApplyNo;
	}

	public String getRefundAttachNo() {
		return refundAttachNo;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public void setRefundAttachNo(String refundAttachNo) {
		this.refundAttachNo = refundAttachNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public BigDecimal getRefundApplyNum() {
		return refundApplyNum;
	}

	public void setRefundApplyNum(BigDecimal refundApplyNum) {
		this.refundApplyNum = refundApplyNum;
	}

	public BigDecimal getRefundApplyAmount() {
		return refundApplyAmount;
	}

	public void setRefundApplyAmount(BigDecimal refundApplyAmount) {
		this.refundApplyAmount = refundApplyAmount;
	}

	public BigDecimal getRefundApplyTax() {
		return refundApplyTax;
	}

	public void setRefundApplyTax(BigDecimal refundApplyTax) {
		this.refundApplyTax = refundApplyTax;
	}

	public Date getRefundApplyDate() {
		return refundApplyDate;
	}

	public void setRefundApplyDate(Date refundApplyDate) {
		this.refundApplyDate = refundApplyDate;
	}

	public BigDecimal getVerifyAmount() {
		return verifyAmount;
	}

	public void setVerifyAmount(BigDecimal verifyAmount) {
		this.verifyAmount = verifyAmount;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
