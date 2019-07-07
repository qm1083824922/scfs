package com.scfs.domain.audit.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 审核节点 Created by Administrator on 2017年7月24日.
 */
public class AuditNode implements Comparable<AuditNode> {
	/**
	 * 审核节点序号
	 */
	private Integer index;
	/**
	 * 审核节点名称
	 */
	private String auditNodeName;
	/**
	 * 审核节点状态
	 */
	private Integer auditNodeState;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getAuditNodeName() {
		return auditNodeName;
	}

	public void setAuditNodeName(String auditNodeName) {
		this.auditNodeName = auditNodeName;
	}

	public Integer getAuditNodeState() {
		return auditNodeState;
	}

	public void setAuditNodeState(Integer auditNodeState) {
		this.auditNodeState = auditNodeState;
	}

	@Override
	public int compareTo(AuditNode o) {
		return this.index.compareTo(o.index);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
