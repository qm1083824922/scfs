package com.scfs.dao.project;

import java.util.List;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.dto.req.ProjectPoolSearchReqDto;
import com.scfs.domain.project.entity.ProjectPool;

/**
 * 池管理
 * 
 * @author Administrator
 *
 */
public interface ProjectPoolDao {

	/**
	 * 插入一条信息
	 * 
	 * @param id
	 * @return
	 */

	int insert(ProjectPool record);

	/**
	 * 根据Id查询单条结果
	 * 
	 * @return
	 */
	ProjectPool selectById(Integer id);

	/**
	 * 根据Id更新单条结果
	 * 
	 * @param id
	 * @return
	 */
	int updateById(ProjectPool record);

	/**
	 * 根据条件分页查询出多条结果
	 * 
	 * @return
	 */
	List<ProjectPool> queryProjectPoolResultsByCon(ProjectPoolSearchReqDto projectReqDto, RowBounds rowBounds);

	/**
	 * 根据条件分页查询出多条结果
	 * 
	 * @return
	 */
	List<ProjectPool> queryProjectPoolResultsByCon(ProjectPoolSearchReqDto projectReqDto);

	ProjectPool queryProjectPoolByProjectId(Integer projectId);

	ProjectPool queryFundSumByProjectId(Integer projectId);

	ProjectPool queryAssertSumByProjectId(Integer projectId);

	int queryProjectPoolDtlsCountByProjectId(Integer projectId);

	int deleteExculdeOfSunyou();

	/**
	 * 获取资金池统计数据 (用于首页 2017-08-10)
	 * 
	 * @return
	 */
	List<ProjectPool> querySumProjectPool();
}