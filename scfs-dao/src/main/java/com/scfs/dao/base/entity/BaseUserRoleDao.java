package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseUserRoles;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BaseUserRoleDao {

	/**
	 * 查询所有用户角色 用于缓存
	 * 
	 * @return
	 */
	List<BaseUserRoles> queryAll(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 分配用户角色
	 * 
	 * @param baseUserRole
	 * @return
	 */
	int insertUserRole(BaseUserRoles baseUserRole);

	/**
	 * 更新用户角色
	 * 
	 * @param baseUserRole
	 * @return
	 */
	int updateUserRole(BaseUserRoles baseUserRole);

	/**
	 * 更新用户角色
	 * 
	 * @param baseUserRoleList
	 * @return
	 */
	int batchUpdateUserRole(List<BaseUserRoles> baseUserRoleList);

	BaseUserRoles queryUserRoleById(int id);

	BaseUserRoles queryUserRoleByCon(BaseUserRoles baseUserRole);
}
