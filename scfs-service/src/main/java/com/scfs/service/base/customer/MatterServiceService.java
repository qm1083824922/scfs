package com.scfs.service.base.customer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.base.entity.MatterServiceDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.resp.MatterServiceResDto;
import com.scfs.domain.base.entity.MatterService;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  服务要求
 *  File: MatterServiceService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年08月03日         Administrator
 *
 * </pre>
 */
@Service
public class MatterServiceService {
	@Autowired
	private MatterServiceDao matterServiceDao;

	/**
	 * 添加数据
	 * 
	 * @param matterService
	 * @return
	 */
	public BaseResult saveOrUpdateMatterService(MatterService matterService) {
		BaseResult result = new BaseResult();
		if (matterService.getId() != null) {// 修改操作
			if (matterService.getServiceType() != BaseConsts.EIGHT) {
				matterService.setServiceTypeRemark(null);
			}
			if (matterService.getAccountPeriod() != BaseConsts.FIVE) {
				matterService.setAccountPeriodRemark(null);
			}
			if (matterService.getCurrnecyType() != BaseConsts.THREE) {
				matterService.setCurrnecyTypeRemark(null);
			}
			if (matterService.getDepositPaid() != BaseConsts.TWO) {
				matterService.setDepositAmount(null);
			}
			if (matterService.getRecFeeType() != BaseConsts.SIX) {
				matterService.setRecFeeRemark(null);
			}
			if (matterService.getPayFeeType() != BaseConsts.SIX) {
				matterService.setPayFeeRemark(null);
			}
			if (matterService.getServiceSettlementTime() != BaseConsts.THREE) {
				matterService.setServiceSettlementRemark(null);
			}
			if (matterService.getLendWay() != BaseConsts.THREE) {
				matterService.setLendWayRemark(null);
			}
			matterServiceDao.queryEntityById(matterService.getMatterId());
			matterServiceDao.updateById(matterService);
		} else {// 添加操作
			matterService
					.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
			matterService.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
			matterService.setCreateAt(new Date());
			Integer resultMap = matterServiceDao.insert(matterService);
			if (resultMap < 1)
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(matterService));
		}
		return result;
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param matterService
	 * @return
	 */
	public Result<MatterServiceResDto> queryMatterServiceById(MatterService matterService) {
		Result<MatterServiceResDto> result = new Result<MatterServiceResDto>();
		MatterService service = matterServiceDao.queryEntityById(matterService.getId());
		result.setItems(convertToResDto(service));
		return result;
	}

	public MatterServiceResDto queryMatterServiceById(Integer matterId) {
		MatterServiceResDto result = convertToResDto(matterServiceDao.queryEntityById(matterId));
		return result;
	}

	/**
	 * @param model
	 * @return
	 */
	public MatterServiceResDto convertToResDto(MatterService model) {
		MatterServiceResDto result = new MatterServiceResDto();
		if (model != null) {
			result.setId(model.getId());
			result.setMatterId(model.getMatterId());
			result.setServiceType(model.getServiceType());
			result.setServiceTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_SERVICE_TYPE, model.getServiceType() + ""));
			result.setServiceTypeRemark(model.getServiceTypeRemark());
			result.setServiceExplain(model.getServiceExplain());
			result.setAccountPeriod(model.getAccountPeriod());
			result.setAccountPeriodName(ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_ACCOUNT_PERIOD,
					model.getAccountPeriod() + ""));
			result.setAccountPeriodRemark(model.getAccountPeriodRemark());
			result.setMatchedAmount(model.getMatchedAmount());
			result.setCurrnecyType(model.getCurrnecyType());
			result.setCurrnecyTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_CURRENCY_TYPE, model.getCurrnecyType() + ""));
			result.setCurrnecyTypeRemark(model.getCurrnecyTypeRemark());
			result.setDepositPaid(model.getDepositPaid());
			result.setDepositPaidName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_DEPOSIT_PAID, model.getDepositPaid() + ""));
			result.setDepositAmount(model.getDepositAmount());
			result.setServiceRateType(model.getServiceRateType());
			result.setServiceRateTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_SERVICE_RATE,
					model.getServiceRateType() + ""));
			result.setServiceRate(model.getServiceRate());
			result.setRecFeeType(model.getRecFeeType());
			result.setRecFeeTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_REC_FEE, model.getRecFeeType() + ""));
			result.setRecFeeRemark(model.getRecFeeRemark());
			result.setPayFeeType(model.getPayFeeType());
			result.setPayFeeTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_PAY_FEE, model.getPayFeeType() + ""));
			result.setPayFeeRemark(model.getPayFeeRemark());
			result.setServiceSettlementTime(model.getServiceSettlementTime());
			result.setServiceSettlementTimeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.MATTER_SERVICE_SETTLEMENT, model.getServiceSettlementTime() + ""));
			result.setServiceSettlementRemark(model.getServiceSettlementRemark());
			result.setPayWay(model.getPayWay());
			result.setPayWayName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_PAY_WAY, model.getPayWay() + ""));
			result.setLendWay(model.getLendWay());
			result.setLendWayName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_LEND_WAY, model.getLendWay() + ""));
			result.setLendWayRemark(model.getLendWayRemark());
			result.setScale(model.getScale());
			result.setTurnoverTimes(model.getTurnoverTimes());
			result.setReturnRate(model.getReturnRate());
			result.setCostExpendType(model.getCostExpendType());
			result.setCostExpendTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_COST_EXPEND, model.getCostExpendType() + ""));
			result.setBizBlance(model.getBizBlance());
			result.setBlance(model.getBlance());
			result.setSignSubject(model.getSignSubject());
			result.setProjectBizManager(model.getProjectBizManager());
			result.setCreator(model.getCreator());
			result.setCreatorId(model.getCreatorId());
			result.setCreateAt(model.getCreateAt());
			result.setCreatorId(model.getCreatorId());
		}
		return result;
	}

}
