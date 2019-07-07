package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectPoDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.invoice.dto.req.InvoiceCollectPoReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectResDto;
import com.scfs.domain.invoice.entity.InvoiceCollect;
import com.scfs.domain.invoice.entity.InvoiceCollectPo;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
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
 * 	收票采购单相关业务
 *  File: InvoiceCollectPoService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月29日			Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectPoService {
	@Autowired
	private InvoiceCollectPoDao invoiceCollectPoDao;

	@Autowired
	private InvoiceCollectService invoiceCollectService;

	@Autowired
	private PurchaseOrderService purchaseOrderService;// 订单信息

	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;

	@Autowired
	CacheService cacheService;

	/**
	 * 获取采购单信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	public PageResult<PoLineModel> queryPoLinesByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(poTitleReqDto.getId());
		poTitleReqDto.setProjectId(conllect.getProjectId());
		poTitleReqDto.setSupplierId(conllect.getSupplierId());
		poTitleReqDto.setIsVatAll(BaseConsts.ONE);
		poTitleReqDto.setState(BaseConsts.FIVE);
		poTitleReqDto.setCurrencyId(BaseConsts.ONE);
		result = purchaseOrderService.queryPoLinesByPoReqDto(poTitleReqDto);
		List<PoLineModel> list = new ArrayList<PoLineModel>();
		if (result.getItems() != null && result.getItems().size() > BaseConsts.ONE) {
			for (PoLineModel model : result.getItems()) {
				if (DecimalUtil.gt(model.getBlanceNum(), BigDecimal.ZERO)
						|| DecimalUtil.gt(model.getBlanceAmount(), BigDecimal.ZERO)) {
					list.add(model);
				}
			}
			result.setItems(list);
		}
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	public BaseResult createInvoiceCollectPo(InvoiceCollectPoReqDto poReqDto) {
		BaseResult baseResult = new BaseResult();
		Integer collectId = poReqDto.getCollectId();
		InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(collectId);
		BigDecimal sum = conllect.getInvoiceAmount();

		List<InvoiceCollectPo> colRel = poReqDto.getColRel();
		for (InvoiceCollectPo invoiceCollectPo : colRel) {
			// 判断输入数量是否小于商品可用数量，含税金额是否小于商品余额
			Integer poId = invoiceCollectPo.getPoId();
			BigDecimal poNum = invoiceCollectPo.getPoNum();
			BigDecimal inRateAmount = null == invoiceCollectPo.getInRateAmount() ? BigDecimal.ZERO
					: invoiceCollectPo.getInRateAmount();
			BigDecimal discountInRateAmount = null == invoiceCollectPo.getDiscountInRateAmount() ? BigDecimal.ZERO
					: invoiceCollectPo.getDiscountInRateAmount();
			BigDecimal actualInvoiceAmount = DecimalUtil.subtract(inRateAmount, discountInRateAmount);

			PurchaseOrderLine line = purchaseOrderLineDao.queryPurchaseOrderLineById(poId);
			BigDecimal goodsNum = null == line.getGoodsNum() ? BigDecimal.ZERO : line.getGoodsNum();
			BigDecimal invoiceNum = null == line.getInvoiceNum() ? BigDecimal.ZERO : line.getInvoiceNum();
			BigDecimal blanceNum = DecimalUtil.subtract(goodsNum, invoiceNum);
			if (DecimalUtil.gt(poNum, blanceNum)) {
				baseResult.setMsg("数量不足！");
				return baseResult;
			}
			BigDecimal amount = null == line.getAmount() ? BigDecimal.ZERO : line.getAmount();
			BigDecimal invoiceAmount = null == line.getInvoiceAmount() ? BigDecimal.ZERO : line.getInvoiceAmount();
			BigDecimal discountAmount = null == line.getDiscountAmount() ? BigDecimal.ZERO : line.getDiscountAmount();
			BigDecimal blanceAmount = DecimalUtil.subtract(DecimalUtil.subtract(amount, discountAmount), invoiceAmount);
			if (DecimalUtil.gt(actualInvoiceAmount, blanceAmount)) {
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}
		Date date = new Date();
		BigDecimal taxRate = BigDecimal.ZERO;
		for (InvoiceCollectPo invoiceCollectPo : colRel) {
			Integer poId = invoiceCollectPo.getPoId();
			BigDecimal poNum = invoiceCollectPo.getPoNum();
			BigDecimal inRateAmount = null == invoiceCollectPo.getInRateAmount() ? BigDecimal.ZERO
					: invoiceCollectPo.getInRateAmount();// 含税金额
			BigDecimal discountInRateAmount = null == invoiceCollectPo.getDiscountInRateAmount() ? BigDecimal.ZERO
					: invoiceCollectPo.getDiscountInRateAmount();// 含税折扣金额
			BigDecimal actualInvoiceAmount = DecimalUtil.subtract(invoiceCollectPo.getInRateAmount(),
					invoiceCollectPo.getDiscountInRateAmount());

			// 修改订单明细
			PurchaseOrderLine orderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(poId);
			PurchaseOrderLine upOrderLine = new PurchaseOrderLine();
			upOrderLine.setId(orderLine.getId());
			upOrderLine.setInvoiceNum(DecimalUtil.add(orderLine.getInvoiceNum(), poNum));
			upOrderLine.setInvoiceAmount(DecimalUtil.add(orderLine.getInvoiceAmount(), actualInvoiceAmount));
			purchaseOrderLineDao.updatePurchaseOrderLineById(upOrderLine);

			// 修改订单头
			PurchaseOrderTitle orderTitle = purchaseOrderTitleDao.queryEntityById(orderLine.getPoId());
			PurchaseOrderTitle upOrderTitle = new PurchaseOrderTitle();
			upOrderTitle.setId(orderTitle.getId());
			upOrderTitle.setInvoiceTotalNum(DecimalUtil.add(orderTitle.getInvoiceTotalNum(), poNum));
			upOrderTitle
					.setInvoiceTotalAmount(DecimalUtil.add(orderTitle.getInvoiceTotalAmount(), actualInvoiceAmount));
			purchaseOrderTitleDao.updatePurchaseOrderTitleById(upOrderTitle);

			BigDecimal taxRateValue = invoiceCollectPo.getTaxRate(); // 获取税率
			// 添加数据
			BigDecimal exAlgorithm = DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE));
			BigDecimal rateAlgorithm = DecimalUtil.divide(taxRateValue,
					DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE)));
			BigDecimal exRateAmount = DecimalUtil.divide(inRateAmount, exAlgorithm);// 未税金额=费用含税金额/(1+收票税率)
			BigDecimal rateAmount = DecimalUtil.multiply(inRateAmount, rateAlgorithm);// 税额
																						// =费用含税金额/(1+收票税率)*收票税率
			BigDecimal discountExRateAmount = DecimalUtil.divide(discountInRateAmount, exAlgorithm);// 未税折扣金额=含税折扣金额/(1+税率)
			BigDecimal discountRateAmount = DecimalUtil.multiply(discountInRateAmount, rateAlgorithm);// 折扣税额=含税折扣金额/(1+税率)*税率
			invoiceCollectPo.setCollectId(collectId);
			invoiceCollectPo.setRateAmount(rateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo.setExRateAmount(exRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo
					.setDiscountExRateAmount(discountExRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo
					.setDiscountRateAmount(discountRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo
					.setActualInvoiceAmount(actualInvoiceAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo.setTaxRate(taxRateValue);
			invoiceCollectPo.setCreateAt(date);
			invoiceCollectPo.setCreator(ServiceSupport.getUser().getChineseName());
			invoiceCollectPo.setCreatorId(ServiceSupport.getUser().getId());
			invoiceCollectPo.setIsDelete(BaseConsts.ZERO);
			invoiceCollectPoDao.insert(invoiceCollectPo);

			sum = DecimalUtil.add(sum, actualInvoiceAmount); // 去掉含税折扣金额
			taxRate = invoiceCollectPo.getTaxRate();
		}
		int taxRateCount = invoiceCollectPoDao.countTaxRateByInvoiceCollectId(collectId);
		if (taxRateCount > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请选择相同税率的采购订单商品！");
		}
		InvoiceCollect upEntity = new InvoiceCollect();
		upEntity.setId(collectId);
		upEntity.setInvoiceAmount(sum.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
		upEntity.setInvoiceTaxRate(taxRate);
		invoiceCollectService.updateInvoiceCollectById(upEntity);// 修改收票金额
		return baseResult;
	}

	/**
	 * 修改收票采购单信息
	 * 
	 * @param poReqDto
	 * @return
	 */
	public BaseResult updateInvoiceCollectPoById(InvoiceCollectPoReqDto poReqDto) {
		BaseResult baseResult = new BaseResult();
		List<InvoiceCollectPo> colRel = poReqDto.getColRel();
		for (InvoiceCollectPo invoiceCollectPo : colRel) {
			BigDecimal poNum = null == invoiceCollectPo.getPoNum() ? BigDecimal.ZERO : invoiceCollectPo.getPoNum();
			BigDecimal inRateAmount = null == invoiceCollectPo.getInRateAmount() ? BigDecimal.ZERO
					: invoiceCollectPo.getInRateAmount();
			BigDecimal discountInRateAmount = null == invoiceCollectPo.getDiscountInRateAmount() ? BigDecimal.ZERO
					: invoiceCollectPo.getDiscountInRateAmount();
			BigDecimal actualInvoiceAmount = DecimalUtil.subtract(inRateAmount, discountInRateAmount);

			InvoiceCollectPo oldEntity = invoiceCollectPoDao.queryEntityById(invoiceCollectPo.getId());
			Integer poId = oldEntity.getPoId();
			PurchaseOrderLine line = purchaseOrderLineDao.queryPurchaseOrderLineById(poId);
			BigDecimal invoiceNum = null == line.getInvoiceNum() ? BigDecimal.ZERO : line.getInvoiceNum();
			BigDecimal goodsNum = null == line.getGoodsNum() ? BigDecimal.ZERO : line.getGoodsNum();
			BigDecimal invoiceAmount = null == line.getInvoiceAmount() ? BigDecimal.ZERO : line.getInvoiceAmount();
			BigDecimal amount = null == line.getAmount() ? BigDecimal.ZERO : line.getAmount();
			BigDecimal discountAmount = null == line.getDiscountAmount() ? BigDecimal.ZERO : line.getDiscountAmount();
			BigDecimal blanceAmount = DecimalUtil.subtract(amount, discountAmount);

			BigDecimal oldPoNum = null == oldEntity.getPoNum() ? BigDecimal.ZERO : oldEntity.getPoNum();
			BigDecimal diffNum = DecimalUtil.subtract(poNum, oldPoNum);// 与原输入差额
			BigDecimal sumNum = DecimalUtil.add(diffNum, invoiceNum); // 已用
			if (DecimalUtil.gt(sumNum, goodsNum)) {
				baseResult.setMsg("数量不足！");
				return baseResult;
			}

			BigDecimal oldActualInvoiceAmount = null == oldEntity.getActualInvoiceAmount() ? BigDecimal.ZERO
					: oldEntity.getActualInvoiceAmount();
			BigDecimal diffAmount = DecimalUtil.subtract(DecimalUtil.subtract(actualInvoiceAmount),
					DecimalUtil.subtract(oldActualInvoiceAmount));// 与原输入差额
			BigDecimal sumAmount = DecimalUtil.add(diffAmount, invoiceAmount);// 总额
			if (DecimalUtil.gt(sumAmount, blanceAmount)) {
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}
		for (InvoiceCollectPo invoiceCollectPo : colRel) {
			BigDecimal poNum = invoiceCollectPo.getPoNum();
			BigDecimal inRateAmount = invoiceCollectPo.getInRateAmount();// 含税金额
			BigDecimal discountInRateAmount = invoiceCollectPo.getDiscountInRateAmount();// 含税折扣金额
			BigDecimal actualInvoiceAmount = DecimalUtil.subtract(inRateAmount, discountInRateAmount);

			InvoiceCollectPo oldEntity = invoiceCollectPoDao.queryEntityById(invoiceCollectPo.getId());
			Integer poId = oldEntity.getPoId();
			Integer collectId = oldEntity.getCollectId();
			InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(collectId);
			BigDecimal invoiceAmount = conllect.getInvoiceAmount();

			BigDecimal taxRateValue = oldEntity.getTaxRate(); // 获取税率
			// 修改数据
			BigDecimal exAlgorithm = DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE));
			BigDecimal rateAlgorithm = DecimalUtil.divide(taxRateValue,
					DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE)));
			BigDecimal exRateAmount = DecimalUtil.divide(inRateAmount, exAlgorithm);// 未税金额=费用含税金额/(1+收票税率)
			BigDecimal rateAmount = DecimalUtil.multiply(inRateAmount, rateAlgorithm);// 税额
																						// =费用含税金额/(1+收票税率)*收票税率
			BigDecimal discountExRateAmount = DecimalUtil.divide(discountInRateAmount, exAlgorithm);// 未税折扣金额=含税折扣金额/(1+税率)
			BigDecimal discountRateAmount = DecimalUtil.multiply(discountInRateAmount, rateAlgorithm);// 折扣税额=含税折扣金额/(1+税率)*税率
			invoiceCollectPo.setRateAmount(rateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo.setExRateAmount(exRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo
					.setDiscountExRateAmount(discountExRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo
					.setDiscountRateAmount(discountRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPo
					.setActualInvoiceAmount(actualInvoiceAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectPoDao.updateById(invoiceCollectPo);

			BigDecimal diffNum = DecimalUtil.subtract(poNum, oldEntity.getPoNum());// 与原输入差额
			BigDecimal diffAmount = DecimalUtil.subtract(actualInvoiceAmount, oldEntity.getActualInvoiceAmount());// 含税金额与原输入差额
			// 修改订单明细
			PurchaseOrderLine orderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(poId);
			PurchaseOrderLine upOrderLine = new PurchaseOrderLine();
			upOrderLine.setId(orderLine.getId());
			upOrderLine.setInvoiceNum(DecimalUtil.add(orderLine.getInvoiceNum(), diffNum).setScale(BaseConsts.EIGHT,
					BigDecimal.ROUND_HALF_UP));
			upOrderLine.setInvoiceAmount(DecimalUtil.add(orderLine.getInvoiceAmount(), diffAmount)
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			purchaseOrderLineDao.updatePurchaseOrderLineById(upOrderLine);

			// 修改订单头
			PurchaseOrderTitle orderTitle = purchaseOrderTitleDao.queryEntityById(orderLine.getPoId());
			PurchaseOrderTitle upOrderTitle = new PurchaseOrderTitle();
			upOrderTitle.setId(orderTitle.getId());
			upOrderTitle.setInvoiceTotalNum(DecimalUtil.add(orderTitle.getInvoiceTotalNum(), diffNum)
					.setScale(BaseConsts.EIGHT, BigDecimal.ROUND_HALF_UP));
			upOrderTitle.setInvoiceTotalAmount(DecimalUtil.add(orderTitle.getInvoiceTotalAmount(), diffAmount)
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			purchaseOrderTitleDao.updatePurchaseOrderTitleById(upOrderTitle);

			// 修改收票金额
			InvoiceCollect upEntity = new InvoiceCollect();
			upEntity.setId(collectId);
			upEntity.setInvoiceAmount(
					DecimalUtil.add(invoiceAmount, diffAmount).setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceCollectService.updateInvoiceCollectById(upEntity);// 修改收票金额
			return baseResult;
		}
		return baseResult;
	}

	/**
	 * 删除数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public BaseResult deleteInvoiceCollectPoByIds(InvoiceCollectPoReqDto poReqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : poReqDto.getIds()) {
			InvoiceCollectPo oldEntity = invoiceCollectPoDao.queryEntityById(id);
			// 修改收票金额
			InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(oldEntity.getCollectId());
			InvoiceCollect upEntity = new InvoiceCollect();
			upEntity.setId(oldEntity.getCollectId());
			upEntity.setInvoiceAmount(
					DecimalUtil.subtract(conllect.getInvoiceAmount(), oldEntity.getActualInvoiceAmount())
							.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP)); // 除掉含税金额
			invoiceCollectService.updateInvoiceCollectById(upEntity);// 修改收票金额

			// 修改订单明细
			PurchaseOrderLine orderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(oldEntity.getPoId());
			PurchaseOrderLine upOrderLine = new PurchaseOrderLine();
			upOrderLine.setId(orderLine.getId());
			upOrderLine.setInvoiceNum(DecimalUtil.subtract(orderLine.getInvoiceNum(), oldEntity.getPoNum())
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			upOrderLine.setInvoiceAmount(
					DecimalUtil.subtract(orderLine.getInvoiceAmount(), oldEntity.getActualInvoiceAmount())
							.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			purchaseOrderLineDao.updatePurchaseOrderLineById(upOrderLine);

			// 修改订单头
			PurchaseOrderTitle orderTitle = purchaseOrderTitleDao.queryEntityById(orderLine.getPoId());
			PurchaseOrderTitle upOrderTitle = new PurchaseOrderTitle();
			upOrderTitle.setId(orderTitle.getId());
			upOrderTitle.setInvoiceTotalNum(DecimalUtil.subtract(orderTitle.getInvoiceTotalNum(), oldEntity.getPoNum())
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			upOrderTitle.setInvoiceTotalAmount(
					DecimalUtil.subtract(orderTitle.getInvoiceTotalAmount(), oldEntity.getActualInvoiceAmount())
							.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			purchaseOrderTitleDao.updatePurchaseOrderTitleById(upOrderTitle);

			InvoiceCollectPo upCollectPo = new InvoiceCollectPo();
			upCollectPo.setId(id);
			upCollectPo.setIsDelete(BaseConsts.ONE);
			invoiceCollectPoDao.updateById(upCollectPo);

		}
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param collectResDto
	 * @return
	 */
	public Result<InvoiceCollectPoResDto> editInvoiceCollectPoById(InvoiceCollectPo invoiceCollectPo) {
		Result<InvoiceCollectPoResDto> result = new Result<InvoiceCollectPoResDto>();
		InvoiceCollectPoResDto resDto = convertInvoiceCollectPoResDto(
				invoiceCollectPoDao.queryInvoiceCollectPoById(invoiceCollectPo.getId()));
		result.setItems(resDto);
		return result;
	}

	/**
	 * 通过收票id获取采购信息
	 * 
	 * @param collectId
	 * @return
	 */
	public List<InvoiceCollectPoResDto> queryInvoiceCollectPoByCollectId(int collectId) {
		InvoiceCollectPoReqDto poReqDto = new InvoiceCollectPoReqDto();
		poReqDto.setCollectId(collectId);
		return convertToInvoiceCollectPoResDtos(invoiceCollectPoDao.queryResultsByCon(poReqDto));
	}

	/**
	 * 获取费用信息
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public PageResult<InvoiceCollectPoResDto> queryInvoiceCollectPoResultsByCon(InvoiceCollectPoReqDto poReqDto) {
		PageResult<InvoiceCollectPoResDto> pageResult = new PageResult<InvoiceCollectPoResDto>();
		int offSet = PageUtil.getOffSet(poReqDto.getPage(), poReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poReqDto.getPer_page());
		List<InvoiceCollectPoResDto> payFeeRelationResDto = convertToInvoiceCollectPoResDtos(
				invoiceCollectPoDao.queryResultsByCon(poReqDto, rowBounds));
		pageResult.setItems(payFeeRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(poReqDto.getPage());
		pageResult.setPer_page(poReqDto.getPer_page());

		// 合计
		List<InvoiceCollectPoResDto> sumCollectPo = convertToInvoiceCollectPoResDtos(
				invoiceCollectPoDao.queryResultsByCon(poReqDto));
		BigDecimal poNumSum = BigDecimal.ZERO;
		BigDecimal inRateAmountSum = BigDecimal.ZERO;
		BigDecimal exRateAmountSum = BigDecimal.ZERO;
		BigDecimal rateAmountSum = BigDecimal.ZERO;
		BigDecimal discountInRateAmountSum = BigDecimal.ZERO;
		BigDecimal discountExRateAmountSum = BigDecimal.ZERO;
		BigDecimal discountRateAmountSum = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(sumCollectPo)) {
			for (InvoiceCollectPoResDto collect : sumCollectPo) {
				poNumSum = DecimalUtil.add(poNumSum, collect.getPoNum());
				inRateAmountSum = DecimalUtil.add(inRateAmountSum, collect.getInRateAmount());
				exRateAmountSum = DecimalUtil.add(exRateAmountSum, collect.getExRateAmount());
				rateAmountSum = DecimalUtil.add(rateAmountSum, collect.getRateAmount());
				discountInRateAmountSum = DecimalUtil.add(discountInRateAmountSum, collect.getDiscountInRateAmount());
				discountExRateAmountSum = DecimalUtil.add(discountExRateAmountSum, collect.getDiscountExRateAmount());
				discountRateAmountSum = DecimalUtil.add(discountRateAmountSum, collect.getDiscountRateAmount());
			}
		}
		String totalStr = "数量  : " + DecimalUtil.formatScale2(poNumSum) + "  &nbsp;&nbsp;&nbsp;  含税金额: "
				+ DecimalUtil.formatScale2(inRateAmountSum) + "  &nbsp;&nbsp;&nbsp;  未税金额: "
				+ DecimalUtil.formatScale2(exRateAmountSum) + "  &nbsp;&nbsp;&nbsp;  税额: "
				+ DecimalUtil.formatScale2(rateAmountSum) + "  &nbsp;&nbsp;&nbsp;  含税折扣金额: "
				+ DecimalUtil.formatScale2(discountInRateAmountSum) + "  &nbsp;&nbsp;&nbsp;  未税折扣金额: "
				+ DecimalUtil.formatScale2(discountExRateAmountSum) + "  &nbsp;&nbsp;&nbsp;  折扣税额: "
				+ DecimalUtil.formatScale2(discountRateAmountSum);
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<InvoiceCollectPoResDto> convertToInvoiceCollectPoResDtos(List<InvoiceCollectPo> result) {
		List<InvoiceCollectPoResDto> invoiceCollectPoResDto = new ArrayList<InvoiceCollectPoResDto>();
		if (ListUtil.isEmpty(result)) {
			return invoiceCollectPoResDto;
		}
		for (InvoiceCollectPo model : result) {
			InvoiceCollectPoResDto collectPoResDto = convertInvoiceCollectPoResDto(model);
			invoiceCollectPoResDto.add(collectPoResDto);
		}
		return invoiceCollectPoResDto;
	}

	public InvoiceCollectPoResDto convertInvoiceCollectPoResDto(InvoiceCollectPo model) {
		InvoiceCollectPoResDto result = new InvoiceCollectPoResDto();
		result.setId(model.getId());
		result.setCollectId(model.getCollectId());
		result.setOrderNo(model.getOrderNo());
		result.setOrderTime(model.getOrderTime());
		model.setGoodsId(model.getGoodsId());
		if (model.getGoodsId() != null) {
			BaseGoods baseGoods = cacheService.getGoodsById(model.getGoodsId());
			result.setGoodsNo(baseGoods.getNumber());
			result.setGoodsDescribe(baseGoods.getName());
			result.setUnit(baseGoods.getUnit());
		}
		result.setPoNum(model.getPoNum());
		result.setGoodsPrice(model.getGoodsPrice());
		result.setInRateAmount(model.getInRateAmount());
		result.setExRateAmount(model.getExRateAmount());
		result.setRateAmount(model.getRateAmount());
		result.setDiscountInRateAmount(model.getDiscountInRateAmount());
		result.setDiscountExRateAmount(model.getDiscountExRateAmount());
		result.setDiscountRateAmount(model.getDiscountRateAmount());
		result.setActualInvoiceAmount(model.getActualInvoiceAmount());
		result.setTaxRate(model.getTaxRate());
		result.setBlanceNum(model.getBlanceNum());
		result.setBlanceAmount(model.getBlanceAmount());
		return result;
	}
}
