package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseRolePermissionGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BaseRolePermissionGroupDao {

	/**
	 * 查询所有，用于缓存
	 * 
	 * @return
	 */
	List<BaseRolePermissionGroup> queryAll(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 新增角色权限组
	 * 
	 * @param baseRolePermissionGroup
	 * @return
	 */
	int insert(BaseRolePermissionGroup baseRolePermissionGroup);

	// /**
	// * 批量新增角色权限组
	// * @param baseRolePermissionGroupList
	// * @return
	// */
	// int batchInsert(List<BaseRolePermissionGroup>
	// baseRolePermissionGroupList);

	/**
	 * 校验是否已经存在，防止重复插入
	 * 
	 * @param baseRolePermissionGroup
	 * @return
	 */
	BaseRolePermissionGroup queryRolePermissionGroup(BaseRolePermissionGroup baseRolePermissionGroup);

	/**
	 * 作废角色与权限组的关系
	 * 
	 * @param baseRolePermissionGroup
	 * @return
	 */
	int invalidRolePermissionGroup(BaseRolePermissionGroup baseRolePermissionGroup);

	// /**
	// * 批量作废角色与权限组的关系
	// * @param baseRolePermissionGroupList
	// * @return
	// */
	// int batchUpdate(List<BaseRolePermissionGroup>
	// baseRolePermissionGroupList);

	BaseRolePermissionGroup queryRolePermissionGroupById(int id);
}
