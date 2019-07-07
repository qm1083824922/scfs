package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: ProjectGoodsSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月25日				cuichao
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProjectGoodsSearchReqDto extends BaseReqDto {
	/** 项目 */
	private Integer projectId;

	/** 编号 */
	private String number;

	/** 名称 */
	private String name;

	/** 型号 */
	private String type;

	/** 条码 */
	private String barCode;

	/** 规格 */
	private String specification;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

}
