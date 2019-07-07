package com.scfs.service.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.common.consts.AccountNoConsts;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountBookService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ExchangeBillExpireJob.java
 *  Description:
 *  TODO
 *  Date,			Who,				
 *  2016年
 *  11月25日			Administrator
 *
 * </pre>
 */

public class ExchangeBillExpireJob {
	@Autowired
	PayOrderDao payOrderDao;

	@Autowired
	BankReceiptDao bankReceiptDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	VoucherService voucherService;

	@Autowired
	AccountBookService accountBookService;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private AccountLineService accountLineService;

	private final static Logger LOGGER = LoggerFactory.getLogger(ExchangeBillExpireJob.class);

	public void execute() throws Exception {

		try {
			List<PayOrder> payOrders = payOrderDao.queryExpireResults(); // 付款承兑汇票到期
			if (payOrders != null) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[应付承兑汇票到期自动记账定时任务]开始时间：" + new Date());

				for (PayOrder payOrder : payOrders) {
					payBookkeeping(payOrder);
				}

				long endTime = System.currentTimeMillis();
				LOGGER.info("[应付承兑汇票到期自动记账定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
			}

			List<BankReceipt> bankReceipts = bankReceiptDao.queryExpireResults();

			if (bankReceipts != null) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[应收承兑汇票到期自动记账定时任务]开始时间：" + new Date());

				for (BankReceipt bankReceipt : bankReceipts) {
					receiptBookkeeping(bankReceipt);
				}

				long endTime = System.currentTimeMillis();
				LOGGER.info("[应收承兑汇票到期自动记账定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
			}
		} catch (Exception e) {
			LOGGER.error("承兑汇票到期自动记账异常：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		}
	}

	private void payBookkeeping(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.INT_11);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_112101,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 应付票据/银票
		drLine.setDebitOrCredit(BaseConsts.ONE);
		drLine.setAccountId(payOrder.getPaymentAccount());

		VoucherLine crLine = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAccountId(payOrder.getPaymentAccount());

		voucherLines.add(drLine);
		voucherLines.add(crLine);

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	private void receiptBookkeeping(BankReceipt bankReceipt) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(bankReceipt, BaseConsts.INT_11);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(bankReceipt, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, bankReceipt.getRecAccountNo()); // 银行存款
		drLine.setDebitOrCredit(BaseConsts.TWO);
		drLine.setAccountId(bankReceipt.getRecAccountNo());

		VoucherLine crLine = convertToVoucherLine(bankReceipt, AccountNoConsts.ACCOUNT_NO_112101,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 应收票据-银票
		crLine.setDebitOrCredit(BaseConsts.ONE);
		crLine.setAccountId(bankReceipt.getRecAccountNo());

		voucherLines.add(drLine);
		voucherLines.add(crLine);

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	private Voucher convertToVoucher(PayOrder payOrder, Integer vourcherType) {
		Voucher voucher = new Voucher();
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(payOrder.getBusiUnit());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, payOrder.getBusiUnit());
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(payOrder.getBusiUnit());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, payOrder.getBusiUnit());
		}
		voucher.setAccountBookId(accountBook.getId());
		voucher.setBusiUnit(payOrder.getBusiUnit());
		voucher.setPayId(payOrder.getId());
		voucher.setVoucherWord(vourcherType);
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setBillType(BaseConsts.FOUR); // 付款单
		voucher.setBillDate(payOrder.getConfirmorAt());
		voucher.setVoucherDate(payOrder.getConfirmorAt());
		voucher.setBillNo(payOrder.getPayNo());
		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.FOUR) + "") + voucher.getBillNo()
						+ "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate())));

		voucher.setState(BaseConsts.THREE);
		voucher.setCreatorId(BaseConsts.ONE);
		voucher.setCreator(BaseConsts.SYSTEM_ROLE_NAME);

		return voucher;
	}

	private VoucherLine convertToVoucherLine(PayOrder payOrder, String account_line_no, String voucherSummary,
			Integer debitOrCredit, Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		if (debitOrCredit.equals(BaseConsts.ONE)) { // 1-借方
			line.setAmount(payOrder.getPayAmount());
			line.setCurrencyType(payOrder.getCurrnecyType());
		} else if (debitOrCredit.equals(BaseConsts.TWO)) { // 2-贷方
			line.setAmount(payOrder.getRealPayAmount());
			line.setCurrencyType(payOrder.getRealCurrencyType());
		}
		// line.setAmount(payOrder.getPayAmount());
		// line.setCurrencyType(payOrder.getCurrnecyType());
		line.setProjectId(payOrder.getProjectId());
		line.setSupplierId(payOrder.getPayee());
		line.setCustId(payOrder.getPayee());
		line.setFeeId(payOrder.getId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, payOrder.getBusiUnit(), bankId,
				type, payOrder.getRealCurrencyType());
		// AccountLine accountLine =
		// cacheService.getAccountLineByNo(account_line_no);
		line.setAccountLineId(accountLine.getId());
		line.setCreatorId(BaseConsts.ONE);
		line.setCreator("管理员");
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

	private Voucher convertToVoucher(BankReceipt bankReceipt, Integer vourcherType) {
		Voucher voucher = new Voucher();
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(bankReceipt.getBusiUnit());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, bankReceipt.getBusiUnit());
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(bankReceipt.getBusiUnit());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, bankReceipt.getBusiUnit());
		}
		voucher.setAccountBookId(accountBook.getId());
		voucher.setBusiUnit(bankReceipt.getBusiUnit());
		voucher.setReceiptId(bankReceipt.getId());
		voucher.setVoucherWord(vourcherType);
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setBillNo(bankReceipt.getReceiptNo());
		voucher.setBillType(BaseConsts.SEVEN); // 水单
		voucher.setBillDate(bankReceipt.getReceiptDate());
		voucher.setVoucherDate(bankReceipt.getReceiptDate());
		voucher.setState(BaseConsts.THREE);
		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.SEVEN) + "") + voucher.getBillNo()
						+ "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate())));
		voucher.setCreatorId(BaseConsts.ONE);
		voucher.setCreator(BaseConsts.SYSTEM_ROLE_NAME);
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
		// line.setAmount(bankReceipt.getReceiptAmount());
		line.setProjectId(bankReceipt.getProjectId());
		line.setSupplierId(bankReceipt.getCustId());
		line.setCustId(bankReceipt.getCustId());
		// line.setCurrencyType(bankReceipt.getCurrencyType());
		line.setReceiptId(bankReceipt.getId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, bankReceipt.getBusiUnit(),
				bankId, type, bankReceipt.getCurrencyType());
		// AccountLine accountLine =
		// cacheService.getAccountLineByNo(account_line_no);
		line.setAccountLineId(accountLine.getId());
		line.setCreatorId(BaseConsts.ONE);
		line.setCreator("管理员");
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

}
