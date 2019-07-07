package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayAdvanceRelationDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.pay.dto.req.PayAdvanceRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayAdvanceRelationResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.PayAdvanceRelation;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PayAdvanceRelationService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */
@Service
public class PayAdvanceRelationService {
	@Autowired
	private PayAdvanceRelationDao payAdvanceRelationDao;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private PayService payService;

	@Autowired
	private BankReceiptService bankReceiptService;

	/**
	 * 获取预收定金列表信息
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public PageResult<BankReceiptResDto> queryAdvanceResultsByCon(BankReceiptSearchReqDto bankReceiptSearchReqDto) {
		PayOrder payOrder = new PayOrder();
		payOrder.setId(bankReceiptSearchReqDto.getId());
		PayOrderResDto payOrderResDto = payService.editPayOrderById(payOrder).getItems();
		bankReceiptSearchReqDto.setProjectId(payOrderResDto.getProjectId());
		bankReceiptSearchReqDto.setState(BaseConsts.THREE);
		bankReceiptSearchReqDto.setIsPayOver(BaseConsts.TWO); // 可付金额不为0
		bankReceiptSearchReqDto.setCurrencyType(payOrderResDto.getCurrnecyType());
		bankReceiptSearchReqDto.setPayType(payOrderResDto.getPayType());
		PageResult<BankReceiptResDto> result = bankReceiptService.queryRootByConPayAdvanceRel(bankReceiptSearchReqDto);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param record
	 * @return
	 */
	public void createPayAdvanceRelation(PayAdvanceRelationReqDto record) {
		int payId = record.getPayId();
		PayOrder payOrder = payService.queryEntityById(payId);
		List<PayAdvanceRelation> results = record.getRelList();
		BigDecimal totalAdvanceAmount = BigDecimal.ZERO;

		Date date = new Date();
		for (PayAdvanceRelation item : results) {
			BigDecimal payAmount = item.getPayAmount();
			int receiptId = item.getAdvanceId();

			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(item.getAdvanceId())
					.getItems();
			if (DecimalUtil.eq(payAmount, DecimalUtil.ZERO)) {
				continue;
			}
			if (DecimalUtil.lt(bankReceiptResDto.getPayableAmount(), payAmount)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "余额不足！");
			}
			// 添加关系
			PayAdvanceRelation payAdvanRel = new PayAdvanceRelation();
			payAdvanRel.setPayId(payId);
			payAdvanRel.setAdvanceId(receiptId);
			payAdvanRel.setPayAmount(payAmount);
			payAdvanRel.setCreateAt(date);
			payAdvanRel.setCreator(ServiceSupport.getUser().getChineseName());
			payAdvanRel.setCreatorId(ServiceSupport.getUser().getId());
			payAdvanceRelationDao.insert(payAdvanRel);

			// 修改水单已付款金额
			BankReceipt updBankReceipt = new BankReceipt();
			updBankReceipt.setId(receiptId);
			updBankReceipt.setPaidAmount(DecimalUtil.add(bankReceiptResDto.getPaidAmount(), payAmount));
			updBankReceipt.setActualPaidAmount(
					DecimalUtil.multiply(updBankReceipt.getPaidAmount(), bankReceiptResDto.getActualCurrencyRate()));
			bankReceiptService.updateBankReceiptById(updBankReceipt);

			totalAdvanceAmount = DecimalUtil.add(totalAdvanceAmount, payAmount);
		}
		// 修改付款单预收金额
		PayOrder payOrderUpd = new PayOrder();
		payOrderUpd.setId(payId);
		payOrderUpd.setAdvanceAmount(DecimalUtil.add(payOrder.getAdvanceAmount(), totalAdvanceAmount));
		payService.updatePayOrderById(payOrderUpd);
	}

	/**
	 * 修改订单信息
	 * 
	 * @param record
	 * @return
	 */
	public BaseResult updatePayAdvanceRelation(PayAdvanceRelationReqDto record) {
		BaseResult baseResult = new BaseResult();
		List<PayAdvanceRelation> results = record.getRelList();
		BigDecimal totalDiffAmount = BigDecimal.ZERO;
		Integer payId = record.getPayId();
		for (PayAdvanceRelation payRelation : results) {
			int id = payRelation.getId();
			PayAdvanceRelation oldEntity = payAdvanceRelationDao.queryPayAdvanceById(id);
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(oldEntity.getAdvanceId())
					.getItems();
			BigDecimal payAmount = payRelation.getPayAmount(); // 本次付款金额
			BigDecimal oldPayAmount = oldEntity.getPayAmount(); // 上次付款金额
			BigDecimal diffPayAmount = DecimalUtil.subtract(payAmount, oldPayAmount);// 差额不能大于
			if (DecimalUtil.lt(bankReceiptResDto.getPayableAmount(), diffPayAmount)) {
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
			// 修改
			PayAdvanceRelation upPayAdvanRelation = new PayAdvanceRelation();
			upPayAdvanRelation.setId(id);
			upPayAdvanRelation.setPayAmount(payAmount);
			payAdvanceRelationDao.updateById(upPayAdvanRelation);

			// 修改水单已付款金额
			BankReceipt updBankReceipt = new BankReceipt();
			updBankReceipt.setId(oldEntity.getAdvanceId());
			updBankReceipt.setPaidAmount(DecimalUtil.add(bankReceiptResDto.getPaidAmount(), diffPayAmount));
			updBankReceipt.setActualPaidAmount(
					DecimalUtil.multiply(updBankReceipt.getPaidAmount(), bankReceiptResDto.getActualCurrencyRate()));
			bankReceiptService.updateBankReceiptById(updBankReceipt);
			totalDiffAmount = DecimalUtil.add(totalDiffAmount, diffPayAmount);
		}
		// 修改付款单预收金额
		PayOrder oldPayOrder = payService.queryEntityById(payId);
		PayOrder payOrderUpd = new PayOrder();
		payOrderUpd.setId(payId);
		payOrderUpd.setAdvanceAmount(DecimalUtil.add(oldPayOrder.getAdvanceAmount(), totalDiffAmount));
		payService.updatePayOrderById(payOrderUpd);
		return baseResult;
	}

	/**
	 * 编辑付款预收信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	public Result<PayAdvanceRelationResDto> editPayAdvanceRelationById(PayAdvanceRelation payAdvanceRelation) {
		Result<PayAdvanceRelationResDto> result = new Result<PayAdvanceRelationResDto>();
		PayAdvanceRelationResDto payAdvan = convertToPayAdvanceRelationResDto(
				payAdvanceRelationDao.queryPayAdvanceById(payAdvanceRelation.getId()));
		result.setItems(payAdvan);
		return result;
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public BaseResult deletePayAdvanRelation(PayAdvanceRelationReqDto payAdvanRelation) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		Integer payId = payAdvanRelation.getPayId();
		for (Integer id : payAdvanRelation.getIds()) {
			PayAdvanceRelation payAdvanceRelation = payAdvanceRelationDao.queryPayAdvanceById(id);
			// 修改水单付款金额
			int receiptId = payAdvanceRelation.getAdvanceId();
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(receiptId).getItems();
			BankReceipt updBankReceipt = new BankReceipt();
			updBankReceipt.setId(receiptId);
			updBankReceipt.setPaidAmount(
					DecimalUtil.subtract(bankReceiptResDto.getPaidAmount(), payAdvanceRelation.getPayAmount()));
			updBankReceipt.setActualPaidAmount(
					DecimalUtil.multiply(updBankReceipt.getPaidAmount(), bankReceiptResDto.getActualCurrencyRate()));

			bankReceiptService.updateBankReceiptById(updBankReceipt);
			// 删除数据
			PayAdvanceRelation payAdvanceRel = new PayAdvanceRelation();
			payAdvanceRel.setId(id);
			payAdvanceRel.setIsDelete(BaseConsts.ONE);
			payAdvanceRelationDao.updateById(payAdvanceRel);

			totalAmount = DecimalUtil.add(totalAmount, payAdvanceRelation.getPayAmount());
		}
		// 修改付款单预收金额
		PayOrder oldEntity = payService.queryEntityById(payId);
		PayOrder payOrderUpd = new PayOrder();
		payOrderUpd.setId(payId);
		payOrderUpd.setAdvanceAmount(DecimalUtil.subtract(oldEntity.getAdvanceAmount(), totalAmount));
		payService.updatePayOrderById(payOrderUpd);
		return new BaseResult();
	}

	/**
	 * 获取付款单下总额
	 * 
	 * @param payId
	 * @return
	 */
	public BigDecimal sumPayAmount(int payId) {
		BigDecimal sum = payAdvanceRelationDao.sumPayAmount(payId);
		if (sum == null) {
			sum = BigDecimal.ZERO;
		}
		return sum;
	}

	/**
	 * 获取所有付款单下信息
	 * 
	 * @param payAdvanceRelation
	 * @return
	 */
	public List<PayAdvanceRelationResDto> queryPayAdvanByPayId(PayAdvanceRelationReqDto payAdvanceRelation) {
		return convertToPayAdvanRelationResDtos(payAdvanceRelationDao.queryResultsByCon(payAdvanceRelation));
	}

	/**
	 * 获取分页列表信息
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	public PageResult<PayAdvanceRelationResDto> queryPayAdvanRelationResultsByCon(
			PayAdvanceRelationReqDto payAdvanceRelation) {
		PageResult<PayAdvanceRelationResDto> pageResult = new PageResult<PayAdvanceRelationResDto>();
		int offSet = PageUtil.getOffSet(payAdvanceRelation.getPage(), payAdvanceRelation.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payAdvanceRelation.getPer_page());
		List<PayAdvanceRelationResDto> payAdvanRelationResDto = convertToPayAdvanRelationResDtos(
				payAdvanceRelationDao.queryResultsByCon(payAdvanceRelation, rowBounds));
		pageResult.setItems(payAdvanRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payAdvanceRelation.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payAdvanceRelation.getPage());
		pageResult.setPer_page(payAdvanceRelation.getPer_page());

		List<PayAdvanceRelationResDto> payAdvanRela = convertToPayAdvanRelationResDtos(
				payAdvanceRelationDao.queryResultsByCon(payAdvanceRelation));
		int currencyType = 0;
		BigDecimal payAmountSum = BigDecimal.ZERO;
		for (PayAdvanceRelationResDto payAdvance : payAdvanRela) {
			if (payAdvance != null) {
				payAmountSum = DecimalUtil.add(payAmountSum, payAdvance.getPayAmount());
				currencyType = payAdvance.getCurrencyType();
			}
		}
		String totalStr = "合计  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum)) + " &nbsp"
				+ BaseConsts.STRING_BLANK_SPACE + (null == BaseConsts.CURRENCY_UNIT_MAP.get(currencyType) ? ""
						: BaseConsts.CURRENCY_UNIT_MAP.get(currencyType));
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<PayAdvanceRelationResDto> queryPayAdvanRelationByPayId(PayAdvanceRelationReqDto payAdvanceRelation) {
		return convertToPayAdvanRelationResDtos(payAdvanceRelationDao.queryResultsByCon(payAdvanceRelation));
	}

	public List<PayAdvanceRelationResDto> convertToPayAdvanRelationResDtos(List<PayAdvanceRelation> result) {
		List<PayAdvanceRelationResDto> payAdvanceRelationResDto = new ArrayList<PayAdvanceRelationResDto>();
		if (ListUtil.isEmpty(result)) {
			return payAdvanceRelationResDto;
		}
		for (PayAdvanceRelation model : result) {
			PayAdvanceRelationResDto advanRelationResDto = convertToPayAdvanceRelationResDto(model);
			payAdvanceRelationResDto.add(advanRelationResDto);
		}
		return payAdvanceRelationResDto;
	}

	public PayAdvanceRelationResDto convertToPayAdvanceRelationResDto(PayAdvanceRelation model) {
		PayAdvanceRelationResDto advanceRel = new PayAdvanceRelationResDto();
		advanceRel.setId(model.getId());
		advanceRel.setPayId(model.getPayId());
		advanceRel.setPayNo(model.getPayNo());
		advanceRel.setAdvanceId(model.getAdvanceId());
		advanceRel.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
		advanceRel.setBusiUnit(cacheService.getSubjectNcByIdAndKey(model.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		advanceRel.setCustName(cacheService.getSubjectNcByIdAndKey(model.getCustId(), CacheKeyConsts.CUSTOMER));
		advanceRel.setCurrencyType(model.getCurrencyType());
		advanceRel.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrencyType() + ""));
		advanceRel.setPayAmount(model.getPayAmount());
		advanceRel.setPaidAmount(model.getPaidAmount());
		advanceRel.setBlance(DecimalUtil.subtract(model.getPreRecSum(), model.getPaidAmount()));
		advanceRel.setReceiptDate(model.getReceiptDate());
		return advanceRel;
	}

	/**
	 * 新增付款单和水单关系
	 * 
	 * @param payAdvanceRelation
	 */
	public void addPayAdvanceRelation(PayAdvanceRelation payAdvanceRelation) {
		payAdvanceRelationDao.insert(payAdvanceRelation);
	}
}
