
package com.scfs.dao.project;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.dto.req.ProjectPoolAdjustSearchReqDto;
import com.scfs.domain.project.entity.ProjectPoolAdjust;

public interface ProjectPoolAdjustDao {

	int deleteById(Integer id);

	int insert(ProjectPoolAdjust record);

	ProjectPoolAdjust queryEntityById(Integer id);

	int updateById(ProjectPoolAdjust record);

	List<ProjectPoolAdjust> queryResultsByCon(ProjectPoolAdjustSearchReqDto req, RowBounds rowBounds);

	List<ProjectPoolAdjust> queryResultsByCon(ProjectPoolAdjustSearchReqDto req);

}