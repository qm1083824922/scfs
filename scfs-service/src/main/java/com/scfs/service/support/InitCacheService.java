package com.scfs.service.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.BizConstantDao;
import com.scfs.dao.DictRegionDao;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.base.entity.BaseDepartmentDao;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.base.entity.BaseInvoiceDao;
import com.scfs.dao.base.entity.BasePermissionDao;
import com.scfs.dao.base.entity.BasePermissionGroupDao;
import com.scfs.dao.base.entity.BasePermissionRelationDao;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.base.entity.BaseRoleDao;
import com.scfs.dao.base.entity.BaseRolePermissionGroupDao;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.base.entity.BaseUserDao;
import com.scfs.dao.base.entity.BaseUserProjectDao;
import com.scfs.dao.base.entity.BaseUserRoleDao;
import com.scfs.dao.base.entity.DistributionGoodsDao;
import com.scfs.dao.fee.FeeSpecDao;
import com.scfs.dao.fi.AccountBookDao;
import com.scfs.dao.fi.AccountLineDao;
import com.scfs.dao.project.ProjectGoodsDao;
import com.scfs.dao.project.ProjectItemDao;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.domain.base.entity.BasePermissionGroup;
import com.scfs.domain.base.entity.BasePermissionRelation;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseRolePermissionGroup;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.domain.base.entity.BaseUserRoles;
import com.scfs.domain.base.entity.BizConstant;
import com.scfs.domain.base.entity.DictRegion;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.fee.dto.req.FeeSpecSearchReqDto;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.rpc.cache.ObjectRedisTemplate;

/**
 * Created by Administrator on 2016/10/25.
 */
public class InitCacheService {

	private final static Logger LOGGER = LoggerFactory.getLogger(InitCacheService.class);
	@Value("${admin.name}")
	private String adminName;

	@Autowired
	private CacheService cacheService;
	@Autowired
	private ObjectRedisTemplate objectRedisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private BaseSubjectDao baseSubjectDao;
	@Autowired
	private BaseProjectDao baseProjectDao;
	@Autowired
	private BaseGoodsDao baseGoodsDao;
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private BaseRoleDao baseRoleDao;

	@Autowired
	private BaseUserRoleDao baseUserRoleDao;
	@Autowired
	private BasePermissionGroupDao basePermissionGroupDao;
	@Autowired
	private BaseRolePermissionGroupDao baseRolePermissionGroupDao;
	@Autowired
	private BasePermissionRelationDao basePermissionRelationDao;
	@Autowired
	private AccountBookDao accountBookDao;
	@Autowired
	private AccountLineDao accountLineDao;
	@Autowired
	private ProjectSubjectDao projectSubjectDao;
	@Autowired
	private ProjectGoodsDao projectGoodsDao;
	@Autowired
	private ProjectItemDao projectItemDao;
	@Autowired
	private BaseAccountDao baseAccountDao;
	@Autowired
	private BaseAddressDao baseAddressDao;
	@Autowired
	private BaseInvoiceDao baseInvoiceDao;
	@Autowired
	private DictRegionDao dictRegionDao;
	@Autowired
	private BaseUserProjectDao baseUserProjectDao;
	@Autowired
	private BasePermissionDao basePermissionDao;
	@Autowired
	private FeeSpecDao feeSpecDao;
	@Autowired
	private BizConstantDao bizConstantDao;
	@Autowired
	private BaseDepartmentDao baseDepartmentDao;
	@Autowired
	private DistributionGoodsDao distributionGoodsDao;// 铺货商品

	// 是否初始化数据
	private boolean initData;

	@PostConstruct
	public void initData() {
		if (initData) {
			long begin = System.currentTimeMillis();
			LOGGER.info("初始化缓存开始====.");

			initDictRegion();// 地址缓存
			initFeeSpec();// 费用
			initBizConstant();// 初始化常量

			initDepartment();// 部门

			initProject();// 项目,项目-经营单位,经营单位---项目
			initGoods();// 商品
			initUser();// 用户
			initRole();// 角色
			initPermissionGroup();// 权限组
			initPermission();// 权限
			initDistributionGoods();// 铺货商品

			initSubject();// 客户 供应商 经营单位 仓库
			initProjectItem();
			initAccount();// 主体下的账号
			initAddress();// 主体下的地址
			initSubjectInvoice();// 主体下的开票
			initProjectSub();// 项目-客户 供应商 仓库
			initProjectGoods();// 项目-商品
			initProjectUser();// 项目下---用户

			initUserProject();// 用户下---项目
			initUserRoles();// 用户下---角色
			initRolePermissionGroup();// 角色---权限组
			initPermGroupPerm();// 角色---权限组

			initAccountBook();// 帐套
			initAccountLine();// 帐套下的科目

			refreshUserPerms();// 组装用户权限缓存

			LOGGER.info("初始化缓存结束,耗时:" + (System.currentTimeMillis() - begin) / 1000);
		}
	}

