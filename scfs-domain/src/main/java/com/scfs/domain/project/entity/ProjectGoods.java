package com.scfs.domain.project.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

public class ProjectGoods extends BaseEntity {

	private static final long serialVersionUID = -9039106743470819269L;

	/** 关联项目 */
	private Integer projectId;

	/** 关联商品 */
	private Integer goodsId;

	/** 状态 */
	private Integer status;

	/** 质押比例 */
	private BigDecimal pledge;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getPledge() {
		return pledge;
	}

	public void setPledge(BigDecimal pledge) {
		this.pledge = pledge;
	}

}
