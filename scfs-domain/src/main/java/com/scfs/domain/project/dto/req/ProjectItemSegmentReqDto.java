package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年5月18日.
 */
public class ProjectItemSegmentReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5718679821529578809L;

	private Integer projectItemId;

	private Integer isDelete;

	private Long segmentDay;

	public Integer getProjectItemId() {
		return projectItemId;
	}

	public void setProjectItemId(Integer projectItemId) {
		this.projectItemId = projectItemId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getSegmentDay() {
		return segmentDay;
	}

	public void setSegmentDay(Long segmentDay) {
		this.segmentDay = segmentDay;
	}

}
