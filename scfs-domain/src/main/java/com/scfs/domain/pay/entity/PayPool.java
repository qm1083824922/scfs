package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PayPool {
	private Integer id;

	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;

	/**
	 * 账户ID
	 */
	private Integer accountId;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 出入类型1、支出；2、收入
	 */
	private Integer inOut;

	/**
	 * 类别：1、收内部款；2、付内部款；3、收客户款；4、付客户款；5、收融资款；6、付融资款
	 */
	private Integer type;

	/**
	 * 单据ID
	 */
	private Integer billId;

	/**
	 * 单据类别:1、付款；2、收款；3、凭证
	 */
	private Integer billType;

	/**
	 * 单据编号
	 */
	private String billNo;

	/**
	 * 金额
	 */
	private BigDecimal amount;

	/**
	 * 币种
	 */
	private Integer currencyType;

	/**
	 * 作废人
	 */
	private String deleter;

	/**
	 * 作废时间
	 */
	private Date deleteAt;

	/**
	 * 创建id
	 */
	private Integer createrId;

	/**
	 * 创建时间
	 */
	private Date createrAt;

	/**
	 * 创建人
	 */
	private String creater;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 作废人id
	 */
	private Integer deleterId;

	/**
	 * 是否作废：0、不作废；1、作废
	 */
	private Integer isDelete;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getInOut() {
		return inOut;
	}

	public void setInOut(Integer inOut) {
		this.inOut = inOut;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter == null ? null : deleter.trim();
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Integer getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public Date getCreaterAt() {
		return createrAt;
	}

	public void setCreaterAt(Date createrAt) {
		this.createrAt = createrAt;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater == null ? null : creater.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getDeleterId() {
		return deleterId;
	}

	public void setDeleterId(Integer deleterId) {
		this.deleterId = deleterId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}