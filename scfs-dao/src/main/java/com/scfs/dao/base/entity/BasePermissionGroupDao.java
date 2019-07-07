package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.entity.BasePermissionGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BasePermissionGroupDao {

	/**
	 * 查询所有权限组，用于缓存
	 * 
	 * @return
	 */
	List<BasePermissionGroup> queryAll(@Param("updateAt") String updateAt);

	List<BasePermissionGroup> queryPermissionsListByPer(BasePermissionReqDto basePermissionReqDto, RowBounds rowBounds);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 查询权限组信息
	 * 
	 * @param basePermissionGroupReqDto
	 * @return
	 */
	List<BasePermissionGroup> queryPermissionsList(BasePermissionGroupReqDto basePermissionGroupReqDto,
			RowBounds rowBounds);

	/**
	 * 根据权限组ID查询权限组信息
	 * 
	 * @param id
	 * @return
	 */
	BasePermissionGroup queryAndLockById(@Param(value = "id") Integer id);

	/**
	 * 新增权限组
	 * 
	 * @param basePermissionGroup
	 * @return
	 */
	int insert(BasePermissionGroup basePermissionGroup);

	/**
	 * 更新权限组
	 * 
	 * @param basePermissionGroup
	 * @return
	 */
	int update(BasePermissionGroup basePermissionGroup);

	/**
	 * 根据权角色ID查询已经分配的权限组列表
	 * 
	 * @param roleId
	 * @param rowBounds
	 * @return
	 */
	List<BasePermissionGroup> queryPermissionGroupByRoleId(@Param(value = "roleId") Integer roleId,
			RowBounds rowBounds);

	/**
	 * 查询 不在此角色的权限组
	 * 
	 * @param basePermissionGroupReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BasePermissionGroup> queryPermissionGroupNotInRoleId(BasePermissionGroupReqDto basePermissionGroupReqDto,
			RowBounds rowBounds);
}
