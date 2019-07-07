package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseUserProject;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BaseUserProjectDao {

	/**
	 * 查询所有用户项目，用于缓存
	 * 
	 * @return
	 */
	List<BaseUserProject> queryAllUserProject(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 分配用户项目
	 * 
	 * @param baseUserProject
	 * @return
	 */
	int insertUserProject(BaseUserProject baseUserProject);

	/**
	 * 更新用户项目
	 * 
	 * @param baseUserProject
	 * @return
	 */
	int updateUserProject(BaseUserProject baseUserProject);

	/**
	 * 批量更新用户项目
	 * 
	 * @param baseUserProjectList
	 * @return
	 */
	int batchUpdateUserProject(List<BaseUserProject> baseUserProjectList);

	BaseUserProject queryUserProjectById(int id);

	List<BaseUserProject> queryUserProjectByCon(BaseUserProject baseUserProject);
}
