package com.scfs.service.api.pms;

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
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.api.pms.PmsStoreOutDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsHttpBillStoreOutResDto;
import com.scfs.domain.api.pms.entity.PmsSeries;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;
import com.scfs.domain.interf.dto.PmsStoreResDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.pay.entity.PmsStoreOut;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.interf.PMSSupplierBindService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PmsSyncBillOutStoreService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月03日			Administrator
 *
 * </pre>
 */
@Service
public class PmsSyncBillOutStoreService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncBillOutStoreService.class);

	@Autowired
	private InvokeConfig invokeConfig;
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private PmsSeriesService pmsSeriesService;
	@Autowired
	private PmsStoreOutDao pmsStoreOutDao;
	@Autowired
	private PMSSupplierBindService pmsSupplierBindService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BaseSubjectService baseSubjectService;

	/**
	 * PMS 同步出库单明细(铺货) 1: log日志的记录 2: 接口调用的记录 3:新增调用的数据明细 4:实现业务操作数量的扣除
	 * 
	 * @param req
	 * @return
	 */
	@IgnoreTransactionalMark
	public List<PmsHttpBillStoreOutResDto> doPmsStoreOut(PmsHttpReqDto req) {
		// pms日志
		List<PmsHttpBillStoreOutResDto> pmsHttpResDto = new ArrayList<PmsHttpBillStoreOutResDto>();
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		// log记录同步出库单明细的log日志记录
		this.queryLogByCon(data, invokeLog);
		// 接口调用状态描述 PMS铺货业务流水表数据的增加
		PmsSeries pmsSeries = pmsSeriesService.createPmsSeries(BaseConsts.TWO);
		try {
			List<PmsStoreOut> storeOut = JSON.parseArray(data, PmsStoreOut.class);
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					invokeLog.setReturnMsg(JSON.toJSONString("请求非法: 签名校验出错"));
					invokeLogService.invokeError(invokeLog);
					return pmsHttpResDto;
				}
			}
			try {
				pmsHttpResDto = this.checkStoreOut(storeOut, pmsHttpResDto, pmsSeries); // 接口前置检查
			} catch (BaseException e) {
				invokeLogService.invokeError(invokeLog);
				return pmsHttpResDto;
			}
			invokeLog.setReturnMsg(JSON.toJSONString(pmsHttpResDto));
			invokeLogService.invokeSuccess(invokeLog);
		} catch (Exception e) {
			LOGGER.error("[pms]pms 同步出库单明细接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			invokeLog.setReturnMsg(JSON.toJSONString(pmsHttpResDto));
			invokeLogService.invokeException(invokeLog, e);
		}
		return pmsHttpResDto;
	}

	/**
	 * log日志的记录
	 * 
	 * @param data
	 */
	private void queryLogByCon(String data, InvokeLog invokeLog) {
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);// 传输内容
		invokeLog.setCreateAt(new Date());// 创建时间
		invokeLog.setConsumer(BaseConsts.THREE);// 调用方
		invokeLog.setProvider(BaseConsts.ONE);// 提供方
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_STORE_OUT.getType()); // pms同步出库单明细
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);
	}

	/**
	 * 校验当前同步出库明细的数据
	 * 
	 * @param storeOut
	 */
	public List<PmsHttpBillStoreOutResDto> checkStoreOut(List<PmsStoreOut> storeOut,
			List<PmsHttpBillStoreOutResDto> pmsHttpResDto, PmsSeries pmsSeries) {

		if (storeOut == null || storeOut.size() < BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "同步出库单明细数据为空");
		}
		for (PmsStoreOut pmsStoreOut : storeOut) {
			PmsHttpBillStoreOutResDto billStoreOutResDto = new PmsHttpBillStoreOutResDto();
			PMSSupplierBind pmsSupplierBind = new PMSSupplierBind();
			billStoreOutResDto.setSku_id(pmsStoreOut.getSku_id());// 销售ID
			StringBuffer buffer = new StringBuffer();
			if (StringUtils.isEmpty(pmsStoreOut.getPurchase_sn())) {
				buffer.append("同步出库单采购单号[purchase_sn]为空;");
			}
			if (StringUtils.isEmpty(pmsStoreOut.getProvider_sn())) {
				buffer.append("同步出库单供应商编号[provider_sn]为空;");
			} else {
				try {
					// 根据结算对象查经营单位
					QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
					querySubjectReqDto.setPmsSupplierCode(pmsStoreOut.getAccount_sn());
					querySubjectReqDto.setSubjectType(BaseConsts.ONE);
					List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
					if (CollectionUtils.isEmpty(custs) || custs.size() > 1) {
						buffer.append("PMS结算对象供应商编码【" + pmsStoreOut.getAccount_sn() + "】不存在或者不唯一");
					} else {
						// 通过pms供应商查询对应的项目与供应商
						PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
						pMSSupplierBindReqDto.setPmsSupplierNo(pmsStoreOut.getProvider_sn());
						pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
						pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
					}
					if (pmsSupplierBind == null) {
						buffer.append("同步出库单供应商编号[provider_sn]为" + pmsStoreOut.getProvider_sn() + "的供应商为空");
					} else {
					}
				} catch (BaseException e) {
					buffer.append(e.getMsg());
				} catch (Exception e) {
					buffer.append(e.getMessage());
				}
			}
			if (StringUtils.isEmpty(pmsStoreOut.getSku())) {
				buffer.append("同步出库单商品[sku]为空;");
			}
			if (null == pmsStoreOut.getWms_out_stockin()) {
				buffer.append("同步出库单销售数量[wms_out_stockin]为空;");
			}
			if (null == pmsStoreOut.getPurchase_price()) {
				buffer.append("同步出库单销售价格[purchase_price]为空;");
			}
			if (StringUtils.isEmpty(pmsStoreOut.getCurrency_type())) {
				buffer.append("同步出库单币种[currency_type]为空;");
			}
			if (null == pmsStoreOut.getSales_date()) {
				buffer.append("同步出库单日期[sales_date]为空;");
			}
			if (null == pmsStoreOut.getSku_id()) {
				buffer.append("同步出库单销售ID[sku_id]为空;");
			}
			if (StringUtils.isEmpty(pmsStoreOut.getAccount_sn())) {
				buffer.append("同步出库单结算对象为空[account_sn]为空;");
			}
			if (buffer.length() > 0) {// 长度大于0,数据出现错误
				String msg = buffer.toString();
				pmsStoreOut.setflag(BaseConsts.ONE);// 数据失败
				pmsStoreOut.setMsg(commonService.getMsg(msg));// 数据返回的MSG
				billStoreOutResDto.setMsg(msg);
				billStoreOutResDto.setFlag(BaseConsts.FLAG_NO);
				sendReceiptSystemAlarm(pmsStoreOut, pmsStoreOut.getMsg());
			} else {
				pmsStoreOut.setflag(BaseConsts.ZERO);// 数据成功
				billStoreOutResDto.setFlag(BaseConsts.FLAG_YES);
			}
			try {
				pmsStoreOut.setCreate_at(new Date());// 当前时间
				pmsStoreOut.setPmsSeriesId(pmsSeries.getId());// PMS铺货业务流水表id
				pmsStoreOut.setDealFlag(BaseConsts.ONE);// 待处理状态
				pmsStoreOutDao.createPmsStoreOut(pmsStoreOut);
			} catch (Exception e) {
				StringBuffer buffer2 = new StringBuffer();
				buffer2.append(billStoreOutResDto.getMsg());
				buffer2.append("插入失败");
				billStoreOutResDto.setMsg(buffer2.toString());
				billStoreOutResDto.setFlag(BaseConsts.FLAG_NO);
				sendReceiptSystemAlarm(pmsStoreOut, pmsStoreOut.getMsg());
			}
			pmsHttpResDto.add(billStoreOutResDto);
		}
		return pmsHttpResDto;
	}

	/**
	 * 封装当前PMS 出库单的明细数据
	 * 
	 * @param storeOuts
	 * @return
	 */
	public List<PmsStoreResDto> convertStoreOutResDtos(List<PmsStoreOut> storeOuts,
			PmsDistributionSearchReqDto reqDto) {
		List<PmsStoreResDto> dtos = new ArrayList<PmsStoreResDto>();
		if (ListUtil.isEmpty(storeOuts)) {
			return dtos;
		} else {
			for (PmsStoreOut storeOut : storeOuts) {
				PmsStoreResDto dto = new PmsStoreResDto();
				if (null != storeOut) {
					BeanUtils.copyProperties(storeOut, dto);
					dto.setId(storeOut.getId());// 入库明细ID
					dto.setPmsSeriesId(storeOut.getPmsSeriesId());// 订单头ID
					dto.setPurchase_sn(storeOut.getPurchase_sn());// 采购单号
					dto.setCurrency_type(storeOut.getCurrency_type());// 币种
					// 通过value查询code
					String code = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
							storeOut.getCurrency_type());
					dto.setCurrencyName(
							ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, code + ""));
					dto.setSku(storeOut.getSku());// 商品sku
					dto.setWms_out_stockin(storeOut.getWms_out_stockin());// 销售数量
					dto.setPurchase_price(storeOut.getPurchase_price());// 销售价格
					dto.setSales_date(storeOut.getSales_date());// 销售日期
					dto.setCreateAt(storeOut.getCreate_at());// 创建时间
					dto.setUpdateAt(storeOut.getUpdateAt());// 修改时间
					dto.setFlag(storeOut.getflag());// 返回值 0 接受成功 1:接受失败
					dto.setMsg(storeOut.getMsg());// 返回信息
					dto.setSku_id(storeOut.getSku_id());// 销售id
					dto.setType(reqDto.getType());// 接口类型
					dto.setDealFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_SERIES_STATE,
							storeOut.getDealFlag() + ""));
					dtos.add(dto);
				}
			}
		}
		return dtos;
	}

	/**
	 * 接口调用后PMS铺货业务流水表的状态改变
	 * 
	 * @param id
	 * @param errorMsg
	 */
	public void dealFail(Integer id, String errorMsg, Integer type) {
		PmsStoreOut pmsStoreOut = pmsStoreOutDao.queryEntityById(id);
		pmsStoreOut.setDealFlag(BaseConsts.TWO);
		pmsStoreOut.setDealMsg(commonService.getMsg(errorMsg));
		pmsStoreOutDao.updateById(pmsStoreOut);
		sendSystemAlarm(pmsStoreOut, BaseConsts.ZERO, errorMsg);
	}

	/**
	 * 接口调用后PMS铺货业务流水表的状态改变
	 * 
	 * @param id
	 * @param successMsg
	 */
	public void dealSuccess(Integer id, String successMsg) {
		PmsStoreOut pmsStoreOut = pmsStoreOutDao.queryEntityById(id);
		pmsStoreOut.setDealFlag(BaseConsts.THREE);
		pmsStoreOut.setDealMsg(successMsg);
		pmsStoreOutDao.updateById(pmsStoreOut);
	}

	/**
	 * 修改当前PMS铺货出库单的数据
	 * 
	 * @param pmsStoreOut
	 */
	public int updateById(PmsStoreOut pmsStoreOut) {
		return pmsStoreOutDao.updateById(pmsStoreOut);
	}

	@IgnoreTransactionalMark
	public void sendSystemAlarm(int pmsStoreOutId, Integer resultType, String errorMsg) {
		PmsStoreOut pmsStoreOut = pmsStoreOutDao.queryEntityById(pmsStoreOutId);
		if (null != pmsStoreOut) {
			sendSystemAlarm(pmsStoreOut, resultType, errorMsg);
		}
	}

	/**
	 * 发送系统消息
	 * 
	 * @param pmsPayId
	 * @param resultType
	 *            处理结果 0-失败 1-成功
	 */
	@IgnoreTransactionalMark
	public void sendSystemAlarm(PmsStoreOut pmsStoreOut, Integer resultType, String errorMsg) {
		try {
			if (resultType.equals(BaseConsts.ZERO)) {
				// 根据结算对象查经营单位
				QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
				querySubjectReqDto.setPmsSupplierCode(pmsStoreOut.getAccount_sn());
				querySubjectReqDto.setSubjectType(BaseConsts.ONE);
				List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
				PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
				if (CollectionUtils.isNotEmpty(custs) && custs.size() == BaseConsts.ONE) {
					pMSSupplierBindReqDto.setPmsSupplierNo(pmsStoreOut.getProvider_sn());
					pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
				}
				PMSSupplierBind pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
				if (pmsSupplierBind == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"PMS入库消息发送,PMS供应商编号【" + pmsStoreOut.getProvider_sn() + "】,结算单编码【"
									+ pmsStoreOut.getAccount_sn() + "】查询PMS供应商绑定数据为空");
				}
				String content = "";
				content = content + "单据编号:" + pmsStoreOut.getPurchase_sn() + "\n";
				content = content + "项目:" + cacheService.getProjectNameById(pmsSupplierBind.getProjectId()) + "\n";
				content = content + "信息:" + "出库失败：" + (StringUtils.isBlank(errorMsg) ? "未知错误" : errorMsg) + "\n";

				content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date())
						+ "\n";
				content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

				String title = "SCFS系统提醒您,有新的【PMS融通铺货出库单】需要处理";
				msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, content, BaseConsts.ONE);
			}
		} catch (Exception e) {
			LOGGER.error("PMS铺货出库单明细接口发送系统消息失败：", e);
		}
	}

	/**
	 * 发送系统消息
	 * 
	 * @param pmsPayId
	 * @param resultType
	 *            处理结果 0-失败 1-成功
	 */
	@IgnoreTransactionalMark
	public void sendReceiptSystemAlarm(PmsStoreOut pmsStoreOut, String errorMsg) {
		try {
			String content = "";
			content = content + "单据编号:" + pmsStoreOut.getPurchase_sn() + "\n";
			content = content + "信息:" + "出库明细接收失败：" + (StringUtils.isBlank(errorMsg) ? "未知错误" : errorMsg) + "\n";

			content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
			content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

			String title = "SCFS系统提醒您,有新的【PMS融通铺货出库单】需要处理";
			msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, content, BaseConsts.ONE);
		} catch (Exception e) {
			LOGGER.error("PMS铺货出库单明细接口发送系统消息失败：", e);
		}
	}
}
