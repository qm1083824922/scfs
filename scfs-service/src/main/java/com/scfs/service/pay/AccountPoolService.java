package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.AccountPoolDao;
import com.scfs.dao.pay.AccountPoolFundDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.ReceiptPool;
import com.scfs.domain.pay.dto.req.AccountPoolReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.resq.AccountPoolResDto;
import com.scfs.domain.pay.entity.AccountPool;
import com.scfs.domain.pay.entity.AccountPoolFund;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *  资金池
 *  File: AccountPoolService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年09月25日         Administrator
 *
 * </pre>
 */
@Service
public class AccountPoolService {
	@Autowired
	private AccountPoolDao accountPoolDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ReceiptPoolDao receiptPoolDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private AccountPoolFundDao accountPoolFundDao;

	/**
	 * 获取列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<AccountPoolResDto> queryAccountPoolResultsByCon(AccountPoolReqDto reqDto) {
		PageResult<AccountPoolResDto> pageResult = new PageResult<AccountPoolResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		reqDto.setUserId(ServiceSupport.getUser().getId());
		List<AccountPoolResDto> result = convertToResDtos(accountPoolDao.queryAccountPoolResults(reqDto, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setItems(result);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 浏览
	 * 
	 * @param reqDto
	 * @return
	 */
	public Result<AccountPoolResDto> detailAccountPool(AccountPool account) {
		Result<AccountPoolResDto> result = new Result<AccountPoolResDto>();
		result.setItems(convertResDto(accountPoolDao.queryEntityById(account.getId())));
		return result;
	}

