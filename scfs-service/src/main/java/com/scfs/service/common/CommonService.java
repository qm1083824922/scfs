package com.scfs.service.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.dao.base.entity.BaseUserDao;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.base.entity.DictRegion;
import com.scfs.domain.base.model.Department;
import com.scfs.domain.common.dto.req.RollbackOrderReqDto;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.rpc.cache.ObjectRedisTemplate;
import com.scfs.service.export.CustomsApplyService;
import com.scfs.service.export.RefundApplyService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.invoice.InvoiceApplyService;
import com.scfs.service.invoice.InvoiceCollectService;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.pay.MergePayOrderService;
import com.scfs.service.pay.PayService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Administrator on 2016/11/3.
 */
@Service
public class CommonService {

	@Autowired
	private ObjectRedisTemplate objectRedisTemplate;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BillInStoreService billInStoreService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private PayService payService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private FeeServiceImpl feeService;
	@Autowired
	private InvoiceApplyService invoiceApplyService;
	@Autowired
	private CustomsApplyService customsApplyService;
	@Autowired
	private InvoiceCollectService invoiceCollectService;
	@Autowired
	private RefundApplyService refundApplyService;
	@Autowired
	private MergePayOrderService mergePayOrderService;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;
	@Autowired
	private BaseUserDao baseUserDao;

	/**
	 * 获取所有下拉选项
	 *
	 * @param key
	 * @param pId
	 * @return
	 */
	@IgnoreTransactionalMark
	public List<CodeValue> queryAllSelectedByKey(String key, String pId) {
		if (StringUtils.isEmpty(key)) {
			return Lists.newArrayList();
		}

		if (StringUtils.isEmpty(pId)) {
			return getAllCdByKey(key);
		} else {
			return getAllOwnCv(key, pId);
		}
	}

