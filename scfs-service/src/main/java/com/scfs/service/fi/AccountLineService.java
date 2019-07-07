package com.scfs.service.fi;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.AccountLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.fi.dto.req.AccountLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountLineResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 
 *  File: AccountLineServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月19日			Administrator
 *
 * </pre>
 */
@Service
public class AccountLineService {
	@Autowired
	AccountLineDao accountLineDao;

	@Autowired
	private AccountBookService accountBookService;
	@Autowired
	CacheService cacheService;

	public BaseResult createAccountLine(AccountLine accountLine) {
		BaseUser user = ServiceSupport.getUser();
		accountLine.setCreator(user.getChineseName());
		accountLine.setCreatorId(user.getId());
		accountLine.setState(BaseConsts.ONE); // 1:待提交、
		accountLine.setAccoutLineState(BaseConsts.ONE);
		accountLineDao.insert(accountLine);
		return new BaseResult();
	}

	public BaseResult updateAccountLineById(AccountLine accountLine) {
		BaseResult baseResult = new BaseResult();
		if (accountLineDao.updateById(accountLine) == 0) {
			baseResult.setMsg("更新科目失败，请重试");
		}
		return baseResult;
	}

	public BaseResult deleteAccountLineById(AccountLine accountLine) {
		AccountLine oldEntity = accountLineDao.queryEntityById(accountLine.getId());
		if (oldEntity.getIsDelete() == BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ALREADY_DELETE, accountLine.getId());
		}
		if (oldEntity.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.DELETE_ERROR, accountLine.getId());
		}
		BaseUser user = ServiceSupport.getUser();
		accountLine.setDeleteAt(new Date());
		accountLine.setIsDelete(BaseConsts.ONE);
		accountLine.setDeleter(user.getChineseName());
		accountLine.setDeleterId(user.getId());
		BaseResult baseResult = new BaseResult();

		if (accountLineDao.deleteById(accountLine) == 0) {
			baseResult.setMsg("删除科目失败，请重试");
		}
		return baseResult;
	}

	public Result<AccountLineResDto> detailAccountLineById(int id) {
		Result<AccountLineResDto> result = new Result<AccountLineResDto>();
		AccountLineResDto resDto = convertToRes(accountLineDao.queryEntityById(id));
		result.setItems(resDto);
		return result;
	}

	public Result<AccountLineResDto> editAccountLineById(int id) {
		Result<AccountLineResDto> result = new Result<AccountLineResDto>();
		AccountLineResDto resDto = convertToRes(accountLineDao.queryEntityById(id));
		result.setItems(resDto);
		return result;
	}

	public BaseResult submitAccountLineById(int id) {
		BaseResult baseResult = new BaseResult();
		AccountLine oldEntity = accountLineDao.queryEntityById(id);
		if (oldEntity.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ALREADY_SUBMIT, id);
		}
		if (accountLineDao.submitById(id, BaseConsts.TWO) == 0) {
			baseResult.setMsg("提交科目失败，请重试");
		}
		return baseResult;
	}

	public PageResult<AccountLineResDto> queryAccountLineByCond(AccountLineSearchReqDto dto) {
		PageResult<AccountLineResDto> pageResult = new PageResult<AccountLineResDto>();
		int offSet = PageUtil.getOffSet(dto.getPage(), dto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, dto.getPer_page());
		List<AccountLine> queryAccountLineList = accountLineDao.queryResultsByCond(dto, rowBounds);
		List<AccountLineResDto> accountLineResDtos = new ArrayList<AccountLineResDto>();
		for (AccountLine line : queryAccountLineList) {
			AccountLineResDto resDto = convertToRes(line);
			resDto.setOpertaList(getOperList(line.getState()));
			accountLineResDtos.add(resDto);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), dto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(dto.getPage());
		pageResult.setPer_page(dto.getPer_page());
		pageResult.setItems(accountLineResDtos);
		return pageResult;
	}

	public List<AccountLineResDto> queryListByCond(AccountLineSearchReqDto dto) {
		List<AccountLine> queryAccountLineList = accountLineDao.queryResultsByCond(dto);
		List<AccountLineResDto> accountLineResDtos = new ArrayList<AccountLineResDto>();
		for (AccountLine line : queryAccountLineList) {
			AccountLineResDto resDto = convertToRes(line);
			resDto.setOpertaList(getOperList(line.getState()));
			accountLineResDtos.add(resDto);
		}
		return accountLineResDtos;
	}

	private AccountLineResDto convertToRes(AccountLine line) {
		AccountLineResDto resDto = new AccountLineResDto();
		BeanUtils.copyProperties(line, resDto);
		resDto.setId(line.getId());
		resDto.setAccountLineLevelName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_LINE_LEVEL, resDto.getAccountLineLevel() + ""));
		resDto.setAccountLineTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_LINE_TYPE, resDto.getAccountLineType() + ""));
		resDto.setDebitOrCreditName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEBIT_OR_CREDIT, resDto.getDebitOrCredit() + ""));
		resDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_LINE_STATE, resDto.getState() + ""));
		resDto.setNeedAccountStr(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedAccount() + ""));
		resDto.setNeedCustStr(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedCust() + ""));
		resDto.setNeedProjectStr(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedProject() + ""));
		resDto.setNeedSupplierStr(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedSupplier() + ""));
		resDto.setNeedTaxRateStr(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedTaxRate() + ""));
		resDto.setNeedUserStr(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedUser() + ""));
		resDto.setNeedInnerBusiUnitStr(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getNeedInnerBusiUnit() + ""));
		resDto.setNeedDec(AccountLine.tranToNeedDec(resDto.getNeedProject(), resDto.getNeedSupplier(),
				resDto.getNeedCust(), resDto.getNeedAccount(), resDto.getNeedTaxRate(), resDto.getNeedUser(),
				resDto.getNeedInnerBusiUnit()));
		resDto.setIsLastLabel(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, resDto.getIsLast() + ""));
		return resDto;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				AccountLineResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		switch (state) {
		// 状态,1表示待提交，2表示已完成，3表示已锁定
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.THREE:
			break;
		}

		return opertaList;
	}

	/**
	 * 根据传入的Type区分查询的方式 type 1 根据经营单位 2 根据银行ID 3 直接查询
	 * 
	 * @param accountNo
	 * @param busiUnit
	 * @param bankId
	 * @param type
	 * @return
	 */
	public AccountLine queryAccountLine(String accountNo, Integer busiUnit, Integer bankId, Integer type,
			Integer realCurrType) {
		AccountLine accountLine = new AccountLine();
		if (type == BaseConsts.ONE) {
			accountLine = queryAccontLineByBusi(accountNo, busiUnit);
			return accountLine;
		}
		if (type == BaseConsts.TWO) {
			accountLine = queryAccountLineByAccountID(bankId, realCurrType);
			return accountLine;
		}
		if (type == BaseConsts.THREE) {
			accountLine = cacheService.getAccountLineByNo(accountNo, BaseConsts.ONE);
			return accountLine;
		}
		return accountLine;
	}

	/**
	 * 根据经营单位和科目前缀编号查询科目信息
	 * 
	 * @param accountNo
	 * @param busiUnit
	 * @return
	 */
	public AccountLine queryAccontLineByBusi(String accountNo, Integer busiUnit) {
		if (StringUtils.isEmpty(accountNo)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "科目前缀编号为空");
		}
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位为空");
		}
		// 经营单位查询本位币
		List<AccountBook> accountBooks = accountBookService.queryEntityByBusiUnit(busiUnit);
		if (CollectionUtils.isEmpty(accountBooks)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "帐套结构数据为空");
		}
		String currencyType = ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				accountBooks.get(BaseConsts.ZERO).getStandardCoin().toString());
		if (StringUtils.isEmpty(currencyType)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种为空");
		}
		// 根据科目前缀和名称进行模糊查询
		AccountLineSearchReqDto reqDto = new AccountLineSearchReqDto();
		reqDto.setAccountLineNo(accountNo);
		reqDto.setAccountLineName(currencyType);
		List<AccountLine> accountLines = accountLineDao.queryResultsByCond(reqDto);
		if (CollectionUtils.isEmpty(accountLines)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"科目编号:【" + accountNo + "】,币种:" + currencyType + ",查询的科目为空");
		}
		if (accountLines.size() > BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"科目编号:【" + accountNo + "】,币种:" + currencyType + ",查询的科目数量有误");
		}
		return accountLines.get(BaseConsts.ZERO);
	}

	/**
	 * 根据收款账号id查询科目信息
	 *
	 * @param accountId
	 * @return
	 */
	public AccountLine queryAccountLineByAccountID(Integer bankId, Integer realCurrType) {
		if (bankId == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "银行账户ID为空");
		}
		if (realCurrType == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "实际币种为空");
		}
		BaseAccount baseAccount = cacheService.getAccountById(bankId);
		if (baseAccount == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "银行账户信息为空");
		}
		String currencyType = ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				realCurrType.toString());
		if (StringUtils.isEmpty(currencyType)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种为空");
		}
		// 根据科目前缀和名称进行模糊查询
		AccountLineSearchReqDto reqDto = new AccountLineSearchReqDto();
		reqDto.setAccountLineName(currencyType + "%" + baseAccount.getAccountNo());
		List<AccountLine> accountLines = accountLineDao.queryResultsByCond(reqDto);
		if (CollectionUtils.isEmpty(accountLines)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"银行编号：" + baseAccount.getAccountNo() + "币种+" + currencyType + ",查询的科目为空");
		}
		if (accountLines.size() > BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"银行编号：" + baseAccount.getAccountNo() + "币种+" + currencyType + ",查询的科目数量有误");
		}
		return accountLines.get(BaseConsts.ZERO);
	}
}
