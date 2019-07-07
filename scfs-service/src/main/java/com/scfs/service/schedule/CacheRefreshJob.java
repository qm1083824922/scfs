package com.scfs.service.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.service.support.InitCacheService;

/**
 * Created by Administrator on 2016/11/23.
 */
@Service
public class CacheRefreshJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheRefreshJob.class);

	@Autowired
	private InitCacheService initCacheService;

	@IgnoreTransactionalMark
	public void refreshCache() {
		long begin = System.currentTimeMillis();
		LOGGER.info("刷新缓存开始......");

		try {
			initCacheService.refreshBizConstant();// 常量类
			initCacheService.refreshFeeSpec();// 费用
			initCacheService.refreshProject();// 项目,项目-经营单位,经营单位---项目
			initCacheService.refreshGoods();// 商品
			initCacheService.refreshUser();// 用户
			initCacheService.refreshRole();// 角色
			initCacheService.refreshPermissionGroup();// 权限组
			initCacheService.refreshPermission();// 权限
			initCacheService.refreshDepartment();// 部门
			initCacheService.refreshProjectItem();
			initCacheService.refreshAccount();
			initCacheService.refreshSubject();// 客户 供应商 经营单位 仓库

			initCacheService.refreshProjectItem();// 项目条款
			initCacheService.refreshAccount();// 主体下的账号
			initCacheService.refreshAddress();// 主体下的地址
			initCacheService.refreshSubjectInvoice();// 主体下的开票

			initCacheService.refreshProjectSub();// 项目-客户 供应商 仓库

			initCacheService.refreshProjectGoods();// 项目-商品

			initCacheService.refreshUserProject();// 用户下---项目
			initCacheService.refreshUserRoles();// 用户下---角色
			initCacheService.refreshRolePermissionGroup();// 角色---权限组
			initCacheService.refreshPermGroupPerm();// 权限组--权限

			initCacheService.refreshAccountBook();// 帐套
			initCacheService.refreshAccountLine();// 帐套下的科目
			initCacheService.refreshUserPerms();// 用户权限缓存
		} catch (Exception e) {
			LOGGER.error("刷新缓存异常：", e);
		}
		LOGGER.info("刷新缓存结束,耗时:" + (System.currentTimeMillis() - begin) / 1000);
	}

}
