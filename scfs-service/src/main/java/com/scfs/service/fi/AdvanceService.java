package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.AdvanceDao;
import com.scfs.dao.fi.AdvanceReceiptRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.resp.AdvanceResDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.Advance;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.result.PageResult;
import com.scfs.common.exception.BaseException;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  转预收业务
 *  File: AdvanceService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator
 *
 * </pre>
 */
@Service
public class AdvanceService {
	@Autowired
	AdvanceDao advanceDao;

	@Autowired
	BankReceiptService bankReceiptService;

	@Autowired
	AdvanceReceiptRelDao advanceReceiptRelDao;

	@Autowired
	CacheService cacheService;

	/**
	 * 添加预收关系业务操作,返回水单与预收关系id
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public Integer createAdvanceRel(AdvanceReceiptRel advanceSearchReqDto) {
		int receiptId = advanceSearchReqDto.getReceiptId();
		BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
		advanceSearchReqDto.setProjectId(bankReceipt.getProjectId());
		advanceSearchReqDto.setCreator(ServiceSupport.getUser().getChineseName());
		advanceSearchReqDto.setCreatorId(ServiceSupport.getUser().getId());
		advanceSearchReqDto.setCreateAt(new Date());
		advanceSearchReqDto.setCurrencyType(bankReceipt.getCurrencyType());
		advanceReceiptRelDao.insert(advanceSearchReqDto);

		// 添加水单预收金额
		BankReceipt upBankReceipt = new BankReceipt();
		upBankReceipt.setId(bankReceipt.getId());
		upBankReceipt.setPreRecAmount(
				DecimalUtil.add(bankReceipt.getPreRecAmount(), advanceSearchReqDto.getExchangeAmount()));
		upBankReceipt.setActualPreRecAmount(
				DecimalUtil.multiply(upBankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceiptService.updateBankReceiptById(upBankReceipt);
		return advanceSearchReqDto.getId();
	}

	/**
	 * 单个删除关系
	 * 
	 * @param advanceSearchReqDto
	 * @return
	 */
	public BaseResult deleteSingleAdvanceRelById(AdvanceSearchReqDto advanceSearchReqDto) {
		AdvanceReceiptRel advanceReceiptRel = new AdvanceReceiptRel();
		advanceReceiptRel.setId(advanceSearchReqDto.getId());
		AdvanceReceiptRel advanceRel = advanceReceiptRelDao.queryEntityById(advanceReceiptRel);

		int receiptId = advanceRel.getReceiptId();
		BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
		// 添加水单预收金额
		BankReceipt upBankReceipt = new BankReceipt();
		upBankReceipt.setId(bankReceipt.getId());
		upBankReceipt
				.setPreRecAmount(DecimalUtil.subtract(bankReceipt.getPreRecAmount(), advanceRel.getExchangeAmount()));
		upBankReceipt.setActualPreRecAmount(
				DecimalUtil.multiply(upBankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceiptService.updateBankReceiptById(upBankReceipt);

		advanceReceiptRelDao.deleteById(advanceSearchReqDto.getId());
		return new BaseResult();
	}

	/**
	 * 批量删除关系
	 * 
	 * @param advanceSearchReqDto
	 * @return
	 */
	public BaseResult deleteAdvanceRelById(AdvanceSearchReqDto advanceSearchReqDto) {
		if (CollectionUtils.isNotEmpty(advanceSearchReqDto.getIds())) {
			for (Integer id : advanceSearchReqDto.getIds()) {
				AdvanceReceiptRel advanceReceiptRel = new AdvanceReceiptRel();
				advanceReceiptRel.setId(id);
				AdvanceReceiptRel advanceRel = advanceReceiptRelDao.queryEntityById(advanceReceiptRel);
				if (StringUtils.isEmpty(advanceRel)) {
					continue;
				}
				int receiptId = advanceRel.getReceiptId();
				BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
				// 添加水单预收金额
				BankReceipt upBankReceipt = new BankReceipt();
				upBankReceipt.setId(bankReceipt.getId());
				upBankReceipt.setPreRecAmount(
						DecimalUtil.subtract(bankReceipt.getPreRecAmount(), advanceRel.getExchangeAmount()));
				upBankReceipt.setActualPreRecAmount(
						DecimalUtil.multiply(upBankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
				bankReceiptService.updateBankReceiptById(upBankReceipt);

				advanceReceiptRelDao.deleteById(id);
			}
		}
		return new BaseResult();
	}

	/**
	 * 回款水单相关业务
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public Integer advanceReceipt(BankReceipt bankReceipt) {
		Integer preRecId = BaseConsts.ZERO;
		try {
			AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
			advanceSearchReqDto.setReceiptId(bankReceipt.getId());
			List<AdvanceReceiptRel> advanceRel = advanceReceiptRelDao.queryResultsByCon(advanceSearchReqDto);
			if (advanceRel != null && advanceRel.size() > BaseConsts.ZERO) {
				for (AdvanceReceiptRel advanceReceiptRel : advanceRel) {
					Advance advanDto = new Advance();
					int custId = advanceReceiptRel.getCustId();
					int projectId = advanceReceiptRel.getProjectId();
					advanDto.setCustId(custId);
					advanDto.setProjectId(projectId);
					advanDto.setBusiUnit(advanceReceiptRel.getBusiUnit());
					advanDto.setAdvanceType(advanceReceiptRel.getAdvanceType());
					advanDto.setCurrencyType(advanceReceiptRel.getCurrencyType());
					Advance advance = advanceDao.queryEntityById(advanDto);
					if (advance == null) {
						Advance saveAdvance = new Advance();
						saveAdvance.setCustId(custId);
						saveAdvance.setProjectId(projectId);
						saveAdvance.setBusiUnit(advanceReceiptRel.getBusiUnit());
						saveAdvance.setPreRecAmount(advanceReceiptRel.getExchangeAmount());// 预收余额
						saveAdvance.setPreRecSum(advanceReceiptRel.getExchangeAmount());// 预收总额
						saveAdvance.setWriteOffSum(BigDecimal.ZERO);// 已核销总额
						saveAdvance.setWritingOffAmount(BigDecimal.ZERO);// 待核销金额
						saveAdvance.setCurrencyType(advanceReceiptRel.getCurrencyType());
						saveAdvance.setCreateAt(new Date());
						saveAdvance.setCreator(ServiceSupport.getUser().getChineseName());
						saveAdvance.setCreatorId(ServiceSupport.getUser().getId());
						saveAdvance.setAdvanceType(advanceReceiptRel.getAdvanceType());
						advanceDao.insert(saveAdvance);

						preRecId = saveAdvance.getId();
						AdvanceReceiptRel upAdvanceReceiptRel = new AdvanceReceiptRel();
						upAdvanceReceiptRel.setId(advanceReceiptRel.getId());
						upAdvanceReceiptRel.setPreRecId(preRecId);
						advanceReceiptRelDao.updateById(upAdvanceReceiptRel);
					} else {
						AdvanceReceiptRel upAdvanceReceiptRel = new AdvanceReceiptRel();
						upAdvanceReceiptRel.setId(advanceReceiptRel.getId());
						upAdvanceReceiptRel.setPreRecId(advance.getId());
						advanceReceiptRelDao.updateById(upAdvanceReceiptRel);

						Advance updateAdvance = new Advance();
						updateAdvance.setPreRecAmount(
								DecimalUtil.add(advance.getPreRecAmount(), advanceReceiptRel.getExchangeAmount()));
						updateAdvance.setPreRecSum(
								DecimalUtil.add(advance.getPreRecSum(), advanceReceiptRel.getExchangeAmount()));// 预收总额
						updateAdvance.setId(advance.getId());
						advanceDao.updateById(updateAdvance);
						preRecId = advance.getId();
					}
				}
			}
		} catch (Exception e) {
			throw new BaseException(ExcMsgEnum.RECEIPT_ADVANCE);
		}
		return preRecId;
	}

	/**
	 * 预收水单相关业务
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public Integer advanceReceiptType(BankReceipt bankReceipt) {
		Advance advanDto = new Advance();
		advanDto.setId(bankReceipt.getPreRecId());
		Advance advance = advanceDao.queryEntityById(advanDto); // 获取预收数据
		BigDecimal writeOffAmount = bankReceipt.getReceiptAmount();// 待核销金额
		Advance updateAdvance = new Advance();
		updateAdvance.setId(advance.getId());
		updateAdvance.setWritingOffAmount(DecimalUtil.subtract(advance.getWritingOffAmount(), writeOffAmount));
		updateAdvance.setWriteOffSum(DecimalUtil.add(advance.getWriteOffSum(), writeOffAmount));// 修改
		advanceDao.updateById(updateAdvance);
		return advance.getId();
	}

	/**
	 * 回滚回款水单核完预收相关操作 TODO.
	 *
	 * @param bankReceipt
	 */
	public void rollbackAdvanceReceipt(BankReceipt bankReceipt) {
		AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
		advanceSearchReqDto.setReceiptId(bankReceipt.getId());
		List<AdvanceReceiptRel> advanceReceiptRels = advanceReceiptRelDao.queryResultsByCon(advanceSearchReqDto);
		if (!CollectionUtils.isEmpty(advanceReceiptRels)) {
			for (AdvanceReceiptRel advanceReceiptRel : advanceReceiptRels) {

				AdvanceReceiptRel arrUpd = new AdvanceReceiptRel();
				arrUpd.setId(advanceReceiptRel.getId());
				arrUpd.setPreRecId(null);
				advanceReceiptRelDao.updateById(arrUpd);

				Advance advance = advanceDao.queryAndLockEntityById(advanceReceiptRel.getPreRecId());
				if (advance == null) {
					throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, AdvanceDao.class,
							advanceReceiptRel.getPreRecId());
				}
				Advance advanceUpd = new Advance();
				advanceUpd.setId(advance.getId());
				advanceUpd.setPreRecAmount(
						DecimalUtil.subtract(advance.getPreRecAmount(), advanceReceiptRel.getExchangeAmount()));
				advanceUpd.setPreRecSum(
						DecimalUtil.subtract(advance.getPreRecSum(), advanceReceiptRel.getExchangeAmount()));
				advanceDao.updateById(advanceUpd);
			}
		}
	}

	/**
	 * 回滚预收水单核完预收相关操作 TODO.
	 *
	 * @param bankReceipt
	 */
	public void rollbackAdvanceReceiptType(BankReceipt bankReceipt) {
		Integer preRecId = bankReceipt.getPreRecId();
		if (StringUtils.isEmpty(preRecId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "数据有误:预收水单类型预收id不存在");
		}
		Advance advance = advanceDao.queryAndLockEntityById(preRecId);
		if (advance == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, AdvanceDao.class, preRecId);
		}
		Advance advanceUpd = new Advance();
		advanceUpd.setId(advance.getId());
		advanceUpd.setWriteOffSum(DecimalUtil.subtract(advance.getWriteOffSum(), bankReceipt.getReceiptAmount()));
		advanceUpd.setWritingOffAmount(DecimalUtil.add(advance.getWriteOffSum(), bankReceipt.getReceiptAmount()));
		advanceDao.updateById(advanceUpd);
	}

	/**
	 * 获取所有水单关联下数据
	 * 
	 * @param advanceSearchReqDto
	 * @return
	 */
	public List<AdvanceResDto> queryAdvanceRelResultsById(AdvanceSearchReqDto advanceSearchReqDto) {
		return convertToAdvanceResDtos(advanceReceiptRelDao.queryResultsByCon(advanceSearchReqDto));
	}

	/**
	 * 查询分页数据
	 * 
	 * @param advanceSearchReqDto
	 * @return
	 */
	public PageResult<AdvanceResDto> queryAdvanceRelResultsByCon(AdvanceSearchReqDto advanceSearchReqDto) {
		PageResult<AdvanceResDto> pageResult = new PageResult<AdvanceResDto>();
		int receiptId = advanceSearchReqDto.getReceiptId();// 获取水单id
		// BankReceipt receipt = new BankReceipt();
		// receipt.setId(receiptId);
		BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
		BigDecimal blance = bankReceipt.getReceiptBlance();
		String totalStr = "水单金额  : " + DecimalUtil.toAmountString(bankReceipt.getReceiptAmount())
				+ "&nbsp;&nbsp;&nbsp;尾差: " + DecimalUtil.toAmountString(bankReceipt.getDiffAmount())
				+ "&nbsp;&nbsp;&nbsp;核销金额: " + DecimalUtil.toAmountString(bankReceipt.getWriteOffAmount())
				+ "&nbsp;&nbsp;&nbsp;预收金额: " + DecimalUtil.toAmountString(bankReceipt.getPreRecAmount())
				+ "&nbsp;&nbsp;&nbsp;余额: " + DecimalUtil.toAmountString(blance);
		pageResult.setTotalStr(totalStr);

		int offSet = PageUtil.getOffSet(advanceSearchReqDto.getPage(), advanceSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, advanceSearchReqDto.getPer_page());
		List<AdvanceResDto> advanceResDto = convertToAdvanceResDtos(
				advanceReceiptRelDao.queryResultsByCon(advanceSearchReqDto, rowBounds));
		pageResult.setItems(advanceResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), advanceSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(advanceSearchReqDto.getPage());
		pageResult.setPer_page(advanceSearchReqDto.getPer_page());
		return pageResult;
	}

	public List<AdvanceResDto> convertToAdvanceResDtos(List<AdvanceReceiptRel> result) {
		List<AdvanceResDto> advanceResDtos = new ArrayList<AdvanceResDto>();
		if (ListUtil.isEmpty(result)) {
			return advanceResDtos;
		}
		for (AdvanceReceiptRel advanceReceiptRel : result) {
			AdvanceResDto advanceResDto = convertToAdvanceResDto(advanceReceiptRel);
			advanceResDtos.add(advanceResDto);
		}
		return advanceResDtos;
	}

	public AdvanceResDto convertToAdvanceResDto(AdvanceReceiptRel advanceReceiptRel) {
		AdvanceResDto advanceResDto = new AdvanceResDto();
		advanceResDto.setId(advanceReceiptRel.getId());
		advanceResDto.setProjectName(cacheService.getProjectNameById(advanceReceiptRel.getProjectId()));
		advanceResDto.setBusiUnit(
				cacheService.getSubjectNcByIdAndKey(advanceReceiptRel.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		advanceResDto.setCustName(
				cacheService.getSubjectNcByIdAndKey(advanceReceiptRel.getCustId(), CacheKeyConsts.CUSTOMER));
		advanceResDto.setPreRecAmount(advanceReceiptRel.getExchangeAmount());
		advanceResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				advanceReceiptRel.getCurrencyType() + ""));
		advanceResDto.setAdvanceType(advanceReceiptRel.getAdvanceType());
		advanceResDto.setAdvanceTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ADVANCE_TYPE, advanceReceiptRel.getAdvanceType() + ""));
		advanceResDto.setPaidAmount(advanceReceiptRel.getPaidAmount());
		advanceResDto.setDeletePrivFlag(advanceReceiptRel.getDeletePrivFlag());
		return advanceResDto;
	}

