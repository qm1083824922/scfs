package com.scfs.domain.interf.entity;

import java.util.Date;

public class PMSSupplierBind {
	private Integer id;
	/** 供应商ID */
	private Integer supplierId;
	/** 供应商编号 */
	private String supplierNo;
	/** PMS供应商编号 */
	private String pmsSupplierNo;
	/** 状态 0-待提交 1-已提交未绑定 2-已提交已绑定 */
	private Integer status;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;
	/** 作废人ID */
	private Integer deleterId;
	/** 作废人 */
	private String deleter;
	/** 作废标记 0:有效 1:删除 */
	private Integer isDelete;
	/** 作废时间 */
	private Date deleteAt;
	/** 项目ID */
	private Integer projectId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getDeleterId() {
		return deleterId;
	}

	public void setDeleterId(Integer deleterId) {
		this.deleterId = deleterId;
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter == null ? null : deleter.trim();
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
}