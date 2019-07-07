package com.scfs.service.bookkeeping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.AccountNoConsts;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fi.VoucherDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.po.dto.req.DistributeAccountReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLineTaxGroupSum;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.po.DistributionOrderService;
import com.scfs.service.support.CacheService;

/**
 * Created by Administrator on 2017年5月12日.
 */
@Service
public class DistributeBookkeepingService {
	@Autowired
	BillOutStoreDao billOutStoreDao;
	@Autowired
	BillOutStoreDtlService billOutStoreDtlService;
	@Autowired
	BillOutStorePickDtlService billOutStorePickDtlService;
	@Autowired
	DistributionOrderService distributionOrderService;
	@Autowired
	VoucherService voucherService;
	@Autowired
	CacheService cacheService;
	@Autowired
	VoucherDao voucherDao;
	@Autowired
	VoucherLineDao voucherLineDao;
	@Autowired
	private AccountLineService accountLineService;
	@Autowired
	private SequenceService sequenceService;

	public void distributeBookkeeping() {
		Date currDate = new Date();
		Date voucherDate = DateFormatUtils.beforeDay(currDate, 1);
		DistributeAccountReqDto distributeAccountReqDto = new DistributeAccountReqDto();
		distributeAccountReqDto.setVoucherDate(voucherDate);
		List<PurchaseOrderTitle> purchaseOrderTitleList = distributionOrderService
				.queryDistributeOrderGroupByProjectId(distributeAccountReqDto);
		Date redVoucherDate = currDate;
		for (PurchaseOrderTitle purchaseOrderTitle : purchaseOrderTitleList) {
			distributeAccountReqDto.setProjectId(purchaseOrderTitle.getProjectId());
			// 入库凭证
			Integer inStoreVoucherId = inStoreBookkeeping(purchaseOrderTitle, distributeAccountReqDto, BaseConsts.ONE);
			// 出库凭证
			Integer outStoreVoucherId = outStoreBookkeeping(purchaseOrderTitle, distributeAccountReqDto,
					BaseConsts.TWO);
			// 红冲入库凭证
			createRedVoucherById(inStoreVoucherId, redVoucherDate, purchaseOrderTitle);
			// 红冲出库凭证
			createRedVoucherById(outStoreVoucherId, redVoucherDate, purchaseOrderTitle);
		}
	}

