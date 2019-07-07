package com.scfs.dao.base.entity;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.base.dto.req.MatterManageReqDto;
import com.scfs.domain.base.entity.MatterManage;

@Repository
public interface MatterManageDao {
	/**
	 * 获取分组数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<MatterManage> queryResultsByCon(MatterManageReqDto reqDto, RowBounds rowBounds);

	/**
	 * 获取所有数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<MatterManage> queryResultsByCon(MatterManageReqDto reqDto);

	MatterManage queryEntityById(Integer id);

	/**
	 * 添加数据
	 * 
	 * @param matterManage
	 * @return
	 */
	int insert(MatterManage matterManage);

	/**
	 * 修改数据
	 * 
	 * @param matterManage
	 * @return
	 */
	int updateById(MatterManage matterManage);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);
}
