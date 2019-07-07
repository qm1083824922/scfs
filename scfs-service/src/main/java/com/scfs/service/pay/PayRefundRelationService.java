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
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.pay.PayRefundRelationDao;
import com.scfs.dao.pay.RefundInformationDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.pay.dto.req.PayRefundRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayRefundRelationResDto;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayRefundRelation;
import com.scfs.domain.pay.entity.RefundInformation;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PayRefundRelationService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月19日			Administrator
 *
 * </pre>
 */
@Service
public class PayRefundRelationService {

	@Autowired
	BankReceiptDao bankReceiptDao;

	@Autowired
	private BankReceiptService bankReceiptService;

	@Autowired
	PayRefundRelationDao refundRelationDao;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private PayOrderDao payOrderDao;

	@Autowired
	private PayService payService;

	@Autowired
	private RefundInformationDao refundInformationDao;

	/**
	 * 查询当前满足退款信息的水单数据 预收订金 预收货款 待核销 核销金额和预收金额为0
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	public PageResult<BankReceiptResDto> queryRefundResultByCon(BankReceiptSearchReqDto bankReceiptSearchReqDto) {
		// 封装其查询水单信息的条件
		bankReceiptSearchReqDto.setState(BaseConsts.TWO);// 状态为2 代表待核销状态
		PageResult<BankReceiptResDto> result = bankReceiptService.queryRootRefundResult(bankReceiptSearchReqDto);// 查询待分页的水单数据
		return result;
	}

	/**
	 * 查询当前付款单下面的退款信息数据
	 * 
	 * @param resDto
	 * @return
	 */
	public PageResult<PayRefundRelationResDto> queryRefundResultByCond(PayRefundRelationReqDto reqDto) {
		PageResult<PayRefundRelationResDto> pageResult = new PageResult<PayRefundRelationResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<PayRefundRelationResDto> payAdvanRelationResDto = convertToPayRefundRelationResDtos(
				refundRelationDao.queryPayRefundLaResultsByCon(reqDto, rowBounds));
		pageResult.setItems(payAdvanRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());

		List<PayRefundRelationResDto> relationResDtos = convertToPayRefundRelationResDtos(
				refundRelationDao.queryPayRefundLaResultsByCon(reqDto));
		int currencyId = 0;
		BigDecimal payAmountSum = BigDecimal.ZERO;
		for (PayRefundRelationResDto resDto : relationResDtos) {
			if (resDto != null) {
				payAmountSum = DecimalUtil.add(payAmountSum, resDto.getRefundAmount());
				currencyId = resDto.getCurrencyType();
			}
		}
		String totalStr = "合计  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum)) + " &nbsp"
				+ BaseConsts.STRING_BLANK_SPACE + (null == BaseConsts.CURRENCY_UNIT_MAP.get(currencyId) ? ""
						: BaseConsts.CURRENCY_UNIT_MAP.get(currencyId));
		pageResult.setTotalStr(totalStr);
		return pageResult;

	}