	public List<AccountPoolResDto> convertToResDtos(List<AccountPool> result) {
		List<AccountPoolResDto> resDtoList = new ArrayList<AccountPoolResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtoList;
		}
		for (AccountPool pool : result) {
			AccountPoolResDto resDto = convertResDto(pool);
			List<CodeValue> operList = getOperList();
			resDto.setOpertaList(operList);
			resDtoList.add(resDto);
		}
		return resDtoList;
	}

	/**
	 * 初始化数据
	 */
	public void refreshAccountPool() {
		AccountPoolReqDto accountReq = new AccountPoolReqDto();
		List<AccountPool> accountList = accountPoolDao.queryAccountPoolResults(accountReq);
		if (!CollectionUtils.isEmpty(accountList)) {// 判断是否存在数据
			for (AccountPool accountPool : accountList) {
				accountPoolDao.delete(accountPool.getId());
				accountPoolFundDao.deleteByPoolId(accountPool.getId());
			}
		}
		AccountPoolReqDto poolreqDto = new AccountPoolReqDto();
		List<AccountPool> poolList = accountPoolDao.queryAccountPool(poolreqDto);// 获取所有内部水单相关数据
		if (!CollectionUtils.isEmpty(poolList)) {
			for (AccountPool model : poolList) {
				Integer accountId = model.getAccountId();
				Integer busiUnit = model.getBusiUnit();
				Integer currencyType = model.getCurrencyType();
				Integer poolId = insertAccountPoolTitle(model);

				PayOrderSearchReqDto payOrderReqDto = new PayOrderSearchReqDto();
				List<Integer> payTypes = new ArrayList<Integer>();
				payTypes.add(BaseConsts.ONE);
				payTypes.add(BaseConsts.TWO);
				payOrderReqDto.setBusiUnit(busiUnit);
				payOrderReqDto.setPaymentAccount(accountId);
				payOrderReqDto.setRealCurrencyType(currencyType);
				payOrderReqDto.setState(BaseConsts.SIX);
				payOrderReqDto.setPayTypeList(payTypes);
				List<PayOrder> payOrderList = payOrderDao.queryResultsByCon(payOrderReqDto);// 订单和费用类型付款进资金明细
				if (!CollectionUtils.isEmpty(payOrderList)) {
					for (PayOrder payModel : payOrderList) {
						AccountPoolFund fund = new AccountPoolFund();
						fund.setPoolId(poolId);
						fund.setAccountId(payModel.getPaymentAccount());
						fund.setBusiUnit(payModel.getBusiUnit());
						fund.setProjectId(payModel.getProjectId());
						fund.setCustomerId(payModel.getPayee());
						fund.setBillNo(payModel.getPayNo());
						fund.setBillType(BaseConsts.FOUR);
						fund.setCurrencyType(payModel.getRealCurrencyType());
						fund.setBillDate(payModel.getConfirmorAt());
						fund.setBillAmount(payModel.getRealPayAmount());
						fund.setBillChargeAmount(payModel.getBankCharge());
						fund.setBillThirdId(payModel.getId());
						fund.setRemark(payModel.getRemark());
						fund.setCreateAt(new Date());
						fund.setCreator(
								ServiceSupport.getUser() == null ? "" : ServiceSupport.getUser().getChineseName());
						fund.setCreatorId(
								ServiceSupport.getUser() == null ? BaseConsts.ZERO : ServiceSupport.getUser().getId());
						accountPoolFundDao.insert(fund);
					}
				}

				BankReceiptSearchReqDto bankReqDto = new BankReceiptSearchReqDto();
				List<Integer> receipt = new ArrayList<Integer>();
				receipt.add(BaseConsts.ONE);
				receipt.add(BaseConsts.TWO);
				receipt.add(BaseConsts.THREE);
				receipt.add(BaseConsts.FIVE);
				bankReqDto.setReceiptTypeList(receipt);
				bankReqDto.setState(BaseConsts.THREE);
				bankReqDto.setBusiUnit(busiUnit);
				bankReqDto.setRecAccountNo(accountId);
				bankReqDto.setActualCurrencyType(currencyType);
				List<BankReceipt> bankList = bankReceiptDao.queryResultsByCon(bankReqDto);// 获取回款、预收定金、预收货款水单进资金明细
				if (!CollectionUtils.isEmpty(bankList)) {
					for (BankReceipt recModel : bankList) {
						AccountPoolFund fund = new AccountPoolFund();
						fund.setPoolId(poolId);
						fund.setAccountId(recModel.getRecAccountNo());
						fund.setBusiUnit(recModel.getBusiUnit());
						fund.setProjectId(recModel.getProjectId());
						fund.setCustomerId(recModel.getCustId());
						fund.setBillNo(recModel.getReceiptNo());
						fund.setBillType(BaseConsts.SEVEN);
						fund.setCurrencyType(recModel.getActualCurrencyType());
						fund.setBillDate(recModel.getReceiptDate());
						fund.setBillAmount(recModel.getActualReceiptAmount());
						fund.setBillChargeAmount(recModel.getActualDiffAmount());
						fund.setBillThirdId(recModel.getId());
						fund.setCreateAt(new Date());
						fund.setCreator(
								ServiceSupport.getUser() == null ? "" : ServiceSupport.getUser().getChineseName());
						fund.setCreatorId(
								ServiceSupport.getUser() == null ? BaseConsts.ZERO : ServiceSupport.getUser().getId());
						accountPoolFundDao.insert(fund);
					}
				}
			}
		}
	}

	/***
	 * 添加资金池信息相关操作
	 * 
	 * @param model
	 * @return
	 */
	public Integer insertAccountPoolTitle(AccountPool model) {
		AccountPool insertPool = new AccountPool();
		Integer accountId = model.getAccountId();
		Integer busiUnit = model.getBusiUnit();
		Integer currencyType = model.getCurrencyType();
		BigDecimal insideReceiptAmount = model.getReceiptAmount();// 内部水单金额
		BigDecimal availableAmount = BigDecimal.ZERO; // 可用资金金额
		BigDecimal realPayAmount = BigDecimal.ZERO; // （订单和费用类型）实际付款金额
		BigDecimal bankCharge = BigDecimal.ZERO; // （订单和费用类型）付款手续费
		BigDecimal receiptAmount = BigDecimal.ZERO; // （回款、预收）类型水单金额
		FundPoolReqDto poolReqDto = new FundPoolReqDto();
		poolReqDto.setBusinessUnitId(busiUnit);
		poolReqDto.setCurrencyType(currencyType);
		ReceiptPool receiptPool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);// 获取资金金额
		if (receiptPool != null) {
			availableAmount = receiptPool.getRemainFundAmount();
		}
		PayOrderSearchReqDto payReqDto = new PayOrderSearchReqDto();
		payReqDto.setBusiUnit(busiUnit);
		payReqDto.setPaymentAccount(accountId);
		payReqDto.setRealCurrencyType(currencyType);
		PayOrder payOrder = accountPoolDao.queryAmount(payReqDto);// 获取（订单和费用类型）实际付款金额,付款手续费
		if (payOrder != null) {
			realPayAmount = payOrder.getRealPayAmount();
			bankCharge = payOrder.getBankCharge();
		}
		AccountPoolReqDto reqDto = new AccountPoolReqDto();
		List<Integer> paramList = new ArrayList<Integer>();
		paramList.add(BaseConsts.ONE);
		paramList.add(BaseConsts.TWO);
		paramList.add(BaseConsts.THREE);
		reqDto.setParamList(paramList);
		reqDto.setAccountId(accountId);
		reqDto.setBusiUnit(busiUnit);
		reqDto.setCurrencyType(currencyType);
		BigDecimal receipt = accountPoolDao.queryReceiptAmount(reqDto);// 获取回款、预收定金、预收货款水单金额
		if (receipt != null) {
			receiptAmount = receipt;
		}
		BigDecimal accountBalanceAmount = DecimalUtil
				.add(DecimalUtil.subtract(insideReceiptAmount, realPayAmount, bankCharge), receiptAmount);
		BigDecimal profitAmount = DecimalUtil.subtract(accountBalanceAmount, availableAmount);

		// 添加头
		insertPool.setAccountId(accountId);
		insertPool.setBusiUnit(busiUnit);
		insertPool.setCurrencyType(currencyType);
		insertPool.setAvailableAmount(availableAmount);
		insertPool.setAccountBalanceAmount(accountBalanceAmount);
		insertPool.setProfitAmount(profitAmount);
		insertPool.setCreateAt(new Date());
		insertPool.setCreator(ServiceSupport.getUser() == null ? "" : ServiceSupport.getUser().getChineseName());
		insertPool.setCreatorId(ServiceSupport.getUser() == null ? BaseConsts.ZERO : ServiceSupport.getUser().getId());
		accountPoolDao.insert(insertPool);
		return insertPool.getId();
	}

	public AccountPoolResDto convertResDto(AccountPool model) {
		AccountPoolResDto result = new AccountPoolResDto();
		if (model != null) {
			result.setId(model.getId());
			result.setAccountId(model.getAccountId());
			BaseAccount baseAccount = cacheService.getAccountById(model.getAccountId());
			if (baseAccount != null) {
				result.setAccountNo(baseAccount.getAccountNo());
			}
			result.setBusiUnit(model.getBusiUnit());
			result.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(model.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
			result.setCurrencyType(model.getCurrencyType());
			result.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					model.getCurrencyType() + ""));
			result.setAvailableAmount(model.getAvailableAmount());
			result.setAccountBalanceAmount(model.getAccountBalanceAmount());
			result.setProfitAmount(model.getProfitAmount());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
		}
		return result;
	}

	/**
	 * 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = setOperList();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				AccountPoolResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> setOperList() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DETAIL);
		return opertaList;
	}
}
