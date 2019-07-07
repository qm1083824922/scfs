package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.pay.PayPoRelationDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.pay.entity.PayPoRelationModel;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PayPoRelationService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月09日			Administrator
 *
 * </pre>
 */
@Service
public class PayPoRelationService {
	@Autowired
	private PayPoRelationDao payPoRelationDao;

	@Autowired
	private PurchaseOrderService purchaseOrderService;// 订单信息

	@Autowired
	private PayOrderDao payOrderDao;

	@Autowired
	private PayService payService;

	@Autowired
	private CacheService cacheService;

	/**
	 * 获取完成订单列表信息
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public PageResult<PoLineModel> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PayOrder pay = payOrderDao.queryEntityById(poTitleReqDto.getId());
		if (pay.getState() != BaseConsts.ZERO && pay.getState() != BaseConsts.SIX) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		} else if ((pay.getState() == BaseConsts.SIX && pay.getNoneOrderFlag() == BaseConsts.ZERO)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		}
		poTitleReqDto.setProjectId(pay.getProjectId());
		poTitleReqDto.setSupplierId(pay.getPayee());
		poTitleReqDto.setIsPayAll(BaseConsts.ONE);
		poTitleReqDto.setState(BaseConsts.FIVE);
		poTitleReqDto.setCurrencyId(pay.getCurrnecyType());
		return purchaseOrderService.queryPoLinesByPoReqDto(poTitleReqDto);
	}

	/**
	 * 订单信息添加
	 * 
	 * @param record
	 */
	public void createPayPoRelation(PayPoRelationReqDto record) {
		createPayPoRelation(record, null);
	}

	/**
	 * 订单信息添加
	 * 
	 * @param record
	 * @return
	 */
	public void createPayPoRelation(PayPoRelationReqDto record, BaseProject baseProject) {
		int payId = record.getPayId();
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		int state = payOrder.getState();
		if (state != BaseConsts.ZERO && state != BaseConsts.THREE && state != BaseConsts.SIX) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		} else if (state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		}
		BigDecimal poBlance = payOrder.getPoBlance();
		List<PayPoRelation> results = record.getRelList();
		BigDecimal poSumAmount = BigDecimal.ZERO;
		BigDecimal discountSumAmount = BigDecimal.ZERO;
		BigDecimal inDiscountSumAmount = BigDecimal.ZERO;
		boolean isPayAmount = false;
		for (PayPoRelation payRel : results) {
			// 判断输入金额是否小于订单余额
			int poLineId = payRel.getPoLineId();
			PurchaseOrderLine purchaseOrderLine = purchaseOrderService.queryPoLineEntityById(poLineId);
			int poId = purchaseOrderLine.getPoId();
			BigDecimal payableAmount = DecimalUtil.formatScale2(DecimalUtil.subtract(
					(purchaseOrderLine.getAmount().subtract(purchaseOrderLine.getDiscountAmount())),
					purchaseOrderLine.getPaidAmount()));
			if (DecimalUtil.gt(payRel.getPayAmount(), payableAmount)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"付款金额" + payRel.getPayAmount().toString() + "不能大于待付款金额" + payableAmount.toString());
			}
			if (DecimalUtil.gt(purchaseOrderLine.getPaidAmount(), BigDecimal.ZERO)) {// 已付款金额是否大于0
				isPayAmount = true;
			}
			BigDecimal payAmount = payRel.getPayAmount();
			BigDecimal prePayAmount = null == payRel.getPrePayAmount() ? BigDecimal.ZERO : payRel.getPrePayAmount();
			// 添加关系
			PayPoRelation payPoRel = new PayPoRelation();
			payPoRel.setPayId(payId);
			payPoRel.setPoId(poId);
			payPoRel.setPoLineId(payRel.getPoLineId());
			payPoRel.setPayAmount(payAmount);
			payPoRel.setDiscountAmount(payRel.getDiscountAmount());
			payPoRel.setDuctionMoney(payRel.getDuctionMoney());// 抵扣金额
			payPoRel.setInDiscountAmount(
					DecimalUtil.formatScale2(payAmount.add(payRel.getDiscountAmount()).add(prePayAmount)));
			payPoRel.setCreateAt(new Date());
			payPoRel.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
			payPoRel.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
			payPoRelationDao.insert(payPoRel);

