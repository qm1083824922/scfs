package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsOutPoRel {
	/**
	 * 铺货出库采购关联id
	 */
	private Integer id;

	/**
	 * 铺货出库ID
	 */
	private Integer pmsOutId;

	/**
	 * 采购单明细Id
	 */
	private Integer poLineId;
	/**
	 * 采购单ID
	 */
	private Integer poId;

	/**
	 * 出库数量
	 */
	private BigDecimal outNumber;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 删除标识 0 否 1 是
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

	public Integer getPmsOutId() {
		return pmsOutId;
	}

	public void setPmsOutId(Integer pmsOutId) {
		this.pmsOutId = pmsOutId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public BigDecimal getOutNumber() {
		return outNumber;
	}

	public void setOutNumber(BigDecimal outNumber) {
		this.outNumber = outNumber;
	}

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}
}