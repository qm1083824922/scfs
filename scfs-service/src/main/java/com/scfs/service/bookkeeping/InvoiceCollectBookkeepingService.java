package com.scfs.service.bookkeeping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.scfs.dao.invoice.InvoiceCollectApproveDao;
import com.scfs.dao.invoice.InvoiceCollectDao;
import com.scfs.dao.invoice.InvoiceCollectFeeDao;
import com.scfs.dao.invoice.InvoiceCollectPoDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.invoice.dto.resp.TaxRateSum;
import com.scfs.domain.invoice.entity.InvoiceCollect;
import com.scfs.domain.invoice.entity.InvoiceCollectApprove;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: InvoiceCollectBookkeepingService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月3日				Administrator
 *
 * </pre>
 */

@Service
public class InvoiceCollectBookkeepingService {
	@Autowired
	InvoiceCollectDao invoiceCollectDao;

	@Autowired
	InvoiceCollectFeeDao invoiceCollectFeeDao;

	@Autowired
	InvoiceCollectPoDao invoiceCollectPoDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	VoucherService voucherService;

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private AccountLineService accountLineService;
	@Autowired
	InvoiceCollectApproveDao invoiceCollectApproveDao;

	public void collectBookkeeping(Integer id) {
		collectBookkeeping(id, null);
	}

