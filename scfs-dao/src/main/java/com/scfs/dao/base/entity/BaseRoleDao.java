package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BaseRoleReqDto;
import com.scfs.domain.base.dto.req.BaseUserRoleReqDto;
import com.scfs.domain.base.entity.BaseRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BaseRoleDao {

	/**
	 * 查询所有，用于缓存
	 * 
	 * @return
	 */
	List<BaseRole> queryAll(@Param("updateAt") String updateAt);

	List<BaseRole> queryRoleByPermissionGroup(BasePermissionGroupReqDto basePermissionReqDto, RowBounds rowBounds);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 新增角色
	 * 
	 * @param baseRoles
	 * @return
	 */
	int insert(BaseRole baseRoles);

	/**
	 * 跟新角色
	 * 
	 * @param baseRole
	 * @return
	 */
	int update(BaseRole baseRole);

	/**
	 * 根据角色ID查询角色信息
	 * 
	 * @param id
	 * @return
	 */
	BaseRole queryBaseRoleById(int id);

	/**
	 * 根据用户名查询角色信息
	 * 
	 * @param userName
	 * @return
	 */
	List<BaseRole> queryBaseRoleListByUserName(String userName);

	/**
	 * 根据条件分页查询角色信息
	 * 
	 * @param baseRoleReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BaseRole> queryBaseRoleList(BaseRoleReqDto baseRoleReqDto, RowBounds rowBounds);

	/**
	 * 查询分配给用户的角色
	 * 
	 * @param userId
	 * @return
	 */
	List<BaseRole> queryDividRoleByUserId(@Param(value = "userId") Integer userId, RowBounds rowBounds);

	/**
	 * 查询未分配给用户的角色
	 * 
	 * @param userId
	 * @return
	 */
	List<BaseRole> queryUnDividRoleByUserId(BaseUserRoleReqDto userId, RowBounds rowBounds);

	/**
	 * 通过用户角色获取信息
	 * 
	 * @param reqDto
	 * @return
	 */
	List<BaseRole> queryRoleByUserByRole(BaseUserRoleReqDto reqDto);
}
