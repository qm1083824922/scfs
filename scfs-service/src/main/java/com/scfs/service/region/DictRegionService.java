package com.scfs.service.region;

import java.util.List;

import com.scfs.domain.base.region.dto.resp.DictRegionResDto;

/**
 * <pre>
 * 
 *  File: DictRegionService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月5日				Administrator
 *
 * </pre>
 */
public interface DictRegionService {
	public List<DictRegionResDto> queryRegions();

	public DictRegionResDto queryRegionsById(Integer regionId);
}
