package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.CopeReceiptRelDao;
import com.scfs.dao.finance.CopeManageDtlDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.dto.req.CopeReceiptRelReqDto;
import com.scfs.domain.fi.dto.req.PrepaidReceiptRelReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.dto.resp.CopeReceiptRelResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.CopeReceiptRel;
import com.scfs.domain.finance.cope.dto.req.CopeManageReqDto;
import com.scfs.domain.finance.cope.dto.resq.CopeManageDtlResDto;
import com.scfs.domain.finance.cope.entity.CopeManageDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.service.finance.CopeManageDtlService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class CopeReceiptRelService {
	@Autowired
	private BankReceiptService bankReceiptService; // 水单
	@Autowired
	private CopeReceiptRelDao copeReceiptRelDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private CopeManageDtlService copeManageDtlService;
	@Autowired
	private CopeManageDtlDao copeManageDtlDao;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private PrepaidReceiptRelService prepaidReceiptRelService;

	/**
	 * 查询分页应付水单关联表的数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	public PageResult<CopeReceiptRelResDto> queryCopeReceiptRelByCon(CopeReceiptRelReqDto receiptRelReqDto) {
		PageResult<CopeReceiptRelResDto> pageResult = new PageResult<CopeReceiptRelResDto>();
		if (receiptRelReqDto != null && !StringUtils.isEmpty(receiptRelReqDto.getReceiptId())) {
			BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptRelReqDto.getReceiptId())
					.getItems();
			BigDecimal blance = bankReceipt.getReceiptBlance();
			String totalStr = "水单金额  : " + DecimalUtil.toAmountString(bankReceipt.getReceiptAmount())
					+ "&nbsp;&nbsp;&nbsp;尾差: " + DecimalUtil.toAmountString(bankReceipt.getDiffAmount())
					+ "&nbsp;&nbsp;&nbsp;核销金额: " + DecimalUtil.toAmountString(bankReceipt.getWriteOffAmount())
					+ "&nbsp;&nbsp;&nbsp;预付金额: " + DecimalUtil.toAmountString(bankReceipt.getPreRecAmount())
					+ "&nbsp;&nbsp;&nbsp;余额: " + DecimalUtil.toAmountString(blance);
			pageResult.setTotalStr(totalStr);
			pageResult.setTotalAmount(blance);
		}
		int offSet = PageUtil.getOffSet(receiptRelReqDto.getPage(), receiptRelReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, receiptRelReqDto.getPer_page());
		List<CopeReceiptRelResDto> relResDtos = convertToCopeReceiptRelDtos(
				copeReceiptRelDao.queryResultsByCon(receiptRelReqDto, rowBounds));
		pageResult.setItems(relResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), receiptRelReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(receiptRelReqDto.getPage());
		pageResult.setPer_page(receiptRelReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 新增水单和应收管理的关系数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	public BaseResult createCopeReceiptRel(CopeReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		// 水单ID
		Integer receiptId = receiptRelReqDto.getReceiptId();
		List<CopeReceiptRel> receiptRels = receiptRelReqDto.getRels();
		BigDecimal receiptWriteAmount = BigDecimal.ZERO;
		if (!CollectionUtils.isEmpty(receiptRels)) {
			for (CopeReceiptRel copeReceiptRel : receiptRels) {
				BigDecimal writeOffAmount = copeReceiptRel.getWriteOffAmount() == null ? BigDecimal.ZERO
						: copeReceiptRel.getWriteOffAmount();
				CopeManageDtlResDto copeManageDtlResDto = copeManageDtlService
						.queryCopeManageById(copeReceiptRel.getCopeDtlId());
				if (DecimalUtil.lt(copeManageDtlResDto.getUnpaidAmount(), writeOffAmount)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"应付添加水单关系，单号为【" + copeManageDtlResDto.getBillNumber() + "】实际核销金额大于原有核销金额");
				}
				copeReceiptRel.setReceiptId(receiptId);
				copeReceiptRel.setProjectId(copeManageDtlResDto.getProjectId());
				copeReceiptRel.setCustomerId(copeManageDtlResDto.getCustomerId());
				copeReceiptRel.setBusiUnitId(copeManageDtlResDto.getBusiUnitId());
				copeReceiptRel.setCurrnecyType(copeManageDtlResDto.getCurrnecyType());
				copeReceiptRel.setIsDelete(BaseConsts.ZERO);
				copeReceiptRel.setCreateAt(new Date());
				copeReceiptRel.setCreator(
						ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
				copeReceiptRel.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
				// 新增水单和应收的关系
				copeReceiptRelDao.insert(copeReceiptRel);
				// 回写当前应收管理的数据
				copeManageDtlService.updateCopeMange(copeManageDtlResDto.getId(), writeOffAmount, BaseConsts.ONE);
				receiptWriteAmount = DecimalUtil.add(receiptWriteAmount, writeOffAmount);
			}
			// 回写水单的核销金额
			BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
			BankReceipt receipt = new BankReceipt();
			receipt.setId(bankReceipt.getId());
			receipt.setWriteOffAmount(DecimalUtil.add(bankReceipt.getWriteOffAmount(), receiptWriteAmount));
			receipt.setActualWriteOffAmount(DecimalUtil.add(bankReceipt.getActualWriteOffAmount(), receiptWriteAmount));
			bankReceiptDao.updateById(receipt);
			if (bankReceipt.getReceiptType().equals(BaseConsts.TEN)
					|| bankReceipt.getReceiptType().equals(BaseConsts.NINE)) {
				BigDecimal blance = DecimalUtil.subtract(bankReceipt.getReceiptBlance(), receiptWriteAmount);
				if (DecimalUtil.gt(blance, BigDecimal.ZERO)) {
					PrepaidReceiptRelReqDto prepaidReqDto = new PrepaidReceiptRelReqDto();
					prepaidReqDto.setReceiptId(receiptId);
					prepaidReqDto.setPrepaidType(BaseConsts.ONE);
					prepaidReqDto.setActualPrepaidAmount(blance);
					prepaidReceiptRelService.createPrepaidReceiptByCon(prepaidReqDto);
				}
			}
		}
		return baseResult;
	}

	/**
	 * 查询满足条件的应付管理数据
	 * 
	 * @param receiptRelReqDto
	 */
	public PageResult<CopeManageDtlResDto> queryCopeDtlDividByCon(CopeReceiptRelReqDto receiptRelReqDto) {
		PageResult<CopeManageDtlResDto> dtlResDto = new PageResult<CopeManageDtlResDto>();
		// 根据水单ID 查询条件数据
		if (receiptRelReqDto != null && !StringUtils.isEmpty(receiptRelReqDto.getReceiptId())) {
			BankReceiptResDto bankReceipt = bankReceiptService.detailBankReceiptById(receiptRelReqDto.getReceiptId())
					.getItems();
			if (bankReceipt != null) {
				CopeManageReqDto copeManageReqDto = new CopeManageReqDto();
				copeManageReqDto.setBusiUnitId(bankReceipt.getBusiUnit());
				copeManageReqDto.setCustomerId(bankReceipt.getCustId());
				copeManageReqDto.setCurrnecyType(bankReceipt.getCurrencyType());
				copeManageReqDto.setProjectId(bankReceipt.getProjectId());
				dtlResDto = copeManageDtlService.queryCopeManageDtlByCon(copeManageReqDto);
				dtlResDto.setTotalAmount(bankReceipt.getReceiptBlance());
			}
		}
		return dtlResDto;
	}

	/**
	 * 删除水单和应收的关系 带改动
	 * 
	 * @param receiptRelReqDto
	 */
	public void deleteCopeRecepitRel(CopeReceiptRelReqDto receiptRelReqDto) {
		if (receiptRelReqDto != null) {
			List<Integer> ids = receiptRelReqDto.getIds();
			BigDecimal writeAmount = BigDecimal.ZERO;
			Integer receiptId = null;
			if (!CollectionUtils.isEmpty(ids)) {
				for (Integer id : ids) {
					CopeReceiptRel copeReceiptRel = copeReceiptRelDao.queryEntityById(id);
					receiptId = copeReceiptRel.getReceiptId();
					copeReceiptRel.setIsDelete(BaseConsts.ONE);
					copeReceiptRelDao.updateById(copeReceiptRel);
					writeAmount = DecimalUtil.add(writeAmount, copeReceiptRel.getWriteOffAmount());
					// 修改应付管理的金额
					copeManageDtlService.updateCopeMange(copeReceiptRel.getCopeDtlId(),
							copeReceiptRel.getWriteOffAmount(), BaseConsts.TWO);
				}
				// 修改水单的金额
				BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptId);
				if (bankReceipt == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付删除水单关系,水单数据为空");
				}
				BankReceipt receipt = new BankReceipt();
				receipt.setId(bankReceipt.getId());
				receipt.setWriteOffAmount(DecimalUtil.subtract(bankReceipt.getWriteOffAmount(), writeAmount));
				receipt.setActualWriteOffAmount(
						DecimalUtil.subtract(bankReceipt.getActualWriteOffAmount(), writeAmount));
				bankReceiptDao.updateById(receipt);
			}
		}
	}

	/**
	 * 封装当前应付水单的显示数据
	 * 
	 * @param copeReceiptRels
	 * @return
	 */
	public List<CopeReceiptRelResDto> convertToCopeReceiptRelDtos(List<CopeReceiptRel> copeReceiptRels) {
		List<CopeReceiptRelResDto> resDtos = new ArrayList<CopeReceiptRelResDto>();
		if (CollectionUtils.isEmpty(copeReceiptRels)) {
			return resDtos;
		}
		for (CopeReceiptRel rel : copeReceiptRels) {
			CopeReceiptRelResDto copeReceiptRel = this.convertToCopeReceiptRelResDto(rel);
			resDtos.add(copeReceiptRel);
		}
		return resDtos;
	}

	/**
	 * 数据封装
	 * 
	 * @param copeReceiptRel
	 * @return
	 */
	public CopeReceiptRelResDto convertToCopeReceiptRelResDto(CopeReceiptRel copeReceiptRel) {
		CopeReceiptRelResDto relResDto = new CopeReceiptRelResDto();
		if (copeReceiptRel != null) {
			BeanUtils.copyProperties(copeReceiptRel, relResDto);
			relResDto.setProjectName(cacheService.getProjectNameById(copeReceiptRel.getProjectId()));
			relResDto.setBusiUnitName(
					cacheService.getSubjectNcByIdAndKey(copeReceiptRel.getBusiUnitId(), CacheKeyConsts.BUSI_UNIT));
			relResDto.setCustomerName(
					cacheService.getSubjectNcByIdAndKey(copeReceiptRel.getCustomerId(), CacheKeyConsts.CUSTOMER));
			relResDto.setCurrnecyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					copeReceiptRel.getCurrnecyType() + ""));
		}
		return relResDto;
	}

	/**
	 * 根据水单的ID回写费用单的已付金额
	 * 
	 * @param receiptId
	 */
	public void updateFeeByReceiptId(Integer receiptId) {
		CopeReceiptRelReqDto receiptRelReqDto = new CopeReceiptRelReqDto();
		receiptRelReqDto.setReceiptId(receiptId);
		List<CopeReceiptRel> copeReceiptRels = copeReceiptRelDao.queryResultsByCon(receiptRelReqDto);
		if (CollectionUtils.isNotEmpty(copeReceiptRels)) {
			for (CopeReceiptRel copeReceiptRel : copeReceiptRels) {
				CopeManageDtl manageDtl = copeManageDtlDao.queryEntityById(copeReceiptRel.getCopeDtlId());
				Fee fee = feeDao.queryEntityById(manageDtl.getBillId());
				BigDecimal writeAmount = copeReceiptRel.getWriteOffAmount() == null ? BigDecimal.ZERO
						: copeReceiptRel.getWriteOffAmount();
				Fee fee2 = new Fee();
				fee2.setId(fee.getId());
				fee2.setPaidAmount(DecimalUtil.add(fee.getPaidAmount() == null ? BigDecimal.ZERO : fee.getPaidAmount(),
						writeAmount));
				fee2.setFundUsed(
						DecimalUtil.add(fee.getFundUsed() == null ? BigDecimal.ZERO : fee.getFundUsed(), writeAmount));
				feeDao.updateById(fee2);
			}
		}

	}
}
