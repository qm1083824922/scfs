package com.scfs.service.base.subject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.subject.dto.req.AddAccountDto;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.base.subject.dto.resp.QueryAccountResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.common.exception.BaseException;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: BaseAccountServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Service
public class BaseAccountService {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseAccountService.class);

	@Autowired
	private BaseAccountDao baseAccountDao;

	@Autowired
	private CacheService cacheService;

	public BaseAccount loadAndLockEntityById(int id) {
		BaseAccount obj = baseAccountDao.loadAndLockEntityById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, baseAccountDao.getClass(), id);
		}
		return obj;
	}

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	public Result<QueryAccountResDto> editAccountById(int id) {
		Result<QueryAccountResDto> result = new Result<QueryAccountResDto>();
		QueryAccountResDto obj = converQueryAccountResDto(baseAccountDao.loadAndLockEntityById(id));
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, baseAccountDao.getClass(), id);
		}
		obj.setSubjectName(cacheService.getBaseSubjectById(obj.getSubjectId()).getChineseName());
		result.setItems(obj);
		return result;
	}

	public void addBaseAccount(AddAccountDto addAccountDto) {
		addAccountDto.setCreator(ServiceSupport.getUser().getChineseName());
		addAccountDto.setState(BaseConsts.ONE);
		int result = baseAccountDao.insertBaseAccount(addAccountDto);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(addAccountDto));
		}
	}

	public PageResult<QueryAccountResDto> queryAccountBySubjectId(QueryAccountReqDto queryAccountReqDto) {
		PageResult<QueryAccountResDto> pageResult = new PageResult<QueryAccountResDto>();
		try {
			int offSet = PageUtil.getOffSet(queryAccountReqDto.getPage(), queryAccountReqDto.getPer_page());
			RowBounds rowBounds = new RowBounds(offSet, queryAccountReqDto.getPer_page());
			List<QueryAccountResDto> queryAccountResDtos = convertToResult(
					baseAccountDao.queryAccountBySubjectId(queryAccountReqDto, rowBounds));
			pageResult.setItems(queryAccountResDtos);
			int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryAccountReqDto.getPer_page());
			pageResult.setLast_page(totalPage);
			pageResult.setTotal(CountHelper.getTotalRow());
			pageResult.setCurrent_page(queryAccountReqDto.getPage());
			pageResult.setPer_page(queryAccountReqDto.getPer_page());
		} catch (Exception e) {
			LOGGER.error("查询账户信息异常[{}]", JSONObject.toJSON(queryAccountReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	private List<QueryAccountResDto> convertToResult(List<BaseAccount> baseAccounts) {
		List<QueryAccountResDto> queryAccountResDtos = new ArrayList<QueryAccountResDto>();
		if (CollectionUtils.isEmpty(baseAccounts)) {
			return queryAccountResDtos;
		}
		for (BaseAccount baseAccount : baseAccounts) {
			QueryAccountResDto queryAccountResDto = converQueryAccountResDto(baseAccount);
			queryAccountResDtos.add(queryAccountResDto);
		}
		return queryAccountResDtos;
	}

	private QueryAccountResDto converQueryAccountResDto(BaseAccount baseAccount) {
		// 操作集合
		QueryAccountResDto queryAccountResDto = new QueryAccountResDto();
		queryAccountResDto.setAccountNo(baseAccount.getAccountNo());
		queryAccountResDto.setBankAddress(baseAccount.getBankAddress());
		queryAccountResDto.setBankCode(baseAccount.getBankCode());
		queryAccountResDto.setBankName(baseAccount.getBankName());
		queryAccountResDto.setPhoneNumber(baseAccount.getPhoneNumber());
		queryAccountResDto.setSubjectId(baseAccount.getSubjectId());
		queryAccountResDto.setId(baseAccount.getId());
		queryAccountResDto.setAccountor(baseAccount.getAccountor());
		queryAccountResDto.setIban(baseAccount.getIban());
		queryAccountResDto.setAccountType(baseAccount.getAccountType());
		queryAccountResDto.setAccountTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_TYPE, baseAccount.getAccountType() + ""));
		queryAccountResDto.setDefaultCurrency(baseAccount.getDefaultCurrency());
		queryAccountResDto.setDefaultCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				baseAccount.getDefaultCurrency() + ""));
		queryAccountResDto.setState(baseAccount.getState());
		queryAccountResDto.setStateLabel(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_STATE_TYPE, baseAccount.getState() + ""));
		queryAccountResDto.setBankSimple(baseAccount.getBankSimple());
		queryAccountResDto.setCapitalAccountType(baseAccount.getCapitalAccountType());
		if (baseAccount.getCapitalAccountType() != null) {
			queryAccountResDto.setCapitalAccountName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.CAPITAL_ACCOUNT_TYPE, baseAccount.getCapitalAccountType() + ""));
		}
		return queryAccountResDto;

	}

	public void updateBaseAccount(BaseAccount baseAccount) {

		int result = baseAccountDao.updateBaseAccountById(baseAccount);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(baseAccount));
		}
	}

	public void invalidBaseAccountByIds(List<Integer> ids) {
		for (Integer id : ids) {
			invalidBaseAccountById(id);
		}
	}

	public void invalidBaseAccountById(Integer id) {
		BaseAccount baseAccount = loadAndLockEntityById(id);
		baseAccount.setDeleteAt(new Date());
		baseAccount.setDeleter(ServiceSupport.getUser().getChineseName());
		baseAccount.setState(BaseConsts.TWO);
		int result = baseAccountDao.invalidAccountById(baseAccount);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "作废失败:" + JSONObject.toJSON(baseAccount));
		}
	}

}
