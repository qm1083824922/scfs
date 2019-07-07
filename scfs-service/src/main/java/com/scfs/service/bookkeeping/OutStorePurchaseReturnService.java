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
import com.scfs.domain.logistics.entity.BillOutStoreTaxGroupCostPrice;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AccountLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: OutStorePurchaseReturnService.java
 *  Description: 采购退货记账
 *  TODO
 *  Date,					Who,				
 *  2017年06月01日			Administrator
 *
 * </pre>
 */
@Service
public class OutStorePurchaseReturnService {

	@Autowired
	BillOutStoreDao billOutStoreDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	BillOutStorePickDtlService billOutStorePickDtlService;

	@Autowired
	VoucherService voucherService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private AccountLineService accountLineService;

	/**
	 * 采购退货的记账
	 * 
	 * @param id
	 * @return
	 */
	public Integer OutStorePurchaseReturn(int id) {
		BillOutStoreSearchReqDto req = new BillOutStoreSearchReqDto();
		req.setId(id);
		BillOutStore billOutStore = billOutStoreDao.queryById(req);
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = convertToVoucher(billOutStore, BaseConsts.SEVEN);
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		// 新凭证规则记账 获取成本单价和出库单价
		List<BillOutStoreTaxGroupCostPrice> priceList = billOutStorePickDtlService.queryPickDtlByOutId(id);
		if (CollectionUtils.isEmpty(priceList)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购退货记账拣货明细为空");
		}
		for (BillOutStoreTaxGroupCostPrice billOutStoreTaxGroupCostPrice : priceList) {
			// 记录凭证 算出发货金额和成本金额
			BigDecimal amount = DecimalUtil.subtract(billOutStoreTaxGroupCostPrice.getSendPriceAmount(),
					billOutStoreTaxGroupCostPrice.getCostPriceAmount());

			BigDecimal sAmount = DecimalUtil.multiply(new BigDecimal("-1"), amount);
			VoucherLine drLine = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 商品存货
			drLine.setDebitOrCredit(BaseConsts.ONE); // 借
			drLine.setAmount(amount);
			voucherLines.add(drLine);

			VoucherLine drLine1 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_610801,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 主营业务成本
			drLine1.setDebitOrCredit(BaseConsts.ONE); // 借
			drLine1.setAmount(sAmount);
			drLine1.setTaxRate(billOutStoreTaxGroupCostPrice.getTaxRate());
			voucherLines.add(drLine1);

			VoucherLine drLine2 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 商品存货
			drLine2.setDebitOrCredit(BaseConsts.ONE); // 借
			drLine2.setAmount(sAmount);
			voucherLines.add(drLine2);

			VoucherLine drLine3 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_220201,
					voucher.getVoucherSummary(), BaseConsts.ONE, null); // 应付账款
			drLine3.setDebitOrCredit(BaseConsts.TWO); // 借
			drLine3.setAmount(sAmount);
			voucherLines.add(drLine3);
			// 记录商品库存
			VoucherLine crLine1 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_1201,
					voucher.getVoucherSummary(), BaseConsts.THREE, null); // 库存商品
			crLine1.setDebitOrCredit(BaseConsts.ONE); // 贷
			crLine1.setAmount(DecimalUtil.multiply(new BigDecimal("-1"),
					DecimalUtil.divide(billOutStoreTaxGroupCostPrice.getCostPriceAmount(),
							DecimalUtil.add(new BigDecimal("1"), billOutStoreTaxGroupCostPrice.getTaxRate()))));
			voucherLines.add(crLine1);
			// （成本单价/1+税）*税
			BigDecimal costAmount = DecimalUtil.multiply(
					DecimalUtil.divide(billOutStoreTaxGroupCostPrice.getCostPriceAmount(),
							DecimalUtil.add(new BigDecimal("1"), billOutStoreTaxGroupCostPrice.getTaxRate())),
					billOutStoreTaxGroupCostPrice.getTaxRate());

