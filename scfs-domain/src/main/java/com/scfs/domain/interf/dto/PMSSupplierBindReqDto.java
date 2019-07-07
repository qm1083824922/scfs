package com.scfs.domain.interf.dto;

import com.scfs.domain.BaseReqDto;

public class PMSSupplierBindReqDto extends BaseReqDto {

	private static final long serialVersionUID = 9147074654664059558L;

	/** 供应商ID */
	private Integer supplierId;
	/** 供应商编号 */
	private String supplierNo;
	/** PMS供应商编号 */
	private String pmsSupplierNo;
	/** 状态 1-待提交 2-已提交未绑定 3-已提交已绑定 */
	private Integer status;
	/** 项目ID */
	private Integer projectId;
	/** 项目类型 1 代理采购 2 应收保理 3 纯购销 4物流分包 5 纯仓储 6融通质押 7 铺货质押 **/
	private Integer bizType;
	/**
	 * 经营单位
	 */
	private Integer businessUnit;

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getPmsSupplierNo() {
		return pmsSupplierNo;
	}

	public void setPmsSupplierNo(String pmsSupplierNo) {
		this.pmsSupplierNo = pmsSupplierNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
	}
}