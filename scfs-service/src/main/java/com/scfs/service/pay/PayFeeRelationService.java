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
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayFeeRelationDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayFeeRelationResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.PayFeeRelation;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.support.ServiceSupport;

@Service
public class PayFeeRelationService {
	@Autowired
	private PayFeeRelationDao payFeeRelationDao;

	@Autowired
	private FeeServiceImpl feeService;// 费用信息

	@Autowired
	private PayOrderDao payOrderDao;

	@Autowired
	private PayService payService;

	/**
	 * 获取费用列表
	 * 
	 * @param queryRecPayFeeReqDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryRecPayFeeReqDto) {
		PayOrder pay = payOrderDao.queryEntityById(queryRecPayFeeReqDto.getId());
		if (pay.getState().compareTo(BaseConsts.ZERO) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误");
		}
		queryRecPayFeeReqDto.setProjectId(pay.getProjectId());
		queryRecPayFeeReqDto.setCustReceiver(pay.getPayee());
		queryRecPayFeeReqDto.setIsPayAll(BaseConsts.ONE);
		queryRecPayFeeReqDto.setState(BaseConsts.THREE);
		queryRecPayFeeReqDto.setCurrencyType(pay.getCurrnecyType());
		return feeService.queryFeeByCond(queryRecPayFeeReqDto);
	}

	/**
	 * 费用信息添加
	 * 
	 * @param record
	 * @return
	 */
	public BaseResult craetePayFeeRelation(PayFeeRelationReqDto record) {
		BaseResult baseResult = new BaseResult();
		int payId = record.getPayId();
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		BigDecimal feeBlance = payOrder.getFeeBlance();

		List<PayFeeRelation> relList = record.getRelList();
		for (PayFeeRelation payFeeRel : relList) {
			// 判断输入金额是否小于费余额
			int feeId = payFeeRel.getFeeId();
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal orderAmount = feeQueryModel.getPayAmount();// 费用金额
			BigDecimal oldAmount = feeQueryModel.getPaidAmount();// 已付款金额
			BigDecimal nowAmout = DecimalUtil.subtract(orderAmount, oldAmount);// 费用余额
			BigDecimal feeAmount = payFeeRel.getPayAmount();// 付款金额
			if (DecimalUtil.multiply(feeAmount, nowAmout).doubleValue() < 0) {
				baseResult.setMsg("费用金额有误！");
				return baseResult;
			}
			if (DecimalUtil.lt(nowAmout.abs(), feeAmount.abs())) {
				baseResult.setMsg("费用余额不足！");
				return baseResult;
			}
		}
		Date date = new Date();
		BigDecimal sum = BigDecimal.ZERO;
		for (PayFeeRelation payFeeRel : relList) {
			BigDecimal payAmount = payFeeRel.getPayAmount();
			int feeId = payFeeRel.getFeeId();
			// 添加关系
			PayFeeRelation addPayFeeRel = new PayFeeRelation();
			addPayFeeRel.setPayId(payId);
			addPayFeeRel.setFeeId(feeId);
			addPayFeeRel.setPayAmount(payAmount);
			addPayFeeRel.setCreateAt(date);
			addPayFeeRel.setCreator(ServiceSupport.getUser().getChineseName());
			addPayFeeRel.setCreatorId(ServiceSupport.getUser().getId());
			payFeeRelationDao.insert(addPayFeeRel);
			// 修改费用已付金额
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal oldAmount = feeQueryModel.getPaidAmount();// 已付款金额
			Fee fee = new Fee();
			fee.setId(feeQueryModel.getId());
			fee.setPaidAmount(DecimalUtil.add(oldAmount, payAmount));
			feeService.updateFeeById(fee);
			// 添加总额
			sum = DecimalUtil.add(sum, payAmount);
		}
		// 修改付款费用总金额
		PayOrder upPayOrder = new PayOrder();
		upPayOrder.setId(payId);
		upPayOrder.setFeeBlance(DecimalUtil.add(feeBlance, sum));
		upPayOrder.setPayAmount(DecimalUtil.add(payOrder.getPayAmount(), sum));
		payService.updatePayOrderById(upPayOrder);
		return baseResult;
	}

