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
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.logistics.dto.req.BillOutStorePickDtlSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStorePickDtlResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月20日.
 */
@Controller
public class BillOutStorePickDtlController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillOutStorePickDtlController.class);
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_BILL_OUT_STORE_PICK_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStorePickDtlResDto> queryBillOutStorePickDtlsByBillOutStoreId(
			BillOutStorePickDtlSearchReqDto billOutStorePickDtlSearchReqDto) {
		PageResult<BillOutStorePickDtlResDto> result = new PageResult<BillOutStorePickDtlResDto>();
		try {
			result = billOutStorePickDtlService
					.queryAllBillOutStorePickDtlsByBillOutStoreId(billOutStorePickDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单拣货列表异常[{}]", JSONObject.toJSON(billOutStorePickDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单拣货明细列表异常[{}]", JSONObject.toJSON(billOutStorePickDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_BILL_OUT_STORE_PICK_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBillOutStorePickDtls(@RequestBody BillOutStoreDtl billOutStoreDtl) {
		BaseResult result = new BaseResult();
		try {
			billOutStorePickDtlService.addBillOutStorePickDtls(billOutStoreDtl);
		} catch (BaseException e) {
			LOGGER.error("新增出库单拣货明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtl), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库单拣货明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtl), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_BILL_OUT_STORE_PICK_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteById(BillOutStorePickDtl billOutStorePickDtl) {
		BaseResult result = new BaseResult();
		try {
			billOutStorePickDtlService.deleteById(billOutStorePickDtl);
		} catch (BaseException e) {
			LOGGER.error("根据拣货明细ID删除拣货明细异常[{}]", JSONObject.toJSON(billOutStorePickDtl), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("根据拣货明细ID删除拣货明细异常[{}]", JSONObject.toJSON(billOutStorePickDtl), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BATCH_DELETE_BILL_OUT_STORE_PICK_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteByBillOutStoreDtlId(BillOutStorePickDtlSearchReqDto billOutStorePickDtlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStorePickDtlService.deleteByBillOutStoreDtlId(billOutStorePickDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("根据明细ID删除拣货明细异常[{}]", JSONObject.toJSON(billOutStorePickDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("根据明细ID删除拣货明细异常[{}]", JSONObject.toJSON(billOutStorePickDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.AUTO_PICK_BILL_OUT_STORE_PICK_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult autoPick(BillOutStore billOutStore) {
		BaseResult result = new BaseResult();
		try {
			billOutStorePickDtlService.autoPick(billOutStore);
		} catch (BaseException e) {
			LOGGER.error("根据出库单ID自动补拣异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("根据出库单ID自动补拣异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
