package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.AdvanceDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.resp.AdvanceResDto;
import com.scfs.domain.fi.entity.Advance;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  转预管理收业务
 *  File: AdvanceService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月07日			Administrator
 *
 * </pre>
 */
@Service
public class AdvanceManagerService {
	@Autowired
	AdvanceDao advanceDao;

	@Autowired
	BankReceiptService bankReceiptService;

	@Autowired
	CacheService cacheService;

	/**
	 * 查询水单管理分页数据
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public PageResult<AdvanceResDto> queryAdvanceResultsByCon(AdvanceSearchReqDto advanceSearchReqDto) {
		PageResult<AdvanceResDto> pageResult = new PageResult<AdvanceResDto>();
		int offSet = PageUtil.getOffSet(advanceSearchReqDto.getPage(), advanceSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, advanceSearchReqDto.getPer_page());
		advanceSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<AdvanceResDto> advanceResDto = convertToAdvanceResDtos(
				advanceDao.queryResultsByCon(advanceSearchReqDto, rowBounds));
		if (advanceSearchReqDto.getNeedSum() != null && advanceSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<Advance> advanceSumList = advanceDao.sumAdvance(advanceSearchReqDto);
			if (CollectionUtils.isNotEmpty(advanceSumList)) {
				BigDecimal preRecAmountSum = BigDecimal.ZERO;
				BigDecimal paidAmountSum = BigDecimal.ZERO;
				BigDecimal blanceSum = BigDecimal.ZERO;
				for (Advance advance : advanceSumList) {
					if (advance != null) {
						preRecAmountSum = DecimalUtil.add(preRecAmountSum, ServiceSupport.amountNewToRMB(
								advance.getPreRecAmount() == null ? DecimalUtil.ZERO : advance.getPreRecAmount(),
								advance.getCurrencyType(), new Date()));
						paidAmountSum = DecimalUtil.add(paidAmountSum,
								ServiceSupport.amountNewToRMB(
										advance.getPaidAmount() == null ? DecimalUtil.ZERO : advance.getPaidAmount(),
										advance.getCurrencyType(), new Date()));
						blanceSum = DecimalUtil.subtract(preRecAmountSum, paidAmountSum);
					}
				}
				String totalStr = "可核销余额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(preRecAmountSum))
						+ " CNY &nbsp;&nbsp;&nbsp;  已付金额: "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(paidAmountSum))
						+ " CNY &nbsp;&nbsp;&nbsp;  可付金额: "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(blanceSum)) + " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(advanceResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), advanceSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(advanceSearchReqDto.getPage());
		pageResult.setPer_page(advanceSearchReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 编辑详情
	 * 
	 * @return
	 */
	public Result<AdvanceResDto> editAdvanceById(Advance advance) {
		Result<AdvanceResDto> result = new Result<AdvanceResDto>();
		AdvanceResDto advanceResDto = convertToAdvanceResDto(advanceDao.queryEntityById(advance));
		result.setItems(advanceResDto);
		return result;
	}

	/***
	 * 修改
	 * 
	 * @param advance
	 * @return
	 */
	public BaseResult updateAdvanceById(Advance advance) {
		advanceDao.updateById(advance);
		return new BaseResult();
	}

	/**
	 * 预收核销 ,返回值为预收生成的水单id
	 * 
	 * @return
	 */
	public Integer createVerify(BankReceipt bankReceipt) {
		int advanceId = bankReceipt.getPreRecId();
		Advance advanceDto = new Advance();
		advanceDto.setId(advanceId);
		Advance advance = advanceDao.queryEntityById(advanceDto);
		BigDecimal preRecAmount = advance.getPreRecAmount();// 获取预收余额
		BigDecimal receiptAmount = bankReceipt.getReceiptAmount();
		if (DecimalUtil.gt(receiptAmount, preRecAmount)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "核销金额不足!");
		}
		Date receiptDate = bankReceipt.getReceiptDate();
		String date = DateFormatUtils.format(receiptDate, "yyyyMMdd");
		bankReceipt.setPreRecId(advanceId);// 转预收id
		bankReceipt.setBankReceiptNo(date);
		bankReceipt.setCustId(advance.getCustId());
		bankReceipt.setBusiUnit(advance.getBusiUnit());
		bankReceipt.setProjectId(advance.getProjectId());
		bankReceipt.setCurrencyType(advance.getCurrencyType());
		bankReceipt.setReceiptType(advance.getAdvanceType() + BaseConsts.ONE);
		Integer receiptId = bankReceiptService.createPreBankReceipt(bankReceipt);

		Advance upAdvance = new Advance();
		upAdvance.setId(advance.getId());
		upAdvance.setPreRecAmount(DecimalUtil.subtract(advance.getPreRecAmount(), receiptAmount));
		upAdvance.setWritingOffAmount(DecimalUtil.add(advance.getWritingOffAmount(), receiptAmount));
		advanceDao.updateById(upAdvance);
		return receiptId;
	}

	public List<AdvanceResDto> convertToAdvanceResDtos(List<Advance> result) {
		List<AdvanceResDto> advanceResDtos = new ArrayList<AdvanceResDto>();
		if (ListUtil.isEmpty(result)) {
			return advanceResDtos;
		}
		for (Advance advance : result) {
			AdvanceResDto advanceResDto = convertToAdvanceResDto(advance);
			advanceResDtos.add(advanceResDto);
		}
		return advanceResDtos;
	}

	public AdvanceResDto convertToAdvanceResDto(Advance advance) {
		AdvanceResDto advanceResDto = new AdvanceResDto();
		advanceResDto.setId(advance.getId());
		advanceResDto.setProjectName(cacheService.getProjectNameById(advance.getProjectId()));
		advanceResDto.setBusiUnit(cacheService.getSubjectNcByIdAndKey(advance.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		advanceResDto.setCustName(cacheService.getSubjectNcByIdAndKey(advance.getCustId(), CacheKeyConsts.CUSTOMER));
		advanceResDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, advance.getCurrencyType() + ""));
		advanceResDto.setPreRecSum(advance.getPreRecSum());
		advanceResDto.setPreRecAmount(advance.getPreRecAmount());
		advanceResDto.setWriteOffSum(advance.getWriteOffSum());
		advanceResDto.setWritingOffAmount(advance.getWritingOffAmount());
		advanceResDto.setAdvanceType(advance.getAdvanceType());
		advanceResDto.setAdvanceTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ADVANCE_TYPE, advance.getAdvanceType() + ""));
		if (advance.getAdvanceType() == BaseConsts.ONE) {
			advanceResDto.setPaidAmount(advance.getPaidAmount());
			advanceResDto.setBlance(DecimalUtil.subtract(advance.getPreRecSum(), advance.getPaidAmount()));
		} else {
			advanceResDto.setPaidAmount(BigDecimal.ZERO);
			advanceResDto.setBlance(BigDecimal.ZERO);
		}
		advanceResDto.setCreator(advance.getCreator());
		advanceResDto.setCreateAt(advance.getCreateAt());
		return advanceResDto;
	}
}
