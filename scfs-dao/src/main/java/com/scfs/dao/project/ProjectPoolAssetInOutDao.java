package com.scfs.dao.project;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.domain.project.entity.ProjectPoolAssetInOut;
import com.scfs.domain.project.entity.ProjectPoolFundInOut;

public interface ProjectPoolAssetInOutDao {
	int deleteById(Integer id);

	int insert(ProjectPoolAssetInOut projectPoolAssetInOut);

	List<ProjectPoolFundInOut> selectByCon(ProjectPoolAssetInOut projectPoolAssetInOut, RowBounds rowBounds);

	int updateById(ProjectPoolAsset projectPoolAssert);

}
