package com.scfs.web.controller.logistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.domain.BaseResult;
import com.scfs.domain.logistics.dto.req.BillInStoreTallyDtlSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.common.exception.BaseException;
import com.scfs.service.logistics.BillInStoreTallyDtlService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月18日.
 */
@Controller
public class BillInStoreTallyDtlController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillInStoreTallyDtlController.class);

	@Autowired
	private BillInStoreTallyDtlService billInStoreTallyDtlService;

	@RequestMapping(value = BusUrlConsts.ADD_BILL_IN_STORE_TALLY_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBillInStoreTallyDtls(@RequestBody BillInStoreDtl billInStoreDtl) {
		BaseResult result = new BaseResult();
		try {
			billInStoreTallyDtlService.addBillInStoreTallyDtls(billInStoreDtl);
		} catch (BaseException e) {
			LOGGER.error("新增入库单理货明细信息异常[{}]", JSONObject.toJSON(billInStoreDtl), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增入库单理货明细信息异常[{}]", JSONObject.toJSON(billInStoreDtl), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_BILL_IN_STORE_TALLY_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteById(BillInStoreTallyDtl billInStoreTallyDtl) {
		BaseResult result = new BaseResult();
		try {
			billInStoreTallyDtlService.deleteById(billInStoreTallyDtl);
		} catch (BaseException e) {
			LOGGER.error("根据理货明细ID删除理货明细异常[{}]", JSONObject.toJSON(billInStoreTallyDtl), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("根据理货明细ID删除理货明细异常[{}]", JSONObject.toJSON(billInStoreTallyDtl), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BATCH_DELETE_BILL_IN_STORE_TALLY_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteByBillInStoreDtlIds(BillInStoreTallyDtlSearchReqDto billInStoreTallyDtlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billInStoreTallyDtlService.deleteByBillInStoreDtlIds(billInStoreTallyDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("根据明细ID删除理货明细异常[{}]", JSONObject.toJSON(billInStoreTallyDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("根据明细ID删除理货明细异常[{}]", JSONObject.toJSON(billInStoreTallyDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.AUTO_TALLY_BILL_IN_STORE_TALLY_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult autoTally(BillInStore billInStore) {
		BaseResult result = new BaseResult();
		try {
			billInStoreTallyDtlService.autoTally(billInStore);
		} catch (BaseException e) {
			LOGGER.error("根据入库单ID自动理货异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("根据入库单ID自动理货异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
