package com.scfs.service.support;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.base.entity.BaseDepartmentDao;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.base.entity.BaseRoleDao;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.base.entity.BaseUserDao;
import com.scfs.dao.base.entity.DistributionGoodsDao;
import com.scfs.dao.base.entity.MatterManageDao;
import com.scfs.dao.fee.FeeSpecDao;
import com.scfs.dao.fi.AccountBookDao;
import com.scfs.dao.fi.AccountLineDao;
import com.scfs.dao.report.ProfitTargetDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.domain.base.entity.BasePermissionGroup;
import com.scfs.domain.base.entity.BasePermissionRelation;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseRolePermissionGroup;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserRoles;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.base.entity.MatterManage;
import com.scfs.domain.base.model.UserPermission;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.domain.report.entity.ProfitTarget;
import com.scfs.rpc.cache.ObjectRedisTemplate;

/**
 * Created by Administrator on 2016/10/21.
 */
@Service
public class CacheService {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

	@Autowired
	private BaseSubjectDao baseSubjectDao;
	@Autowired
	private BaseProjectDao baseProjectDao;
	@Autowired
	private BaseGoodsDao baseGoodsDao;
	@Autowired
	private BaseAddressDao baseAddressDao;
	@Autowired
	private BaseAccountDao baseAccountDao;
	@Autowired
	private ObjectRedisTemplate objectRedisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private BaseDepartmentDao baseDepartmentDao;
	@Autowired
	private BaseRoleDao baseRoleDao;
	@Autowired
	private FeeSpecDao feeSpecDao;
	@Autowired
	private AccountBookDao accountBookDao;
	@Autowired
	private AccountLineDao accountLineDao;
	@Autowired
	private DistributionGoodsDao distributionGoodsDao;
	@Autowired
	private ProfitTargetDao profitTargetDao;// 业务目标值
	@Autowired
	private MatterManageDao matterManageDao;// 事项管理

	@IgnoreTransactionalMark
	public List<UserPermission> queryMenuPermissions() {
		List<UserPermission> menuPermissions = Lists.newArrayList();
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PERMISSIONS)
				.get(String.valueOf(ServiceSupport.getUser().getId()));
		if (obj != null) {
			List<BasePermission> permissionList = (List<BasePermission>) obj;
			for (BasePermission permission : permissionList) {
				if (permission.getType() == BaseConsts.TWO && permission.getMenuLevel() == BaseConsts.ONE) {// 菜单权限
																											// //一级菜单
					UserPermission userPermission = createUpermission(permission);
					menuPermissions.add(userPermission);
				}
			}
			Collections.sort(menuPermissions);// 一级菜单排序
			for (BasePermission permission : permissionList) {
				if (permission.getType() == BaseConsts.TWO && permission.getMenuLevel() == BaseConsts.TWO) {// 菜单权限//二级菜单
					for (UserPermission oneMenu : menuPermissions) {
						if (permission.getParentId() == oneMenu.getPermId()) {
							List<UserPermission> twoList = oneMenu.getTwoLevelPermissions();
							UserPermission userPermission = createUpermission(permission);
							if (CollectionUtils.isNotEmpty(twoList)) {
								twoList.add(userPermission);
								Collections.sort(twoList);// 二级菜单排序
							} else {
								List<UserPermission> twoMenuList = Lists.newArrayList();
								twoMenuList.add(userPermission);
								oneMenu.setTwoLevelPermissions(twoMenuList);
							}
						}
					}
				}
			}
		}

