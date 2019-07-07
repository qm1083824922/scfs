package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.AccountPoolDao;
import com.scfs.dao.pay.AccountPoolFundDao;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.ReceiptPool;
import com.scfs.domain.pay.dto.req.AccountPoolFundReqDto;
import com.scfs.domain.pay.dto.req.AccountPoolReqDto;
import com.scfs.domain.pay.dto.resq.AccountPoolFundResDto;
import com.scfs.domain.pay.entity.AccountPool;
import com.scfs.domain.pay.entity.AccountPoolFund;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  资金明细信息
 *  File: AccountPoolFundService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年09月26日         Administrator
 *
 * </pre>
 */
@Service
public class AccountPoolFundService {
	@Autowired
	private AccountPoolFundDao accountPoolFundDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AccountPoolDao accountPoolDao;
	@Autowired
	private ReceiptPoolDao receiptPoolDao;

	/**
	 * 获取列表数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<AccountPoolFundResDto> queryAccountPoolResultsByCon(AccountPoolFundReqDto reqDto) {
		PageResult<AccountPoolFundResDto> pageResult = new PageResult<AccountPoolFundResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<AccountPoolFundResDto> result = convertToResDtos(
				accountPoolFundDao.queryAccountPoolResults(reqDto, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setItems(result);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 订单、费用类型付款确认处理相关业务
	 * 
	 * @param payModel
	 */
	public void dealPayOrder(PayOrder payModel) {
		Integer accountId = payModel.getPaymentAccount();
		Integer busiUnit = payModel.getBusiUnit();
		Integer currencyType = payModel.getRealCurrencyType();
		BaseAccount baseAccount = cacheService.getAccountById(accountId);
		if (baseAccount != null && baseAccount.getCapitalAccountType().equals(BaseConsts.TWO)) {// 账户为独立账户进行操作
			BigDecimal availableAmount = BigDecimal.ZERO; // 可用资金金额
			FundPoolReqDto poolReqDto = new FundPoolReqDto();
			poolReqDto.setBusinessUnitId(busiUnit);
			poolReqDto.setCurrencyType(currencyType);
			ReceiptPool receiptPool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);// 获取资金金额
			if (receiptPool != null) {
				availableAmount = receiptPool.getRemainFundAmount();
			}

			AccountPoolReqDto reqDto = new AccountPoolReqDto();
			reqDto.setAccountId(accountId);
			reqDto.setBusiUnit(busiUnit);
			reqDto.setCurrencyType(currencyType);
			AccountPool accountPool = accountPoolDao.queryEntityByParam(reqDto);// 是否存在资金池信息
			Integer poolId = null;
			if (accountPool != null) {// 存在则对账户余额进行处理
				poolId = accountPool.getId();
				BigDecimal accountBlance = DecimalUtil.subtract(accountPool.getAccountBalanceAmount(),
						payModel.getRealPayAmount(), payModel.getBankCharge());// -付款金额-银行手续费
				BigDecimal profitAmount = DecimalUtil.subtract(accountBlance, availableAmount);
				AccountPool upAccountPool = new AccountPool();
				upAccountPool.setId(poolId);
				upAccountPool.setAvailableAmount(availableAmount);
				upAccountPool.setAccountBalanceAmount(accountBlance);
				upAccountPool.setProfitAmount(profitAmount);
				accountPoolDao.update(upAccountPool);

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
				fund.setCreator(ServiceSupport.getUser() == null ? "" : ServiceSupport.getUser().getChineseName());
				fund.setCreatorId(
						ServiceSupport.getUser() == null ? BaseConsts.ZERO : ServiceSupport.getUser().getId());
				accountPoolFundDao.insert(fund);// 添加资金明细
			}
		}
	}

	/**
	 * 回款预收水单核完相关操作
	 * 
	 * @param recModel
	 */
	public void dealBankReceipt(BankReceipt recModel) {
		Integer accountId = recModel.getRecAccountNo();
		Integer busiUnit = recModel.getBusiUnit();
		Integer currencyType = recModel.getCurrencyType();
		BaseAccount baseAccount = cacheService.getAccountById(accountId);
		if (baseAccount != null && baseAccount.getCapitalAccountType().equals(BaseConsts.TWO)) {// 账户为独立账户进行操作
			BigDecimal availableAmount = BigDecimal.ZERO; // 可用资金金额
			FundPoolReqDto poolReqDto = new FundPoolReqDto();
			poolReqDto.setBusinessUnitId(busiUnit);
			poolReqDto.setCurrencyType(currencyType);
			ReceiptPool receiptPool = receiptPoolDao.quertReceiptPoolResultByCon(poolReqDto);// 获取资金金额
			if (receiptPool != null) {
				availableAmount = receiptPool.getRemainFundAmount();
			}

			AccountPoolReqDto reqDto = new AccountPoolReqDto();
			reqDto.setAccountId(accountId);
			reqDto.setBusiUnit(busiUnit);
			reqDto.setCurrencyType(currencyType);
			AccountPool accountPool = accountPoolDao.queryEntityByParam(reqDto);// 是否存在资金池信息
			Integer poolId = null;
			if (accountPool != null) {// 存在则对账户余额进行处理
				poolId = accountPool.getId();
				BigDecimal accountBlance = DecimalUtil.add(accountPool.getAccountBalanceAmount(),
						recModel.getActualReceiptAmount());// +水单金额
				BigDecimal profitAmount = DecimalUtil.subtract(accountBlance, availableAmount);
				AccountPool upAccountPool = new AccountPool();
				upAccountPool.setId(poolId);
				upAccountPool.setAvailableAmount(availableAmount);
				upAccountPool.setAccountBalanceAmount(accountBlance);
				upAccountPool.setProfitAmount(profitAmount);
				accountPoolDao.update(upAccountPool);
			} else { // 不存在则，添加资金池信息
				if (recModel.getReceiptType().equals(BaseConsts.FIVE)) {// 内部水单

					BigDecimal accountBalanceAmount = recModel.getActualReceiptAmount();
					BigDecimal profitAmount = DecimalUtil.subtract(accountBalanceAmount, availableAmount);
					// 添加头
					AccountPool insertPool = new AccountPool();
					insertPool.setAccountId(accountId);
					insertPool.setBusiUnit(busiUnit);
					insertPool.setCurrencyType(currencyType);
					insertPool.setAvailableAmount(availableAmount);
					insertPool.setAccountBalanceAmount(accountBalanceAmount);
					insertPool.setProfitAmount(profitAmount);
					insertPool.setCreateAt(new Date());
					insertPool.setCreator(
							ServiceSupport.getUser() == null ? "" : ServiceSupport.getUser().getChineseName());
					insertPool.setCreatorId(
							ServiceSupport.getUser() == null ? BaseConsts.ZERO : ServiceSupport.getUser().getId());
					accountPoolDao.insert(insertPool);
					poolId = insertPool.getId();
				}
			}
			if (poolId != null) {
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
				fund.setCreator(ServiceSupport.getUser() == null ? "" : ServiceSupport.getUser().getChineseName());
				fund.setCreatorId(
						ServiceSupport.getUser() == null ? BaseConsts.ZERO : ServiceSupport.getUser().getId());
				accountPoolFundDao.insert(fund);// 添加资金明细
			}
		}
	}

	public List<AccountPoolFundResDto> convertToResDtos(List<AccountPoolFund> result) {
		List<AccountPoolFundResDto> resDtoList = new ArrayList<AccountPoolFundResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtoList;
		}
		for (AccountPoolFund pool : result) {
			AccountPoolFundResDto resDto = convertResDto(pool);
			resDtoList.add(resDto);
		}
		return resDtoList;
	}

	public AccountPoolFundResDto convertResDto(AccountPoolFund model) {
		AccountPoolFundResDto result = new AccountPoolFundResDto();
		if (model != null) {
			result.setId(model.getId());
			result.setPoolId(model.getPoolId());
			result.setAccountId(model.getAccountId());
			result.setBusiUnit(model.getBusiUnit());
			result.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(model.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
			result.setProjectId(model.getProjectId());
			result.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
			result.setCustomerId(model.getCustomerId());
			result.setCustomerName(cacheService.getSubjectNcByIdAndKey(model.getCustomerId(), CacheKeyConsts.CUSTOMER));
			result.setSupplieId(model.getSupplieId());
			result.setSupplieName(cacheService.getSubjectNoNameById(model.getSupplieId()));
			result.setBillNo(model.getBillNo());
			result.setBillType(model.getBillType());
			result.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, model.getBillType() + ""));
			result.setCurrencyType(model.getCurrencyType());
			result.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					model.getCurrencyType() + ""));
			result.setBillDate(model.getBillDate());
			result.setBillAmount(model.getBillAmount());
			result.setBillChargeAmount(model.getBillChargeAmount());
			result.setBillThirdId(model.getBillThirdId());
			result.setRemark(model.getRemark());
		}
		return result;
	}

}
