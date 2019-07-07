package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.PrepaidReceiptRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.PrepaidReceiptRelReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.dto.resp.PrepaidReceiptRelResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.PrepaidReceiptRel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class PrepaidReceiptRelService {

	@Autowired
	private BankReceiptService bankReceiptService; // 水单
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PrepaidReceiptRelDao prepaidReceiptRelDao;
	@Autowired
	private BankReceiptDao bankReceiptDao;

	/**
	 * 查询预收水单关系数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	public PageResult<PrepaidReceiptRelResDto> queryPrepaidReceiptRelByCon(PrepaidReceiptRelReqDto receiptRelReqDto) {
		PageResult<PrepaidReceiptRelResDto> pageResult = new PageResult<PrepaidReceiptRelResDto>();
		if (receiptRelReqDto != null && !StringUtils.isEmpty(receiptRelReqDto.getReceiptId())) {
			BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptRelReqDto.getReceiptId())
					.getItems();
			BigDecimal blance = bankReceipt.getReceiptBlance();
			String totalStr = "水单金额  : " + DecimalUtil.toAmountString(bankReceipt.getReceiptAmount())
					+ "&nbsp;&nbsp;&nbsp;尾差: " + DecimalUtil.toAmountString(bankReceipt.getDiffAmount())
					+ "&nbsp;&nbsp;&nbsp;核销金额: " + DecimalUtil.toAmountString(bankReceipt.getWriteOffAmount())
					+ "&nbsp;&nbsp;&nbsp;预收金额: " + DecimalUtil.toAmountString(bankReceipt.getPreRecAmount())
					+ "&nbsp;&nbsp;&nbsp;余额: " + DecimalUtil.toAmountString(blance);
			pageResult.setTotalStr(totalStr);
			pageResult.setTotalAmount(blance);
		}
		int offSet = PageUtil.getOffSet(receiptRelReqDto.getPage(), receiptRelReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, receiptRelReqDto.getPer_page());
		List<PrepaidReceiptRelResDto> relResDtos = convertToPrepaidReceiptRelDtos(
				prepaidReceiptRelDao.queryResultsByCon(receiptRelReqDto, rowBounds));
		pageResult.setItems(relResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), receiptRelReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(receiptRelReqDto.getPage());
		pageResult.setPer_page(receiptRelReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 查询预付水单编辑的数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	public Result<PrepaidReceiptRelResDto> editPrepaidReceiptByCon(PrepaidReceiptRelReqDto receiptRelReqDto) {
		Result<PrepaidReceiptRelResDto> result = new Result<PrepaidReceiptRelResDto>();
		if (receiptRelReqDto != null && receiptRelReqDto.getId() != null) {
			BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptRelReqDto.getId());
			if (bankReceipt != null) {
				PrepaidReceiptRelResDto dto = new PrepaidReceiptRelResDto();
				dto.setReceiptId(bankReceipt.getId());
				dto.setProjectId(bankReceipt.getProjectId());
				dto.setProjectName(cacheService.getProjectNameById(bankReceipt.getProjectId()));
				dto.setCustomerId(bankReceipt.getCustId());
				dto.setCustomerName(
						cacheService.getSubjectNcByIdAndKey(bankReceipt.getCustId(), CacheKeyConsts.CUSTOMER));
				dto.setBusiUnitId(bankReceipt.getBusiUnit());
				dto.setBusiUnitName(
						cacheService.getSubjectNcByIdAndKey(bankReceipt.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
				dto.setPrepaidAmount(DecimalUtil.subtract(
						DecimalUtil.subtract(bankReceipt.getReceiptAmount(), bankReceipt.getWriteOffAmount()),
						bankReceipt.getPreRecAmount()));
				result.setItems(dto);
			}
		}
		return result;
	}

	/**
	 * 预付水单明细数据保存
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	public BaseResult createPrepaidReceiptByCon(PrepaidReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		if (receiptRelReqDto != null) {
			BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptRelReqDto.getReceiptId());
			if (bankReceipt != null) {
				BigDecimal prepaidAmount = DecimalUtil.subtract(
						DecimalUtil.subtract(bankReceipt.getReceiptAmount(), bankReceipt.getWriteOffAmount()),
						bankReceipt.getPreRecAmount());
				if (DecimalUtil.lt(DecimalUtil.subtract(prepaidAmount, receiptRelReqDto.getActualPrepaidAmount()),
						BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"预付添加水单关系,水单编号为【" + bankReceipt.getReceiptNo() + "】实际转预付金额大于可转预付金额");
				}
				// 保存预付水单关系数据
				PrepaidReceiptRel prepaidReceiptRel = new PrepaidReceiptRel();
				prepaidReceiptRel.setReceiptId(bankReceipt.getId());
				prepaidReceiptRel.setProjectId(bankReceipt.getProjectId());
				prepaidReceiptRel.setCustomerId(bankReceipt.getCustId());
				prepaidReceiptRel.setBusiUnitId(bankReceipt.getActualCurrencyType());
				prepaidReceiptRel.setCurrnecyType(bankReceipt.getActualCurrencyType());
				prepaidReceiptRel.setPrepaidAmount(prepaidAmount);
				prepaidReceiptRel.setActualPrepaidAmount(receiptRelReqDto.getActualPrepaidAmount());
				prepaidReceiptRel.setPrepaidType(receiptRelReqDto.getPrepaidType());
				prepaidReceiptRelDao.insert(prepaidReceiptRel);
				// 修改水单预付数据
				BankReceipt receipt = new BankReceipt();
				receipt.setId(bankReceipt.getId());
				receipt.setPreRecAmount(
						DecimalUtil.add(bankReceipt.getPreRecAmount(), receiptRelReqDto.getActualPrepaidAmount()));
				receipt.setActualPreRecAmount(DecimalUtil.add(bankReceipt.getActualPreRecAmount(),
						receiptRelReqDto.getActualPrepaidAmount()));
				bankReceiptDao.updateById(receipt);
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"预付添加水单关系,水单id为【" + receiptRelReqDto.getReceiptId() + "】水单数据为空");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预付添加水单关系,数据传输为空");
		}
		return baseResult;
	}

	/**
	 * 删除预付水单关系数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	public BaseResult deletePrepaidRecepitRel(PrepaidReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		if (receiptRelReqDto != null) {
			List<Integer> ids = receiptRelReqDto.getIds();
			BigDecimal preAmount = BigDecimal.ZERO;
			Integer receiptId = null;
			if (!CollectionUtils.isEmpty(ids)) {
				for (Integer id : ids) {
					PrepaidReceiptRel receiptRel = new PrepaidReceiptRel();
					PrepaidReceiptRel prepaidReceiptRel = prepaidReceiptRelDao.queryEntityById(id);
					receiptId = prepaidReceiptRel.getReceiptId();
					receiptRel.setId(prepaidReceiptRel.getId());
					receiptRel.setIsDelete(BaseConsts.ONE);
					this.updatePrepaidReceiptBy(receiptRel);
					preAmount = DecimalUtil.add(preAmount, prepaidReceiptRel.getActualPrepaidAmount());
				}
				// 修改水单的金额
				BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptId);
				if (bankReceipt == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付删除水单关系,水单数据为空");
				}
				BankReceipt receipt = new BankReceipt();
				receipt.setId(bankReceipt.getId());
				receipt.setPreRecAmount(DecimalUtil.subtract(bankReceipt.getPreRecAmount(), preAmount));
				receipt.setActualPreRecAmount(DecimalUtil.subtract(bankReceipt.getActualPreRecAmount(), preAmount));
				bankReceiptDao.updateById(receipt);
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预付水单删除关系,数据传输为空");
		}
		return baseResult;
	}

	/**
	 * 更新预付水单数据或者是新增
	 * 
	 * @param prepaidReceiptRel
	 */
	public void updatePrepaidReceiptBy(PrepaidReceiptRel prepaidReceiptRel) {

		if (prepaidReceiptRel.getId() != null) { // 修改数据
			prepaidReceiptRelDao.updateById(prepaidReceiptRel);
		} else { // 新增数据
			prepaidReceiptRelDao.insert(prepaidReceiptRel);
		}

	}

	/**
	 * 封装当前预付水单的显示数据
	 * 
	 * @param copeReceiptRels
	 * @return
	 */
	public List<PrepaidReceiptRelResDto> convertToPrepaidReceiptRelDtos(List<PrepaidReceiptRel> prepaidReceiptRels) {
		List<PrepaidReceiptRelResDto> resDtos = new ArrayList<PrepaidReceiptRelResDto>();
		if (CollectionUtils.isEmpty(prepaidReceiptRels)) {
			return resDtos;
		}
		for (PrepaidReceiptRel receiptRel : prepaidReceiptRels) {
			PrepaidReceiptRelResDto receiptRelResDto = this.convertToPrePaidReceiptRelResDto(receiptRel);
			resDtos.add(receiptRelResDto);
		}
		return resDtos;
	}

	/**
	 * 数据封装
	 * 
	 * @param copeReceiptRel
	 * @return
	 */
	public PrepaidReceiptRelResDto convertToPrePaidReceiptRelResDto(PrepaidReceiptRel prepaidReceiptRel) {
		PrepaidReceiptRelResDto relResDto = new PrepaidReceiptRelResDto();
		if (prepaidReceiptRel != null) {
			BeanUtils.copyProperties(prepaidReceiptRel, relResDto);
			relResDto.setProjectName(cacheService.getProjectNameById(prepaidReceiptRel.getProjectId()));
			relResDto.setBusiUnitName(
					cacheService.getSubjectNcByIdAndKey(prepaidReceiptRel.getBusiUnitId(), CacheKeyConsts.BUSI_UNIT));
			relResDto.setCustomerName(
					cacheService.getSubjectNcByIdAndKey(prepaidReceiptRel.getCustomerId(), CacheKeyConsts.CUSTOMER));
			relResDto.setCurrnecyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					prepaidReceiptRel.getCurrnecyType() + ""));
			relResDto.setPrepaidName(ServiceSupport.getValueByBizCode(BizCodeConsts.PREPAID_TYPE,
					prepaidReceiptRel.getPrepaidType() + ""));
		}
		return relResDto;
	}

}
