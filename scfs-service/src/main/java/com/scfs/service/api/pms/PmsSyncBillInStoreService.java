package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.api.pms.PmsStoreInDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsStoreInHttpResDto;
import com.scfs.domain.api.pms.entity.PmsSeries;
import com.scfs.domain.api.pms.entity.PmsStoreIn;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;
import com.scfs.domain.interf.dto.PmsStoreResDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.interf.PMSSupplierBindService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class PmsSyncBillInStoreService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncBillInStoreService.class);
	@Autowired
	private InvokeConfig invokeConfig;

	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private PmsStoreInDao pmsStoreInDao;
	@Autowired
	private PmsSeriesService pmsSeriesService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BaseSubjectService baseSubjectService;
	@Autowired
	private PMSSupplierBindService pmsSupplierBindService;

	@IgnoreTransactionalMark
	public List<PmsStoreInHttpResDto> doPmsSyncBillInStore(PmsHttpReqDto req) {
		// pms日志
		List<PmsStoreInHttpResDto> returnList = new ArrayList<PmsStoreInHttpResDto>();
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.THREE);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setBillType(BaseConsts.FOUR);
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_STORE_IN.getType()); // pms同步入库接口
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);
		// 接口调用状态描述
		PmsSeries pmsSeries = pmsSeriesService.createPmsSeries(BaseConsts.ONE);
		try {
			// pms调用scfs接口
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			List<PmsStoreIn> pmsStoreInList = JSON.parseArray(data, PmsStoreIn.class);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					invokeLog.setReturnMsg(JSON.toJSONString("请求非法: 签名校验出错"));
					invokeLogService.invokeError(invokeLog);
					return returnList;
				}
			}
			try {
				returnList = checkData(pmsStoreInList); // 接口前置检查
			} catch (BaseException e) {
				invokeLog.setReturnMsg(invokeLogService.getMsg(JSON.toJSONString(returnList)));
				invokeLogService.invokeError(invokeLog);
				return returnList;
			}
			invokeLog.setReturnMsg(JSON.toJSONString(invokeLogService.getMsg(JSON.toJSONString(returnList))));
			invokeLogService.invokeSuccess(invokeLog);
			createPmsBillInStore(pmsStoreInList, pmsSeries, returnList);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步入库接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			invokeLog.setReturnMsg(JSON.toJSONString(returnList));
			invokeLogService.invokeException(invokeLog, e);
		}
		return returnList;
	}

	private void createPmsBillInStore(List<PmsStoreIn> pmsStoreInList, PmsSeries pmsSeries,
			List<PmsStoreInHttpResDto> returnList) {
		// TODO Auto-generated method stub
		for (PmsStoreIn pmsStoreIn : pmsStoreInList) {
			try {
				pmsStoreIn.setCreateAt(new Date());
				pmsStoreIn.setPmsSeriesId(pmsSeries.getId());
				pmsStoreInDao.insert(pmsStoreIn);
			} catch (Exception e) {
				for (PmsStoreInHttpResDto pmsStoreInHttpResDto : returnList) {
					if (pmsStoreInHttpResDto.getPurchase_delivery_sn().equals(pmsStoreIn.getPurchase_delivery_sn())
							&& pmsStoreInHttpResDto.getSku().equals(pmsStoreIn.getSku())
							&& pmsStoreInHttpResDto.getPurchase_sn().equals(pmsStoreIn.getPurchase_sn())) {
						StringBuilder return_msg = new StringBuilder(pmsStoreInHttpResDto.getMsg());
						pmsStoreInHttpResDto.setFlag(BaseConsts.FLAG_NO);
						return_msg.append("插入失败");
						pmsStoreIn.setMsg(commonService.getMsg(return_msg.toString()));
						break;
					}
				}
			}
		}
	}

	public List<PmsStoreInHttpResDto> checkData(List<PmsStoreIn> pmsStoreInList) {
		List<PmsStoreInHttpResDto> returnList = new ArrayList<PmsStoreInHttpResDto>();
		if (pmsStoreInList == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		for (PmsStoreIn pmsStoreIn : pmsStoreInList) {
			StringBuilder return_msg = new StringBuilder();
			PMSSupplierBind pmsSupplierBind = new PMSSupplierBind();
			PmsStoreInHttpResDto pmsStoreInHttpResDto = new PmsStoreInHttpResDto();
			// 根据结算对象查经营单位
			QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
			querySubjectReqDto.setPmsSupplierCode(pmsStoreIn.getAccount_sn());
			querySubjectReqDto.setSubjectType(BaseConsts.ONE);
			List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
			if (CollectionUtils.isEmpty(custs) || custs.size() > 1) {
				return_msg.append("PMS结算对象供应商编码【" + pmsStoreIn.getAccount_sn() + "】不存在或者不唯一");
			} else {
				PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
				pMSSupplierBindReqDto.setPmsSupplierNo(pmsStoreIn.getProvider_sn());
				pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
				pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
			}
			if (pmsSupplierBind != null && null != pmsSupplierBind.getProjectId()) {
				// 通过项目下的商品是否存在
				if (StringUtils.isEmpty(pmsStoreIn.getSku())) {
					return_msg.append("sku不能为空");
				}
				if (null == pmsStoreIn.getPurchase_price()) {
					return_msg.append("入库价格[purchase_price]不能为空;");
				}
				if (StringUtils.isEmpty(pmsStoreIn.getCurrency_type())) {
					return_msg.append("币种[currency_type]不能为空;");
				}
				if (StringUtils.isEmpty(pmsStoreIn.getPurchase_delivery_sn())) {
					return_msg.append("送货单号[purchase_delivery_sn]不能为空;");
				}
				if (StringUtils.isEmpty(pmsStoreIn.getPurchase_sn())) {
					return_msg.append("采购单号[purchase_sn]不能为空;");
				}
				if (StringUtils.isEmpty(pmsStoreIn.getProvider_sn())) {
					return_msg.append("供应商编号[provider_sn]不能为空;");
				}
				if (null == pmsStoreIn.getStockin_num()
						|| DecimalUtil.ge(BigDecimal.ZERO, pmsStoreIn.getStockin_num())) {
					return_msg.append("入库数量[stockin_num]不能为空或者小于等于0;");
				}
				if (StringUtils.isEmpty(pmsStoreIn.getAccount_sn())) {
					return_msg.append("结算对象[account_sn]不能为空");
				}
				pmsStoreInHttpResDto.setPurchase_delivery_sn(pmsStoreIn.getPurchase_delivery_sn());
				pmsStoreInHttpResDto.setPurchase_sn(pmsStoreIn.getPurchase_sn());
				pmsStoreInHttpResDto.setSku(pmsStoreIn.getSku());
				pmsStoreIn.setDealFlag(BaseConsts.ONE);
				if (return_msg.length() > 0) {
					pmsStoreIn.setFlag(BaseConsts.ONE);
					pmsStoreIn.setMsg(commonService.getMsg(return_msg.toString()));
					pmsStoreInHttpResDto.setFlag(BaseConsts.FLAG_NO);
					pmsStoreInHttpResDto.setMsg(return_msg.toString());
					sendReceiptSystemAlarm(pmsStoreIn, return_msg.toString());
				} else {
					pmsStoreIn.setFlag(BaseConsts.ZERO);
					pmsStoreInHttpResDto.setFlag(BaseConsts.FLAG_YES);
				}
				returnList.add(pmsStoreInHttpResDto);
			} else {
				return_msg.append("该供应商[provider_sn]无质押项目");
				pmsStoreIn.setFlag(BaseConsts.ONE);
				pmsStoreIn.setDealFlag(BaseConsts.ONE);
				pmsStoreInHttpResDto.setPurchase_delivery_sn(pmsStoreIn.getPurchase_delivery_sn());
				pmsStoreInHttpResDto.setPurchase_sn(pmsStoreIn.getPurchase_sn());
				pmsStoreInHttpResDto.setSku(pmsStoreIn.getSku());
				pmsStoreInHttpResDto.setFlag(BaseConsts.FLAG_NO);
				pmsStoreInHttpResDto.setMsg(return_msg.toString());
				returnList.add(pmsStoreInHttpResDto);
				sendReceiptSystemAlarm(pmsStoreIn, return_msg.toString());
			}
		}
		return returnList;
	}

	/**
	 * 封装当前的PMS入库单的明细数据
	 * 
	 * @param storeIns
	 * @return
	 */
	public List<PmsStoreResDto> convertStoreInResDtos(List<PmsStoreIn> storeIns, PmsDistributionSearchReqDto reqDto) {
		List<PmsStoreResDto> dtos = new ArrayList<PmsStoreResDto>();
		if (ListUtil.isEmpty(storeIns)) {
			return dtos;
		} else {
			for (PmsStoreIn storeIn : storeIns) {
				PmsStoreResDto dto = new PmsStoreResDto();
				if (null != storeIn) {
					BeanUtils.copyProperties(storeIn, dto);
					dto.setId(storeIn.getId());// 入库明细ID
					dto.setPmsSeriesId(storeIn.getPmsSeriesId());// 订单头ID
					dto.setPurchase_sn(storeIn.getPurchase_sn());// 采购单号
					dto.setCurrency_type(storeIn.getCurrency_type());// 币种
					// 通过value查询code
					String code = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
							storeIn.getCurrency_type());
					dto.setCurrencyName(
							ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, code + ""));
					dto.setSku(storeIn.getSku());// 商品sku
					dto.setStockin_num(storeIn.getStockin_num());// 入库数量
					dto.setPurchase_price(storeIn.getPurchase_price());// 入库价格
					dto.setPurchase_delivery_sn(storeIn.getPurchase_delivery_sn());// 送货单号
					dto.setStockin_time(storeIn.getStockin_time());// 入库时间
					dto.setCreateAt(storeIn.getCreateAt());// 创建时间
					dto.setUpdateAt(storeIn.getUpdateAt());// 修改时间
					dto.setType(reqDto.getType());// 类型
					dto.setDealFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_SERIES_STATE,
							storeIn.getDealFlag() + ""));
					dtos.add(dto);
				}
			}
		}
		return dtos;
	}

	public void dealFail(Integer id, String errorMsg, Integer type) {
		PmsStoreIn pmsStoreIn = pmsStoreInDao.queryEntityById(id);
		pmsStoreIn.setDealFlag(BaseConsts.TWO);
		pmsStoreIn.setDealMsg(commonService.getMsg(errorMsg));
		pmsStoreInDao.updateById(pmsStoreIn);
		sendSystemAlarm(pmsStoreIn, BaseConsts.ZERO, errorMsg);
	}

	public void dealSuccess(Integer id, String successMsg) {
		PmsStoreIn pmsStoreIn = pmsStoreInDao.queryEntityById(id);
		pmsStoreIn.setDealFlag(BaseConsts.THREE);
		pmsStoreIn.setDealMsg(successMsg);
		pmsStoreInDao.updateById(pmsStoreIn);
	}

	@IgnoreTransactionalMark
	public void sendSystemAlarm(int pmsStoreInId, Integer resultType, String errorMsg) {
		PmsStoreIn pmsStoreIn = pmsStoreInDao.queryEntityById(pmsStoreInId);
		if (null != pmsStoreIn) {
			sendSystemAlarm(pmsStoreIn, resultType, errorMsg);
		}
	}

	/**
	 * 发送系统消息
	 * 
	 * @param pmsStoreIn
	 * @param resultType
	 *            处理结果 0-失败 1-成功
	 */
	@IgnoreTransactionalMark
	public void sendSystemAlarm(PmsStoreIn pmsStoreIn, Integer resultType, String errorMsg) {
		try {
			// 根据结算对象查经营单位
			QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
			querySubjectReqDto.setPmsSupplierCode(pmsStoreIn.getAccount_sn());
			querySubjectReqDto.setSubjectType(BaseConsts.ONE);
			List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
			PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
			if (CollectionUtils.isNotEmpty(custs) && custs.size() == BaseConsts.ONE) {
				pMSSupplierBindReqDto.setPmsSupplierNo(pmsStoreIn.getProvider_sn());
				pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
			}
			PMSSupplierBind pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
			if (pmsSupplierBind == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS入库消息发送,PMS供应商编号【" + pmsStoreIn.getProvider_sn()
						+ "】,结算单编码【" + pmsStoreIn.getAccount_sn() + "】查询PMS供应商绑定数据为空");
			}
			BaseUser toUser = ServiceSupport.getOfficalUser(pmsSupplierBind.getProjectId()); // 商务主管
			String content = "";
			content = content + "单据编号:" + pmsStoreIn.getPurchase_sn() + "\n";
			content = content + "项目:" + cacheService.getProjectNameById(pmsSupplierBind.getProjectId()) + "\n";
			if (resultType.equals(BaseConsts.ONE)) {
				content = content + "信息:" + "入库成功" + "\n";
				;
			} else {
				content = content + "信息:" + "入库失败：" + (StringUtils.isBlank(errorMsg) ? "未知错误" : errorMsg) + "\n";
				;
			}
			content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
			content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

			String title = "SCFS系统提醒您,有新的【PMS融通铺货入库单】需要处理";
			if (resultType.equals(BaseConsts.ONE)) {
				msgContentService.addMsgContentByUserId(toUser.getId(), title, content, BaseConsts.ONE);
			} else {
				msgContentService.addMsgContentByUserId(toUser.getId(), title, content, BaseConsts.ONE);
				msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, content, BaseConsts.ONE);
			}
		} catch (Exception e) {
			LOGGER.error("PMS铺货入库单明细接口发送系统消息失败：", e);
		}
	}

	/**
	 * 入库单接收失败发送消息
	 * 
	 * @param pmsStoreIn
	 */
	@IgnoreTransactionalMark
	public void sendReceiptSystemAlarm(PmsStoreIn pmsStoreIn, String errorMsg) {
		try {
			String content = "";
			content = content + "单据编号:" + pmsStoreIn.getPurchase_sn() + "\n";
			content = content + "信息:" + "入库明细接收失败：" + (StringUtils.isBlank(errorMsg) ? "未知错误" : errorMsg);
			content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
			content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

			String title = "SCFS系统提醒您,有新的【PMS融通铺货入库单】需要处理";
			msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, content, BaseConsts.ONE);
		} catch (Exception e) {
			LOGGER.error("PMS铺货入库单明细接口发送系统消息失败：", e);
		}
	}

}
