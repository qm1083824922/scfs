package com.scfs.dao.base.entity;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.base.dto.req.CustomerMaintainReqDto;
import com.scfs.domain.base.entity.CustomerMaintain;

@Repository
public interface CustomerMaintainDao {
	/**
	 * 获取分组数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<CustomerMaintain> queryResultsByCon(CustomerMaintainReqDto reqDto, RowBounds rowBounds);

	/**
	 * 获取所有数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<CustomerMaintain> queryResultsByCon(CustomerMaintainReqDto reqDto);

	CustomerMaintain queryEntityById(Integer id);

	/**
	 * 添加数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	int insert(CustomerMaintain customerMaintain);

	/**
	 * 修改数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	int updateById(CustomerMaintain customerMaintain);

	int updateStateById(CustomerMaintain customerMaintain);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);
}
