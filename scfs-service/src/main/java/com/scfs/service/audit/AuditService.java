package com.scfs.service.audit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.audit.AuditDao;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.audit.dto.req.AuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.dto.resp.AuditResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.pay.entity.MergePayOrder;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.service.base.user.UserWechatService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.scfs.service.util.ApplicationContextHolder;

/**
 * Created by Administrator on 2016/10/31.
 */
@Service
public class AuditService {
	@Autowired
	private AuditDao auditDao;
	@Value("${domain}")
	private String domain;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private UserWechatService userWechatService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;
	@Autowired
	private ProjectSubjectDao projectSubjectDao;

	private static Map<Integer, String> typeServiceMap = new ConcurrentHashMap<Integer, String>();

	static {
		typeServiceMap.put(1, "poAuditService");
		typeServiceMap.put(2, "billDeliveryAuditService");
		typeServiceMap.put(3, "billOutStoreAuditService");
		typeServiceMap.put(4, "feeAuditService");
		typeServiceMap.put(5, "payAuditService");
		typeServiceMap.put(6, "voucherAuditService");
		typeServiceMap.put(7, "projectItemAuditService");
		typeServiceMap.put(8, "feeAuditService");
		typeServiceMap.put(9, "feeAuditService");
		typeServiceMap.put(10, "invoiceAuditService");
		typeServiceMap.put(11, "invoiceCollectAuditService");
		typeServiceMap.put(12, "refundApplyAuditService");
		typeServiceMap.put(14, "mergePayAuditService");
		typeServiceMap.put(16, "projectPoolAdjustAuditService");
		typeServiceMap.put(17, "billReturnAuditService");
		typeServiceMap.put(18, "poReturnAuditService");
		typeServiceMap.put(19, "invoiceOverseasAuditService");
		typeServiceMap.put(21, "distributionGoodsAuditService");
		typeServiceMap.put(22, "distributionReturnAuditService");
		typeServiceMap.put(23, "profitTargetAuditService");
	}

	public void batchAudit(BaseReqDto baseReqDto, int checkAudit) {
		if (CollectionUtils.isNotEmpty(baseReqDto.getIds())) {
			if (checkAudit == 0) {// 0 表示批量审核通过
				for (Integer id : baseReqDto.getIds()) {
					Audit audit = auditDao.queryAuditById(id);
					AuditService auditService = (AuditService) ApplicationContextHolder
							.getBean(typeServiceMap.get(audit.getPoType()));
					auditService.batchPassAudit(audit);
				}
			} else if (checkAudit == 1) {// 1 表示批量审核不通过
				for (Integer id : baseReqDto.getIds()) {
					Audit audit = auditDao.queryAuditById(id);
					AuditService auditService = (AuditService) ApplicationContextHolder
							.getBean(typeServiceMap.get(audit.getPoType()));
					auditService.batchUnPassAudit(audit);
				}
			}
		}
	}

	public void batchPassAudit(Audit audit) {
	}

	public void batchUnPassAudit(Audit audit) {
	}

	public PageResult<AuditResDto> queryAuditResultsByCon(AuditReqDto auditReqDto) {

		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		int offSet = PageUtil.getOffSet(auditReqDto.getPage(), auditReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, auditReqDto.getPer_page());
		List<Audit> PoTitles = auditDao.queryAuditResultsByCon(auditReqDto, rowBounds);
		// 添加操作
		List<AuditResDto> poRespDto = convertToResult(PoTitles);
		result.setItems(poRespDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), auditReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(auditReqDto.getPage());
		result.setPer_page(auditReqDto.getPer_page());
		return result;
	}

	public PageResult<AuditResDto> queryAuditWechatResultsByCon(AuditReqDto auditReqDto) {

		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		int offSet = PageUtil.getOffSet(auditReqDto.getPage(), auditReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, auditReqDto.getPer_page());
		List<Audit> PoTitles = auditDao.queryAuditWechatResultsByCon(auditReqDto, rowBounds);
		// 添加操作
		List<AuditResDto> poRespDto = convertToResult(PoTitles);
		result.setItems(poRespDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), auditReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(auditReqDto.getPage());
		result.setPer_page(auditReqDto.getPer_page());
		return result;
	}

	public Result<Audit> queryAuditResultsNext(AuditReqDto auditReqDto) {
		Result<Audit> result = new Result<Audit>();
		Audit PoTitles = auditDao.queryAuditResultsNext(auditReqDto);
		result.setItems(PoTitles);
		return result;
	}

