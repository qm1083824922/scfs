package com.scfs.domain.base.region.dto.resp;

import java.util.List;

/**
 * <pre>
 * 
 *  File: DictRegionResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月5日				Administrator
 *
 * </pre>
 */
public class DictRegionResDto {

	private Integer regionId;
	private Integer parentId;
	private String regionName;
	private Integer regionType;

	List<DictRegionResDto> childrenRegions;

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getRegionType() {
		return regionType;
	}

	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}

	public List<DictRegionResDto> getChildrenRegions() {
		return childrenRegions;
	}

	public void setChildrenRegions(List<DictRegionResDto> childrenRegions) {
		this.childrenRegions = childrenRegions;
	}
}