	/**
	 * 修改费用单信息
	 * 
	 * @param record
	 * @return
	 */
	public BaseResult updatePayFeeRelation(PayFeeRelationReqDto record) {
		BaseResult baseResult = new BaseResult();

		List<PayFeeRelation> relList = record.getRelList();
		for (PayFeeRelation payFeeRel : relList) {
			BigDecimal feeAmount = payFeeRel.getPayAmount();// 付款金额
			// 判断输入金额是否小于费余额
			PayFeeRelation payFee = payFeeRelationDao.queryPayFeeById(payFeeRel.getId());
			int feeId = payFee.getFeeId();
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal diffPayAmount = DecimalUtil.subtract(feeQueryModel.getPaidAmount(), payFee.getPayAmount());// 已付款金额减去上次录入
			BigDecimal orderAmount = feeQueryModel.getPayAmount();// 费用金额
			BigDecimal nowAmout = DecimalUtil.add(feeAmount, diffPayAmount);
			if (DecimalUtil.lt(orderAmount.abs(), nowAmout.abs())) {
				baseResult.setMsg("费用余额不足！");
				return baseResult;
			}

			if (DecimalUtil.multiply(orderAmount, nowAmout).doubleValue() < 0) {
				baseResult.setMsg("费用金额有误！");
				return baseResult;
			}
		}
		for (PayFeeRelation payFeeRel : relList) {
			int id = payFeeRel.getId();
			BigDecimal payAmount = payFeeRel.getPayAmount();
			// 计算修改差额并修改
			PayFeeRelation oldEntity = payFeeRelationDao.queryPayFeeById(id);
			BigDecimal diffPayAmount = DecimalUtil.subtract(payAmount, oldEntity.getPayAmount());// 本次输入与上次金额差额
			// 修改本次金额
			PayFeeRelation payPeeRel = new PayFeeRelation();
			payPeeRel.setId(id);
			payPeeRel.setPayAmount(payAmount);
			payFeeRelationDao.updateById(payPeeRel);
			// 修改费用已付款金额
			int feeId = oldEntity.getFeeId();
			FeeQueryModel feeQueryModel = feeService.queryEntityById(feeId).getItems();
			BigDecimal oldAmount = feeQueryModel.getPaidAmount();
			oldAmount = DecimalUtil.add(oldAmount, diffPayAmount);// 已付款金额
			Fee fee = new Fee();
			fee.setId(feeQueryModel.getId());
			fee.setPaidAmount(oldAmount);
			feeService.updateFeeById(fee);
			// 修改付款费用总金额
			int payId = oldEntity.getPayId();
			PayOrder payOrder = new PayOrder();
			payOrder.setId(payId);
			PayOrderResDto payOrderResDto = payService.editPayOrderById(payOrder).getItems();
			BigDecimal feeBlance = payOrderResDto.getFeeBlance();
			PayOrder upPayOrder = new PayOrder();
			upPayOrder.setId(payId);
			upPayOrder.setFeeBlance(DecimalUtil.add(feeBlance, diffPayAmount));
			upPayOrder.setPayAmount(DecimalUtil.add(payOrderResDto.getPayAmount(), diffPayAmount));
			payService.updatePayOrderById(upPayOrder);
		}
		return baseResult;

	}

