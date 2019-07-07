package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BasePermissionRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BasePermissionRelationDao {

	/**
	 * 查询所有，用于缓存
	 * 
	 * @return
	 */
	List<BasePermissionRelation> queryAll(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 新增权限组与权限关联
	 * 
	 * @param basePermissionRelation
	 * @return
	 */
	int insert(BasePermissionRelation basePermissionRelation);

	/**
	 * 作废权限组与权限的关系
	 * 
	 * @param basePermissionRelation
	 * @return
	 */
	int invalidPermissionRelationById(BasePermissionRelation basePermissionRelation);

	/**
	 * 根据权限组ID与权限ID查询是否已经分配
	 * 
	 * @param basePermissionRelation
	 * @return
	 */
	BasePermissionRelation queryPermissionRelation(BasePermissionRelation basePermissionRelation);

	BasePermissionRelation queryPermissionRelationByCon(BasePermissionRelation basePermissionRelation);
}
