package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsReturnPoRel {
	/**
	 * 退货采购关联Id
	 */
	private Integer id;

	/**
	 * 铺货退货类型采购头ID
	 */
	private Integer rtPoId;

	/**
	 * 铺货退货类型采购明细ID
	 */
	private Integer rtPoLineId;

	/**
	 * 代销订单采购头ID
	 */
	private Integer poId;

	/**
	 * 代销订单采购明细ID
	 */
	private Integer poLineId;

	/**
	 * 退货数量
	 */
	private BigDecimal returnNumber;

	/**
	 * 创建时间
	 */
	private Date createAt;

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

	public Integer getRtPoId() {
		return rtPoId;
	}

	public void setRtPoId(Integer rtPoId) {
		this.rtPoId = rtPoId;
	}

	public Integer getRtPoLineId() {
		return rtPoLineId;
	}

	public void setRtPoLineId(Integer rtPoLineId) {
		this.rtPoLineId = rtPoLineId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public BigDecimal getReturnNumber() {
		return returnNumber;
	}

	public void setReturnNumber(BigDecimal returnNumber) {
		this.returnNumber = returnNumber;
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