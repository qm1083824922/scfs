package com.scfs.service.bookkeeping;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountBookService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.pay.PayFeeRelationService;
import com.scfs.service.pay.PayService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FiCacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PayOrderKeepingService.java
 *  Description:            付款自动记录凭证
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */

@Service
public class PayOrderKeepingService {
	@Autowired
	PayService payService;

	@Autowired
	CacheService cacheService;

	@Autowired
	VoucherService voucherService;

	@Autowired
	AccountBookService accountBookService;

	@Autowired
	PayFeeRelationService payFeeRelationService;

	@Autowired
	FeeDao feeDao;

	@Autowired
	FiCacheService fiCacheService;

	@Autowired
	private AccountLineService accountLineService;

	@Autowired
	private SequenceService sequenceService;

	/**
	 * 
	 * TODO.
	 *
	 * @param id
	 *            付款id
	 */
	public void payBookkeeping(Integer id) {
		PayOrder payOrder = payService.queryEntityById(id);
		if (payOrder == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PayService.class, id);
		}
		switch (payOrder.getPayType()) {
		case BaseConsts.ONE: // 货款
			if (payOrder.getPayWay() == BaseConsts.ONE) { // 转账
				payBookkeeping_1_1(payOrder);
			} else if (payOrder.getPayWay() == BaseConsts.TWO) { // 承兑汇票
				payBookkeeping_1_2(payOrder);
			}
			break;
		case BaseConsts.TWO: // 费用
			if (payOrder.getPayWay() == BaseConsts.ONE) { // 转账
				payBookkeeping_2_1(payOrder);
			} else if (payOrder.getPayWay() == BaseConsts.TWO) { // 承兑汇票
				payBookkeeping_2_2(payOrder);
			}
			break;
		case BaseConsts.THREE: // 借款
			if (payOrder.getPayWay() == BaseConsts.ONE) { // 转账
				payBookkeeping_4_1(payOrder);
			}
			break;
		case BaseConsts.FOUR: // 保证金
			if (payOrder.getPayWay() == BaseConsts.ONE) { // 转账 {
				payBookkeeping_3_1(payOrder);
			} else if (payOrder.getPayWay() == BaseConsts.TWO) { // 承兑汇票
				payBookkeeping_3_2(payOrder);
			}
			break;
		case BaseConsts.FIVE: // 预付款
			if (payOrder.getPayWay() == BaseConsts.ONE) { // 转账 {
				payBookkeeping_5_1(payOrder);
			} else if (payOrder.getPayWay() == BaseConsts.TWO) { // 承兑汇票
				// payBookkeeping_5_2(payOrder);
			}
			break;
		case BaseConsts.SIX: // 付退款
			if (payOrder.getPayWay() == BaseConsts.ONE) {// 转账
				payBookkeeping_6_1(payOrder);
			}
		default:
			break;
		}

	}

	public void rollbackPayBookkeeping(Integer id) {
		PayOrder payOrder = payService.queryEntityById(id);
		if (payOrder == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PayService.class, id);
		}
		VoucherSearchReqDto voucherSearchReqDto = new VoucherSearchReqDto();
		voucherSearchReqDto.setBillType(BaseConsts.SEVEN);
		voucherSearchReqDto.setBillNo(payOrder.getPayNo());
		List<Voucher> vouchers = voucherService.queryListByCon(voucherSearchReqDto);
		if (!CollectionUtils.isEmpty(vouchers)) {
			Voucher voucher = vouchers.get(0);
			voucherService.deleteOverVoucherById(voucher.getId());
		}
	}

	private void payBookkeeping_1_1(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.THREE);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220201,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款
		drLine.setDebitOrCredit(BaseConsts.ONE);
		voucherLines.add(drLine);

		VoucherLine crLine = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAccountId(payOrder.getPaymentAccount());
		voucherLines.add(crLine);

		// 判断是否是转账手续费
		if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

			VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setAccountId(payOrder.getPaymentAccount());
			drLine1.setCurrencyType(payOrder.getRealCurrencyType());
			drLine1.setAmount(payOrder.getBankCharge());
			voucherLines.add(drLine1);

			VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
					BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAccountId(payOrder.getPaymentAccount());
			crLine1.setAmount(payOrder.getBankCharge());
			voucherLines.add(crLine1);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	private void payBookkeeping_1_2(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.THREE);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) {
			VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220201,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/货款
			drLine.setDebitOrCredit(BaseConsts.ONE);
			voucherLines.add(drLine);

			VoucherLine crLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_112101,
					voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.THREE, null); // 应付票据/银票
			crLine.setDebitOrCredit(BaseConsts.TWO);
			crLine.setAccountId(payOrder.getPaymentAccount());
			voucherLines.add(crLine);

			if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

				VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
				drLine1.setDebitOrCredit(BaseConsts.ONE);
				drLine1.setCurrencyType(payOrder.getRealCurrencyType());
				drLine1.setAccountId(payOrder.getPaymentAccount());
				drLine1.setAmount(payOrder.getBankCharge());

				VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
						BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
				crLine1.setDebitOrCredit(BaseConsts.TWO);
				crLine1.setAccountId(payOrder.getPaymentAccount());
				crLine1.setAmount(payOrder.getBankCharge());

				voucherLines.add(drLine1);
				voucherLines.add(crLine1);
			}

			voucherDetail.setVoucher(voucher);
			voucherDetail.setVoucherLines(voucherLines);

			voucherService.createVoucherDetail(voucherDetail);
		}
	}

	private void payBookkeeping_2_1(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.FOUR);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		List<PayFeeRelationModel> payFeeRelationModels = payFeeRelationService
				.queryGroupFeeByPayOrderId(payOrder.getId());
		for (PayFeeRelationModel payFeeRelationModel : payFeeRelationModels) {
			switch (payFeeRelationModel.getFeeType()) {
			case BaseConsts.TWO: // 应付
				VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220299,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/费用
				drLine.setDebitOrCredit(BaseConsts.ONE);
				drLine.setAmount(payFeeRelationModel.getPayAmount());
				voucherLines.add(drLine);
				break;
			case BaseConsts.THREE: // 应收应付
				VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220299,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 其他应付款/外部/代付款
				drLine1.setDebitOrCredit(BaseConsts.ONE);
				drLine1.setAmount(payFeeRelationModel.getPayAmount());
				voucherLines.add(drLine1);
				break;
			default:
				break;
			}
		}
		VoucherLine crLine = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAccountId(payOrder.getPaymentAccount());
		voucherLines.add(crLine);
		// 判断是否是转账手续费
		if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {
			VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null);// 财务费用/手续费
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setCurrencyType(payOrder.getRealCurrencyType());
			drLine1.setAccountId(payOrder.getPaymentAccount());
			drLine1.setAmount(payOrder.getBankCharge());

			VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
					BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAccountId(payOrder.getPaymentAccount());
			crLine1.setAmount(payOrder.getBankCharge());

			voucherLines.add(drLine1);
			voucherLines.add(crLine1);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	private void payBookkeeping_2_2(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.FOUR);
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) {
			List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
			PayFeeRelationReqDto req = new PayFeeRelationReqDto();
			req.setPayId(payOrder.getId());
			List<PayFeeRelationModel> payFeeRelationModels = payFeeRelationService.queryPayFeeRelatioByCon(req);
			for (PayFeeRelationModel payFeeRelationModel : payFeeRelationModels) {
				switch (payFeeRelationModel.getFeeType()) {
				case BaseConsts.TWO: // 应付
					VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220299,
							voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/外部/费用
					drLine.setDebitOrCredit(BaseConsts.ONE);
					drLine.setAmount(payFeeRelationModel.getPayAmount());
					voucherLines.add(drLine);
					break;
				case BaseConsts.THREE: // 应收应付
					VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220299,
							voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 其他应付款/外部/代付款
					drLine1.setDebitOrCredit(BaseConsts.ONE);
					drLine1.setAmount(payFeeRelationModel.getPayAmount());
					voucherLines.add(drLine1);
					break;
				default:
					break;
				}
			}
			VoucherLine crLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220101,
					voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.THREE, null); // 应付票据/银票
			crLine.setDebitOrCredit(BaseConsts.TWO);
			crLine.setAccountId(payOrder.getPaymentAccount());
			voucherLines.add(crLine);

			if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

				VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
				drLine1.setDebitOrCredit(BaseConsts.ONE);
				drLine1.setCurrencyType(payOrder.getRealCurrencyType());
				drLine1.setAccountId(payOrder.getPaymentAccount());
				drLine1.setAmount(payOrder.getBankCharge());

				VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
						BaseConsts.TWO, payOrder.getPaymentAccount());// 银行存款
				crLine1.setDebitOrCredit(BaseConsts.TWO);
				crLine1.setAccountId(payOrder.getPaymentAccount());
				crLine1.setAmount(payOrder.getBankCharge());

				voucherLines.add(drLine1);
				voucherLines.add(crLine1);
			}

			voucherDetail.setVoucher(voucher);
			voucherDetail.setVoucherLines(voucherLines);

			voucherService.createVoucherDetail(voucherDetail);
		}

	}

	private void payBookkeeping_3_1(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.FIVE);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_1159,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 其他应收款/外部/保证金
		drLine.setDebitOrCredit(BaseConsts.ONE);
		voucherLines.add(drLine);

		VoucherLine crLine = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAccountId(payOrder.getPaymentAccount());
		voucherLines.add(crLine);

		// 判断是否是转账手续费
		if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

			VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setCurrencyType(payOrder.getRealCurrencyType());
			drLine1.setAccountId(payOrder.getPaymentAccount());
			drLine1.setAmount(payOrder.getBankCharge());

			VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
					BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAccountId(payOrder.getPaymentAccount());
			crLine1.setAmount(payOrder.getBankCharge());

			voucherLines.add(drLine1);
			voucherLines.add(crLine1);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail);
	}

	private void payBookkeeping_3_2(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.FIVE);
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) {
			List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

			VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_1159,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 其他应收款/外部/保证金
			drLine.setDebitOrCredit(BaseConsts.ONE);

			VoucherLine crLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220101,
					voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.THREE, null); // 应付票据/银票//
																							// 应付票据/银票
			crLine.setDebitOrCredit(BaseConsts.TWO);
			crLine.setAccountId(payOrder.getPaymentAccount());

			voucherLines.add(drLine);
			voucherLines.add(crLine);

			if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

				VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
						voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
				drLine1.setDebitOrCredit(BaseConsts.ONE);
				drLine1.setCurrencyType(payOrder.getRealCurrencyType());
				drLine1.setAccountId(payOrder.getPaymentAccount());
				drLine1.setAmount(payOrder.getBankCharge());

				VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
						BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
				crLine1.setDebitOrCredit(BaseConsts.TWO);
				crLine1.setAccountId(payOrder.getPaymentAccount());
				crLine1.setAmount(payOrder.getBankCharge());

				voucherLines.add(drLine1);
				voucherLines.add(crLine1);
			}
			voucherDetail.setVoucher(voucher);
			voucherDetail.setVoucherLines(voucherLines);

			voucherService.createVoucherDetail(voucherDetail);
		}
	}

	/**
	 * 付款单借款的凭证记录
	 * 
	 */
	private void payBookkeeping_4_1(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.SIX);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine crLine = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount());// 银行存款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAccountId(payOrder.getPaymentAccount());
		crLine.setAmount(payOrder.getRealPayAmount());
		voucherLines.add(crLine);

		VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_1159,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 其他应收款-美元-客户往来（核算项）
		drLine.setDebitOrCredit(BaseConsts.ONE);//
		drLine.setAmount(payOrder.getPayAmount());
		voucherLines.add(drLine);

		// 判断是否是转账手续费
		if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

			VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setCurrencyType(payOrder.getRealCurrencyType());
			drLine1.setAccountId(payOrder.getPaymentAccount());
			drLine1.setAmount(payOrder.getBankCharge());

			VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
					BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAccountId(payOrder.getPaymentAccount());
			crLine1.setAmount(payOrder.getBankCharge());

			voucherLines.add(drLine1);
			voucherLines.add(crLine1);
		}

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		voucherService.createVoucherDetail(voucherDetail);
	}

	/**
	 * 预付款记账操作
	 * 
	 * @param payOrder
	 * @return
	 */
	private void payBookkeeping_5_1(PayOrder payOrder) {

		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.THREE);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		// 根据经营单位查询币种
		VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_220202,
				voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.ONE, null); // 应付账款/物流费
		drLine.setDebitOrCredit(BaseConsts.ONE);
		voucherLines.add(drLine);
		// 银行存款
		VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
		crLine1.setDebitOrCredit(BaseConsts.TWO);
		crLine1.setAccountId(payOrder.getPaymentAccount());
		crLine1.setAmount(payOrder.getRealPayAmount());
		voucherLines.add(crLine1);
		// 判断是否是转账手续费
		if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {
			VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setCurrencyType(payOrder.getRealCurrencyType());
			drLine1.setAccountId(payOrder.getPaymentAccount());
			drLine1.setAmount(payOrder.getBankCharge());

			VoucherLine crLine2 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
					BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
			crLine2.setDebitOrCredit(BaseConsts.TWO);
			crLine2.setAccountId(payOrder.getPaymentAccount());
			crLine2.setAmount(payOrder.getBankCharge());

			voucherLines.add(drLine1);
			voucherLines.add(crLine2);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail);
	}

	/**
	 * 新凭证的记账 付退款
	 * 
	 * @param payOrder
	 */
	private void payBookkeeping_6_1(PayOrder payOrder) {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(payOrder, BaseConsts.THREE);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();

		VoucherLine drLine = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_1223,
				voucher.getVoucherSummary(), BaseConsts.TWO, BaseConsts.ONE, null); // 其他应收款/外部/保证金
		drLine.setDebitOrCredit(BaseConsts.TWO);
		drLine.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), payOrder.getPayAmount()));
		drLine.setCurrencyType(payOrder.getCurrnecyType());
		voucherLines.add(drLine);

		VoucherLine crLine = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
				BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
		crLine.setDebitOrCredit(BaseConsts.TWO);
		crLine.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), payOrder.getRealPayAmount()));
		crLine.setCurrencyType(payOrder.getRealCurrencyType());
		crLine.setAccountId(payOrder.getPaymentAccount());
		voucherLines.add(crLine);

		// 判断是否是转账手续费
		if (payOrder.getBankCharge() != null && !DecimalUtil.eq(payOrder.getBankCharge(), DecimalUtil.ZERO)) {

			VoucherLine drLine1 = convertToVoucherLine(payOrder, AccountNoConsts.ACCOUNT_NO_660301,
					voucher.getVoucherSummary(), BaseConsts.ONE, BaseConsts.THREE, null); // 财务费用/手续费
			drLine1.setDebitOrCredit(BaseConsts.ONE);
			drLine1.setCurrencyType(payOrder.getRealCurrencyType());
			drLine1.setAccountId(payOrder.getPaymentAccount());
			drLine1.setAmount(payOrder.getBankCharge());

			VoucherLine crLine1 = convertToVoucherLine(payOrder, null, voucher.getVoucherSummary(), BaseConsts.TWO,
					BaseConsts.TWO, payOrder.getPaymentAccount()); // 银行存款
			crLine1.setDebitOrCredit(BaseConsts.TWO);
			crLine1.setAccountId(payOrder.getPaymentAccount());
			crLine1.setAmount(payOrder.getBankCharge());

			voucherLines.add(drLine1);
			voucherLines.add(crLine1);
		}
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
		BaseSubject baseSubject = cacheService.getBaseSubjectById(payOrder.getBusiUnit());
		voucher.setAccountBookId(accountBook.getId());
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(baseSubject.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setBusiUnit(payOrder.getBusiUnit());
		voucher.setPayId(payOrder.getId());
		voucher.setVoucherWord(vourcherType);
		voucher.setBillType(BaseConsts.FOUR); // 付款单
		voucher.setBillNo(payOrder.getPayNo());
		voucher.setVoucherDate(payOrder.getConfirmorAt());
		voucher.setBillDate(payOrder.getConfirmorAt());
		voucher.setState(BaseConsts.THREE);
		voucher.setVoucherSummary(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.FOUR) + "") + voucher.getBillNo()
						+ "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate())));

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
		line.setProjectId(payOrder.getProjectId());
		line.setSupplierId(payOrder.getPayee());
		line.setCustId(payOrder.getPayee());
		line.setPayId(payOrder.getId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, payOrder.getBusiUnit(), bankId,
				type, payOrder.getRealCurrencyType());
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

}
