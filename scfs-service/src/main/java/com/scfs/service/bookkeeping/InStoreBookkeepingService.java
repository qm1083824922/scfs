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
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtlTaxGroupSum;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillInStoreDtlService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: InStoreBookkeepingService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */

@Service
public class InStoreBookkeepingService {
	@Autowired
	BillInStoreDao billInStoreDao;

	@Autowired
	BillInStoreDtlService billInStoreDtlService;

	@Autowired
	VoucherService voucherService;

	@Autowired
	CacheService cacheService;
	@Autowired
	private AccountLineService accountLineService;

	@Autowired
	private SequenceService sequenceService;

	public void inStoreBookkeeping(int id) {
		BillInStoreSearchReqDto req = new BillInStoreSearchReqDto();
		req.setId(id);
		BillInStore billInStore = billInStoreDao.queryEntityById(req);
		VoucherDetail voucherDetail = new VoucherDetail();

		Voucher voucher = convertToVoucher(billInStore, BaseConsts.SEVEN);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) { // 国内
			List<BillInStoreDtlTaxGroupSum> groupSums = billInStoreDtlService.queryTaxGroupSumByBillInStoreId(id);
			for (BillInStoreDtlTaxGroupSum item : groupSums) {
				BigDecimal amount = DecimalUtil.formatScale2(item.getReceiveAmount());
				BigDecimal taxRate = item.getTaxRate();
				VoucherLine drLine1 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_1201,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
				drLine1.setDebitOrCredit(BaseConsts.ONE); // 借
				drLine1.setTaxRate(taxRate);
				drLine1.setAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(amount, DecimalUtil.add(taxRate, new BigDecimal("1")))));

				VoucherLine drLine2 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_220205,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 预提应交税金-增值税-进项税额
				drLine2.setDebitOrCredit(BaseConsts.ONE); // 借
				drLine2.setTaxRate(taxRate);
				drLine2.setAmount(DecimalUtil.subtract(amount, drLine1.getAmount()));