	/**
	 * 编辑付款费用信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	public Result<PayFeeRelationResDto> editPayFeeRelationById(PayFeeRelationReqDto payFeeRelationReqDto) {
		Result<PayFeeRelationResDto> result = new Result<PayFeeRelationResDto>();
		List<PayFeeRelationModel> listFeeRel = payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto);
		PayFeeRelationResDto payPo = convertPayFeeRelationResDto(listFeeRel.get(BaseConsts.ZERO));
		result.setItems(payPo);
		return result;
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public BaseResult deletePayFeeRelation(PayFeeRelationReqDto payFeeRelation) {
		for (Integer id : payFeeRelation.getIds()) {
			PayFeeRelation oldEntity = payFeeRelationDao.queryPayFeeById(id);
			// 减少费用信息相关金额
			FeeQueryModel feeQueryModel = feeService.queryEntityById(oldEntity.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(feeQueryModel.getId());
			upFee.setPaidAmount(DecimalUtil.subtract(feeQueryModel.getPaidAmount(), oldEntity.getPayAmount()));
			feeService.updateFeeById(upFee);
			// 减少付款费用总金额
			int payId = oldEntity.getPayId();
			PayOrder payOrder = new PayOrder();
			payOrder.setId(payId);
			PayOrderResDto payOrderResDto = payService.editPayOrderById(payOrder).getItems();
			PayOrder upPayOrder = new PayOrder();
			upPayOrder.setId(payId);
			upPayOrder.setFeeBlance(DecimalUtil.subtract(payOrderResDto.getFeeBlance(), oldEntity.getPayAmount()));
			upPayOrder.setPayAmount(DecimalUtil.subtract(payOrderResDto.getPayAmount(), oldEntity.getPayAmount()));
			payService.updatePayOrderById(upPayOrder);
			// 删除
			PayFeeRelation payFeeRel = new PayFeeRelation();
			payFeeRel.setId(id);
			payFeeRel.setIsDelete(BaseConsts.ONE);
			payFeeRelationDao.updateById(payFeeRel);
		}
		return new BaseResult();
	}

	/**
	 * 获取所有信息接口
	 * 
	 * @param payFeeRelationReqDto
	 * @return
	 */
	public List<PayFeeRelationModel> queryPayFeeRelatioByCon(PayFeeRelationReqDto payFeeRelationReqDto) {
		return payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto);
	}

	/**
	 * 获取所有付款订单数据
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	public List<PayFeeRelationResDto> queryPayFeeRelationAuditByCon(PayFeeRelationReqDto payFeeRelationReqDto) {
		List<PayFeeRelationResDto> payFeeRelationResDto = convertToPayFeeRelationResDtos(
				payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto));
		return payFeeRelationResDto;
	}

	/**
	 * 通过付款订单查询费用总额 TODO.
	 *
	 * @param payId
	 * @return
	 */
	public List<PayFeeRelationModel> queryGroupFeeByPayOrderId(Integer payId) {
		List<PayFeeRelationModel> payFeeRelationModel = payFeeRelationDao.queryGroupFeeByPayOrderId(payId);
		return payFeeRelationModel;
	}

	/**
	 * 获取列表信息
	 * 
	 * @param payFeeRelationReqDto
	 * @return
	 */
	public PageResult<PayFeeRelationResDto> queryPayFeeRelationResultsByCon(PayFeeRelationReqDto payFeeRelationReqDto) {
		PageResult<PayFeeRelationResDto> pageResult = new PageResult<PayFeeRelationResDto>();
		int offSet = PageUtil.getOffSet(payFeeRelationReqDto.getPage(), payFeeRelationReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payFeeRelationReqDto.getPer_page());
		List<PayFeeRelationResDto> payFeeRelationResDto = convertToPayFeeRelationResDtos(
				payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto, rowBounds));
		pageResult.setItems(payFeeRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payFeeRelationReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payFeeRelationReqDto.getPage());
		pageResult.setPer_page(payFeeRelationReqDto.getPer_page());

		List<PayFeeRelationResDto> payFeeResDto = convertToPayFeeRelationResDtos(
				payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto));
		BigDecimal payAmountSum = BigDecimal.ZERO;
		for (PayFeeRelationResDto payFeeRela : payFeeResDto) {
			if (payFeeRela != null) {
				payAmountSum = DecimalUtil.add(payAmountSum, payFeeRela.getPayAmount());
			}
		}
		String totalStr = "合计  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum));
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<PayFeeRelationResDto> convertToPayFeeRelationResDtos(List<PayFeeRelationModel> result) {
		List<PayFeeRelationResDto> payFeeRelationResDto = new ArrayList<PayFeeRelationResDto>();
		if (ListUtil.isEmpty(result)) {
			return payFeeRelationResDto;
		}
		for (PayFeeRelationModel model : result) {
			PayFeeRelationResDto poRelationResDto = convertPayFeeRelationResDto(model);
			payFeeRelationResDto.add(poRelationResDto);
		}
		return payFeeRelationResDto;
	}

	public PayFeeRelationResDto convertPayFeeRelationResDto(PayFeeRelationModel model) {
		PayFeeRelationResDto result = new PayFeeRelationResDto();
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
		result.setWriteOffFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.WRITE_OFF_FLAG, model.getWriteOffFlag() + ""));
		return result;
	}
}
