package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.scfs.common.consts.AccountNoConsts;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.AccountBookDao;
import com.scfs.dao.fi.VoucherDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineModelResDto;
import com.scfs.domain.fi.dto.resp.VoucherModelResDto;
import com.scfs.domain.fi.dto.resp.VoucherResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.result.PageResult;
import com.scfs.service.audit.VoucherAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.common.CurrencyRateService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FiCacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * 
 * <pre>
 * 
 *  File: VoucherServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月24日			Administrator
 *
 * </pre>
 */
@Service
public class VoucherService {
	@Autowired
	VoucherDao voucherDao;
	@Autowired
	VoucherLineDao voucherLineDao;
	@Autowired
	CacheService cacheService;
	@Autowired
	FiCacheService fiCacheService;
	@Autowired
	VoucherLineService voucherLineService;
	@Autowired
	VoucherAuditService voucherAuditService;
	@Autowired
	BaseExchangeRateService baseExchangeRateService;
	@Autowired
	CurrencyRateService currencyRateService;
	@Autowired
	AuditFlowService auditFlowService;
	@Autowired
	private AccountBookDao accountBookDao;
	@Autowired
	private SequenceService sequenceService;

	public Integer createVoucherDetail(VoucherDetail voucherDetail) {
		return createVoucherDetail(voucherDetail, false);
	}

	public Integer createVoucherDetailOne(VoucherDetail voucherDetail) {
		return createVoucherDetail(voucherDetail, true);
	}