			// 修改订单明细已付款金额
			PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
			poLineUpd.setId(payRel.getPoLineId());
			poLineUpd.setPoId(poId);
			poLineUpd.setPaidAmount(DecimalUtil.add(purchaseOrderLine.getPaidAmount(), payAmount));
			purchaseOrderService.updatePoLinesById(poLineUpd);

			// 修改订单已付款金额
			PoTitleRespDto poTitleRespDto = purchaseOrderService.queryPurchaseOrderTitleById(poId).getItems();
			BigDecimal oldAmount = poTitleRespDto.getPayAmount();// 已付款金额
			PurchaseOrderTitle upTitle = new PurchaseOrderTitle();
			upTitle.setId(poTitleRespDto.getId());
			upTitle.setPayAmount(DecimalUtil.add(oldAmount, payAmount));
			purchaseOrderService.updatePurchaseOrderTitle(upTitle);
			// 添加总额
			poSumAmount = DecimalUtil.add(poSumAmount, payAmount);
			discountSumAmount = DecimalUtil.add(discountSumAmount, payRel.getDiscountAmount());
			inDiscountSumAmount = DecimalUtil.add(inDiscountSumAmount, payPoRel.getInDiscountAmount());
		}
		// 修改付款订单总金额
		PayOrder upPayOrder = new PayOrder();
		upPayOrder.setId(payId);
		upPayOrder.setPoBlance(DecimalUtil.add(poBlance, poSumAmount));
		upPayOrder.setDiscountAmount(DecimalUtil.formatScale2(payOrder.getDiscountAmount().add(discountSumAmount)));
		upPayOrder.setInDiscountAmount(inDiscountSumAmount);
		if (!(state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ONE)) {	//已完成且无单据付款，不需要重算付款金额
			upPayOrder.setPayAmount(DecimalUtil.add(payOrder.getPayAmount(), poSumAmount));
		}
		System.out.println("付款金额[" + upPayOrder.getPayAmount() + "],货款金额[" + upPayOrder.getPoBlance() + "]");
		if (null == baseProject) {
			baseProject = cacheService.getProjectById(payOrder.getProjectId());
		}
		if (baseProject.getBizType().equals(BaseConsts.SEVEN)) {// 判断是否是质押项目
			if (isPayAmount) {// 是否是尾款
				upPayOrder.setPayWayType(BaseConsts.TWO);
			}
		}
		payService.updatePayOrderById(upPayOrder);
	}

	/**
	 * 修改订单信息
	 * 
	 * @param record
	 * @return
	 */
	public void updatePayPoRelation(PayPoRelationReqDto record) {
		List<PayPoRelation> results = record.getRelList();
		for (PayPoRelation payRel : results) {
			// 判断输入金额是否小于订余额
			int id = payRel.getId();
			BigDecimal payAmount = payRel.getPayAmount();
			BigDecimal discountAmount = payRel.getDiscountAmount();
			// 计算修改差额并修改
			PayPoRelation oldEntity = payPoRelationDao.queryPayPoById(id);
			BigDecimal diffPayAmount = DecimalUtil
					.formatScale2(DecimalUtil.subtract(payAmount, oldEntity.getPayAmount()));// 本次输入与上次金额差额
			BigDecimal diffDiscount = DecimalUtil
					.formatScale2(DecimalUtil.subtract(discountAmount, oldEntity.getDiscountAmount()));// 本次输入与上次金额差额

			int poLineId = payRel.getPoLineId();
			PurchaseOrderLine purchaseOrderLine = purchaseOrderService.queryPoLineEntityById(poLineId);
			BigDecimal payableAmount = DecimalUtil
					.formatScale2(purchaseOrderLine.getAmount().subtract(purchaseOrderLine.getDiscountAmount()));

			if (DecimalUtil.gt(diffPayAmount, payableAmount.subtract(purchaseOrderLine.getPaidAmount()))) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额" + payRel.getPayAmount().toString()
						+ "不能大于待付款金额" + payableAmount.subtract(purchaseOrderLine.getPaidAmount()).toString());
			}
			// 修改本次金额
			PayPoRelation payPoRel = new PayPoRelation();
			payPoRel.setId(id);
			payPoRel.setPayAmount(DecimalUtil.formatScale2(payAmount));
			payPoRel.setDiscountAmount(DecimalUtil.formatScale2(discountAmount));
			payPoRel.setInDiscountAmount(DecimalUtil.formatScale2(payAmount.add(discountAmount)));
			payPoRelationDao.updateById(payPoRel);
			// 修改订单明细已付款金额
			PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
			poLineUpd.setId(poLineId);
			poLineUpd.setPaidAmount(DecimalUtil.add(poLineUpd.getPaidAmount(), diffPayAmount));
			purchaseOrderService.updatePoLinesById(purchaseOrderLine);
			// 修改订单已付款金额
			int poId = oldEntity.getPoId();
			PoTitleRespDto poTitleRespDto = purchaseOrderService.queryPurchaseOrderTitleById(poId).getItems();
			BigDecimal oldAmount = poTitleRespDto.getPayAmount();
			oldAmount = DecimalUtil.add(oldAmount, diffPayAmount);// 已付款金额
			PurchaseOrderTitle upTitle = new PurchaseOrderTitle();
			upTitle.setId(poTitleRespDto.getId());
			upTitle.setPayAmount(oldAmount);
			purchaseOrderService.updatePurchaseOrderTitle(upTitle);
			// 修改付款订单总金额
			int payId = oldEntity.getPayId();
			PayOrder payOrder = new PayOrder();
			payOrder.setId(payId);
			PayOrderResDto payOrderResDto = payService.editPayOrderById(payOrder).getItems();
			int state = payOrderResDto.getStateInt();
			BigDecimal poBlance = payOrderResDto.getPoBlance();
			PayOrder upPayOrder = new PayOrder();
			upPayOrder.setId(payId);
			upPayOrder.setPoBlance(DecimalUtil.add(poBlance, diffPayAmount));
			if (!(state == BaseConsts.SIX && payOrderResDto.getNoneOrderFlag() == BaseConsts.ONE)) {	//已完成且无单据付款，不需要重算付款金额
				upPayOrder.setPayAmount(DecimalUtil.add(payOrderResDto.getPayAmount(), diffPayAmount));
			}
			upPayOrder.setDiscountAmount(DecimalUtil.formatScale2(payOrderResDto.getDiscountAmount().add(diffDiscount)));
			upPayOrder.setInDiscountAmount(payOrderResDto.getInDiscountAmount().add(diffDiscount));
			payService.updatePayOrderById(upPayOrder);
		}
	}

	/**
	 * 编辑付款订单信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	public Result<PayPoRelationResDto> editPayPoRelationById(PayPoRelationReqDto payPoRelationReqDto) {
		Result<PayPoRelationResDto> result = new Result<PayPoRelationResDto>();
		List<PayPoRelationModel> listPoRel = payPoRelationDao.queryResultsByCon(payPoRelationReqDto);
		PayPoRelationResDto payPo = convertToPayPoRelationResDto(listPoRel.get(BaseConsts.ZERO));
		result.setItems(payPo);
		return result;
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public BaseResult deletePayPoRelation(PayPoRelationReqDto payPoRelation) {
		for (Integer id : payPoRelation.getIds()) {
			PayPoRelation oldEntity = payPoRelationDao.queryPayPoById(id);
			// 修改订单明细已付金额
			PurchaseOrderLine purchaseOrderLine = purchaseOrderService.queryPoLineEntityById(oldEntity.getPoLineId());
			PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
			poLineUpd.setId(purchaseOrderLine.getId());
			poLineUpd.setPoId(purchaseOrderLine.getPoId());
			poLineUpd.setPaidAmount(DecimalUtil.subtract(purchaseOrderLine.getPaidAmount(), oldEntity.getPayAmount()));
			purchaseOrderService.updatePoLinesById(poLineUpd);
			// 减少订单信息相关金额
			PoTitleRespDto poTitleRespDto = purchaseOrderService.queryPurchaseOrderTitleById(oldEntity.getPoId())
					.getItems();
			PurchaseOrderTitle upTitle = new PurchaseOrderTitle();
			upTitle.setId(poTitleRespDto.getId());
			upTitle.setPayAmount(DecimalUtil.subtract(
					poTitleRespDto.getPayAmount() == null ? BigDecimal.ZERO : poTitleRespDto.getPayAmount(),
					oldEntity.getPayAmount() == null ? BigDecimal.ZERO : oldEntity.getPayAmount()));
			purchaseOrderService.updatePurchaseOrderTitle(upTitle);
			// 修改付款订单总金额
			int payId = oldEntity.getPayId();
			PayOrder payOrder = new PayOrder();
			payOrder.setId(payId);
			PayOrderResDto payOrderResDto = payService.editPayOrderById(payOrder).getItems();
			int state = payOrderResDto.getStateInt();
			PayOrder upPayOrder = new PayOrder();
			upPayOrder.setId(payId);
			upPayOrder.setPoBlance(DecimalUtil.subtract(payOrderResDto.getPoBlance(), oldEntity.getPayAmount()));
			if (!(state == BaseConsts.SIX && payOrderResDto.getNoneOrderFlag() == BaseConsts.ONE)) {	//已完成且无单据付款，不需要重算付款金额
				upPayOrder.setPayAmount(DecimalUtil.subtract(payOrderResDto.getPayAmount(), oldEntity.getPayAmount()));
			}
			upPayOrder.setDiscountAmount(DecimalUtil
					.formatScale2(payOrderResDto.getDiscountAmount().subtract(oldEntity.getDiscountAmount())));
			upPayOrder.setInDiscountAmount(DecimalUtil
					.formatScale2(payOrderResDto.getInDiscountAmount().subtract(oldEntity.getInDiscountAmount())));
			payService.updatePayOrderById(upPayOrder);
			// 删除数据
			PayPoRelation payPoRel = new PayPoRelation();
			payPoRel.setId(id);
			payPoRel.setIsDelete(BaseConsts.ONE);
			payPoRelationDao.updateById(payPoRel);
		}
		return new BaseResult();
	}

	/**
	 * 获取所有信息
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	public List<PayPoRelationModel> queryPayPoRelationByCon(PayPoRelationReqDto payPoRelationSearchReqDto) {
		return payPoRelationDao.queryResultsByCon(payPoRelationSearchReqDto);
	}

	/**
	 * 获取所有付款订单数据
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	public List<PayPoRelationResDto> queryPayPoRelationAuditByCon(PayPoRelationReqDto payPoRelationSearchReqDto) {
		List<PayPoRelationResDto> payPoRelationResDto = convertToPayPoRelationResDtos(
				payPoRelationDao.queryResultsByCon(payPoRelationSearchReqDto));
		return payPoRelationResDto;
	}

	/**
	 * 获取分页列表信息
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	public PageResult<PayPoRelationResDto> queryPayPoRelationResultsByCon(
			PayPoRelationReqDto payPoRelationSearchReqDto) {
		PageResult<PayPoRelationResDto> pageResult = new PageResult<PayPoRelationResDto>();
		int offSet = PageUtil.getOffSet(payPoRelationSearchReqDto.getPage(), payPoRelationSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payPoRelationSearchReqDto.getPer_page());
		List<PayPoRelationResDto> payPoRelationResDto = convertToPayPoRelationResDtos(
				payPoRelationDao.queryResultsByCon(payPoRelationSearchReqDto, rowBounds));
		pageResult.setItems(payPoRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payPoRelationSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payPoRelationSearchReqDto.getPage());
		pageResult.setPer_page(payPoRelationSearchReqDto.getPer_page());

		List<PayPoRelationResDto> payPoRelation = convertToPayPoRelationResDtos(
				payPoRelationDao.queryResultsByCon(payPoRelationSearchReqDto));
		int currencyId = 0;
		BigDecimal payAmountSum = BigDecimal.ZERO;
		for (PayPoRelationResDto payPoRela : payPoRelation) {
			if (payPoRela != null) {
				payAmountSum = DecimalUtil.add(payAmountSum, payPoRela.getPayAmount());
				currencyId = payPoRela.getCurrencyId();
			}
		}
		String totalStr = "合计  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum)) + " &nbsp"
				+ BaseConsts.STRING_BLANK_SPACE + (null == BaseConsts.CURRENCY_UNIT_MAP.get(currencyId) ? ""
						: BaseConsts.CURRENCY_UNIT_MAP.get(currencyId));
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<PayPoRelationResDto> convertToPayPoRelationResDtos(List<PayPoRelationModel> result) {
		List<PayPoRelationResDto> payPoRelationResDto = new ArrayList<PayPoRelationResDto>();
		if (ListUtil.isEmpty(result)) {
			return payPoRelationResDto;
		}
		for (PayPoRelationModel model : result) {
			PayPoRelationResDto poRelationResDto = convertToPayPoRelationResDto(model);
			payPoRelationResDto.add(poRelationResDto);
		}
		return payPoRelationResDto;
	}

	public PayPoRelationResDto convertToPayPoRelationResDto(PayPoRelationModel model) {
		PayPoRelationResDto payRel = new PayPoRelationResDto();
		BeanUtils.copyProperties(model, payRel);
		BigDecimal goodsAmount = BigDecimal.ZERO; // 商品金额
		BigDecimal paidAmount = BigDecimal.ZERO;// 已付款金额
		if (model.getGoodsAmount() != null) {
			goodsAmount = model.getGoodsAmount();
		}
		if (model.getPaidAmount() != null) {
			paidAmount = model.getPaidAmount();
		}
		payRel.setPaymentAmount(DecimalUtil.subtract(DecimalUtil.add(model.getPayAmount(), goodsAmount), paidAmount));
		payRel.setCurrencyName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrencyId() + ""));
		if (model.getGoodsId() != null) {
			BaseGoods goods = cacheService.getGoodsById(model.getGoodsId());
			payRel.setGoodsName(goods.getName());
			payRel.setGoodsNo(goods.getNumber());
			payRel.setPledge(goods.getPledgeProportion());
		}
		payRel.setArrivalAmount(payRel.getArrivalAmount() == null ? DecimalUtil.ZERO
				: DecimalUtil.formatScale2(payRel.getArrivalAmount()));
		payRel.setRequiredSendPrice(model.getRequiredSendPrice() == null ? DecimalUtil.ZERO
				: DecimalUtil.formatScale2(payRel.getRequiredSendPrice()));
		payRel.setProfitMargin(BigDecimal.ZERO);
		if (DecimalUtil.gt(payRel.getRequiredSendPrice(), DecimalUtil.ZERO)) {
			BigDecimal sum = DecimalUtil.subtract(payRel.getRequiredSendPrice(), model.getGoodsPrice());
			BigDecimal margin = DecimalUtil.divide(sum, payRel.getRequiredSendPrice());
			margin = DecimalUtil.toPercent(margin);
			payRel.setProfitMargin(margin);
		}
		if (DecimalUtil.gt(model.getInDiscountAmount(), DecimalUtil.ZERO)) {
			payRel.setDiscountRate(DecimalUtil.divide(model.getDiscountAmount(), model.getInDiscountAmount()));
			payRel.setDiscountRateStr(DecimalUtil.toPercentString(payRel.getDiscountRate()));
		} else {
			payRel.setDiscountRateStr("0.00%");
		}
		payRel.setWriteOffFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.WRITE_OFF_FLAG, model.getWriteOffFlag() + ""));
		return payRel;
	}

	public BigDecimal queryTotalOrderNum(Integer payOrderId) {
		return payPoRelationDao.queryTotalOrderNum(payOrderId);
	}

}
