package com.scfs.dao.base.entity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.base.entity.CustomerFollow;

@Repository
public interface CustomerFollowDao {
	/**
	 * 获取所有数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<CustomerFollow> queryResultsByCon(CustomerFollow customerFollow);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	CustomerFollow queryEntityById(Integer id);

	CustomerFollow queryEntityByNotin(CustomerFollow customerFollow);

	CustomerFollow queryEntityByMaxUpdate(Integer custId);

	/**
	 * 添加数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	int insert(CustomerFollow customerFollow);

	/**
	 * 修改数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	int updateById(CustomerFollow customerFollow);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);
}
