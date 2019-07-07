package com.scfs.web.controller.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.service.common.RefreshDataService;
import com.scfs.service.common.RefreshPmsPayOrderService;
import com.scfs.service.common.RefreshPoolAssetDtlService;
import com.scfs.service.common.RefreshSaleInfoService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2017年2月28日.
 */
@Controller
public class RefreshDataController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AsyncExcelController.class);
	@Autowired
	private RefreshSaleInfoService refreshSaleInfoService;
	@Autowired
	private RefreshPoolAssetDtlService refreshPoolAssetDtlService;
	@Autowired
	private RefreshPmsPayOrderService refreshPmsPayOrderService;
	@Autowired
	private RefreshDataService refreshDataService;

	@RequestMapping(value = BusUrlConsts.REFRESH_SALE_PRICE, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshSalePrice(String billNos) {
		BaseResult result = new BaseResult();
		try {
			if (StringUtils.isNotBlank(billNos)) {
				refreshSaleInfoService.refreshSalePrice(billNos);
			}
		} catch (BaseException e) {
			LOGGER.error("重新刷新提货单销售价和利润单价异常[{}]", JSONObject.toJSON(billNos), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("重新刷新提货单销售价和利润单价异常[{}]", JSONObject.toJSON(billNos), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REFRESH_POOL_ASSET_DTL, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshPoolAssetDtl() {
		BaseResult result = new BaseResult();
		try {
			refreshPoolAssetDtlService.refreshPoolAssetDtl();
		} catch (BaseException e) {
			LOGGER.error("重新刷新资产明细异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("重新刷新资产明细异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REFRESH_PMS_PAY_ORDER, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshPmsPayOrder(String billNo) {
		BaseResult result = new BaseResult();
		try {
			if (StringUtils.isNotBlank(billNo)) {
				refreshPmsPayOrderService.refreshPmsPayOrder(billNo);
			}
		} catch (BaseException e) {
			LOGGER.error("刷新pms应收保理付款确认未生成的入库、销售、出库等数据异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新pms应收保理付款确认未生成的入库、销售、出库等数据异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REFRESH_FUND_USED, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshFundUsed(@RequestParam(value = "isVirtualReceipt") String isVirtualReceipt) {
		BaseResult result = new BaseResult();
		try {
			refreshPmsPayOrderService.refreshFundUsed(isVirtualReceipt);
		} catch (BaseException e) {
			LOGGER.error("刷新水单和应收关系的资金占用异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新水单和应收关系的资金占用异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REFRESH_PROJECT_POOL, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshProjectPool() {
		BaseResult result = new BaseResult();
		try {
			refreshPoolAssetDtlService.refreshProjectPool();
		} catch (BaseException e) {
			LOGGER.error("刷新融资池异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新融资池异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REFRESH_PROJECT_NO, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshProjectNo() {
		BaseResult result = new BaseResult();
		try {
			refreshDataService.refreshProjectNo();
		} catch (BaseException e) {
			LOGGER.error("刷新项目编号异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新项目编号异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