	public Integer inStoreBookkeeping(PurchaseOrderTitle purchaseOrderTitle,
			DistributeAccountReqDto distributeAccountReqDto, Integer createType) {
		VoucherDetail voucherDetail = new VoucherDetail();

		Voucher voucher = convertToVoucher(purchaseOrderTitle, distributeAccountReqDto, createType, BaseConsts.SEVEN);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) { // 国内
			List<PurchaseOrderLineTaxGroupSum> lineTaxGroupSumList = distributionOrderService
					.queryTaxGroupSumByProjectId(distributeAccountReqDto); // 项目按商品按税率分组总金额
			for (PurchaseOrderLineTaxGroupSum item : lineTaxGroupSumList) {
				BigDecimal amount = DecimalUtil.formatScale2(item.getUnDistributeAmount());
				BigDecimal taxRate = item.getTaxRate();
				VoucherLine drLine1 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_1201,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
				drLine1.setDebitOrCredit(BaseConsts.ONE); // 借123

				drLine1.setTaxRate(taxRate);
				drLine1.setAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(amount, DecimalUtil.add(taxRate, new BigDecimal("1")))));
				voucherLines.add(drLine1);
			}
		} else if (isHome == BaseConsts.ZERO) {// 香港
			BigDecimal receiveAmount = DecimalUtil
					.formatScale2(distributionOrderService.queryUnDistributeAmountByProjectId(distributeAccountReqDto));
			VoucherLine drLine3 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
			drLine3.setDebitOrCredit(BaseConsts.ONE);
			drLine3.setAmount(receiveAmount);
			drLine3.setTaxRate(DecimalUtil.ZERO);
			voucherLines.add(drLine3);
		}

		VoucherLine crLine = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_2202010,
				voucher.getVoucherSummary(), BaseConsts.ONE, null); // 应付账款/外部/货款
		crLine.setDebitOrCredit(BaseConsts.TWO); // 贷

		voucherLines.add(crLine);
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		return voucherService.createVoucherDetail(voucherDetail);
	}

	public Integer outStoreBookkeeping(PurchaseOrderTitle purchaseOrderTitle,
			DistributeAccountReqDto distributeAccountReqDto, Integer createType) {
		VoucherDetail voucherDetail = new VoucherDetail();

		Voucher voucher = convertToVoucher(purchaseOrderTitle, distributeAccountReqDto, createType, BaseConsts.SEVEN);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		VoucherLine drLine = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_1223,
				voucher.getVoucherSummary(), BaseConsts.ONE, null); // 应收账款/外部/货款
		drLine.setDebitOrCredit(BaseConsts.ONE); // 借
		voucherLines.add(drLine);

		int isHome = cacheService.getAccountBookById(voucher.getAccountBookId()).getIsHome();
		if (isHome == BaseConsts.ONE) { // 国内
			List<PurchaseOrderLineTaxGroupSum> lineTaxGroupSumList = distributionOrderService
					.queryTaxGroupSumByProjectId(distributeAccountReqDto); // 项目按商品按税率分组总金额

			for (PurchaseOrderLineTaxGroupSum item : lineTaxGroupSumList) {
				BigDecimal sendAmount = DecimalUtil.formatScale2(item.getUnDistributeAmount());
				BigDecimal taxRate = item.getTaxRate();
				VoucherLine crLine1 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_6001,
						voucher.getVoucherSummary(), BaseConsts.ONE, null); // 主营业务收入/销售收入/外部销售
				crLine1.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine1.setTaxRate(taxRate);
				crLine1.setAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(sendAmount, DecimalUtil.add(taxRate, new BigDecimal("1")))));

				voucherLines.add(crLine1);
			}

			List<PurchaseOrderLineTaxGroupSum> lineTaxGroupSumList2 = distributionOrderService
					.queryTaxGroupSumByProjectId(distributeAccountReqDto); // 项目按商品按税率分组总金额
			for (PurchaseOrderLineTaxGroupSum item : lineTaxGroupSumList2) {
				BigDecimal taxRate = item.getTaxRate();
				BigDecimal cost = item.getUnDistributeAmount();
				BigDecimal amount = DecimalUtil
						.formatScale2(DecimalUtil.divide(cost, DecimalUtil.add(taxRate, new BigDecimal("1"))));

				VoucherLine drLine3 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_610801,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本/销售成本/外部销售
				drLine3.setDebitOrCredit(BaseConsts.ONE); // 借
				drLine3.setTaxRate(taxRate);
				drLine3.setAmount(amount);

				VoucherLine crLine3 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_1201,
						voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
				crLine3.setDebitOrCredit(BaseConsts.TWO); // 贷
				crLine3.setTaxRate(taxRate);
				crLine3.setAmount(amount);

				voucherLines.add(drLine3);
				voucherLines.add(crLine3);
			}
		} else if (isHome == BaseConsts.ZERO) { // 香港
			BigDecimal unDistributeAmount = distributionOrderService
					.queryUnDistributeAmountByProjectId(distributeAccountReqDto);
			BigDecimal costAmount = DecimalUtil.formatScale2(unDistributeAmount);
			BigDecimal sendAmount = DecimalUtil.formatScale2(unDistributeAmount);
			VoucherLine f_crLine1 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_6001,
					voucher.getVoucherSummary(), BaseConsts.ONE, null); // 主营业务收入/销售收入/外部销售
			f_crLine1.setDebitOrCredit(BaseConsts.TWO);
			f_crLine1.setAmount(sendAmount);
			f_crLine1.setTaxRate(DecimalUtil.ZERO);

			VoucherLine f_drLine = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_610801,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本/销售成本/外部销售
			f_drLine.setDebitOrCredit(BaseConsts.ONE);
			f_drLine.setAmount(costAmount);
			f_drLine.setTaxRate(DecimalUtil.ZERO);

			VoucherLine f_crLine2 = convertToVoucherLine(purchaseOrderTitle, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
			f_crLine2.setDebitOrCredit(BaseConsts.TWO);
			f_crLine2.setAmount(costAmount);
			f_crLine2.setTaxRate(DecimalUtil.ZERO);

			voucherLines.add(f_crLine1);
			voucherLines.add(f_drLine);
			voucherLines.add(f_crLine2);
		}

		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);

		return voucherService.createVoucherDetail(voucherDetail);
	}

	private Voucher convertToVoucher(PurchaseOrderTitle purchaseOrderTitle,
			DistributeAccountReqDto distributeAccountReqDto, Integer createType, Integer vourcherType) {
		Voucher voucher = new Voucher();
		BaseProject project = cacheService.getProjectById(purchaseOrderTitle.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, purchaseOrderTitle.getProjectId());
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
		voucher.setVoucherWord(vourcherType);

		String createName = "";
		if (createType == 1) {
			voucher.setBillType(BaseConsts.TWO); // 入库单
			createName = "入库未请款暂估凭证";
		} else if (createType == 2) {
			voucher.setBillType(BaseConsts.THREE); // 出库单
			createName = "出库未请款暂估凭证";
		}
		voucher.setVoucherSummary(createName + "/凭证日期"
				+ (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, distributeAccountReqDto.getVoucherDate())));
		voucher.setVoucherDate(distributeAccountReqDto.getVoucherDate()); // 凭证日期
		voucher.setState(BaseConsts.THREE);
		return voucher;
	}

	public VoucherLine convertToVoucherLine(PurchaseOrderTitle purchaseOrderTitle, String account_line_no,
			String voucherSummary, Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		line.setAmount(purchaseOrderTitle.getUnDistributeAmount());
		line.setProjectId(purchaseOrderTitle.getProjectId());
		line.setSupplierId(purchaseOrderTitle.getSupplierId());
		line.setCustId(purchaseOrderTitle.getCustomerId());
		line.setCurrencyType(purchaseOrderTitle.getCurrencyId());
		BaseProject baseProject = cacheService.getProjectById(purchaseOrderTitle.getProjectId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, baseProject.getBusinessUnitId(),
				bankId, type, purchaseOrderTitle.getCurrencyId());
		line.setAccountLineId(accountLine.getId());
		line.setVoucherLineSummary(voucherSummary);
		return line;
	}

	/**
	 * 
	 * 红冲凭证
	 * 
	 * @param voucherId
	 * @param voucherDate
	 */
	public void createRedVoucherById(Integer voucherId, Date voucherDate, PurchaseOrderTitle purchaseOrderTitle) {
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
		BaseProject project = cacheService.getProjectById(purchaseOrderTitle.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, purchaseOrderTitle.getProjectId());
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(project.getBusinessUnitId());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, project.getBusinessUnitId());
		}
		List<VoucherLine> voucherLines = voucherLineDao.queryResultsByVoucherId(voucherId);
		VoucherDetail voucherDetail = new VoucherDetail();
		BigDecimal multiplier = new BigDecimal("-1");
		voucher.setVoucherDate(voucherDate);
		voucher.setVoucherSummary(voucher.getVoucherSummary() + "-红冲");
		voucher.setState(BaseConsts.THREE);
		voucher.setId(null);
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucherDetail.setVoucher(voucher);

		for (VoucherLine entity : voucherLines) {
			entity.setAmount(DecimalUtil.multiply(entity.getAmount(), multiplier));
			entity.setId(null);
			entity.setVoucherId(null);
		}
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.createVoucherDetail(voucherDetail, false);
	}
}
