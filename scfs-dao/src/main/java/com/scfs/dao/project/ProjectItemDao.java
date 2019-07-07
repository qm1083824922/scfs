package com.scfs.dao.project;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.dto.req.ProjectItemSearchReqDto;
import com.scfs.domain.project.entity.ProjectItem;

public interface ProjectItemDao {

	int deleteById(ProjectItem projectItem);

	int updateById(ProjectItem projectItem);

	int insert(ProjectItem projectItem);

	ProjectItem queryEntityById(Integer id);

	List<ProjectItem> queryProjectItemResultsByCon(ProjectItemSearchReqDto projectReqDto, RowBounds rowBounds);

	List<ProjectItem> queryProjectItemResultsByCon(ProjectItemSearchReqDto projectReqDto);

	List<ProjectItem> queryProjectItemByProjectId(ProjectItemSearchReqDto projectReqDto);

	List<ProjectItem> queryAllProjectItem(@Param("updateAt") String updateAt);

	List<ProjectItem> queryProjectItemResultsByMutCon(ProjectItemSearchReqDto projectReqDto);

	int updateStatusById(ProjectItem projectItem);

	List<ProjectItem> sumPoTitle(ProjectItemSearchReqDto projectReqDto);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();
}