package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsPayPoRel {
	/**
	 * 请款单采购关联ID
	 */
	private Integer id;

	/**
	 * 铺货出库ID
	 */
	private Integer pmsPayDtlId;

	/**
	 * 铺货类型的采购单ID
	 */
	private Integer poId;

	/**
	 * 铺货类型的采购单明细ID
	 */
	private Integer poLineId;

	/**
	 * 采购类型的采购明细ID
	 */
	private Integer pyPoLineId;

	/**
	 * 请款数量
	 */
	private BigDecimal payQuantity;

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

	public Integer getPmsPayDtlId() {
		return pmsPayDtlId;
	}

	public void setPmsPayDtlId(Integer pmsPayDtlId) {
		this.pmsPayDtlId = pmsPayDtlId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public BigDecimal getPayQuantity() {
		return payQuantity;
	}

	public void setPayQuantity(BigDecimal payQuantity) {
		this.payQuantity = payQuantity;
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

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public Integer getPyPoLineId() {
		return pyPoLineId;
	}

	public void setPyPoLineId(Integer pyPoLineId) {
		this.pyPoLineId = pyPoLineId;
	}
}