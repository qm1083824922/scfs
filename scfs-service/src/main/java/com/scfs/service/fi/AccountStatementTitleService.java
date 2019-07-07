package com.scfs.service.fi;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.AccountStatementTitleDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.fi.dto.req.AccountStatementSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountStatementTitleResDto;
import com.scfs.domain.fi.dto.resp.VoucherResDto;
import com.scfs.domain.fi.entity.AccountStatementTitle;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: AccountStatementTitleService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日			Administrator
 *
 * </pre>
 */
@Service
public class AccountStatementTitleService {
	@Autowired
	AccountStatementTitleDao accountStatementTitleDao;

	@Autowired
	CacheService cacheService;

	public Integer createAccountStatementTitle(AccountStatementTitle accountStatementTitle) {
		BaseProject baseProject = cacheService.getProjectById(accountStatementTitle.getProjectId());
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目不能为空，请重试");
		}
		accountStatementTitle.setCurrencyType(baseProject.getAmountUnit());
		accountStatementTitle.setBillNo("-");
		accountStatementTitle.setState(BaseConsts.ONE);
		accountStatementTitle.setCreator(ServiceSupport.getUser().getChineseName());
		accountStatementTitle.setCreatorId(ServiceSupport.getUser().getId());
		accountStatementTitleDao.insert(accountStatementTitle);
		return accountStatementTitle.getId();
	}

	public void deleteById(Integer id) {
		AccountStatementTitle entity = accountStatementTitleDao.queryEntityById(id);
		if (entity.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.DELETE_ERROR);
		}
		accountStatementTitleDao.deleteById(id);
	}

	public void updateById(AccountStatementTitle accountStatementTitle) {
		accountStatementTitleDao.updateById(accountStatementTitle);
	}

	public void submitById(Integer id) {
		AccountStatementTitle entity = accountStatementTitleDao.queryEntityById(id);
		if (entity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, AccountStatementTitleDao.class, id);
		}
		if (entity.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ALREADY_SUBMIT);
		}
		AccountStatementTitle newEntity = new AccountStatementTitle();
		newEntity.setId(entity.getId());
		newEntity.setState(BaseConsts.TWO);
		accountStatementTitleDao.updateById(newEntity);
	}

	public AccountStatementTitle queryEntityById(Integer id) {
		return accountStatementTitleDao.queryEntityById(id);
	}

	public AccountStatementTitleResDto detailById(Integer id) {
		AccountStatementTitle entity = accountStatementTitleDao.queryEntityById(id);
		return convertToResult(entity);
	}

	public PageResult<AccountStatementTitleResDto> queryResultsByCon(AccountStatementSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		PageResult<AccountStatementTitleResDto> pageResult = new PageResult<AccountStatementTitleResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<AccountStatementTitle> accountStatementTitles = accountStatementTitleDao.queryResultsByCon(req, rowBounds);
		List<AccountStatementTitleResDto> accountStatementTitleResDtos = new ArrayList<AccountStatementTitleResDto>();
		for (AccountStatementTitle accountStatementTitle : accountStatementTitles) {
			AccountStatementTitleResDto queryAccountBookResDto = convertToResult(accountStatementTitle);
			queryAccountBookResDto.setOpertaList(getOperList(accountStatementTitle.getState()));
			accountStatementTitleResDtos.add(queryAccountBookResDto);
		}

		pageResult.setItems(accountStatementTitleResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	private AccountStatementTitleResDto convertToResult(AccountStatementTitle accountStatementTitle) {
		AccountStatementTitleResDto resDto = new AccountStatementTitleResDto();
		BeanUtils.copyProperties(accountStatementTitle, resDto);
		resDto.setProjectName(cacheService.getProjectNameById(accountStatementTitle.getProjectId()));
		resDto.setCustName(cacheService.getSubjectNoNameById(accountStatementTitle.getCustId()));
		resDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.AST_STATE, accountStatementTitle.getState() + ""));
		resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				accountStatementTitle.getCurrencyType() + ""));
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
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList, VoucherResDto.Operate.operMap);
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
		// 状态,1表示待提交，2表示待财务审核，3表示已完成
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
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}
		return opertaList;
	}

}