	public void collectBookkeeping(Integer id, Integer invoiceCollectApproveId) {
		InvoiceCollect invoiceCollect = invoiceCollectDao.queryEntityById(id);
		InvoiceCollectApprove invoiceCollectApprove = null;
		if (null != invoiceCollectApproveId) {
			invoiceCollectApprove = invoiceCollectApproveDao.queryEntityById(invoiceCollectApproveId);
		}
		if (invoiceCollect == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, InvoiceCollectDao.class, id);
		}
		if (invoiceCollect.getInvoiceType().equals(BaseConsts.TWO)) { // 增值税普票不记账
			if (null == invoiceCollectApproveId && invoiceCollect.getState().equals(BaseConsts.THREE)) {
				auditBookkeeping(invoiceCollect);
			} else if (null != invoiceCollectApproveId && (invoiceCollect.getState().equals(BaseConsts.THREE)
					|| invoiceCollect.getState().equals(BaseConsts.FOUR))) {
				approveBookkeeping(invoiceCollect, invoiceCollectApprove);
			}
		}
	}

	private void auditBookkeeping(InvoiceCollect invoiceCollect) {
		if (DecimalUtil.gt(invoiceCollect.getInvoiceAmount(), DecimalUtil.ZERO)) {
			VoucherDetail voucherDetail = new VoucherDetail();
			Voucher voucher = convertToVoucher(invoiceCollect, BaseConsts.SEVEN);
			List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
			Integer isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
			if (isHome.equals(BaseConsts.ONE)) {
				List<TaxRateSum> list = new ArrayList<TaxRateSum>();
				if (invoiceCollect.getBillType().equals(BaseConsts.ONE)) {
					list = invoiceCollectPoDao.queryRateSumByInvoiceCollectId(invoiceCollect.getId());
				} else if (invoiceCollect.getBillType().equals(BaseConsts.TWO)) {
					list = invoiceCollectFeeDao.queryRateSumByInvoiceCollectId(invoiceCollect.getId());
				}
				for (TaxRateSum item : list) {
					BigDecimal taxRate = item.getTaxRate();
					BigDecimal amount = DecimalUtil.formatScale2(item.getTaxRateSum());

					VoucherLine drLine = convertToVoucherLine(invoiceCollect, AccountNoConsts.ACCOUNT_NO_22210105,
							voucher.getVoucherSummary(), BaseConsts.THREE, null);
					drLine.setDebitOrCredit(BaseConsts.ONE);
					drLine.setAmount(amount);
					drLine.setTaxRate(taxRate);

					VoucherLine crLine = convertToVoucherLine(invoiceCollect, AccountNoConsts.ACCOUNT_NO_220205,
							voucher.getVoucherSummary(), BaseConsts.THREE, null);
					crLine.setDebitOrCredit(BaseConsts.TWO);
					crLine.setAmount(amount);
					crLine.setTaxRate(taxRate);
					voucherLines.add(drLine);
					voucherLines.add(crLine);
				}
				voucherDetail.setVoucher(voucher);
				voucherDetail.setVoucherLines(voucherLines);
				voucherService.createVoucherDetail(voucherDetail);
			}
		}
	}

	public void approveBookkeeping(InvoiceCollect invoiceCollect, InvoiceCollectApprove invoiceCollectApprove) {
		VoucherDetail voucherDetail = new VoucherDetail();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		Voucher voucher = convertToVoucher(invoiceCollect, invoiceCollectApprove, BaseConsts.SEVEN);
		Integer isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome.equals(BaseConsts.ONE)) { // 只有国内需要记账
			BigDecimal taxRate = invoiceCollect.getInvoiceTaxRate();
			BigDecimal amount = invoiceCollectApprove.getApproveAmount();

			BigDecimal rateAlgorithm = DecimalUtil.divide(taxRate,
					DecimalUtil.add(taxRate, new BigDecimal(String.valueOf(BaseConsts.ONE))));
			BigDecimal rateAmount = DecimalUtil.multiply(amount, rateAlgorithm);// 凭证税额

			if (DecimalUtil.gt(invoiceCollect.getInvoiceAmount(), DecimalUtil.ZERO)) {
				VoucherLine drLine = convertToVoucherLine(invoiceCollect, AccountNoConsts.ACCOUNT_NO_222102,
						voucher.getVoucherSummary(), BaseConsts.THREE, null);
				drLine.setDebitOrCredit(BaseConsts.ONE);
				drLine.setAmount(rateAmount);
				drLine.setTaxRate(taxRate);

				VoucherLine crLine = convertToVoucherLine(invoiceCollect, AccountNoConsts.ACCOUNT_NO_22210105,
						voucher.getVoucherSummary(), BaseConsts.THREE, null);
				crLine.setDebitOrCredit(BaseConsts.TWO);
				crLine.setAmount(rateAmount);
				crLine.setTaxRate(taxRate);
				if (null != invoiceCollectApprove) {
					crLine.setInvoiceCollectApproveId(invoiceCollectApprove.getId());
				}
				voucherLines.add(drLine);
				voucherLines.add(crLine);
			} else if (DecimalUtil.lt(invoiceCollect.getInvoiceAmount(), DecimalUtil.ZERO)) {
				VoucherLine drLine = convertToVoucherLine(invoiceCollect, AccountNoConsts.ACCOUNT_NO_22210108,
						voucher.getVoucherSummary(), BaseConsts.THREE, null);
				drLine.setDebitOrCredit(BaseConsts.TWO);
				drLine.setAmount(rateAmount);
				drLine.setTaxRate(taxRate);
				if (null != invoiceCollectApprove) {
					drLine.setInvoiceCollectApproveId(invoiceCollectApprove.getId());
				}
				VoucherLine crLine = convertToVoucherLine(invoiceCollect, AccountNoConsts.ACCOUNT_NO_22210102,
						voucher.getVoucherSummary(), BaseConsts.THREE, null);
				crLine.setDebitOrCredit(BaseConsts.TWO);
				crLine.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), rateAmount));
				crLine.setTaxRate(taxRate);

				voucherLines.add(drLine);
				voucherLines.add(crLine);
			}

			voucherDetail.setVoucher(voucher);
			voucherDetail.setVoucherLines(voucherLines);
			voucherService.createVoucherDetail(voucherDetail);
		}
	}

	private Voucher convertToVoucher(InvoiceCollect invoiceCollect, Integer vourcherType) {
		return convertToVoucher(invoiceCollect, null, vourcherType);
	}

	private Voucher convertToVoucher(InvoiceCollect invoiceCollect, InvoiceCollectApprove invoiceCollectApprove,
			Integer vourcherType) {
		Voucher voucher = new Voucher();
		BaseProject project = cacheService.getProjectById(invoiceCollect.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, invoiceCollect.getProjectId());
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(invoiceCollect.getBusinessUnit());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, invoiceCollect.getBusinessUnit());
		}
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(busiUnit.getId());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, busiUnit.getId());
		}
		voucher.setAccountBookId(accountBook.getId());
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setBusiUnit(busiUnit.getId());
		voucher.setAcceptInvoiceId(invoiceCollect.getId());
		voucher.setVoucherWord(vourcherType);
		voucher.setBillNo(invoiceCollect.getApplyNo());
		voucher.setBillType(BaseConsts.FIVE); // 收票单

		if (null == invoiceCollectApprove && invoiceCollect.getState().equals(BaseConsts.THREE)) {
			voucher.setBillDate(new Date()); // 审核通过日期(当前记账日期)
			voucher.setVoucherDate(new Date()); // 凭证日期
		} else if (null != invoiceCollectApprove && (invoiceCollect.getState().equals(BaseConsts.THREE)
				|| invoiceCollect.getState().equals(BaseConsts.FOUR))) {
			voucher.setBillDate(invoiceCollectApprove.getApproveDate()); // 认证日期
			voucher.setVoucherDate(invoiceCollectApprove.getApproveDate()); // 凭证日期
		}

		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.FIVE) + "") + voucher.getBillNo()
						+ "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate()) + "/发票号"
								+ invoiceCollect.getInvoiceNo()));
		voucher.setState(BaseConsts.THREE);
		return voucher;
	}

	private VoucherLine convertToVoucherLine(InvoiceCollect invoiceCollect, String accountLineNo, String voucherSummary,
			Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		line.setProjectId(invoiceCollect.getProjectId());
		line.setSupplierId(invoiceCollect.getSupplierId());
		line.setCustId(invoiceCollect.getSupplierId());
		line.setCurrencyType(BaseConsts.ONE); // 人民币
		line.setAcceptInvoiceId(invoiceCollect.getId());
		AccountLine accountLine = accountLineService.queryAccountLine(accountLineNo, invoiceCollect.getBusinessUnit(),
				bankId, type, null);
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}
}
