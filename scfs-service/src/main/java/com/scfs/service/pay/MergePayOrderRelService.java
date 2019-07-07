package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.MergePayOrderDao;
import com.scfs.dao.pay.MergePayOrderRelDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.pay.dto.req.MergePayOrderRelSearchReqDto;
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderRelResDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;
import com.scfs.domain.pay.entity.MergePayOrder;
import com.scfs.domain.pay.entity.MergePayOrderRel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: MergePayOrderRelService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */

@Service
public class MergePayOrderRelService {
	@Autowired
	MergePayOrderRelDao mergePayOrderRelDao;
	@Autowired
	MergePayOrderDao mergePayOrderDao;
	@Autowired
	PayOrderDao payOrderDao;
	@Autowired
	PayPoRelationService payPoRelationService;

	public void createMergePayOrderRel(MergePayOrderRelSearchReqDto req) {
		Integer mergePayId = req.getMergePayId();
		MergePayOrder mergePayOrder = mergePayOrderDao.queryEntityById(mergePayId);
		if (mergePayOrder == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, MergePayOrderDao.class, mergePayId);
		}
		BigDecimal diffAmount = DecimalUtil.ZERO;
		List<Integer> ids = req.getIds();
		for (Integer id : ids) {
			PayOrder payOrder = payOrderDao.queryEntityById(id);
			MergePayOrderRel mergePayOrderRel = new MergePayOrderRel();
			mergePayOrderRel.setCreateAt(new Date());
			mergePayOrderRel.setMergePayId(mergePayId);
			mergePayOrderRel.setPayId(id);
			mergePayOrderRel.setPayAmount(payOrder.getPayAmount());
			if (ServiceSupport.getUser() == null) {
				mergePayOrderRel.setCreator(BaseConsts.SYSTEM_ROLE_NAME);
				mergePayOrderRel.setCreatorId(BaseConsts.SYSTEM_ROLE_ID);
			} else {
				mergePayOrderRel.setCreator(ServiceSupport.getUser().getChineseName());
				mergePayOrderRel.setCreatorId(ServiceSupport.getUser().getId());
			}
			mergePayOrderRelDao.insert(mergePayOrderRel);
			diffAmount = DecimalUtil.add(diffAmount, payOrder.getPayAmount());
			PayOrder payOrderUpd = new PayOrder();
			payOrderUpd.setId(mergePayOrderRel.getPayId());
			assert (!StringUtils.isEmpty(mergePayOrder.getMergePayNo()));
			payOrderUpd.setMergePayNo(mergePayOrder.getMergePayNo());
			payOrderDao.updateById(payOrderUpd);
		}
		MergePayOrder mergePayOrderUpd = new MergePayOrder();
		mergePayOrderUpd.setId(mergePayId);
		mergePayOrderUpd
				.setPayAmount(DecimalUtil.formatScale2(DecimalUtil.add(mergePayOrder.getPayAmount(), diffAmount)));
		mergePayOrderDao.updateById(mergePayOrderUpd);
	}

	public void deleteMergeRelById(MergePayOrderRelSearchReqDto req) {
		List<Integer> ids = req.getIds();
		Integer mergePayId = req.getMergePayId();
		BigDecimal diffAmount = DecimalUtil.ZERO;
		if (!CollectionUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				MergePayOrderRel entity = mergePayOrderRelDao.queryEntityById(id);
				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(entity.getPayId());
				payOrderUpd.setMergePayNo("-1");
				payOrderDao.updateById(payOrderUpd);
				diffAmount = DecimalUtil.add(diffAmount, entity.getPayAmount());
				mergePayOrderRelDao.deleteById(id);
			}
		}
		MergePayOrder mergePayOrder = mergePayOrderDao.queryEntityById(mergePayId);
		MergePayOrder mergePayOrderUpd = new MergePayOrder();
		mergePayOrderUpd.setId(mergePayId);
		mergePayOrderUpd
				.setPayAmount(DecimalUtil.formatScale2(DecimalUtil.subtract(mergePayOrder.getPayAmount(), diffAmount)));
		mergePayOrderDao.updateById(mergePayOrderUpd);
	}

	public MergePayOrderRelResDto detailMergeRel(Integer id) {
		return convertToMergePayOrderResDto(mergePayOrderRelDao.queryEntityById(id));
	}

	public PageResult<MergePayOrderRelResDto> queryResultsByMergeId(MergePayOrderRelSearchReqDto req) {
		PageResult<MergePayOrderRelResDto> pageResult = new PageResult<MergePayOrderRelResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<MergePayOrderRel> mergePayOrderRels = mergePayOrderRelDao.queryResultsByMergeId(req, rowBounds);
		if (!StringUtils.isEmpty(req.getMergePayId())) {
			MergePayOrder sumOrder = mergePayOrderDao.querySumById(req.getMergePayId());
			if (!StringUtils.isEmpty(sumOrder)) {
				String totalStr = "付款金额  : "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(sumOrder.getPayAmount())) + " CNY  折扣金额: "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(sumOrder.getDiscountAmount()))
						+ " CNY  折扣前金额: "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(sumOrder.getInDiscountAmount()));
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(convertToMergePayOrderResDtos(mergePayOrderRels));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	public List<MergePayOrderRelResDto> queryListByMergeId(MergePayOrderRelSearchReqDto req) {
		return convertToMergePayOrderResDtos(mergePayOrderRelDao.queryResultsByMergeId(req));
	}

	private List<MergePayOrderRelResDto> convertToMergePayOrderResDtos(List<MergePayOrderRel> mergePayOrderRels) {
		List<MergePayOrderRelResDto> mergePayOrderRelResDtos = new ArrayList<MergePayOrderRelResDto>();
		for (MergePayOrderRel mergePayOrderRel : mergePayOrderRels) {
			MergePayOrderRelResDto mergePayOrderRelResDto = new MergePayOrderRelResDto();
			BeanUtils.copyProperties(mergePayOrderRel, mergePayOrderRelResDto);
			mergePayOrderRelResDto.setStateName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_STATE, mergePayOrderRel.getState() + ""));
			mergePayOrderRelResDto.setCurrencyTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, mergePayOrderRel.getCurrencyType() + ""));
			mergePayOrderRelResDtos.add(mergePayOrderRelResDto);

			PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
			payPoRelationReqDto.setPayId(mergePayOrderRel.getPayId());
			List<PayPoRelationResDto> payPoRelationResDto = payPoRelationService
					.queryPayPoRelationAuditByCon(payPoRelationReqDto);
			BigDecimal sumSendPrice = BigDecimal.ZERO;// 计算总销售价
			if (payPoRelationResDto != null) {
				for (int i = 0; i < payPoRelationResDto.size(); i++) {
					BigDecimal sendPrice = payPoRelationResDto.get(i).getRequiredSendPrice();
					BigDecimal num = payPoRelationResDto.get(i).getGoodsNum();
					BigDecimal count = DecimalUtil.multiply(num, sendPrice);
					sumSendPrice = DecimalUtil.add(sumSendPrice, count);
				}
			}
			BigDecimal payProfit = BigDecimal.ZERO;
			if (DecimalUtil.gt(sumSendPrice, BigDecimal.ZERO)) {
				BigDecimal count = DecimalUtil.subtract(sumSendPrice, mergePayOrderRel.getPayAmount());
				payProfit = DecimalUtil.divide(count, sumSendPrice);
			}
			mergePayOrderRelResDto.setSumProfit(DecimalUtil.toPercentString(payProfit));
			if (DecimalUtil.gt(mergePayOrderRel.getInDiscountAmount(), DecimalUtil.ZERO)) {
				mergePayOrderRelResDto.setDiscountRateStr(DecimalUtil.toPercentString(DecimalUtil
						.divide(mergePayOrderRel.getDiscountAmount(), mergePayOrderRel.getInDiscountAmount())));
			} else {
				mergePayOrderRelResDto.setDiscountRateStr("0.00%");
			}
			mergePayOrderRelResDto.setPayWayType(mergePayOrderRel.getPayWayType());
			mergePayOrderRelResDto.setPayWayTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_WAY_TYPE,
					mergePayOrderRel.getPayWayType() + ""));
			mergePayOrderRelResDto.setRemark(mergePayOrderRel.getRemark());
		}
		return mergePayOrderRelResDtos;
	}

	private MergePayOrderRelResDto convertToMergePayOrderResDto(MergePayOrderRel mergePayOrderRel) {
		MergePayOrderRelResDto mergePayOrderRelResDto = new MergePayOrderRelResDto();
		BeanUtils.copyProperties(mergePayOrderRel, mergePayOrderRelResDto);
		mergePayOrderRelResDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_STATE, mergePayOrderRel.getState() + ""));
		mergePayOrderRelResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				mergePayOrderRel.getCurrencyType() + ""));
		return mergePayOrderRelResDto;
	}

}
