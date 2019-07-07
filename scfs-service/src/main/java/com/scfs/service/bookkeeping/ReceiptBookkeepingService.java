package com.scfs.service.bookkeeping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.AccountNoConsts;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fi.AdvanceReceiptRelDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.fi.VlReceiptRelDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.dto.req.RecReceiptRelReqDto;
import com.scfs.domain.fi.dto.req.VlReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.fi.entity.VlReceiptRel;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountBookService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.fi.RecReceiptRelService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FiCacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ReceiptBookkeepingService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */

@Service
public class ReceiptBookkeepingService {

	@Autowired
	BankReceiptService bankReceiptService;

	@Autowired
	CacheService cacheService;

	@Autowired
	AccountBookService accountBookService;

	@Autowired
	RecReceiptRelDao recReceiptRelDao;

	@Autowired
	RecLineDao recLineDao;

	@Autowired
	VoucherService voucherService;

	@Autowired
	AdvanceReceiptRelDao advanceReceiptRelDao;

	@Autowired
	VoucherLineDao voucherLineDao;

	@Autowired
	VlReceiptRelDao vlReceiptRelDao;

	@Autowired
	FeeDao feeDao;

	@Autowired
	BillOutStoreDao billOutStoreDao;

	@Autowired
	BillOutStoreService billOutStoreService;

	@Autowired
	FiCacheService fiCacheService;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	BankReceiptDao bankReceiptDao;
	@Autowired
	ReceiveService receiveService; // 应收
	@Autowired
	RecReceiptRelService recReceiptRelService;
	@Autowired
	private AccountLineService accountLineService;
	@Autowired
	private SequenceService sequenceService;

