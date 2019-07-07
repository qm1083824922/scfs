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
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtlTaxGroupSum;
import com.scfs.domain.logistics.entity.BillOutStoreTaxGroupCostPrice;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: OutStoreBookkeepingService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */

@Service
public class OutStoreBookkeepingService {
	@Autowired
	BillOutStoreDao billOutStoreDao;

	@Autowired
	BillOutStoreDtlService billOutStoreDtlService;

	@Autowired
	BillOutStorePickDtlService billOutStorePickDtlService;

	@Autowired
	VoucherService voucherService;
	@Autowired
	AccountLineService accountLineService;
	@Autowired
	CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;

	public Integer outStoreBookkeeping(int id) {
		BillOutStoreSearchReqDto req = new BillOutStoreSearchReqDto();
		req.setId(id);
		BillOutStore billOutStore = billOutStoreDao.queryById(req);
		VoucherDetail voucherDetail = new VoucherDetail();

		Voucher voucher = convertToVoucher(billOutStore, BaseConsts.EIGHT);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_1223,
				voucher.getVoucherSummary(), BaseConsts.ONE, null); // 应收账款/外部/货款
		drLine.setDebitOrCredit(BaseConsts.ONE); // 借
		voucherLines.add(drLine);

		VoucherLine drLine4 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_5100,
				voucher.getVoucherSummary(), BaseConsts.THREE, null); // 业绩量
		drLine4.setDebitOrCredit(BaseConsts.ONE); // 借
		drLine4.setAmount(billOutStore.getSendAmount() == null ? BigDecimal.ZERO : billOutStore.getSendAmount());
		voucherLines.add(drLine4);

		VoucherLine drLine5 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_5100,
				voucher.getVoucherSummary(), BaseConsts.THREE, null); // 业绩量
		drLine5.setDebitOrCredit(BaseConsts.TWO); // 贷
		drLine5.setAmount(billOutStore.getSendAmount() == null ? BigDecimal.ZERO : billOutStore.getSendAmount());
		voucherLines.add(drLine5);

		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) { // 国内
			List<BillOutStoreDtlTaxGroupSum> outGroupSum = billOutStoreDtlService.queryTaxGroupSumByBillOutStoreId(id); // 出库商品按税率分组总金额
			for (BillOutStoreDtlTaxGroupSum item : outGroupSum) {
				BigDecimal sendAmount = (null == item.getSendAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(item.getSendAmount()));
				BigDecimal taxRate = item.getTaxRate();
				VoucherLine crLine1 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_6001,
						voucher.getVoucherSummary(), BaseConsts.ONE, null); // 主营业务收入/销售收入/外部销售
				crLine1.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine1.setTaxRate(taxRate);
				crLine1.setAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(sendAmount, DecimalUtil.add(taxRate, new BigDecimal("1")))));

				VoucherLine crLine2 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_122320,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 预提应交税金-增值税-进项税额
				crLine2.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine2.setTaxRate(taxRate);
				crLine2.setAmount(DecimalUtil.subtract(sendAmount, crLine1.getAmount()));

				voucherLines.add(crLine1);
				voucherLines.add(crLine2);

			}
			List<BillOutStoreTaxGroupCostPrice> costPrice = billOutStorePickDtlService
					.queryCostPriceByBillOutStoreId(id);
			for (BillOutStoreTaxGroupCostPrice item : costPrice) {
				BigDecimal taxRate = item.getTaxRate();
				BigDecimal cost = item.getCostPriceAmount();
				BigDecimal amount = DecimalUtil
						.formatScale2(DecimalUtil.divide(cost, DecimalUtil.add(taxRate, new BigDecimal("1"))));

				VoucherLine drLine3 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_610801,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本/销售成本/外部销售
				drLine3.setDebitOrCredit(BaseConsts.ONE); // 借
				drLine3.setTaxRate(taxRate);
				drLine3.setAmount(amount);

				VoucherLine crLine3 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_1201,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
				crLine3.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine3.setTaxRate(taxRate);
				crLine3.setAmount(amount);

				voucherLines.add(drLine3);
				voucherLines.add(crLine3);
			}

		} else if (isHome == BaseConsts.ZERO) { // 香港
			BigDecimal costPrice = DecimalUtil.formatScale2(billOutStorePickDtlService.queryCostAmountByOutStoreId(id));
			BigDecimal sendAmount = DecimalUtil.formatScale2(billOutStoreDtlService.querySumByBillOutStoreId(id));
			VoucherLine f_crLine1 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_6001,
					voucher.getVoucherSummary(), BaseConsts.ONE, null); // 主营业务收入/销售收入/外部销售
			f_crLine1.setDebitOrCredit(BaseConsts.TWO);
			f_crLine1.setAmount(sendAmount);
			f_crLine1.setTaxRate(DecimalUtil.ZERO);

			VoucherLine f_drLine = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_610801,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本/销售成本/外部销售
			f_drLine.setDebitOrCredit(BaseConsts.ONE);
			f_drLine.setAmount(costPrice);
			f_drLine.setTaxRate(DecimalUtil.ZERO);

			VoucherLine f_crLine2 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_1201,
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

	private Voucher convertToVoucher(BillOutStore billOutStore, Integer vourcherType) {
		Voucher voucher = new Voucher();
		BaseProject project = cacheService.getProjectById(billOutStore.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, billOutStore.getProjectId());
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
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setOutStoreId(billOutStore.getId());
		voucher.setVoucherWord(vourcherType);
		Date billDate = billOutStore.getSendDate() == null ? new Date() : billOutStore.getSendDate();
		voucher.setVoucherSummary(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.THREE) + "")
				+ billOutStore.getBillNo() + "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, billDate)));
		voucher.setBillNo(billOutStore.getBillNo());
		voucher.setBillType(BaseConsts.THREE); // 出库单
		voucher.setBillDate(billDate); // 单据日期
		voucher.setVoucherDate(billDate); // 凭证日期
		voucher.setState(BaseConsts.THREE);

		return voucher;
	}

	public VoucherLine convertToVoucherLine(BillOutStore billOutStore, String account_line_no, String voucherSummary,
			Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		line.setAmount(billOutStore.getSendAmount());
		line.setProjectId(billOutStore.getProjectId());
		line.setSupplierId(billOutStore.getCustomerId());
		line.setCustId(billOutStore.getCustomerId());
		line.setCurrencyType(billOutStore.getCurrencyType());
		line.setOutStoreId(billOutStore.getId());
		BaseProject project = cacheService.getProjectById(billOutStore.getProjectId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, project.getBusinessUnitId(),
				bankId, type, billOutStore.getCurrencyType());
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}
}
