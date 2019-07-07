package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayDeductionFeeRelationDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.pay.PayPoRelationDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.pay.dto.req.PayDeductionFeeRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayDeductionFeeRelationResDto;
import com.scfs.domain.pay.entity.PayDeductionFeeRelation;
import com.scfs.domain.pay.entity.PayDeductionFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class PayDeductionFeeRelationService {
	@Autowired
	private PayDeductionFeeRelationDao payDeductionFeeRelationDao;
	@Autowired
	private FeeServiceImpl feeService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayPoRelationDao payPoRelationDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private PayService payService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private CacheService cacheService;

	/**
	 * 获取抵扣费用列表
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryFeeReqDto) {
		PayOrder pay = payOrderDao.queryEntityById(queryFeeReqDto.getId());
		if (pay.getState() != BaseConsts.ZERO && pay.getState() != BaseConsts.SIX) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		} else if ((pay.getState() == BaseConsts.SIX && pay.getNoneOrderFlag() == BaseConsts.ZERO)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		}
		queryFeeReqDto.setProjectId(pay.getProjectId());
		queryFeeReqDto.setCustReceiver(pay.getPayee());
		queryFeeReqDto.setIsPayAll(BaseConsts.ONE);
		queryFeeReqDto.setState(BaseConsts.THREE);
		queryFeeReqDto.setCurrencyType(pay.getCurrnecyType());
		queryFeeReqDto.setFeeType(BaseConsts.FIVE);
		return feeService.queryFeeByCond(queryFeeReqDto);
	}

	/**
	 * 抵扣费用信息添加
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	public BaseResult createPayDeductionFeeRelation(PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		BaseResult baseResult = new BaseResult();
		int payId = payDeductionFeeRelationReqDto.getPayId();
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		int state = payOrder.getState();

		List<PayDeductionFeeRelation> relList = payDeductionFeeRelationReqDto.getRelList();
		for (PayDeductionFeeRelation payFeeRel : relList) {
			// 判断输入金额是否小于费用余额
			int feeId = payFeeRel.getFeeId();
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal payAmount = feeQueryModel.getPayAmount();// 抵扣费用金额
			BigDecimal paidAmount = feeQueryModel.getPaidAmount();// 已付款金额
			BigDecimal nowAmount = DecimalUtil.subtract(payAmount, paidAmount);// 抵扣费用余额
			BigDecimal feeAmount = payFeeRel.getPayAmount();// 付款金额
			if (DecimalUtil.multiply(feeAmount, nowAmount).doubleValue() < 0) {
				baseResult.setMsg("抵扣费用金额有误！");
				return baseResult;
			}
			if (DecimalUtil.lt(nowAmount.abs(), feeAmount.abs())) {
				baseResult.setMsg("抵扣费用余额不足！");
				return baseResult;
			}
		}
		Date date = new Date();
		BigDecimal sum = BigDecimal.ZERO;
		for (PayDeductionFeeRelation payFeeRel : relList) {
			BigDecimal payAmount = payFeeRel.getPayAmount();
			int feeId = payFeeRel.getFeeId();
			// 添加关系
			PayDeductionFeeRelation addPayFeeRel = new PayDeductionFeeRelation();
			addPayFeeRel.setPayId(payId);
			addPayFeeRel.setFeeId(feeId);
			addPayFeeRel.setPayAmount(payAmount);
			addPayFeeRel.setCreateAt(date);
			addPayFeeRel.setCreator(ServiceSupport.getUser().getChineseName());
			addPayFeeRel.setCreatorId(ServiceSupport.getUser().getId());
			payDeductionFeeRelationDao.insert(addPayFeeRel);
			// 修改抵扣费用已付金额
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal oldAmount = feeQueryModel.getPaidAmount();// 已付款金额
			Fee fee = new Fee();
			fee.setId(feeQueryModel.getId());
			fee.setPaidAmount(DecimalUtil.add(oldAmount, payAmount));
			feeService.updateFeeById(fee);
			// 添加总额
			sum = DecimalUtil.add(sum, payAmount);
		}
		// 修改付款抵扣费用总金额
		PayOrder upPayOrder = new PayOrder();
		upPayOrder.setId(payId);
		upPayOrder.setDeductionFeeAmount(DecimalUtil.add(payOrder.getDeductionFeeAmount(), sum));
		if (!(state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ONE)) {	//已完成且无单据付款，不需要重算付款金额
			upPayOrder.setPayAmount(getPayAmount(payOrder, sum, upPayOrder.getDeductionFeeAmount(), BaseConsts.ONE));
		}
		payService.updatePayOrderById(upPayOrder);
		return baseResult;
	}

	/**
	 * 获取付款金额
	 * 
	 * @param payOrder
	 *            付款单
	 * @param deductionFeeAmount
	 *            变化的抵扣费用
	 * @param totalDeductionFeeAmount
	 *            总的抵扣费用
	 * @param operateType
	 *            1-新增 2-修改 3-删除
	 * @return
	 */
	public BigDecimal getPayAmount(PayOrder payOrder, BigDecimal deductionFeeAmount, BigDecimal totalDeductionFeeAmount,
			Integer operateType) {
		BigDecimal currPayAmount = BigDecimal.ZERO;
		BigDecimal payAmount = BigDecimal.ZERO;
		List<PayPoRelation> payPoRelationList = payPoRelationDao.queryPayPoListByPayId(payOrder.getId());
		if (!CollectionUtils.isEmpty(payPoRelationList)) {
			BigDecimal totalPayAmount = payPoRelationDao.queryTotalPayAmount(payOrder.getId());
			BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());
			for (PayPoRelation payPoRelation : payPoRelationList) {
				PoLineModel poLineModel = purchaseOrderLineDao.queryPoLineByPoLineId(payPoRelation.getPoLineId());
				payAmount = purchaseOrderService.calcPayAmount(poLineModel, payOrder.getPayWayType(),
						baseProject.getBizType(), payPoRelation.getPayAmount(), totalPayAmount,
						totalDeductionFeeAmount);
				currPayAmount = DecimalUtil.add(currPayAmount, payAmount);
			}
		} else {
			if (operateType.equals(BaseConsts.THREE)) {
				payAmount = DecimalUtil.add(payOrder.getPayAmount(), deductionFeeAmount);
			} else {
				payAmount = DecimalUtil.subtract(payOrder.getPayAmount(), deductionFeeAmount);
			}
			currPayAmount = payAmount;
		}
		return currPayAmount;
	}

	/**
	 * 修改抵扣费用单信息
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	public BaseResult updatePayDeductionFeeRelation(PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		BaseResult baseResult = new BaseResult();
		Integer payId = payDeductionFeeRelationReqDto.getPayId();
		List<PayDeductionFeeRelation> relList = payDeductionFeeRelationReqDto.getRelList();
		for (PayDeductionFeeRelation payFeeRel : relList) {
			BigDecimal feeAmount = payFeeRel.getPayAmount();// 付款金额
			// 判断输入金额是否小于费用余额
			PayDeductionFeeRelation payFee = payDeductionFeeRelationDao.queryLockEntityById(payFeeRel.getId());
			int feeId = payFee.getFeeId();
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal diffPayAmount = DecimalUtil.subtract(feeQueryModel.getPaidAmount(), payFee.getPayAmount());// 已付款金额减去上次录入
			BigDecimal orderAmount = feeQueryModel.getPayAmount();// 抵扣费用金额
			BigDecimal nowAmout = DecimalUtil.add(feeAmount, diffPayAmount);
			if (DecimalUtil.lt(orderAmount.abs(), nowAmout.abs())) {
				baseResult.setMsg("抵扣费用余额不足！");
				return baseResult;
			}

			if (DecimalUtil.multiply(orderAmount, nowAmout).doubleValue() < 0) {
				baseResult.setMsg("抵扣费用金额有误！");
				return baseResult;
			}
		}
		BigDecimal sum = BigDecimal.ZERO;
		for (PayDeductionFeeRelation payFeeRel : relList) {
			int id = payFeeRel.getId();
			BigDecimal payAmount = payFeeRel.getPayAmount();
			// 计算修改差额并修改
			PayDeductionFeeRelation oldEntity = payDeductionFeeRelationDao.queryLockEntityById(id);
			BigDecimal diffPayAmount = DecimalUtil.subtract(payAmount, oldEntity.getPayAmount());// 本次输入与上次金额差额
			// 修改本次金额
			PayDeductionFeeRelation payPeeRel = new PayDeductionFeeRelation();
			payPeeRel.setId(id);
			payPeeRel.setPayAmount(payAmount);
			payDeductionFeeRelationDao.updateById(payPeeRel);
			// 修改抵扣费用已付款金额
			int feeId = oldEntity.getFeeId();
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal oldAmount = feeQueryModel.getPaidAmount();
			oldAmount = DecimalUtil.add(oldAmount, diffPayAmount);// 已付款金额
			Fee fee = new Fee();
			fee.setId(feeQueryModel.getId());
			fee.setPaidAmount(oldAmount);
			feeService.updateFeeById(fee);
			sum = DecimalUtil.add(sum, diffPayAmount);
		}
		// 修改付款抵扣费用总金额
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		int state = payOrder.getState();
		PayOrder upPayOrder = new PayOrder();
		upPayOrder.setId(payId);
		upPayOrder.setDeductionFeeAmount(DecimalUtil.add(payOrder.getDeductionFeeAmount(), sum));
		if (!(state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ONE)) {	//已完成且无单据付款，不需要重算付款金额
			upPayOrder.setPayAmount(getPayAmount(payOrder, sum, upPayOrder.getDeductionFeeAmount(), BaseConsts.TWO));
		}
		payService.updatePayOrderById(upPayOrder);
		return baseResult;
	}

	/**
	 * 编辑付款抵扣费用信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	public Result<PayDeductionFeeRelationResDto> editPayDeductionFeeRelationById(
			PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		Result<PayDeductionFeeRelationResDto> result = new Result<PayDeductionFeeRelationResDto>();
		List<PayDeductionFeeRelationModel> listFeeRel = payDeductionFeeRelationDao
				.queryResultsByCon(payDeductionFeeRelationReqDto);
		PayDeductionFeeRelationResDto payPo = convertPayDeductionFeeRelationResDto(listFeeRel.get(BaseConsts.ZERO));
		result.setItems(payPo);
		return result;
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public BaseResult deletePayDeductionFeeRelation(PayDeductionFeeRelationReqDto payDeductionFeeRelation) {
		Integer payId = payDeductionFeeRelation.getPayId();
		BigDecimal sum = BigDecimal.ZERO;
		for (Integer id : payDeductionFeeRelation.getIds()) {
			PayDeductionFeeRelation oldEntity = payDeductionFeeRelationDao.queryLockEntityById(id);
			// 减少抵扣费用信息相关金额
			FeeQueryModel feeQueryModel = feeService.queryEntityById(oldEntity.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(feeQueryModel.getId());
			upFee.setPaidAmount(DecimalUtil.subtract(feeQueryModel.getPaidAmount(), oldEntity.getPayAmount()));
			feeService.updateFeeById(upFee);

			// 删除
			PayDeductionFeeRelation payFeeRel = new PayDeductionFeeRelation();
			payFeeRel.setId(id);
			payFeeRel.setIsDelete(BaseConsts.ONE);
			payDeductionFeeRelationDao.updateById(payFeeRel);

			sum = DecimalUtil.add(sum, oldEntity.getPayAmount());
		}
		// 修改付款抵扣费用总金额
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		int state = payOrder.getState();
		PayOrder upPayOrder = new PayOrder();
		upPayOrder.setId(payId);
		upPayOrder.setDeductionFeeAmount(DecimalUtil.subtract(payOrder.getDeductionFeeAmount(), sum));
		if (!(state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ONE)) {	//已完成且无单据付款，不需要重算付款金额
			upPayOrder.setPayAmount(getPayAmount(payOrder, sum, upPayOrder.getDeductionFeeAmount(), BaseConsts.THREE));
		}
		payService.updatePayOrderById(upPayOrder);
		return new BaseResult();
	}

	/**
	 * 获取所有信息接口
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	public List<PayDeductionFeeRelationModel> queryPayFeeRelatioByCon(
			PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		return payDeductionFeeRelationDao.queryResultsByCon(payDeductionFeeRelationReqDto);
	}

	/**
	 * 获取所有付款订单数据
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	public List<PayDeductionFeeRelationResDto> queryPayDeductionFeeRelationAuditByCon(
			PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		List<PayDeductionFeeRelationResDto> payDeductionFeeRelationResDto = convertToPayDeductionFeeRelationResDtos(
				payDeductionFeeRelationDao.queryResultsByCon(payDeductionFeeRelationReqDto));
		return payDeductionFeeRelationResDto;
	}

	/**
	 * 通过付款订单查询抵扣费用总额
	 *
	 * @param payId
	 * @return
	 */
	public List<PayDeductionFeeRelationModel> queryGroupFeeByPayOrderId(Integer payId) {
		List<PayDeductionFeeRelationModel> payDeductionFeeRelationModel = payDeductionFeeRelationDao
				.queryGroupDeductionFeeByPayOrderId(payId);
		return payDeductionFeeRelationModel;
	}

	/**
	 * 获取列表信息
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	public PageResult<PayDeductionFeeRelationResDto> queryPayDeductionFeeRelationResultsByCon(
			PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		PageResult<PayDeductionFeeRelationResDto> pageResult = new PageResult<PayDeductionFeeRelationResDto>();
		int offSet = PageUtil.getOffSet(payDeductionFeeRelationReqDto.getPage(),
				payDeductionFeeRelationReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payDeductionFeeRelationReqDto.getPer_page());
		List<PayDeductionFeeRelationResDto> payDeductionFeeRelationResDto = convertToPayDeductionFeeRelationResDtos(
				payDeductionFeeRelationDao.queryResultsByCon(payDeductionFeeRelationReqDto, rowBounds));
		pageResult.setItems(payDeductionFeeRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payDeductionFeeRelationReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payDeductionFeeRelationReqDto.getPage());
		pageResult.setPer_page(payDeductionFeeRelationReqDto.getPer_page());

		List<PayDeductionFeeRelationResDto> payFeeResDto = convertToPayDeductionFeeRelationResDtos(
				payDeductionFeeRelationDao.queryResultsByCon(payDeductionFeeRelationReqDto));
		BigDecimal payAmountSum = BigDecimal.ZERO;
		for (PayDeductionFeeRelationResDto payFeeRela : payFeeResDto) {
			if (payFeeRela != null) {
				payAmountSum = DecimalUtil.add(payAmountSum, payFeeRela.getPayAmount());
			}
		}
		String totalStr = "合计  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum));
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<PayDeductionFeeRelationResDto> convertToPayDeductionFeeRelationResDtos(
			List<PayDeductionFeeRelationModel> result) {
		List<PayDeductionFeeRelationResDto> payDeductionFeeRelationResDto = new ArrayList<PayDeductionFeeRelationResDto>();
		if (ListUtil.isEmpty(result)) {
			return payDeductionFeeRelationResDto;
		}
		for (PayDeductionFeeRelationModel model : result) {
			PayDeductionFeeRelationResDto poRelationResDto = convertPayDeductionFeeRelationResDto(model);
			payDeductionFeeRelationResDto.add(poRelationResDto);
		}
		return payDeductionFeeRelationResDto;
	}

	public PayDeductionFeeRelationResDto convertPayDeductionFeeRelationResDto(PayDeductionFeeRelationModel model) {
		PayDeductionFeeRelationResDto result = new PayDeductionFeeRelationResDto();
		result.setId(model.getId());
		result.setPayId(model.getPayId());
		result.setPayNo(model.getPayNo());
		result.setFeeId(model.getFeeId());
		result.setPayAmount(model.getPayAmount());
		result.setFeeNo(model.getFeeNo());
		result.setFeeType(model.getFeeType());
		result.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, model.getFeeType() + ""));
		result.setPayDate(model.getPayDate());
		result.setExpPayAmount(model.getExpPayAmount());
		result.setAcceptInvoiceAmount(model.getAcceptInvoiceAmount());
		result.setOldPayAmount(model.getOldPayAmount());
		result.setPaymentAmount(DecimalUtil.subtract(DecimalUtil.add(model.getExpPayAmount(), model.getPayAmount()),
				model.getOldPayAmount()));
		return result;
	}
}
