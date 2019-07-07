package com.scfs.domain.base.entity;

/**
 * <pre>
 *  客户跟进信息
 *  File: CustomerMaintain.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月27日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CustomerFollow extends BaseEntity {
	/** 客户id **/
	private Integer customerId;
	/** 所处阶段 1 意向阶段 2 合作阶段 3 已取消 **/
	private Integer stage;
	/** 跟进内容 **/
	private String content;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