	private List<AuditResDto> convertToResult(List<Audit> PoTitles) {
		List<AuditResDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (Audit vo : PoTitles) {
			AuditResDto dto = new AuditResDto();
			dto.setId(vo.getId());
			dto.setPoId(vo.getPoId());
			dto.setPoType(vo.getPoType());
			dto.setPoNo(vo.getPoNo());
			dto.setPoDate(vo.getPoDate());
			dto.setProposerId(vo.getProposerId());
			dto.setProposer(vo.getProposer());
			dto.setProposerAt(vo.getProposerAt());
			dto.setAuditorId(vo.getAuditorId());
			dto.setAuditor(cacheService.getUserChineseNameByid(vo.getAuditorId()));
			dto.setAmount(vo.getAmount());
			dto.setBusinessUnitId(vo.getBusinessUnitId());
			dto.setBusinessUnitName(cacheService.getSubjectNoNameById(vo.getBusinessUnitId()));
			dto.setProjectId(vo.getProjectId());
			dto.setProjectName(cacheService.getProjectNameById(vo.getProjectId()));
			dto.setSupplierId(vo.getSupplierId());
			dto.setSupplierName(cacheService.getSubjectNoNameById(vo.getSupplierId()));
			dto.setCustomerId(vo.getCustomerId());
			dto.setCustomerName(cacheService.getSubjectNoNameById(vo.getCustomerId()));
			dto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, vo.getState() + ""));
			dto.setPoTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_POTYPE, vo.getPoType() + ""));
			dto.setState(vo.getState());
			dto.setCurrencyId(vo.getCurrencyId());
			if (vo.getCurrencyId() != null) {
				dto.setCurrencyName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getCurrencyId() + ""));
			}

			Date proposerDate = vo.getProposerAt();
			if (proposerDate != null) {
				Date d = new Date();
				Date pDate = DateUtils.addHours(proposerDate, BaseConsts.TWO);
				if (d.compareTo(pDate) >= 0) {
					dto.setWarn(true);
				} else {
					dto.setWarn(false);
				}
				try {
					long day = DateFormatUtils.diffDate(d, proposerDate);
					if (day > 0) {
						dto.setProposerDayTime(day + "天");
					} else {
						long time = DateFormatUtils.diffDateTime(d, proposerDate);
						BigDecimal hourTime = DecimalUtil.divide(new BigDecimal(time), new BigDecimal(60 * 60));
						dto.setProposerDayTime(DecimalUtil.formatScale2(hourTime) + "小时");
					}
				} catch (ParseException e) {
				}
			}
			poTitleList.add(dto);
		}
		return poTitleList;
	}

	/**
	 * 创建提交节点
	 *
	 * @param audit
	 */
	public void createSubmitAudit(Audit audit) {
		audit.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		audit.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		audit.setCreateAt(new Date());
		audit.setIsDelete(BaseConsts.ZERO);
		audit.setState(BaseConsts.ZERO);
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditor(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		audit.setAuditorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		audit.setAuditorPass(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion("已提交");
		auditDao.insert(audit);
	}

	/**
	 * 新增法务审核信息
	 * 
	 * @param oldAudit
	 * @return
	 */
	public int createLawAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_11);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = null;
		if (newAudit.getPoType().equals(BaseConsts.INT_27)) {// 事项管理
			user = ServiceSupport.getMatterJusticeUser(newAudit.getPoId());
		} else {
			user = ServiceSupport.getLawDepartUser(newAudit.getProjectId());
		}
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增事业部审核信息
	 * 
	 * @param oldAudit
	 * @return
	 */
	public int createCareerAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_14);
		newAudit.setAuditState(BaseConsts.ZERO);
		DistributionGoods resDto = cacheService.getDistributionGoodsById(oldAudit.getPoId());
		BaseUser user = cacheService.getUserByid(resDto.getCareerId());
		if (user == null || user.getId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"【" + ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, newAudit.getState() + "")
							+ "】节点没有配置审核人");
		}
		newAudit.setAuditor(user.getChineseName());
		newAudit.setAuditorId(user.getId());
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增采购审核信息
	 *
	 * @param oldAudit
	 */
	public int createPurchaseAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_15);
		newAudit.setAuditState(BaseConsts.ZERO);
		DistributionGoods resDto = cacheService.getDistributionGoodsById(oldAudit.getPoId());
		BaseUser user = cacheService.getUserByid(resDto.getPurchaseId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增供应链小组审核信息
	 *
	 * @param oldAudit
	 */
	public int createSupplyChainGroupAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_16);
		newAudit.setAuditState(BaseConsts.ZERO);
		DistributionGoods resDto = cacheService.getDistributionGoodsById(oldAudit.getPoId());
		BaseUser user = cacheService.getUserByid(resDto.getSupplyChainGroupId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增供应链服务部审核信息
	 *
	 * @param oldAudit
	 */
	public int createSupplyChainServiceAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_17);
		newAudit.setAuditState(BaseConsts.ZERO);
		DistributionGoods resDto = cacheService.getDistributionGoodsById(oldAudit.getPoId());
		BaseUser user = cacheService.getUserByid(resDto.getSupplyChainServiceId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增铺货商品风控审核
	 *
	 * @param oldAudit
	 */
	public int creatSupplyChainRiskAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_18);
		newAudit.setAuditState(BaseConsts.ZERO);
		DistributionGoods resDto = cacheService.getDistributionGoodsById(oldAudit.getPoId());
		BaseUser user = cacheService.getUserByid(resDto.getRiskId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增商务审核信息
	 *
	 * @param oldAudit
	 */
	public int createBizAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.TEN);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = null;
		if (newAudit.getPoType().equals(BaseConsts.INT_27)) {// 事项管理
			user = ServiceSupport.getMatterOfficalUser(newAudit.getPoId());
		} else {
			user = ServiceSupport.getOfficalUser(newAudit.getProjectId());
		}
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增业务审核信息
	 *
	 * @param oldAudit
	 */
	public int createBusiAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_20);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = null;
		if (newAudit.getPoType().equals(BaseConsts.INT_23)) {// 业务目标值
			user = ServiceSupport.getProfitBusDepartUser(newAudit.getPoId());
		} else if (newAudit.getPoType().equals(BaseConsts.INT_27)) {// 事项管理
			user = ServiceSupport.getMatterBusiUser(newAudit.getPoId());
		} else {
			user = ServiceSupport.getBusDepartUser(newAudit.getProjectId());
		}
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增财务专员审核
	 *
	 * @param oldAudit
	 */
	public int createFinanceAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_25);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = ServiceSupport.getFinanceSpecialDepartUser(newAudit.getProjectId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增财务主管审核
	 *
	 * @param oldAudit
	 */
	public int createFinance2Audit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_30);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = null;
		if (oldAudit.getPoType() == BaseConsts.FIVE && newAudit.getProjectId() == null
				&& newAudit.getBusinessUnitId() != null) {
			user = ServiceSupport.getFinanceUserByBusi(newAudit.getBusinessUnitId());
		} else {
			if (newAudit.getPoType().equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterFinanceUser(newAudit.getPoId());
			} else {
				user = ServiceSupport.getFinanceDepartUser(newAudit.getProjectId());
			}
		}
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增风控主管审核
	 *
	 * @param oldAudit
	 */
	public int createRiskAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_40);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = null;
		if (newAudit.getPoType().equals(BaseConsts.INT_27)) {// 事项管理
			user = ServiceSupport.getMatterRiskUser(newAudit.getPoId());
		} else {
			user = ServiceSupport.getRiskDepartUser(newAudit.getProjectId());
		}
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增风控专员审核
	 * 
	 * @param oldAudit
	 * @return
	 */
	public int createRiskSpecialAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_35);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = ServiceSupport.getRiskSpecialDepartUser(newAudit.getProjectId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增商务审核
	 * 
	 * @param oldAudit
	 * @return
	 */
	public int creatRiskBusinessAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.TEN);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = ServiceSupport.getOfficalUser(newAudit.getProjectId());
		if (user == null || user.getId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"【" + ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, newAudit.getState() + "")
							+ "】节点没有配置审核人");
		}
		newAudit.setAuditor(user.getChineseName());
		newAudit.setAuditorId(user.getId());
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增部门主管审核
	 *
	 * @param oldAudit
	 */
	public int creatDeptManageAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_80);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = null;
		if (oldAudit.getPoType() == BaseConsts.FIVE && newAudit.getProjectId() == null
				&& newAudit.getBusinessUnitId() != null) {
			user = ServiceSupport.getDepartManageUserByBusi(newAudit.getBusinessUnitId());
		} else {
			if (newAudit.getPoType().equals(BaseConsts.INT_23)) {// 业务目标值
				user = ServiceSupport.getProfitBusDeptManageUser(newAudit.getPoId());
			} else if (newAudit.getPoType().equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterDepartUser(newAudit.getPoId());
			} else {
				user = ServiceSupport.getDeptManageDepartUser(newAudit.getProjectId());
			}
		}
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增总经理审核
	 *
	 * @param oldAudit
	 */
	public int creatBossAudit(Audit oldAudit) {
		Audit newAudit = getNewAuditByOld(oldAudit);
		newAudit.setState(BaseConsts.INT_90);
		newAudit.setAuditState(BaseConsts.ZERO);
		BaseUser user = ServiceSupport.getBossUser(newAudit.getProjectId());
		setAuditor(oldAudit, newAudit, user);
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 设置审核人
	 * 
	 * @param oldAudit
	 * @param newAudit
	 * @param user
	 */
	public void setAuditor(Audit oldAudit, Audit newAudit, BaseUser user) {
		if (oldAudit.getPoType().equals(BaseConsts.SIX)) { // 6-凭证
			Voucher voucher = voucherService.queryEntityById(oldAudit.getPoId());
			AccountBook accountBook = cacheService.getAccountBookById(voucher.getAccountBookId());
			newAudit.setAuditor(accountBook.getAuditor()); // 凭证审核人
			newAudit.setAuditorId(accountBook.getAuditorId());
		} else {
			if (user == null || user.getId() == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"【" + ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, newAudit.getState() + "")
								+ "】节点没有配置审核人");
			}
			newAudit.setAuditor(user.getChineseName());
			newAudit.setAuditorId(user.getId());
		}
	}

	/**
	 * 创建起始审核节点
	 * 
	 * @param auditNode
	 * @param audit
	 * @return
	 */
	public Integer createStartNode2Audit(AuditNode auditNode, Audit audit) {
		return createNextNode2Audit(auditNode, audit);
	}

	/**
	 * 重要 TODO 按审核单据类型创建下一个审核节点，每新增一种类型审核节点，需在这个方法内配置
	 * 
	 * @param nextAuditNode
	 * @param audit
	 * @return
	 */
	public Integer createNextNode2Audit(AuditNode auditNode, Audit audit) {
		Integer auditId = null;
		if (auditNode != null) {
			if (auditNode.getAuditNodeState().equals(BaseConsts.TEN)) {
				auditId = createBizAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_11)) {
				auditId = createLawAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_14)) {
				auditId = createCareerAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_15)) {
				auditId = createPurchaseAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_16)) {
				auditId = createSupplyChainGroupAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_17)) {
				auditId = createSupplyChainServiceAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_18)) {
				auditId = creatSupplyChainRiskAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_20)) {
				auditId = createBusiAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_25)) {
				auditId = createFinanceAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_30)) {
				auditId = createFinance2Audit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_35)) {
				auditId = createRiskSpecialAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_40)) {
				auditId = createRiskAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_80)) {
				auditId = creatDeptManageAudit(audit);
			}
			if (auditNode.getAuditNodeState().equals(BaseConsts.INT_90)) {
				auditId = creatBossAudit(audit);
			}
		}
		return auditId;
	}

	/**
	 * 当节点审核通过时，关闭未审核的加签审核
	 *
	 * @param auditId
	 */
	public void closeSighAudit(Integer auditId) {
		Audit audit = queryAuditByld(auditId);
		List<Audit> audits = auditDao
				.queryAuditSighs(audit.getPauditId() != null ? audit.getPauditId() : audit.getId());
		for (int i = 0; i < audits.size(); i++) {
			Audit vo = auditDao.queryAuditById(audits.get(i).getId());
			if (vo.getAuditState() == BaseConsts.ZERO) {
				vo.setSuggestion("已由节点主审核人" + ServiceSupport.getUser().getChineseName() + "处理!");
				vo.setAuditState(BaseConsts.ONE);
				auditDao.updateAuditById(vo);
			}
		}
	}

	/**
	 * 新增加签
	 *
	 * @param auditId
	 *            原审核的ID
	 * @param auditorId
	 *            加签人
	 */
	public int creatSighAudit(Integer auditId, Integer auditorId) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		if (newAudit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "审核状态错误");
		}
		newAudit.setId(null);
		newAudit.setSuggestion(null);
		newAudit.setAuditorPass(null);
		newAudit.setAuditorPassId(null);
		newAudit.setAuditorPassAt(null);
		newAudit.setCreator(ServiceSupport.getUser().getChineseName());
		newAudit.setCreatorId(ServiceSupport.getUser().getId());
		newAudit.setCreateAt(new Date());
		newAudit.setAuditType(BaseConsts.THREE);
		if (newAudit.getPauditId() == null) {
			newAudit.setPauditId(auditId);
		}
		newAudit.setPauditorId(ServiceSupport.getUser().getId());
		newAudit.setPauditor(ServiceSupport.getUser().getChineseName());
		newAudit.setIsDelete(BaseConsts.ZERO);
		BaseUser auditor = cacheService.getUserByid(auditorId);
		newAudit.setAuditorId(auditor.getId());
		newAudit.setAuditor(auditor.getChineseName());
		auditDao.insert(newAudit);
		return newAudit.getId();
	}

	/**
	 * 新增转交
	 *
	 * @param auditId
	 *            原审核的ID
	 * @param auditorId
	 *            转交人
	 */
	public int creatDeliverAudit(Integer auditId, Integer auditorId) {

		Audit newAudit = auditDao.queryAuditById(auditId);
		if (newAudit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "审核状态错误");
		}
		BaseUser auditor = cacheService.getUserByid(auditorId);
		newAudit.setAuditState(BaseConsts.THREE);
		newAudit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		newAudit.setAuditorPassId(ServiceSupport.getUser().getId());
		newAudit.setAuditorPassAt(new Date());
		newAudit.setSuggestion("转交给" + auditor.getChineseName() + "处理审核!");
		updateAudit(newAudit);// 更新审核状态,终止流程

		newAudit.setId(null);
		newAudit.setAuditState(BaseConsts.ZERO);
		newAudit.setSuggestion(null);
		newAudit.setAuditorPass(null);
		newAudit.setAuditorPassId(null);
		newAudit.setAuditorPassAt(null);
		newAudit.setCreator(ServiceSupport.getUser().getChineseName());
		newAudit.setCreatorId(ServiceSupport.getUser().getId());
		newAudit.setCreateAt(new Date());
		newAudit.setAuditType(BaseConsts.TWO);
		if (newAudit.getPauditId() == null) {
			newAudit.setPauditId(auditId);
		}
		newAudit.setPauditorId(ServiceSupport.getUser().getId());
		newAudit.setPauditor(ServiceSupport.getUser().getChineseName());
		newAudit.setIsDelete(BaseConsts.ZERO);
		newAudit.setAuditorId(auditor.getId());
		newAudit.setAuditor(auditor.getChineseName());
		auditDao.insert(newAudit);
		return newAudit.getId();

	}

	/**
	 * 更新审核状态
	 */
	public void updateAudit(Audit audit) {
		// 更新业务审核记录，新增财务审核记录
		auditDao.updateAuditById(audit);
	}

	/**
	 * 获取数据
	 *
	 * @param id
	 * @return
	 */
	public Audit queryAuditByld(int id) {
		return auditDao.queryAuditById(id);
	}

	private Audit getNewAuditByOld(Audit audit) {
		Audit oldAudit = auditDao.queryAuditById(audit.getId());
		if (oldAudit != null) {
			oldAudit.setId(null);
			oldAudit.setAuditType(BaseConsts.ONE);
			oldAudit.setSuggestion(null);
			oldAudit.setAuditorPass(null);
			oldAudit.setAuditorPassId(null);
			oldAudit.setAuditorPassAt(null);
			oldAudit.setPauditId(null);
			oldAudit.setPauditor(null);
			oldAudit.setPauditorId(null);
			oldAudit.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
			oldAudit.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
			oldAudit.setCreateAt(new Date());
			oldAudit.setIsDelete(BaseConsts.ZERO);
			return oldAudit;
		} else {// 首次 提交审核，查询不到,返回业务传入的audit
			audit.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
			audit.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
			audit.setCreateAt(new Date());
			audit.setIsDelete(BaseConsts.ZERO);
			return audit;
		}
	}

	protected PageResult<AuditFlowsResDto> queryAuditFlowsByCon(Integer poId, Integer poType,
			List<Integer> auditflows) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();

		AuditReqDto auditReqDto = new AuditReqDto();
		auditReqDto.setPoId(poId);
		auditReqDto.setPoType(poType);

		List<Audit> audits = auditDao.queryAuditFlows(auditReqDto);

		List<AuditFlowsResDto> list1 = new ArrayList<AuditFlowsResDto>();

		Map<Integer, List<AuditFlowsResDto>> map2 = new HashMap<Integer, List<AuditFlowsResDto>>();
		Map<Integer, List<AuditFlowsResDto>> map3 = new HashMap<Integer, List<AuditFlowsResDto>>();

		for (int i = 0; i < audits.size(); i++) {
			Audit audit = audits.get(i);
			BaseUser audituser = cacheService.getUserByid(audit.getAuditorId());
			AuditFlowsResDto vo2 = new AuditFlowsResDto();
			vo2.setId(audit.getId());
			vo2.setCreateTime(audit.getCreateAt());
			vo2.setState(audit.getState());
			vo2.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, audit.getState() + ""));
			vo2.setSuggestion(audit.getSuggestion());
			vo2.setAuditType(audit.getAuditType());
			vo2.setPauditId(audit.getPauditId());
			vo2.setProjectId(audit.getProjectId());
			vo2.setPoType(audit.getPoType());
			vo2.setDealTime(audit.getAuditorPassAt());
			vo2.setAuditState(audit.getAuditState());
			vo2.setAuditStateName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_AUDITSTATE, audit.getAuditState() + ""));
			vo2.setBusiUnit(audit.getBusinessUnitId());

			if (audit.getAuditType() == BaseConsts.ONE) {
				vo2.setDealName(audituser.getChineseName());
				if (StringUtils.isBlank(vo2.getDealName())) {
					vo2.setDealName(audit.getAuditor());
				}
				list1.add(vo2);
			} else if (audit.getAuditType() == BaseConsts.TWO) {
				if (audit.getPauditId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,无原始转交人!");
				}
				BaseUser paudituser = cacheService.getUserByid(audit.getPauditorId());
				vo2.setDealName(audituser.getChineseName() + "【转交审】    转交人" + paudituser.getChineseName());
				if (map2.get(audit.getPauditId()) != null) {
					List<AuditFlowsResDto> ls = map2.get(audit.getPauditId());
					ls.add(vo2);
					map2.put(audit.getPauditId(), ls);
				} else {
					List<AuditFlowsResDto> ls = new ArrayList<AuditFlowsResDto>();
					ls.add(vo2);
					map2.put(audit.getPauditId(), ls);
				}
			} else if (audit.getAuditType() == BaseConsts.THREE) {
				if (audit.getPauditId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,无原始加签人!");
				}
				BaseUser paudituser = cacheService.getUserByid(audit.getPauditorId());
				vo2.setDealName(audituser.getChineseName() + "【加签审】    加签人" + paudituser.getChineseName());

				if (map3.get(audit.getPauditId()) != null) {
					List<AuditFlowsResDto> ls = map3.get(audit.getPauditId());
					ls.add(vo2);
					map3.put(audit.getPauditId(), ls);
				} else {
					List<AuditFlowsResDto> ls = new ArrayList<AuditFlowsResDto>();
					ls.add(vo2);
					map3.put(audit.getPauditId(), ls);
				}
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,审核类型错误!");
			}

		}

		List<AuditFlowsResDto> list_all = new ArrayList<AuditFlowsResDto>();

		for (int i = 0; i < list1.size(); i++) {
			AuditFlowsResDto dto = list1.get(i);

			List<AuditFlowsResDto> list_tmp = new ArrayList<AuditFlowsResDto>();
			list_tmp.add(dto);

			if (map2.get(dto.getId()) != null) {
				list_tmp.addAll(map2.get(dto.getId()));
			}
			if (map3.get(dto.getId()) != null) {
				list_tmp.addAll(map3.get(dto.getId()));
			}

			List<AuditFlowsResDto> list_tmp2 = new ArrayList<AuditFlowsResDto>();
			if (i == list1.size() - 1) {
				for (int j = 0; j < list_tmp.size(); j++) {
					AuditFlowsResDto vo = list_tmp.get(j);
					if (vo.getAuditState() == BaseConsts.ZERO) {
						vo.setFontcolor(BaseConsts.TWO + "");
					}
					list_tmp2.add(vo);
				}
			} else {
				list_tmp2.addAll(list_tmp);
			}

			list_all.addAll(list_tmp2);

			if (i == list1.size() - 1) {
				int k = auditflows.indexOf(dto.getState());
				if (k > 0 || (!poType.equals(BaseConsts.FIVE) && !poType.equals(BaseConsts.INT_14))) {
					for (int j = k + 1; j < auditflows.size(); j++) {
						Integer status = auditflows.get(j);
						AuditFlowsResDto vo = new AuditFlowsResDto();
						BaseUser user = getDealUser(status, dto.getProjectId(), dto.getBusiUnit(), poId, poType); // 获取处理人
						vo.setAuditType(dto.getAuditType());
						vo.setState(status);
						vo.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, status + ""));
						vo.setProjectId(dto.getProjectId());
						vo.setAuditState(BaseConsts.ZERO);
						vo.setAuditStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_AUDITSTATE,
								vo.getAuditState() + ""));
						vo.setDealName(user.getChineseName());
						list_all.add(vo);
					}
				}
			}
		}

		for (int i = 0; i < list_all.size(); i++) {
			AuditFlowsResDto dto = list_all.get(i);
			if (dto.getBackcolor() == null) {
				if (dto.getAuditState() == BaseConsts.ONE || dto.getAuditState() == BaseConsts.THREE) {
					dto.setBackcolor(BaseConsts.ONE + "");
				}
			}
			if (dto.getFontcolor() == null) {
				if (dto.getAuditState() == BaseConsts.TWO) {
					dto.setFontcolor(BaseConsts.ONE + "");
				}
			}
		}

		result.setItems(list_all);
		return result;
	}

	public BaseUser getDealUser(Integer status, Integer projectId, Integer busiUnit, Integer poId, Integer poType) {
		BaseUser user = new BaseUser();
		if (status == BaseConsts.TEN) {
			if (poType.equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterOfficalUser(poId);
			} else {
				user = ServiceSupport.getOfficalUser(projectId);
			}
		}
		if (status == BaseConsts.INT_11) {
			if (poType.equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterJusticeUser(poId);
			} else {
				user = ServiceSupport.getLawDepartUser(projectId);
			}
		}
		if (status == BaseConsts.INT_14) {// 铺货商品待事业部审核
			DistributionGoods distribution = cacheService.getDistributionGoodsById(poId);
			user = cacheService.getUserByid(distribution.getCareerId());
		}
		if (status == BaseConsts.INT_15) {// 铺货商品待采购审核
			DistributionGoods distribution = cacheService.getDistributionGoodsById(poId);
			user = cacheService.getUserByid(distribution.getPurchaseId());
		}
		if (status == BaseConsts.INT_16) {// 铺货商品待供应链小组审核
			DistributionGoods distribution = cacheService.getDistributionGoodsById(poId);
			user = cacheService.getUserByid(distribution.getSupplyChainGroupId());
		}
		if (status == BaseConsts.INT_17) {// 铺货商品待供应链服务部审核
			DistributionGoods distribution = cacheService.getDistributionGoodsById(poId);
			user = cacheService.getUserByid(distribution.getSupplyChainServiceId());
		}
		if (status == BaseConsts.INT_18) {// 铺货商品风控审核
			DistributionGoods distribution = cacheService.getDistributionGoodsById(poId);
			user = cacheService.getUserByid(distribution.getRiskId());
		}
		if (status == BaseConsts.INT_20) {
			if (poType.equals(BaseConsts.INT_23)) {// 业务目标值
				user = ServiceSupport.getProfitBusDepartUser(poId);
			} else if (poType.equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterBusiUser(poId);
			} else {
				user = ServiceSupport.getBusDepartUser(projectId);
			}
		}
		if (status == BaseConsts.INT_25) {
			user = ServiceSupport.getFinanceSpecialDepartUser(projectId);
		}
		if (status == BaseConsts.INT_30) {
			if (poType.equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterFinanceUser(poId);
			} else if (poType.equals(BaseConsts.FIVE) && projectId == null && busiUnit != null) {
				user = ServiceSupport.getFinanceUserByBusi(busiUnit);
			} else {
				user = ServiceSupport.getFinanceDepartUser(projectId);
			}
		}
		if (status == BaseConsts.INT_35) {
			user = ServiceSupport.getRiskSpecialDepartUser(projectId);
		}
		if (status == BaseConsts.INT_40) {
			if (poType.equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterRiskUser(poId);
			} else {
				user = ServiceSupport.getRiskDepartUser(projectId);
			}
		}
		if (status == BaseConsts.INT_80) {
			if (poType.equals(BaseConsts.INT_23)) {// 业务目标值
				user = ServiceSupport.getProfitBusDeptManageUser(poId);
			} else if (poType.equals(BaseConsts.INT_27)) {// 事项管理
				user = ServiceSupport.getMatterDepartUser(poId);
			} else if (poType.equals(BaseConsts.FIVE) && projectId == null && busiUnit != null) {
				user = ServiceSupport.getDepartManageUserByBusi(busiUnit);
			} else {
				user = ServiceSupport.getDeptManageDepartUser(projectId);
			}
		}
		if (status == BaseConsts.INT_90) {
			user = ServiceSupport.getBossUser(projectId);
		}
		return user;
	}

	/**
	 * 发送邮件消息提醒
	 *
	 * @param userId
	 * @param title
	 * @param content
	 */
	public void sendWarnMail(Integer userId, String title, String content) {
		msgContentService.addMsgContentByUserId(userId, title, content, BaseConsts.TWO);
	}

	/**
	 * 发送RTX消息提醒
	 *
	 * @param userId
	 * @param title
	 * @param content
	 */
	public void sendWarnRtx(Integer userId, String title, String content) {
		msgContentService.addMsgContentByUserId(userId, title, content, BaseConsts.ONE);
	}

	/**
	 *
	 * 根据审核ID发送微信消息
	 * 
	 * @param auditId
	 *            对应tb_audit表中id,对应审核节点的ID
	 * @param title
	 *            标题
	 */
	public void sendWechatMsg(int auditId, String title) {
		Audit audit = auditDao.queryAuditById(auditId);
		List<WechatUser> wechatUsers = userWechatService.queryBindWechatsByUserId(audit.getAuditorId());
		if (CollectionUtils.isNotEmpty(wechatUsers)) {
			StringBuilder acc = new StringBuilder();
			for (WechatUser wechatUser : wechatUsers) {
				acc.append(wechatUser.getOpenid()).append(",");
			}
			Map<String, String> map = Maps.newHashMap();
			map.put("template_id", "HxFcIi7MutssPcL5k5ZfK5JrF9YslcfUQf16q12Q3Vo");
			map.put("first", title);
			String url = domain + "wechat/html/audit/audit_" + audit.getPoType() + "_" + audit.getState()
					+ "_new.html?id=" + audit.getId() + "&poId=" + audit.getPoId();
			map.put("url", url);
			if (audit.getAmount() == null) {
				audit.setAmount(BigDecimal.ZERO);
			}
			map.put("keyword1", DecimalUtil.formatScale2(audit.getAmount()).toString());
			map.put("keyword2", DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, audit.getCreateAt()));
			String projectName = cacheService.getProjectNameById(audit.getProjectId());
			String customerName = "";
			if (null != audit.getCustomerId()) {
				BaseSubject baseSubject = cacheService.getCustomerById(audit.getCustomerId());
				if (null != baseSubject) {
					customerName = baseSubject.getNoName();
				}
			}
			if (StringUtils.isBlank(customerName)) {
				if (null != audit.getSupplierId()) {
					BaseSubject baseSubject = cacheService.getCustomerById(audit.getSupplierId());
					if (null != baseSubject) {
						customerName = baseSubject.getNoName();
					}
				}
			}

			String stateName = ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, audit.getState() + "");
			map.put("remark", "项目名称：" + projectName + "\n客户名称："
					+ (StringUtils.isBlank(customerName) ? "-" : customerName) + "\n状态：" + stateName + "\n点击进入查看详情");
			String content = JSONObject.toJSONString(map);
			msgContentService.addMsgContent(acc.toString(), null, content, BaseConsts.FOUR);
		}
	}

	/**
	 * 根据项目id发送该项目下供应商的用户下的微信用户进行推送
	 * 
	 * @param projectId
	 */
	public void sendWechatMsgByProject(PayOrder payOrder) {
		ProjectSubjectSearchReqDto projectReqDto = new ProjectSubjectSearchReqDto();
		projectReqDto.setProjectId(payOrder.getProjectId());
		projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_SUPPLIER);
		List<ProjectSubject> projectSubject = projectSubjectDao.queryResultsByCon(projectReqDto);
		if (!CollectionUtils.isEmpty(projectSubject)) {
			for (ProjectSubject subject : projectSubject) {
				BaseUserSubjectReqDto baseUserSubject = new BaseUserSubjectReqDto();
				baseUserSubject.setSubjectId(subject.getSubjectId());
				List<BaseUserSubject> baseUserSubjects = baseUserSubjectDao.queryUserSubjectByCon(baseUserSubject);
				if (!CollectionUtils.isEmpty(baseUserSubjects)) {
					for (BaseUserSubject baseUserSubject2 : baseUserSubjects) {
						this.sendWechatMsgByUserId(baseUserSubject2.getUserId(), "您有新的请款单,请确认请款", payOrder);
					}
				}
			}
		}
	}

	/**
	 * 发送模版
	 * 
	 * @param userId
	 */
	public void sendWechatMsgByUserId(Integer userId, String title, PayOrder payOrder) {
		List<WechatUser> wechatUsers = userWechatService.queryBindWechatsByUserId(userId);
		if (CollectionUtils.isNotEmpty(wechatUsers)) {
			StringBuilder builder = new StringBuilder();
			for (WechatUser wechatUser : wechatUsers) {
				builder.append(wechatUser.getOpenid()).append(",");
			}
			Map<String, String> map = Maps.newHashMap();
			map.put("template_id", "YqSMmIJOEA7pcF9EHQ7GLnL_0Ks5U5qDhisBh3m5gQ8");
			map.put("first", title);
			String url = domain + "wechat/html/audit/audit_requestFunds.html";
			map.put("url", url);
			map.put("keyword1", cacheService.getProjectNameById(payOrder.getProjectId()));
			map.put("keyword2", payOrder.getPayNo());
			map.put("keyword3",
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, payOrder.getRequestPayTime()));
			map.put("keyword4", ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.FOUR) + ""));
			map.put("keyword5", payOrder.getPayAmount().toString());
			map.put("remark", "点击查看请款详情");
			String content = JSONObject.toJSONString(map);
			msgContentService.addMsgContent(builder.toString(), null, content, BaseConsts.FOUR);
		}
	}

	/**
	 * 根据项目id发送该项目下供应商的用户下的微信用户进行推送
	 * 
	 * @param projectId
	 */
	public void sendWechatMsgByProjectMerge(MergePayOrder mergePayOrder) {
		ProjectSubjectSearchReqDto projectReqDto = new ProjectSubjectSearchReqDto();
		projectReqDto.setProjectId(mergePayOrder.getProjectId());
		projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_SUPPLIER);
		List<ProjectSubject> projectSubject = projectSubjectDao.queryResultsByCon(projectReqDto);
		if (!CollectionUtils.isEmpty(projectSubject)) {
			for (ProjectSubject subject : projectSubject) {
				BaseUserSubjectReqDto baseUserSubject = new BaseUserSubjectReqDto();
				baseUserSubject.setSubjectId(subject.getSubjectId());
				List<BaseUserSubject> baseUserSubjects = baseUserSubjectDao.queryUserSubjectByCon(baseUserSubject);
				if (!CollectionUtils.isEmpty(baseUserSubjects)) {
					for (BaseUserSubject baseUserSubject2 : baseUserSubjects) {
						this.sendWechatMsgByUserIdMerge(baseUserSubject2.getUserId(), "您有新的请款单,请确认请款", mergePayOrder);
					}
				}
			}
		}
	}

	/**
	 * 发送模版
	 * 
	 * @param userId
	 */
	public void sendWechatMsgByUserIdMerge(Integer userId, String title, MergePayOrder mergePayOrder) {
		List<WechatUser> wechatUsers = userWechatService.queryBindWechatsByUserId(userId);
		if (CollectionUtils.isNotEmpty(wechatUsers)) {
			StringBuilder builder = new StringBuilder();
			for (WechatUser wechatUser : wechatUsers) {
				builder.append(wechatUser.getOpenid()).append(",");
			}
			Map<String, String> map = Maps.newHashMap();
			map.put("template_id", "YqSMmIJOEA7pcF9EHQ7GLnL_0Ks5U5qDhisBh3m5gQ8");
			map.put("first", title);
			String url = domain + "wechat/html/audit/audit_requestFunds.html";
			map.put("url", url);
			map.put("keyword1", cacheService.getProjectNameById(mergePayOrder.getProjectId()));
			map.put("keyword2", mergePayOrder.getMergePayNo());
			map.put("keyword3",
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, mergePayOrder.getRequestPayTime()));
			map.put("keyword4", ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.FOUR) + ""));
			map.put("keyword5", mergePayOrder.getPayAmount().toString());
			map.put("remark", "点击查看请款详情");
			String content = JSONObject.toJSONString(map);
			msgContentService.addMsgContent(builder.toString(), null, content, BaseConsts.FOUR);
		}
	}
}
