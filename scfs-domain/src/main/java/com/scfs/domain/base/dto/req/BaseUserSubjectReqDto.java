package com.scfs.domain.base.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.base.entity.BaseUserSubject;

/**
 * <pre>
 *  用户基础信息关系service
 *  File: BaseUserSubjectReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class BaseUserSubjectReqDto extends BaseReqDto {

	private Integer userId;

	private Integer subjectId;

	private Integer subjectType;
	private String subjectNo;
	private String abbreviation;
	private String chineseName;
	List<BaseUserSubject> userSubjectList;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
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

	public List<BaseUserSubject> getUserSubjectList() {
		return userSubjectList;
	}

	public void setUserSubjectList(List<BaseUserSubject> userSubjectList) {
		this.userSubjectList = userSubjectList;
	}

}
