package com.scfs.dao.project;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.entity.ProjectSubject;

public interface ProjectSubjectDao {

	/**
	 * 查询项目主体列表
	 * 
	 * @param projectId
	 * @param subjectType
	 * @param rowBounds
	 * @return
	 */
	List<ProjectSubject> queryResultsByCon(ProjectSubjectSearchReqDto reqDto, RowBounds rowBounds);

	List<ProjectSubject> queryResultsByCon(ProjectSubjectSearchReqDto reqDto);

	/**
	 * 根据主键查询信息
	 */
	ProjectSubject loadAndLockEntityById(@Param(value = "id") int id);

	/**
	 * 根据项目和主体ID查询是否存在
	 */
	ProjectSubject queryIsExistByProjectSub(ProjectSubject projectSubject);

	/**
	 * 更新项目主体
	 * 
	 * @param projectSubject
	 * @return
	 */
	int updateById(ProjectSubject projectSubject);

	/**
	 * 插入项目主体
	 * 
	 * @param projectSubject
	 * @return
	 */
	int insert(ProjectSubject projectSubject);

	/**
	 * 查询未分配给项目的主体
	 * 
	 * @param projectId
	 * @param subjectType
	 * @param rowBounds
	 * @return
	 */
	List<BaseSubject> querySubjectToProjectByCon(ProjectSubjectSearchReqDto reqDto, RowBounds rowBounds);

	/**
	 * 根据主题类型查询所有可能的项目与主题关系
	 * 
	 * @param updateAt
	 * @return
	 */
	List<ProjectSubject> queryAllProjectSub(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

}