			// 应付账款\待抵扣进项税额（暂估） 退货税
			VoucherLine crLine2 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_220205,
					voucher.getVoucherSummary(), BaseConsts.THREE, null);
			crLine2.setDebitOrCredit(BaseConsts.ONE); // 贷
			crLine2.setAmount(DecimalUtil.multiply(new BigDecimal("-1"), costAmount));
			voucherLines.add(crLine2);

			// 应付账款\货款\CNY
			VoucherLine crLine3 = convertToVoucherLine(billOutStore, AccountNoConsts.ACCOUNT_NO_220201,
					voucher.getVoucherSummary(), BaseConsts.ONE, null); // 库存商品
			crLine3.setDebitOrCredit(BaseConsts.TWO); // 贷
			crLine3.setAmount(
					DecimalUtil.multiply(new BigDecimal("-1"), billOutStoreTaxGroupCostPrice.getCostPriceAmount()));
			voucherLines.add(crLine3);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		return voucherService.createVoucherDetail(voucherDetail);
	}

	/**
	 * 封装凭证的数据
	 * 
	 * @param billOutStore
	 * @return
	 */
	private Voucher convertToVoucher(BillOutStore billOutStore, Integer vourcherType) {
		Voucher voucher = new Voucher();
		// 根据项目的ID获取项目信息
		BaseProject project = cacheService.getProjectById(billOutStore.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, billOutStore.getProjectId());
		}
		// 根据项目的经营单位获取客户信息
		BaseSubject busiUnit = cacheService.getBusiUnitById(project.getBusinessUnitId());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, project.getBusinessUnitId());
		}
		// 根据经营单位的ID 获取帐套信息
		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(busiUnit.getId());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, busiUnit.getId());
		}
		voucher.setAccountBookId(accountBook.getId());// 帐套的ID
		voucher.setVoucherNo(sequenceService.getNumDateByBusName(busiUnit.getSubjectNo(), SeqConsts.S_VOUCHER_NO,
				BaseConsts.INT_13));
		voucher.setBusiUnit(busiUnit.getId());// 经营单位的ID
		voucher.setOutStoreId(billOutStore.getId());// 出库单的ID
		voucher.setVoucherWord(vourcherType);// 凭证字
		Date billDate = billOutStore.getSendDate() == null ? new Date() : billOutStore.getSendDate();
		voucher.setVoucherSummary(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.THREE) + "")
				+ billOutStore.getBillNo() + "/单据日期" + (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, billDate)));
		voucher.setBillNo(billOutStore.getBillNo());// 单据编号
		voucher.setBillType(BaseConsts.THREE); // 出库单
		voucher.setBillDate(billDate); // 单据日期
		voucher.setVoucherDate(billDate); // 凭证日期
		voucher.setState(BaseConsts.THREE);// 已完成
		return voucher;
	}

	/**
	 * 采购退货出库分录信息的封装
	 * 
	 * @param billOutStore
	 * @param account_line_no
	 * @param voucherSummary
	 * @return
	 */
	public VoucherLine convertToVoucherLine(BillOutStore billOutStore, String account_line_no, String voucherSummary,
			Integer type, Integer bankId) {
		VoucherLine line = new VoucherLine();
		line.setAmount(billOutStore.getSendAmount());// 发货的金额
		line.setProjectId(billOutStore.getProjectId());// 项目的id
		line.setSupplierId(billOutStore.getCustomerId());// 客户的ID
		line.setCustId(billOutStore.getCustomerId());// 客户的ID
		line.setCurrencyType(billOutStore.getCurrencyType());// 币种
		line.setOutStoreId(billOutStore.getId());// 出库单的id
		BaseProject baseProject = cacheService.getProjectById(billOutStore.getProjectId());
		AccountLine accountLine = accountLineService.queryAccountLine(account_line_no, baseProject.getBusinessUnitId(),
				bankId, type, billOutStore.getCurrencyType());
		line.setAccountLineId(accountLine.getId());// 科目的ID
		line.setVoucherLineSummary(voucherSummary);// 摘要
		return line;
	}

}