				voucherLines.add(drLine1);
				voucherLines.add(drLine2);
			}

		} else if (isHome == BaseConsts.ZERO) { // 香港
			BigDecimal receiveAmount = DecimalUtil.formatScale2(billInStoreDtlService.querySumByBillInStoreId(id));
			VoucherLine drLine3 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
			drLine3.setDebitOrCredit(BaseConsts.ONE);
			drLine3.setAmount(receiveAmount);
			drLine3.setTaxRate(DecimalUtil.ZERO);
			voucherLines.add(drLine3);
		}

		VoucherLine crLine = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, null); // 应付账款/外部/货款
		crLine.setDebitOrCredit(BaseConsts.TWO); // 贷

		voucherLines.add(crLine);
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	/**
	 * 销售退货 与出库单记账保持一致，金额为负数
	 * 
	 * @param id
	 * @return
	 */
	public Integer outStoreBookkeeping(int id) {
		BillInStoreSearchReqDto req = new BillInStoreSearchReqDto();
		req.setId(id);
		BillInStore billInStore = billInStoreDao.queryEntityById(req);
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(billInStore, BaseConsts.EIGHT);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_1223,
				voucher.getVoucherSummary(), BaseConsts.ONE, null); // 应收账款/外部/货款
		drLine.setAmount(drLine.getAmount().multiply(new BigDecimal("-1")));
		drLine.setDebitOrCredit(BaseConsts.ONE); // 借
		voucherLines.add(drLine);

		VoucherLine drLine4 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_5100,
				voucher.getVoucherSummary(), BaseConsts.THREE, null); // 业绩量
		drLine4.setDebitOrCredit(BaseConsts.ONE); // 借
		drLine4.setAmount(DecimalUtil.multiply(new BigDecimal("-1"),
				billInStore.getReceiveAmount() == null ? BigDecimal.ZERO : billInStore.getReceiveAmount()));
		voucherLines.add(drLine4);

		VoucherLine drLine5 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_5100,
				voucher.getVoucherSummary(), BaseConsts.THREE, null); // 业绩量
		drLine5.setDebitOrCredit(BaseConsts.TWO); // 贷
		drLine5.setAmount(DecimalUtil.multiply(new BigDecimal("-1"),
				billInStore.getReceiveAmount() == null ? BigDecimal.ZERO : billInStore.getReceiveAmount()));
		voucherLines.add(drLine5);

		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) { // 国内
			List<BillInStoreDtlTaxGroupSum> outGroupSum = billInStoreDtlService.queryTaxGroupSumByBillInStoreId(id);
			for (BillInStoreDtlTaxGroupSum item : outGroupSum) {
				BigDecimal reciveAmount = item.getReceiveAmount().multiply(new BigDecimal("-1"));
				BigDecimal taxRate = item.getTaxRate();
				BigDecimal amount = DecimalUtil
						.formatScale2(DecimalUtil.divide(reciveAmount, DecimalUtil.add(taxRate, new BigDecimal("1"))));
				VoucherLine crLine1 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_6001,
						voucher.getVoucherSummary(), BaseConsts.ONE, null); // 主营业务收入/销售收入/外部销售
				crLine1.setAmount(amount);
				crLine1.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine1.setTaxRate(taxRate);
				crLine1.setAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(reciveAmount, DecimalUtil.add(taxRate, new BigDecimal("1")))));

				VoucherLine crLine2 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_122320,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 预提应交税金-增值税-进项税额
				crLine2.setAmount(amount.multiply(taxRate));
				crLine2.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine2.setTaxRate(taxRate);
				crLine2.setAmount(DecimalUtil.subtract(reciveAmount, crLine1.getAmount()));

				voucherLines.add(crLine1);
				voucherLines.add(crLine2);

			}
			for (BillInStoreDtlTaxGroupSum item : outGroupSum) {
				BigDecimal taxRate = item.getTaxRate();
				BigDecimal cost = item.getCostPrice().multiply(new BigDecimal("-1"));
				BigDecimal amount = DecimalUtil
						.formatScale2(DecimalUtil.divide(cost, DecimalUtil.add(taxRate, new BigDecimal("1"))));

				VoucherLine drLine3 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_610801,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本/销售成本/外部销售
				drLine3.setDebitOrCredit(BaseConsts.ONE); // 借
				drLine3.setTaxRate(taxRate);
				drLine3.setAmount(amount);

				VoucherLine crLine3 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_1201,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
				crLine3.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine3.setTaxRate(taxRate);
				crLine3.setAmount(amount);

				voucherLines.add(drLine3);
				voucherLines.add(crLine3);
			}

		} else if (isHome == BaseConsts.ZERO) { // 香港
			BigDecimal recivePriceAmount = DecimalUtil.formatScale2(billInStoreDtlService.querySumByBillInStoreId(id))
					.multiply(new BigDecimal(-1));
			BigDecimal costPrice = DecimalUtil.formatScale2(billInStoreDtlService.querySumCostAmountByBillInStoreId(id))
					.multiply(new BigDecimal(-1));
			VoucherLine f_crLine1 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_6001,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务收入/销售收入/外部销售
			f_crLine1.setDebitOrCredit(BaseConsts.TWO);
			f_crLine1.setAmount(recivePriceAmount);
			f_crLine1.setTaxRate(DecimalUtil.ZERO);

			VoucherLine f_drLine = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_610801,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本/销售成本/外部销售
			f_drLine.setDebitOrCredit(BaseConsts.ONE);
			f_drLine.setAmount(costPrice);
			f_drLine.setTaxRate(DecimalUtil.ZERO);

			VoucherLine f_crLine2 = convertToVoucherLine(billInStore, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
			f_crLine2.setDebitOrCredit(BaseConsts.TWO);
			f_crLine2.setAmount(costPrice);
			f_crLine2.setTaxRate(DecimalUtil.ZERO);

			voucherLines.add(f_crLine1);
			voucherLines.add(f_drLine);
			voucherLines.add(f_crLine2);
		}

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		return voucherService.createVoucherDetail(voucherDetail);
	}

	private Voucher convertToVoucher(BillInStore billInStore, Integer vourcherType) {
		Voucher voucher = new Voucher();
		BaseProject project = cacheService.getProjectById(billInStore.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, billInStore.getProjectId());
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(project.getBusinessUnitId());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, project.getBusinessUnitId());
		}
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(busiUnit.getId());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, busiUnit.getId());
		}

		voucher.setAccountBookId(accountBook.getId());
		voucher.setBusiUnit(busiUnit.getId());
		voucher.setInStoreId(billInStore.getId());
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setVoucherWord(vourcherType);
		Date billDate = billInStore.getAcceptTime();
		voucher.setVoucherSummary(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.TWO) + "")
				+ billInStore.getBillNo() + "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, billDate)));
		voucher.setBillNo(billInStore.getBillNo());
		voucher.setBillType(BaseConsts.TWO); // 付款单
		voucher.setBillDate(billDate); // 单据日期
		voucher.setVoucherDate(billDate); // 凭证日期
		voucher.setState(BaseConsts.THREE);

		return voucher;
	}

	private VoucherLine convertToVoucherLine(BillInStore billInStore, String account_line_no, String voucherSummary,
			Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		line.setAmount(billInStore.getReceiveAmount());
		line.setProjectId(billInStore.getProjectId());
		line.setSupplierId(billInStore.getSupplierId());
		line.setCustId(billInStore.getSupplierId());
		line.setCurrencyType(billInStore.getCurrencyType());
		line.setInStoreId(billInStore.getId());
		BaseProject baseProject = cacheService.getProjectById(billInStore.getProjectId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, baseProject.getBusinessUnitId(),
				bankId, type, billInStore.getCurrencyType());
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}
}
