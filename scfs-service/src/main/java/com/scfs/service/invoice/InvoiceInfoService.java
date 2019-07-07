package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseInvoiceDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceApplyDao;
import com.scfs.dao.invoice.InvoiceDtlInfoDao;
import com.scfs.dao.invoice.InvoiceFeeDao;
import com.scfs.dao.invoice.InvoiceInfoDao;
import com.scfs.dao.invoice.InvoiceSaleDao;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceDtlInfo;
import com.scfs.domain.invoice.entity.InvoiceFeeManager;
import com.scfs.domain.invoice.entity.InvoiceInfo;
import com.scfs.domain.invoice.entity.InvoiceInfoDtl;
import com.scfs.domain.invoice.entity.InvoiceSaleManager;
import com.scfs.domain.result.PageResult;
import com.scfs.service.bookkeeping.InvoiceBookkeepingService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;

@Service
public class InvoiceInfoService {

	@Autowired
	private InvoiceSaleDao invoiceSaleDao;
	@Autowired
	private InvoiceInfoDao invoiceInfoDao;
	@Autowired
	private InvoiceDtlInfoDao invoiceDtlInfoDao;
	@Autowired
	private InvoiceApplyDao invoiceApplyDao;
	@Autowired
	private InvoiceFeeDao invoiceFeeDao;
	@Autowired
	private BaseInvoiceDao baseInvoiceDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private InvoiceApplyService invoiceApplyService;
	@Autowired
	private InvoiceBookkeepingService invoiceBookkeepingService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	public PageResult<InvoiceInfoDtl> insertSimulationInvoiceInfo(Integer invopiceApplyId) {
		PageResult<InvoiceInfoDtl> rs = null;
		InvoiceApplyManager invoiceApply = invoiceApplyDao.queryEntityById(invopiceApplyId);
		if (invoiceApply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		// 根据经营单位ID获取经营单位
		BaseSubject busiUnit = cacheService.getBusiUnitById(invoiceApply.getBusinessUnitId());
		if (invoiceApply.getBillType() == BaseConsts.ONE) {
			rs = addGoodsInfo(invopiceApplyId, busiUnit);
		} else if (invoiceApply.getBillType() == BaseConsts.TWO) {
			rs = addFeeInfo(invopiceApplyId, busiUnit);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "类型不正确");
		}
		invoiceApply.setStatus(BaseConsts.TWO);
		invoiceApplyService.updateInvoiceApplyStatus(invoiceApply);
		return rs;
	}