		return menuPermissions;
	}

	/**
	 * 获取当前登录用户的所有权限，权限ID，权限
	 *
	 * @return
	 */
	public Collection<BasePermission> getUserPermsByUserId(String userId) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_ROLES).get(userId);
		LOGGER.info("获取用户[{}]菜单：", userId);
		Map<Integer, BasePermission> permissionMap = Maps.newHashMap();
		if (obj != null) {
			List<BaseUserRoles> urList = (List<BaseUserRoles>) obj;
			for (BaseUserRoles userRole : urList) { // 用户下的角色列表
				if (userRole.getIsDelete() == BaseConsts.ZERO && userRole.getStatus() != null
						&& userRole.getStatus() == BaseConsts.ONE) {// 关系没有删除
					Object role = objectRedisTemplate.boundHashOps(CacheKeyConsts.ROLES)
							.get(String.valueOf(userRole.getRoleId()));
					if (role != null) {
						BaseRole baseRole = (BaseRole) role;
						if (baseRole.getState() == BaseConsts.TWO && baseRole.getIsDelete() == BaseConsts.ZERO) {
							Object rolePermGroup = objectRedisTemplate
									.boundHashOps(CacheKeyConsts.ROLE_PERMISSION_GROUP)
									.get(String.valueOf(baseRole.getId()));// 获取角色下的权限组关系列表
							if (rolePermGroup != null) {// 角色权限组关系
								List<BaseRolePermissionGroup> rolePermGroupList = (List<BaseRolePermissionGroup>) rolePermGroup;
								for (BaseRolePermissionGroup rolePermissionGroup : rolePermGroupList) {
									if (rolePermissionGroup.getIsDelete() == BaseConsts.ZERO) {// 角色和权限组关系没有删除
										Object permGroup = objectRedisTemplate
												.boundHashOps(CacheKeyConsts.PERMISSION_GROUP)
												.get(String.valueOf(rolePermissionGroup.getPermissionGroupId()));
										if (permGroup != null) {
											BasePermissionGroup pg = (BasePermissionGroup) permGroup;
											if (pg.getState() == BaseConsts.ONE
													&& pg.getIsDelete() == BaseConsts.ZERO) {// 权限组已经完成状态
												Object permGroupRelation = objectRedisTemplate
														.boundHashOps(CacheKeyConsts.PERMISSION_GROUP_PERMISSION)
														.get(String.valueOf(pg.getId()));
												if (permGroupRelation != null) {
													List<BasePermissionRelation> pgRelationList = (List<BasePermissionRelation>) permGroupRelation;
													for (BasePermissionRelation bgRelation : pgRelationList) {
														if (bgRelation.getIsDelete() == BaseConsts.ZERO) {
															// 获取权限
															Object perm = objectRedisTemplate
																	.boundHashOps(CacheKeyConsts.PERMISSIONS)
																	.get(String.valueOf(bgRelation.getPermissionId()));
															if (perm != null) {
																BasePermission permission = (BasePermission) perm;
																if (permission.getIsDelete() == BaseConsts.ZERO
																		&& permission.getState() == BaseConsts.TWO) {
																	permissionMap.put(permission.getId(), permission);
																}
															}

														}
													}
												}

											}

										}
									}
								}

							}

						}
					}

				}
			}
		}
		return permissionMap.values();
	}

	private UserPermission createUpermission(BasePermission permission) {
		UserPermission userPerm = new UserPermission();
		userPerm.setPermId(permission.getId());
		userPerm.setMenuLevel(permission.getMenuLevel());
		userPerm.setName(permission.getName());
		userPerm.setUrl(permission.getUrl());
		userPerm.setOrd(permission.getOrd() == null ? 0 : permission.getOrd());
		userPerm.setParentId(permission.getParentId() == null ? 0 : permission.getParentId());
		userPerm.setType(permission.getType() == null ? 0 : permission.getType());
		userPerm.setState(permission.getState() == null ? 0 : permission.getState());
		return userPerm;
	}

	public BaseGoods getGoodsById(Integer id) {
		if (id == null) {
			return new BaseGoods();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.GOODS).get(String.valueOf(id));
		if (obj == null) {
			return baseGoodsDao.queryBaseGoodsById(id);
		}
		return (BaseGoods) obj;
	}

	/**
	 * 根据项目ID和商品编码查询商品
	 *
	 * @param pid
	 * @param goodsNo
	 * @return
	 */
	public BaseGoods getGoodsByPidAndNo(Integer pid, String goodsNo) {
		if (pid == null || goodsNo == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_GOODS).get(pid.toString());
		if (obj != null) {
			List<ProjectGoods> subList = (List<ProjectGoods>) obj;
			for (ProjectGoods projectGoods : subList) {
				BaseGoods goods = getGoodsById(projectGoods.getGoodsId());
				if (goods != null && goodsNo.equals(goods.getNumber())) {
					return goods;
				}
			}
		}

		return null;
	}

	/**
	 * 获取铺货商品信息
	 * 
	 * @param id
	 * @return
	 */
	public DistributionGoods getDistributionGoodsById(Integer id) {
		if (id == null) {
			return new DistributionGoods();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.DISTRIBUTION_GOODS).get(String.valueOf(id));
		if (obj == null) {
			return distributionGoodsDao.queryDistributionGoodsById(id);
		}
		return (DistributionGoods) obj;
	}

	public CodeValue getGoodsCvById(Integer id) {
		BaseGoods bg = getGoodsById(id);
		if (bg != null) {
			return new CodeValue(String.valueOf(id), bg.getName());
		} else {
			return new CodeValue(String.valueOf(id), null);
		}
	}

	/**
	 * 根据部门ID获取部门
	 *
	 * @param id
	 * @return
	 */
	public BaseDepartment getBaseDepartmentById(Integer id) {
		if (id == null) {
			return new BaseDepartment();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT).get(String.valueOf(id));
		if (obj == null) {
			return baseDepartmentDao.selectById(id);
		}
		return (BaseDepartment) obj;
	}

	/**
	 * 根据用户ID获取中文名
	 *
	 * @param id
	 * @return
	 */
	public String getUserChineseNameByid(Integer id) {
		BaseUser user = getUserByid(id);
		if (user != null) {
			return user.getChineseName();
		} else {
			return null;
		}
	}

	/**
	 * 根据用户ID获取用户
	 *
	 * @param id
	 * @return
	 */
	public BaseUser getUserByid(Integer id) {
		if (id == null) {
			return new BaseUser();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER).get(String.valueOf(id));
		if (obj == null) {
			return baseUserDao.queryBaseUserById(id);
		}
		return (BaseUser) obj;
	}

	public List<BaseUser> getUsersByDepartmentId(Integer departmentId) {
		if (departmentId == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT_USER).get(departmentId.toString());
		if (obj != null) {
			return (List<BaseUser>) obj;
		} else {
			return null;
		}
	}

	/**
	 * 根据用户名获取用户
	 *
	 * @param userName
	 * @return
	 */
	public BaseUser getUserByUsername(String userName) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USERNAME_ID).get(userName);
		if (obj != null) {
			String userId = (String) obj;
			return getUserByid(Integer.valueOf(userId));
		} else {
			BaseUser baseUser = new BaseUser();
			baseUser.setUserName(userName);
			return baseUserDao.queryBaseUserByUser(baseUser);
		}
	}

	/**
	 * 根据用户ID获取角色列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<BaseRole> getRolesByUserId(Integer userId) {
		List<BaseRole> result = Lists.newArrayList();
		if (userId == null) {
			return result;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_ROLES).get(String.valueOf(userId));
		if (obj != null) {
			List<BaseUserRoles> useRoleList = (List<BaseUserRoles>) obj;
			for (BaseUserRoles userRole : useRoleList) {
				if (userRole.getIsDelete() == BaseConsts.ZERO && userRole.getStatus() != null
						&& userRole.getStatus() == BaseConsts.ONE) {
					result.add(getRoleById(userRole.getRoleId()));
				}
			}
		}
		return result;
	}

	/**
	 * 根据角色ID获取角色信息
	 * 
	 * @param roleId
	 * @return
	 */
	public BaseRole getRoleById(Integer roleId) {
		if (roleId == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.ROLES).get(String.valueOf(roleId));
		if (obj != null) {
			return (BaseRole) obj;
		} else {
			return baseRoleDao.queryBaseRoleById(roleId);
		}
	}

	/**
	 * 根据 ID 和缓存key获取主题简称
	 *
	 * @param id
	 * @param cacheKey
	 * @return
	 */
	public String showSubjectNameByIdAndKey(Integer id, String cacheKey) {
		BaseSubject bs = getSubjectById(id, cacheKey);
		if (bs != null) {
			return bs.getNoName();
		} else {
			return null;
		}
	}

	/**
	 * 根据账号ID获取收款账号NO
	 *
	 * @param id
	 * @return
	 */
	public String getAccountNoById(Integer id) {
		BaseAccount account = getAccountById(id);
		if (account == null) {
			return null;
		} else {
			return account.getAccountNo();
		}
	}

	/**
	 * 根据账号ID获取账号
	 *
	 * @param id
	 * @return
	 */
	public BaseAccount getAccountById(Integer id) {
		if (id == null || id < 0) {
			return new BaseAccount();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNT).get(String.valueOf(id));
		if (obj == null) {
			BaseAccount account = baseAccountDao.queryEntityById(id);
			return account;
		}
		return (BaseAccount) obj;
	}

	/**
	 * @param id
	 * @param cacheKey
	 * @return
	 */
	public CodeValue getSubjectCvByIdAndKey(Integer id, String cacheKey) {
		BaseSubject bs = getSubjectById(id, cacheKey);
		if (bs != null) {
			return new CodeValue(String.valueOf(id), bs.getNoName());
		} else {
			return new CodeValue(String.valueOf(id), null);
		}
	}

	/**
	 * 根据 ID 和缓存key获取“编号-主题简称”
	 *
	 * @param id
	 * @param cacheKey
	 * @return
	 */
	public String getSubjectNcByIdAndKey(Integer id, String cacheKey) {
		BaseSubject bs = getSubjectById(id, cacheKey);
		if (bs != null) {
			return bs.getSubjectNo() + BaseConsts.CONJUNCTION_FLAG + bs.getAbbreviation();
		} else {
			return null;
		}
	}

	/**
	 * 根据主题ID获取编码简称
	 *
	 * @param id
	 * @param cacheKey
	 * @return
	 */
	public String getSubjectNameByIdAndKey(Integer id, String cacheKey) {
		BaseSubject bs = getSubjectById(id, cacheKey);
		if (bs != null) {
			return bs.getNoName();
		} else {
			return null;
		}
	}

	/**
	 * 根据项目ID获取项目简称
	 *
	 * @param id
	 * @return
	 */
	public String showProjectNameById(Integer id) {
		BaseProject baseProject = getProjectById(id);
		if (baseProject != null) {
			return baseProject.getNoName();
		} else {
			return null;
		}
	}

	/**
	 * 根据主题ID获取主题信息
	 *
	 * @param id
	 * @return
	 */
	public BaseSubject getBaseSubjectById(Integer id) {
		if (id == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.CUSTOMER).get(String.valueOf(id));
		if (obj == null) {
			obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUPPLIER).get(String.valueOf(id));
			if (obj == null) {
				obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.WAREHOUSE).get(String.valueOf(id));
				if (obj == null) {
					obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BUSI_UNIT).get(String.valueOf(id));
					if (obj == null) {
						BaseSubject baseSubject = baseSubjectDao.queryEntityById(id);
						return baseSubject;
					}
				}
			}
		}
		return (BaseSubject) obj;
	}

	/**
	 * 获取主题编码和简称
	 *
	 * @param id
	 * @return
	 */
	public String getSubjectNoNameById(Integer id) {
		BaseSubject bs = getBaseSubjectById(id);
		if (bs != null) {
			return bs.getNoName();
		} else {
			return null;
		}
	}

	/**
	 * 根据项目ID获取项目名称
	 *
	 * @param id
	 * @return
	 */
	public String getProjectNameById(Integer id) {
		BaseProject baseProject = getProjectById(id);
		if (baseProject != null) {
			return baseProject.getNoName();
		} else {
			return null;
		}
	}

	public BaseSubject getCustomerById(Integer id) {
		return getSubjectById(id, CacheKeyConsts.CUSTOMER);
	}

	/**
	 * 根据客户编码获取客户
	 *
	 * @param cNo
	 * @return
	 */
	public BaseSubject getCustomerByCno(String cNo) {
		if (StringUtils.isBlank(cNo)) {
			return null;
		}
		BoundHashOperations custCacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.CUSTOMER);// 客户
		return getSubjectByNo(cNo, custCacheHash.values());
	}

	/**
	 * 根据项目ID和客户编码查询此项目下的客户
	 *
	 * @param pid
	 * @param cNo
	 * @return
	 */
	public BaseSubject getCustomerByPidAndNo(Integer pid, String cNo) {
		if (pid == null || cNo == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_CUSTOMER).get(pid.toString());
		if (obj != null) {
			List<ProjectSubject> subList = (List<ProjectSubject>) obj;
			for (ProjectSubject projectSubject : subList) {
				if (projectSubject.getStatus() == BaseConsts.ONE && projectSubject.getIsDelete() == BaseConsts.ZERO) {// 可用
					BaseSubject baseSubject = getCustomerById(projectSubject.getSubjectId());
					if (baseSubject.getIsDelete() == BaseConsts.ZERO && baseSubject.getState() == BaseConsts.TWO) {
						if (baseSubject != null && cNo.equals(baseSubject.getSubjectNo())) {
							return baseSubject;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * 根据经营单位ID获取经营单位
	 *
	 * @param id
	 * @return
	 */
	public BaseSubject getBusiUnitById(Integer id) {
		return getSubjectById(id, CacheKeyConsts.BUSI_UNIT);
	}

	/**
	 * 根据仓库ID获取仓库
	 *
	 * @param id
	 * @return
	 */
	public BaseSubject getWarehouseById(Integer id) {
		return getSubjectById(id, CacheKeyConsts.WAREHOUSE);
	}

	/**
	 * 根据仓库编码获取仓库
	 *
	 * @param wNo
	 * @return
	 */
	public BaseSubject getWarehouseByWno(String wNo) {
		if (StringUtils.isBlank(wNo)) {
			return null;
		}
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.WAREHOUSE);
		return getSubjectByNo(wNo, cacheHash.values());
	}

	/**
	 * 根据项目ID和仓库编码查询此项目下的仓库
	 *
	 * @param pid
	 * @param wNo
	 * @return
	 */
	public BaseSubject getWarehouseByPidAndNo(Integer pid, String wNo) {
		if (pid == null || wNo == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_WAREHOUSE).get(pid.toString());
		if (obj != null) {
			List<ProjectSubject> subList = (List<ProjectSubject>) obj;
			for (ProjectSubject projectSubject : subList) {
				if (projectSubject.getStatus() == BaseConsts.ONE && projectSubject.getIsDelete() == BaseConsts.ZERO) {// 可用
					BaseSubject baseSubject = getWarehouseById(projectSubject.getSubjectId());
					if (baseSubject.getIsDelete() == BaseConsts.ZERO && baseSubject.getState() == BaseConsts.TWO) {
						if (baseSubject != null && wNo.equals(baseSubject.getSubjectNo())) {
							return baseSubject;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据供应商ID获取供应商
	 *
	 * @param id
	 * @return
	 */
	public BaseSubject getSupplierById(Integer id) {
		return getSubjectById(id, CacheKeyConsts.SUPPLIER);
	}

	/**
	 * 根据供应商编码获取供应商
	 *
	 * @param sNo
	 * @return
	 */
	public BaseSubject getSupplierBySno(String sNo) {
		if (StringUtils.isBlank(sNo)) {
			return null;
		}
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUPPLIER);// 客户
		return getSubjectByNo(sNo, cacheHash.values());
	}

	/**
	 * 根据项目ID和供应商编码查询此项目下的供应商
	 *
	 * @param pid
	 * @param sNo
	 * @return
	 */
	public BaseSubject getSupplierByPidAndNo(Integer pid, String sNo) {
		if (pid == null || sNo == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_SUPPLIER).get(pid.toString());
		if (obj != null) {
			List<ProjectSubject> subList = (List<ProjectSubject>) obj;
			for (ProjectSubject projectSubject : subList) {
				if (projectSubject.getStatus() == BaseConsts.ONE && projectSubject.getIsDelete() == BaseConsts.ZERO) {// 可用
					BaseSubject baseSubject = getSupplierById(projectSubject.getSubjectId());
					if (baseSubject.getIsDelete() == BaseConsts.ZERO && baseSubject.getState() == BaseConsts.TWO) {
						if (baseSubject != null && sNo.equals(baseSubject.getSubjectNo())) {
							return baseSubject;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据ID获取帐套信息
	 *
	 * @param id
	 * @return
	 */
	public FeeSpec getFeeSpecById(int id) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PAY_FEE_SPEC).get(String.valueOf(id));
		if (obj == null) {
			obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.REC_FEE_SPEC).get(String.valueOf(id));
			if (obj == null) {
				obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.REC_PAY_FEE_SPEC).get(String.valueOf(id));
				if (obj == null) {
					return feeSpecDao.queryEntityById(id);
				}
			}
		}
		return (FeeSpec) obj;
	}

	public String getFeeSpecNoNameById(Integer id) {
		if (id == null) {
			return "";
		}
		FeeSpec feeSpec = getFeeSpecById(id);
		if (feeSpec == null) {
			return "";
		} else {
			return feeSpec.getFeeSpecNo() + "-" + feeSpec.getFeeSpecName();
		}
	}

	/**
	 * 根据项目ID获取项目条款
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectItem getProjectItemByPid(Integer projectId) {
		if (projectId == null) {
			return null;
		}
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_ITEM);
		Object obj = cacheHash.get(projectId.toString());
		if (obj != null) {
			List<ProjectItem> pjList = (List<ProjectItem>) obj;
			for (ProjectItem projectItem : pjList) {
				if ((projectItem.getStatus() == BaseConsts.SIX || projectItem.getStatus() == BaseConsts.SEVEN)) {
					return projectItem;
				}
			}
		}
		return null;
	}

	/**
	 * 根据项目ID获取项目信息
	 *
	 * @param id
	 * @return
	 */
	public BaseProject getProjectById(Integer id) {
		if (id == null || id < 0) {
			return new BaseProject();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT).get(String.valueOf(id));
		if (obj == null) {
			BaseProject baseProject = baseProjectDao.queryEntityById(id);
			if (baseProject != null) {
				objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT).put(String.valueOf(baseProject.getId()),
						baseProject);
				return baseProject;
			}
		}
		return (BaseProject) obj;
	}

	/**
	 * 根据项目编号获取项目信息
	 *
	 * @param pNo
	 * @return
	 */
	public BaseProject getProjectByPno(String pNo) {
		if (StringUtils.isBlank(pNo)) {
			return new BaseProject();
		}
		BoundHashOperations cacheHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT);
		if (cacheHash != null) {
			List<BaseProject> bpList = cacheHash.values();
			if (CollectionUtils.isNotEmpty(bpList)) {
				for (BaseProject baseProject : bpList) {
					if (pNo.equalsIgnoreCase(baseProject.getProjectNo())) {
						return baseProject;
					}
				}
			}
		}
		return null;
	}

	public BaseSubject getSubjectById(Integer id, String cacheKey) {
		if (id == null || id < 0) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(cacheKey).get(String.valueOf(id));
		if (obj == null) {
			BaseSubject baseSubject = baseSubjectDao.queryEntityById(id);
			return baseSubject;
		}
		return (BaseSubject) obj;
	}

	/**
	 * 根据地址ID获取名称
	 *
	 * @param id
	 * @return
	 */
	public String getDictNameById(Integer id) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.DICT_REGION).get(String.valueOf(id));
		if (obj != null) {
			return (String) obj;
		} else {
			return null;
		}
	}

	public BaseAddress getAddressById(Integer id) {
		if (id == null || id < 0) {
			return new BaseAddress();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.ADDRESS).get(String.valueOf(id));
		if (obj == null) {
			BaseAddress address = baseAddressDao.queryEntityById(id);
			if (address != null) {
				address.setNationName(getDictNameById(address.getNationId()));
				address.setProvinceName(getDictNameById(address.getProvinceId()));
				address.setCityName(getDictNameById(address.getCityId()));
				address.setCountyName(getDictNameById(address.getCountyId()));
				objectRedisTemplate.boundHashOps(CacheKeyConsts.ADDRESS).put(String.valueOf(address.getId()), address);
				return address;
			}
		}
		return (BaseAddress) obj;
	}

	public void setLoginToken(String userName, String token) {
		// 30分钟缓存登陆信息
		stringRedisTemplate.opsForValue().set(userName, token, 30, TimeUnit.MINUTES);
	}

	public String getLoginToken(String userName) {
		return stringRedisTemplate.opsForValue().get(userName);
	}

	public void delLoginToken(String userName) {
		if (userName != null) {
			stringRedisTemplate.delete(userName);
		}
	}

	// 登陆次数+1
	public long incrLoginErrorCount(String userName) {
		stringRedisTemplate.boundValueOps(userName).expire(30, TimeUnit.MINUTES);
		return stringRedisTemplate.boundValueOps(userName).increment(1L);
	}

	public void delLoginErrorCount(String userName) {
		if (userName != null) {
			stringRedisTemplate.delete(userName);
		}
	}

	public AccountLine getAccountLineByNo(String accountLineNo) {
		AccountLine accountLine = new AccountLine();
		accountLine = getAccountLineByNo(accountLineNo, BaseConsts.ZERO);
		return accountLine;
	}

	/**
	 * 根据新科目编号查询 0 老凭证 1 新凭证
	 * 
	 * @param accountLineNo
	 * @param lineState
	 * @return
	 */
	public AccountLine getAccountLineByNo(String accountLineNo, Integer lineState) {
		AccountLine accountLine = new AccountLine();
		List<AccountLine> accountLines = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTLINE).values();
		if (accountLines == null) {
			return accountLine;
		}
		// 查询老科目
		if (lineState == BaseConsts.ZERO) {
			for (AccountLine item : accountLines) {
				if (item.getAccountLineNo().equals(accountLineNo) && item.getAccoutLineState() == BaseConsts.ZERO) {
					accountLine = item;
					break;
				}
			}
		}
		if (lineState == BaseConsts.ONE) {
			for (AccountLine item : accountLines) {
				if (item.getAccountLineNo().equals(accountLineNo) && item.getAccoutLineState() == BaseConsts.ONE) {
					accountLine = item;
					break;
				}
			}
		}
		return accountLine;
	}

	public AccountLine getAccountLineById(Integer id) {
		if (id == null || id < 0) {
			return new AccountLine();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTLINE).get(String.valueOf(id));
		if (obj == null) {
			AccountLine accountLine = accountLineDao.queryEntityById(id);
			if (accountLine != null) {
				objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTLINE).put(String.valueOf(accountLine.getId()),
						accountLine);
				return accountLine;
			}
		}
		return (AccountLine) obj;
	}

	public AccountBook getAccountBookById(Integer id) {
		if (id == null || id < 0) {
			return new AccountBook();
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTBOOK).get(String.valueOf(id));
		if (obj == null) {
			AccountBook accountBook = accountBookDao.queryEntityById(id);
			if (accountBook != null) {
				objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTBOOK).put(String.valueOf(accountBook.getId()),
						accountBook);
				return accountBook;
			}
		}
		return (AccountBook) obj;
	}

	public AccountBook getAccountBookByBusiUnitAndState(Integer busiUnit) {
		AccountBook accountBook = new AccountBook();
		if (busiUnit == null || busiUnit < 0) {
			return accountBook;
		}
		List<AccountBook> accountBooks = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTBOOK).values();
		for (AccountBook item : accountBooks) {
			if (item.getBusiUnit().equals(busiUnit) && item.getState() == BaseConsts.TWO) {
				accountBook = item;
				break;
			}
		}
		return accountBook;
	}

	/**
	 * 根据编号获取主体
	 *
	 * @param no
	 * @param subjectList
	 * @return
	 */
	private BaseSubject getSubjectByNo(String no, List<BaseSubject> subjectList) {
		if (CollectionUtils.isNotEmpty(subjectList)) {
			for (BaseSubject subject : subjectList) {
				if (no.equalsIgnoreCase(subject.getSubjectNo())) {
					return subject;
				}
			}
		}
		return null;
	}

	/**
	 * 获取业务目标值
	 * 
	 * @param profitId
	 * @return
	 */
	public ProfitTarget getProfitTarget(Integer profitId) {
		return profitTargetDao.queryEntityById(profitId);
	}

	/**
	 * 获取事项管理
	 * 
	 * @param matterId
	 * @return
	 */
	public MatterManage getMatterService(Integer matterId) {
		return matterManageDao.queryEntityById(matterId);
	}
}
