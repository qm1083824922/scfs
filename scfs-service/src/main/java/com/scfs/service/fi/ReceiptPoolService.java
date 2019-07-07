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
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.fi.ReceiptPoolDtlDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.resp.FundPoolResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.ReceiptPool;
import com.scfs.domain.fi.entity.ReceiptPoolDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ReceiptPoolService.java
 *  Description:  资金池服务类
 *  TODO
 *  Date,					Who,				
 *  2017年06月09日			Administrator
 *
 * </pre>
 */
@Service
public class ReceiptPoolService {

	@Autowired
	private ReceiptPoolDao receiptPoolDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	BankReceiptDao bankReceiptDao;
	@Autowired
	private ReceiptPoolDtlDao receiptPoolDtlDao;
	@Autowired
	private BaseExchangeRateService rateService;
	@Autowired
	private ReceiptFundPoolService fundPoolService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 分页查询当前资金池数据
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public PageResult<FundPoolResDto> queryRecPoolResultByCon(FundPoolReqDto poolReqDto) throws Exception {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		int offSet = PageUtil.getOffSet(poolReqDto.getPage(), poolReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poolReqDto.getPer_page());
		poolReqDto.setUserId(ServiceSupport.getUser().getId());
		List<ReceiptPool> receiptPools = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto, rowBounds);
		List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
		if (CollectionUtils.isNotEmpty(receiptPools)) {
			for (ReceiptPool receiptPool : receiptPools) {
				FundPoolResDto fundPoolResDto = convertToResDto(receiptPool);
				dtos.add(fundPoolResDto);
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poolReqDto.getPer_page());
		pageResult.setItems(dtos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(poolReqDto.getPage());
		pageResult.setPer_page(poolReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 根据资金ID查询资金池的基本信息
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public Result<FundPoolResDto> queryReceiptPoolResultByid(FundPoolReqDto poolReqDto) {
		Result<FundPoolResDto> result = new Result<FundPoolResDto>();
		if (poolReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金池查询信息为空");
		}
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		result.setItems(convertToResDto(pool));
		return result;
	}

	/**
	 * 封装当前资金池的信息列表
	 * 
	 * @param receiptPool
	 * @return
	 */
	public FundPoolResDto convertToResDto(ReceiptPool receiptPool) {
		FundPoolResDto poolResDto = new FundPoolResDto();
		poolResDto.setId(receiptPool.getId());// 资金池ID
		poolResDto.setCountRecriptAmount(DecimalUtil.formatScale2(receiptPool.getCountFundAmount()));// 资金额度
		poolResDto.setBusinessUnitId(receiptPool.getBusinessUnitId());// 经营单位ID
		poolResDto.setBusinessUnitName(
				cacheService.getSubjectNcByIdAndKey(receiptPool.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));// 经营单位名称
		poolResDto.setCurrencyType(receiptPool.getCurrencyType());// 币种类型
		poolResDto.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				receiptPool.getCurrencyType() + ""));// 币种名称
		poolResDto.setUsedFundAmount(DecimalUtil.formatScale2(receiptPool.getUsedFundAmount()));// 已使用资金额度
		poolResDto.setRemainFundAmount(DecimalUtil.formatScale2(receiptPool.getRemainFundAmount()));// 资金余额
		poolResDto.setRemainAssetAmount(DecimalUtil.formatScale2(receiptPool.getRemainAssetAmount()));// 资产总额
		// 取中国银行的汇率
		BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", BaseConsts.ONE + "",
				receiptPool.getCurrencyType() + "", new Date()); // 根据日期算出汇率
		if (receiptPool.getCurrencyType() == BaseConsts.ONE) {
			poolResDto.setCountRecriptAmountCny(DecimalUtil.formatScale2(receiptPool.getCountFundAmount()));// 资金额度（CNY）
			poolResDto.setUsedFundAmountCny(DecimalUtil.formatScale2(receiptPool.getUsedFundAmount()));// 已使用资金额度（CNY）
			poolResDto.setRemainFundAmountCny(DecimalUtil.formatScale2(receiptPool.getRemainFundAmount()));// 资金余额（CNY）
			poolResDto.setRemainAssetAmountCny(DecimalUtil.formatScale2(receiptPool.getRemainAssetAmount()));// 资产总额
		} else {
			poolResDto.setCountRecriptAmountCny(
					DecimalUtil.formatScale2(DecimalUtil.multiply(receiptPool.getCountFundAmount(), cny_exrate)));// 资金额度（CNY）
			poolResDto.setUsedFundAmountCny(
					DecimalUtil.formatScale2(DecimalUtil.multiply(receiptPool.getUsedFundAmount(), cny_exrate)));// 已使用资金额度（CNY）
			poolResDto.setRemainFundAmountCny(
					DecimalUtil.formatScale2(DecimalUtil.multiply(receiptPool.getRemainFundAmount(), cny_exrate)));// 资金余额（CNY）
			poolResDto.setRemainAssetAmountCny(
					DecimalUtil.formatScale2(DecimalUtil.multiply(receiptPool.getRemainAssetAmount(), cny_exrate)));// 资产总额
		}
		poolResDto.setAdvancePayAmount(DecimalUtil.formatScale2(receiptPool.getAdvancePayAmount()));// 预付款金额
		poolResDto.setPaymentAmount(DecimalUtil.formatScale2(receiptPool.getPaymentAmount()));
		poolResDto.setStlAmount(DecimalUtil.formatScale2(receiptPool.getStlAmount()));
		poolResDto.setRecAmount(DecimalUtil.formatScale2(receiptPool.getRecAmount()));
		poolResDto.setCreateAt(receiptPool.getCreateAt());// 创建时间
		poolResDto.setOpertaList(getOperList());
		return poolResDto;
	}

