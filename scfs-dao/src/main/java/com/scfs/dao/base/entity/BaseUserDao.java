package com.scfs.dao.base.entity;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.project.dto.req.ProjectSearchReqDto;

public interface BaseUserDao {

	/**
	 * 查询所有用户，用户缓存
	 * 
	 * @return
	 */
	List<BaseUser> queryAllUser(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	List<BaseUser> queryBaseUserList(BaseUser baseUser, RowBounds rowBounds);

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	List<BaseUser> queryBaseUserList(BaseUser baseUser);

	/**
	 * 查询权限组用户列表
	 * 
	 * @param basePermissionGroupReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BaseUser> queryUserByPermissionGroup(BasePermissionGroupReqDto basePermissionGroupReqDto, RowBounds rowBounds);

	/**
	 * 根据用户信息查询用户
	 * 
	 * @param baseUser
	 * @return
	 */
	BaseUser queryBaseUserByUser(BaseUser baseUser);

	/**
	 * 根据ID查询用户
	 * 
	 * @param id
	 * @return
	 */
	BaseUser queryBaseUserById(int id);

	/**
	 * 新增用户
	 * 
	 * @param baseUser
	 * @return
	 */
	int insert(BaseUser baseUser);

	/**
	 * 更新用户
	 * 
	 * @param baseUser
	 * @return
	 */
	int update(BaseUser baseUser);

	/**
	 * 更新内部用户信息
	 * 
	 * @param baseUser
	 * @return
	 */
	int updateInnerUser(BaseUser baseUser);

	/**
	 * 提交操作
	 * 
	 * @param baseUser
	 * @return
	 */
	int submit(BaseUser baseUser);

	/**
	 * 锁定操作
	 * 
	 * @param baseUser
	 * @return
	 */
	int lock(BaseUser baseUser);

	/**
	 * 解锁操作
	 * 
	 * @param baseUser
	 * @return
	 */
	int unlock(BaseUser baseUser);

	/**
	 * 删除操作
	 * 
	 * @param baseUser
	 * @return
	 */
	int delete(BaseUser baseUser);

	/**
	 * 根据角色名称查询用户列表
	 * 
	 * @param roleName
	 * @return
	 */
	List<BaseUser> queryUserListByRoleName(@Param("roleName") String roleName);

	/**
	 * 根据角色id查询用户列表
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	List<BaseUser> queryBaseUserListByRoleId(BaseUserReqDto baseUserReqDto, RowBounds rowBounds);

	/**
	 * 根据项目id查询用户列表
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	List<BaseUser> queryBaseUserListByPorjectId(ProjectSearchReqDto projectSearchReqDto, RowBounds rowBounds);

	/**
	 * 根据项目id查询未分配用户列表
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	List<BaseUser> queryUndivideUserByPorjectId(BaseUserReqDto baseUserReqDto, RowBounds rowBounds);

	/**
	 * 根据主体id查询用户列表
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	List<BaseUser> queryBaseUserListBySubjectId(BaseUserReqDto baseUserReqDto, RowBounds rowBounds);

	/**
	 * 根据主体id查询未分配用户列表
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	List<BaseUser> queryUndivideUserBySubjectId(BaseUserReqDto baseUserReqDto, RowBounds rowBounds);

	/**
	 * 通过部门ID和角色名称查询数据
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	List<BaseUser> queryUserListByRoleNameAndDepId(BaseUserReqDto baseUserReqDto);
}
