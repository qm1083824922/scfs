package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectItemSegment {
	/**
	 * 自增ID
	 */
	private Integer id;

	/**
	 * 项目条款ID
	 */
	private Integer projectItemId;

	/**
	 * 分段天数
	 */
	private Integer segmentDay;

	/**
	 * 服务费率
	 */
	private BigDecimal segmentFundMonthRate;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	/**
	 * 作废人
	 */
	private String deleter;

	/**
	 * 作废时间
	 */
	private Date deleteAt;

	/**
	 * 删除标记 0: 有效 1: 删除
	 */
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectItemId() {
		return projectItemId;
	}

	public void setProjectItemId(Integer projectItemId) {
		this.projectItemId = projectItemId;
	}

	public Integer getSegmentDay() {
		return segmentDay;
	}

	public void setSegmentDay(Integer segmentDay) {
		this.segmentDay = segmentDay;
	}

	public BigDecimal getSegmentFundMonthRate() {
		return segmentFundMonthRate;
	}

	public void setSegmentFundMonthRate(BigDecimal segmentFundMonthRate) {
		this.segmentFundMonthRate = segmentFundMonthRate;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}