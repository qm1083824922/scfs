package com.scfs.domain.interf.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

public class PMSSupplierBindResDto implements Serializable {

	private static final long serialVersionUID = -5822149316697584912L;
	private Integer id;
	/** 供应商ID */
	private String supplierName;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	private String projectName;
	/** 状态名称 */
	private String statusName;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PMSS_SUPPLIER_BIND);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_PMSS_SUPPLIER_BIND);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_PMSS_SUPPLIER_BIND);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_PMSS_SUPPLIER_BIND);
			operMap.put(OperateConsts.UNBIND, BusUrlConsts.UNBIND_PMSS_SUPPLIER_BIND);
			operMap.put(OperateConsts.BIND, BusUrlConsts.BIND_PMSS_SUPPLIER_BIND);
		}
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}