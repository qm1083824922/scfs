package com.scfs.domain.api.pms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PmsReturnDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * PMS退货申请单id
	 */
	private Integer pms_return_id;

	/**
	 * 商品sku
	 */
	private String sku;

	/**
	 * 退货申请数量
	 */
	private BigDecimal refund_quantity;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPms_return_id() {
		return pms_return_id;
	}

	public void setPms_return_id(Integer pms_return_id) {
		this.pms_return_id = pms_return_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getRefund_quantity() {
		return refund_quantity;
	}

	public void setRefund_quantity(BigDecimal refund_quantity) {
		this.refund_quantity = refund_quantity;
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
}