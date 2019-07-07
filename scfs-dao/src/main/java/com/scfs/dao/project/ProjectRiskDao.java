package com.scfs.dao.project;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.project.dto.req.ProjectRiskSearchReqDto;
import com.scfs.domain.project.entity.ProjectRisk;

@Repository
public interface ProjectRiskDao {
	/**
	 * 添加数据
	 * 
	 * @param projectRisk
	 * @return
	 */
	int insert(ProjectRisk projectRisk);

	/**
	 * 修改数据
	 * 
	 * @param projectRisk
	 * @return
	 */
	int updateById(ProjectRisk projectRisk);

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	ProjectRisk queryEntityById(int id);

	/**
	 * 分页查询
	 * 
	 * @param projectRiskSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<ProjectRisk> queryResultsByCon(ProjectRiskSearchReqDto projectRiskSearchReqDto, RowBounds rowBounds);
}
