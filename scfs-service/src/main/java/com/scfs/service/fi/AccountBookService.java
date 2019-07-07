package com.scfs.service.fi;

import com.scfs.common.consts.*;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.fi.AccountBookDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.fi.dto.req.AccountBookSearchReqDto;
import com.scfs.domain.fi.dto.req.AccountLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountBookResDto;
import com.scfs.domain.fi.dto.resp.AccountLineResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;
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
 *  File: AccountBookServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月19日			Administrator
 *
 * </pre>
 */
@Service
public class AccountBookService {
	@Autowired
	AccountBookDao accountBookDao;

	@Autowired
	SequenceService sequenceService;

	@Autowired
	BaseSubjectDao baseSubjectDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	AccountLineService accountLineService;

	public Result<Integer> createAccountBook(AccountBook accountBook) {
		BaseUser user = ServiceSupport.getUser();
		BaseSubject busiUnit = cacheService.getBusiUnitById(accountBook.getBusiUnit());
		String accountBookNo = sequenceService.getNumIncByBusName(BaseConsts.ZT_NO_PREFIX + busiUnit.getSubjectNo(),
				SeqConsts.S_ACCOUNT_BOOK_NO, BaseConsts.INT_11);
		accountBook.setAccountBookNo(accountBookNo);
		accountBook.setState(BaseConsts.ONE);
		accountBook.setCreator(user.getChineseName());
		accountBook.setCreatorId(user.getId());
		accountBook.setAuditor(cacheService.getUserChineseNameByid(accountBook.getAuditorId()));
		Result<Integer> result = new Result<Integer>();
		accountBookDao.insert(accountBook);
		result.setItems(accountBook.getId());
		return result;
	}

	public BaseResult updateAccountBookById(AccountBook accountBook) {
		BaseResult baseResult = new BaseResult();
		if (accountBook.getAuditorId() != null) {
			accountBook.setAuditor(cacheService.getUserChineseNameByid(accountBook.getAuditorId()));
		}
		int result = accountBookDao.updateById(accountBook);
		if (result == 0) {
			baseResult.setMsg("更新帐套失败，请重试");
		}
		return baseResult;
	}

	public BaseResult submitAccountBookById(int id) {
		// 加锁
		AccountBook oldEntity = accountBookDao.queryEntityById(id);
		// 只有待提交状态才能提交
		if (oldEntity.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ALREADY_SUBMIT, id);
		}
		AccountBook accountBook = new AccountBook();
		accountBook.setState(BaseConsts.TWO);
		accountBook.setId(id);
		BaseResult baseResult = new BaseResult();
		int result = accountBookDao.submitById(accountBook);
		if (result == 0) {
			baseResult.setMsg("提交帐套失败，请重试");
		}
		return baseResult;
	}

	public BaseResult deleteAccountBookById(int id) {
		BaseUser user = ServiceSupport.getUser();
		// 加锁
		AccountBook oldEntity = accountBookDao.queryEntityById(id);
		if (BaseConsts.ONE == oldEntity.getIsDelete()) {
			throw new BaseException(ExcMsgEnum.ALREADY_DELETE, id);
		}
		if (oldEntity.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.DELETE_ERROR, id);
		}
		BaseResult baseResult = new BaseResult();
		AccountBook accountBook = new AccountBook();
		accountBook.setId(id);
		accountBook.setDeleter(user.getChineseName());
		accountBook.setDeleterId(user.getId());
		accountBook.setDeleteAt(new Date());
		/**
		 * 0 : 可用 1 : 删除
		 **/
		accountBook.setIsDelete(BaseConsts.ONE);
		int result = accountBookDao.deleteById(accountBook);
		if (result == 0) {
			baseResult.setMsg("删除帐套失败，请重试");
		}
		return baseResult;
	}

	public Result<AccountBook> detailAccountBookById(int id) {
		Result<AccountBook> result = new Result<AccountBook>();
		AccountBook accountBook = accountBookDao.queryEntityById(id);
		accountBook.setBusiUnitName(
				cacheService.getSubjectNcByIdAndKey(accountBook.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		accountBook.setHomeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, accountBook.getIsHome() + ""));
		accountBook.setStandardCoinName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				accountBook.getStandardCoin() + ""));
		result.setItems(accountBook);
		return result;
	}

	public Result<AccountBook> editAccountBookById(int id) {
		Result<AccountBook> result = new Result<AccountBook>();
		AccountBook accountBook = accountBookDao.queryEntityById(id);
		accountBook.setBusiUnitName(
				cacheService.getSubjectNcByIdAndKey(accountBook.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		accountBook.setStandardCoinName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				accountBook.getStandardCoin() + ""));
		result.setItems(accountBook);
		return result;
	}

	public PageResult<AccountLineResDto> divideAccountBook(AccountLineSearchReqDto reqDto) {
		PageResult<AccountLineResDto> pageResult = new PageResult<AccountLineResDto>();
		reqDto.setState(BaseConsts.TWO);
		/**
		 * 1.科目状态为已完成 2.与accountBookId不存在可用的关联
		 */
		pageResult = accountLineService.queryAccountLineByCond(reqDto);
		return pageResult;
	}

	public List<AccountBook> queryEntityByBusiUnit(Integer busiUnit) {
		return accountBookDao.queryEntityByBusiUnit(busiUnit);
	}

	public PageResult<AccountBookResDto> queryAccountBookByCond(AccountBookSearchReqDto queryAccountBookReqDto) {
		PageResult<AccountBookResDto> pageResult = new PageResult<AccountBookResDto>();
		int offSet = PageUtil.getOffSet(queryAccountBookReqDto.getPage(), queryAccountBookReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryAccountBookReqDto.getPer_page());
		List<AccountBookResDto> queryAccountBookResDtos = convertToResult(
				accountBookDao.queryResultsByCond(queryAccountBookReqDto, rowBounds));
		pageResult.setItems(queryAccountBookResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryAccountBookReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryAccountBookReqDto.getPage());
		pageResult.setPer_page(queryAccountBookReqDto.getPer_page());

		return pageResult;
	}

	public List<AccountBookResDto> queryListByCon(AccountBookSearchReqDto queryAccountBookReqDto) {
		List<AccountBookResDto> queryAccountBookResDtos = convertToResult(
				accountBookDao.queryResultsByCond(queryAccountBookReqDto));
		return queryAccountBookResDtos;
	}

	private List<AccountBookResDto> convertToResult(List<AccountBook> accountBooks) {
		if (accountBooks == null) {
			return null;
		}
		List<AccountBookResDto> queryAccountBookResDtos = new ArrayList<AccountBookResDto>();
		for (AccountBook accountBook : accountBooks) {
			AccountBookResDto resDto = convertToAccountBookResDto(accountBook);
			List<CodeValue> operList = getOperList(accountBook.getState());
			resDto.setOpertaList(operList);
			queryAccountBookResDtos.add(resDto);
		}
		return queryAccountBookResDtos;
	}

	private AccountBookResDto convertToAccountBookResDto(AccountBook accountBook) {
		AccountBookResDto dto = new AccountBookResDto();
		BeanUtils.copyProperties(accountBook, dto);
		dto.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(accountBook.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		dto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_BOOK_STATE, accountBook.getState() + ""));
		dto.setHomeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, accountBook.getIsHome() + ""));
		dto.setStandardCoinName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				accountBook.getStandardCoin() + ""));
		return dto;
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
				AccountBookResDto.Operate.operMap);
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
}
