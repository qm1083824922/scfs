package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.entity.BasePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
@Repository
public interface BasePermissionDao {

	/**
	 * 查询所有权限，用于缓存
	 * 
	 * @return
	 */
	List<BasePermission> queryAll(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 新增权限
	 * 
	 * @param basePermission
	 * @return
	 */
	int insert(BasePermission basePermission);

	/**
	 * 更新权限信息
	 * 
	 * @param basePermission
	 * @return
	 */
	int update(BasePermission basePermission);

	/**
	 * 分页查询权限信息
	 * 
	 * @param basePermissionReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BasePermission> queryPermissions(BasePermissionReqDto basePermissionReqDto, RowBounds rowBounds);

	/**
	 * 查询所有权限信息
	 * 
	 * @param basePermissionReqDto
	 * @return
	 */
	List<BasePermission> queryAllPermission(BasePermissionReqDto basePermissionReqDto);

	/**
	 * 根据权限ID查询权限信息
	 * 
	 * @param id
	 * @return
	 */
	BasePermission queryPermissionById(int id);

	/**
	 * 根据权限组ID查询已经分配的权限列表
	 * 
	 * @param id
	 * @param rowBounds
	 * @return
	 */
	List<BasePermission> queryPermissionsByGroupId(@Param(value = "id") Integer id, RowBounds rowBounds);

	/**
	 * 查询权限列表不在此权限组
	 * 
	 * @param basePermissionReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BasePermission> queryPermissionsNotInGroupId(BasePermissionReqDto basePermissionReqDto, RowBounds rowBounds);

	/**
	 * 查询所有的一级菜单
	 * 
	 * @return
	 */
	List<BasePermission> queryFisrtPermission();

	/**
	 * 分页查询权限信息
	 * 
	 * @param basePermissionReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BasePermission> queryPermissionByRoleId(BasePermissionReqDto basePermissionReqDto, RowBounds rowBounds);
}
