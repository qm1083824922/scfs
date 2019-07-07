package com.scfs.service.bookkeeping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.fi.AccountBookService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: FeeKeepingService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月22日			Administrator
 *
 * </pre>
 */
@Service
public class FeeKeepingService {
	@Autowired
	FeeDao feeDao;

	@Autowired
	VoucherService voucherService;

	@Autowired
	CacheService cacheService;

	@Autowired
	AccountBookService accountBookService;

	@Autowired
	VoucherLineService voucherLineService;
	@Autowired
	private AccountLineService accountLineService;

	@Autowired
	private SequenceService sequenceService;

	/**
	 * 
	 * TODO.
	 *
	 * @param id
	 *            费用id
	 */
	public Integer feeBookkeeping(Integer id) {
		Integer lineId = null;
		Fee fee = feeDao.queryEntityById(id);
		if (fee == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, FeeServiceImpl.class, id);
		}
		switch (fee.getFeeType()) {
		case BaseConsts.ONE:
			lineId = recFeeBookkeeping(fee);
			break;
		case BaseConsts.TWO:
			payFeeBookkeeping(fee);
			break;
		case BaseConsts.THREE:
			recPayFeeBookkeeping(fee);
			break;
		}
		return lineId;
	}

	private Integer recFeeBookkeeping(Fee fee) {

		BigDecimal taxRate = fee.getProvideInvoiceTaxRate();
		BigDecimal amount = fee.getRecAmount();

		VoucherDetail voucherDetail = new VoucherDetail();

		Voucher voucher = convertToVoucher(fee, BaseConsts.TEN);
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		if (isHome == BaseConsts.ONE) { // 1：国内
			VoucherLine drLine = convertToVoucherLine(fee, voucher.getVoucherSummary(), AccountNoConsts.ACCOUNT_NO_1223,
					BaseConsts.ONE, null);// 应收账款/外部/费用
			drLine.setDebitOrCredit(BaseConsts.ONE);
			drLine.setAmount(amount);
			drLine.setProjectId(fee.getProjectId());
			// 客户和供应商相同，主体是客户，供应商就是客户，主体是供应商，客户就是供应商
			drLine.setSupplierId(fee.getCustPayer());
			drLine.setCustId(fee.getCustPayer());

			VoucherLine crLine1 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_6051, BaseConsts.ONE, null); // 主营业务收入/服务收入/外部销售
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAmount(DecimalUtil
					.formatScale2(DecimalUtil.divide(amount, DecimalUtil.add(taxRate, new BigDecimal("1")))));
			crLine1.setProjectId(fee.getProjectId());
			crLine1.setSupplierId(fee.getCustPayer());
			crLine1.setCustId(fee.getCustPayer());
			crLine1.setTaxRate(taxRate);
			// 预提应交税费-增值税-销项税额
			VoucherLine crLine2 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_122320, BaseConsts.THREE, null);
			crLine2.setDebitOrCredit(BaseConsts.TWO);
			crLine2.setAmount(DecimalUtil.subtract(amount, crLine1.getAmount()));
			crLine2.setProjectId(fee.getProjectId());
			crLine2.setSupplierId(fee.getCustPayer());
			crLine2.setCustId(fee.getCustPayer());
			crLine2.setTaxRate(taxRate);

			voucherLines.add(drLine);
			voucherLines.add(crLine1);
			voucherLines.add(crLine2);

		} else if (isHome == BaseConsts.ZERO) { // 0:香港

			VoucherLine drLine = convertToVoucherLine(fee, voucher.getVoucherSummary(), AccountNoConsts.ACCOUNT_NO_1223,
					BaseConsts.ONE, null);
			drLine.setDebitOrCredit(BaseConsts.ONE);
			drLine.setAmount(amount);
			drLine.setProjectId(fee.getProjectId());
			// 客户和供应商相同，主体是客户，供应商就是客户，主体是供应商，客户就是供应商
			drLine.setSupplierId(fee.getCustPayer());
			drLine.setCustId(fee.getCustPayer());

			VoucherLine crLine1 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_6051, BaseConsts.ONE, null);
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAmount(amount);
			crLine1.setProjectId(fee.getProjectId());
			crLine1.setSupplierId(fee.getCustPayer());
			crLine1.setCustId(fee.getCustPayer());
			crLine1.setTaxRate(DecimalUtil.ZERO);

			voucherLines.add(drLine);
			voucherLines.add(crLine1);

		}

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		Integer id = voucherService.createVoucherDetailOne(voucherDetail);
		return id;
	}

	private void payFeeBookkeeping(Fee fee) {
		BigDecimal taxRate = fee.getAcceptInvoiceTaxRate();
		BigDecimal amount = fee.getPayAmount();

		VoucherDetail voucherDetail = new VoucherDetail();

		Voucher voucher = convertToVoucher(fee, BaseConsts.TEN);
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		if (isHome == BaseConsts.ONE) { // 1：国内
			VoucherLine drLine1 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_64020101, BaseConsts.THREE, null);// 主营业务成本/服务成本/对外销售
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setAmount(DecimalUtil
					.formatScale2(DecimalUtil.divide(amount, DecimalUtil.add(taxRate, new BigDecimal("1")))));
			drLine1.setProjectId(fee.getProjectId());
			drLine1.setSupplierId(fee.getCustReceiver());
			drLine1.setCustId(fee.getCustReceiver());
			drLine1.setTaxRate(taxRate);

			VoucherLine drLine2 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_220205, BaseConsts.THREE, null); // 预提应交税费-增值税-进项税额
			drLine2.setDebitOrCredit(BaseConsts.ONE);
			drLine2.setAmount(DecimalUtil.subtract(amount, drLine1.getAmount()));
			drLine2.setProjectId(fee.getProjectId());
			drLine2.setSupplierId(fee.getCustReceiver());
			drLine2.setCustId(fee.getCustReceiver());
			drLine2.setTaxRate(taxRate);

			voucherLines.add(drLine1);
			voucherLines.add(drLine2);
		} else if (isHome == BaseConsts.ZERO) { // 0:香港
			VoucherLine drLine1 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_64020101, BaseConsts.THREE, null);// 主营业务成本/服务成本/对外销售
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setAmount(amount);
			drLine1.setProjectId(fee.getProjectId());
			drLine1.setSupplierId(fee.getCustReceiver());
			drLine1.setCustId(fee.getCustReceiver());
			drLine1.setTaxRate(DecimalUtil.ZERO);
			voucherLines.add(drLine1);
		}

		VoucherLine crLine = convertToVoucherLine(fee, voucher.getVoucherSummary(), AccountNoConsts.ACCOUNT_NO_220201,
				BaseConsts.ONE, null);// 应付账款/外部/费用
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAmount(amount);
		crLine.setProjectId(fee.getProjectId());
		// 客户和供应商相同，主体是客户，供应商就是客户，主体是供应商，客户就是供应商
		crLine.setSupplierId(fee.getCustReceiver());
		crLine.setCustId(fee.getCustReceiver());

		voucherLines.add(crLine);
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	private void recPayFeeBookkeeping(Fee fee) {
		BigDecimal recAmount = fee.getRecAmount();
		BigDecimal payAmount = fee.getPayAmount();
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(fee, BaseConsts.TEN);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(fee, voucher.getVoucherSummary(), AccountNoConsts.ACCOUNT_NO_1159,
				BaseConsts.ONE, null);// 其他应收款-代收款
		drLine.setDebitOrCredit(BaseConsts.ONE);
		drLine.setAmount(recAmount);
		drLine.setProjectId(fee.getProjectId());
		drLine.setSupplierId(fee.getCustPayer());
		drLine.setCustId(fee.getCustPayer());

		VoucherLine crLine = convertToVoucherLine(fee, voucher.getVoucherSummary(), AccountNoConsts.ACCOUNT_NO_2159,
				BaseConsts.ONE, null);// 其他应付款-代付款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAmount(payAmount);
		crLine.setProjectId(fee.getProjectId());
		// 客户和供应商相同，主体是客户，供应商就是客户，主体是供应商，客户就是供应商
		crLine.setSupplierId(fee.getCustReceiver());
		crLine.setCustId(fee.getCustReceiver());

		if (DecimalUtil.gt(recAmount, payAmount)) { // 应收大于应付 ，记借
			VoucherLine crLine1 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_610801, BaseConsts.THREE, null);// 主营业务收入/服务收入/外部销售
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAmount(DecimalUtil.subtract(recAmount, payAmount));
			crLine1.setProjectId(fee.getProjectId());
			// 客户和供应商相同，主体是客户，供应商就是客户，主体是供应商，客户就是供应商
			crLine1.setSupplierId(fee.getCustReceiver());
			crLine1.setCustId(fee.getCustReceiver());
			crLine1.setTaxRate(fee.getAcceptInvoiceTaxRate());
			voucherLines.add(crLine1);
		} else if (DecimalUtil.gt(payAmount, recAmount)) {// 应付大于应收
			VoucherLine drLine1 = convertToVoucherLine(fee, voucher.getVoucherSummary(),
					AccountNoConsts.ACCOUNT_NO_610801, BaseConsts.THREE, null);// 其他应收款-代收款
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setAmount(DecimalUtil.subtract(payAmount, recAmount));
			drLine1.setProjectId(fee.getProjectId());
			drLine1.setSupplierId(fee.getCustPayer());
			drLine1.setCustId(fee.getCustPayer());
			drLine1.setTaxRate(DecimalUtil.ZERO);
			voucherLines.add(drLine1);
		}
		voucherLines.add(crLine);
		voucherLines.add(drLine);

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);

	}

	private Voucher convertToVoucher(Fee fee, Integer vourcherType) {
		Voucher voucher = new Voucher();
		BaseProject project = cacheService.getProjectById(fee.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, fee.getProjectId());
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
		voucher.setFeeId(fee.getId());
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setVoucherWord(vourcherType);
		voucher.setBillNo(fee.getFeeNo());
		voucher.setBillType(BaseConsts.ONE);
		Date billDate = fee.getBookDate();
		voucher.setBillDate(billDate);
		voucher.setState(BaseConsts.THREE);
		voucher.setVoucherDate(billDate);
		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.ONE) + "") + voucher.getBillNo()
						+ "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate())));

		return voucher;
	}

	private VoucherLine convertToVoucherLine(Fee fee, String voucherSummary, String account_line_no, Integer type,
			Integer bankId) {
		VoucherLine line = new VoucherLine();
		BaseProject baseProject = cacheService.getProjectById(fee.getProjectId());
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目数据为空");
		}
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, baseProject.getBusinessUnitId(),
				bankId, type, fee.getCurrencyType());
		line.setCurrencyType(fee.getCurrencyType());
		line.setFeeId(fee.getId());
		line.setVoucherLineSummary(voucherSummary);
		line.setAccountLineId(accountLine.getId());
		return line;
	}

	/**
	 * 
	 * TODO. 回滚费用记账
	 * 
	 * @param id
	 */
	public void rollbackFeeBookkeeping(Integer id) {
		Fee fee = feeDao.queryEntityById(id);
		if (fee == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, FeeDao.class, id);
		}
		VoucherSearchReqDto voucherSearchReqDto = new VoucherSearchReqDto();
		voucherSearchReqDto.setBillType(BaseConsts.ONE);
		voucherSearchReqDto.setBillNo(fee.getFeeNo());
		List<Voucher> vouchers = voucherService.queryListByCon(voucherSearchReqDto);
		if (!CollectionUtils.isEmpty(vouchers)) {
			Voucher voucher = vouchers.get(0);
			voucherService.deleteOverVoucherById(voucher.getId());
		}
	}
}
