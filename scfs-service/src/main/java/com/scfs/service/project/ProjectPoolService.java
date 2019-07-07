package com.scfs.service.project;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.pay.PayFeeRelationDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.project.ProjectPoolAssetDao;
import com.scfs.dao.project.ProjectPoolDao;
import com.scfs.dao.project.ProjectPoolFundDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectPoolSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectPoolResDto;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectPool;
import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.domain.project.entity.ProjectPoolFund;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.pay.PayService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class ProjectPoolService {

	@Autowired
	private ProjectPoolDao projectPoolDao;
	@Autowired
	private ProjectPoolFundDao projectPoolFundDao;
	@Autowired
	private ProjectPoolAssetDao projectPoolAssetDao;
	@Autowired
	private BaseExchangeRateService rateService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private RecReceiptRelDao recReceiptRelDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private RecLineDao recLineDao;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private PayFeeRelationDao payFeeRelationDao;
	@Autowired
	private PayService payService;

	/**
	 * 翻页查询融资池基本信息
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 * @throws Exception
	 */
	public PageResult<ProjectPoolResDto> queryProjectPoolResultsByCon(ProjectPoolSearchReqDto queryProjectPoolReqDto)
			throws Exception {
		PageResult<ProjectPoolResDto> pageResult = new PageResult<ProjectPoolResDto>();
		int offSet = PageUtil.getOffSet(queryProjectPoolReqDto.getPage(), queryProjectPoolReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryProjectPoolReqDto.getPer_page());
		queryProjectPoolReqDto.setUserId(ServiceSupport.getUser().getId());
		List<ProjectPool> result = projectPoolDao.queryProjectPoolResultsByCon(queryProjectPoolReqDto, rowBounds);
		List<ProjectPoolResDto> vos = new ArrayList<ProjectPoolResDto>();
		if (CollectionUtils.isNotEmpty(result)) {
			for (ProjectPool rs : result) {
				ProjectPoolResDto recQueryResDto = convertToResDto(rs);
				vos.add(recQueryResDto);
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryProjectPoolReqDto.getPer_page());
		pageResult.setItems(vos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryProjectPoolReqDto.getPage());
		pageResult.setPer_page(queryProjectPoolReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 查询资金明细
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 * @throws Exception
	 */
	public PageResult<ProjectPoolFund> queryProjectPoolFundResultsByCon(
			ProjectPoolDtlSearchReqDto queryProjectPoolReqDto) {
		PageResult<ProjectPoolFund> pageResult = new PageResult<ProjectPoolFund>();
		int offSet = PageUtil.getOffSet(queryProjectPoolReqDto.getPage(), queryProjectPoolReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryProjectPoolReqDto.getPer_page());
		ProjectPool vo = projectPoolDao.selectById(queryProjectPoolReqDto.getId());
		queryProjectPoolReqDto.setProjectId(vo.getProjectId());
		queryProjectPoolReqDto.setId(null);
		List<ProjectPoolFund> Result = projectPoolFundDao.selectByCon(queryProjectPoolReqDto, rowBounds);
		for (int i = 0; i < Result.size(); i++) {
			ProjectPoolFund pro = Result.get(i);
			pro.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FUND_TYPE, pro.getType() + "")); // 入、出
			// pro.setBillSourceName(ServiceSupport.getValueByBizCode(BizCodeConsts.FUNDS_TYPE,
			// pro.getBillSource() + ""));
			pro.setProjectName(cacheService.getProjectNameById(pro.getProjectId()));
			pro.setCustomerName(cacheService.getSubjectNoNameById(pro.getCustomerId()));
			pro.setSupplierName(cacheService.getSubjectNoNameById(pro.getSupplierId()));
			pro.setBillCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					pro.getBillCurrencyType() + ""));

			pro.setBillSourceName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_SOURCE, pro.getBillSource() + ""));
			pro.setBillAmountValue(DecimalUtil.toAmountString(pro.getBillAmount()));
			pro.setProjectAmountValue(DecimalUtil.toAmountString(pro.getProjectAmount()));
			pro.setCnyAmountValue(DecimalUtil.toAmountString(pro.getCnyAmount()));
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryProjectPoolReqDto.getPer_page());
		pageResult.setItems(Result);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryProjectPoolReqDto.getPage());
		pageResult.setPer_page(queryProjectPoolReqDto.getPer_page());
		return pageResult;
	}

	public List<ProjectPoolFund> queryFundResultsByCon(ProjectPoolDtlSearchReqDto queryProjectPoolReqDto) {
		ProjectPool vo = projectPoolDao.selectById(queryProjectPoolReqDto.getId());
		queryProjectPoolReqDto.setProjectId(vo.getProjectId());
		queryProjectPoolReqDto.setId(null);
		List<ProjectPoolFund> Result = projectPoolFundDao.selectByCon(queryProjectPoolReqDto);
		for (int i = 0; i < Result.size(); i++) {
			ProjectPoolFund pro = Result.get(i);
			pro.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FUND_TYPE, pro.getType() + ""));
			pro.setProjectName(cacheService.getProjectNameById(pro.getProjectId()));
			pro.setCustomerName(cacheService.getSubjectNoNameById(pro.getCustomerId()));
			pro.setSupplierName(cacheService.getSubjectNoNameById(pro.getSupplierId()));
			pro.setBillCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					pro.getBillCurrencyType() + ""));

			pro.setBillSourceName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_SOURCE, pro.getBillSource() + ""));
			pro.setBillAmountValue(DecimalUtil.toAmountString(pro.getBillAmount()));
			pro.setProjectAmountValue(DecimalUtil.toAmountString(pro.getProjectAmount()));
			pro.setCnyAmountValue(DecimalUtil.toAmountString(pro.getCnyAmount()));
		}
		return Result;
	}

	/**
	 * 查询资产明细
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 * @throws Exception
	 */
	public PageResult<ProjectPoolAsset> queryProjectPoolAssertResultsByCon(
			ProjectPoolDtlSearchReqDto queryProjectPoolReqDto) {
		PageResult<ProjectPoolAsset> pageResult = new PageResult<ProjectPoolAsset>();
		int offSet = PageUtil.getOffSet(queryProjectPoolReqDto.getPage(), queryProjectPoolReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryProjectPoolReqDto.getPer_page());
		ProjectPool vo = projectPoolDao.selectById(queryProjectPoolReqDto.getId());
		queryProjectPoolReqDto.setProjectId(vo.getProjectId());
		queryProjectPoolReqDto.setId(null);
		List<ProjectPoolAsset> result = projectPoolAssetDao.selectByCon(queryProjectPoolReqDto, rowBounds);
		for (int i = 0; i < result.size(); i++) {
			ProjectPoolAsset pro = result.get(i);
			pro.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FUND_TYPE, pro.getType() + "")); // 入、出
			pro.setBillSourceName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.ASSERT_TYPE, pro.getBillSource() + ""));
			pro.setProjectName(cacheService.getProjectNameById(pro.getProjectId()));
			pro.setCustomerName(cacheService.getSubjectNoNameById(pro.getCustomerId()));
			pro.setSupplierName(cacheService.getSubjectNoNameById(pro.getSupplierId()));
			pro.setBillCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					pro.getBillCurrencyType() + ""));
			pro.setBillAmountValue(DecimalUtil.toAmountString(pro.getBillAmount()));
			pro.setProjectAmountValue(DecimalUtil.toAmountString(pro.getProjectAmount()));
			pro.setCnyAmountValue(DecimalUtil.toAmountString(pro.getCnyAmount()));
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryProjectPoolReqDto.getPer_page());
		pageResult.setItems(result);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryProjectPoolReqDto.getPage());
		pageResult.setPer_page(queryProjectPoolReqDto.getPer_page());
		return pageResult;
	}

	public List<ProjectPoolAsset> queryAssertResultsByCon(ProjectPoolDtlSearchReqDto queryProjectPoolReqDto) {
		ProjectPool vo = projectPoolDao.selectById(queryProjectPoolReqDto.getId());
		queryProjectPoolReqDto.setProjectId(vo.getProjectId());
		queryProjectPoolReqDto.setId(null);
		List<ProjectPoolAsset> result = projectPoolAssetDao.selectByCon(queryProjectPoolReqDto);
		for (int i = 0; i < result.size(); i++) {
			ProjectPoolAsset pro = result.get(i);
			pro.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FUND_TYPE, pro.getType() + "")); // 入、出
			pro.setBillSourceName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.ASSERT_TYPE, pro.getBillSource() + ""));
			pro.setProjectName(cacheService.getProjectNameById(pro.getProjectId()));
			pro.setCustomerName(cacheService.getSubjectNoNameById(pro.getCustomerId()));
			pro.setSupplierName(cacheService.getSubjectNoNameById(pro.getSupplierId()));
			pro.setBillCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					pro.getBillCurrencyType() + ""));
			pro.setBillAmountValue(DecimalUtil.toAmountString(pro.getBillAmount()));
			pro.setProjectAmountValue(DecimalUtil.toAmountString(pro.getProjectAmount()));
			pro.setCnyAmountValue(DecimalUtil.toAmountString(pro.getCnyAmount()));
		}
		return result;
	}

	/**
	 * 根据Id查询单条融资池信息
	 * 
	 * @param projectPool
	 * @return
	 */
	public ProjectPoolResDto detailProjectItemById(Integer id) throws Exception {
		ProjectPool vo = projectPoolDao.selectById(id);
		return convertToResDto(vo);
	}

	/**
	 * 根据Id查询单条融资池信息
	 * 
	 * @param projectPool
	 * @return
	 */
	public ProjectPoolResDto detailProjectItemByProjectId(Integer projectId) throws Exception {
		ProjectPool vo = projectPoolDao.queryProjectPoolByProjectId(projectId);
		return convertToResDto(vo);
	}

	/**
	 * 新增资产明细,
	 * 添加前,需要赋值type,bill_no,bill_source,project_id,customer_id,supplier_id,
	 * business_date,bill_amount,bill_currency_type,remark
	 * 
	 * @param projectPoolAssertList
	 * @param poolType
	 *            1-入库单 2-出库单 3-顺友(特殊)
	 */
	public void addProjectPoolAsset(ProjectPoolAsset vo, Integer poolType) {
		convertPoolAssetAmount(vo, poolType);
		vo.setCreateAt(new Date());
		vo.setCreatorId(null == ServiceSupport.getUser() ? null : ServiceSupport.getUser().getId());
		vo.setCreator(null == ServiceSupport.getUser() ? null : ServiceSupport.getUser().getChineseName());
		if (DecimalUtil.ne(DecimalUtil.ZERO, vo.getBillAmount())) {
			projectPoolAssetDao.insert(vo);
		}
	}

	/**
	 * 更新资产明细
	 * 
	 * @param vo
	 *            需type,bill_no,bill_source,project_id,customer_id,supplier_id,
	 *            business_date,bill_amount,bill_currency_type,remark
	 */
	public void updateProjectPoolAssert(ProjectPoolAsset vo, Integer poolType) {
		convertPoolAssetAmount(vo, poolType);
		projectPoolAssetDao.updateById(vo);
	}

	public ProjectPool queryProjectPoolByProjectId(Integer projectId) {
		ProjectPool pp = projectPoolDao.queryProjectPoolByProjectId(projectId);
		return pp;
	}

	/**
	 * 按币种和汇率转换金额
	 * 
	 * @param vo
	 */
	public void convertPoolAssetAmount(ProjectPoolAsset vo, Integer poolType) {
		vo.setBillAmount(vo.getBillAmount() == null ? BigDecimal.ZERO : vo.getBillAmount());
		ProjectPool pp = projectPoolDao.queryProjectPoolByProjectId(vo.getProjectId());
		ProjectItem item = projectItemService.getProjectItem(vo.getProjectId());

		if (item.getAccountRateType() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,条款:" + item.getItemNo() + "结算汇率类型不能为空");
		}

		BigDecimal project_exrate = null;
		BigDecimal cny_exrate = null;

		if (item.getAccountRateType() == 1) {
			Integer accountMethod = null;
			if (item.getAccountMethod() != null) {
				accountMethod = item.getAccountMethod();
			} else {
				accountMethod = BaseConsts.TWO;
			}
			if (poolType.equals(BaseConsts.ONE) || poolType.equals(BaseConsts.TWO)) { // 1-入库单
																						// 2-出库单
				BigDecimal projectAmount = BigDecimal.ZERO;
				if (poolType.equals(BaseConsts.ONE)) { // 1-入库单
					projectAmount = billInStoreDao.queryPayAmountByPayRate(vo.getBillNo());
				}
				if (poolType.equals(BaseConsts.TWO)) { // 2-出库单
					projectAmount = billOutStoreDao.queryPayAmountByPayRate(vo.getBillNo());
				}
				if (null != projectAmount && !DecimalUtil.eq(projectAmount, BigDecimal.ZERO)
						&& null != vo.getBillAmount() && !DecimalUtil.eq(vo.getBillAmount(), BigDecimal.ZERO)) {
					vo.setProjectAmount(DecimalUtil.formatScale2(projectAmount));
					vo.setBillProjectExchangeRate(DecimalUtil.divide(vo.getProjectAmount(), vo.getBillAmount())
							.setScale(8, BigDecimal.ROUND_HALF_UP));
				} else {
					vo.setProjectAmount(BigDecimal.ZERO);
					vo.setBillProjectExchangeRate(BigDecimal.ZERO);
				}
			} else {
				project_exrate = rateService.convertCurrency(item.getBankId() + "", vo.getBillCurrencyType() + "",
						pp.getCurrencyType() + "", vo.getBusinessDate(), accountMethod + "");
				vo.setProjectAmount(vo.getBillAmount().divide(project_exrate, 2, BigDecimal.ROUND_HALF_UP));
				vo.setBillProjectExchangeRate(project_exrate);
			}

			cny_exrate = rateService.convertCurrency(item.getBankId() + "", vo.getBillCurrencyType() + "",
					BaseConsts.ONE + "", vo.getBusinessDate(), accountMethod + "");

			vo.setBillCnyExchangeRate(cny_exrate);
			vo.setProjectCurrencyType(item.getAmountCurrency());
			vo.setCnyAmount(vo.getBillAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP));
		} else if (item.getAccountRateType() == 2) {
			if (!vo.getBillCurrencyType().equals(item.getAmountCurrency())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,单据币种与条款:" + item.getItemNo() + "币种不一致");
			}
			vo.setBillProjectExchangeRate(DecimalUtil.ONE);
			vo.setBillCnyExchangeRate(DecimalUtil.ONE);
			vo.setProjectCurrencyType(item.getAmountCurrency());
			vo.setProjectAmount(vo.getBillAmount());
			vo.setCnyAmount(vo.getBillAmount());
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,条款:" + item.getItemNo() + "结算汇率类型错误");
		}
	}

	/**
	 * 新增资金明细,
	 * 添加前,需要赋值type,bill_no,bill_source,project_id,customer_id,supplier_id,
	 * business_date,bill_amount,bill_currency_type,remark
	 * 
	 * @param projectPoolFundList
	 * @param poolType
	 *            1-付费用 2-付货款 3-正常水单 4-虚拟水单 5 预付款
	 */
	public void addProjectPoolFund(ProjectPoolFund vo, Integer poolType) {
		convertPoolFundAmount(vo, poolType);
		vo.setCreateAt(new Date());
		vo.setCreatorId(null == ServiceSupport.getUser() ? null : ServiceSupport.getUser().getId());
		vo.setCreator(null == ServiceSupport.getUser() ? null : ServiceSupport.getUser().getChineseName());
		if (DecimalUtil.ne(DecimalUtil.ZERO, vo.getBillAmount())) {
			projectPoolFundDao.insert(vo);
		}
	}

	/**
	 * 更新资金明细
	 * 
	 * @param vo
	 *            需type,bill_no,bill_source,project_id,customer_id,supplier_id,
	 *            business_date,bill_amount,bill_currency_type,remark
	 */
	public void updateProjectPoolFund(ProjectPoolFund vo, Integer poolType) {
		convertPoolFundAmount(vo, poolType);
		projectPoolFundDao.updateById(vo);
	}

	/**
	 * 按币种和汇率转换金额
	 * 
	 * @param vo
	 */
	public void convertPoolFundAmount(ProjectPoolFund vo, Integer poolType) {
		vo.setBillAmount(vo.getBillAmount() == null ? BigDecimal.ZERO : vo.getBillAmount());
		ProjectItem item = projectItemService.getProjectItem(vo.getProjectId());

		if (item.getAccountRateType() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,条款:" + item.getItemNo() + "结算汇率类型不能为空");
		}

		BigDecimal cny_exrate = BigDecimal.ZERO;
		if (item.getAccountRateType() == 1) {
			Integer accountMethod = null;
			if (item.getAccountMethod() != null) {
				accountMethod = item.getAccountMethod();
			} else {
				accountMethod = BaseConsts.TWO;
			}

			BigDecimal projectAmount = BigDecimal.ZERO; // 实际占用金额
			if (poolType.equals(BaseConsts.THREE) || poolType.equals(BaseConsts.FOUR)) { // 3-正常水单
																							// 4-虚拟水单
				if (poolType.equals(BaseConsts.THREE)) {
					BigDecimal fundUsed = recReceiptRelDao.queryFundUsedByReceiptNo(vo.getBillNo());
					projectAmount = fundUsed;
				}
				if (poolType.equals(BaseConsts.FOUR)) {
					// BigDecimal fundUsed =
					// recReceiptRelDao.queryFundUsedByReceiptNo(vo.getBillNo());
					BankReceipt bankReceipt = bankReceiptDao.queryEntityByBankReceiptNo(vo.getBillNo());
					RecLine recLine = recLineDao.queryRecLineByVirtualReceiptNo(vo.getBillNo());
					if (null != recLine && null != recLine.getFeeId()) {// 费用单
						BigDecimal fundUsed = BigDecimal.ZERO;
						Fee fee = feeDao.queryEntityById(recLine.getFeeId());
						if (null != fee && fee.getPayFeeType().equals(BaseConsts.ONE)) {
							PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
							payFeeRelationReqDto.setFeeId(fee.getId());
							payFeeRelationReqDto.setPayFeeType(BaseConsts.ONE);
							List<PayFeeRelationModel> list = payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto);
							for (PayFeeRelationModel payFeeRelationModel : list) {
								BigDecimal fundUsedAmount = payService
										.queryFundUsedByPayIdAndFee(payFeeRelationModel.getPayId(), fee);
								fundUsed = DecimalUtil.add(fundUsed, fundUsedAmount);
							}
							projectAmount = DecimalUtil.formatScale2(fundUsed);
						}
					}
					if (null != recLine && null != recLine.getOutStoreId()) {// 出库单
						BigDecimal fundUsed = recReceiptRelDao.queryFundUsedByReceiptNo(vo.getBillNo());
						BigDecimal payRate = payOrderDao.queryPayRateByQk(bankReceipt.getBankReceiptNo());
						projectAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(fundUsed, payRate));
					}
				}
				if (null != projectAmount && !DecimalUtil.eq(projectAmount, BigDecimal.ZERO)
						&& null != vo.getBillAmount() && !DecimalUtil.eq(vo.getBillAmount(), BigDecimal.ZERO)) {
					vo.setProjectAmount(DecimalUtil.formatScale2(projectAmount));
					vo.setBillProjectExchangeRate(DecimalUtil.divide(vo.getProjectAmount(), vo.getBillAmount())
							.setScale(8, BigDecimal.ROUND_HALF_UP));
				} else {
					vo.setProjectAmount(BigDecimal.ZERO);
					vo.setBillProjectExchangeRate(BigDecimal.ZERO);
				}
			} else if (poolType.equals(BaseConsts.ONE) || poolType.equals(BaseConsts.TWO)) { // 1-付费用
																								// 2-付货款
				projectAmount = DecimalUtil.multiply(vo.getBillAmount(), vo.getBillProjectExchangeRate());
				vo.setProjectAmount(projectAmount);
			} else if (poolType == BaseConsts.FIVE) {
				vo.setProjectAmount(vo.getBillAmount());
			}

			cny_exrate = rateService.convertCurrency(item.getBankId() + "", vo.getBillCurrencyType() + "",
					BaseConsts.ONE + "", vo.getBusinessDate(), accountMethod + "");
			vo.setBillCnyExchangeRate(cny_exrate);
			vo.setProjectCurrencyType(item.getAmountCurrency());
			vo.setCnyAmount(vo.getBillAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP));
		} else if (item.getAccountRateType() == 2) {
			if (!vo.getBillCurrencyType().equals(item.getAmountCurrency())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,单据币种与条款:" + item.getItemNo() + "币种不一致");
			}
			vo.setBillProjectExchangeRate(DecimalUtil.ONE);
			vo.setBillCnyExchangeRate(DecimalUtil.ONE);
			vo.setProjectCurrencyType(item.getAmountCurrency());
			vo.setProjectAmount(vo.getBillAmount());
			vo.setCnyAmount(vo.getBillAmount());
		} else if (item.getAccountRateType() == 0) {
			// 无汇率约定
			if (vo.getBillCurrencyType() != 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,无汇率约定的只接受人民币单据");
			}
			if (item.getAmountCurrency() != 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,无汇率约定的只接受人民币条款");
			}
			vo.setBillProjectExchangeRate(DecimalUtil.ONE);
			vo.setBillCnyExchangeRate(DecimalUtil.ONE);
			vo.setProjectCurrencyType(item.getAmountCurrency());
			vo.setProjectAmount(vo.getBillAmount());
			vo.setCnyAmount(vo.getBillAmount());
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,条款:" + item.getItemNo() + "结算汇率类型错误");
		}
	}

	public void updateProjectPool(ProjectPool projectPool) {
		projectPoolDao.updateById(projectPool);
	}

	/**
	 * 更新融资池信息
	 */
	public void updateProjectPoolInfo(Integer projectId) {
		updateProjectPoolInfo(projectId, false);
	}

	/**
	 * 更新融资池信息
	 * 
	 * @param projectId
	 *            项目ID
	 * @param isChangeProjectItem
	 *            是否变更条款
	 */
	public void updateProjectPoolInfo(Integer projectId, boolean isChangeProjectItem) {
		ProjectPool projectPool = projectPoolDao.queryProjectPoolByProjectId(projectId);
		if (null != projectPool) {
			projectPool = getProjectPool(projectId, projectPool, isChangeProjectItem);
			projectPoolDao.updateById(projectPool);
		} else {
			projectPool = getProjectPool(projectId, projectPool, isChangeProjectItem);
			projectPoolDao.insert(projectPool);
		}
	}

	public ProjectPool getProjectPool(Integer projectId, ProjectPool projectPool, boolean isChangeProjectItem) {
		/**
		 * TODO 金额需转换至指定币种金额
		 */
		if (null == projectPool) {
			ProjectItem pi = projectItemService.getProjectItem(projectId);
			projectPool = new ProjectPool();
			projectPool.setProjectId(pi.getProjectId());
			projectPool.setProjectAmount(null == pi.getTotalAmount() ? BigDecimal.ZERO : pi.getTotalAmount());
			projectPool.setCurrencyType(pi.getAmountCurrency());
		} else {
			if (isChangeProjectItem == true) {
				ProjectItem pi = projectItemService.getProjectItem(projectId);
				projectPool.setProjectId(pi.getProjectId());
				projectPool.setProjectAmount(null == pi.getTotalAmount() ? BigDecimal.ZERO : pi.getTotalAmount());
				projectPool.setCurrencyType(pi.getAmountCurrency());
			}
		}

		ProjectPool projectPoolFundSum = projectPoolDao.queryFundSumByProjectId(projectId);
		projectPool.setProjectAmount(
				null == projectPool.getProjectAmount() ? BigDecimal.ZERO : projectPool.getProjectAmount());
		projectPool.setProjectAmountCny(
				null == projectPool.getProjectAmountCny() ? BigDecimal.ZERO : projectPool.getProjectAmountCny());
		if (null != projectPoolFundSum) {
			projectPool.setUsedFundAmount(null == projectPoolFundSum.getUsedFundAmount() ? BigDecimal.ZERO
					: projectPoolFundSum.getUsedFundAmount());
			projectPool.setUsedFundAmountCny(null == projectPoolFundSum.getUsedFundAmountCny() ? BigDecimal.ZERO
					: projectPoolFundSum.getUsedFundAmountCny());
			projectPool.setRemainFundAmount(
					DecimalUtil.subtract(projectPool.getProjectAmount(), projectPool.getUsedFundAmount()));
			projectPool.setRemainFundAmountCny(
					DecimalUtil.subtract(projectPool.getProjectAmountCny(), projectPool.getUsedFundAmountCny()));
		}
		ProjectPool projectPoolAssertSum = projectPoolDao.queryAssertSumByProjectId(projectId);
		if (null != projectPoolAssertSum) {
			projectPool.setUsedAssetAmount(null == projectPoolAssertSum.getUsedAssetAmount() ? BigDecimal.ZERO
					: projectPoolAssertSum.getUsedAssetAmount());
			projectPool.setUsedAssetAmountCny(null == projectPoolAssertSum.getUsedAssetAmountCny() ? BigDecimal.ZERO
					: projectPoolAssertSum.getUsedAssetAmountCny());
			projectPool.setRemainAssetAmount(
					DecimalUtil.subtract(projectPool.getProjectAmount(), projectPool.getUsedAssetAmount()));
			projectPool.setRemainAssetAmountCny(
					DecimalUtil.subtract(projectPool.getProjectAmountCny(), projectPool.getUsedAssetAmountCny()));
		}
		return projectPool;
	}

	/**
	 * 封装响应数据
	 * 
	 * @param projectPool
	 * @return
	 * @throws Exception
	 */
	private ProjectPoolResDto convertToResDto(ProjectPool projectPool) throws Exception {
		ProjectPoolResDto dto = new ProjectPoolResDto();
		dto.setId(projectPool.getId());
		dto.setProjectId(projectPool.getProjectId());
		dto.setProjectname(cacheService.showProjectNameById(projectPool.getProjectId()));
		dto.setProjectAmount(DecimalUtil.toAmountString(projectPool.getProjectAmount()));
		dto.setCurrencyType(projectPool.getCurrencyType());
		dto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				projectPool.getCurrencyType() + ""));

		dto.setUsedAssetAmount(DecimalUtil.toAmountString(projectPool.getUsedAssetAmount()));
		dto.setUsedFundAmount(DecimalUtil.toAmountString(projectPool.getUsedFundAmount()));
		dto.setRemainAssetAmount(DecimalUtil.toAmountString(projectPool.getRemainAssetAmount()));
		dto.setRemainFundAmount(DecimalUtil.toAmountString(projectPool.getRemainFundAmount()));

		if (projectPool.getCurrencyType().compareTo(BaseConsts.ONE) == 0) {
			dto.setProjectAmountCny(DecimalUtil.toAmountString(projectPool.getProjectAmount()));
			dto.setUsedAssetAmountCny(DecimalUtil.toAmountString(projectPool.getUsedAssetAmount()));
			dto.setUsedFundAmountCny(DecimalUtil.toAmountString(projectPool.getUsedFundAmount()));
			dto.setRemainAssetAmountCny(DecimalUtil.toAmountString(projectPool.getRemainAssetAmount()));
			dto.setRemainFundAmountCny(DecimalUtil.toAmountString(projectPool.getRemainFundAmount()));
		} else {

			ProjectItem item = projectItemService.getProjectItem(projectPool.getProjectId());
			if (item.getAccountRateType() == 1) {
				Integer accountMethod = null;
				if (item.getAccountMethod() != null) {
					accountMethod = item.getAccountMethod();
				} else {
					accountMethod = BaseConsts.TWO;
				}

				BigDecimal cny_exrate = rateService.convertCurrency(item.getBankId() + "",
						item.getAmountCurrency() + "", BaseConsts.ONE + "", new Date(), accountMethod + "");

				dto.setProjectAmountCny(DecimalUtil.toAmountString(
						projectPool.getProjectAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setUsedAssetAmountCny(DecimalUtil.toAmountString(
						projectPool.getUsedAssetAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setUsedFundAmountCny(DecimalUtil.toAmountString(
						projectPool.getUsedFundAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setRemainAssetAmountCny(DecimalUtil.toAmountString(
						projectPool.getRemainAssetAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setRemainFundAmountCny(DecimalUtil.toAmountString(
						projectPool.getRemainFundAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));

			} else if (item.getAccountRateType() == 2) {

				// 取中国银行的汇率
				BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", item.getAmountCurrency() + "",
						BaseConsts.ONE + "", new Date());

				dto.setProjectAmountCny(DecimalUtil.toAmountString(
						projectPool.getProjectAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setUsedAssetAmountCny(DecimalUtil.toAmountString(
						projectPool.getUsedAssetAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setUsedFundAmountCny(DecimalUtil.toAmountString(
						projectPool.getUsedFundAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setRemainAssetAmountCny(DecimalUtil.toAmountString(
						projectPool.getRemainAssetAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));
				dto.setRemainFundAmountCny(DecimalUtil.toAmountString(
						projectPool.getRemainFundAmount().divide(cny_exrate, 2, BigDecimal.ROUND_HALF_UP)));

			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,条款:" + item.getItemNo() + "结算汇率类型错误");
			}
		}

		dto.setOpertaList(getOperList());
		return dto;
	}

	/**
	 * 根据状态获取操作列表 0表示查询，有删除操作
	 * 
	 * @param state
	 * @return
	 */

	private List<String> getOperListByState() {

		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.DETAIL);
		return opertaList;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProjectPoolResDto.Operate.operMap);
		return oprResult;
	}

}
