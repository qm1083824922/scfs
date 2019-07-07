package com.scfs.domain.fi.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Administrator on 2017年3月3日.
 */
public class VoucherLineModel extends VoucherLine{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2976592761354628041L;
    /**帐套id 关联tb_fi_account_book{id} **/
    private Integer accountBookId;
    
    /**凭证日期，可能不是凭证创建日期 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date voucherDate;
    
    /**单据类型*/
    private Integer billType;
    
    /**本位币**/
    private Integer standardCoin;
    
    /**凭证编号 **/
    private String voucherNo;
    
    /** 行号 **/
    private Integer lineNumber;
    
	/** 部门ID */
	private Integer departmentId;
	
	/** 供应商编码 */
	private String supplierNo;
	
	/** 创建日期 */
	private Date voucherCreateAt;

	public Integer getAccountBookId() {
		return accountBookId;
	}

	public void setAccountBookId(Integer accountBookId) {
		this.accountBookId = accountBookId;
	}

	public Date getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getStandardCoin() {
		return standardCoin;
	}

	public void setStandardCoin(Integer standardCoin) {
		this.standardCoin = standardCoin;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Date getVoucherCreateAt() {
		return voucherCreateAt;
	}

	public void setVoucherCreateAt(Date voucherCreateAt) {
		this.voucherCreateAt = voucherCreateAt;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
    
    
    
}