	/**
	 * TODO. 水单记账
	 * 
	 * @param id
	 *            水单id
	 */
	public void receiptBookkeeping(Integer id) {
		BankReceipt bankReceipt = bankReceiptService.queryEntityById(id);
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BankReceiptService.class, id);
		}
		switch (bankReceipt.getReceiptType()) {
		case BaseConsts.ONE: // 1.回款
			bankReceiptBookkeeping(bankReceipt);
			break;
		case BaseConsts.TWO: // 2.预收定金
			preReceiptBookkeeping(bankReceipt);
			break;
		case BaseConsts.THREE:// 3.预收货款
			preReceiptBookkeeping(bankReceipt);
			break;
		case BaseConsts.FIVE:// 5.内部
			operatReceiptBookkeeping(bankReceipt);
			break;
		case BaseConsts.SIX:// 6.虚拟类型
			bankReceiptBookkeeping(bankReceipt);
			break;
		case BaseConsts.SEVEN:// 7.供应商退款类型
			supplierReceiptBookkeeping(bankReceipt);
			break;
		case BaseConsts.EIGHT:// 8: 预收退款类型
			preReceiptReturnBookkeeping(bankReceipt);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * TODO. 回滚水单记账
	 * 
	 * @param id
	 */
	public void rollbackReceiptBookkeeping(Integer id) {
		BankReceipt bankReceipt = bankReceiptService.queryEntityById(id);
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BankReceiptService.class, id);
		}
		VoucherSearchReqDto voucherSearchReqDto = new VoucherSearchReqDto();
		voucherSearchReqDto.setBillType(BaseConsts.SEVEN);
		voucherSearchReqDto.setBillNo(bankReceipt.getReceiptNo());
		List<Voucher> vouchers = voucherService.queryListByCon(voucherSearchReqDto);
		if (!CollectionUtils.isEmpty(vouchers)) {

			VlReceiptRelSearchReqDto vlReceiptRelSearchReqDto = new VlReceiptRelSearchReqDto();
			vlReceiptRelSearchReqDto.setReceiptId(id);
			List<VlReceiptRel> vlReceiptRels = vlReceiptRelDao.queryRecustsByCon(vlReceiptRelSearchReqDto);
			if (!CollectionUtils.isEmpty(vlReceiptRels)) {
				for (VlReceiptRel vlReceiptRel : vlReceiptRels) {
					VoucherLine oldEntity = voucherLineDao.queryEntityById(vlReceiptRel.getVoucherLineId());
					updateBill(oldEntity, DecimalUtil.multiply(vlReceiptRel.getWriteOffAmount(), new BigDecimal("-1")));
				}
			}

			Voucher voucher = vouchers.get(0);
			voucherService.deleteOverVoucherById(voucher.getId());
		}
	}

	private void preReceiptBookkeeping(BankReceipt bankReceipt) {
		VoucherDetail voucherDetail = new VoucherDetail();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		Voucher voucher = convertToVoucher(bankReceipt, BaseConsts.THREE);
		VlReceiptRelSearchReqDto searchReqDto = new VlReceiptRelSearchReqDto();
		searchReqDto.setReceiptId(bankReceipt.getId());
		List<VlReceiptRel> vlReceiptRels = vlReceiptRelDao.queryRecustsByCon(searchReqDto);
		if (vlReceiptRels != null) {
			Iterator<VlReceiptRel> iterator = vlReceiptRels.iterator();
			while (iterator.hasNext()) {
				VlReceiptRel vlReceiptRel = iterator.next();
				VoucherLine oldEntity = voucherLineDao.queryEntityById(vlReceiptRel.getVoucherLineId());
				if (oldEntity == null) {
					throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, VoucherLineDao.class,
							vlReceiptRel.getVoucherLineId());
				}
				BigDecimal writeOffAmount = vlReceiptRel.getWriteOffAmount();
				VoucherLine crLine = convertToLineById(oldEntity, bankReceipt.getId(), writeOffAmount,
						voucher.getVoucherSummary(), BaseConsts.TWO);
				crLine.setDebitOrCredit(BaseConsts.TWO);
				voucherLines.add(crLine);

				updateBill(oldEntity, writeOffAmount); // 更新单据已收金额
			}
		}
		if (bankReceipt.getPreRecAmount() != null && DecimalUtil.gt(bankReceipt.getPreRecAmount(), DecimalUtil.ZERO)) { // 转预收
			VoucherLine crLine1 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_1223,
					voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 应收账款/外部/货款
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAmount(bankReceipt.getPreRecAmount());
			voucherLines.add(crLine1);
		}

		VoucherLine crLine = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_1223,
				voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 应收账款/外部/货款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAmount(DecimalUtil.multiply(bankReceipt.getReceiptAmount(), new BigDecimal("-1"))); // 红字，记负
		voucherLines.add(crLine);

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail);
	}

	private void bankReceiptBookkeeping(BankReceipt bankReceipt) {
		VoucherDetail voucherDetail = new VoucherDetail();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		Voucher voucher = convertToVoucher(bankReceipt, BaseConsts.INT_11);
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (bankReceipt.getReceiptWay() == BaseConsts.TWO && isHome == BaseConsts.ZERO) {
			// 国外的承兑汇票不记凭证
		} else {
			Integer drCount = 0;
			Integer crCount = 0;
			if (bankReceipt.getReceiptWay() == BaseConsts.ONE) { // 转账
				VoucherLine drLine1 = convertToVoucherLine(bankReceipt, null, voucher.getVoucherSummary(),
						BaseConsts.ONE, BaseConsts.TWO, bankReceipt.getRecAccountNo()); // 银行存款
				drLine1.setDebitOrCredit(BaseConsts.ONE);
				drLine1.setAccountId(bankReceipt.getRecAccountNo());
				voucherLines.add(drLine1);
				drCount++;
			} else {// 承兑汇票
				VoucherLine drLine1 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_112101,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 应收票据-银票
				drLine1.setDebitOrCredit(BaseConsts.ONE);
				voucherLines.add(drLine1);
				drCount++;
			}
			VlReceiptRelSearchReqDto searchReqDto = new VlReceiptRelSearchReqDto();
			searchReqDto.setReceiptId(bankReceipt.getId());
			List<VlReceiptRel> vlReceiptRels = vlReceiptRelDao.queryRecustsByCon(searchReqDto);
			if (vlReceiptRels != null) {
				Iterator<VlReceiptRel> iterator = vlReceiptRels.iterator();
				while (iterator.hasNext()) {
					VlReceiptRel vlReceiptRel = iterator.next();
					VoucherLine oldEntity = voucherLineDao.queryEntityById(vlReceiptRel.getVoucherLineId());
					if (oldEntity == null) {
						throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, VoucherLineDao.class,
								vlReceiptRel.getVoucherLineId());
					}
					BigDecimal writeOffAmount = vlReceiptRel.getWriteOffAmount();
					VoucherLine crLine = convertToLineById(oldEntity, bankReceipt.getId(), writeOffAmount,
							voucher.getVoucherSummary(), BaseConsts.TWO);
					crLine.setDebitOrCredit(BaseConsts.TWO);
					voucherLines.add(crLine);

					updateBill(oldEntity, writeOffAmount); // 更新单据已收金额
					crCount++;
				}
			} // 应收核销对冲分录
			if (bankReceipt.getReceiptWay() == BaseConsts.ONE) { // 转账
				if (bankReceipt.getDiffAmount() != null
						&& DecimalUtil.gt(bankReceipt.getDiffAmount(), DecimalUtil.ZERO)) { // 存在尾差
					VoucherLine drLine2 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_660301,
							voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用-现金折扣
					drLine2.setDebitOrCredit(BaseConsts.ONE);
					drLine2.setAccountId(bankReceipt.getRecAccountNo());
					drLine2.setAmount(bankReceipt.getDiffAmount());
					voucherLines.add(drLine2);
					drCount++;
				}
			}
			if (bankReceipt.getPreRecAmount() != null
					&& DecimalUtil.gt(bankReceipt.getPreRecAmount(), DecimalUtil.ZERO)) { // 转预收
				VoucherLine crLine1 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_1223,
						voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 应收账款/外部/货款
				crLine1.setDebitOrCredit(BaseConsts.TWO);
				crLine1.setAmount(bankReceipt.getPreRecAmount());
				voucherLines.add(crLine1);
				crCount++;
			}
			if (drCount == 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "借方不存在，请核查");
			}
			if (crCount == 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "贷方不存在，请核查");
			}
			voucherDetail.setVoucher(voucher);
			voucherDetail.setVoucherLines(voucherLines);
			voucherService.createVoucherDetail(voucherDetail);
		}
	}

	private Voucher convertToVoucher(BankReceipt bankReceipt, Integer vourcherType) {
		Voucher voucher = new Voucher();
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(bankReceipt.getBusiUnit());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, bankReceipt.getBusiUnit());
		}
		BaseSubject baseSubject = cacheService.getBaseSubjectById(bankReceipt.getBusiUnit());
		if (null == baseSubject) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位为空");
		}
		voucher.setAccountBookId(accountBook.getId());
		voucher.setBusiUnit(bankReceipt.getBusiUnit());
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(baseSubject.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setReceiptId(bankReceipt.getId());
		voucher.setVoucherWord(vourcherType);
		voucher.setBillNo(bankReceipt.getReceiptNo());
		voucher.setBillType(BaseConsts.SEVEN); // 水单
		voucher.setBillDate(bankReceipt.getReceiptDate());
		voucher.setVoucherDate(bankReceipt.getReceiptDate());
		voucher.setState(BaseConsts.THREE);
		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.SEVEN) + "") + voucher.getBillNo()
						+ "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate())));

		return voucher;
	}

	private VoucherLine convertToVoucherLine(BankReceipt bankReceipt, String account_line_no, String voucherSummary,
			Integer debitOrCredit, Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		if (debitOrCredit.equals(BaseConsts.ONE)) { // 1-借方
			line.setAmount(bankReceipt.getActualReceiptAmount());
			line.setCurrencyType(bankReceipt.getActualCurrencyType());
		} else if (debitOrCredit.equals(BaseConsts.TWO)) { // 2-贷方
			line.setAmount(bankReceipt.getReceiptAmount());
			line.setCurrencyType(bankReceipt.getCurrencyType());
		}
		line.setProjectId(bankReceipt.getProjectId());
		line.setSupplierId(bankReceipt.getCustId());
		line.setCustId(bankReceipt.getCustId());
		line.setReceiptId(bankReceipt.getId());
		line.setExchangeRate(new BigDecimal("1")); // 暂存为1
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, bankReceipt.getBusiUnit(),
				bankId, type, bankReceipt.getActualCurrencyType());
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

	private VoucherLine convertToLineById(VoucherLine oldEntity, Integer receiptId, BigDecimal amount,
			String voucherSummary, Integer debitOrCredit) {
		VoucherLine line = new VoucherLine();
		line.setAmount(amount);
		line.setCurrencyType(oldEntity.getCurrencyType());
		line.setProjectId(oldEntity.getProjectId());
		line.setSupplierId(oldEntity.getSupplierId());
		line.setCustId(oldEntity.getCustId());
		line.setReceiptId(receiptId);
		line.setAccountLineId(oldEntity.getAccountLineId());
		line.setTaxRate(oldEntity.getTaxRate());
		line.setUserId(oldEntity.getUserId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

	private void updateBill(VoucherLine oldEntity, BigDecimal amountReceived) {
		switch (oldEntity.getBillType()) {
		case BaseConsts.ONE: // 费用单
			updateFee(oldEntity.getFeeId(), amountReceived);
			break;
		case BaseConsts.TWO: // 入库单
			break;
		case BaseConsts.THREE: // 出库单
			updateBillOutStore(oldEntity.getOutStoreId(), amountReceived);
			break;
		case BaseConsts.FOUR: // 付款单
			break;
		case BaseConsts.FIVE: // 收票单
			break;
		case BaseConsts.SIX: // 开票单
			break;
		case BaseConsts.SEVEN: // 水单
			break;
		}
	}

	private void updateFee(Integer feeId, BigDecimal amountReceived) {
		Fee oldEntity = feeDao.queryEntityById(feeId);
		if (oldEntity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, FeeDao.class, feeId);
		}
		Fee fee = new Fee();
		fee.setId(feeId);
		fee.setReceivedAmount(DecimalUtil.add(amountReceived, oldEntity.getReceivedAmount()));
		feeDao.updateById(fee);
	}

	private void updateBillOutStore(Integer outStoreId, BigDecimal amountReceived) {
		BillOutStore oldEntity = billOutStoreDao.queryEntityById(outStoreId);
		if (oldEntity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BillOutStoreDao.class, outStoreId);
		}
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setId(outStoreId);
		billOutStore.setReceivedAmount(DecimalUtil.add(amountReceived, oldEntity.getReceivedAmount()));
		billOutStoreDao.updateById(billOutStore);
	}

	/**
	 * 供应商退款类型的记账
	 * 
	 * @param bankReceipt
	 */
	private void supplierReceiptBookkeeping(BankReceipt bankReceipt) {
		VoucherDetail voucherDetail = new VoucherDetail();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		// 更新和校验当前出库单的金额差
		BigDecimal countWriteAmount = billOutStoreService.updateOutBillRecAmountByCon(bankReceipt);
		BigDecimal receiptAmount = bankReceipt.getActualReceiptAmount();// 水单金额
		// 封装当前凭证信息
		Voucher voucher = convertToVoucher(bankReceipt, BaseConsts.INT_11);
		if (bankReceipt.getReceiptWay().equals(BaseConsts.ONE)) {
			VoucherLine crLine1 = convertToVoucherLine(bankReceipt, null, voucher.getVoucherSummary(), BaseConsts.ONE,
					BaseConsts.TWO, bankReceipt.getRecAccountNo()); // 银行存款 水单金额
			crLine1.setDebitOrCredit(BaseConsts.ONE);
			crLine1.setAccountId(bankReceipt.getRecAccountNo());
			voucherLines.add(crLine1);
		} else {
			VoucherLine crLine5 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_112101,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 银行存款
																							// 水单金额
			crLine5.setDebitOrCredit(BaseConsts.ONE);
			crLine5.setAccountId(bankReceipt.getRecAccountNo());
			voucherLines.add(crLine5);
		}

		VoucherLine crLine = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款核销金额
		crLine.setDebitOrCredit(BaseConsts.ONE);
		crLine.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), countWriteAmount)); // 核销金额出库单的发货金额
		crLine.setAccountId(bankReceipt.getRecAccountNo());
		voucherLines.add(crLine);

		BigDecimal countRefundAmount = purchaseOrderService.updatePoLineRefundAmount(bankReceipt);
		if (DecimalUtil.lt(DecimalUtil.subtract(receiptAmount, countWriteAmount), countRefundAmount)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单金额小于铺货结算单退货金额");
		}
		VoucherLine crLine2 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款
																					// 核销金额
		crLine2.setDebitOrCredit(BaseConsts.ONE);
		crLine2.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), countRefundAmount)); // 核销金额铺货结算单金额
		crLine2.setAccountId(bankReceipt.getRecAccountNo());
		voucherLines.add(crLine2);

		// 应收的操作
		BigDecimal countWriteOffAmount = BigDecimal.ZERO;
		List<RecReceiptRel> receiptRels = recReceiptRelDao.getRecReceiptRelByReceiptId(bankReceipt.getId());
		if (!CollectionUtils.isEmpty(receiptRels)) {
			for (RecReceiptRel recReceiptRel : receiptRels) {
				BigDecimal writeOffAmount = recReceiptRel.getWriteOffAmount();
				countWriteOffAmount = DecimalUtil.add(countWriteOffAmount, writeOffAmount);
			}
			RecReceiptRelReqDto recReceiptRelReqDto = new RecReceiptRelReqDto();
			recReceiptRelReqDto.setRelList(receiptRels);
			recReceiptRelReqDto.setReceiptId(bankReceipt.getId());
			recReceiptRelReqDto.setWtCheckWriteAmount(BaseConsts.ONE);
			recReceiptRelService.createRecReceiptRel(recReceiptRelReqDto);
			// 应收凭证
			VoucherLine crLine3 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_1223,
					voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 应收账款/外部/货款
			crLine3.setDebitOrCredit(BaseConsts.TWO);
			crLine3.setAmount(countWriteOffAmount);
			crLine3.setAccountId(bankReceipt.getRecAccountNo());
			voucherLines.add(crLine3);
		}
		BigDecimal sendReceiptAmount = DecimalUtil.subtract(receiptAmount,
				DecimalUtil.add(countWriteAmount, DecimalUtil.add(countRefundAmount, countWriteOffAmount)));
		if (DecimalUtil.gt(sendReceiptAmount, BigDecimal.ZERO)) {// 金额大于0,将预收金额转换为新信息水单预收退款类型
			// 余额生成凭证
			VoucherLine crLine4 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款
			crLine4.setDebitOrCredit(BaseConsts.ONE);
			crLine4.setAmount(DecimalUtil.multiply(bankReceipt.getPreRecAmount(), new BigDecimal("-1")));
			crLine4.setAccountId(bankReceipt.getRecAccountNo());
			voucherLines.add(crLine4);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail);
	}

	/**
	 * 水单内部类型的记账
	 * 
	 * @param bankReceipt
	 */
	private void operatReceiptBookkeeping(BankReceipt bankReceipt) {
		VoucherDetail voucherDetail = new VoucherDetail();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		Voucher voucher = convertToVoucher(bankReceipt, BaseConsts.SIX);
		VoucherLine crLine1 = convertToVoucherLine(bankReceipt, null, voucher.getVoucherSummary(), BaseConsts.ONE,
				BaseConsts.TWO, bankReceipt.getRecAccountNo()); // 银行存款 水单金额
		crLine1.setDebitOrCredit(BaseConsts.ONE);// 借方
		crLine1.setAccountId(bankReceipt.getRecAccountNo());
		voucherLines.add(crLine1);

		VoucherLine crLine = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_1159,
				voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 其他应收款-美元-客户往来核算项
		crLine.setDebitOrCredit(BaseConsts.TWO);// 贷方
		crLine.setAccountId(bankReceipt.getRecAccountNo());
		BigDecimal receiptAmount = DecimalUtil.add(bankReceipt.getReceiptAmount(), bankReceipt.getActualDiffAmount());// 水单的金额
		crLine.setAmount(receiptAmount);
		if (bankReceipt.getReceiptWay() == BaseConsts.ONE) { // 转账
			if (bankReceipt.getDiffAmount() != null && DecimalUtil.gt(bankReceipt.getDiffAmount(), DecimalUtil.ZERO)) { // 存在尾差
				VoucherLine drLine2 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_660301,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用-现金折扣
				drLine2.setDebitOrCredit(BaseConsts.ONE);
				drLine2.setAccountId(bankReceipt.getRecAccountNo());
				drLine2.setAmount(bankReceipt.getDiffAmount());
				voucherLines.add(drLine2);
			}
		}
		voucherLines.add(crLine);
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail);
	}

	/**
	 * 预收退款的凭证记录
	 * 
	 * @param bankReceipt
	 */
	public void preReceiptReturnBookkeeping(BankReceipt bankReceipt) {

		VoucherDetail voucherDetail = new VoucherDetail();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		// 更新和校验当前出库单的金额差
		BigDecimal countWriteAmount = billOutStoreService.updateOutBillRecAmountByCon(bankReceipt);
		BigDecimal receiptAmount = bankReceipt.getReceiptAmount();// 水单金额
		// 封装当前凭证信息
		Voucher voucher = convertToVoucher(bankReceipt, BaseConsts.INT_11);
		VoucherLine crLine1 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null);
		crLine1.setDebitOrCredit(BaseConsts.ONE);
		crLine1.setAccountId(bankReceipt.getRecAccountNo());
		crLine1.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), bankReceipt.getReceiptAmount()));
		voucherLines.add(crLine1);

		VoucherLine crLine = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款核销金额
		crLine.setDebitOrCredit(BaseConsts.ONE);
		crLine.setAmount(countWriteAmount); // 核销金额出库单的发货金额
		crLine.setAccountId(bankReceipt.getRecAccountNo());
		voucherLines.add(crLine);

		BigDecimal countRefundAmount = purchaseOrderService.updatePoLineRefundAmount(bankReceipt);
		if (DecimalUtil.lt(DecimalUtil.subtract(receiptAmount, countWriteAmount), countRefundAmount)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单金额小于铺货结算单退货金额");
		}
		VoucherLine crLine2 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款
																					// 核销金额
		crLine2.setDebitOrCredit(BaseConsts.ONE);
		crLine2.setAmount(countRefundAmount); // 核销金额铺货结算单金额
		crLine2.setAccountId(bankReceipt.getRecAccountNo());
		voucherLines.add(crLine2);

		// 应收的操作
		BigDecimal countWriteOffAmount = BigDecimal.ZERO;
		List<RecReceiptRel> receiptRels = recReceiptRelDao.getRecReceiptRelByReceiptId(bankReceipt.getId());
		if (!CollectionUtils.isEmpty(receiptRels)) {
			for (RecReceiptRel recReceiptRel : receiptRels) {
				BigDecimal writeOffAmount = recReceiptRel.getWriteOffAmount();
				countWriteOffAmount = DecimalUtil.add(countWriteOffAmount, writeOffAmount);
			}
			RecReceiptRelReqDto recReceiptRelReqDto = new RecReceiptRelReqDto();
			recReceiptRelReqDto.setRelList(receiptRels);
			recReceiptRelReqDto.setReceiptId(bankReceipt.getId());
			recReceiptRelReqDto.setWtCheckWriteAmount(BaseConsts.ONE);
			recReceiptRelService.createRecReceiptRel(recReceiptRelReqDto);
			VoucherLine crLine3 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_1223,
					voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 应收账款/外部/货款
			crLine3.setDebitOrCredit(BaseConsts.TWO);
			crLine3.setAmount(countWriteOffAmount);
			crLine3.setAccountId(bankReceipt.getRecAccountNo());
			voucherLines.add(crLine3);
		}
		BigDecimal sendReceiptAmount = DecimalUtil.subtract(receiptAmount,
				DecimalUtil.add(countWriteAmount, DecimalUtil.add(countRefundAmount, countWriteOffAmount)));
		if (DecimalUtil.gt(sendReceiptAmount, BigDecimal.ZERO)) {// 金额大于0,将预收金额转换为新信息水单预收退款类型
			// 余额生成凭证
			VoucherLine crLine4 = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_220201,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款
			crLine4.setDebitOrCredit(BaseConsts.ONE);
			crLine4.setAmount(bankReceipt.getPreRecAmount());
			crLine4.setAccountId(bankReceipt.getRecAccountNo());
			voucherLines.add(crLine4);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail);

	}
}
