package com.scfs.domain.base.entity;

import java.io.Serializable;

/**
 * <pre>
 * 
 *  File: DictRegion.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月5日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class DictRegion implements Serializable {
	private Integer regionId;
	private Integer parentId;
	private String regionName;
	private Integer regionType;

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

}
