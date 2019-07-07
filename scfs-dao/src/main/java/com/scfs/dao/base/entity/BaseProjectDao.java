package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.domain.project.dto.req.ProjectSearchReqDto;
import com.scfs.domain.project.dto.req.UserProjectReqDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

public interface BaseProjectDao {

	/**
	 * 查询所有项目,用于缓存
	 * 
	 * @return
	 */
	List<BaseProject> queryAllProject(@Param("updateAt") String updateAt);

	int queryProjectByProNo(@Param("projectNo") String projectNo);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 物理删除项目
	 * 
	 * @return
	 */
	int deleteById(BaseProject baseProject);

	/**
	 * 更新项目数据
	 * 
	 * @return
	 */
	int updateById(BaseProject baseProject);

	/**
	 * 新建项目
	 * 
	 * @return
	 */
	int insert(BaseProject baseProject);

	/**
	 * 根据Id查询单条结果
	 * 
	 * @return
	 */
	BaseProject queryEntityById(Integer id);

	/**
	 * 根据Id锁定单条结果
	 * 
	 * @return
	 */
	BaseProject lockEntityById(Integer id);

	/**
	 * 根据条件分页查询出多条结果
	 * 
	 * @return
	 */
	List<BaseProject> queryProjectResultsByCon(ProjectSearchReqDto projectReqDto, RowBounds rowBounds);

	List<BaseProject> queryProjectResultsByCon(ProjectSearchReqDto projectReqDto);

	/**
	 * 根据条件分页查询当前登录用户的项目
	 * 
	 * @return
	 */
	List<BaseProject> queryUserProjectResultsByCon(ProjectSearchReqDto projectReqDto, RowBounds rowBounds);

	List<BaseProject> queryUserProjectResultsByCon(ProjectSearchReqDto projectReqDto);

	/**
	 * 查询分配给用户的项目
	 * 
	 * @param userId
	 * @return
	 */
	List<BaseUserProject> queryUserProjectAssignedToUser(Integer userId, RowBounds rowBounds);

	/**
	 * 查询分配给用户的项目
	 * 
	 * @param userId
	 * @return
	 */
	List<BaseUserProject> queryUserProjectAssignedToUserById(Integer id);

	/**
	 * 查询未分配给用户的项目
	 * 
	 * @param userId
	 * @return
	 */
	List<BaseProject> queryProjectNotAssignedToUser(UserProjectReqDto userId, RowBounds rowBounds);

	/**
	 * 根据经营单位查询项目
	 * 
	 * @param busiUnitIdList
	 * @return
	 */
	List<BaseProject> queryProjectByBusiUnit(@Param("userId") Integer userId,
			@Param("busiUnitIdList") List<String> busiUnitIdList);

	/**
	 * 获取总行数
	 * 
	 * @param projectReqDto
	 * @return
	 */
	int isOverasyncMaxLine(ProjectSearchReqDto projectReqDto);

	int isOverasyncMaxUserLine(ProjectSearchReqDto projectReqDto);

	// 通过部门找项目
	List<BaseProject> queryProjectByDepartmentId(UserProjectReqDto userProjectReqDto);
}