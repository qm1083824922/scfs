package com.scfs.service.bookkeeping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import com.scfs.common.consts.AccountNoConsts;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fi.AccountLineDao;
import com.scfs.dao.invoice.InvoiceApplyDao;
import com.scfs.dao.invoice.InvoiceDtlInfoDao;
import com.scfs.dao.invoice.InvoiceInfoDao;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.invoice.dto.resp.InvoiceSumTaxGroup;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceInfo;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: InvoiceBookkeepingService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月7日				Administrator
 *
 * </pre>
 */

@Service
public class InvoiceBookkeepingService {
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private InvoiceInfoDao invoiceInfoDao;
	@Autowired
	private InvoiceApplyDao invoiceApplyDao;
	@Autowired
	private InvoiceDtlInfoDao invoiceDtlInfoDao;
	@Autowired
	private AccountLineService accountLineService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private AccountLineDao accountLineDao;

	public void invoiceBookkeeping(Integer invoiceId) {
		InvoiceInfo invoiceInfo = invoiceInfoDao.queryEntityById(invoiceId);
		if (invoiceInfo == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, InvoiceInfoDao.class, invoiceId);
		}
		InvoiceApplyManager invoiceApplyManager = invoiceApplyDao.queryEntityById(invoiceInfo.getInvoiceApplyId());
		if (invoiceApplyManager == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, InvoiceApplyDao.class,
					invoiceInfo.getInvoiceApplyId());
		}
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(invoiceInfo, invoiceApplyManager, BaseConsts.NINE);
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) {
			List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
			List<InvoiceSumTaxGroup> list = invoiceDtlInfoDao.querySumTaxGroupByInvoiceId(invoiceId);
			if (list != null) {
				List<VoucherLine> vatList = Lists.newArrayList();
				for (InvoiceSumTaxGroup item : list) {
					BigDecimal exRateAmount = DecimalUtil
							.formatScale2(DecimalUtil.subtract(item.getExRateAmount(), item.getDiscountExRateAmount()));
					VoucherLine crLine1 = convertToVoucherLine(invoiceInfo, invoiceApplyManager,
							AccountNoConsts.ACCOUNT_NO_660198, voucher.getVoucherSummary(), BaseConsts.THREE, null,
							item.getTaxRate()); // 主营业务收入\开票备查\已开票
					crLine1.setAmount(exRateAmount);
					crLine1.setDebitOrCredit(BaseConsts.TWO);
					crLine1.setTaxRate(item.getTaxRate());

					VoucherLine crLine2 = convertToVoucherLine(invoiceInfo, invoiceApplyManager,
							AccountNoConsts.ACCOUNT_NO_660198, voucher.getVoucherSummary(), BaseConsts.THREE, null,
							item.getTaxRate()); // 主营业务收入\开票备查\待开票
					crLine2.setAmount(
							DecimalUtil.formatScale2(DecimalUtil.multiply(exRateAmount, new BigDecimal("-1"))));
					crLine2.setDebitOrCredit(BaseConsts.TWO);
					crLine2.setTaxRate(item.getTaxRate());

					BigDecimal rateAmount = DecimalUtil
							.formatScale2(DecimalUtil.subtract(item.getRateAmount(), item.getDiscountRateAmount()));
					VoucherLine drLine = convertToVoucherLine(invoiceInfo, invoiceApplyManager,
							AccountNoConsts.ACCOUNT_NO_122320, voucher.getVoucherSummary(), BaseConsts.THREE, null,
							item.getTaxRate()); // 预提应交税金-增值税-销项税额
					drLine.setAmount(rateAmount);
					drLine.setDebitOrCredit(BaseConsts.ONE);
					drLine.setTaxRate(item.getTaxRate());

					VoucherLine crLine = convertToVoucherLine(invoiceInfo, invoiceApplyManager,
							AccountNoConsts.ACCOUNT_NO_22210101, voucher.getVoucherSummary(), BaseConsts.THREE, null,
							item.getTaxRate()); // 应交税费-应交增值税-销项税额
					crLine.setAmount(rateAmount);
					crLine.setDebitOrCredit(BaseConsts.TWO);
					crLine.setTaxRate(item.getTaxRate());

					voucherLines.add(crLine1);
					voucherLines.add(crLine2);
					voucherLines.add(drLine);
					voucherLines.add(crLine);
					vatList.add(crLine);
				}

				voucherDetail.setVoucher(voucher);
				voucherDetail.setVoucherLines(voucherLines);
				voucherService.createVoucherDetail(voucherDetail);
			}
		}

	}

	private Voucher convertToVoucher(InvoiceInfo invoiceInfo, InvoiceApplyManager invoiceApplyManager,
			Integer vourcherType) {
		Voucher voucher = new Voucher();
		if (invoiceApplyManager == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, InvoiceApplyDao.class,
					invoiceInfo.getInvoiceApplyId());
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(invoiceApplyManager.getBusinessUnitId());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class,
					invoiceApplyManager.getBusinessUnitId());
		}
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(busiUnit.getId());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, busiUnit.getId());
		}
		voucher.setAccountBookId(accountBook.getId());
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setBusiUnit(busiUnit.getId());
		voucher.setProvideInvoiceId(invoiceInfo.getId());
		voucher.setVoucherWord(vourcherType);
		voucher.setBillNo(invoiceInfo.getInvoiceApplyNo());
		voucher.setBillType(BaseConsts.SIX); // 开票单
		voucher.setBillDate(invoiceInfo.getInvoiceDate()); // 单据日期
		voucher.setVoucherDate(invoiceInfo.getInvoiceDate()); // 凭证日期
		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (voucher.getBillType()) + "")
						+ voucher.getBillNo() + "/单据日期"
						+ (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getVoucherDate())) + "/发票编号"
						+ invoiceInfo.getInvoiceNo() + "/发票号" + invoiceInfo.getInvoiceCode());
		voucher.setState(BaseConsts.THREE);

		return voucher;
	}

	private VoucherLine convertToVoucherLine(InvoiceInfo invoiceInfo, InvoiceApplyManager invoiceApplyManager,
			String accountLineNo, String voucherSummary, Integer type, Integer bankId, BigDecimal taxRate) {
		VoucherLine line = new VoucherLine();
		line.setProjectId(invoiceApplyManager.getProjectId());
		line.setSupplierId(invoiceApplyManager.getCustomerId());
		line.setCustId(invoiceApplyManager.getCustomerId());
		line.setCurrencyType(BaseConsts.ONE); // 只能是人民币
		line.setProvideInvoiceId(invoiceInfo.getId());
		AccountLine accountLine = null;
		if (AccountNoConsts.ACCOUNT_NO_22210101.equals(accountLineNo)) {
			if (null == taxRate) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "未找到应交税费\\应交增值税\\销项税科目");
			}
			accountLine = accountLineService.queryAccountLine(accountLineNo, invoiceApplyManager.getBusinessUnitId(),
					bankId, type, null);
			String accountLineName = accountLine.getAccountLineName();
			String taxRateStr = DecimalUtil.multiply(taxRate, new BigDecimal("100")).setScale(0).toString() + "%";
			accountLineName = accountLineName + taxRateStr;
			accountLine = accountLineDao.queryAccountLineByNoAndName(accountLineNo, accountLineName);
			if (null == accountLine) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "未找到应交税费\\应交增值税\\销项税科目");
			}
		} else {
			accountLine = accountLineService.queryAccountLine(accountLineNo, invoiceApplyManager.getBusinessUnitId(),
					bankId, type, null);
		}
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

}
