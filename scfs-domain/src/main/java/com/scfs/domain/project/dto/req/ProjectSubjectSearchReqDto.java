package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

public class ProjectSubjectSearchReqDto extends BaseReqDto {

	private static final long serialVersionUID = 7282909059311225067L;

	/**
	 * 主键
	 */
	private Integer projectId;

	/**
	 * 类型
	 */
	private Integer subjectType;

	/**
	 * 主体编号
	 */
	private String subjectNo;

	/**
	 * 简称
	 */
	private String abbreviation;

	/**
	 * 中文名
	 */
	private String chineseName;

	/**
	 * 英文名
	 */
	private String englishName;

	/**
	 * 状态
	 */
	private Integer status;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

}
