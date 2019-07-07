package com.scfs.domain.project.entity;

import java.util.Date;

public class ProjectNoSeq {
	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 项目编号类型
	 */
	private String projectNoType;

	/**
	 * 序列值
	 */
	private Integer seqVal;

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

	public String getProjectNoType() {
		return projectNoType;
	}

	public void setProjectNoType(String projectNoType) {
		this.projectNoType = projectNoType == null ? null : projectNoType.trim();
	}

	public Integer getSeqVal() {
		return seqVal;
	}

	public void setSeqVal(Integer seqVal) {
		this.seqVal = seqVal;
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