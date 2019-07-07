package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceApplyDao;
import com.scfs.dao.invoice.InvoiceSaleDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.invoice.dto.req.InvoiceReqList;
import com.scfs.domain.invoice.dto.req.InvoiceSaleManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceSaleManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceSaleManager;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class InvoiceSaleService {

	@Autowired
	private InvoiceSaleDao invoiceDao;
	@Autowired
	private BaseGoodsDao baseGoodsDao;
	@Autowired
	private InvoiceApplyDao invoiceApplyDao;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private InvoiceInfoService invoiceInfoService;

	/**
	 * 查询销售信息(已选择)
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageResult<InvoiceSaleManagerResDto> queryInvoiceResultsByCon(
			InvoiceSaleManagerReqDto invoiceSaleManagerReqDto) {
		PageResult<InvoiceSaleManagerResDto> pageResult = new PageResult<InvoiceSaleManagerResDto>();
		int offSet = PageUtil.getOffSet(invoiceSaleManagerReqDto.getPage(), invoiceSaleManagerReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, invoiceSaleManagerReqDto.getPer_page());
		List<InvoiceSaleManager> result = invoiceDao.selectByInvoiceId(invoiceSaleManagerReqDto.getId(), rowBounds);
		List<InvoiceSaleManagerResDto> vos = new ArrayList<InvoiceSaleManagerResDto>();
		if (result != null) {
			for (InvoiceSaleManager inv : result) {
				InvoiceSaleManagerResDto res = convertToResDto(inv);
				vos.add(res);
				if (pageResult.getFeeTotalAmount() != null)
					pageResult.setFeeTotalAmount(res.getProvideInvoiceAmount().add(pageResult.getFeeTotalAmount()));
				else
					pageResult.setFeeTotalAmount(res.getProvideInvoiceAmount());
				if (pageResult.getRateTotalAmount() != null)
					pageResult.setRateTotalAmount(res.getRateAmount().add(pageResult.getRateTotalAmount()));
				else
					pageResult.setRateTotalAmount(res.getRateAmount());
				if (pageResult.getExRateTotalAmount() != null)
					pageResult.setExRateTotalAmount(res.getExRateAmount().add(pageResult.getExRateTotalAmount()));
				else
					pageResult.setExRateTotalAmount(res.getExRateAmount());
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), invoiceSaleManagerReqDto.getPer_page());
		pageResult.setItems(vos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(invoiceSaleManagerReqDto.getPage());
		pageResult.setPer_page(invoiceSaleManagerReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<InvoiceSaleManagerResDto> queryInvoiceResultsById(InvoiceSaleManagerReqDto ivoiceSaleManagerReqDto) {
		Result<InvoiceSaleManagerResDto> result = new Result<InvoiceSaleManagerResDto>();
		InvoiceSaleManager invoiceRes = invoiceDao.querySaleFee(ivoiceSaleManagerReqDto.getId());
		InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(invoiceRes.getInvoiceApplyId());
		if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		InvoiceSaleManagerResDto res = convertToResDto(invoiceRes);
		result.setItems(res);
		return result;
	}

	/**
	 * 查询销售信息(未选择)
	 * 
	 * @param invoiceSaleReqDto
	 * @return
	 * @throws Exception
	 */
	public PageResult<InvoiceSaleManagerResDto> querySaleNotSelectByCon(InvoiceSaleManagerReqDto invoiceSaleReqDto) {

		PageResult<InvoiceSaleManagerResDto> pageResult = new PageResult<InvoiceSaleManagerResDto>();
		int offSet = PageUtil.getOffSet(invoiceSaleReqDto.getPage(), invoiceSaleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, invoiceSaleReqDto.getPer_page());
		InvoiceApplyManager vo = invoiceApplyDao.queryEntityById(invoiceSaleReqDto.getId());
		invoiceSaleReqDto.setCustomerId(vo.getCustomerId());
		invoiceSaleReqDto.setProjectId(vo.getProjectId());
		invoiceSaleReqDto.setCurrencyType(vo.getCurrencyType());
		List<BillDeliveryDtl> result = billDeliveryDtlDao.queryNotSelectByCon(invoiceSaleReqDto, rowBounds);
		List<InvoiceSaleManagerResDto> invoiceRes = new LinkedList<InvoiceSaleManagerResDto>();
		for (int i = 0; i < result.size(); i++) {
			InvoiceSaleManagerResDto res = new InvoiceSaleManagerResDto();
			res.setId(result.get(i).getId());
			res.setBillNo(result.get(i).getBillNo());
			res.setBillDate(result.get(i).getDeliveryDate());
			res.setInvoiceApplyId(vo.getId());
			res.setGoodsNumber(cacheService.getGoodsById(result.get(i).getGoodsId()).getNumber());
			res.setGoodsName(cacheService.getGoodsById(result.get(i).getGoodsId()).getName());
			res.setBillId(invoiceSaleReqDto.getBillId());
			res.setBillDtlId(result.get(i).getId());
			res.setGoodsId(result.get(i).getGoodsId());
			if (result.get(i).getProvideInvoiceNum() != null) {
				BigDecimal sumNum = result.get(i).getRequiredSendNum().subtract(result.get(i).getProvideInvoiceNum());
				res.setProvideMaxNum(sumNum);
				res.setProvideInvoiceNum(sumNum);
			} else {
				res.setProvideMaxNum(result.get(i).getRequiredSendNum());
				res.setProvideInvoiceNum(result.get(i).getRequiredSendNum());
			}
			if (result.get(i).getProvideInvoiceAmount() != null) {
				BigDecimal sumAmount = result.get(i).getRequiredSendNum()
						.multiply(result.get(i).getRequiredSendPrice());
				res.setProvideMaxAmount(sumAmount.subtract(result.get(i).getProvideInvoiceAmount()).setScale(2,
						BigDecimal.ROUND_HALF_UP));
				res.setProvideInvoiceAmount(sumAmount.subtract(result.get(i).getProvideInvoiceAmount()).setScale(2,
						BigDecimal.ROUND_HALF_UP));
			} else {
				res.setProvideMaxAmount(result.get(i).getRequiredSendNum()
						.multiply(result.get(i).getRequiredSendPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
				res.setProvideInvoiceAmount(result.get(i).getRequiredSendNum()
						.multiply(result.get(i).getRequiredSendPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			res.setProvideInvoicePrice(result.get(i).getRequiredSendPrice());
			invoiceRes.add(res);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), invoiceSaleReqDto.getPer_page());
		pageResult.setItems(invoiceRes);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(invoiceSaleReqDto.getPage());
		pageResult.setPer_page(invoiceSaleReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 更新销售信息
	 * 
	 * @param invoiceSaleReqDto
	 * @return
	 * @throws Exception
	 */
	public BaseResult updateSaleByCon(InvoiceSaleManagerResDto invoiceSaleManager) {
		BaseResult baseResult = new BaseResult();
		InvoiceSaleManager invoiceSale = invoiceDao.queryAndLockById(invoiceSaleManager.getId());
		InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(invoiceSale.getInvoiceApplyId());
		if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		BillDeliveryDtl vo = billDeliveryDtlDao.queryAndLockEntityById(invoiceSale.getBillDtlId());
		// 判断明细开票数量和最大数量比较
		BigDecimal compNum = vo.getRequiredSendNum().subtract(vo.getProvideInvoiceNum());
		if (invoiceSaleManager.getProvideInvoiceNum().compareTo(invoiceSale.getProvideInvoiceNum().add(compNum)) > 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_NUM__MAX_EXCEPTION);
		} else {
			// 判断明细开票金额和最大金额比较
			BigDecimal comAmount = vo.getRequiredSendPrice().multiply(vo.getRequiredSendNum())
					.setScale(2, BigDecimal.ROUND_HALF_UP).subtract(vo.getProvideInvoiceAmount());
			if (invoiceSaleManager.getProvideInvoiceAmount()
					.compareTo(comAmount.add(invoiceSale.getProvideInvoiceAmount())) > 0) {
				throw new BaseException(ExcMsgEnum.INVOICE_AMOUNT_MAX_EXCEPTION);
			}
			InvoiceSaleManager invoice = new InvoiceSaleManager();
			BillDeliveryDtl billDelivery = new BillDeliveryDtl();
			billDelivery.setId(vo.getId());
			invoice.setId(invoiceSaleManager.getId());
			invoice.setProvideInvoiceAmount(invoiceSaleManager.getProvideInvoiceAmount());
			invoice.setProvideInvoiceNum(invoiceSaleManager.getProvideInvoiceNum());
			BigDecimal currAmount = invoiceSaleManager.getProvideInvoiceAmount()
					.subtract(invoiceSale.getProvideInvoiceAmount());
			billDelivery.setProvideInvoiceAmount(vo.getProvideInvoiceAmount().add(currAmount));
			BigDecimal currNum = invoiceSaleManager.getProvideInvoiceNum().subtract(invoiceSale.getProvideInvoiceNum());
			billDelivery.setProvideInvoiceNum(vo.getProvideInvoiceNum().add(currNum));
			billDeliveryDtlDao.updateById(billDelivery);
			invoiceDao.updateById(invoice);
		}
		return baseResult;
	}

	/**
	 * 批量删除销售信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteInvoiceByIds(List<Integer> ids) throws Exception {
		if (!CollectionUtils.isEmpty(ids)) {
			Integer invoiceApplyId = 0;
			for (Integer id : ids) {
				InvoiceSaleManager vo = invoiceDao.querySaleFee(id);
				InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(vo.getInvoiceApplyId());
				if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
				}
				invoiceApplyId = vo.getInvoiceApplyId();
				invoiceDao.deleteByCon(id);
				BillDeliveryDtl dtl1 = billDeliveryDtlDao.queryAndLockEntityById(vo.getBillDtlId());
				dtl1.setProvideInvoiceNum(dtl1.getProvideInvoiceNum().subtract(vo.getProvideInvoiceNum()));
				dtl1.setProvideInvoiceAmount(dtl1.getProvideInvoiceAmount().subtract(vo.getProvideInvoiceAmount()));
				billDeliveryDtlDao.updateById(dtl1);
			}
			invoiceInfoService.deleteSimulateInvoiceInfo(invoiceApplyId);
		}
	}

	/**
	 * 删除单条销售信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteInvoiceById(Integer id) {
		InvoiceSaleManager vo = invoiceDao.querySaleFee(id);
		InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(vo.getInvoiceApplyId());
		if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		invoiceDao.deleteByCon(id);
		BillDeliveryDtl dtl1 = billDeliveryDtlDao.queryAndLockEntityById(vo.getBillDtlId());
		dtl1.setProvideInvoiceNum(dtl1.getProvideInvoiceNum().subtract(vo.getProvideInvoiceNum()));
		dtl1.setProvideInvoiceAmount(dtl1.getProvideInvoiceAmount().subtract(vo.getProvideInvoiceAmount()));
		billDeliveryDtlDao.updateById(dtl1);
	}

	/**
	 * 批量插入销售信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BaseResult addBactchInvoice(InvoiceReqList invoiceSaleManager) {
		BaseResult baseResult = new BaseResult();

		List<InvoiceSaleManager> lists = invoiceSaleManager.getInvoiceSaleManagerReqDto();
		if (!CollectionUtils.isEmpty(lists)) {
			InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(invoiceSaleManager.getId());
			if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
			}
			for (InvoiceSaleManager list : lists) {
				BillDeliveryDtl vo1 = billDeliveryDtlDao.queryAndLockEntityById(list.getBillDtlId());
				if (vo1.getProvideInvoiceNum() == null) {
					vo1.setProvideInvoiceNum(BigDecimal.ZERO);
				}
				if (vo1.getRequiredSendPrice() == null) {
					vo1.setRequiredSendPrice(BigDecimal.ZERO);
				}
				if (vo1.getProvideInvoiceAmount() == null) {
					vo1.setProvideInvoiceAmount(BigDecimal.ZERO);
				}
				if (list.getProvideInvoiceNum()
						.compareTo(vo1.getRequiredSendNum().subtract(vo1.getProvideInvoiceNum())) > 0) {
					throw new BaseException(ExcMsgEnum.INVOICE_NUM__MAX_EXCEPTION);
				} else {
					BigDecimal com = vo1.getRequiredSendPrice().multiply(vo1.getRequiredSendNum())
							.setScale(2, BigDecimal.ROUND_HALF_UP).subtract(vo1.getProvideInvoiceAmount());
					if (list.getProvideInvoiceAmount().compareTo(com) > 0) {
						throw new BaseException(ExcMsgEnum.INVOICE_AMOUNT_MAX_EXCEPTION);
					} else {
						BaseGoods baseGoods = baseGoodsDao.queryBaseGoodsById(list.getGoodsId());
						list.setCreator(ServiceSupport.getUser().getChineseName());
						list.setCreatorId(ServiceSupport.getUser().getId());
						list.setCreateAt(new Date());
						list.setInRateAmount(list.getProvideInvoiceAmount());
						list.setRateAmount(
								DecimalUtil
										.multiply(
												DecimalUtil
														.divide(list.getProvideInvoiceAmount(),
																DecimalUtil.ONE.add(baseGoods.getTaxRate()))
														.setScale(2, BigDecimal.ROUND_HALF_UP),
												baseGoods.getTaxRate()));
						list.setExRateAmount(DecimalUtil
								.divide(list.getProvideInvoiceAmount(), DecimalUtil.ONE.add(baseGoods.getTaxRate()))
								.setScale(2, BigDecimal.ROUND_HALF_UP));
						invoiceDao.insert(list);
						// 更新销售单明细信息
						BillDeliveryDtl billDelivery = new BillDeliveryDtl();
						billDelivery.setId(list.getBillDtlId());
						billDelivery.setProvideInvoiceAmount(
								vo1.getProvideInvoiceAmount().add(list.getProvideInvoiceAmount()));
						billDelivery.setProvideInvoiceNum(vo1.getProvideInvoiceNum().add(list.getProvideInvoiceNum()));
						billDeliveryDtlDao.updateById(billDelivery);
					}
				}
			}
		}
		return baseResult;
	}

	/**
	 * 插入销售信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void addInvoice(InvoiceSaleManager invoiceSaleManager) throws Exception {
		if (invoiceSaleManager != null) {
			invoiceDao.insert(invoiceSaleManager);
			// 更新销售单明细信息
			BillDeliveryDtl billDelivery = new BillDeliveryDtl();
			billDelivery.setId(invoiceSaleManager.getBillDtlId());
			billDelivery.setProvideInvoiceNum(invoiceSaleManager.getProvideInvoiceNum());
			billDelivery.setProvideInvoiceAmount(invoiceSaleManager.getProvideInvoiceAmount());
			// billDeliveryDtlDao.updateById(billDelivery);
		}
	}

	/**
	 * 转换响应结果
	 * 
	 * @param inv
	 * @return
	 */
	private InvoiceSaleManagerResDto convertToResDto(InvoiceSaleManager inv) {
		// TODO Auto-generated method stub
		BaseGoods baseGoods = baseGoodsDao.queryBaseGoodsById(inv.getGoodsId());
		BillDeliveryDtl vo = billDeliveryDtlDao.queryAndLockEntityById(inv.getBillDtlId());
		InvoiceSaleManagerResDto res = new InvoiceSaleManagerResDto();
		res.setId(inv.getId());
		res.setBillNo(inv.getBillNo());
		BaseGoods good = baseGoodsDao.queryBaseGoodsById(inv.getGoodsId());
		res.setGoodsId(inv.getGoodsId());
		res.setGoodsName(good.getName());
		res.setBillDate(inv.getBillDate());
		res.setBillDateValue(inv.getBillDate());
		res.setGoodsNumber(cacheService.getGoodsById(inv.getGoodsId()).getNumber());
		res.setProvideInvoiceNum(inv.getProvideInvoiceNum());
		res.setProvideInvoicePrice(inv.getProvideInvoicePrice());
		res.setProvideInvoiceNumValue(inv.getProvideInvoiceNum() + good.getUnit());
		res.setProvideInvoiceAmount(inv.getProvideInvoiceAmount());
		res.setRateAmount(DecimalUtil
				.multiply(DecimalUtil.divide(inv.getProvideInvoiceAmount(), DecimalUtil.ONE.add(baseGoods.getTaxRate()))
						.setScale(2, BigDecimal.ROUND_HALF_UP), baseGoods.getTaxRate()));
		res.setExRateAmount(
				DecimalUtil.divide(inv.getProvideInvoiceAmount(), DecimalUtil.ONE.add(baseGoods.getTaxRate()))
						.setScale(2, BigDecimal.ROUND_HALF_UP));
		res.setDiscountInRateAmount(inv.getDiscountInRateAmount());
		res.setDiscountRateAmount(
				DecimalUtil
						.multiply(DecimalUtil.divide(inv.getDiscountInRateAmount(),
								DecimalUtil.ONE.add(baseGoods.getTaxRate())), baseGoods.getTaxRate())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
		res.setInvoiceNum(vo.getProvideInvoiceNum());
		res.setRecNum(vo.getRequiredSendNum());
		res.setRecAmount(DecimalUtil.formatScale2(vo.getRequiredSendPrice().multiply(vo.getRequiredSendNum())));
		res.setInvoiceAmount(vo.getProvideInvoiceAmount());
		res.setUseNum(vo.getRequiredSendNum().subtract(vo.getProvideInvoiceNum().subtract(inv.getProvideInvoiceNum())));
		res.setUseAmount(
				res.getRecAmount().subtract(vo.getProvideInvoiceAmount().subtract(inv.getProvideInvoiceAmount())));
		res.setOpertaList(getOperList());
		return res;
	}

	/**
	 * 根据状态获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.EDIT);
		return opertaList;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvoiceSaleManagerResDto.Operate.operMap);
		return oprResult;
	}

}
