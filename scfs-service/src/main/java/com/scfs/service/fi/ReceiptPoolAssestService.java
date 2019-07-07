package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.api.pms.PmsPayPoRelDao;
import com.scfs.dao.fi.ReceiptPoolAssestDao;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.pay.PayPoRelationDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.resp.FundPoolResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.ReceiptPool;
import com.scfs.domain.fi.entity.ReceiptPoolAssest;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ReceiptPoolAssestService.java
 *  Description:  资金池资产服务类
 *  TODO
 *  Date,					Who,				
 *  2017年06月09日			Administrator
 *
 * </pre>
 */
@Service
public class ReceiptPoolAssestService {

	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ReceiptPoolDao receiptPoolDao;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private ReceiptPoolAssestDao receiptPoolAssestDao;
	@Autowired
	private ReceiptPoolService poolService;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BaseExchangeRateService rateService;
	@Autowired
	private PayPoRelationDao payPoRelationDao;
	@Autowired
	private PmsPayPoRelDao pmsPayPoRelDao;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 查询当期资金池ID对应的资产信息列表
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public PageResult<FundPoolResDto> queryFundPoolAssestResultByCon(FundPoolReqDto poolReqDto) {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		int offSet = PageUtil.getOffSet(poolReqDto.getPage(), poolReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poolReqDto.getPer_page());
		poolReqDto.setUserId(ServiceSupport.getUser().getId());
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		// 查询资金池信息
		if (pool != null) {
			// 封装当前查询数据
			poolReqDto.setBusinessUnitId(pool.getBusinessUnitId());
			// 带修改 根据业务需求 不增加币种的条件
			poolReqDto.setCurrencyType(null);
			poolReqDto.setId(null);
			List<ReceiptPoolAssest> receiptPoolAssests = receiptPoolAssestDao.queryFundPoolAssestResults(poolReqDto,
					rowBounds);
			List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
			if (CollectionUtils.isNotEmpty(receiptPoolAssests)) {
				for (ReceiptPoolAssest poolAssest : receiptPoolAssests) {
					FundPoolResDto fundPoolResDto = convertToFundPoolAssestDao(poolAssest);
					dtos.add(fundPoolResDto);
				}
			}
			int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poolReqDto.getPer_page());
			pageResult.setItems(dtos);
			pageResult.setLast_page(totalPage);
			pageResult.setTotal(CountHelper.getTotalRow());
			pageResult.setCurrent_page(poolReqDto.getPage());
			pageResult.setPer_page(poolReqDto.getPer_page());
		}
		return pageResult;
	}

	/**
	 * 封装当前资产列表信息
	 * 
	 * @param poolAssest
	 * @return
	 */
	public FundPoolResDto convertToFundPoolAssestDao(ReceiptPoolAssest poolAssest) {
		FundPoolResDto fundPoolResDto = new FundPoolResDto();
		fundPoolResDto.setId(poolAssest.getId());// 主键ID
		fundPoolResDto.setBusinessUnitId(poolAssest.getBusinessUnitId());// 经营单位ID
		fundPoolResDto.setBusinessUnitName(
				cacheService.getSubjectNcByIdAndKey(poolAssest.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));// 经营单位名称
		fundPoolResDto.setProject(poolAssest.getProjectId());
		fundPoolResDto.setProjectName(cacheService.getProjectNameById(poolAssest.getProjectId()));// 项目名称
		fundPoolResDto.setCustomerName(cacheService.getSubjectNoNameById(poolAssest.getCustomerId()));// 客户名称
		fundPoolResDto.setSupplieName(cacheService.getSubjectNoNameById(poolAssest.getSupplierId()));// 供应商名称
		fundPoolResDto.setCurrencyType(poolAssest.getBusinessUnitId());// 币种类型
		fundPoolResDto.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				poolAssest.getBillCurrencyType() + ""));// 币种名称
		fundPoolResDto.setBillAmount(DecimalUtil.formatScale2(poolAssest.getBillAmount()));// 单据金额
		fundPoolResDto.setExchangeRate(poolAssest.getExchangeRate());// 汇率
		fundPoolResDto.setBillAmountCny(DecimalUtil.formatScale2(poolAssest.getAmountCny()));// 人民币金额
		fundPoolResDto.setBusinessDate(poolAssest.getBusinessDate());// 记账日期
		fundPoolResDto.setType(poolAssest.getType());// 类型
		fundPoolResDto
				.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FUND_TYPE, poolAssest.getType() + ""));// 类型
		fundPoolResDto.setBillSource(poolAssest.getBillSource());// 单据来源
		if (poolAssest.getBillType().equals(BaseConsts.SEVEN) || poolAssest.getBillType().equals(BaseConsts.FOUR)) {
			fundPoolResDto.setBillSourceName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FUNDS_TYPE, poolAssest.getBillSource() + ""));// 来源名称
		} else {
			fundPoolResDto.setBillSourceName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.ASSERT_TYPE, poolAssest.getBillSource() + ""));// 来源名称
		}

		fundPoolResDto.setBillNo(poolAssest.getBillNo());// 单据编号
		fundPoolResDto.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (poolAssest.getBillType()) + ""));
		fundPoolResDto.setRemark(poolAssest.getRemark());// 备注
		return fundPoolResDto;
	}

	/**
	 * 新增出库单资金池资产数据
	 * 
	 * @param poolReqDto
	 */
	public void createPoolAssestOut(FundPoolReqDto poolReqDto) {
		// 根据传入的ID查询出库单数据
		BillOutStore billOutStore = billOutStoreDao.queryEntityById(poolReqDto.getId());
		if (billOutStore == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产出池出库单数据为空");
		}
		if (billOutStore.getBillType() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产出池出库单类型有误");
		}
		if (billOutStore.getStatus() != BaseConsts.FIVE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产出池出库单状态有误");
		}
		// 根据出库单的经营单位资金池是否存在数据
		BaseProject baseProject = cacheService.getProjectById(billOutStore.getProjectId());
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目为 【" + billOutStore.getProjectId() + "】对应的经营单位为空");
		}
		// 业务不完善 待改动
		poolReqDto.setBusinessUnitId(baseProject.getBusinessUnitId());// 经营单位
		poolReqDto.setCurrencyType(null);
		poolReqDto.setId(null);
		ReceiptPool pool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);
		if (null != pool) { // 资金池信息不为空
			// 根据当前出库查询付款单的汇率
			List<BillOutStoreDtl> dtls = billOutStoreDtlDao.queryResultOutStoreID(billOutStore.getId());
			if (CollectionUtils.isEmpty(dtls)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "出库单明细为空");
			}
			BigDecimal countAmount = BigDecimal.ZERO;
			BigDecimal amout = BigDecimal.ZERO;
			Integer currencyType = null;
			for (BillOutStoreDtl billOutStoreDtl : dtls) {
				// 根据当前的单价和数量算出金额
				BigDecimal outAmout = DecimalUtil.format(DecimalUtil.multiply(
						null == billOutStoreDtl.getPayPrice() ? BigDecimal.ZERO : billOutStoreDtl.getPayPrice(),
						null == billOutStoreDtl.getSendNum() ? BigDecimal.ZERO : billOutStoreDtl.getSendNum()));
				// 按照入库单汇率转换后的金额
				countAmount = DecimalUtil.add(
						DecimalUtil.multiply(outAmout,
								null == billOutStoreDtl.getPayRate() ? BigDecimal.ZERO : billOutStoreDtl.getPayRate()),
						countAmount);
				amout = DecimalUtil.add(amout, outAmout);
				currencyType = billOutStoreDtl.getPayRealCurrency();
			}
			// 当前计算的金额不等于0
			if (DecimalUtil.ne(countAmount, BigDecimal.ZERO)) {
				ReceiptPoolAssest poolAssest = new ReceiptPoolAssest();
				poolAssest.setType(BaseConsts.TWO);// 出库
				poolAssest.setBillNo(billOutStore.getBillNo());// 出库单编号
				poolAssest.setBillSource(BaseConsts.TWO);// 发货
				poolAssest.setCustomerId(billOutStore.getCustomerId());// 客户ID
				poolAssest.setProjectId(billOutStore.getProjectId());// 项目ID
				poolAssest.setBusinessUnitId(baseProject.getBusinessUnitId());// 经营单位
				poolAssest.setBusinessDate(billOutStore.getSendDate());// 发货日期
				poolAssest.setCreateAt(new Date());// 创建时间
				poolAssest.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
				poolAssest.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
				poolAssest.setBillId(billOutStore.getId());// 出库单ID
				poolAssest.setBillAmount(countAmount);// 单据占用金额
				poolAssest.setBillCurrencyType(currencyType);// 出库单币种
				poolAssest.setRemark("【出库单】金额:" + DecimalUtil.formatScale2(countAmount).toString());
				poolAssest.setBillType(BaseConsts.THREE);// 出库单
				// 取中国银行的汇率 计算出库单币种和日期算出汇率
				BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", BaseConsts.ONE + "",
						currencyType + "", billOutStore.getSendDate()); // 根据日期算出汇率
				if (currencyType == BaseConsts.ONE) {
					poolAssest.setAmountCny(countAmount);// 人民币的金额
					pool.setRemainAssetAmountCny(
							DecimalUtil.format(DecimalUtil.subtract(pool.getRemainAssetAmountCny(), countAmount)));
				} else {
					poolAssest.setAmountCny(DecimalUtil.format(DecimalUtil.multiply(countAmount, cny_exrate)));// 人民后的金额
					pool.setRemainAssetAmountCny(DecimalUtil.format(DecimalUtil.subtract(pool.getRemainAssetAmountCny(),
							DecimalUtil.multiply(countAmount, cny_exrate))));
				}
				// 修复库存金额
				pool.setStlAmount(DecimalUtil
						.subtract(pool.getStlAmount() == null ? BigDecimal.ZERO : pool.getStlAmount(), countAmount));
				pool.setRecAmount(DecimalUtil.add(pool.getRecAmount() == null ? BigDecimal.ZERO : pool.getRecAmount(),
						countAmount));
				pool.setRemainAssetAmount(
						DecimalUtil.format(DecimalUtil.subtract(pool.getRemainAssetAmount(), countAmount)));// 资产总值
				receiptPoolAssestDao.insertSelective(poolAssest);
				poolService.updateReceitPool(pool);
				// 新增出库单资产
				poolAssest.setBillAmount(DecimalUtil.multiply(new BigDecimal("-1"), poolAssest.getBillAmount()));// 单据占用金额
				poolAssest.setRemark("【出库单  记负数】金额:" + DecimalUtil.formatScale2(poolAssest.getBillAmount()).toString());
				poolAssest.setAmountCny(DecimalUtil.multiply(new BigDecimal("-1"), poolAssest.getAmountCny()));// 人民币的金额
				receiptPoolAssestDao.insertSelective(poolAssest);
				if (currencyType == BaseConsts.ONE) {
					pool.setRemainAssetAmountCny(DecimalUtil.format(DecimalUtil.subtract(pool.getRemainAssetAmountCny(),
							DecimalUtil.multiply(new BigDecimal("-1"), countAmount))));
				} else {
					pool.setRemainAssetAmountCny(
							DecimalUtil.format(DecimalUtil.subtract(pool.getRemainAssetAmountCny(), DecimalUtil
									.multiply(new BigDecimal("-1"), DecimalUtil.multiply(countAmount, cny_exrate)))));
				}
				pool.setRemainAssetAmount(DecimalUtil.format(DecimalUtil.subtract(pool.getRemainAssetAmount(),
						DecimalUtil.multiply(new BigDecimal("-1"), countAmount))));// 资产总值
				poolService.updateReceitPool(pool);

			}
		}
	}

	/**
	 * 新增资产入库单的数据
	 * 
	 * @param poolReqDto
	 */
	public void createPoolAssestIn(FundPoolReqDto poolReqDto) {
		// 根据入单单ID查询数据
		BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
		billInStoreSearchReqDto.setId(poolReqDto.getId());
		BillInStore billInStore = billInStoreDao.queryEntityById(billInStoreSearchReqDto);
		if (billInStore == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产入池入库单数据为空");
		}
		if (billInStore.getBillType() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产入池入库单类型有误");
		}
		if (billInStore.getStatus() != BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产入池入库单状态有误");
		}
		// 根据入库单的经营单位资金池是否存在数据
		BaseProject baseProject = cacheService.getProjectById(billInStore.getProjectId());
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池资产入池经营单位为空");
		}
		poolReqDto.setBusinessUnitId(baseProject.getBusinessUnitId());// 经营单位
		poolReqDto.setId(null);
		ReceiptPool pool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);
		if (pool != null) {
			Integer inStoreID = billInStore.getId();
			List<BillInStoreDtl> billInStoreDtls = billInStoreDtlDao.queryResultInStoreID(inStoreID);// 入库单的明细数据
			if (CollectionUtils.isEmpty(billInStoreDtls)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "入库单明细为空");
			}
			BigDecimal countAmount = BigDecimal.ZERO;
			BigDecimal payAllAmout = BigDecimal.ZERO;
			BigDecimal payAmout = BigDecimal.ZERO;
			Integer currencyType = null;
			Integer payWayType = null;
			for (BillInStoreDtl billInStoreDtl : billInStoreDtls) {
				// 根据当前的单价和数量算出金额
				BigDecimal outAmout = DecimalUtil.multiply(
						DecimalUtil.multiply(billInStoreDtl.getPayPrice(), billInStoreDtl.getReceiveNum()),
						billInStoreDtl.getPayRate());
				// 按照入库单汇率转换后的金额
				currencyType = billInStoreDtl.getPayRealCurrency();
				countAmount = DecimalUtil.format(DecimalUtil.add(outAmout, countAmount));
				// 根据业务需求 只需要根据一个poLineId查询付款单的付款类型就可以了
				Integer poLineId = billInStoreDtl.getPoDtlId();
				if (poLineId == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"入库单编号为" + billInStore.getBillNo() + "下的采购明细Id为空");
				}
				// 根据采购明细id查询付款单
				payWayType = payPoRelationDao.queryPayWayTypeByPoLineId(poLineId);
				if (payWayType == null) {
					// 当前采购类型的明细ID找不到付款单，就去找PMS铺货类型的采购ID
					payWayType = pmsPayPoRelDao.queryPmsPayPoLineIdByLineId(poLineId);
				}
				if (payWayType != null) {
					if (payWayType.equals(BaseConsts.ZERO)) {
						payAllAmout = DecimalUtil.add(payAllAmout, outAmout);
					} else if (payWayType.equals(BaseConsts.ONE)) {
						payAmout = DecimalUtil.add(payAmout, outAmout);
					}
				}
			}
			if (DecimalUtil.ne(countAmount, BigDecimal.ZERO)) {
				// 取中国银行的汇率 计算出库单的汇率
				BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", BaseConsts.ONE + "",
						currencyType + "", billInStore.getReceiveDate()); // 根据日期算出汇率
				ReceiptPoolAssest poolAssest = this.contPoolAssestIn(billInStore, currencyType, cny_exrate,
						baseProject.getBusinessUnitId());
				// 根据水单ID查询应收关联表 获取资金占用
				poolAssest.setBillAmount(countAmount);// 单据占用金额
				poolAssest.setRemark("【入库单】金额:" + DecimalUtil.formatScale2(countAmount).toString());
				poolAssest.setBillType(BaseConsts.TWO);// 入库单
				// 根据日期算出汇率
				if (currencyType == BaseConsts.ONE) {
					poolAssest.setAmountCny(countAmount);
					pool.setRemainAssetAmountCny(
							DecimalUtil.format(DecimalUtil.add(pool.getRemainAssetAmountCny(), countAmount)));
				} else {
					poolAssest.setAmountCny(DecimalUtil.format(DecimalUtil.multiply(countAmount, cny_exrate)));
					pool.setRemainAssetAmountCny(DecimalUtil.format(DecimalUtil.add(pool.getRemainAssetAmountCny(),
							DecimalUtil.multiply(countAmount, cny_exrate))));
				}
				receiptPoolAssestDao.insertSelective(poolAssest);
				pool.setRemainAssetAmount(
						DecimalUtil.format(DecimalUtil.add(pool.getRemainAssetAmount(), countAmount)));// 资产总值
				// 金额记正数的时候,根据支付类型回写金额并记录库存
				// if(payWayType != null){
				// if(payWayType==BaseConsts.ZERO){//全部
				pool.setPaymentAmount(DecimalUtil.subtract(
						pool.getPaymentAmount() == null ? BigDecimal.ZERO : pool.getPaymentAmount(), payAllAmout));
				// }else if(payWayType==BaseConsts.ONE){//预付
				pool.setAdvancePayAmount(DecimalUtil.subtract(
						pool.getAdvancePayAmount() == null ? BigDecimal.ZERO : pool.getAdvancePayAmount(), payAmout));
				// }
				// }
				pool.setStlAmount(DecimalUtil.add(pool.getStlAmount() == null ? BigDecimal.ZERO : pool.getStlAmount(),
						countAmount));
				poolService.updateReceitPool(pool);
				// 新增一条入库单金额为负数
				poolAssest.setBillAmount(DecimalUtil.multiply(new BigDecimal("-1"), poolAssest.getBillAmount()));
				poolAssest.setRemark("【入库单  记负数】金额:" + DecimalUtil.formatScale2(poolAssest.getBillAmount()).toString());
				poolAssest.setAmountCny(DecimalUtil.multiply(new BigDecimal("-1"), poolAssest.getAmountCny()));
				if (currencyType == BaseConsts.ONE) {
					pool.setRemainAssetAmountCny(DecimalUtil.format(DecimalUtil.add(pool.getRemainAssetAmountCny(),
							DecimalUtil.multiply(new BigDecimal("-1"), countAmount))));
				} else {
					pool.setRemainAssetAmountCny(
							DecimalUtil.format(DecimalUtil.add(pool.getRemainAssetAmountCny(), DecimalUtil
									.multiply(DecimalUtil.multiply(new BigDecimal("-1"), countAmount), cny_exrate))));
				}
				receiptPoolAssestDao.insertSelective(poolAssest);
				pool.setRemainAssetAmount(DecimalUtil.format(DecimalUtil.add(pool.getRemainAssetAmount(),
						DecimalUtil.multiply(new BigDecimal("-1"), countAmount))));// 资产总值
				poolService.updateReceitPool(pool);
			}
		}
	}

	/**
	 * 封装入库单的入池数据
	 * 
	 * @param billInStore
	 * @param currencyType
	 * @param cny_exrate
	 * @param busId
	 * @return
	 */
	private ReceiptPoolAssest contPoolAssestIn(BillInStore billInStore, Integer currencyType, BigDecimal cny_exrate,
			Integer busId) {
		ReceiptPoolAssest poolAssest = new ReceiptPoolAssest();
		poolAssest.setType(BaseConsts.ONE);// 入库
		poolAssest.setBillNo(billInStore.getBillNo());// 水单编号
		poolAssest.setBillSource(BaseConsts.ONE);// 收货
		poolAssest.setCustomerId(billInStore.getCustomerId());// 客户ID
		poolAssest.setProjectId(billInStore.getProjectId());// 项目ID
		poolAssest.setBusinessDate(billInStore.getReceiveDate());// 发货日期
		poolAssest.setSupplierId(billInStore.getSupplierId());// 供应商ID
		poolAssest.setBusinessUnitId(busId);// 项目ID
		poolAssest.setCreateAt(new Date());// 创建时间
		poolAssest.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		poolAssest.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		poolAssest.setBillId(billInStore.getId());// 水单ID
		poolAssest.setBillCurrencyType(currencyType);// 水单币种
		poolAssest.setExchangeRate(cny_exrate);// 汇率
		return poolAssest;
	}

	/**
	 * 付款单货款资产的入池操作
	 * 
	 * @param payOrder
	 *            付款单
	 * @param cny_exrate
	 *            汇率
	 * @param amount
	 *            金额
	 * @param receiptPool
	 *            资金池信息
	 */
	public void createPoolAssestPay1_1(PayOrder payOrder, BigDecimal cny_exrate, BigDecimal amount) {
		ReceiptPoolAssest poolAssest = this.contPoolAssestPay(payOrder, cny_exrate, amount);
		poolAssest.setRemark("【付款单 货款】金额:" + DecimalUtil.formatScale2(amount).toString());
		receiptPoolAssestDao.insertSelective(poolAssest);
	}

	/**
	 * 付款单费用资产的入池操作
	 * 
	 * @param payOrder
	 *            付款单
	 * @param cny_exrate
	 *            汇率
	 * @param amount
	 *            金额
	 * @param receiptPool
	 *            资金池信息
	 */
	public void createPoolAssestPay1_2(PayOrder payOrder, BigDecimal cny_exrate, BigDecimal amount) {
		ReceiptPoolAssest poolAssest = this.contPoolAssestPay(payOrder, cny_exrate, amount);
		poolAssest.setRemark("【付款单 费用单】金额:" + DecimalUtil.formatScale2(amount).toString());
		receiptPoolAssestDao.insertSelective(poolAssest);
	}

	/**
	 * 资金池付款单数据的封装
	 * 
	 * @param payOrder
	 * @param cny_exrate
	 * @param amount
	 * @return
	 */
	private ReceiptPoolAssest contPoolAssestPay(PayOrder payOrder, BigDecimal cny_exrate, BigDecimal amount) {
		// 新增资产信息
		ReceiptPoolAssest poolAssest = new ReceiptPoolAssest();
		poolAssest.setType(BaseConsts.ONE);// 入库
		poolAssest.setBillNo(payOrder.getPayNo());// 付款单编号
		poolAssest.setBillSource(BaseConsts.TWO);// 付款
		poolAssest.setProjectId(payOrder.getProjectId());// 项目ID
		poolAssest.setBusinessUnitId(payOrder.getBusiUnit());// 经营单位
		poolAssest.setSupplierId(payOrder.getPayee());// 供应商ID
		poolAssest.setBusinessDate(payOrder.getConfirmorAt());// 记账日期
		poolAssest.setCreateAt(new Date());// 创建时间
		poolAssest.setBillAmount(amount);// 单据金额
		poolAssest.setBillCurrencyType(payOrder.getRealCurrencyType());// 单据币种
		poolAssest.setBillType(BaseConsts.FOUR);// 付款单
		poolAssest.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		poolAssest.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		poolAssest.setBillId(payOrder.getId());// 单据Id
		poolAssest.setBillType(BaseConsts.FOUR);// 付款单
		poolAssest.setExchangeRate(cny_exrate);// 汇率.
		if (payOrder.getRealCurrencyType().equals(BaseConsts.ONE)) {// 人民币
			poolAssest.setAmountCny(amount);
		} else {
			poolAssest.setAmountCny(DecimalUtil.format(DecimalUtil.multiply(amount, cny_exrate)));
		}
		return poolAssest;
	}

	/**
	 * 资金资产水单的数据增加
	 * 
	 * @param bankReceipt
	 * @param cny_exrate
	 * @param amount
	 * @return
	 */
	public void createPoolAssestRec(BankReceipt bankReceipt, BigDecimal cny_exrate, BigDecimal amount) {
		// 新增资产信息
		ReceiptPoolAssest poolAssest = new ReceiptPoolAssest();
		poolAssest.setType(BaseConsts.TWO);// 出库
		poolAssest.setBillNo(bankReceipt.getReceiptNo());// 水单编号
		poolAssest.setBillSource(BaseConsts.ONE);// 收款
		poolAssest.setCustomerId(bankReceipt.getCustId());// 客户ID
		poolAssest.setProjectId(bankReceipt.getProjectId());// 项目ID
		poolAssest.setBusinessUnitId(bankReceipt.getBusiUnit());// 经营单位
		poolAssest.setBusinessDate(bankReceipt.getReceiptDate());// 发货日期
		poolAssest.setCreateAt(new Date());// 创建时间
		poolAssest.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		poolAssest.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		poolAssest.setBillId(bankReceipt.getId());// 出库单ID
		poolAssest.setBillAmount(amount);// 单据占用金额
		poolAssest.setBillCurrencyType(bankReceipt.getActualCurrencyType());// 水单币种
		if (bankReceipt.getReceiptType() == BaseConsts.ONE) {
			poolAssest.setRemark("【水单 回款】金额:" + DecimalUtil.formatScale2(amount).toString());
		}
		if (bankReceipt.getReceiptType() == BaseConsts.TWO) {
			poolAssest.setRemark("【水单 预收定金】金额:" + DecimalUtil.formatScale2(amount).toString());
		}
		if (bankReceipt.getReceiptType() == BaseConsts.THREE) {
			poolAssest.setRemark("【水单 预收货款】金额:" + DecimalUtil.formatScale2(amount).toString());
		}
		poolAssest.setBillType(BaseConsts.SEVEN);// 出库单
		if (bankReceipt.getActualCurrencyType() == BaseConsts.ONE) {
			poolAssest.setAmountCny(amount);// 人民币的金额
		} else {
			poolAssest.setAmountCny(DecimalUtil.format(DecimalUtil.multiply(amount, cny_exrate)));// 人民后的金额
		}
		receiptPoolAssestDao.insertSelective(poolAssest);
	}

	public List<FundPoolResDto> queryReceiptPoolFundResultsByEx(FundPoolReqDto poolReqDto) {
		poolReqDto.setUserId(ServiceSupport.getUser().getId());
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
		// 查询资金池信息
		if (pool != null) {
			// 封装当前查询数据
			poolReqDto.setBusinessUnitId(pool.getBusinessUnitId());
			// 带修改 根据业务需求 不增加币种的条件
			poolReqDto.setCurrencyType(null);
			poolReqDto.setId(null);
			List<ReceiptPoolAssest> receiptPoolAssests = receiptPoolAssestDao.queryFundPoolAssestResults(poolReqDto);
			if (CollectionUtils.isNotEmpty(receiptPoolAssests)) {
				for (ReceiptPoolAssest poolAssest : receiptPoolAssests) {
					FundPoolResDto fundPoolResDto = convertToFundPoolAssestDao(poolAssest);
					dtos.add(fundPoolResDto);
				}
			}
		}
		return dtos;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(FundPoolReqDto poolReqDto) {
		poolReqDto.setUserId(ServiceSupport.getUser().getId());
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		poolReqDto.setBusinessUnitId(pool.getBusinessUnitId()); // 经营单位
		poolReqDto.setCurrencyType(null);// 币种
		poolReqDto.setId(null);
		int count = receiptPoolAssestDao.isOverasyncMaxLine(poolReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("融资池资产数据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncReceiptPoolAssestApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/pool/receiptPoolAssest_list");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_42);
			asyncExcelService.addAsyncExcel(poolReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	/**
	 * 融资池明细数据导出
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public Map<String, Object> asyncReceiptPoolAssestApplyExport(FundPoolReqDto poolReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<FundPoolResDto> fundPoolResDtos = queryFundPoolAssestResultByCon(poolReqDto).getItems();
		model.put("receiptPoolAssestList", fundPoolResDtos);
		return model;
	}
}
