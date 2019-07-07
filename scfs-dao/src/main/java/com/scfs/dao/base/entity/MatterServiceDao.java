package com.scfs.dao.base.entity;

import org.springframework.stereotype.Repository;
import com.scfs.domain.base.entity.MatterService;

@Repository
public interface MatterServiceDao {
	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	MatterService queryEntityById(Integer matterId);

	/**
	 * 添加数据
	 * 
	 * @param matterService
	 * @return
	 */
	int insert(MatterService matterService);

	/**
	 * 修改数据
	 * 
	 * @param matterService
	 * @return
	 */
	int updateById(MatterService matterService);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);
}