	/**
	 * 根据状态获取操作列表
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
				FundPoolResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 查询当前资金明细信息
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public PageResult<FundPoolResDto> queryFundPoolDtlResultByCon(FundPoolReqDto poolReqDto) {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		// 根据资金id查询资金池信息
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		if (pool != null) {
			poolReqDto.setBusinessUnitId(pool.getBusinessUnitId()); // 经营单位
			poolReqDto.setCurrencyType(pool.getCurrencyType());// 币种
			poolReqDto.setId(null);
			// 根据经营单位和币种查询资金明细
			int offSet = PageUtil.getOffSet(poolReqDto.getPage(), poolReqDto.getPer_page());
			RowBounds rowBounds = new RowBounds(offSet, poolReqDto.getPer_page());
			poolReqDto.setUserId(ServiceSupport.getUser().getId());
			List<ReceiptPoolDtl> poolDtls = receiptPoolDtlDao.queryFundResults(poolReqDto, rowBounds);
			List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
			if (CollectionUtils.isNotEmpty(poolDtls)) {
				BigDecimal billAmount = BigDecimal.ZERO;
				BigDecimal diffAmount = BigDecimal.ZERO;
				for (ReceiptPoolDtl receiptPoolDtl : poolDtls) {
					FundPoolResDto fundPoolResDto = convertToFundPoolDtlDao(receiptPoolDtl);
					dtos.add(fundPoolResDto);
					billAmount = DecimalUtil.add(receiptPoolDtl.getBillAmount(), billAmount);
					diffAmount = DecimalUtil.add(receiptPoolDtl.getDiffAmount(), diffAmount);
				}
				String totalStr = "单据金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(billAmount))
						+ " &nbsp; 尾差  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(diffAmount))
						+ " &nbsp;";
				pageResult.setTotalStr(totalStr);
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
	 * 封装当前资金明细信息
	 * 
	 * @param receiptPoolDtl
	 * @return
	 */
	public FundPoolResDto convertToFundPoolDtlDao(ReceiptPoolDtl receiptPoolDtl) {
		FundPoolResDto fundPoolResDto = new FundPoolResDto();
		fundPoolResDto.setId(receiptPoolDtl.getId());// 主键ID
		fundPoolResDto.setBusinessUnitId(receiptPoolDtl.getBusinessUnitId());// 经营单位ID
		fundPoolResDto.setBusinessUnitName(
				cacheService.getSubjectNcByIdAndKey(receiptPoolDtl.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));// 经营单位名称
		fundPoolResDto.setProject(receiptPoolDtl.getProjectId());
		fundPoolResDto.setProjectName(cacheService.getProjectNameById(receiptPoolDtl.getProjectId()));
		fundPoolResDto.setCurrencyType(receiptPoolDtl.getCurrencyType());// 币种类型
		fundPoolResDto.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				receiptPoolDtl.getCurrencyType() + ""));// 币种名称
		fundPoolResDto.setBillAmount(DecimalUtil.formatScale2(receiptPoolDtl.getBillAmount()));// 单据金额
		fundPoolResDto.setExchangeRate(receiptPoolDtl.getExchangeRate());// 汇率
		fundPoolResDto.setBillAmountCny(DecimalUtil.formatScale2(receiptPoolDtl.getBillAmountCny()));// 人民币金额
		fundPoolResDto.setBusinessDate(receiptPoolDtl.getBusinessDate());// 记账日期
		fundPoolResDto.setCreateAt(receiptPoolDtl.getCreateAt());// 创建日期
		fundPoolResDto.setRemark(receiptPoolDtl.getRemark());
		fundPoolResDto.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (receiptPoolDtl.getBillType()) + ""));
		fundPoolResDto.setBillNo(receiptPoolDtl.getBillNo());// 单据编号
		fundPoolResDto.setCustomerName(cacheService.getSubjectNoNameById(receiptPoolDtl.getCustomerId()));// 客户名称
		fundPoolResDto.setSupplieName(cacheService.getSubjectNoNameById(receiptPoolDtl.getSupplierId()));
		fundPoolResDto.setDiffAmount(
				receiptPoolDtl.getDiffAmount() == null ? BigDecimal.ZERO : receiptPoolDtl.getDiffAmount());// 尾差
		return fundPoolResDto;
	}

	/**
	 * 新增资金池头信息
	 * 
	 * @param poolReqDto
	 */
	public void createReceiptPoolByCon(FundPoolReqDto poolReqDto) {
		// 根据传入的ID进行水单查询
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(poolReqDto.getId());
		fundPoolService.checkReceipt(bankReceipt);
		// 根据当前水单状态币种和经营单位查询资金池信息
		poolReqDto.setCurrencyType(bankReceipt.getActualCurrencyType());// 实际币种
		poolReqDto.setBusinessUnitId(bankReceipt.getBusiUnit());// 经营单位
		poolReqDto.setId(null);
		ReceiptPool pool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);
		// 取中国银行的汇率 转换为人民币的汇率
		BigDecimal cny_exrate = rateService.convertCurrency(BaseConsts.TWO + "", BaseConsts.ONE + "",
				bankReceipt.getActualCurrencyType() + "", bankReceipt.getReceiptDate()); // 根据日期算出汇率

		// 水单金额和 汇率为空的赋值操作
		BigDecimal receiptAmount = (null == bankReceipt.getReceiptAmount() ? BigDecimal.ZERO
				: bankReceipt.getReceiptAmount());
		cny_exrate = null == cny_exrate ? BigDecimal.ZERO : cny_exrate;

		if (pool == null) {// 新增头信息
			pool = this.convertReceiptPool(bankReceipt, cny_exrate);
		} else {
			pool.setCountFundAmount(DecimalUtil.add(
					null == pool.getCountFundAmount() ? BigDecimal.ZERO : pool.getCountFundAmount(), receiptAmount));// 资金额度
			pool.setRemainFundAmount(DecimalUtil.add(
					null == pool.getRemainFundAmount() ? BigDecimal.ZERO : pool.getRemainFundAmount(), receiptAmount));// 资金余额
			if (bankReceipt.getActualCurrencyType() == BaseConsts.ONE) {// 实际支付币种为人民币
				pool.setCountFundAmountCny(
						DecimalUtil.add(null == pool.getCountFundAmount() ? BigDecimal.ZERO : pool.getCountFundAmount(),
								receiptAmount));// 资金额度（CNY）
				pool.setRemainFundAmountCny(DecimalUtil.add(
						null == pool.getRemainFundAmountCny() ? BigDecimal.ZERO : pool.getRemainFundAmountCny(),
						receiptAmount));// 资金余额（CNY）
			} else {
				pool.setCountFundAmountCny(DecimalUtil.add(DecimalUtil.multiply(receiptAmount, cny_exrate),
						null == pool.getCountFundAmount() ? BigDecimal.ZERO : pool.getCountFundAmount()));// 资金额度（CNY）
				pool.setRemainFundAmountCny(DecimalUtil.add(
						null == pool.getRemainFundAmountCny() ? BigDecimal.ZERO : pool.getRemainFundAmountCny(),
						DecimalUtil.multiply(receiptAmount, cny_exrate)));// 资金余额（CNY）
			}
		}
		// 新增或者是更新资金池
		this.updateReceitPool(pool);
		// 新增资金池明细表
		this.createPoolDtl(bankReceipt, cny_exrate);
	}

	/**
	 * 新增资金池明细表数据
	 * 
	 * @param bankReceipt
	 */
	public void createPoolDtl(BankReceipt bankReceipt, BigDecimal cny_exrate) {
		ReceiptPoolDtl poolDtl = new ReceiptPoolDtl();
		poolDtl.setReceiptId(bankReceipt.getId());// 水单ID
		poolDtl.setCurrencyType(bankReceipt.getActualCurrencyType());// 币种
		poolDtl.setBusinessUnitId(bankReceipt.getBusiUnit());// 经营单位
		poolDtl.setBillAmount(
				null == bankReceipt.getActualReceiptAmount() ? BigDecimal.ZERO : bankReceipt.getActualReceiptAmount());// 水单金额
		poolDtl.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		poolDtl.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		poolDtl.setCreateAt(new Date());// 创建时间
		poolDtl.setProjectId(bankReceipt.getProjectId());// 项目ID
		poolDtl.setExchangeRate(cny_exrate);// 汇率
		poolDtl.setBillAmountCny(DecimalUtil.multiply(
				null == bankReceipt.getActualReceiptAmount() ? BigDecimal.ZERO : bankReceipt.getActualReceiptAmount(),
				cny_exrate));// 转换为人民币后的金额
		poolDtl.setBusinessDate(bankReceipt.getReceiptDate());// 记账日期
		poolDtl.setRemark("【水单 内部】金额:" + DecimalUtil.formatScale2(bankReceipt.getActualReceiptAmount()).toString());
		poolDtl.setBillType(BaseConsts.SEVEN);// 水单类型
		poolDtl.setBillNo(bankReceipt.getReceiptNo());
		poolDtl.setPayId(bankReceipt.getId());
		poolDtl.setCustomerId(bankReceipt.getCustId());// 客户ID
		poolDtl.setDiffAmount(
				bankReceipt.getActualDiffAmount() == null ? BigDecimal.ZERO : bankReceipt.getActualDiffAmount());
		receiptPoolDtlDao.insertReceiptPool(poolDtl);
	}

	/**
	 * 新增或者是更新资金池数据
	 * 
	 * @param pool
	 */
	public BaseResult updateReceitPool(ReceiptPool pool) {
		BaseResult baseResult = new BaseResult();
		int result = BaseConsts.ZERO;
		if (pool.getId() == null) {// 新增的操作
			result = receiptPoolDao.insertSelective(pool);
		} else {
			result = receiptPoolDao.updateByPrimaryKeySelective(pool);
		}
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("跟新资金池失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 封装资金信息
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public ReceiptPool convertReceiptPool(BankReceipt bankReceipt, BigDecimal cny_exrate) {
		ReceiptPool receiptPool = new ReceiptPool();
		BigDecimal receiptAmount = null == bankReceipt.getReceiptAmount() ? BigDecimal.ZERO
				: bankReceipt.getReceiptAmount();
		receiptPool.setCountFundAmount(receiptAmount);// 资金额度
		receiptPool.setBusinessUnitId(bankReceipt.getBusiUnit());// 经营单位
		receiptPool.setCurrencyType(bankReceipt.getActualCurrencyType());// 水单实际支付币种
		receiptPool.setRemainFundAmount(receiptAmount);// 资金余额
		if (bankReceipt.getActualCurrencyType() == BaseConsts.ONE) {// 人民币币值
			receiptPool.setCountFundAmountCny(receiptAmount);// 资金额度（CNY）
			receiptPool.setRemainFundAmountCny(receiptAmount);// 资金余额（CNY）
		} else {
			receiptPool.setCountFundAmountCny(DecimalUtil.multiply(receiptAmount, cny_exrate));// 资金额度（CNY）
			receiptPool.setRemainFundAmountCny(DecimalUtil.multiply(receiptAmount, cny_exrate));// 资金余额（CNY）
		}
		receiptPool.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		receiptPool.setCreatorId(ServiceSupport.getUser().getId());// 创建人ID
		receiptPool.setCreateAt(new Date());// 创建时间
		return receiptPool;
	}

	/**
	 * 查询融资池明细所有数据
	 * 
	 * @param poolReqDto
	 * @return
	 */
	public List<FundPoolResDto> queryReceiptPoolDtltResultsByEx(FundPoolReqDto poolReqDto) {
		// 根据资金id查询资金池信息
		List<FundPoolResDto> dtos = new ArrayList<FundPoolResDto>();
		ReceiptPool pool = receiptPoolDao.selectByPrimaryKey(poolReqDto.getId());
		if (pool != null) {
			poolReqDto.setBusinessUnitId(pool.getBusinessUnitId()); // 经营单位
			poolReqDto.setCurrencyType(pool.getCurrencyType());// 币种
			poolReqDto.setId(null);
			poolReqDto.setUserId(ServiceSupport.getUser().getId());
			List<ReceiptPoolDtl> poolDtls = receiptPoolDtlDao.queryFundResults(poolReqDto);
			if (CollectionUtils.isNotEmpty(poolDtls)) {
				for (ReceiptPoolDtl receiptPoolDtl : poolDtls) {
					FundPoolResDto fundPoolResDto = convertToFundPoolDtlDao(receiptPoolDtl);
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
		int count = receiptPoolDtlDao.isOverasyncMaxLine(poolReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("融资池明细数据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncReceiptPoolDtlApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/pool/receiptPoolDtl_list");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_41);
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
	public Map<String, Object> asyncReceiptPoolDtlApplyExport(FundPoolReqDto poolReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<FundPoolResDto> fundPoolResDtos = queryFundPoolDtlResultByCon(poolReqDto).getItems();
		model.put("receiptPoolDtlList", fundPoolResDtos);
		return model;
	}
}