	private void initDepartment() {
		objectRedisTemplate.delete(CacheKeyConsts.DEPARTMENT);
		stringRedisTemplate.delete(CacheKeyConsts.DEPARTMENT + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshDepartment();
	}

	public void refreshDepartment() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT);
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.DEPARTMENT + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseDepartment> departmentList = baseDepartmentDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(departmentList)) {
			LOGGER.info("刷新BaseDepartment表的条数：" + departmentList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BaseDepartment department : departmentList) {
				cacheHash.put(department.getId().toString(), department);
			}
			Date updateAt = baseDepartmentDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.DEPARTMENT + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initBizConstant() {
		objectRedisTemplate.delete(CacheKeyConsts.BIZ_CONSTANTS);
		objectRedisTemplate.delete(CacheKeyConsts.BIZ_CONSTANTS_PARENT);
		stringRedisTemplate.delete(CacheKeyConsts.BIZ_CONSTANTS + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshBizConstant();
	}

	public void refreshBizConstant() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS);
		BoundHashOperations constHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS_PARENT);

		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.BIZ_CONSTANTS + CacheKeyConsts.MAX_DATE_TIME);
		List<BizConstant> bizConstantList = bizConstantDao.queryAllBizConstant(lastUpdateAt);

		if (CollectionUtils.isNotEmpty(bizConstantList)) {
			LOGGER.info("刷新bizConstant表的条数：" + bizConstantList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BizConstant bizConstant : bizConstantList) {
				// 通过业务常量归类
				if (cacheHash.hasKey(bizConstant.getBizCode())) {
					List<CodeValue> bizList = (List<CodeValue>) cacheHash.get(bizConstant.getBizCode());
					CodeValue existCodeValue = null;
					for (CodeValue codeValue : bizList) {
						if (codeValue.getCode().equals(bizConstant.getCode())) {
							existCodeValue = codeValue;
							break;
						}
					}
					if (existCodeValue != null) {
						bizList.remove(existCodeValue);
					}
					if (bizConstant.getIsDelete() == BaseConsts.ZERO) {
						bizList.add(new CodeValue(bizConstant.getCode(), bizConstant.getValue()));
					}
					cacheHash.put(bizConstant.getBizCode(), bizList);
				} else {
					List<CodeValue> bizList = Lists.newArrayList();
					bizList.add(new CodeValue(bizConstant.getCode(), bizConstant.getValue()));
					cacheHash.put(bizConstant.getBizCode(), bizList);
				}
				// 上下级常量类
				if (bizConstant.getpBizCodeCode() != null) {
					if (constHash.hasKey(bizConstant.getpBizCodeCode())) {
						List<CodeValue> bizList = (List<CodeValue>) constHash.get(bizConstant.getpBizCodeCode());
						CodeValue existOldObj = null;
						for (CodeValue cv : bizList) {
							if (cv.getCode().equals(bizConstant.getCode())) {
								existOldObj = cv;
								break;
							}
						}
						if (existOldObj != null) {
							bizList.remove(existOldObj);
						}
						if (bizConstant.getIsDelete() == BaseConsts.ZERO) {
							bizList.add(new CodeValue(bizConstant.getCode(), bizConstant.getValue()));
						}
						constHash.put(bizConstant.getpBizCodeCode(), bizList);
					} else {
						List<CodeValue> bizList = Lists.newArrayList();
						bizList.add(new CodeValue(bizConstant.getCode(), bizConstant.getValue()));
						constHash.put(bizConstant.getpBizCodeCode(), bizList);
					}
				}
			}
			Date updateAt = bizConstantDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.BIZ_CONSTANTS + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initPermission() {
		objectRedisTemplate.delete(CacheKeyConsts.PERMISSIONS);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.PERMISSIONS + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshPermission();
	}

	public void refreshPermission() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PERMISSIONS);
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PERMISSIONS + CacheKeyConsts.MAX_DATE_TIME);
		List<BasePermission> permissionList = basePermissionDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(permissionList)) {
			LOGGER.info("刷新basePermission表的条数：" + permissionList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BasePermission permission : permissionList) {
				cacheHash.put(String.valueOf(permission.getId()), permission);
			}
			Date updateAt = basePermissionDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.PERMISSIONS + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initPermissionGroup() {
		objectRedisTemplate.delete(CacheKeyConsts.PERMISSION_GROUP);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.PERMISSION_GROUP + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshPermissionGroup();
	}

	public void refreshPermissionGroup() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PERMISSION_GROUP);
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PERMISSION_GROUP + CacheKeyConsts.MAX_DATE_TIME);
		List<BasePermissionGroup> permissionGroupList = basePermissionGroupDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(permissionGroupList)) {
			LOGGER.info("刷新basePermissionGroup表的条数：" + permissionGroupList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BasePermissionGroup permissionGroup : permissionGroupList) {
				cacheHash.put(String.valueOf(permissionGroup.getId()), permissionGroup);
			}
			Date updateAt = basePermissionGroupDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.PERMISSION_GROUP + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}

	}

	private void initRole() {
		objectRedisTemplate.delete(CacheKeyConsts.ROLES);// 缓存之前，先删除KEY，防止序列化不一致
		objectRedisTemplate.delete(CacheKeyConsts.ADMIN_ROLE);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.ROLES + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshRole();
	}

	public void refreshRole() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.ROLES + CacheKeyConsts.MAX_DATE_TIME);// 先看缓存中是否存在最后更新时间
		List<BaseRole> roleList = baseRoleDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(roleList)) {
			LOGGER.info("刷新baseRole表的条数：" + roleList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.ROLES);
			BoundHashOperations adminCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.ADMIN_ROLE);
			if (adminName == null || !"超级管理员".equalsIgnoreCase(adminName) || !"管理员".equalsIgnoreCase(adminName)) {
				adminName = "超级管理员";
			}
			LOGGER.info(",管理员名称：{}", adminName);
			for (BaseRole role : roleList) {
				cacheHash.put(String.valueOf(role.getId()), role);
				if (adminName.equalsIgnoreCase(role.getName())) {
					LOGGER.info("管理员：" + role.getId());
					adminCacheHash.put(String.valueOf(role.getId()), role);
				}
			}
			Date updateAt = baseRoleDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.ROLES + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}

	}

	private void initPermGroupPerm() {
		objectRedisTemplate.delete(CacheKeyConsts.PERMISSION_GROUP_PERMISSION);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.PERMISSION_GROUP_PERMISSION + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshPermGroupPerm();
	}

	public void refreshPermGroupPerm() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PERMISSION_GROUP_PERMISSION + CacheKeyConsts.MAX_DATE_TIME);
		List<BasePermissionRelation> prList = basePermissionRelationDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(prList)) {
			LOGGER.info("刷新basePermissionRelation表的条数：" + prList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate
					.boundHashOps(CacheKeyConsts.PERMISSION_GROUP_PERMISSION);
			for (BasePermissionRelation pr : prList) {
				Object obj = cacheHash.get(String.valueOf(pr.getPermissionGroupId()));
				if (obj != null) {// 存在 则直接加入
					List<BasePermissionRelation> urList = (List<BasePermissionRelation>) obj;

					BasePermissionRelation existOldObj = null;
					for (BasePermissionRelation permissionRelation : urList) {
						if (pr.getPermissionId().intValue() == permissionRelation.getPermissionId().intValue()) {
							existOldObj = permissionRelation;
							break;
						}
					}
					if (existOldObj != null) {
						urList.remove(existOldObj);
					}
					urList.add(pr);
					cacheHash.put(String.valueOf(pr.getPermissionGroupId()), urList);
				} else {
					List<BasePermissionRelation> urList = Lists.newArrayList();
					urList.add(pr);
					cacheHash.put(String.valueOf(pr.getPermissionGroupId()), urList);
				}
			}
			Date updateAt = basePermissionRelationDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(
						CacheKeyConsts.PERMISSION_GROUP_PERMISSION + CacheKeyConsts.MAX_DATE_TIME, modifyLastUpdate);
			}
		}
	}

	private void initRolePermissionGroup() {
		objectRedisTemplate.delete(CacheKeyConsts.ROLE_PERMISSION_GROUP);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.ROLE_PERMISSION_GROUP + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshRolePermissionGroup();
	}

	public void refreshRolePermissionGroup() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.ROLE_PERMISSION_GROUP + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseRolePermissionGroup> roleGroupList = baseRolePermissionGroupDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(roleGroupList)) {
			LOGGER.info("刷新baseRolePermissionGroup表的条数：" + roleGroupList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.ROLE_PERMISSION_GROUP);
			for (BaseRolePermissionGroup roleGroup : roleGroupList) {
				Object obj = cacheHash.get(String.valueOf(roleGroup.getRoleId()));
				if (obj != null) {// 存在 则直接加入
					List<BaseRolePermissionGroup> urList = (List<BaseRolePermissionGroup>) obj;
					BaseRolePermissionGroup existOldObj = null;
					for (BaseRolePermissionGroup rp : urList) {
						if (rp.getPermissionGroupId().intValue() == roleGroup.getPermissionGroupId().intValue()) {
							existOldObj = rp;
							break;
						}
					}
					if (existOldObj != null) {
						urList.remove(existOldObj);
					}
					urList.add(roleGroup);
					cacheHash.put(String.valueOf(roleGroup.getRoleId()), urList);
				} else {
					List<BaseRolePermissionGroup> urList = Lists.newArrayList();
					urList.add(roleGroup);
					cacheHash.put(String.valueOf(roleGroup.getRoleId()), urList);
				}
			}
			Date updateAt = baseRolePermissionGroupDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue()
						.set(CacheKeyConsts.ROLE_PERMISSION_GROUP + CacheKeyConsts.MAX_DATE_TIME, modifyLastUpdate);
			}
		}
	}

	private void initUserRoles() {
		objectRedisTemplate.delete(CacheKeyConsts.USER_ROLES);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.USER_ROLES + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshUserRoles();
	}

	public void refreshUserRoles() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.USER_ROLES + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseUserRoles> useRoleList = baseUserRoleDao.queryAll(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(useRoleList)) {
			LOGGER.info("刷新baseUserRole表的条数：" + useRoleList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_ROLES);
			for (BaseUserRoles userRole : useRoleList) {
				Object obj = cacheHash.get(String.valueOf(userRole.getUserId()));
				if (obj != null) {// 存在 则直接加入
					List<BaseUserRoles> urList = (List<BaseUserRoles>) obj;
					BaseUserRoles existOldObj = null;
					for (BaseUserRoles baseUserRoles : urList) {
						if (userRole.getRoleId().intValue() == baseUserRoles.getRoleId().intValue()) {
							existOldObj = baseUserRoles;
							break;
						}
					}
					if (existOldObj != null) {
						urList.remove(existOldObj);
					}
					urList.add(userRole);
					cacheHash.put(String.valueOf(userRole.getUserId()), urList);
				} else {
					List<BaseUserRoles> urList = Lists.newArrayList();
					urList.add(userRole);
					cacheHash.put(String.valueOf(userRole.getUserId()), urList);
				}
			}
			Date updateAt = baseUserRoleDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.USER_ROLES + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}

		}
	}

	private void initFeeSpec() {
		objectRedisTemplate.delete(CacheKeyConsts.PAY_FEE_SPEC);
		objectRedisTemplate.delete(CacheKeyConsts.REC_FEE_SPEC);
		objectRedisTemplate.delete(CacheKeyConsts.REC_PAY_FEE_SPEC);
		objectRedisTemplate.delete(CacheKeyConsts.PAY_FEE_MANAGE);
		objectRedisTemplate.delete(CacheKeyConsts.PAY_FEE_ARTIFICIAL);
		refreshFeeSpec();
	}

	public void refreshFeeSpec() {
		BoundHashOperations feeHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PAY_FEE_SPEC);
		FeeSpecSearchReqDto feeSpecSearchReqDto = new FeeSpecSearchReqDto();
		feeSpecSearchReqDto.setFeeType(BaseConsts.TWO);
		List<FeeSpec> feeSpecList = feeSpecDao.queryAllFeeSpec(feeSpecSearchReqDto);
		if (CollectionUtils.isNotEmpty(feeSpecList)) {
			for (FeeSpec fee : feeSpecList) {
				feeHash.put(String.valueOf(fee.getId()), fee);
			}
		}
		feeHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.REC_FEE_SPEC);
		feeSpecSearchReqDto.setFeeType(BaseConsts.ONE);
		feeSpecList = feeSpecDao.queryAllFeeSpec(feeSpecSearchReqDto);
		if (CollectionUtils.isNotEmpty(feeSpecList)) {
			for (FeeSpec fee : feeSpecList) {
				feeHash.put(String.valueOf(fee.getId()), fee);
			}
		}
		feeHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.REC_PAY_FEE_SPEC);
		feeSpecSearchReqDto.setFeeType(BaseConsts.THREE);
		feeSpecList = feeSpecDao.queryAllFeeSpec(feeSpecSearchReqDto);
		if (CollectionUtils.isNotEmpty(feeSpecList)) {
			for (FeeSpec fee : feeSpecList) {
				feeHash.put(String.valueOf(fee.getId()), fee);
			}
		}
		feeHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PAY_FEE_MANAGE);
		feeSpecSearchReqDto.setFeeType(BaseConsts.FOUR);
		feeSpecList = feeSpecDao.queryAllFeeSpec(feeSpecSearchReqDto);
		if (CollectionUtils.isNotEmpty(feeSpecList)) {
			for (FeeSpec fee : feeSpecList) {
				feeHash.put(String.valueOf(fee.getId()), fee);
			}
		}

		feeHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PAY_FEE_ARTIFICIAL);
		feeSpecSearchReqDto.setFeeType(BaseConsts.FIVE);
		feeSpecList = feeSpecDao.queryAllFeeSpec(feeSpecSearchReqDto);
		if (CollectionUtils.isNotEmpty(feeSpecList)) {
			for (FeeSpec fee : feeSpecList) {
				feeHash.put(String.valueOf(fee.getId()), fee);
			}
		}
	}

	private void initProjectItem() {
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_ITEM);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.PROJECT_ITEM + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshProjectItem();
	}

	public void refreshProjectItem() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PROJECT_ITEM + CacheKeyConsts.MAX_DATE_TIME);
		List<ProjectItem> projectItems = projectItemDao.queryAllProjectItem(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(projectItems)) {
			LOGGER.info("刷新ProjectItem表的条数：" + projectItems.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_ITEM);
			for (ProjectItem projectItem : projectItems) {
				String projectId = String.valueOf(projectItem.getProjectId());
				if (cacheHash.hasKey(projectId)) {
					List<ProjectItem> subList = (List<ProjectItem>) cacheHash.get(projectId);

					ProjectItem exsitProjectItem = null;
					for (ProjectItem proItem : subList) {
						if (proItem.getId().intValue() == projectItem.getId().intValue()) {
							exsitProjectItem = proItem;
							break;
						}
					}
					if (exsitProjectItem != null) {
						subList.remove(exsitProjectItem);
					}

					subList.add(projectItem);
					cacheHash.put(projectId, subList);
				} else {
					List<ProjectItem> cvList = Lists.newArrayList();
					cvList.add(projectItem);
					cacheHash.put(projectId, cvList);
				}
			}
		}
		Date updateAt = projectItemDao.queryLastUpdateAt();
		if (updateAt != null) {
			String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
			stringRedisTemplate.opsForValue().set(CacheKeyConsts.PROJECT_ITEM + CacheKeyConsts.MAX_DATE_TIME,
					modifyLastUpdate);
		}
	}

	private void initUserProject() {
		objectRedisTemplate.delete(CacheKeyConsts.USER_PROJECT);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.USER_PROJECT + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshUserProject();
	}

	public void refreshUserProject() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.USER_PROJECT + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseUserProject> userProjects = baseUserProjectDao.queryAllUserProject(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(userProjects)) {
			LOGGER.info("刷新baseUserProject表的条数：" + userProjects.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PROJECT);
			for (BaseUserProject up : userProjects) {
				String userId = String.valueOf(up.getUserId());
				if (cacheHash.hasKey(userId)) {
					List<BaseUserProject> subList = (List<BaseUserProject>) cacheHash.get(userId);

					BaseUserProject exsitUserProject = null;
					for (BaseUserProject baseUserProject : subList) {
						if (baseUserProject.getProjectId().intValue() == up.getProjectId().intValue()) {
							exsitUserProject = baseUserProject;
							break;
						}
					}
					if (exsitUserProject != null) {
						subList.remove(exsitUserProject);
					}

					subList.add(up);
					cacheHash.put(userId, subList);
				} else {
					List<BaseUserProject> cvList = Lists.newArrayList();
					cvList.add(up);
					cacheHash.put(userId, cvList);
				}
			}
		}
		Date updateAt = baseUserProjectDao.queryLastUpdateAt();
		if (updateAt != null) {
			String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
			stringRedisTemplate.opsForValue().set(CacheKeyConsts.USER_PROJECT + CacheKeyConsts.MAX_DATE_TIME,
					modifyLastUpdate);
		}
	}

	private void initProjectUser() {
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_USER);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.PROJECT_USER + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshProjectUser();
	}

	public void refreshProjectUser() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PROJECT_USER + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseUserProject> userProjects = baseUserProjectDao.queryAllUserProject(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(userProjects)) {
			LOGGER.info("刷新baseUserProject表的条数：" + userProjects.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_USER);
			for (BaseUserProject up : userProjects) {
				String projectId = String.valueOf(up.getProjectId());
				if (cacheHash.hasKey(projectId)) {
					List<BaseUserProject> subList = (List<BaseUserProject>) cacheHash.get(projectId);

					BaseUserProject exsitUserProject = null;
					for (BaseUserProject baseUserProject : subList) {
						if (baseUserProject.getUserId().intValue() == up.getUserId().intValue()) {
							exsitUserProject = baseUserProject;
							break;
						}
					}
					if (exsitUserProject != null) {
						subList.remove(exsitUserProject);
					}

					subList.add(up);
					cacheHash.put(projectId, subList);
				} else {
					List<BaseUserProject> cvList = Lists.newArrayList();
					cvList.add(up);
					cacheHash.put(projectId, cvList);
				}
			}
		}
		Date updateAt = baseUserProjectDao.queryLastUpdateAt();
		if (updateAt != null) {
			String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
			stringRedisTemplate.opsForValue().set(CacheKeyConsts.PROJECT_USER + CacheKeyConsts.MAX_DATE_TIME,
					modifyLastUpdate);
		}
	}

	private void initDictRegion() {
		List<DictRegion> provinces = Lists.newArrayList();
		List<DictRegion> citys = Lists.newArrayList();
		List<DictRegion> countys = Lists.newArrayList();
		List<DictRegion> allRegions = dictRegionDao.queryAllDictRegion();
		/**
		 * 地区类型 1:省 2:市 3:县
		 */
		List<DictRegion> rootRegions = Lists.newArrayList();
		Map<String, List<CodeValue>> dictRegionMap = Maps.newHashMap();
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.DICT_REGION);
		for (DictRegion dictRegion : allRegions) {
			cacheHash.put(String.valueOf(dictRegion.getRegionId()), dictRegion.getRegionName());
			if (dictRegion.getRegionType() == BaseConsts.ZERO) {
				rootRegions.add(dictRegion);
			} else if (dictRegion.getRegionType() == BaseConsts.ONE) {
				provinces.add(dictRegion);
			} else if (dictRegion.getRegionType() == BaseConsts.TWO) {
				citys.add(dictRegion);
			} else if (dictRegion.getRegionType() == BaseConsts.THREE) {
				countys.add(dictRegion);
			}
		}

		for (DictRegion nation : rootRegions) {
			List<CodeValue> childProvinces = Lists.newArrayList();
			for (DictRegion province : provinces) {
				if (province.getParentId().equals(nation.getRegionId())) {
					childProvinces.add(new CodeValue(String.valueOf(province.getRegionId()), province.getRegionName()));
					List<CodeValue> childCitys = Lists.newArrayList();
					for (DictRegion city : citys) {
						List<CodeValue> childCountys = Lists.newArrayList();
						if (city.getParentId().equals(province.getRegionId())) {
							childCitys.add(new CodeValue(String.valueOf(city.getRegionId()), city.getRegionName()));
							for (DictRegion county : countys) {
								if (county.getParentId().equals(city.getRegionId())) {
									childCountys.add(new CodeValue(String.valueOf(county.getRegionId()),
											county.getRegionName()));
								}
							}
							dictRegionMap.put(String.valueOf(city.getRegionId()), childCountys);
						}
					}
					dictRegionMap.put(String.valueOf(province.getRegionId()), childCitys);
				}
			}
			dictRegionMap.put(String.valueOf(nation.getRegionId()), childProvinces);
		}
		objectRedisTemplate.delete(CacheKeyConsts.NATIOIN_DICT_REGION);
		objectRedisTemplate.opsForList().leftPushAll(CacheKeyConsts.NATIOIN_DICT_REGION, rootRegions);
		objectRedisTemplate.delete(CacheKeyConsts.DICT_REGION_MAP);
		objectRedisTemplate.boundHashOps(CacheKeyConsts.DICT_REGION_MAP).putAll(dictRegionMap);
	}

	private void initSubjectInvoice() {
		objectRedisTemplate.delete(CacheKeyConsts.SUBJECT_INVICE);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.SUBJECT_INVICE + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshSubjectInvoice();
	}

	public void refreshSubjectInvoice() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.SUBJECT_INVICE + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseInvoice> invoiceList = baseInvoiceDao.queryAllInvoice(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(invoiceList)) {
			LOGGER.info("刷新baseInvoice表的条数：" + invoiceList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUBJECT_INVICE);
			for (BaseInvoice invocie : invoiceList) {
				String subjectId = String.valueOf(invocie.getSubjectId());
				if (cacheHash.hasKey(subjectId)) {
					List<BaseInvoice> subList = (List<BaseInvoice>) cacheHash.get(subjectId);

					BaseInvoice exsitInvoice = null;
					for (BaseInvoice baseInvoice : subList) {
						if (baseInvoice.getId().intValue() == invocie.getId().intValue()) {
							exsitInvoice = baseInvoice;
							break;
						}
					}
					if (exsitInvoice != null) {
						subList.remove(exsitInvoice);
					}

					subList.add(invocie);
					cacheHash.put(subjectId, subList);
				} else {
					List<BaseInvoice> cvList = Lists.newArrayList();
					cvList.add(invocie);
					cacheHash.put(subjectId, cvList);
				}
			}
			Date updateAt = baseInvoiceDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.SUBJECT_INVICE + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}

		}
	}

	private void initAddress() {
		objectRedisTemplate.delete(CacheKeyConsts.SUBJECT_ADDRESS);// 缓存之前，先删除KEY，防止序列化不一致
		objectRedisTemplate.delete(CacheKeyConsts.ADDRESS);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.ADDRESS + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshAddress();
	}

	public void refreshAddress() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.ADDRESS + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseAddress> addressList = baseAddressDao.queryAllSubjectAddress(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(addressList)) {
			LOGGER.info("刷新baseAddress表的条数：" + addressList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUBJECT_ADDRESS);
			BoundHashOperations addCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.ADDRESS);
			for (BaseAddress address : addressList) {
				address.setNationName(cacheService.getDictNameById(address.getNationId()));
				address.setProvinceName(cacheService.getDictNameById(address.getProvinceId()));
				address.setCityName(cacheService.getDictNameById(address.getCityId()));
				address.setCountyName(cacheService.getDictNameById(address.getCountyId()));
				addCacheHash.put(String.valueOf(address.getId()), address);

				String subjectId = String.valueOf(address.getSubjectId());
				if (cacheHash.hasKey(subjectId)) {
					List<BaseAddress> subList = (List<BaseAddress>) cacheHash.get(subjectId);

					BaseAddress exsitAddress = null;
					for (BaseAddress baseAddress : subList) {
						if (baseAddress.getId().intValue() == address.getId().intValue()) {
							exsitAddress = baseAddress;
							break;
						}
					}
					if (exsitAddress != null) {
						subList.remove(exsitAddress);
					}
					subList.add(address);
					cacheHash.put(subjectId, subList);
				} else {
					List<BaseAddress> cvList = Lists.newArrayList();
					cvList.add(address);
					cacheHash.put(subjectId, cvList);
				}
			}
			Date updateAt = baseAddressDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.ADDRESS + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initAccount() {
		objectRedisTemplate.delete(CacheKeyConsts.ACCOUNT);
		objectRedisTemplate.delete(CacheKeyConsts.SUBJECT_ACCOUNT);// 主体下的账号CacheKeyConsts.SUBJECT_ACCOUNT
		stringRedisTemplate.delete(CacheKeyConsts.ACCOUNT + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshAccount();
	}

	public void refreshAccount() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.ACCOUNT + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseAccount> accountList = baseAccountDao.queryAllAccount(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(accountList)) {
			LOGGER.info("刷新baseAccount表的条数：" + accountList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNT);
			BoundHashOperations subjCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUBJECT_ACCOUNT);
			for (BaseAccount account : accountList) {
				String accountId = String.valueOf(account.getId());
				String subjectId = String.valueOf(account.getSubjectId());
				cacheHash.put(accountId, account);
				// 主题下的账号
				if (subjCacheHash.hasKey(subjectId)) {
					List<BaseAccount> subList = (List<BaseAccount>) subjCacheHash.get(subjectId);

					BaseAccount exsitAccount = null;
					for (BaseAccount baseAccount : subList) {
						if (baseAccount.getId().intValue() == account.getId().intValue()) {
							exsitAccount = baseAccount;
							break;
						}
					}
					if (exsitAccount != null) {
						subList.remove(exsitAccount);
					}
					subList.add(account);
					subjCacheHash.put(subjectId, subList);
				} else {
					List<BaseAccount> cvList = Lists.newArrayList();
					cvList.add(account);
					subjCacheHash.put(subjectId, cvList);
				}
			}
			Date updateAt = baseAccountDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.ACCOUNT + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}

		}
	}

	private void initProjectGoods() {
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_GOODS);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.PROJECT_GOODS + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshProjectGoods();
	}

	public void refreshProjectGoods() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PROJECT_GOODS + CacheKeyConsts.MAX_DATE_TIME);
		List<ProjectGoods> pgList = projectGoodsDao.queryAllProGoods(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(pgList)) {
			LOGGER.info("刷新projectGoods表的条数：" + pgList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_GOODS);
			for (ProjectGoods pg : pgList) {
				String projectId = String.valueOf(pg.getProjectId());
				if (cacheHash.hasKey(projectId)) {
					List<ProjectGoods> subList = (List<ProjectGoods>) cacheHash.get(projectId);

					ProjectGoods exsitProjectGoods = null;
					for (ProjectGoods projectGoods : subList) {
						if (projectGoods.getGoodsId().intValue() == pg.getGoodsId().intValue()) {
							exsitProjectGoods = projectGoods;
							break;
						}
					}
					if (exsitProjectGoods != null) {
						subList.remove(exsitProjectGoods);
					}

					subList.add(pg);
					cacheHash.put(projectId, subList);
				} else {
					List<ProjectGoods> cvList = Lists.newArrayList();
					cvList.add(pg);
					cacheHash.put(projectId, cvList);
				}
			}

			Date updateAt = projectGoodsDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.PROJECT_GOODS + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initProjectSub() {
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_CUSTOMER);
		objectRedisTemplate.delete(CacheKeyConsts.CUSTOMER_PROJECT);
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_SUPPLIER);
		objectRedisTemplate.delete(CacheKeyConsts.SUPPLIER_PROJECT);
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_WAREHOUSE);
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_VIRTUAL_WAREHOUSE);
		stringRedisTemplate.delete(CacheKeyConsts.PROJECT_SUBJECT + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshProjectSub();
	}

	public void refreshProjectSub() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PROJECT_SUBJECT + CacheKeyConsts.MAX_DATE_TIME);
		List<ProjectSubject> psList = projectSubjectDao.queryAllProjectSub(lastUpdateAt);

		if (CollectionUtils.isNotEmpty(psList)) {
			LOGGER.info("刷新projectSubject表的条数：" + psList.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations custCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_CUSTOMER);// 项目-客户
			BoundHashOperations custProjectCacheHash = objectRedisTemplate
					.boundHashOps(CacheKeyConsts.CUSTOMER_PROJECT);// 客户下的项目
			BoundHashOperations supplierCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_SUPPLIER);// 项目-供应商
			BoundHashOperations wareCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_WAREHOUSE);// 项目-仓库
			BoundHashOperations virtualWareCacheHash = objectRedisTemplate
					.boundHashOps(CacheKeyConsts.PROJECT_VIRTUAL_WAREHOUSE);// 项目-虚拟仓库
			BoundHashOperations supplierProCacheHash = objectRedisTemplate
					.boundHashOps(CacheKeyConsts.SUPPLIER_PROJECT);// 供应商 绑定的项目
			for (ProjectSubject ps : psList) {
				String projectId = String.valueOf(ps.getProjectId());
				switch (ps.getSubjectType()) {
				case BaseConsts.SUBJECT_TYPE_CUSTOMER:
					putProjectSub(ps, custCacheHash, projectId);
					putSubjectProject(ps, custProjectCacheHash);
					break;
				case BaseConsts.SUBJECT_TYPE_SUPPLIER:
					putProjectSub(ps, supplierCacheHash, projectId);
					putSubjectProject(ps, supplierProCacheHash);
					break;
				case BaseConsts.SUBJECT_TYPE_WAREHOUSE:
					putProjectSub(ps, wareCacheHash, projectId);
					BaseSubject baseSubject = cacheService.getWarehouseById(ps.getSubjectId());
					if (null != baseSubject && null != baseSubject.getWarehouseType()
							&& baseSubject.getWarehouseType().equals(BaseConsts.THREE)
							&& !baseSubject.getId().equals(BaseConsts.ONE)) {
						putProjectSub(ps, virtualWareCacheHash, projectId);
					}
					break;
				}
			}
			Date updateAt = projectSubjectDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.PROJECT_SUBJECT + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}

		}
	}

	private void putSubjectProject(ProjectSubject ps, BoundHashOperations supplierProCacheHash) {
		if (ps.getSubjectId() != null) {
			String supplierId = ps.getSubjectId().toString();
			if (supplierProCacheHash.hasKey(supplierId)) {
				List<ProjectSubject> subList = (List<ProjectSubject>) supplierProCacheHash.get(supplierId);
				ProjectSubject exsitProjectSubject = null;
				for (ProjectSubject projectSubject : subList) {
					if (projectSubject.getProjectId().intValue() == ps.getProjectId().intValue()) {
						exsitProjectSubject = projectSubject;
						break;
					}
				}
				if (exsitProjectSubject != null) {
					subList.remove(exsitProjectSubject);
				}
				subList.add(ps);
				supplierProCacheHash.put(supplierId, subList);
			} else {
				List<ProjectSubject> cvList = Lists.newArrayList();
				cvList.add(ps);
				supplierProCacheHash.put(supplierId, cvList);
			}
		}
	}

	private void putProjectSub(ProjectSubject ps, BoundHashOperations cacheHash, String projectId) {
		if (cacheHash.hasKey(projectId)) {
			List<ProjectSubject> subList = (List<ProjectSubject>) cacheHash.get(projectId);

			ProjectSubject exsitProject = null;
			for (ProjectSubject projectSubject : subList) {
				if (projectSubject.getSubjectId().intValue() == ps.getSubjectId().intValue()) {
					exsitProject = projectSubject;
					break;
				}
			}
			if (exsitProject != null) {
				subList.remove(exsitProject);
			}

			subList.add(ps);
			cacheHash.put(projectId, subList);
		} else {
			List<ProjectSubject> cvList = Lists.newArrayList();
			cvList.add(ps);
			cacheHash.put(projectId, cvList);
		}
	}

	private void initUser() {
		objectRedisTemplate.delete(CacheKeyConsts.USER);
		objectRedisTemplate.delete(CacheKeyConsts.USERNAME_ID);
		objectRedisTemplate.delete(CacheKeyConsts.DEPARTMENT_USER);
		stringRedisTemplate.delete(CacheKeyConsts.USER + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshUser();
	}

	public void refreshUser() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER);
		BoundHashOperations usernameHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.USERNAME_ID);
		BoundHashOperations departUserHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT_USER);
		String lastUpdateAt = stringRedisTemplate.opsForValue().get(CacheKeyConsts.USER + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseUser> userList = baseUserDao.queryAllUser(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(userList)) {
			LOGGER.info("刷新baseUser表的条数：" + userList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BaseUser bs : userList) {
				Object obj = cacheHash.get(String.valueOf(bs.getId()));// 旧用户属于的部门需要移除
				cacheHash.put(String.valueOf(bs.getId()), bs);
				usernameHash.put(bs.getUserName(), String.valueOf(bs.getId()));

				if (bs.getDepartmentId() != null) {
					String depatmentId = bs.getDepartmentId().toString();
					if (departUserHash.hasKey(depatmentId)) {
						List<BaseUser> subList = (List<BaseUser>) departUserHash.get(depatmentId);
						BaseUser exsitUser = null;
						for (BaseUser user : subList) {
							if (user.getId().intValue() == bs.getId().intValue()) {
								exsitUser = user;
								break;
							}
						}
						if (exsitUser != null) {
							subList.remove(exsitUser);
						}
						subList.add(bs);
						departUserHash.put(depatmentId, subList);
						// 需要移除老部门中用户
						if (obj != null) {
							BaseUser oldUser = (BaseUser) obj;
							if (oldUser.getDepartmentId() != null
									&& oldUser.getDepartmentId().intValue() != bs.getDepartmentId()) {
								subList = (List<BaseUser>) departUserHash.get(oldUser.getDepartmentId().toString());
								Iterator<BaseUser> iterator = subList.iterator();
								while (iterator.hasNext()) {
									BaseUser u = iterator.next();
									if (u.getId().intValue() == oldUser.getId().intValue()) {
										subList.remove(u);
										break;
									}
								}
								departUserHash.put(oldUser.getDepartmentId().toString(), subList);
							}
						}
					} else {
						List<BaseUser> cvList = Lists.newArrayList();
						cvList.add(bs);
						departUserHash.put(depatmentId, cvList);
					}
				}

			}
			Date updateAt = baseUserDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.USER + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initSubject() {
		objectRedisTemplate.delete(CacheKeyConsts.CUSTOMER);
		objectRedisTemplate.delete(CacheKeyConsts.SUPPLIER);
		objectRedisTemplate.delete(CacheKeyConsts.BUSI_UNIT);
		objectRedisTemplate.delete(CacheKeyConsts.WAREHOUSE);
		stringRedisTemplate.delete(CacheKeyConsts.SUBJECT + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshSubject();
	}

	public void refreshSubject() {
		BoundHashOperations custCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.CUSTOMER);// 客户
		BoundHashOperations supplierCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUPPLIER);// 供应商
		BoundHashOperations busiCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.BUSI_UNIT);// 经营单位
		BoundHashOperations wareCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.WAREHOUSE);// 仓库
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.SUBJECT + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseSubject> subjectList = baseSubjectDao.queryAllSubject(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(subjectList)) {
			LOGGER.info("刷新baseSubject表的条数：" + subjectList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BaseSubject bs : subjectList) {
				if (busiCacheHash.hasKey(bs.getId().toString())) {
					busiCacheHash.put(bs.getId().toString(), bs);
				}
				if (supplierCacheHash.hasKey(bs.getId().toString())) {
					supplierCacheHash.put(bs.getId().toString(), bs);
				}
				if (custCacheHash.hasKey(bs.getId().toString())) {
					custCacheHash.put(bs.getId().toString(), bs);
				}
				switch (bs.getSubjectType()) {
				case BaseConsts.SUBJECT_TYPE_CUSTOMER:// 客户
					custCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.SUBJECT_TYPE_SUPPLIER:// 供应商
					supplierCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.SUBJECT_TYPE_BUSI_UNIT:// 经营单位
					busiCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.FIVE:// 经营单位 供应商
					supplierCacheHash.put(String.valueOf(bs.getId()), bs);
					busiCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.NINE:// 经营单位 客户
					busiCacheHash.put(String.valueOf(bs.getId()), bs);
					custCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.INT_12:// 供应商 客户
					custCacheHash.put(String.valueOf(bs.getId()), bs);
					supplierCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.INT_13:// 经营单位 供应商 客户
					busiCacheHash.put(String.valueOf(bs.getId()), bs);
					supplierCacheHash.put(String.valueOf(bs.getId()), bs);
					custCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				case BaseConsts.SUBJECT_TYPE_WAREHOUSE:// 仓库
					wareCacheHash.put(String.valueOf(bs.getId()), bs);
					break;
				}
			}
			Date updateAt = baseSubjectDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.SUBJECT + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initProject() {
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT);
		objectRedisTemplate.delete(CacheKeyConsts.PROJECT_BUSI_UNIT);
		objectRedisTemplate.delete(CacheKeyConsts.BUSI_UNIT_PROJECT);
		objectRedisTemplate.delete(CacheKeyConsts.DEPARTMENT_PROJECT);
		stringRedisTemplate.delete(CacheKeyConsts.PROJECT + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除

		refreshProject();
	}

	public void refreshProject() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT);
		BoundHashOperations pbHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_BUSI_UNIT);
		BoundHashOperations busiProHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.BUSI_UNIT_PROJECT);
		BoundHashOperations departProHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT_PROJECT);
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.PROJECT + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseProject> projectList = baseProjectDao.queryAllProject(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(projectList)) {
			LOGGER.info("刷新baseProject表的条数：" + projectList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BaseProject bp : projectList) {
				cacheHash.put(String.valueOf(bp.getId()), bp);
				// 项目下的经营单位
				if (pbHash.hasKey(String.valueOf(bp.getId()))) {
					List<BaseProject> subList = (List<BaseProject>) pbHash.get(String.valueOf(bp.getId()));
					BaseProject exsitProject = null;
					for (BaseProject baseProject : subList) {
						if (baseProject.getBusinessUnitId().intValue() == bp.getBusinessUnitId().intValue()) {
							exsitProject = baseProject;
							break;
						}
					}
					if (exsitProject != null) {
						subList.remove(exsitProject);
					}
					subList.add(bp);
					pbHash.put(String.valueOf(bp.getId()), subList);
				} else {
					List<BaseProject> cvList = Lists.newArrayList();
					cvList.add(bp);
					pbHash.put(String.valueOf(bp.getId()), cvList);
				}
				// 经营单位下的项目
				if (busiProHash.hasKey(String.valueOf(bp.getBusinessUnitId()))) {
					List<BaseProject> subList = (List<BaseProject>) busiProHash
							.get(String.valueOf(bp.getBusinessUnitId()));
					BaseProject exsitProject = null;
					for (BaseProject baseProject : subList) {
						if (baseProject.getId().intValue() == bp.getId().intValue()) {
							exsitProject = baseProject;
							break;
						}
					}
					if (exsitProject != null) {
						subList.remove(exsitProject);
					}

					subList.add(bp);
					busiProHash.put(String.valueOf(bp.getBusinessUnitId()), subList);
				} else {
					List<BaseProject> cvList = Lists.newArrayList();
					cvList.add(bp);
					busiProHash.put(String.valueOf(bp.getBusinessUnitId()), cvList);
				}
				// 部门下的项目
				putDepartProject(bp, departProHash);

			}
		}
		Date updateAt = baseProjectDao.queryLastUpdateAt();
		if (updateAt != null) {
			String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
			stringRedisTemplate.opsForValue().set(CacheKeyConsts.PROJECT + CacheKeyConsts.MAX_DATE_TIME,
					modifyLastUpdate);
		}

	}

	private void putDepartProject(BaseProject bp, BoundHashOperations departProHash) {
		if (bp.getDepartmentId() != null) {
			String departId = bp.getDepartmentId().toString();
			if (departProHash.hasKey(departId)) {
				List<BaseProject> subList = (List<BaseProject>) departProHash.get(departId);
				BaseProject exsitProject = null;
				for (BaseProject baseProject : subList) {
					if (baseProject.getId().intValue() == bp.getId().intValue()) {
						exsitProject = baseProject;
						break;
					}
				}
				if (exsitProject != null) {
					subList.remove(exsitProject);
				}
				subList.add(bp);
				departProHash.put(departId, subList);
			} else {
				List<BaseProject> cvList = Lists.newArrayList();
				cvList.add(bp);
				departProHash.put(departId, cvList);
			}
		}
	}

	private void initGoods() {
		objectRedisTemplate.delete(CacheKeyConsts.GOODS);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.GOODS + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshGoods();
	}

	public void refreshGoods() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.GOODS);
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.GOODS + CacheKeyConsts.MAX_DATE_TIME);
		List<BaseGoods> goodsList = baseGoodsDao.queryAllGoodsList(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(goodsList)) {
			LOGGER.info("刷新baseGoods表的条数：" + goodsList.size() + ",最后更新时间：" + lastUpdateAt);
			for (BaseGoods good : goodsList) {
				cacheHash.put(String.valueOf(good.getId()), good);
			}
			Date updateAt = baseGoodsDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.GOODS + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initDistributionGoods() {// 铺货商品
		objectRedisTemplate.delete(CacheKeyConsts.DISTRIBUTION_GOODS);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.DISTRIBUTION_GOODS + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshDistributionGoods();
	}

	public void refreshDistributionGoods() {
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.DISTRIBUTION_GOODS);
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.DISTRIBUTION_GOODS + CacheKeyConsts.MAX_DATE_TIME);
		List<DistributionGoods> goodsList = distributionGoodsDao.queryAllDistributeGoodsList(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(goodsList)) {
			LOGGER.info("刷新distributionGoods表的条数：" + goodsList.size() + ",最后更新时间：" + lastUpdateAt);
			for (DistributionGoods good : goodsList) {
				cacheHash.put(String.valueOf(good.getId()), good);
			}
			Date updateAt = distributionGoodsDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.DISTRIBUTION_GOODS + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	private void initAccountBook() {
		objectRedisTemplate.delete(CacheKeyConsts.ACCOUNTBOOK);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.ACCOUNTBOOK + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshAccountBook();
	}

	public void refreshAccountBook() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.ACCOUNTBOOK + CacheKeyConsts.MAX_DATE_TIME);
		List<AccountBook> accountBooks = accountBookDao.queryAllAccountBook(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(accountBooks)) {
			LOGGER.info("刷新accountBook表的条数：" + accountBooks.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations accountBookCache = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTBOOK);
			for (AccountBook accountBook : accountBooks) {
				accountBookCache.put(String.valueOf(accountBook.getId()), accountBook);
			}

			Date updateAt = accountBookDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.ACCOUNTBOOK + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}

		}
	}

	private void initAccountLine() {
		objectRedisTemplate.delete(CacheKeyConsts.ACCOUNTLINE);// 缓存之前，先删除KEY，防止序列化不一致
		stringRedisTemplate.delete(CacheKeyConsts.ACCOUNTLINE + CacheKeyConsts.MAX_DATE_TIME);// 最后更新时间删除
		refreshAccountLine();
	}

	public void refreshAccountLine() {
		String lastUpdateAt = stringRedisTemplate.opsForValue()
				.get(CacheKeyConsts.ACCOUNTLINE + CacheKeyConsts.MAX_DATE_TIME);
		List<AccountLine> accountLines = accountLineDao.queryAllAccountLine(lastUpdateAt);
		if (CollectionUtils.isNotEmpty(accountLines)) {
			LOGGER.info("刷新accountLine表的条数：" + accountLines.size() + ",最后更新时间：" + lastUpdateAt);
			BoundHashOperations accountLineCache = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTLINE);
			for (AccountLine accountLine : accountLines) {
				accountLineCache.put(String.valueOf(accountLine.getId()), accountLine);
			}
			Date updateAt = accountLineDao.queryLastUpdateAt();
			if (updateAt != null) {
				String modifyLastUpdate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, updateAt);
				stringRedisTemplate.opsForValue().set(CacheKeyConsts.ACCOUNTLINE + CacheKeyConsts.MAX_DATE_TIME,
						modifyLastUpdate);
			}
		}
	}

	public void refreshUserPerms() {
		List<BaseUser> userList = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER).values();
		if (CollectionUtils.isNotEmpty(userList)) {
			for (BaseUser user : userList) {
				if (user.getIsDelete() == BaseConsts.ZERO && user.getStatus() == BaseConsts.ZERO) {
					Collection<BasePermission> userPerms = cacheService.getUserPermsByUserId(user.getId().toString());
					if (userPerms != null) {
						objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PERMISSIONS).put(user.getId().toString(),
								new ArrayList<BasePermission>(userPerms));
					}
				}
			}
		}
	}

	public void setInitData(boolean initData) {
		this.initData = initData;
	}

}