	/**
	 * 插入货物模拟信息数据
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private PageResult<InvoiceInfoDtl> addGoodsInfo(Integer invoiceApplyId, BaseSubject busiUnit) {

		InvoiceApplyManager invoiceApply = invoiceApplyDao.queryEntityById(invoiceApplyId);

		// 查询该发票对应的货物信息
		List<InvoiceSaleManager> results = invoiceSaleDao.selectByInvoiceId(invoiceApplyId);
		if (CollectionUtils.isEmpty(results)) {
			throw new BaseException(ExcMsgEnum.INVOICE_DETAIL_EXCEPTION);
		}
		// 查找相同税率的货物
		Map<BigDecimal, List<InvoiceSaleManager>> taxRateMap = new HashMap<BigDecimal, List<InvoiceSaleManager>>();
		for (int i = 0; i < results.size(); i++) {
			BaseGoods product = cacheService.getGoodsById(results.get(i).getGoodsId());
			BigDecimal rate = product.getTaxRate();
			if (taxRateMap.containsKey(rate)) {
				taxRateMap.get(rate).add(results.get(i));
			} else {
				List<InvoiceSaleManager> ids = new ArrayList<InvoiceSaleManager>();
				ids.add(results.get(i));
				taxRateMap.put(rate, ids);
			}
		}

		Iterator<BigDecimal> keys_taxRate = taxRateMap.keySet().iterator();
		if (busiUnit == null || DecimalUtil.le(busiUnit.getInvoiceQuotaType(), BigDecimal.ZERO)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位为空且开票限额为空");
		}
		int num = 0;
		while (keys_taxRate.hasNext()) {
			BigDecimal taxRate = keys_taxRate.next();
			BigDecimal invoiceMax = busiUnit.getInvoiceQuotaType().multiply(DecimalUtil.ONE.add(taxRate)).setScale(2,
					BigDecimal.ROUND_DOWN);
			List<InvoiceSaleManager> invoicels = taxRateMap.get(taxRate);

			Iterator<InvoiceSaleManager> invoicels2 = invoicels.iterator();

			if (invoiceApply.getIsGoodsMerge() == BaseConsts.ONE) {
				Map<Integer, InvoiceSaleManager> productMap = new HashMap<Integer, InvoiceSaleManager>();
				// 是否同品合并处理
				for (int i = 0; i < invoicels.size(); i++) {
					InvoiceSaleManager vo = invoicels.get(i);

					if (productMap.containsKey(vo.getGoodsId())) {
						InvoiceSaleManager vo2 = productMap.get(vo.getGoodsId());
						vo2.setProvideInvoiceNum(vo2.getProvideInvoiceNum().add(vo.getProvideInvoiceNum()));
						vo2.setProvideInvoiceAmount(vo2.getProvideInvoiceAmount().add(vo.getProvideInvoiceAmount()));
						vo2.setDiscountInRateAmount(vo2.getDiscountInRateAmount().add(vo.getDiscountInRateAmount()));
					} else {
						productMap.put(vo.getGoodsId(), vo);
					}
				}
				invoicels2 = productMap.values().iterator();
			}

			List<InvoiceSaleManager> invoicels3 = new ArrayList<InvoiceSaleManager>();

			BigDecimal pmoney = DecimalUtil.ZERO;// 多余出来的
			BigDecimal num_total = DecimalUtil.ZERO;
			BigDecimal amount_total = DecimalUtil.ZERO;
			BigDecimal discount_total = DecimalUtil.ZERO;

			// 处理0数量和0金额
			while (invoicels2.hasNext()) {
				InvoiceSaleManager vo = invoicels2.next();
				BigDecimal subDidscountAmount = vo.getProvideInvoiceAmount().subtract(vo.getDiscountInRateAmount());
				// 处理顺序不能变
				if (vo.getProvideInvoiceNum().compareTo(DecimalUtil.ZERO) == 0) {
					pmoney = pmoney.add(subDidscountAmount);
					continue;
				}
				if (subDidscountAmount.compareTo(DecimalUtil.ZERO) == 0) {
					continue;
				}
				if (subDidscountAmount.compareTo(DecimalUtil.ZERO) < 0) {
					pmoney = pmoney.add(vo.getDiscountInRateAmount());
					vo.setDiscountInRateAmount(DecimalUtil.ZERO);
				}

				num_total = num_total.add(vo.getProvideInvoiceNum());
				amount_total = amount_total.add(vo.getProvideInvoiceAmount());
				discount_total = discount_total.add(vo.getDiscountInRateAmount());
				invoicels3.add(vo);
			}

			// 如果多出来的钱不等于0，需要按等比例分摊到每个明细上
			if (pmoney.compareTo(DecimalUtil.ZERO) != 0) {
				BigDecimal rate = pmoney.divide(amount_total.subtract(discount_total), 8, BigDecimal.ROUND_HALF_UP);
				BigDecimal remain_pmoney = DecimalUtil.ZERO;
				for (int i = 0; i < invoicels3.size(); i++) {
					InvoiceSaleManager vo = invoicels3.get(i);
					if (i == invoicels3.size() - 1) {
						vo.setProvideInvoiceAmount(
								vo.getProvideInvoiceAmount().subtract(pmoney.subtract(remain_pmoney)));
					} else {
						BigDecimal subDidscountAmount = vo.getProvideInvoiceAmount()
								.subtract(vo.getDiscountInRateAmount());
						BigDecimal provideInvoiceAmount = subDidscountAmount.multiply(rate).setScale(2);
						remain_pmoney = remain_pmoney.add(provideInvoiceAmount);
						vo.setProvideInvoiceAmount(vo.getProvideInvoiceAmount().subtract(provideInvoiceAmount));
					}
				}
			}

			// 拆分生成发票信息
			BigDecimal invoice_useamont = DecimalUtil.ZERO;
			List<List<InvoiceSaleManager>> invoices = new ArrayList<List<InvoiceSaleManager>>();
			List<InvoiceSaleManager> invoicels4 = null;
			List<InvoiceSaleManager> invoicels5 = null;

			do {
				invoicels5 = new ArrayList<InvoiceSaleManager>();
				invoicels4 = new ArrayList<InvoiceSaleManager>();
				invoice_useamont = DecimalUtil.ZERO;

				for (int i = 0; i < invoicels3.size(); i++) {
					InvoiceSaleManager vo = invoicels3.get(i);
					BigDecimal subDidscountAmount = vo.getProvideInvoiceAmount().subtract(vo.getDiscountInRateAmount());
					if ((invoice_useamont.add(subDidscountAmount)).compareTo(invoiceMax) > 0) {
						BigDecimal amount = invoiceMax.subtract(invoice_useamont);
						BigDecimal price = subDidscountAmount.divide(vo.getProvideInvoiceNum(), 8,
								BigDecimal.ROUND_HALF_UP);
						BigDecimal qty = amount.divide(price, 0, BigDecimal.ROUND_DOWN);// 数量向下取整
						if (qty.compareTo(DecimalUtil.ZERO) == 0) {
							qty = amount.divide(vo.getProvideInvoiceNum(), 8, BigDecimal.ROUND_DOWN);
						}
						InvoiceSaleManager vo2 = new InvoiceSaleManager();
						vo2.setDiscountInRateAmount(vo.getDiscountInRateAmount());
						vo2.setProvideInvoiceAmount(vo.getProvideInvoiceAmount());
						vo2.setProvideInvoiceNum(vo.getProvideInvoiceNum());
						vo2.setGoodsId(vo.getGoodsId());
						vo2.setInvoiceApplyId(vo.getInvoiceApplyId());
						BigDecimal t_amount = price.multiply(qty).setScale(2, BigDecimal.ROUND_DOWN);
						vo2.setProvideInvoiceNum(qty);
						vo2.setProvideInvoiceAmount(t_amount);
						vo2.setDiscountInRateAmount(DecimalUtil.ZERO);
						invoicels4.add(vo2);

						vo.setProvideInvoiceNum(vo.getProvideInvoiceNum().subtract(qty));
						vo.setProvideInvoiceAmount(vo.getProvideInvoiceAmount().subtract(t_amount));
						vo.setDiscountInRateAmount(DecimalUtil.ZERO);
						invoicels5.add(vo);
						if (i != invoicels3.size() - 1) {
							invoicels5.addAll(invoicels3.subList(i + 1, invoicels3.size()));
						}
						break;

					} else {
						invoice_useamont = invoice_useamont.add(subDidscountAmount);
						invoicels4.add(vo);
					}
				}
				invoices.add(invoicels4);
				invoicels3 = invoicels5;
			} while (invoicels3.size() > 0);

			for (int i = 0; i < invoices.size(); i++) {
				InvoiceInfo invoiceInfo = new InvoiceInfo();
				invoiceInfo.setCustomerId(invoiceApply.getCustomerId());
				invoiceInfo.setInvoiceApplyId(invoiceApply.getId());
				invoiceInfo.setInvoiceApplyNo(invoiceApply.getApplyNo());
				invoiceInfo.setBaseInvoiceId(invoiceApply.getBaseInvoiceId());
				num = num + 1;
				invoiceInfo.setInvoiceNo(invoiceApply.getApplyNo() + BaseConsts.CONJUNCTION_FLAG + num);
				invoiceInfo.setInvoiceRemark(invoiceApply.getInvoiceRemark());
				invoiceInfo.setCreateAt(new Date());
				invoiceInfo.setCreator(ServiceSupport.getUser().getChineseName());
				invoiceInfo.setCreatorId(ServiceSupport.getUser().getId());
				invoiceInfo.setStatus(BaseConsts.ONE);
				Integer info = invoiceInfoDao.insert(invoiceInfo);
				if (info < 0) {
					throw new BaseException(ExcMsgEnum.INVOICE_SIMULATE_ADD_EXCEPTION);
				}

				BigDecimal inRateAmount = DecimalUtil.ZERO;// 含税金额
				BigDecimal exRateAmount = DecimalUtil.ZERO;// 未税金额
				BigDecimal rateAmount = DecimalUtil.ZERO;// 税额
				BigDecimal discountInRateAmount = DecimalUtil.ZERO;// 折扣含税金额
				BigDecimal discountExRateAmount = DecimalUtil.ZERO;// 折扣未税金额
				BigDecimal discountRateAmount = DecimalUtil.ZERO;// 折扣税额

				List<InvoiceSaleManager> ls = invoices.get(i);

				for (int j = 0; j < ls.size(); j++) {
					InvoiceSaleManager line = ls.get(j);
					InvoiceDtlInfo invoiceDtlInfo = new InvoiceDtlInfo();
					BaseGoods goods = cacheService.getGoodsById(line.getGoodsId());
					BigDecimal subdiscountAmount = line.getProvideInvoiceAmount()
							.subtract(line.getDiscountInRateAmount()).setScale(2);
					invoiceDtlInfo.setName(goods.getName());
					invoiceDtlInfo.setType(goods.getType());
					invoiceDtlInfo.setUnit(goods.getUnit());
					invoiceDtlInfo.setSpecification(goods.getSpecification());
					invoiceDtlInfo.setTaxCateNo(goods.getTaxClassification());

					// 显示折扣的
					if (Integer.valueOf(BaseConsts.ONE).compareTo(invoiceApply.getIsDisplayDiscount()) == 0) {
						if (invoiceApply.getDiscount() != null
								&& invoiceApply.getDiscount().compareTo(DecimalUtil.ZERO) != 0) {
							// 新未扣除折扣的含税金额=含税金额/(1-折扣率)
							BigDecimal addDiscountAmount = subdiscountAmount.divide(
									(DecimalUtil.ONE
											.subtract(invoiceApply.getDiscount().divide(new BigDecimal("100")))),
									2, BigDecimal.ROUND_HALF_UP);

							invoiceDtlInfo.setInRateAmount(addDiscountAmount);
							BigDecimal exRateAmount_tmp = addDiscountAmount.divide((DecimalUtil.ONE.add(taxRate)), 2,
									BigDecimal.ROUND_HALF_UP);
							invoiceDtlInfo.setExRateAmount(exRateAmount_tmp);
							BigDecimal rateAmount_tmp = addDiscountAmount.subtract(exRateAmount_tmp);
							invoiceDtlInfo.setRateAmount(rateAmount_tmp);

							BigDecimal addDiscount = addDiscountAmount
									.multiply(invoiceApply.getDiscount().divide(new BigDecimal("100")));

							invoiceDtlInfo.setDiscountInRateAmount(addDiscount);
							BigDecimal discountExRateAmount_tmp = addDiscount.divide((DecimalUtil.ONE.add(taxRate)), 2,
									BigDecimal.ROUND_HALF_UP);
							invoiceDtlInfo.setDiscountExRateAmount(discountExRateAmount_tmp);
							BigDecimal discountRateAmount_tmp = addDiscount.subtract(discountExRateAmount_tmp);
							invoiceDtlInfo.setDiscountRateAmount(discountRateAmount_tmp);
						} else {
							invoiceDtlInfo.setInRateAmount(line.getAcceptInvoiceAmount());
							BigDecimal exRateAmount_tmp = line.getAcceptInvoiceAmount()
									.divide((DecimalUtil.ONE.add(taxRate)), 2, BigDecimal.ROUND_HALF_UP);
							invoiceDtlInfo.setExRateAmount(exRateAmount_tmp);
							BigDecimal rateAmount_tmp = line.getAcceptInvoiceAmount().subtract(exRateAmount_tmp);
							invoiceDtlInfo.setRateAmount(rateAmount_tmp);

							if (line.getDiscountInRateAmount() != null
									&& line.getDiscountInRateAmount().compareTo(DecimalUtil.ZERO) != 0) {
								invoiceDtlInfo.setDiscountInRateAmount(line.getDiscountInRateAmount());
								BigDecimal discountExRateAmount_tmp = line.getDiscountInRateAmount()
										.divide((DecimalUtil.ONE.add(taxRate)), 2, BigDecimal.ROUND_HALF_UP);
								invoiceDtlInfo.setDiscountExRateAmount(discountExRateAmount_tmp);
								BigDecimal discountRateAmount_tmp = line.getDiscountInRateAmount()
										.subtract(discountExRateAmount_tmp);
								invoiceDtlInfo.setDiscountRateAmount(discountRateAmount_tmp);
							} else {
								invoiceDtlInfo.setDiscountInRateAmount(DecimalUtil.ZERO);
								invoiceDtlInfo.setDiscountExRateAmount(DecimalUtil.ZERO);
								invoiceDtlInfo.setDiscountRateAmount(DecimalUtil.ZERO);
							}
						}
					} else {
						invoiceDtlInfo.setInRateAmount(subdiscountAmount);
						BigDecimal exRateAmount_tmp = subdiscountAmount.divide((DecimalUtil.ONE.add(taxRate)), 2,
								BigDecimal.ROUND_HALF_UP);
						invoiceDtlInfo.setExRateAmount(exRateAmount_tmp);
						BigDecimal rateAmount_tmp = subdiscountAmount.subtract(exRateAmount_tmp);
						invoiceDtlInfo.setRateAmount(rateAmount_tmp);

						invoiceDtlInfo.setDiscountInRateAmount(DecimalUtil.ZERO);
						invoiceDtlInfo.setDiscountExRateAmount(DecimalUtil.ZERO);
						invoiceDtlInfo.setDiscountRateAmount(DecimalUtil.ZERO);

					}

					invoiceDtlInfo.setDiscount(invoiceDtlInfo.getDiscountRateAmount()
							.divide(invoiceDtlInfo.getRateAmount(), 6, BigDecimal.ROUND_HALF_UP));
					invoiceDtlInfo.setNum(line.getProvideInvoiceNum());
					invoiceDtlInfo.setPrice(invoiceDtlInfo.getExRateAmount().divide(line.getProvideInvoiceNum(), 8,
							BigDecimal.ROUND_HALF_DOWN));
					invoiceDtlInfo.setRate(goods.getTaxRate());
					invoiceDtlInfo.setInvoiceId(invoiceInfo.getId());
					invoiceDtlInfoDao.insert(invoiceDtlInfo);

					inRateAmount = inRateAmount.add(invoiceDtlInfo.getInRateAmount());// 含税金额
					exRateAmount = exRateAmount.add(invoiceDtlInfo.getExRateAmount());// 未税金额
					rateAmount = rateAmount.add(invoiceDtlInfo.getRateAmount());// 税额
					discountInRateAmount = discountInRateAmount.add(invoiceDtlInfo.getDiscountInRateAmount());// 折扣含税金额
					discountExRateAmount = discountExRateAmount.add(invoiceDtlInfo.getDiscountExRateAmount());// 折扣未税金额
					discountRateAmount = discountRateAmount.add(invoiceDtlInfo.getDiscountRateAmount());// 折扣税额
				}
				invoiceInfo.setInRateAmount(inRateAmount);
				invoiceInfo.setExRateAmount(exRateAmount);
				invoiceInfo.setRateAmount(rateAmount);
				invoiceInfo.setDiscountInRateAmount(discountInRateAmount);
				invoiceInfo.setDiscountExRateAmount(discountExRateAmount);
				invoiceInfo.setDiscountRateAmount(discountRateAmount);
				invoiceInfoDao.updateById(invoiceInfo);
			}

		}

		PageResult<InvoiceInfoDtl> result = queryInvoiceResultsByCon(invoiceApplyId);
		return result;
	}

	private PageResult<InvoiceInfoDtl> addFeeInfo(Integer invoiceApplyId, BaseSubject busiUnit) {
		InvoiceApplyManager invoiceApply = invoiceApplyDao.queryEntityById(invoiceApplyId);

		BigDecimal totalamount = DecimalUtil.ZERO;
		// 查询该发票对应的费用信息
		List<InvoiceFeeManager> results = invoiceFeeDao.selectByInvoiceId(invoiceApplyId);
		if (CollectionUtils.isEmpty(results)) {
			throw new BaseException(ExcMsgEnum.INVOICE_DETAIL_EXCEPTION);
		}
		for (int i = 0; i < results.size(); i++) {
			InvoiceFeeManager vo = results.get(i);
			totalamount = totalamount.add(vo.getProvideInvoiceAmount());
		}

		if (totalamount.compareTo(DecimalUtil.ZERO) > 0) {
			if (busiUnit == null || DecimalUtil.le(busiUnit.getInvoiceQuotaType(), BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位为空且开票限额为空");
			}
			BigDecimal invoiceMax = busiUnit.getInvoiceQuotaType()
					.multiply(DecimalUtil.ONE.add(invoiceApply.getInvoiceTaxRate())).setScale(2, BigDecimal.ROUND_DOWN);
			List<InvoiceFeeManager> invoicels = new ArrayList<InvoiceFeeManager>();
			BigDecimal remainamount = totalamount;
			int num = 0;
			do {

				if (remainamount.compareTo(invoiceMax) > 0) {
					BigDecimal voiceamount = invoiceMax;
					remainamount = remainamount.subtract(voiceamount);
					InvoiceFeeManager vo = new InvoiceFeeManager();
					vo.setInvoiceApplyId(invoiceApplyId);
					vo.setProvideInvoiceAmount(voiceamount);
					invoicels.add(vo);
				} else {
					BigDecimal voiceamount = remainamount;
					remainamount = remainamount.subtract(voiceamount);
					InvoiceFeeManager vo = new InvoiceFeeManager();
					vo.setInvoiceApplyId(invoiceApplyId);
					vo.setProvideInvoiceAmount(voiceamount);
					invoicels.add(vo);
				}

			} while (remainamount.compareTo(DecimalUtil.ZERO) > 0);

			for (int i = 0; i < invoicels.size(); i++) {

				BigDecimal inRateAmount = DecimalUtil.ZERO;// 含税金额
				BigDecimal exRateAmount = DecimalUtil.ZERO;// 未税金额
				BigDecimal rateAmount = DecimalUtil.ZERO;// 税额

				InvoiceInfo invoiceInfo = new InvoiceInfo();
				invoiceInfo.setCustomerId(invoiceApply.getCustomerId());
				invoiceInfo.setInvoiceApplyId(invoiceApply.getId());
				invoiceInfo.setInvoiceApplyNo(invoiceApply.getApplyNo());
				invoiceInfo.setBaseInvoiceId(invoiceApply.getBaseInvoiceId());
				num = num + 1;
				invoiceInfo.setInvoiceNo(invoiceApply.getApplyNo() + BaseConsts.CONJUNCTION_FLAG + num);
				invoiceInfo.setInvoiceRemark(invoiceApply.getInvoiceRemark());
				invoiceInfo.setCreateAt(new Date());
				invoiceInfo.setCreator(ServiceSupport.getUser().getChineseName());
				invoiceInfo.setCreatorId(ServiceSupport.getUser().getId());
				invoiceInfo.setStatus(BaseConsts.ONE);
				Integer info = invoiceInfoDao.insert(invoiceInfo);
				if (info < 0) {
					throw new BaseException(ExcMsgEnum.INVOICE_SIMULATE_ADD_EXCEPTION);
				}

				InvoiceFeeManager line = invoicels.get(i);
				InvoiceDtlInfo invoiceDtlInfo = new InvoiceDtlInfo();
				BigDecimal subdiscountAmount = line.getProvideInvoiceAmount().setScale(2);
				invoiceDtlInfo.setName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_DETAIL, invoiceApply.getFeeType() + ""));
				invoiceDtlInfo.setType("");
				invoiceDtlInfo.setUnit("次");
				invoiceDtlInfo.setSpecification("");
				invoiceDtlInfo.setTaxCateNo(invoiceApply.getTaxCateNo() + "");

				invoiceDtlInfo.setInRateAmount(subdiscountAmount);
				BigDecimal exRateAmount_tmp = subdiscountAmount
						.divide((DecimalUtil.ONE.add(invoiceApply.getInvoiceTaxRate())), 2, BigDecimal.ROUND_HALF_UP);
				invoiceDtlInfo.setExRateAmount(exRateAmount_tmp);
				BigDecimal rateAmount_tmp = subdiscountAmount.subtract(exRateAmount_tmp);
				invoiceDtlInfo.setRateAmount(rateAmount_tmp);
				invoiceDtlInfo.setDiscountInRateAmount(DecimalUtil.ZERO);
				invoiceDtlInfo.setDiscountExRateAmount(DecimalUtil.ZERO);
				invoiceDtlInfo.setDiscountRateAmount(DecimalUtil.ZERO);

				invoiceDtlInfo.setDiscount(invoiceDtlInfo.getDiscountRateAmount().divide(invoiceDtlInfo.getRateAmount(),
						6, BigDecimal.ROUND_HALF_UP));
				invoiceDtlInfo.setNum(DecimalUtil.ONE);
				invoiceDtlInfo.setPrice(invoiceDtlInfo.getExRateAmount());
				invoiceDtlInfo.setRate(invoiceApply.getInvoiceTaxRate());
				invoiceDtlInfo.setInvoiceId(invoiceInfo.getId());
				invoiceDtlInfoDao.insert(invoiceDtlInfo);

				inRateAmount = inRateAmount.add(invoiceDtlInfo.getInRateAmount());// 含税金额
				exRateAmount = exRateAmount.add(invoiceDtlInfo.getExRateAmount());// 未税金额
				rateAmount = rateAmount.add(invoiceDtlInfo.getRateAmount());// 税额

				invoiceInfo.setInRateAmount(inRateAmount);
				invoiceInfo.setExRateAmount(exRateAmount);
				invoiceInfo.setRateAmount(rateAmount);
				invoiceInfo.setDiscountInRateAmount(DecimalUtil.ZERO);
				invoiceInfo.setDiscountExRateAmount(DecimalUtil.ZERO);
				invoiceInfo.setDiscountRateAmount(DecimalUtil.ZERO);
				invoiceInfoDao.updateById(invoiceInfo);
			}
		}

		PageResult<InvoiceInfoDtl> result = queryInvoiceResultsByCon(invoiceApplyId);
		return result;
	}

	/**
	 * 删除模拟信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteSimulateInvoiceInfo(Integer invopiceApplyId) {
		InvoiceApplyManager invoiceApply = invoiceApplyDao.queryEntityById(invopiceApplyId);
		if (invoiceApply.getStatus().compareTo(BaseConsts.TWO) != 0
				&& invoiceApply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}

		List<InvoiceInfo> invoiceInfo = invoiceInfoDao.selectByApplyId(invopiceApplyId);
		if (CollectionUtils.isNotEmpty(invoiceInfo)) {
			for (int i = 0; i < invoiceInfo.size(); i++) {
				List<InvoiceDtlInfo> invoiceDtlInfoList = invoiceDtlInfoDao.selectByApplyId(invoiceInfo.get(i).getId());
				invoiceInfoDao.deleteById(invoiceInfo.get(i).getId());
				for (int j = 0; j < invoiceDtlInfoList.size(); j++)
					invoiceDtlInfoDao.deleteById(invoiceDtlInfoList.get(j).getId());
			}
		}
		invoiceApply.setStatus(BaseConsts.ONE);
		invoiceApplyService.updateInvoiceApplyStatus(invoiceApply);
	}

	/**
	 * 查询发票信息
	 * 
	 * @param invoiceApplyId
	 * @return
	 * @throws Exception
	 */
	public PageResult<InvoiceInfoDtl> queryInvoiceResultsByCon(Integer invoiceApplyId) {
		BaseInvoice invoice = new BaseInvoice();
		InvoiceApplyManager applyManager = invoiceApplyDao.queryEntityById(invoiceApplyId);
		List<InvoiceInfoDtl> invoiceInfoDtlList = new LinkedList<InvoiceInfoDtl>();
		PageResult<InvoiceInfoDtl> result = new PageResult<InvoiceInfoDtl>();
		List<InvoiceInfo> invoiceInfo = invoiceInfoDao.selectByApplyId(invoiceApplyId);
		for (int i = 0; i < invoiceInfo.size(); i++) {
			if (applyManager.getBaseInvoiceId() != null) {
				invoice = baseInvoiceDao.queryInvoiceById(applyManager.getBaseInvoiceId());
				invoiceInfo.get(i).setPhoneNumber(invoice.getPhoneNumber());
				invoiceInfo.get(i).setAccountNo(invoice.getAccountNo());
				invoiceInfo.get(i).setBankName(invoice.getBankName());
				invoiceInfo.get(i).setAddress(invoice.getAddress());
			}
			invoiceInfo.get(i).setStatus(applyManager.getStatus());
			invoiceInfo.get(i).setTaxPay(invoice.getTaxPayer());
			invoiceInfo.get(i).setInRateAmount(BigDecimal.ZERO);
			invoiceInfo.get(i).setExRateAmount(BigDecimal.ZERO);
			invoiceInfo.get(i).setRateAmount(BigDecimal.ZERO);
			invoiceInfo.get(i)
					.setCustomerName(cacheService.getCustomerById(applyManager.getCustomerId()).getChineseName());
			invoiceInfo.get(i).setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_STATUS,
					invoiceInfo.get(i).getStatus() + ""));
			InvoiceInfoDtl invoiceInfoDtl = new InvoiceInfoDtl();
			invoiceInfoDtl.setInvoiceInfoList(invoiceInfo.get(i));
			List<InvoiceDtlInfo> invoiceDtlInfoList = invoiceDtlInfoDao.selectByApplyId(invoiceInfo.get(i).getId());
			for (int j = 0; j < invoiceDtlInfoList.size(); j++) {
				invoiceInfo.get(i).setInRateAmount(
						invoiceInfo.get(i).getInRateAmount().add(invoiceDtlInfoList.get(j).getInRateAmount()));
				invoiceInfo.get(i).setExRateAmount(
						invoiceInfo.get(i).getExRateAmount().add(invoiceDtlInfoList.get(j).getExRateAmount()));
				invoiceInfo.get(i).setRateAmount(
						invoiceInfo.get(i).getRateAmount().add(invoiceDtlInfoList.get(j).getRateAmount()));
				invoiceInfo.get(i).setInvoiceInAmount(
						invoiceInfo.get(i).getInRateAmount().subtract(invoiceInfo.get(i).getDiscountInRateAmount()));
				invoiceInfo.get(i).setInvoiceExAmount(
						invoiceInfo.get(i).getExRateAmount().subtract(invoiceInfo.get(i).getDiscountExRateAmount()));
				invoiceInfo.get(i).setInvoiceRateAmount(
						invoiceInfo.get(i).getRateAmount().subtract(invoiceInfo.get(i).getDiscountRateAmount()));
			}
			invoiceInfoDtl.setId(invoiceApplyId);
			invoiceInfoDtl.setInvoiceDtlInfoList(invoiceDtlInfoList);
			invoiceInfoDtlList.add(invoiceInfoDtl);
		}
		result.setItems(invoiceInfoDtlList);
		return result;
	}

	public PageResult<InvoiceInfo> printInvoiceResultsByCon(Integer invoiceApplyId) {
		PageResult<InvoiceInfo> result = new PageResult<InvoiceInfo>();
		PageResult<InvoiceInfoDtl> pageResult = queryInvoiceResultsByCon(invoiceApplyId);
		List<InvoiceInfo> invoiceList = new ArrayList<InvoiceInfo>();
		for (int i = 0; i < pageResult.getItems().size(); i++) {
			invoiceList.add(pageResult.getItems().get(i).getInvoiceInfoList());
		}
		result.setItems(invoiceList);
		return result;
	}

	/**
	 * 开票确认
	 * 
	 * @param invoiceInfo
	 * @return
	 * @throws Exception
	 */
	public void ensureInvoiceEnsure(InvoiceInfo invoiceInfo, int count) {
		InvoiceInfo vo = invoiceInfoDao.queryEntityById(invoiceInfo.getId());
		if (vo.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		// 判断该发票号是否重复
		// Integer invoiceCodeNum =
		// invoiceInfoDao.queryByInvoiceCode(invoiceInfo.getInvoiceCode());
		vo.setStatus(BaseConsts.TWO);
		vo.setInvoiceCode(invoiceInfo.getInvoiceCode());
		vo.setInvoiceDate(invoiceInfo.getInvoiceDate());
		Integer flag = invoiceInfoDao.updateById(vo);
		if (flag < 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_SIMULATE_ENSURE_EXCEPTION);
		}
		List<InvoiceInfo> ls = invoiceInfoDao.selectByApplyId(vo.getInvoiceApplyId());
		boolean isok = true;
		for (int i = 0; i < ls.size(); i++) {
			InvoiceInfo invo = ls.get(i);
			if (invo.getStatus().compareTo(BaseConsts.ONE) == 0) {
				isok = false;
			}
		}
		if (isok) {
			InvoiceApplyManager vo2 = new InvoiceApplyManager();
			vo2.setId(vo.getInvoiceApplyId());
			vo2.setStatus(BaseConsts.FIVE);
			invoiceApplyService.updateInvoiceApplyStatus(vo2);
		}
		invoiceBookkeepingService.invoiceBookkeeping(invoiceInfo.getId());

	}

	/**
	 * 开票确认查询
	 */
	public PageResult<InvoiceApplyManagerResDto> queryInvoiceByCon(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		PageResult<InvoiceApplyManagerResDto> result = new PageResult<InvoiceApplyManagerResDto>();
		int offSet = PageUtil.getOffSet(queryInvoiceReqDto.getPage(), queryInvoiceReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryInvoiceReqDto.getPer_page());
		List<InvoiceApplyManagerResDto> invocieList = new LinkedList<InvoiceApplyManagerResDto>();
		queryInvoiceReqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceInfo> list = invoiceInfoDao.queryResult(queryInvoiceReqDto, rowBounds);
		if (queryInvoiceReqDto.getNeedSum() != null && queryInvoiceReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			BigDecimal proSumList = invoiceInfoDao.sumPoTitle(queryInvoiceReqDto);
			result.setTotalAmount(proSumList);
		}
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				InvoiceInfo invoice = list.get(i);
				InvoiceApplyManager applyManager = invoiceApplyDao.queryEntityById(invoice.getInvoiceApplyId());
				InvoiceApplyManagerResDto res = new InvoiceApplyManagerResDto();
				res.setId(invoice.getId());
				res.setApplyNo(invoice.getInvoiceApplyNo());
				res.setInvoiceNo(invoice.getInvoiceNo());
				res.setProjectName(cacheService.getProjectNameById(applyManager.getProjectId()));
				res.setCreateAt(invoice.getCreateAt());
				res.setCreator(invoice.getCreator());
				res.setBusinessUnitName(cacheService.getSubjectNameByIdAndKey(applyManager.getBusinessUnitId(),
						CacheKeyConsts.BUSI_UNIT));
				res.setCustomerName(
						cacheService.getSubjectNameByIdAndKey(applyManager.getCustomerId(), CacheKeyConsts.BCS));
				res.setInvoiceTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_TYPE,
						applyManager.getInvoiceType() + ""));
				res.setBillTypeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, applyManager.getBillType() + ""));
				res.setApplyAmount(DecimalUtil.toAmountString(invoice.getInRateAmount()));
				res.setStatusName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_STATUS, applyManager.getStatus() + ""));
				invocieList.add(res);

			}
		}
		result.setItems(invocieList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryInvoiceReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(queryInvoiceReqDto.getPage());
		result.setPer_page(queryInvoiceReqDto.getPer_page());
		return result;
	}

	public List<InvoiceApplyManagerResDto> queryInvoiceByconAndPage(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		List<InvoiceApplyManagerResDto> invocieList = new LinkedList<InvoiceApplyManagerResDto>();
		queryInvoiceReqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceInfo> list = invoiceInfoDao.queryResult(queryInvoiceReqDto);
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				InvoiceInfo invoice = list.get(i);
				InvoiceApplyManager applyManager = invoiceApplyDao.queryEntityById(invoice.getInvoiceApplyId());
				InvoiceApplyManagerResDto res = new InvoiceApplyManagerResDto();
				res.setId(invoice.getId());
				res.setApplyNo(invoice.getInvoiceApplyNo());
				res.setInvoiceNo(invoice.getInvoiceNo());
				res.setProjectName(cacheService.getProjectNameById(applyManager.getProjectId()));
				res.setCreateAt(invoice.getCreateAt());
				res.setCreator(invoice.getCreator());
				res.setBusinessUnitName(cacheService.getSubjectNameByIdAndKey(applyManager.getBusinessUnitId(),
						CacheKeyConsts.BUSI_UNIT));
				res.setCustomerName(
						cacheService.getSubjectNameByIdAndKey(applyManager.getCustomerId(), CacheKeyConsts.BCS));
				res.setInvoiceTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_TYPE,
						applyManager.getInvoiceType() + ""));
				res.setBillTypeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, applyManager.getBillType() + ""));
				res.setApplyAmount(DecimalUtil.toAmountString(invoice.getInRateAmount()));
				res.setStatusName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_STATUS, applyManager.getStatus() + ""));
				res.setInvoiceDate(invoice.getInvoiceDate());
				invocieList.add(res);

			}
		}
		return invocieList;
	}

	/**
	 * 开票确认导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void importExcel(MultipartFile importFile) {
		List<InvoiceApplyManagerResDto> invoiceList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("invoiceEnsureList", invoiceList);
		ExcelService.resolverExcel(importFile, "/excel/invoice/invoice_ensure.xml", beans);
		// 业务逻辑处理
		invoiceList = (List<InvoiceApplyManagerResDto>) beans.get("invoiceEnsureList");
		if (CollectionUtils.isNotEmpty(invoiceList)) {
			if (invoiceList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			int i = 1;
			for (InvoiceApplyManagerResDto invoice : invoiceList)
				validateInvoiceInfo(invoice, i++);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入发票信息不能为空");
		}
	}

	/**
	 * 校验并更新导入发票
	 * 
	 * @param baseGoodsResDto
	 * @return
	 */
	private void validateInvoiceInfo(InvoiceApplyManagerResDto invoice, int count) {
		if (invoice.getInvoiceCode() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "第" + count + " 行发票号为空;");
		}
		if (invoice.getInvoiceDate() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "第" + count + " 行确认日期为空;");
		}
		InvoiceInfo invoiceInfo = new InvoiceInfo();
		String number = invoice.getApplyNo();
		if (StringUtils.isNotBlank(number)) {
			InvoiceApplyManager invoiceApply = invoiceApplyDao.queryEntityByApplyNo(number);
			if (invoiceApply != null) {
				invoiceInfo.setInvoiceNo(invoice.getInvoiceNo());
				InvoiceInfo invInfo = invoiceInfoDao.queryEntityByInvoiceNo(invoice.getInvoiceNo());
				if (invInfo != null) {
					invoiceInfo.setInvoiceCode(invoice.getInvoiceCode());
					invoiceInfo.setInvoiceDate(invoice.getInvoiceDate());
					invoiceInfo.setId(invInfo.getId());
					ensureInvoiceEnsure(invoiceInfo, count);
				} else {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "第" + count + " 行发票编号信息查询不存在;");
				}
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "第" + count + " 行申请编号查询信息不存在;");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请编号不能为空");
		}
	}

	/**
	 * 查询发票信息
	 * 
	 * @param invoiceApplyId
	 * @return
	 * @throws Exception
	 */
	public List<InvoiceDtlInfo> exportInvoiceByCon(Integer invoiceApplyId) {

		List<InvoiceDtlInfo> result = new LinkedList<InvoiceDtlInfo>();
		List<InvoiceInfo> invoiceInfo = invoiceInfoDao.selectByApplyId(invoiceApplyId);
		for (int i = 0; i < invoiceInfo.size(); i++) {
			InvoiceInfo info = invoiceInfo.get(i);
			List<InvoiceDtlInfo> invoiceDtlInfoList = invoiceDtlInfoDao.selectByApplyId(invoiceInfo.get(i).getId());
			for (int j = 0; j < invoiceDtlInfoList.size(); j++) {
				invoiceDtlInfoList.get(j).setInvoiceCode(info.getInvoiceCode());
				invoiceDtlInfoList.get(j).setInvoiceNo(info.getInvoiceNo());
				invoiceDtlInfoList.get(j).setVersion("1.0");
				invoiceDtlInfoList.get(j).setRule(0);
				invoiceDtlInfoList.get(j)
						.setCustomerName(cacheService.getCustomerById(info.getCustomerId()).getChineseName());
				invoiceDtlInfoList.get(j).setInvoiceDate(info.getInvoiceDate());
				result.add(invoiceDtlInfoList.get(j));
			}
		}
		return result;
	}

	public boolean isOverasyncInvoiceMaxLine(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		int result = invoiceInfoDao.isOverasyncMaxLine(queryInvoiceReqDto);
		if (result >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("发票确认单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceEnsureExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/invoice/makeinvoice/makeinvoice_info_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.SIX);
			asyncExcelService.addAsyncExcel(queryInvoiceReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceEnsureExport(InvoiceApplyManagerReqDto invoiceApplyManagerReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<InvoiceApplyManagerResDto> PoTitles = queryInvoiceByconAndPage(invoiceApplyManagerReqDto);
		model.put("invoiceEnsureDtlList", PoTitles);
		return model;
	}

}