	/**
	 * 根据项目ID和客户ID已经币种查询水单信息，客户ID可以为空(定金)
	 * 
	 * @param projectId
	 * @param customerId
	 * @param currency
	 * @return
	 */
	public List<Advance> queryAdvanceByProCId(Integer projectId, Integer customerId, Integer currency) {
		Advance ad = new Advance();
		ad.setAdvanceType(BaseConsts.ONE);
		ad.setProjectId(projectId);
		ad.setCustId(customerId);
		ad.setCurrencyType(currency);
		return advanceDao.queryPerAdvanceByProCId(ad);
	}

	/**
	 * 根据项目ID和客户ID已经币种查询水单信息，客户ID可以为空(货款)
	 * 
	 * @param projectId
	 * @param customerId
	 * @param currency
	 * @return
	 */
	public List<Advance> queryGoodsAdvanceByProCId(Integer projectId, Integer customerId, Integer currency) {
		Advance ad = new Advance();
		ad.setAdvanceType(BaseConsts.TWO);
		ad.setProjectId(projectId);
		ad.setCustId(customerId);
		ad.setCurrencyType(currency);
		return advanceDao.queryPerAdvanceByProCId(ad);
	}

	/**
	 * 根据水单ID锁定水单
	 * 
	 * @param id
	 * @return
	 */
	public Advance queryAndLockEntityById(int id) {
		return advanceDao.queryAndLockEntityById(id);
	}

	/**
	 * 根据水单ID更新水单信息
	 * 
	 * @param advance
	 * @return
	 */
	public int updateAdvanceById(Advance advance) {
		return advanceDao.updateById(advance);
	}

	public AdvanceResDto detailAdvanceReceiptRelById(int id) {
		AdvanceReceiptRel advanceReceiptRel = new AdvanceReceiptRel();
		advanceReceiptRel.setId(id);
		return convertToAdvanceResDto(advanceReceiptRelDao.queryEntityById(advanceReceiptRel));
	}

}
