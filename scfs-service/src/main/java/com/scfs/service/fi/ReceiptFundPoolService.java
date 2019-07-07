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
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.fi.ReceiptPoolDtlDao;
import com.scfs.dao.fi.ReceiptPoolFundDtlDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayFeeRelationDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.resp.FundPoolResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.ReceiptPool;
import com.scfs.domain.fi.entity.ReceiptPoolDtl;
import com.scfs.domain.fi.entity.ReceiptPoolFundDtl;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class ReceiptFundPoolService {
	@Autowired
	private ReceiptPoolDtlDao receiptPoolDtlDao;
	@Autowired
	BankReceiptDao bankReceiptDao;
	@Autowired
	private ReceiptPoolDao receiptPoolDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private RecReceiptRelDao recReceiptRelDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayFeeRelationDao payFeeRelationDao;
	@Autowired
	private BaseExchangeRateService rateService;
	@Autowired
	private ReceiptPoolFundDtlDao poolFundDtlDao;
	@Autowired
	private ReceiptPoolService receiptPoolService;
	@Autowired
	private ReceiptPoolAssestService receiptPoolAssestService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 根据资金ID查询资金信息列表
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public PageResult<FundPoolResDto> queryPoolFundResultByCon(FundPoolReqDto poolReqDto) {
		if (poolReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池查询信息为空");
		}
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		if (pool != null) {
			int offSet = PageUtil.getOffSet(poolReqDto.getPage(), poolReqDto.getPer_page());
			RowBounds rowBounds = new RowBounds(offSet, poolReqDto.getPer_page());
			poolReqDto.setCurrencyType(pool.getCurrencyType());// 币种类型
			poolReqDto.setId(null);
			poolReqDto.setBusinessUnitId(pool.getBusinessUnitId());// 经营单位
			List<ReceiptPoolFundDtl> poolFundDtl = poolFundDtlDao.selectPoolFundResultByCon(poolReqDto, rowBounds);
			List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
			if (CollectionUtils.isNotEmpty(poolFundDtl)) {
				for (ReceiptPoolFundDtl receiptPoolFundDtl : poolFundDtl) {
					FundPoolResDto fundPoolResDto = convertToFundPoolDao(receiptPoolFundDtl);
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
	 * 封装资金明细
	 * 
	 * @param receiptPoolFundDtl
	 * @return
	 */
	public FundPoolResDto convertToFundPoolDao(ReceiptPoolFundDtl receiptPoolFundDtl) {
		FundPoolResDto fundPoolResDto = new FundPoolResDto();
		fundPoolResDto.setId(receiptPoolFundDtl.getId());// 主键ID
		fundPoolResDto.setBusinessUnitId(receiptPoolFundDtl.getBusinessUnitId());// 经营单位ID
		fundPoolResDto.setBusinessUnitName(
				cacheService.getSubjectNcByIdAndKey(receiptPoolFundDtl.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));// 经营单位名称
		fundPoolResDto.setCurrencyType(receiptPoolFundDtl.getBusinessUnitId());// 币种类型
		fundPoolResDto.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				receiptPoolFundDtl.getBillCurrencyType() + ""));// 币种名称
		fundPoolResDto.setProject(receiptPoolFundDtl.getProjectId());
		fundPoolResDto.setProjectName(cacheService.getProjectNameById(receiptPoolFundDtl.getProjectId()));// 经营单位名称
		fundPoolResDto.setCustomerName(cacheService.getSubjectNoNameById(receiptPoolFundDtl.getCustomerId()));
		fundPoolResDto.setSupplieName(cacheService.getSubjectNoNameById(receiptPoolFundDtl.getSupplierId()));
		fundPoolResDto.setBillAmount(DecimalUtil.formatScale2(receiptPoolFundDtl.getBillAmount()));// 单据金额
		fundPoolResDto.setExchangeRate(receiptPoolFundDtl.getExchangeRate());
		fundPoolResDto.setBillAmountCny(DecimalUtil.formatScale2(receiptPoolFundDtl.getBillAmountCny()));
		fundPoolResDto.setBusinessDate(receiptPoolFundDtl.getBusinessDate());
		fundPoolResDto.setType(receiptPoolFundDtl.getType());
		fundPoolResDto.setTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.FUND_TYPE, receiptPoolFundDtl.getType() + ""));
		fundPoolResDto.setBillSource(receiptPoolFundDtl.getBillSource());
		fundPoolResDto.setBillSourceName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.FUNDS_TYPE, receiptPoolFundDtl.getBillSource() + ""));
		fundPoolResDto.setBillNo(receiptPoolFundDtl.getBillNo());
		fundPoolResDto.setBillType(receiptPoolFundDtl.getBillType());
		fundPoolResDto.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (receiptPoolFundDtl.getBillType()) + ""));
		fundPoolResDto.setRemark(receiptPoolFundDtl.getRemark());
		return fundPoolResDto;
	}

	/**
	 * 新增资金池资金信息
	 * 
	 * @param poolReqDto
	 */
	public void createPoolFundByCon(FundPoolReqDto poolReqDto) {
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(poolReqDto.getId());
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单信息为空");
		}
		// 水单为核完状态
		if (bankReceipt.getState() != BaseConsts.THREE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态有误");
		}
		// 根据水单的经营单位和币种查询资金池
		poolReqDto.setCurrencyType(bankReceipt.getActualCurrencyType());
		poolReqDto.setBusinessUnitId(bankReceipt.getBusiUnit());
		poolReqDto.setId(null);
		ReceiptPool receiptPool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);
		if (null != receiptPool) {
			// 新增当前资金明细的数据
			ReceiptPoolFundDtl fundDtl = new ReceiptPoolFundDtl();
			fundDtl.setType(BaseConsts.TWO);// 出
			fundDtl.setBillNo(bankReceipt.getReceiptNo());// 水单编号
			fundDtl.setBillSource(BaseConsts.ONE);// 收款
			fundDtl.setCustomerId(bankReceipt.getCustId());// 客户ID
			fundDtl.setBusinessUnitId(bankReceipt.getBusiUnit());// 经营单位
			fundDtl.setProjectId(bankReceipt.getProjectId());// 项目ID
			fundDtl.setBusinessDate(bankReceipt.getReceiptDate());// 水单日期
			fundDtl.setCreateAt(new Date());// 创建时间
			fundDtl.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
			fundDtl.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
			fundDtl.setBillId(bankReceipt.getId());// 水单ID
			// 根据水单ID查询应收关联表 获取资金占用
			BigDecimal fundUsed = recReceiptRelDao.queryFundUsedByReceiptId(bankReceipt.getId());
			fundUsed = null == fundUsed ? BigDecimal.ZERO : fundUsed;
			if (DecimalUtil.ne(fundUsed, BigDecimal.ZERO)) {
				fundDtl.setBillAmount(fundUsed);// 单据占用金额
				fundDtl.setBillCurrencyType(bankReceipt.getActualCurrencyType());// 水单实际币种
				if (bankReceipt.getReceiptType() == BaseConsts.ONE) {
					fundDtl.setRemark("【水单 回款】金额:" + DecimalUtil.formatScale2(fundUsed).toString());
				}
				if (bankReceipt.getReceiptType() == BaseConsts.TWO) {
					fundDtl.setRemark("【水单 预收定金】金额:" + DecimalUtil.formatScale2(fundUsed).toString());
				}
				if (bankReceipt.getReceiptType() == BaseConsts.THREE) {
					fundDtl.setRemark("【水单 预收货款】金额:" + DecimalUtil.formatScale2(fundUsed).toString());
				}
				// // 取中国银行的汇率
				BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", BaseConsts.ONE + "",
						bankReceipt.getActualCurrencyType() + "", bankReceipt.getReceiptDate()); // 根据日期算出汇率
				cny_exrate = null == cny_exrate ? BigDecimal.ZERO : cny_exrate;
				fundDtl.setExchangeRate(cny_exrate);// 汇率
				fundDtl.setBillType(BaseConsts.SEVEN);// 水单
				if (bankReceipt.getActualCurrencyType() == BaseConsts.ONE) {// 币种为人民币
					fundDtl.setBillAmountCny(fundUsed);
					receiptPool.setUsedFundAmountCny(DecimalUtil
							.formatScale2(DecimalUtil.subtract(receiptPool.getUsedFundAmountCny(), fundUsed)));// 已使用资金额度变动CNY
					receiptPool.setRemainFundAmountCny(
							DecimalUtil.formatScale2(DecimalUtil.add(receiptPool.getRemainFundAmountCny(), fundUsed)));// 资金余额变动CNY
					receiptPool.setRemainAssetAmountCny(
							DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainAssetAmountCny(), fundUsed)));
				} else {
					fundDtl.setBillAmountCny(DecimalUtil.format(DecimalUtil.multiply(fundUsed, cny_exrate)));
					receiptPool.setUsedFundAmountCny(DecimalUtil.formatScale2(DecimalUtil
							.subtract(receiptPool.getUsedFundAmountCny(), DecimalUtil.multiply(fundUsed, cny_exrate))));// 已使用资金额度变动CNY
					receiptPool.setRemainFundAmountCny(DecimalUtil.add(receiptPool.getRemainFundAmountCny(),
							DecimalUtil.multiply(fundUsed, cny_exrate)));// 资金余额变动CNY
					receiptPool.setRemainAssetAmountCny(DecimalUtil.format(DecimalUtil.subtract(
							receiptPool.getRemainAssetAmountCny(), DecimalUtil.multiply(fundUsed, cny_exrate))));
				}
				poolFundDtlDao.insertPoolDtl(fundDtl);
				receiptPool.setUsedFundAmount(
						DecimalUtil.format(DecimalUtil.subtract(receiptPool.getUsedFundAmount(), fundUsed)));// 已使用资金额度变动
				receiptPool.setRemainFundAmount(DecimalUtil.add(receiptPool.getRemainFundAmount(), fundUsed));// 资金余额变动
				receiptPool.setRemainAssetAmount(
						DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainAssetAmount(), fundUsed)));// 资产总值
				// 新增资金资产信息数据
				receiptPoolAssestService.createPoolAssestRec(bankReceipt, cny_exrate, fundUsed);
				receiptPool.setRecAmount(DecimalUtil.subtract(receiptPool.getRecAmount(), fundUsed));
				receiptPoolService.updateReceitPool(receiptPool);
			}
		}
	}

	/**
	 * 校验水单的数据
	 * 
	 * @param bankReceipt
	 */
	public void checkReceipt(BankReceipt bankReceipt) {
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单信息为空");
		}
		// 水单为核完的状态
		if (bankReceipt.getState() != BaseConsts.THREE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态有误");
		}
		if (bankReceipt.getReceiptType() != BaseConsts.FIVE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单类型有误");
		}
	}

	/**
	 * 封装付款单资金池公共属性
	 * 
	 * @return
	 */
	private ReceiptPoolFundDtl poolPayAltAttribute(ReceiptPoolFundDtl fundDtl, PayOrder payOrder,
			BigDecimal cny_exrate) {
		fundDtl.setType(BaseConsts.ONE);// 入
		fundDtl.setBillNo(payOrder.getPayNo());// 付款编号
		fundDtl.setBillSource(BaseConsts.TWO);// 付款
		fundDtl.setBusinessUnitId(payOrder.getBusiUnit());// 经营单位
		fundDtl.setSupplierId(payOrder.getPayee());// 供应商ID
		fundDtl.setProjectId(payOrder.getProjectId());// 项目ID
		fundDtl.setBusinessDate(payOrder.getConfirmorAt());// 记账日期
		fundDtl.setCreateAt(new Date());// 创建时间
		fundDtl.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		fundDtl.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		fundDtl.setBillId(payOrder.getId());// 付款单ID
		fundDtl.setBillCurrencyType(payOrder.getRealCurrencyType());// 付款单币种
		fundDtl.setBillType(BaseConsts.FOUR);// 付款单
		fundDtl.setExchangeRate(cny_exrate);// 汇率
		return fundDtl;

	}

	/***
	 * 付款单资金的添加
	 * 
	 * @param poolReqDto
	 */
	public void createFundPoolPayByCon(FundPoolReqDto poolReqDto) {
		PayOrder payOrder = payOrderDao.queryEntityById(poolReqDto.getId());
		if (payOrder == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单信息为空");
		}
		if (payOrder.getState() != BaseConsts.SIX) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态不对");
		}
		// 根据水单的经营单位和币种查询资金池
		poolReqDto.setCurrencyType(payOrder.getRealCurrencyType());
		poolReqDto.setBusinessUnitId(payOrder.getBusiUnit());
		poolReqDto.setId(null);
		ReceiptPool receiptPool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);
		if (null != receiptPool) {
			// 取中国银行的汇率
			BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", BaseConsts.ONE + "",
					payOrder.getRealCurrencyType() + "", payOrder.getConfirmorAt()); // 根据日期算出汇率
			cny_exrate = null == cny_exrate ? BigDecimal.ZERO : cny_exrate;
			if (payOrder.getPayType() == BaseConsts.ONE) {// 订单货款的操作
				this.payTypeInPool1_1(payOrder, receiptPool, cny_exrate);
			} else if (payOrder.getPayType() == BaseConsts.TWO) {// 付费用单
				this.payTypeInPool1_2(payOrder, receiptPool, cny_exrate);
			} else if (payOrder.getPayType() == BaseConsts.THREE) {// 付借款单
				this.payTypeInPool1_3(payOrder, receiptPool, cny_exrate);
			}
		}
	}

	/**
	 * 付款单类型为订单货款 入池操作
	 * 
	 * @param payOrder
	 * @param receiptPool
	 * @param cny_exrate
	 */
	public void payTypeInPool1_1(PayOrder payOrder, ReceiptPool receiptPool, BigDecimal cny_exrate) {
		// 当前资金明细的数据
		ReceiptPoolFundDtl fundDtl = new ReceiptPoolFundDtl();
		fundDtl = this.poolPayAltAttribute(fundDtl, payOrder, cny_exrate);
		// 根据付款单id查询 实际付款金额减去与预收金额
		BigDecimal amount = payOrderDao.queryPayRecrAmount(payOrder.getId());
		fundDtl.setBillAmount(amount);// 单据占用金额
		fundDtl.setRemark("【付款单  订单货款】金 额:" + DecimalUtil.formatScale2(amount).toString());
		if (payOrder.getRealCurrencyType() == BaseConsts.ONE) {// 人民币币种
			fundDtl.setBillAmountCny(amount);
			receiptPool.setUsedFundAmountCny(
					DecimalUtil.format(DecimalUtil.add(receiptPool.getUsedFundAmountCny(), amount)));// 已使用资金额度变动CNY
			receiptPool.setRemainFundAmountCny(
					DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainFundAmountCny(), amount)));// 资金余额变动CNY
			receiptPool.setRemainAssetAmountCny(
					DecimalUtil.format(DecimalUtil.add(receiptPool.getRemainAssetAmountCny(), amount)));
		} else {
			fundDtl.setBillAmountCny(DecimalUtil.format(DecimalUtil.multiply(amount, cny_exrate)));
			receiptPool.setUsedFundAmountCny(DecimalUtil.format(
					DecimalUtil.add(receiptPool.getUsedFundAmountCny(), DecimalUtil.multiply(amount, cny_exrate))));// 已使用资金额度变动CNY
			receiptPool.setRemainFundAmountCny(DecimalUtil.format(DecimalUtil
					.subtract(receiptPool.getRemainFundAmountCny(), DecimalUtil.multiply(amount, cny_exrate))));// 资金余额变动CNY
			receiptPool.setRemainAssetAmountCny(DecimalUtil.format(
					DecimalUtil.add(receiptPool.getRemainAssetAmountCny(), DecimalUtil.multiply(amount, cny_exrate))));
		}

		// 新增资金明细
		poolFundDtlDao.insertPoolDtl(fundDtl);
		// 改变资金池信息
		receiptPool.setUsedFundAmount(DecimalUtil.format(DecimalUtil.add(receiptPool.getUsedFundAmount(), amount)));// 已使用资金额度变动
		receiptPool.setRemainFundAmount(
				DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainFundAmount(), amount)));// 资金余额变动
		receiptPool
				.setRemainAssetAmount(DecimalUtil.format(DecimalUtil.add(receiptPool.getRemainAssetAmount(), amount)));// 资产总值
		// 新增付款单资产的入库数据
		receiptPoolAssestService.createPoolAssestPay1_1(payOrder, cny_exrate, amount);
		// 修改资金池的数据
		receiptPool = this.contReceiptPoolPay(receiptPool, payOrder.getPayWayType(), amount, payOrder.getPayType());
		receiptPoolService.updateReceitPool(receiptPool);
	}

	/**
	 * 公共 封装付款单的资金池预付款
	 * 
	 * @param receiptPool
	 * @param payWay
	 *            付款支付类型
	 * @param amount
	 *            金额
	 * @return
	 */
	public ReceiptPool contReceiptPoolPay(ReceiptPool receiptPool, Integer payWay, BigDecimal amount, Integer payType) {
		if (payWay == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单付款支付类型为空");
		}
		if (payType.equals(BaseConsts.ONE)) {// 订单货款付款类型
			if (payWay.equals(BaseConsts.ZERO)) {// 全部
				receiptPool.setPaymentAmount(DecimalUtil.add(receiptPool.getPaymentAmount(), amount));
			} else if (payWay.equals(BaseConsts.ONE)) {// 预付
				receiptPool.setAdvancePayAmount(DecimalUtil.add(receiptPool.getAdvancePayAmount(), amount));
			}
		}
		return receiptPool;
	}

	/**
	 * 付款单费用单类型的入池操作
	 * 
	 * @param payOrder
	 * @param receiptPool
	 * @param cny_exrate
	 */
	public void payTypeInPool1_2(PayOrder payOrder, ReceiptPool receiptPool, BigDecimal cny_exrate) {
		List<PayFeeRelationModel> feeRelationModels = payFeeRelationDao.queryPayFeeByPayID(payOrder.getId());
		if (CollectionUtils.isNotEmpty(feeRelationModels)) {
			BigDecimal countAmount = BigDecimal.ZERO;
			BigDecimal bfCounAmount = BigDecimal.ZERO;
			for (PayFeeRelationModel payFeeRelationModel : feeRelationModels) {
				// 当前资金明细的数据
				ReceiptPoolFundDtl fundDtl = new ReceiptPoolFundDtl();
				fundDtl = this.poolPayAltAttribute(fundDtl, payOrder, cny_exrate);
				BigDecimal amount = BigDecimal.ZERO;
				// if
				// (payOrder.getRealCurrencyType().equals(payOrder.getCurrnecyType()))
				// {// 支付币种等于实际支付币种
				// amount = payFeeRelationModel.getPayAmount();
				// fundDtl.setBillAmount(amount);// 单据金额
				// } else {
				// 币种不一致 根据当前的资金占用金额*付款汇率
				amount = DecimalUtil
						.format(DecimalUtil.multiply(payFeeRelationModel.getPayAmount(), payOrder.getPayRate()));
				fundDtl.setBillAmount(amount);// 单据金额
				// }
				bfCounAmount = DecimalUtil.add(bfCounAmount, amount);
				fundDtl.setRemark("【付款单  付费用】金额:" + DecimalUtil.formatScale2(amount).toString());
				if (payOrder.getRealCurrencyType() == BaseConsts.ONE) {
					fundDtl.setBillAmountCny(amount);
					countAmount = DecimalUtil.add(countAmount, amount);
				} else {
					countAmount = DecimalUtil.add(countAmount, DecimalUtil.multiply(amount, cny_exrate));
					fundDtl.setBillAmountCny(DecimalUtil.format(DecimalUtil.multiply(amount, cny_exrate)));
				}
				poolFundDtlDao.insertPoolDtl(fundDtl);
				receiptPoolAssestService.createPoolAssestPay1_2(payOrder, cny_exrate, amount);
			}
			receiptPool.setUsedFundAmountCny(
					DecimalUtil.format(DecimalUtil.add(receiptPool.getUsedFundAmountCny(), countAmount)));// 已使用资金额度变动CNY
			receiptPool.setRemainFundAmountCny(
					DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainFundAmountCny(), countAmount)));// 资金余额变动CNY
			receiptPool.setUsedFundAmount(
					DecimalUtil.format(DecimalUtil.add(receiptPool.getUsedFundAmount(), bfCounAmount)));// 已使用资金额度变动
			receiptPool.setRemainFundAmount(
					DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainFundAmount(), bfCounAmount)));// 资金余额变动
			receiptPool.setRemainAssetAmountCny(
					DecimalUtil.format(DecimalUtil.add(receiptPool.getRemainAssetAmountCny(), countAmount)));
			receiptPool.setRemainAssetAmount(
					DecimalUtil.format(DecimalUtil.add(receiptPool.getRemainAssetAmount(), bfCounAmount)));// 资产总值
			receiptPool = this.contReceiptPoolPay(receiptPool, payOrder.getPayWayType(), bfCounAmount,
					payOrder.getPayType());
			receiptPoolService.updateReceitPool(receiptPool);
		}
	}

	/**
	 * 付款单状态为付借款单
	 * 
	 * @param payOrder
	 * @param receiptPool
	 * @param cny_exrate
	 */
	public void payTypeInPool1_3(PayOrder payOrder, ReceiptPool receiptPool, BigDecimal cny_exrate) {
		BigDecimal realPayAmount = payOrder.getRealPayAmount() == null ? BigDecimal.ZERO : payOrder.getRealPayAmount();
		receiptPool.setCountFundAmount(DecimalUtil.formatScale2(
				DecimalUtil.format(DecimalUtil.subtract(receiptPool.getCountFundAmount(), realPayAmount))));// 资金额度
		receiptPool.setRemainFundAmount(DecimalUtil.formatScale2(
				DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainFundAmount(), realPayAmount))));// 资金余额
		if (payOrder.getRealCurrencyType() == BaseConsts.ONE) {// 人民币
			receiptPool.setCountFundAmountCny(DecimalUtil.formatScale2(
					DecimalUtil.format(DecimalUtil.subtract(receiptPool.getCountFundAmount(), realPayAmount))));// 资金额度（CNY）
			receiptPool.setRemainFundAmountCny(DecimalUtil.formatScale2(
					DecimalUtil.format(DecimalUtil.subtract(receiptPool.getRemainFundAmountCny(), realPayAmount))));// 资金余额（CNY）
		} else {
			receiptPool.setCountFundAmountCny(DecimalUtil.formatScale2(
					DecimalUtil.subtract(DecimalUtil.format(DecimalUtil.multiply(realPayAmount, cny_exrate)),
							receiptPool.getCountFundAmount())));// 资金额度（CNY）
			receiptPool.setRemainFundAmountCny(DecimalUtil.formatScale2(DecimalUtil
					.subtract(receiptPool.getRemainFundAmountCny(), DecimalUtil.multiply(cny_exrate, realPayAmount))));// 资金余额（CNY）
		}
		receiptPool = this.contReceiptPoolPay(receiptPool, payOrder.getPayWayType(), realPayAmount,
				payOrder.getPayType());
		// 新增或者是更新资金池
		receiptPoolService.updateReceitPool(receiptPool);
		this.createPoolDtlByPay(payOrder, cny_exrate);
	}

	/**
	 * 新增资金池明细表数据
	 * 
	 * @param bankReceipt
	 */
	public void createPoolDtlByPay(PayOrder payOrder, BigDecimal cny_exrate) {
		ReceiptPoolDtl poolDtl = new ReceiptPoolDtl();
		poolDtl.setCurrencyType(payOrder.getRealCurrencyType());// 币种
		poolDtl.setPayId(payOrder.getId());// 付款单ID
		poolDtl.setBusinessUnitId(payOrder.getBusiUnit());// 经营单位
		poolDtl.setBillAmount(payOrder.getRealPayAmount());// 金额
		poolDtl.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		poolDtl.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		poolDtl.setBillNo(payOrder.getPayNo());
		poolDtl.setCreateAt(new Date());// 创建时间
		poolDtl.setProjectId(payOrder.getProjectId());// 项目ID
		poolDtl.setExchangeRate(cny_exrate);// 汇率
		if (payOrder.getRealCurrencyType() == BaseConsts.ONE) {// 人民币币种
			poolDtl.setBillAmountCny(payOrder.getRealPayAmount());
		} else {
			poolDtl.setBillAmountCny(DecimalUtil.format(DecimalUtil.multiply(payOrder.getRealPayAmount(), cny_exrate)));
		}
		poolDtl.setBusinessDate(payOrder.getConfirmorAt());// 记账日期
		poolDtl.setBillType(BaseConsts.FOUR); // 付款单
		poolDtl.setRemark("【付款单  付借款】金额:" + DecimalUtil.formatScale2(payOrder.getRealPayAmount()).toString());
		poolDtl.setSupplierId(payOrder.getPayer());// 供应商
		poolDtl.setDiffAmount(BigDecimal.ZERO);
		receiptPoolDtlDao.insertReceiptPool(poolDtl);
	}

	public List<FundPoolResDto> queryReceiptPoolFundResultsByEx(FundPoolReqDto poolReqDto) {
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
		if (pool != null) {
			poolReqDto.setCurrencyType(pool.getCurrencyType());// 币种类型
			poolReqDto.setId(null);
			poolReqDto.setBusinessUnitId(pool.getBusinessUnitId());// 经营单位
			List<ReceiptPoolFundDtl> poolFundDtl = poolFundDtlDao.selectPoolFundResultByCon(poolReqDto);
			if (CollectionUtils.isNotEmpty(poolFundDtl)) {
				for (ReceiptPoolFundDtl receiptPoolFundDtl : poolFundDtl) {
					FundPoolResDto fundPoolResDto = convertToFundPoolDao(receiptPoolFundDtl);
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
		poolReqDto.setCurrencyType(pool.getCurrencyType());// 币种
		poolReqDto.setId(null);
		int count = poolFundDtlDao.isOverasyncMaxLine(poolReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("融资池资金数据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncReceiptPoolFundApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/pool/receiptPoolFund_list");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_43);
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
	public Map<String, Object> asyncReceiptPoolFundApplyExport(FundPoolReqDto poolReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<FundPoolResDto> fundPoolResDtos = queryPoolFundResultByCon(poolReqDto).getItems();
		model.put("receiptPoolFundList", fundPoolResDtos);
		return model;
	}
}
