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
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreDtlResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.BillInStoreDtlService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月18日.
 */
@Controller
public class BillInStoreDtlController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillInStoreDtlController.class);

	@Autowired
	private BillInStoreDtlService billInStoreDtlService;

	@RequestMapping(value = BusUrlConsts.ADD_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBillInStoreDtls(@RequestBody BillInStoreSearchReqDto billInStoreDtlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billInStoreDtlService.addBillInStoreDtls(billInStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增入库单明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增入库单明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBillInStoreDtls(@RequestBody BillInStore billInStore) {
		BaseResult result = new BaseResult();
		try {
			billInStoreDtlService.updateBillInStoreDtls(billInStore);
		} catch (BaseException e) {
			LOGGER.error("更新入库单明细信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新入库单明细信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillInStoreDtlResDto> detailBillInStoreDtlById(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		Result<BillInStoreDtlResDto> result = new Result<BillInStoreDtlResDto>();
		try {
			result = billInStoreDtlService.queryBillInStoreDtlById(billInStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询入库单单条明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单单条明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillInStoreDtlResDto> editBillInStoreDtlById(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		Result<BillInStoreDtlResDto> result = new Result<BillInStoreDtlResDto>();
		try {
			result = billInStoreDtlService.queryBillInStoreDtlById(billInStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询入库单单条明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单单条明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillInStoreDtlResDto> queryAllBillInStoreDtlsByBillInStoreId(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();
		try {
			result = billInStoreDtlService.queryAllBillInStoreDtlsByBillInStoreId(billInStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询入库单明细列表异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单明细列表异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillInStoreDtlResDto> queryBillInStoreDtlsByBillInStoreId(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();
		try {
			result = billInStoreDtlService.queryBillInStoreDtlsByBillInStoreId(billInStoreDtlSearchReqDto, false);
		} catch (BaseException e) {
			LOGGER.error("查询入库单明细列表异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单明细列表异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_IN_STORE_DTL_FOR_TALLY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillInStoreDtlResDto> queryBillInStoreTallyDtlsByBillInStoreId(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();
		try {
			result = billInStoreDtlService.queryBillInStoreDtlsByBillInStoreId(billInStoreDtlSearchReqDto, true);
		} catch (BaseException e) {
			LOGGER.error("查询入库单明细列表(含理货明细)异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单明细列表(含理货明细)异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BATCH_DETAIL_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillInStoreDtlResDto> queryBillInStoreDtlByIds(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();
		try {
			result = billInStoreDtlService.queryBillInStoreDtlByIds(billInStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询指定ID集合入库单明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询指定ID集合入库单明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BATCH_DELETE_BILL_IN_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBillInStoreDtlsByIds(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billInStoreDtlService.deleteBillInStoreDtlsByIds(billInStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("删除入库单明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除入库单明细信息异常[{}]", JSONObject.toJSON(billInStoreDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
