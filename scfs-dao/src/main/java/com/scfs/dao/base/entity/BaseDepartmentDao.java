package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.BaseDepartmentReqDto;
import com.scfs.domain.base.entity.BaseDepartment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

public interface BaseDepartmentDao {
	int deleteById(Integer id);

	int insert(BaseDepartment record);

	BaseDepartment selectById(Integer id);

	List<BaseDepartment> queryByNumberAndName(BaseDepartment baseDepartment);

	int updateById(BaseDepartment record);

	List<BaseDepartment> queryDepartMentByCon(BaseDepartmentReqDto BaseDepartmentReqDto, RowBounds rowBounds);

	List<BaseDepartment> queryDepartMentByParentId(Integer parentId);

	/**
	 * 查询所有权限，用于缓存
	 * 
	 * @return
	 */
	List<BaseDepartment> queryAll(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 通过参数获取部门
	 * 
	 * @param baseDepartment
	 * @return
	 */
	BaseDepartment queryEntityParam(BaseDepartment baseDepartment);

}