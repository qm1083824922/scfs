package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.interf.PMSSupplierBindDao;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsHttpResDto;
import com.scfs.domain.api.pms.entity.PmsPayOrder;
import com.scfs.domain.api.pms.entity.PmsPayOrderDtl;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.interf.PmsPayOrderTitleService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;

/**
 * <pre>
 * 
 *  File: PmsSynPayService.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2016年12月15日                                    Administrator
 *
 * </pre>
 */
@Service
public class PmsSyncPayService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncPayService.class);

	@Autowired
	InvokeConfig invokeConfig;

	@Autowired
	InvokeLogService invokeLogService;

	@Autowired
	PmsPayOrderTitleService pmsPayOrderService;

	@Autowired
	PMSSupplierBindDao pmsSupplierBindDao;

	@Autowired
	PmsPayOrderTitleDao pmsPayOrderTitleDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	PmsStoreOutService pmsStoreOutService;

	@Autowired
	PurchaseOrderService purchaseOrderService;

	@IgnoreTransactionalMark
	public PmsHttpResDto doPmsSyncPay(PmsHttpReqDto req) {
		if (req == null) {
			return null;
		}

		PmsHttpResDto res = new PmsHttpResDto();
		res.setFlag(BaseConsts.FLAG_YES);
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.THREE);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_ORDER.getType()); // pms同步请款接口
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);

		try {
			PmsPayOrder pmsPayOrder = JSON.parseObject(data, PmsPayOrder.class);
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					res.setMsg("请求非法: 签名校验出错");
					invokeLog.setReturnMsg(JSON.toJSONString(res));
					invokeLogService.invokeError(invokeLog);
					return res;
				}
			}
			try {
				checkData(pmsPayOrder); // 接口前置检查
			} catch (BaseException e) {
				res.setMsg(e.getMsg());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
				return res;
			}
			res.setPayNo(pmsPayOrder.getOrderTitle().getPayNo());
			invokeLog.setBillNo(pmsPayOrder.getOrderTitle().getPayNo());
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeSuccess(invokeLog);
			pmsPayOrderService.createPmsPayOrder(pmsPayOrder);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步请款接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			res.setMsg("[pms]pms同步请款接口请求失败");
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeException(invokeLog, e);
		}
		return res;
	}

	private void checkData(PmsPayOrder pmsPayOrder) {
		if (pmsPayOrder == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		PmsPayOrderTitle orderTitle = pmsPayOrder.getOrderTitle();
		List<PmsPayOrderDtl> orderDtls = pmsPayOrder.getOrderDtls();
		if (orderTitle == null || orderDtls == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		if (StringUtils.isEmpty(orderTitle.getInnerPayDate())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 预计内部打款日期不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getPayCurrency())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款币种不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getPayDate())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 请款日期不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getPayMoney())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款总金额不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getPayNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款编号不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getVendorNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 供应商编号不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getCorporation_code())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款法人代码不能为空");
		}
		if (StringUtils.isEmpty(orderTitle.getCorporation_name())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款法人编号不能为空");
		}
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (PmsPayOrderDtl item : orderDtls) {
			if (StringUtils.isEmpty(item.getGoodsName())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 商品名称不能为空");
			}
			if (StringUtils.isEmpty(item.getGoodsNo())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 商品编号不能为空");
			}
			if (StringUtils.isEmpty(item.getInPrice())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 单价成本不能为空");
			}
			if (StringUtils.isEmpty(item.getInQty())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 入库数量不能为空");
			}
			if (StringUtils.isEmpty(item.getPoNo())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 采购订单编号不能为空");
			}
			totalAmount = DecimalUtil.formatScale2(
					DecimalUtil.add(totalAmount, DecimalUtil.multiply(item.getInPrice(), item.getInQty())));
		}
		if (!DecimalUtil.eq(totalAmount, DecimalUtil.format(orderTitle.getPayMoney()))) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款总金额与明细不匹配");
		}
		PMSSupplierBindReqDto reqDto = new PMSSupplierBindReqDto();
		reqDto.setPmsSupplierNo(orderTitle.getVendorNo());
		reqDto.setStatus(BaseConsts.TWO);
		reqDto.setBizType(BaseConsts.TWO);// 应收保理
		List<PMSSupplierBind> pmsSupplierBinds = pmsSupplierBindDao.queryByProjectStatus(reqDto);
		if (CollectionUtils.isEmpty(pmsSupplierBinds)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 供应商编号[" + orderTitle.getVendorNo() + "]未开通应收结算");
		}
		orderTitle.setSupplierNo(pmsSupplierBinds.get(0).getSupplierNo());
		Integer currencyType = PmsPayOrderTitle.convertCurrencyType(orderTitle.getPayCurrency());
		if (currencyType == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"请求参数有误: SCFS系统暂不支持币种" + orderTitle.getPayCurrency() + "，可以选择的币种有人民币，美元，港元");
		}
		orderTitle.setCurrencyType(currencyType);
		Integer projectId = pmsSupplierBinds.get(0).getProjectId();
		BaseProject baseProject = cacheService.getProjectById(projectId);
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误： 找不到绑定的项目");
		}
		BaseSubject baseSubject = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
		if (baseSubject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误： 找不到项目下的经营单位");
		}
		/**
		 * if (!
		 * orderTitle.getCorporation_code().equals(baseSubject.getBusinessUnitCode()))
		 * { throw new BaseException(ExcMsgEnum.ERROR_GENERAL , "请求参数有误：法人代码【" +
		 * orderTitle.getCorporation_code() + "】有误"); }
		 **/
		PmsPoTitleSearchReqDto pmsPoTitleSearchReqDto = new PmsPoTitleSearchReqDto();
		pmsPoTitleSearchReqDto.setPayNo(orderTitle.getPayNo());
		List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByPayNo(pmsPoTitleSearchReqDto);
		if (!CollectionUtils.isEmpty(pmsPayOrderTitles)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误:付款单号[" + orderTitle.getPayNo() + "]已经存在");
		}
	}

}