	public Integer createVoucherDetail(VoucherDetail voucherDetail, boolean isReturnLineId) {
		BigDecimal debitAmount = BigDecimal.ZERO;
		BigDecimal creditAmount = BigDecimal.ZERO;
		Voucher voucher = voucherDetail.getVoucher();
		if (voucher == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误，请核查");
		}
		AccountBook accountBook = cacheService.getAccountBookById(voucher.getAccountBookId());
		if (accountBook.getId() == null || accountBook.getIsDelete().equals(BaseConsts.ONE)
				|| !accountBook.getState().equals(BaseConsts.TWO)) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_NOT_IN_USE, voucher.getAccountBookId());
		}
		voucher.setBusiUnit(accountBook.getBusiUnit());
		List<VoucherLine> voucherLines = voucherDetail.getVoucherLines();
		// 计算借方总金额和贷方总金额
		if (voucherLines == null || voucherLines.size() == 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误，请核查");
		}
		// 查询业务日期财务汇率
		Date voucherDate = voucher.getVoucherDate();
		Integer standardCoin = accountBook.getStandardCoin();
		if (null == standardCoin) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置账套【 " + accountBook.getAccountBookName() + "】的本币币种");
		}

		Integer debitCurrencyType = null;
		Integer creditCurrencyType = null;
		Set<Integer> debitCurrencyTypeSet = new HashSet<Integer>();
		Set<Integer> creditCurrencyTypeSet = new HashSet<Integer>();
		VoucherLine debitVoucherLine = null;
		// TODO 优化
		/**
		Integer currDebitCurrencyType = null;
		for (VoucherLine voucherLine : voucherLines) {
			if (voucherLine.getDebitOrCredit() == BaseConsts.ONE) {// 借方
				Integer accountLineId = voucherLine.getAccountLineId(); // 科目ID
				if (accountLineId != null) {
					AccountLine accountLine = cacheService.getAccountLineById(accountLineId);
					if (accountLine != null && !accountLine.getAccountLineNo().equals(BaseConsts.FINANCIAL_FEES_NO)) {
						currDebitCurrencyType = voucherLine.getCurrencyType();
						break;
					}
				}
			}
		}
		**/
		for (VoucherLine voucherLine : voucherLines) {
			BigDecimal amount = voucherLine.getAmount();
			switch (voucherLine.getDebitOrCredit()) {
			case BaseConsts.ONE: // 借方
				/**
				Integer accountLineId = voucherLine.getAccountLineId(); // 科目ID
				if (accountLineId != null) {
					// 销售手续费科目与其他借方科目存在币种不一致的情况，销售手续费转换为其他借方科目币种
					AccountLine accountLine = cacheService.getAccountLineById(accountLineId);
					if (accountLine != null && accountLine.getAccountLineNo().equals(BaseConsts.FINANCIAL_FEES_NO)) {
						amount = DecimalUtil
								.formatScale2(DecimalUtil.multiply(amount, currencyRateService.queryRateByTheMonthCd(
										voucherLine.getCurrencyType(), currDebitCurrencyType, theMonthCd)));
						voucherLine.setAmount(amount);
						voucherLine.setCurrencyType(currDebitCurrencyType);
					}
				}
				**/
				debitAmount = DecimalUtil.add(debitAmount, amount);
				debitCurrencyType = voucherLine.getCurrencyType();
				debitCurrencyTypeSet.add(debitCurrencyType);
				debitVoucherLine = voucherLine;
				break;
			case BaseConsts.TWO: // 贷方
				creditAmount = DecimalUtil.add(creditAmount, amount);
				creditCurrencyType = voucherLine.getCurrencyType();
				creditCurrencyTypeSet.add(creditCurrencyType);
				break;
			}
			Integer currencyType = voucherLine.getCurrencyType();
			if (currencyType == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不能为空，请核查");
			}
		}
		if (!CollectionUtils.isEmpty(debitCurrencyTypeSet) && debitCurrencyTypeSet.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "借方币种必须一致，请核查");
		}
		if (!CollectionUtils.isEmpty(creditCurrencyTypeSet) && creditCurrencyTypeSet.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "贷方币种必须一致，请核查");
		}

		// 转换成本币金额
		BigDecimal standardDebitAmount = BigDecimal.ZERO;
		if (null != debitCurrencyType && null != standardCoin) {
			standardDebitAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(debitAmount,
					baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
							String.valueOf(debitCurrencyType), String.valueOf(standardCoin), voucherDate)));
		}
		BigDecimal standardCreditAmount = BigDecimal.ZERO;
		if (null != creditCurrencyType && null != standardCoin) {
			standardCreditAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(creditAmount,
					baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
							String.valueOf(creditCurrencyType), String.valueOf(standardCoin), voucherDate)));
		}

		if (!DecimalUtil.eq(standardDebitAmount, standardCreditAmount)) {
			BigDecimal profitLossAmount = DecimalUtil.subtract(standardCreditAmount, standardDebitAmount);
			if (null != debitVoucherLine && !DecimalUtil.eq(profitLossAmount, BigDecimal.ZERO)) { // 添加损益分录
				VoucherLine profitLossVoucherLine = createProfitLossVoucherLine(debitVoucherLine, profitLossAmount,
						standardCoin);
				voucherLines.add(profitLossVoucherLine);
			}
		}

		voucher.setDebitCurrencyType(debitCurrencyType);
		voucher.setCreditCurrencyType(creditCurrencyType);
		voucher.setDebitAmount(debitAmount);
		voucher.setCreditAmount(creditAmount);
		voucher.setVoucherLineNumber(voucherLines.size());
		// 插入凭证
		int voucherId = createVoucher(voucher);
		// 插入分录
		List<Integer> ids = new ArrayList<Integer>();
		for (VoucherLine voucherLine : voucherLines) {
			if (DecimalUtil.eq(BigDecimal.ZERO, voucherLine.getAmount()) && (null == voucherLine.getStandardAmount()
					|| DecimalUtil.eq(BigDecimal.ZERO, voucherLine.getStandardAmount()))) {
				continue;
			}
			voucherLine.setBillNo(voucher.getBillNo());
			voucherLine.setBillType(voucher.getBillType());
			voucherLine.setBillDate(voucher.getBillDate());
			voucherLine.setBusiUnit(voucher.getBusiUnit());
			voucherLine.setExchangeRate(DecimalUtil.ONE);
			voucherLine.setVoucherId(voucherId);
			voucherLine.setCnyAmount(DecimalUtil.divide(voucherLine.getAmount(), voucherLine.getExchangeRate()));
			voucherLine.setStandardCoin(standardCoin);
			voucherLine.setStandardRate(baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
					String.valueOf(voucherLine.getCurrencyType()), String.valueOf(voucherLine.getStandardCoin()), voucherDate));
			if (null == voucherLine.getStandardAmount()) {
				voucherLine.setStandardAmount(DecimalUtil
						.formatScale2(DecimalUtil.multiply(voucherLine.getAmount(), voucherLine.getStandardRate())));
			}
			Integer id = voucherLineService.createVoucherLine(voucherLine);
			ids.add(id);
		}
		Integer returnId = voucherId;
		if (isReturnLineId == true) {
			if (ids.size() == BaseConsts.ZERO) {
				returnId = null;
			} else {
				returnId = ids.get(BaseConsts.ZERO);
			}
		}
		return returnId;
	}

	private VoucherLine createProfitLossVoucherLine(VoucherLine debitVoucherLine, BigDecimal profitLossAmount,
			Integer standardCoin) {
		VoucherLine profitLossVoucherLine = new VoucherLine();
		BeanUtils.copyProperties(debitVoucherLine, profitLossVoucherLine);
		profitLossVoucherLine.setId(null);
		profitLossVoucherLine.setCurrencyType(standardCoin);
		profitLossVoucherLine.setAmount(null);
		AccountLine accountLine = cacheService.getAccountLineByNo(AccountNoConsts.FINANCIAL_PROFITLOSS_FEES_NO);
		profitLossVoucherLine.setVoucherLineSummary(accountLine.getAccountLineName());
		profitLossVoucherLine.setAccountLineId(accountLine.getId());
		profitLossVoucherLine.setStandardAmount(profitLossAmount);
		profitLossVoucherLine.setAmount(BigDecimal.ZERO);
		return profitLossVoucherLine;
	}

	public Integer createVoucher(Voucher voucher) {
		// voucher.setVoucherNo("-"); // 编号规则财务给出，未定
		if (voucher.getState() == null) {
			voucher.setState(BaseConsts.ONE);
		}
		voucher.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: ServiceSupport.getUser().getChineseName());
		voucher.setCreatorId(
				ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID : ServiceSupport.getUser().getId());
		voucherDao.insert(voucher);
		return voucher.getId();
	}

	public VoucherDetailResDto detailVoucherDetailById(int voucherId) {
		VoucherDetailResDto result = new VoucherDetailResDto();
		Voucher voucher = voucherDao.queryEntityById(voucherId);
		if (voucher == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, "不存在id=" + voucherId + "的凭证");
		}
		VoucherModelResDto voucherModelResDto = convertToVoucherModelRes(voucher);
		List<VoucherLineModelResDto> voucherLineModelResDtos = voucherLineService.queryResultsByVoucherId(voucherId);
		result.setVoucherLines(voucherLineModelResDtos);
		result.setVoucher(voucherModelResDto);
		return result;
	}

	public Voucher queryEntityById(Integer id) {
		return voucherDao.queryEntityById(id);
	}

	public VoucherDetailResDto editVoucherDetailById(int voucherId) {
		VoucherDetailResDto result = new VoucherDetailResDto();
		Voucher voucher = voucherDao.queryEntityById(voucherId);
		if (voucher == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, "不存在id=" + voucherId + "的凭证");
		}
		VoucherModelResDto voucherModelResDto = convertToVoucherModelRes(voucher);
		List<VoucherLineModelResDto> voucherLineModelResDtos = voucherLineService.queryResultsByVoucherId(voucherId);
		result.setVoucherLines(voucherLineModelResDtos);
		result.setVoucher(voucherModelResDto);
		return result;
	}

	/**
	 * 通过条件查询凭证详情
	 * 
	 * @param voucherId
	 * @return
	 */
	public VoucherDetailResDto editVoucherDetailByParam(Voucher req) {
		List<VoucherLineModelResDto> voucherLineModelResDtos = new ArrayList<VoucherLineModelResDto>();
		VoucherModelResDto voucherModelResDto = new VoucherModelResDto();
		VoucherDetailResDto result = new VoucherDetailResDto();
		Voucher voucher = voucherDao.queryEntityByParam(req);
		if (voucher == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT,
					"不存在billNo=" + req.getBillNo() + "billType=" + req.getBillType() + "的凭证");
		} else {
			voucherModelResDto = convertToVoucherModelRes(voucher);
			voucherLineModelResDtos = voucherLineService.queryResultsByVoucherId(voucher.getId());
		}
		result.setVoucherLines(voucherLineModelResDtos);
		result.setVoucher(voucherModelResDto);
		return result;
	}

	public void updateVoucherDetail(VoucherDetail voucherDetail) {
		BigDecimal debitAmount = BigDecimal.ZERO;
		BigDecimal creditAmount = BigDecimal.ZERO;
		Voucher voucher = voucherDetail.getVoucher();
		if (voucher == null || voucher.getId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误,请核查");
		}
		AccountBook accountBook = cacheService.getAccountBookById(voucher.getAccountBookId());
		if (accountBook.getId() == null || accountBook.getIsDelete().equals(BaseConsts.ONE)
				|| !accountBook.getState().equals(BaseConsts.TWO)) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_NOT_IN_USE, voucher.getAccountBookId());
		}
		// 查询业务日期财务汇率
		Date voucherDate = voucher.getVoucherDate();
		Integer standardCoin = accountBook.getStandardCoin();
		if (null == standardCoin) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置账套【 " + accountBook.getAccountBookName() + "】的本币币种");
		}

		Integer debitCurrencyType = null;
		Integer creditCurrencyType = null;
		Set<Integer> debitCurrencyTypeSet = new HashSet<Integer>();
		Set<Integer> creditCurrencyTypeSet = new HashSet<Integer>();
		VoucherLine debitVoucherLine = null;
		List<VoucherLine> voucherLines = voucherDetail.getVoucherLines();
		if (voucherLines != null && voucherLines.size() > 0) {
			// 计算借方总金额和贷方总金额
			for (VoucherLine voucherLine : voucherLines) {
				BigDecimal amount = voucherLine.getAmount();
				switch (voucherLine.getDebitOrCredit()) {
				case BaseConsts.ONE: // 借方
					debitAmount = DecimalUtil.add(debitAmount, amount);
					debitCurrencyType = voucherLine.getCurrencyType();
					debitCurrencyTypeSet.add(debitCurrencyType);
					debitVoucherLine = voucherLine;
					break;
				case BaseConsts.TWO: // 贷方
					creditAmount = DecimalUtil.add(creditAmount, amount);
					creditCurrencyType = voucherLine.getCurrencyType();
					creditCurrencyTypeSet.add(creditCurrencyType);
					break;
				}
				Integer currencyType = voucherLine.getCurrencyType();
				if (currencyType == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不能为空，请核查");
				}
			}
		}
		if (!CollectionUtils.isEmpty(debitCurrencyTypeSet) && debitCurrencyTypeSet.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "借方币种必须一致，请核查");
		}
		if (!CollectionUtils.isEmpty(creditCurrencyTypeSet) && creditCurrencyTypeSet.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "贷方币种必须一致，请核查");
		}

		// 转换成本币金额
		BigDecimal standardDebitAmount = BigDecimal.ZERO;
		if (null != debitCurrencyType && null != standardCoin) {
			standardDebitAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(debitAmount,
					baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
							String.valueOf(debitCurrencyType), String.valueOf(standardCoin), voucherDate)));
		}
		BigDecimal standardCreditAmount = BigDecimal.ZERO;
		if (null != creditAmount && null != standardCoin) {
			standardCreditAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(creditAmount,
					baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
							String.valueOf(creditCurrencyType), String.valueOf(standardCoin), voucherDate)));
		}

		if (!DecimalUtil.eq(standardDebitAmount, standardCreditAmount)) {
			BigDecimal profitLossAmount = DecimalUtil.subtract(standardCreditAmount, standardDebitAmount);
			if (null != debitVoucherLine && !DecimalUtil.eq(profitLossAmount, BigDecimal.ZERO)) { // 添加损益分录
				VoucherLine profitLossVoucherLine = createProfitLossVoucherLine(debitVoucherLine, profitLossAmount,
						standardCoin);
				voucherLines.add(profitLossVoucherLine);
			}
		}

		voucher.setDebitCurrencyType(debitCurrencyType);
		voucher.setCreditCurrencyType(creditCurrencyType);
		voucher.setDebitAmount(debitAmount);
		voucher.setCreditAmount(creditAmount);
		voucher.setVoucherLineNumber(voucherLines.size());
		voucherDao.updateById(voucher);
		List<VoucherLine> curVoucherLines = voucherLineDao.queryResultsByVoucherId(voucher.getId());
		List<VoucherLine> deletes = curVoucherLines;
		List<VoucherLine> adds = new ArrayList<VoucherLine>();
		if (!StringUtils.isEmpty(voucherLines)) {
			for (int i = 0; i < voucherLines.size(); i++) {
				VoucherLine nItem = voucherLines.get(i);
				if (nItem.getId() == null) {
					adds.add(nItem);
					continue;
				}
				for (int j = 0; j < curVoucherLines.size(); j++) {
					VoucherLine oItem = curVoucherLines.get(j);
					if (nItem.getId().equals(oItem.getId())) {
						deletes.remove(j);
						voucherLineService.updateVoucherLine(nItem);
					}
				}
			}
		}
		if (deletes != null) {
			for (VoucherLine item : deletes) {
				voucherLineDao.deleteById(item.getId());
			}
		}
		if (adds != null && adds.size() > 0) {
			Integer tempCurrencyType = adds.get(0).getCurrencyType();
			BigDecimal exchangeRate = baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
					String.valueOf(BaseConsts.ONE), String.valueOf(tempCurrencyType), new Date()); // 取中国银行人民币兑外币汇率
			for (VoucherLine item : adds) {
				if (DecimalUtil.eq(BigDecimal.ZERO, item.getAmount()) && (null == item.getStandardAmount()
						|| DecimalUtil.eq(BigDecimal.ZERO, item.getStandardAmount()))) {
					continue;
				}

				item.setBillNo(voucher.getBillNo());
				item.setBillType(voucher.getBillType());
				item.setBillDate(voucher.getBillDate());
				item.setBusiUnit(accountBook.getBusiUnit());
				item.setExchangeRate(exchangeRate);
				item.setVoucherId(voucher.getId());
				item.setCnyAmount(DecimalUtil.divide(item.getAmount(), item.getExchangeRate()));
				item.setStandardCoin(standardCoin);
				item.setStandardRate(baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
						String.valueOf(item.getCurrencyType()), String.valueOf(item.getStandardCoin()), voucherDate));
				if (null == item.getStandardAmount()) {
					item.setStandardAmount(
							DecimalUtil.formatScale2(DecimalUtil.multiply(item.getAmount(), item.getStandardRate())));
				}
				voucherLineService.createVoucherLine(item);
			}
		}
	}

	/**
	 * 
	 * TODO. 红冲凭证
	 * 
	 * @param voucherId
	 */
	public void createRedVoucherById(Integer voucherId) {
		Voucher voucher = voucherDao.queryEntityById(voucherId);
		if (voucher == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, VoucherDao.class, voucherId);
		}
		if (voucher.getIsDelete().equals(BaseConsts.ONE)) {
			throw new BaseException(ExcMsgEnum.VOUCHER_ALREADY_DELETE);
		}
		if (!voucher.getState().equals(BaseConsts.THREE)) {
			throw new BaseException(ExcMsgEnum.RED_VOUCHER_STATE_ERROR);
		}
		List<VoucherLine> voucherLines = voucherLineDao.queryResultsByVoucherId(voucherId);
		VoucherDetail voucherDetail = new VoucherDetail();
		BigDecimal multiplier = new BigDecimal("-1");
		voucher.setVoucherSummary(voucher.getVoucherSummary() + "-红冲");
		voucher.setState(BaseConsts.THREE);
		voucher.setId(null);
		voucherDetail.setVoucher(voucher);

		for (VoucherLine entity : voucherLines) {
			entity.setAmount(DecimalUtil.multiply(entity.getAmount(), multiplier));
			entity.setId(null);
			entity.setVoucherId(null);
		}
		voucherDetail.setVoucherLines(voucherLines);
		createVoucherDetail(voucherDetail);
	}

	/**
	 * 新增凭证
	 * 
	 * @param voucherDetail
	 * @return
	 */
	public Integer createVoucherDetailByCon(VoucherDetail voucherDetail) {
		Voucher voucher = voucherDetail.getVoucher();
		if (voucher == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "凭证为空");
		}
		AccountBook accountBook = accountBookDao.queryEntityById(voucher.getAccountBookId());
		if (null == accountBook) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "帐套主键为【" + voucher.getAccountBookId() + "】对应的帐套数据为空");
		}
		BaseSubject baseSubject = cacheService.getBaseSubjectById(accountBook.getBusiUnit());
		if (null == baseSubject) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位为空");
		}
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(baseSubject.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		return this.createVoucherDetail(voucherDetail);
	}

	public void updateVoucherById(Voucher voucher) {
		voucherDao.updateById(voucher);
	}

	public void deleteVoucherById(int id) {

		Voucher voucher = voucherDao.queryEntityById(id);
		if (voucher.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.DELETE_ERROR, id);
		}
		List<VoucherLine> voucherLines = voucherLineDao.queryResultsByVoucherId(voucher.getId());
		for (VoucherLine item : voucherLines) {
			voucherLineDao.deleteById(item.getId());
		}
		voucherDao.deleteById(id);
	}

	public void deleteOverVoucherById(int id) {
		List<VoucherLine> voucherLines = voucherLineDao.queryResultsByVoucherId(id);
		for (VoucherLine item : voucherLines) {
			voucherLineDao.deleteById(item.getId());
		}
		voucherDao.deleteById(id);
	}

	public void submitVoucherById(int id) {
		Voucher voucher = voucherDao.queryEntityById(id);
		if (voucher.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ALREADY_SUBMIT, id);
		}
		// 提交到财务审核
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.SIX, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		voucherDao.submitById(id, startAuditNode.getAuditNodeState());

		voucherAuditService.startAudit(id, startAuditNode);
	}

	public List<Voucher> queryListByCon(VoucherSearchReqDto voucherSearchReqDto) {
		return voucherDao.queryResultsByCon(voucherSearchReqDto);
	}

	public PageResult<VoucherResDto> queryVoucherResultsByCon(VoucherSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		PageResult<VoucherResDto> pageResult = new PageResult<VoucherResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<Voucher> voucherList = voucherDao.queryResultsByCon(req, rowBounds);
		List<VoucherResDto> voucherResDtos = new ArrayList<VoucherResDto>();
		for (Voucher voucher : voucherList) {
			VoucherResDto voucherResDto = convertToVoucherRes(voucher);
			voucherResDto.setOpertaList(getOperList(voucher.getState()));
			voucherResDtos.add(voucherResDto);
		}
		pageResult.setItems(voucherResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	private VoucherResDto convertToVoucherRes(Voucher voucher) {
		VoucherResDto resDto = new VoucherResDto();
		BeanUtils.copyProperties(voucher, resDto);
		resDto.setAccountBookName(fiCacheService.getAbNameById(voucher.getAccountBookId()));
		resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, voucher.getBillType() + ""));
		resDto.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(voucher.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.VOUCHER_STATE, voucher.getState() + ""));
		resDto.setVoucherWord(
				ServiceSupport.getValueByBizCode(BizCodeConsts.VOUCHER_WORD, voucher.getVoucherWord() + ""));
		return resDto;
	}

	private VoucherModelResDto convertToVoucherModelRes(Voucher voucher) {
		VoucherModelResDto resDto = new VoucherModelResDto();
		BeanUtils.copyProperties(voucher, resDto);
		resDto.setAccountBookName(fiCacheService.getAbNameById(voucher.getAccountBookId()));
		resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, voucher.getBillType() + ""));
		BaseSubject busiUnit = cacheService.getBusiUnitById(voucher.getBusiUnit());
		resDto.setBusiUnitName(busiUnit.getChineseName());
		resDto.setBusiUnitAddress(busiUnit.getOfficeAddress());
		resDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.VOUCHER_STATE, voucher.getState() + ""));
		resDto.setVoucherWordName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.VOUCHER_WORD, voucher.getVoucherWord() + ""));
		resDto.setSystemTime(new Date());
		resDto.setVoucherNo(voucher.getVoucherNo());
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
		// 状态,1表示待提交，2表示待审核，3表示已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.INT_25:
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
