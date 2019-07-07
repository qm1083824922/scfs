package com.scfs.domain.interf.dto;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: PmsPayOrderSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */
public class PmsPoTitleSearchReqDto extends BaseReqDto {

	private static final long serialVersionUID = 2073649455820490066L;

	private String payNo;
	private String corporation_code;
	private String corporation_name;
	private String vendorNo;
	private String supplierNo;
	private Integer state;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startPayDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endPayDate;
	// 1.待处理(待处理或者处理失败)
	private Integer searchType;

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getCorporation_code() {
		return corporation_code;
	}

	public void setCorporation_code(String corporation_code) {
		this.corporation_code = corporation_code;
	}

	public String getCorporation_name() {
		return corporation_name;
	}

	public void setCorporation_name(String corporation_name) {
		this.corporation_name = corporation_name;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getStartPayDate() {
		return startPayDate;
	}

	public void setStartPayDate(Date startPayDate) {
		this.startPayDate = startPayDate;
	}

	public Date getEndPayDate() {
		return endPayDate;
	}

	public void setEndPayDate(Date endPayDate) {
		this.endPayDate = endPayDate;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}
}
