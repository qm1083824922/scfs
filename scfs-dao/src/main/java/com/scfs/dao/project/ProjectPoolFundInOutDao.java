package com.scfs.dao.project;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.entity.ProjectPoolFundInOut;

public interface ProjectPoolFundInOutDao {
	int deleteById(Integer id);

	int insert(ProjectPoolFundInOut record);

	List<ProjectPoolFundInOut> selectByCon(ProjectPoolFundInOut projectPoolFundInOut, RowBounds rowBounds);

	int updateById(ProjectPoolFundInOut record);
}