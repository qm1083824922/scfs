package com.scfs.service.fi;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.AccountBookDao;
import com.scfs.dao.fi.AccountBookLineRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.fi.dto.req.AcctBookLineRelSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountBookResDto;
import com.scfs.domain.fi.dto.resp.AcctBookLineRelResDto;
import com.scfs.domain.fi.entity.AccountBookLineRel;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.result.PageResult;
import com.scfs.common.exception.BaseException;
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
 *  File: AccountLineRelServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月19日			Administrator
 *
 * </pre>
 */
@Service
public class AccountBookLineRelService {
	@Autowired
	AccountBookLineRelDao accountBookLineRelDao;

	@Autowired
	AccountBookDao accountBookDao;

	public void createRel(AccountBookLineRel accountBookLineRel) {
		// 1.给帐套加锁
		accountBookDao.queryEntityById(accountBookLineRel.getAccountBookId());
		// 2.校验是否是否已经添加过关系
		AcctBookLineRelSearchReqDto reqDto = new AcctBookLineRelSearchReqDto();
		reqDto.setAccountBookId(accountBookLineRel.getAccountBookId());
		reqDto.setAccountLineId(accountBookLineRel.getAccountLineId());
		reqDto.setState(BaseConsts.ONE);
		int relCount = accountBookLineRelDao.queryCountByCon(reqDto);
		if (relCount > 0) {
			throw new BaseException(ExcMsgEnum.REL_ALREADY_EXIST);
		}
		BaseUser user = ServiceSupport.getUser();
		accountBookLineRel.setState(BaseConsts.ONE);
		accountBookLineRel.setCreator(user.getChineseName());
		accountBookLineRel.setCreatorId(user.getId());
		// 3.添加关系
		accountBookLineRelDao.insert(accountBookLineRel);
	}

	public void batchCreateRel(BaseReqDto req) {
		for (Integer accountLineId : req.getIds()) {
			AccountBookLineRel rel = new AccountBookLineRel();
			rel.setAccountLineId(accountLineId);
			rel.setAccountBookId(req.getId());
			createRel(rel);
		}
	}

	public void deleteRel(Integer id) {
		BaseUser user = ServiceSupport.getUser();
		// 1.校验是否已经作废
		AccountBookLineRel rel = accountBookLineRelDao.queryEntityById(id);
		if (rel.getState() == BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ALREADY_INVALID, id);
		}
		AccountBookLineRel accountBookLineRel = new AccountBookLineRel();
		accountBookLineRel.setId(id);
		accountBookLineRel.setDeleter(user.getChineseName());
		accountBookLineRel.setDeleterId(user.getCreatorId());
		accountBookLineRel.setDeleteAt(new Date());
		accountBookLineRel.setState(BaseConsts.TWO);
		accountBookLineRelDao.deleteById(accountBookLineRel);
	}

	public void batchDeleteRel(BaseReqDto req) {
		for (Integer id : req.getIds()) {
			deleteRel(id);
		}
	}

	public PageResult<AcctBookLineRelResDto> queryRelByBookId(AcctBookLineRelSearchReqDto reqDto) {
		PageResult<AcctBookLineRelResDto> pageResult = new PageResult<AcctBookLineRelResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<AccountBookLineRel> entitys = accountBookLineRelDao.queryResultsByBookId(reqDto, rowBounds);
		List<AcctBookLineRelResDto> relResDtos = new ArrayList<AcctBookLineRelResDto>();
		for (AccountBookLineRel entity : entitys) {
			AcctBookLineRelResDto relResDto = new AcctBookLineRelResDto();
			BeanUtils.copyProperties(entity, relResDto);
			relResDto.setAccountLineLevelName(ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_LINE_LEVEL,
					relResDto.getAccountLineLevel() + ""));
			relResDto.setAccountLineTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_LINE_TYPE,
					relResDto.getAccountLineType() + ""));
			relResDto.setDebitOrCreditName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEBIT_OR_CREDIT, relResDto.getDebitOrCredit() + ""));
			relResDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_BOOK_LINE_REL_STATE,
					relResDto.getState() + ""));
			relResDto.setOpertaList(getOperList(relResDto.getState()));
			relResDto.setNeedDec(AccountLine.tranToNeedDec(relResDto.getNeedProject(), relResDto.getNeedSupplier(),
					relResDto.getNeedCust(), relResDto.getNeedAccount(), relResDto.getNeedTaxRate(),
					relResDto.getNeedUser(), relResDto.getNeedInnerBusiUnit()));
			relResDtos.add(relResDto);
		}
		pageResult.setItems(relResDtos);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	public PageResult<AccountBookLineRel> queryAllUseLastRelByBookId(AcctBookLineRelSearchReqDto reqDto) {
		reqDto.setState(BaseConsts.ONE);
		reqDto.setIsLast(BaseConsts.ONE);
		PageResult<AccountBookLineRel> pageResult = new PageResult<AccountBookLineRel>();
		List<AccountBookLineRel> entitys = accountBookLineRelDao.queryResultsByBookId(reqDto);
		for (AccountBookLineRel entity : entitys) {
			entity.setAccountLineName(entity.getAccountLineNo() + "-" + entity.getAccountLineName());
		}
		pageResult.setItems(entitys);
		return pageResult;
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
		// 状态,1表示可用，2表示作废
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.INVALID);
			break;
		case BaseConsts.TWO:
			break;
		}
		return opertaList;
	}
}