	// 封装当期付款单和退款信息的查询条件
	public List<PayRefundRelationResDto> convertToPayRefundRelationResDtos(List<PayRefundRelation> result) {
		List<PayRefundRelationResDto> resDtos = new ArrayList<PayRefundRelationResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtos;
		}
		for (PayRefundRelation model : result) {
			PayRefundRelationResDto advanRelationResDto = convertPayRefundRelationResDto(model);
			resDtos.add(advanRelationResDto);
		}
		return resDtos;
	}

	public PayRefundRelationResDto convertPayRefundRelationResDto(PayRefundRelation refundRelation) {
		PayRefundRelationResDto relationResDto = new PayRefundRelationResDto();
		BankReceipt bankReceipt = bankReceiptService.queryEntityById(refundRelation.getReceiptId());
		relationResDto.setId(refundRelation.getId());
		relationResDto.setPayId(refundRelation.getPayId());// 付款的ID
		relationResDto.setRefundId(refundRelation.getRefundImId());// 退款信息表的ID
		relationResDto.setRefundAmount(refundRelation.getPayAmount());// 退款金额
		relationResDto.setCurrencyType(refundRelation.getCurrencyType());// 币种、
		relationResDto.setPaidAmount(DecimalUtil.subtract(bankReceipt.getReceiptAmount(), bankReceipt.getPaidAmount()));// 已付金额
		relationResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				refundRelation.getCurrencyType() + ""));
		relationResDto.setProjectName(cacheService.getProjectNameById(refundRelation.getProjectId()));
		relationResDto.setReceiptDate(refundRelation.getReceiptAt());
		relationResDto.setBusiUnit(
				cacheService.getSubjectNcByIdAndKey(refundRelation.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		relationResDto
				.setCustName(cacheService.getSubjectNcByIdAndKey(refundRelation.getCustId(), CacheKeyConsts.CUSTOMER));
		return relationResDto;
	}

	/**
	 * 新增水单的退款数据到退款信息
	 * 
	 * @param reqDto
	 */
	public void createPayfundByCon(PayRefundRelationReqDto reqDto) {
		// 根据付款单的iD 获取付款单
		if (reqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单数据有误");
		}
		List<PayRefundRelation> results = reqDto.getRelList();
		for (PayRefundRelation payRefundRelation : results) {
			int payId = reqDto.getPayId();
			// 获取付款信息
			PayOrder payOrder = payOrderDao.queryEntityById(payId);
			if (payOrder.getState() != BaseConsts.ZERO) {// 付款状态不为待提交状态
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态有误");
			}
			BigDecimal payAmount = payRefundRelation.getPayAmount();// 支付金额
			int receipId = payRefundRelation.getReceiptId();// 水单的ID
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(receipId).getItems();
			if (DecimalUtil.eq(payAmount, DecimalUtil.ZERO)) {
				continue;
			}
			if (DecimalUtil.lt(bankReceiptResDto.getCanPaidAmount(), payAmount)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "余额不足！");
			}
			if (!payOrder.getCurrnecyType().equals(bankReceiptResDto.getCurrencyType())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不同,无法操作！");
			}
			/** 1:新增退款单信息 *****/
			int refundID = this.createRefundByCond(payRefundRelation, payOrder, bankReceiptResDto);
			/** 2:新增付款单和退款单关联的数据 ******/
			this.createPayRefundByCon(refundID, payRefundRelation, payOrder, bankReceiptResDto);
			/** 3:修改当前付款单的支付金额 *******/
			this.updatePayOrderByCon(payOrder, payRefundRelation);
			/** 4: 回写水单的已付金额 ************/
			this.updateReceiptBank(payRefundRelation, bankReceiptResDto);
		}
	}

	/**
	 * 增加当前付款单和退款单的关联数据
	 * 
	 * @param refundId
	 * @param refundRelation
	 */
	public void createPayRefundByCon(int refundID, PayRefundRelation refundRelation, PayOrder payOrder,
			BankReceiptResDto bankReceiptResDto) {
		PayRefundRelation relation = new PayRefundRelation();
		relation.setPayId(payOrder.getId());// 付款单的ID
		relation.setRefundImId(refundID);// 退款单的ID
		relation.setCreator(ServiceSupport.getUser().getChineseName());// 创建人
		relation.setCreatorId(ServiceSupport.getUser().getId());// 创建ID
		relation.setCreateAt(new Date());
		relation.setReceiptId(bankReceiptResDto.getId());// 水单的id
		relation.setProjectId(bankReceiptResDto.getProjectId());// 项目的id
		relation.setBusiUnit(bankReceiptResDto.getBusiUnit());// 经营单位
		relation.setCustId(bankReceiptResDto.getCustId());// 客户的ID
		relation.setReceiptAt(bankReceiptResDto.getReceiptDate());// 水单日期
		relation.setPayAmount(refundRelation.getPayAmount());// 付款金额
		relation.setCurrencyType(bankReceiptResDto.getCurrencyType());
		refundRelationDao.insert(relation);
	}

	/**
	 * 修改当前付款单的信息
	 * 
	 * @param payOrder
	 * @param refundRelation
	 */
	public void updatePayOrderByCon(PayOrder payOrder, PayRefundRelation refundRelation) {
		PayOrder payOrderUpd = new PayOrder();
		payOrderUpd.setId(payOrder.getId());
		// 原有的付款金额加上现在新增的付款金额
		payOrderUpd.setPayAmount(DecimalUtil.add(payOrder.getPayAmount(), refundRelation.getPayAmount()));
		payService.updatePayOrderById(payOrderUpd);
	}

	/**
	 * 更新当前水单的已付款金额
	 * 
	 * @param payRefundRelation
	 * @param bankReceiptResDto
	 */
	public void updateReceiptBank(PayRefundRelation payRefundRelation, BankReceiptResDto bankReceiptResDto) {
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(bankReceiptResDto.getId());
		// 原有的水单付款金额加上现有的付款进行
		bankReceipt.setPaidAmount(DecimalUtil.add(bankReceipt.getPaidAmount(), payRefundRelation.getPayAmount()));
		bankReceipt.setActualPaidAmount(
				DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceiptService.updateBankReceiptById(bankReceipt);
	}

	/**
	 * 退款单的批量删除 1：退款单明细和退款付款信息关联表数据的逻辑删除 2：修改当前付款单的已付金额 3: 修改水单的已付金额
	 * 
	 * @param reqDto
	 */
	public void deleteRefundByCon(PayRefundRelationReqDto reqDto) {
		int payId = reqDto.getPayId();
		for (Integer id : reqDto.getIds()) {
			PayRefundRelation payRefundRelation = refundRelationDao.selectByPrimaryKey(id);
			RefundInformation information = refundInformationDao.selectByPrimaryKey(payRefundRelation.getRefundImId());// 查询当前退款信息
			/*** 1: 逻辑删除当前退款单详情数据 **************/
			this.updateRefundByCon(information);
			/** 2: 逻辑删除当前付款单和退款的关联数据 ********/
			this.updatePayRefundByCon(payRefundRelation);
			/** 3:修改付款单已付款金额 ******************/
			this.updatePayOrderByCon(payId, information);
			/*** 4:回写水单的已付金额 *******************/
			this.updateReceiptBankByCon(information, payRefundRelation);
		}
	}

	/**
	 * 逻辑删除付款单
	 * 
	 * @param refundRelatio
	 */
	public void updateRefundByCon(RefundInformation information) {
		information.setIsDelete(BaseConsts.ONE);// 逻辑删除
		refundInformationDao.updateByPrimaryKey(information);
	}

	/**
	 * 逻辑删除当前付款单和退款单的关联数据
	 * 
	 * @param id
	 */
	public void updatePayRefundByCon(PayRefundRelation payRefundRelation) {
		payRefundRelation.setIsDelete(BaseConsts.ONE);
		refundRelationDao.updateByPrimaryKey(payRefundRelation);
	}

	/**
	 * 更新当前付款单的已付金额
	 * 
	 * @param payId
	 * @param information
	 */
	public void updatePayOrderByCon(Integer payId, RefundInformation information) {
		// 获取付款信息
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		if (payOrder.getState() != BaseConsts.ZERO) {// 付款状态不为待提交状态
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态有误");
		}
		if (DecimalUtil.lt(payOrder.getPayAmount(), information.getRefundAmount())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "余额不足！");
		}
		payOrder.setPayAmount(DecimalUtil.subtract(payOrder.getPayAmount(), information.getRefundAmount()));
		payService.updatePayOrderById(payOrder);
	}

	/**
	 * 修改当前水单账号的已付金额
	 * 
	 * @param payRefundRelation
	 * @param bankReceiptResDto
	 */
	public void updateReceiptBankByCon(RefundInformation information, PayRefundRelation payRefundRelation) {
		PayRefundRelation refundRelation = refundRelationDao.queryPayRefundByfundId(information.getId());
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(refundRelation.getReceiptId());
		if (bankReceipt.getState() != BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态有误");
		}
		bankReceipt.setPaidAmount(DecimalUtil.subtract(bankReceipt.getPaidAmount(), information.getRefundAmount()));
		bankReceipt.setActualPaidAmount(
				DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceiptService.updateBankReceiptById(bankReceipt);
	}

	/**
	 * 编辑当前退款信息的数据
	 * 
	 * @param reqDto
	 */
	public Result<PayRefundRelationResDto> editPayRefundByCon(PayRefundRelationReqDto reqDto) {
		Result<PayRefundRelationResDto> result = new Result<PayRefundRelationResDto>();
		PayRefundRelationResDto resDtos = convertPayRefundRelationResDto(
				refundRelationDao.selectByPrimaryKey(reqDto.getId()));
		result.setItems(resDtos);
		return result;
	}

	/**
	 * 退款信息的修改
	 * 
	 * @param reqDto
	 */
	public BaseResult updatePayRefundByCon(PayRefundRelationReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		Integer payId = reqDto.getPayId();
		List<PayRefundRelation> results = reqDto.getRelList();
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		if (payOrder.getState() != BaseConsts.ZERO) {// 付款状态不为待提交状态
			baseResult.setMsg("付款单状态有误");
			baseResult.setSuccess(false);
			return baseResult;
		}
		for (PayRefundRelation payRefundRelation : results) {
			int refundId = payRefundRelation.getId();
			PayRefundRelation relation = refundRelationDao.selectByPrimaryKey(refundId);
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(relation.getReceiptId())
					.getItems();
			BankReceipt bankReceipt = bankReceiptDao.queryEntityById(relation.getReceiptId());
			if (bankReceiptResDto.getState() != BaseConsts.TWO) {// 判断水单的状态2
				baseResult.setMsg("水单状态有误");
				baseResult.setSuccess(false);
				return baseResult;
			}
			if (DecimalUtil.lt(
					DecimalUtil.subtract(bankReceiptResDto.getReceiptAmount(),
							DecimalUtil.subtract(bankReceiptResDto.getPaidAmount(), relation.getPayAmount())),
					payRefundRelation.getPayAmount())) {// 退款金额和付款的金额
				baseResult.setMsg("金额不足");
				baseResult.setSuccess(false);
				return baseResult;
			}
			/*** 1:修改退款单信息的数据 *********************/
			this.updatePayRefund(relation, payRefundRelation);
			/*** 2:修改付款单的付款金额 *********************/
			PayOrder payOrderUpd = new PayOrder();
			payOrderUpd.setId(payOrder.getId());
			// 原有的付款金额加上现在新增的付款金额
			payOrderUpd.setPayAmount(
					DecimalUtil.add(DecimalUtil.subtract(payOrder.getPayAmount(), relation.getPayAmount()),
							payRefundRelation.getPayAmount()));
			payService.updatePayOrderById(payOrderUpd);
			/*** 3:修改水单的付款金额 ********************/
			bankReceipt.setPaidAmount(
					DecimalUtil.add(DecimalUtil.subtract(bankReceipt.getPaidAmount(), relation.getPayAmount()),
							payRefundRelation.getPayAmount()));
			bankReceipt.setActualPaidAmount(
					DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
			bankReceiptService.updateBankReceiptById(bankReceipt);
			/*** 4:修改付款单和退款数据的关联表 ********************/
			relation.setPayAmount(payRefundRelation.getPayAmount());
			refundRelationDao.updateByPrimaryKey(relation);
		}
		return baseResult;
	}

	/**
	 * 修改当前退款单的退款金额
	 * 
	 * @param refundId
	 */
	public void updatePayRefund(PayRefundRelation relation, PayRefundRelation payRefundRelation) {
		RefundInformation information = refundInformationDao.selectByPrimaryKey(relation.getRefundImId());
		information.setRefundAmount(payRefundRelation.getPayAmount());
		refundInformationDao.updateByPrimaryKey(information);
	}

	/**
	 * 退款信息的新增
	 * 
	 * @param payRefundRelation
	 */
	public int createRefundByCond(PayRefundRelation payRefundRelation, PayOrder payOrder,
			BankReceiptResDto bankReceiptResDto) {
		RefundInformation information = new RefundInformation();
		information.setCustId(bankReceiptResDto.getCustId());
		information.setBusiUnit(payOrder.getBusiUnit());// 经营单位
		information.setProjectId(payOrder.getProjectId());// 项目
		information.setCurrencyType(bankReceiptResDto.getCurrencyType());// 币种
		information.setReceiptDate(bankReceiptResDto.getReceiptDate());// 水单日期
		information.setRefundAmount(payRefundRelation.getPayAmount());// 水单金额
		refundInformationDao.insertRefundInformation(information);
		return information.getId();
	}

	/**
	 * 付退款提交的时候做数据的校验 1:校验当前退款的总金额是否等于付款单的已付金额 2:判断所关联的水单是否处于未核销状态
	 * 
	 * @param payOrder
	 */
	public void checkSubmitRefund(PayOrder payOrder) {
		PayRefundRelationReqDto relationReqDto = new PayRefundRelationReqDto();
		relationReqDto.setPayId(payOrder.getId());
		List<PayRefundRelation> list = refundRelationDao.queryPayRefundLaResultsByCon(relationReqDto);
		BigDecimal allRefundAmount = BigDecimal.ZERO;// 付退款总金额
		for (PayRefundRelation payRefundRelation : list) {
			BankReceipt bankReceipt = bankReceiptService.queryEntityById(payRefundRelation.getReceiptId());
			if (bankReceipt.getState() != BaseConsts.TWO) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态错误,不能提交");
			}
			if (!DecimalUtil.eq(payRefundRelation.getPayAmount(), bankReceipt.getPaidAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单已付金额和退款金额不等,不能提交");
			}
			// 做水单预收金额的赋值
			bankReceipt
					.setPreRecAmount(DecimalUtil.subtract(bankReceipt.getReceiptAmount(), bankReceipt.getPaidAmount()));// 预收金额
			bankReceipt.setWriteOffAmount(bankReceipt.getPaidAmount());// 核销金额
			bankReceipt.setActualPreRecAmount(
					DecimalUtil.multiply(bankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
			bankReceipt.setActualWriteOffAmount(
					DecimalUtil.multiply(bankReceipt.getWriteOffAmount(), bankReceipt.getActualCurrencyRate()));

			bankReceiptService.updateBankReceiptById(bankReceipt);
			allRefundAmount = DecimalUtil.add(allRefundAmount, payRefundRelation.getPayAmount());
		}
		if (!DecimalUtil.eq(payOrder.getPayAmount(), allRefundAmount)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额和退款金额不等，不能提交");
		}
	}

	/**
	 * 付退款单的核销操作 1: 水单的核销操作 2：水单已付金额回写到PId上一级的已付金额
	 * 
	 * @param order
	 */
	public void refundWriteByCon(PayOrder order) {
		PayRefundRelationReqDto relationReqDto = new PayRefundRelationReqDto();
		relationReqDto.setPayId(order.getId());
		List<PayRefundRelation> list = refundRelationDao.queryPayRefundLaResultsByCon(relationReqDto);
		for (PayRefundRelation payRefundRelation : list) {
			BankReceipt bankReceipt = bankReceiptDao.queryEntityById(payRefundRelation.getReceiptId());
			if (!DecimalUtil.eq(bankReceipt.getReceiptAmount(),
					DecimalUtil.add(bankReceipt.getWriteOffAmount(), bankReceipt.getPreRecAmount()))) {// 核销金额和预收金额与水单金额的对比
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单金额不等于核销金额和预收金额之和,不能提交");
			}
			bankReceipt.setPaidAmount(BigDecimal.ZERO);// 清空当前水单的已付款金额
			bankReceipt.setActualPaidAmount(
					DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
			bankReceiptDao.updateById(bankReceipt);
			bankReceiptService.submitBankReceiptByState(bankReceipt);
		}
	}

}