	/**
	 * 判断当前登录用户是否拥有此URL权限
	 *
	 * @param url
	 * @return
	 */
	@IgnoreTransactionalMark
	public boolean isOwnPermUrl(String url) {
		boolean result = ServiceSupport.isAllowPerm(url);
		if (url.equals(BusUrlConsts.SUBMIT_BILL_IN_STORE) || url.equals(BusUrlConsts.SUBMIT_BILL_OUT_STORE)
				|| url.equals(BusUrlConsts.SEND_BILL_OUT_STORE)) {// 入库单提交，出库单提交和送货
			BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();
			baseReq.setUserId(ServiceSupport.getUser().getId());
			List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
			if (CollectionUtils.isEmpty(userSubject)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * @param projectId
	 * @param name
	 * @return
	 */
	@IgnoreTransactionalMark
	public List<BaseGoods> queryProGoods(Integer projectId, String name) {
		List<BaseGoods> goodsList = Lists.newArrayList();
		if (projectId == null || name == null || name.length() < 2) {
			return goodsList;
		}
		name = name.toLowerCase();
		Object cvList = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT_GOODS).get(String.valueOf(projectId));
		if (cvList != null) {
			List<ProjectGoods> projectGoodsList = (List<ProjectGoods>) cvList;
			int count = 0;
			for (ProjectGoods projectGoods : projectGoodsList) {
				if (count >= 10) {// 显示10个商品
					break;
				}
				if (projectGoods.getStatus() == BaseConsts.ONE && projectGoods.getIsDelete() == BaseConsts.ZERO) {
					BaseGoods goods = cacheService.getGoodsById(projectGoods.getGoodsId());
					if (goods.getStatus() == BaseConsts.TWO) {
						if (StringUtils.contains(goods.getName() != null ? goods.getName().toLowerCase() : null, name)
								|| StringUtils.contains(goods.getType() != null ? goods.getType().toLowerCase() : null,
										name)
								|| StringUtils.contains(
										goods.getNumber() != null ? goods.getNumber().toLowerCase() : null, name)
								|| StringUtils.contains(
										goods.getBarCode() != null ? goods.getBarCode().toLowerCase() : null, name)) {
							goodsList.add(goods);
							count++;
						}
					}
				}
			}
		}
		return goodsList;
	}

	public List<CodeValue> getAllCdByKey(String key) {
		switch (key) {
		case CacheKeyConsts.CUSTOMER:
		case CacheKeyConsts.BUSI_UNIT:
		case CacheKeyConsts.WAREHOUSE:
		case CacheKeyConsts.SUPPLIER:
			return sortCvList(getAllSubject(key));
		case CacheKeyConsts.PROJECT:
			return sortCvList(getAllProjcet(key));
		case CacheKeyConsts.USER:
			return getAllUser(key);
		case CacheKeyConsts.ACCOUNT:
			return getAllAccount(key);
		case CacheKeyConsts.NATIOIN_DICT_REGION:
			return getAllNation(key);
		case CacheKeyConsts.ACCOUNTBOOK:
			return getAllAccountBook(key);
		case CacheKeyConsts.ACCOUNTLINE:
			return getAllAccountLine(key);
		case CacheKeyConsts.ACCOUNTLINE_LAST:
			return getAllLastAccountLine(CacheKeyConsts.ACCOUNTLINE);
		case CacheKeyConsts.BCS:// 经营单位 客户 供应商
			return sortCvList(getAllBcs());
		case CacheKeyConsts.PAY_FEE_SPEC:// 应付费用
		case CacheKeyConsts.REC_FEE_SPEC:// 应收费用
		case CacheKeyConsts.REC_PAY_FEE_SPEC:// 应付 应收
		case CacheKeyConsts.PAY_FEE_MANAGE:// 管理费用
		case CacheKeyConsts.PAY_FEE_ARTIFICIAL:// 人工费用
			return getAllFee(key);
		case CacheKeyConsts.PROJECT_ALL_CUSTOMER:// 用户项目下所有的客户
			return getAllProjcetCustomers();
		case CacheKeyConsts.USER_PROJECT:
			return sortCvList(getUserProject(key, ServiceSupport.getUser().getId() + ""));// 当前登录用户下的项目
		default:
			return getBizConstant(key);// 常量类
		}

	}

	public List<CodeValue> getAllOwnCv(String key, String pId) {
		switch (key) {
		case CacheKeyConsts.BUSI_UNIT_PROJECT:
			return sortCvList(getBusiUnitProject(key, pId));
		case CacheKeyConsts.DEPARTMENT_USER_PROJECT:
			return sortCvList(getDepartUserProject(key, pId));
		case CacheKeyConsts.PROJECT_BUSI_UNIT:
			return sortCvList(getProjectBusiUnit(key, pId));
		case CacheKeyConsts.PROJECT_CUSTOMER:
			return sortCvList(getProjectSubject(key, pId, CacheKeyConsts.CUSTOMER));
		case CacheKeyConsts.CUSTOMER_PROJECT:
			return sortCvList(getSubjectProject(key, pId));

		case CacheKeyConsts.PROJECT_CS:// 经营单位 客户
			return sortCvList(getProjectCS(key, pId));
		case CacheKeyConsts.PROJECT_WAREHOUSE:
			return sortCvList(getProjectSubject(key, pId, CacheKeyConsts.WAREHOUSE));
		case CacheKeyConsts.PROJECT_VIRTUAL_WAREHOUSE:
			return sortCvList(getProjectSubject(key, pId, CacheKeyConsts.WAREHOUSE));
		case CacheKeyConsts.PROJECT_SUPPLIER:
			return sortCvList(getProjectSubject(key, pId, CacheKeyConsts.SUPPLIER));
		case CacheKeyConsts.DICT_REGION_MAP:
			return getDictRegion(key, pId);
		case CacheKeyConsts.PROJECT_GOODS:
			return getProjectGoods(key, pId);
		case CacheKeyConsts.PROJECT_BCS:
			return sortCvList(getProjectBcs(key, pId));
		case CacheKeyConsts.SUPPLIER_PROJECT:
			return getSubjectProject(key, pId);
		case CacheKeyConsts.SUBJECT_ACCOUNT:
			return getSubjectAccount(key, pId);
		case CacheKeyConsts.SUBJECT_ADDRESS:
			return getSubjectAddress(key, pId);
		case CacheKeyConsts.SUBJECT_INVICE:
			return getSubjectInvoice(key, pId);
		case CacheKeyConsts.DEPARTMENT_USER:
			return getDepartmentUser(key, pId);
		case CacheKeyConsts.PROJECT_USER:
			return getProjectUser(key, pId);
		default:// 获取上下级常量类
			return getPbizConstant(key, pId);// 常量类
		}
	}

	private List<CodeValue> getDepartUserProject(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();

		// 获取所有部门
		List<BaseDepartment> baseDepartmentList = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT).values();
		List<Integer> ids = Lists.newArrayList();
		ids.add(Integer.valueOf(pId));
		List<Department> childList = Lists.newArrayList();
		// 获取此部门下的所有下级部门
		if (CollectionUtils.isNotEmpty(baseDepartmentList)) {
			BaseDepartment baseDepartment = new BaseDepartment();// 根节点
			baseDepartment.setId(Integer.valueOf(pId));
			buildDepartment(baseDepartment, baseDepartmentList, childList, ids);
		}

		List<BaseProject> subList = Lists.newArrayList();
		for (Integer id : ids) {
			Object list = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT_PROJECT).get(id.toString());// 部门下的项目
			if (list != null) {
				subList.addAll((List<BaseProject>) list);
			}
		}
		if (CollectionUtils.isNotEmpty(subList)) {
			Object userProlist = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PROJECT)
					.get(ServiceSupport.getUser().getId().toString());// 获取当前登陆用户下的项目
			if (userProlist != null) {
				List<BaseUserProject> userProjectList = (List<BaseUserProject>) userProlist;
				for (BaseProject bp : subList) {
					for (BaseUserProject userProject : userProjectList) {
						if (bp.getId().intValue() == userProject.getProjectId().intValue()
								&& userProject.getIsDelete() == BaseConsts.ZERO
								&& userProject.getState() == BaseConsts.ONE) {
							if (getProjectCV(bp) != null) {
								cvList.add(getProjectCV(bp));
							}
							break;
						}
					}
				}
			}

		}
		return cvList;
	}

	private List<CodeValue> getSubjectProject(String cacheKey, String pId) {// 供应商ID
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<ProjectSubject> subList = (List<ProjectSubject>) list;
			for (ProjectSubject ps : subList) {
				if (ps.getStatus() == BaseConsts.ONE && ps.getIsDelete() == BaseConsts.ZERO) {// 可用
					BaseProject baseProject = cacheService.getProjectById(ps.getProjectId());
					CodeValue cv = getProjectCV(baseProject);
					if (cv != null) {
						cvList.add(cv);
					}
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getAllProjcetCustomers() {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PROJECT)
				.get(ServiceSupport.getUser().getId().toString());
		if (list != null) {
			List<BaseUserProject> subList = (List<BaseUserProject>) list;
			for (BaseUserProject up : subList) {
				if (up.getState() == BaseConsts.ONE && up.getIsDelete() == BaseConsts.ZERO) {
					List<CodeValue> pcList = getProjectSubject(CacheKeyConsts.PROJECT_CUSTOMER,
							up.getProjectId().toString(), CacheKeyConsts.CUSTOMER);
					cvList.addAll(pcList);

				}
			}
		}
		Set<CodeValue> set = new TreeSet<CodeValue>(new Comparator<CodeValue>() {
			@Override
			public int compare(CodeValue o1, CodeValue o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});
		set.addAll(cvList);
		return new ArrayList<>(set);
	}

	private List<CodeValue> getDepartmentUser(String key, String pId) {
		List<CodeValue> resultList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(key).get(pId);
		if (list != null) {
			List<BaseUser> userList = (List<BaseUser>) list;
			for (BaseUser user : userList) {
				if (user.getIsDelete() == BaseConsts.ZERO && user.getStatus() == BaseConsts.ZERO
						&& user.getUserProperty() == BaseConsts.ZERO) {
					CodeValue cv = new CodeValue(String.valueOf(user.getId()), user.getChineseName());
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private List<CodeValue> getPbizConstant(String key, String pId) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS_PARENT).get(key + "_" + pId);
		if (obj != null) {
			List<CodeValue> bizConstantList = (List<CodeValue>) obj;
			return bizConstantList;
		}
		return null;
	}

	private List<CodeValue> getDictRegion(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			cvList = (List<CodeValue>) list;
			return cvList;
		}
		return cvList;
	}

	public List<CodeValue> getBizConstant(String key) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS).get(key);
		if (obj != null) {
			List<CodeValue> bizConstantList = (List<CodeValue>) obj;
			return bizConstantList;
		}
		return null;
	}

	private List<CodeValue> getSubjectInvoice(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseInvoice> subList = (List<BaseInvoice>) list;
			for (BaseInvoice invocie : subList) {
				if (invocie.getState() == BaseConsts.ONE) {// 可用
					CodeValue cv = new CodeValue(invocie.getId() + "", invocie.getShowValue());
					cvList.add(cv);
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getSubjectAddress(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseAddress> subList = (List<BaseAddress>) list;
			for (BaseAddress address : subList) {
				if (address.getState() == BaseConsts.ONE) {// 可用
					CodeValue cv = new CodeValue(address.getId() + "", address.getShowValue());
					cvList.add(cv);
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getSubjectAccount(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseAccount> subList = (List<BaseAccount>) list;
			for (BaseAccount account : subList) {
				if (account.getState() == BaseConsts.ONE) {// 可用
					CodeValue cv = new CodeValue(String.valueOf(account.getId()), account.getShowValue());
					cvList.add(cv);
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getProjectGoods(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<ProjectGoods> subList = (List<ProjectGoods>) list;
			for (ProjectGoods ps : subList) {
				if (ps.getStatus() == BaseConsts.ONE && ps.getIsDelete() == BaseConsts.ZERO) {// 可用
					CodeValue cv = cacheService.getGoodsCvById(ps.getGoodsId());
					cvList.add(cv);
				}
			}
		}
		return cvList;
	}

	public List<CodeValue> getProjectBcs(String key, String pId) {// 组合 项目下的经营单位
																	// 客户 供应商
		List<CodeValue> resultList = Lists.newArrayList();
		List<CodeValue> cvList = getProjectBusiUnit(CacheKeyConsts.PROJECT_BUSI_UNIT, pId);
		addNotExsitCv(cvList, resultList);
		cvList = getProjectSubject(CacheKeyConsts.PROJECT_CUSTOMER, pId, CacheKeyConsts.CUSTOMER);
		addNotExsitCv(cvList, resultList);
		cvList = getProjectSubject(CacheKeyConsts.PROJECT_SUPPLIER, pId, CacheKeyConsts.SUPPLIER);
		addNotExsitCv(cvList, resultList);
		return resultList;
	}

	private List<CodeValue> getProjectCS(String key, String pId) {// 组合 项目下的 客户
																	// 供应商
		List<CodeValue> resultList = Lists.newArrayList();
		List<CodeValue> cvList = getProjectSubject(CacheKeyConsts.PROJECT_CUSTOMER, pId, CacheKeyConsts.CUSTOMER);
		addNotExsitCv(cvList, resultList);
		cvList = getProjectSubject(CacheKeyConsts.PROJECT_SUPPLIER, pId, CacheKeyConsts.SUPPLIER);
		addNotExsitCv(cvList, resultList);
		return resultList;
	}

	private void addNotExsitCv(List<CodeValue> newCvList, List<CodeValue> oldCvList) {
		if (CollectionUtils.isNotEmpty(newCvList)) {
			for (CodeValue cv : newCvList) {
				if (!oldCvList.contains(cv)) {
					oldCvList.add(cv);
				}

			}
		}
	}

	private List<CodeValue> getProjectSubject(String cacheKey, String pId, String subjectKey) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<ProjectSubject> subList = (List<ProjectSubject>) list;
			for (ProjectSubject ps : subList) {
				if (ps.getStatus() == BaseConsts.ONE && ps.getIsDelete() == BaseConsts.ZERO) {// 可用
					BaseSubject baseSubject = cacheService.getSubjectById(ps.getSubjectId(), subjectKey);
					if (baseSubject.getIsDelete() == BaseConsts.ZERO && baseSubject.getState() == BaseConsts.TWO
							&& isSubjectType(baseSubject.getSubjectType(), subjectKey)) {
						CodeValue cv = cacheService.getSubjectCvByIdAndKey(ps.getSubjectId(), subjectKey);
						cvList.add(cv);
					}
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getProjectBusiUnit(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseProject> subList = (List<BaseProject>) list;
			for (BaseProject bp : subList) {
				if (bp.getStatus() == BaseConsts.TWO && bp.getIsDelete() == BaseConsts.ZERO) {// 可用
					BaseSubject baseSubject = cacheService.getSubjectById(bp.getBusinessUnitId(),
							CacheKeyConsts.BUSI_UNIT);
					if (baseSubject.getIsDelete() == BaseConsts.ZERO && baseSubject.getState() == BaseConsts.TWO
							&& isSubjectType(baseSubject.getSubjectType(), CacheKeyConsts.BUSI_UNIT)) {
						CodeValue cv = new CodeValue(bp.getBusinessUnitId() + "", baseSubject.getNoName());
						cvList.add(cv);
					}
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getBusiUnitProject(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseProject> subList = (List<BaseProject>) list;
			for (BaseProject bp : subList) {
				CodeValue cv = getProjectCV(bp);
				if (cv != null) {
					cvList.add(cv);
				}
			}
		}
		return cvList;
	}

	public List<CodeValue> getUserProject(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseUserProject> subList = (List<BaseUserProject>) list;
			for (BaseUserProject up : subList) {
				if (up.getState() == BaseConsts.ONE && up.getIsDelete() == BaseConsts.ZERO) {
					BaseProject project = cacheService.getProjectById(up.getProjectId());
					if (project.getStatus() == BaseConsts.TWO && project.getIsDelete() == BaseConsts.ZERO) {
						CodeValue cv = new CodeValue(String.valueOf(up.getProjectId()), project.getNoName());
						cvList.add(cv);
					}
				}
			}
		}
		return cvList;
	}

	public List<CodeValue> getProjectUser(String cacheKey, String pId) {
		List<CodeValue> cvList = Lists.newArrayList();
		Object list = objectRedisTemplate.boundHashOps(cacheKey).get(pId);
		if (list != null) {
			List<BaseUserProject> subList = (List<BaseUserProject>) list;
			for (BaseUserProject up : subList) {
				if (up.getState() == BaseConsts.ONE && up.getIsDelete() == BaseConsts.ZERO) {
					BaseUser user = cacheService.getUserByid(up.getUserId());
					if (user.getStatus() == BaseConsts.ZERO && user.getIsDelete() == BaseConsts.ZERO) {
						CodeValue cv = new CodeValue(String.valueOf(up.getUserId()), user.getChineseName());
						cvList.add(cv);
					}
				}
			}
		}
		return cvList;
	}

	private List<CodeValue> getAllBcs() {
		List<CodeValue> resultList = Lists.newArrayList();
		List<BaseSubject> subjectList = objectRedisTemplate.boundHashOps(CacheKeyConsts.BUSI_UNIT).values();
		List<CodeValue> cvList = getCodeValueList(subjectList, CacheKeyConsts.BUSI_UNIT);
		addNotExsitCv(cvList, resultList);
		subjectList = objectRedisTemplate.boundHashOps(CacheKeyConsts.CUSTOMER).values();
		cvList = getCodeValueList(subjectList, CacheKeyConsts.CUSTOMER);
		addNotExsitCv(cvList, resultList);
		subjectList = objectRedisTemplate.boundHashOps(CacheKeyConsts.SUPPLIER).values();
		cvList = getCodeValueList(subjectList, CacheKeyConsts.SUPPLIER);
		addNotExsitCv(cvList, resultList);
		return resultList;
	}

	private List<CodeValue> getAllAccount(String key) {
		List<BaseAccount> accountList = objectRedisTemplate.boundHashOps(key).values();
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(accountList)) {
			for (BaseAccount account : accountList) {
				if (account.getState() == BaseConsts.ONE) {// 1表示可用
					CodeValue cv = new CodeValue(account.getId() + "", account.getShowValue());
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private List<CodeValue> getAllLastAccountLine(String key) {
		List<AccountLine> accountBookList = objectRedisTemplate.boundHashOps(key).values();
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(accountBookList)) {
			for (AccountLine accountLine : accountBookList) {
				if (accountLine.getIsDelete() == BaseConsts.ZERO && accountLine.getState() == BaseConsts.TWO
						&& accountLine.getIsLast() == BaseConsts.ONE) {
					CodeValue cv = new CodeValue(accountLine.getId() + "", accountLine.getNameNo());
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private List<CodeValue> getAllAccountLine(String key) {
		List<AccountLine> accountBookList = objectRedisTemplate.boundHashOps(key).values();
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(accountBookList)) {
			for (AccountLine accountLine : accountBookList) {
				if (accountLine.getIsDelete() == BaseConsts.ZERO && accountLine.getState() == BaseConsts.TWO) {
					CodeValue cv = new CodeValue(accountLine.getId() + "", accountLine.getNameNo());
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private List<CodeValue> getAllAccountBook(String key) {
		List<AccountBook> accountBookList = objectRedisTemplate.boundHashOps(key).values();
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(accountBookList)) {
			for (AccountBook accountBook : accountBookList) {
				if (accountBook.getIsDelete() == BaseConsts.ZERO && accountBook.getState() == BaseConsts.TWO) {
					CodeValue cv = new CodeValue(accountBook.getId() + "", accountBook.getNameNo());
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private List<CodeValue> getAllNation(String key) {
		List<DictRegion> dictRegionList = objectRedisTemplate.opsForList().range(CacheKeyConsts.NATIOIN_DICT_REGION, 0,
				-1);
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(dictRegionList)) {
			for (DictRegion dict : dictRegionList) {
				CodeValue cv = new CodeValue(dict.getRegionId() + "", dict.getRegionName());
				resultList.add(cv);
			}
		}
		return resultList;
	}

	private List<CodeValue> getAllUser(String key) {
		/**
		 * List<BaseUser> userList =
		 * objectRedisTemplate.boundHashOps(key).values(); List<CodeValue>
		 * resultList = Lists.newArrayList(); if
		 * (CollectionUtils.isNotEmpty(userList)) { for (BaseUser user :
		 * userList) { if (user.getIsDelete() == BaseConsts.ZERO &&
		 * user.getStatus() == BaseConsts.ZERO && user.getUserProperty() ==
		 * BaseConsts.ZERO) { CodeValue cv = new
		 * CodeValue(String.valueOf(user.getId()), user.getChineseName());
		 * resultList.add(cv); } } }
		 **/
		BaseUser baseUser = new BaseUser();
		baseUser.setIsDelete(BaseConsts.ZERO);
		baseUser.setStatus(BaseConsts.ZERO);
		baseUser.setUserProperty(BaseConsts.ZERO);
		List<CodeValue> resultList = Lists.newArrayList();
		List<BaseUser> userList = baseUserDao.queryBaseUserList(baseUser);
		for (BaseUser user : userList) {
			CodeValue cv = new CodeValue(String.valueOf(user.getId()), user.getChineseName());
			resultList.add(cv);
		}

		// JDKz自带对数组进行排序。
		Collections.sort(resultList, new Comparator<CodeValue>() {
			@Override
			public int compare(CodeValue o1, CodeValue o2) {
				String value1 = o1.getValue();
				String value2 = o2.getValue();
				return populatePinYing(value1).compareTo(populatePinYing(value2));
			}

			/**
			 * 判断一个字符是否是中文字符
			 */
			private boolean isChineseCharacter(char c) {
				return String.valueOf(c).matches("[\\u4E00-\\u9FA5]+");
			}

			/**
			 * 将一个含有中文的字符串转换成拼音。 Note： 这里只是将中文转换成拼音，其它的各种字符将保持原来的样子。
			 */
			public String populatePinYing(String aChineseValue) {

				if (null == aChineseValue) {
					return null;
				}

				StringBuilder sb = new StringBuilder();
				char[] charArray = aChineseValue.toCharArray();

				HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
				outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
				outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
				outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
				if (isChineseCharacter(charArray[0])) {
					try {
						sb.append(PinyinHelper.toHanyuPinyinStringArray(charArray[0], outputFormat)[0]);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
					}
				} else {
					sb.append(charArray[0]);
				}
				return sb.toString();
			}
		});
		return resultList;
	}

	private List<CodeValue> getAllProjcet(String key) {
		List<BaseProject> projectList = objectRedisTemplate.boundHashOps(key).values();
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(projectList)) {
			for (BaseProject baseProject : projectList) {
				CodeValue cv = getProjectCV(baseProject);
				if (cv != null) {
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private CodeValue getProjectCV(BaseProject baseProject) {
		if (baseProject.getIsDelete() == BaseConsts.ZERO && baseProject.getStatus() == BaseConsts.TWO) {
			CodeValue cv = new CodeValue(baseProject.getId() + "", baseProject.getNoName());
			return cv;
		} else {
			return null;
		}
	}

	private List<CodeValue> getAllSubject(String cacheKey) {
		List<BaseSubject> subjectList = objectRedisTemplate.boundHashOps(cacheKey).values();
		return getCodeValueList(subjectList, cacheKey);
	}

	private List<CodeValue> getCodeValueList(List<BaseSubject> subjectList, String cacheKey) {
		List<CodeValue> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(subjectList)) {
			for (BaseSubject baseSubject : subjectList) {
				if (baseSubject.getIsDelete() == BaseConsts.ZERO && baseSubject.getState() == BaseConsts.TWO
						&& isSubjectType(baseSubject.getSubjectType(), cacheKey)) {
					CodeValue cv = new CodeValue(baseSubject.getId() + "", baseSubject.getNoName());
					resultList.add(cv);
				}
			}
		}
		return resultList;
	}

	private static boolean isSubjectType(int type, String cacheKey) {
		int aimType = 2;// 默认是仓库
		switch (cacheKey) {
		case CacheKeyConsts.BUSI_UNIT:
			aimType = 1;
			break;
		case CacheKeyConsts.SUPPLIER:
			aimType = 4;
			break;
		case CacheKeyConsts.CUSTOMER:
			aimType = 8;
			break;
		}
		return (type & aimType) == aimType;
	}

	public List<CodeValue> getAllFee(String key) {
		List<CodeValue> resultList = Lists.newArrayList();
		List<FeeSpec> feeList = objectRedisTemplate.boundHashOps(key).values();
		if (CollectionUtils.isNotEmpty(feeList)) {
			for (FeeSpec fee : feeList) {
				CodeValue cv = new CodeValue(fee.getId() + "", fee.getNameNo());
				resultList.add(cv);
			}
		}
		return resultList;
	}

	/**
	 * 根据部门ID获取部门用户信息集合
	 *
	 * @param departId
	 * @return
	 */
	public List<BaseUser> getUsersByDepartId(int departId) {
		List<BaseUser> userList = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER).values();
		List<BaseUser> resultList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(userList)) {
			for (BaseUser user : userList) {
				if (user.getIsDelete() == BaseConsts.ZERO && user.getStatus() == BaseConsts.ZERO) {
					if (user.getDepartmentId() == departId) {
						resultList.add(user);
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取用户所在部门下的所有部门
	 *
	 * @return
	 */
	public Department getUserDepartments() {
		List<BaseDepartment> baseDepartmentList = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT).values();
		Department departments = new Department();
		List<Department> childList = Lists.newArrayList();
		departments.setChildren(childList);
		Integer baseDepartmentId = ServiceSupport.getUser().getDepartmentId();
		BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseDepartmentId);
		if (baseDepartment != null) {
			departments.setId(baseDepartment.getId());
			departments.setName(baseDepartment.getNameNo());
			buildDepartment(baseDepartment, baseDepartmentList, departments.getChildren(), null);
		}
		return departments;
	}

	/**
	 * 获取所有部门
	 *
	 * @return
	 */
	public Department getAllDepartment() {
		List<BaseDepartment> baseDepartmentList = objectRedisTemplate.boundHashOps(CacheKeyConsts.DEPARTMENT).values();
		Department departments = new Department();
		List<Department> childList = Lists.newArrayList();
		departments.setChildren(childList);
		if (CollectionUtils.isNotEmpty(baseDepartmentList)) {
			for (BaseDepartment baseDepartment : baseDepartmentList) {
				if (baseDepartment.getParentId() == null) {// 根节点
					departments.setId(baseDepartment.getId());
					departments.setName(baseDepartment.getNameNo());
					buildDepartment(baseDepartment, baseDepartmentList, departments.getChildren(), null);
					break;
				}
			}
		}
		return departments;
	}

	private void buildDepartment(BaseDepartment baseDepartment, List<BaseDepartment> baseDepartmentList,
			List<Department> departList, List<Integer> ids) {
		List<BaseDepartment> childrens = getChildrenDepartment(baseDepartment, baseDepartmentList);
		if (CollectionUtils.isNotEmpty(childrens)) {
			for (BaseDepartment nextDepartment : childrens) {
				Department childDepart = new Department(nextDepartment.getId(), nextDepartment.getNameNo());
				if (CollectionUtils.isNotEmpty(ids)) {
					ids.add(nextDepartment.getId());
				}
				List<Department> childNextDeparts = Lists.newArrayList();
				childDepart.setChildren(childNextDeparts);
				departList.add(childDepart);
				Collections.sort(departList);
				buildDepartment(nextDepartment, baseDepartmentList, childDepart.getChildren(), ids);
			}
		}
	}

	private List<BaseDepartment> getChildrenDepartment(BaseDepartment baseDepartment,
			List<BaseDepartment> nextDepartList) {
		List<BaseDepartment> nextPepartments = Lists.newArrayList();
		for (BaseDepartment nextBaseDepart : nextDepartList) {
			if (nextBaseDepart.getParentId() != null
					&& baseDepartment.getId().intValue() == nextBaseDepart.getParentId().intValue()
					&& nextBaseDepart.getIsDelete() == BaseConsts.ZERO) {
				nextPepartments.add(nextBaseDepart);
			}
		}
		return nextPepartments;
	}

	/**
	 * 更新打印次数
	 *
	 * @param id
	 * @param billType
	 */
	public void updatePrintNum(Integer id, Integer billType) {
		// 单据类型 1-采购订单 2-销售单 3-出库单 4-入库单 5-付款单，其余类型按需增加
		// 6-应收费用，7-应付费用，8-应收应付费用，9-开票，10-收票，11-报关申请，12-退税申请, 14-合并付款

		if (billType.equals(BaseConsts.TWO)) {
			billDeliveryService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.THREE)) {
			billOutStoreService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.FOUR)) {
			billInStoreService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.FIVE)) {
			payService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.ONE)) {
			purchaseOrderService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.SIX)) {
			feeService.updatePrintNum(id, BaseConsts.SIX);
		}
		if (billType.equals(BaseConsts.SEVEN)) {
			feeService.updatePrintNum(id, BaseConsts.SEVEN);
		}
		if (billType.equals(BaseConsts.EIGHT)) {
			feeService.updatePrintNum(id, BaseConsts.EIGHT);
		}
		if (billType.equals(BaseConsts.NINE)) {
			invoiceApplyService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.TEN)) {
			invoiceCollectService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.INT_11)) {
			customsApplyService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.INT_12)) {
			refundApplyService.updatePrintNum(id);
		}
		if (billType.equals(BaseConsts.INT_14)) {
			mergePayOrderService.updatePrintNum(id);
		}
	}

	/**
	 * 批量更新打印次数
	 *
	 * @param ids
	 * @param billType
	 */
	public void batchUpdatePrintNum(String ids, Integer billType) {
		// 单据类型 1-采购订单 2-销售单 3-出库单 4-入库单 5-付款单，其余类型按需增加
		// 6-应收费用，7-应付费用，8-应收应付费用，9-开票，10-收票，11-报关申请，12-退税申请

		if (billType.equals(BaseConsts.FIVE)) {
			payService.batchUpdatePrintNum(ids);
		}
		// 解析当前传递的IDS
		List<String> list = Arrays.asList(ids.split(","));
		if (!CollectionUtils.isEmpty(list)) {
			for (String str : list) {
				Integer id = Integer.valueOf(str);
				if (billType.equals(BaseConsts.ONE)) {
					purchaseOrderService.updatePrintNum(id);
				}
				if (billType.equals(BaseConsts.THREE)) {
					billOutStoreService.updatePrintNum(id);
				}
			}
		}
	}

	private List<CodeValue> sortCvList(List<CodeValue> codeValueList) {
		Collections.sort(codeValueList);
		return codeValueList;
	}

	/**
	 * TODO 驳回
	 * 
	 * @param rollbackOrderReqDto
	 */
	public void rollbackOrder(RollbackOrderReqDto rollbackOrderReqDto) {

	}

	public String getExceptionMsg(Exception e) {
		String exceptionMsg = e.getMessage();
		if (StringUtils.isNotBlank(exceptionMsg)) {
			exceptionMsg = exceptionMsg.substring(0, exceptionMsg.length() > 500 ? 500 : exceptionMsg.length());
		}
		return exceptionMsg;
	}

	public String getMsg(String msg) {
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.substring(0, msg.length() > 255 ? 255 : msg.length());
		}
		return msg;
	}

}
