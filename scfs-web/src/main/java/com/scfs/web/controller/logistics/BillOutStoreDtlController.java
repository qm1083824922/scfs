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
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlCustomsResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月20日.
 */
@Controller
public class BillOutStoreDtlController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillOutStoreDtlController.class);

	@Autowired
	private BillOutStoreDtlService billOutStoreDtlService;

	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult queryBillOutStoreDtlById(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		Result<BillOutStoreDtlResDto> result = new Result<BillOutStoreDtlResDto>();
		try {
			result = billOutStoreDtlService.queryBillOutStoreDtlById(billOutStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单单条明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单单条明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStoreDtlResDto> editBillOutStoreDtlById(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		Result<BillOutStoreDtlResDto> result = new Result<BillOutStoreDtlResDto>();
		try {
			result = billOutStoreDtlService.queryBillOutStoreDtlById(billOutStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单单条明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单单条明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreDtlResDto> queryBillOutStoreDtlsByBillOutStoreId(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();
		try {
			result = billOutStoreDtlService.queryBillOutStoreDtlsByBillOutStoreId(billOutStoreDtlSearchReqDto, false);
		} catch (BaseException e) {
			LOGGER.error("查询出库单明细列表异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单明细列表异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreDtlResDto> queryAllBillOutStoreDtlsByBillOutStoreId(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();
		try {
			result = billOutStoreDtlService.queryAllBillOutStoreDtlsByBillOutStoreId(billOutStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单明细列表异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单明细列表异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_OUT_STORE_DTL_FOR_PICK, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreDtlResDto> queryBillOutStorePickDtlsByBillOutStoreId(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();
		try {
			result = billOutStoreDtlService.queryBillOutStoreDtlsByBillOutStoreId(billOutStoreDtlSearchReqDto, true);
		} catch (BaseException e) {
			LOGGER.error("查询出库单明细列表(含拣货明细)异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单明细列表(含拣货明细)异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBillOutStoreDtls(@RequestBody BillOutStoreReqDto billOutStoreReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreDtlService.addBillOutStoreDtls(billOutStoreReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增出库单明细异常[{}]", JSONObject.toJSON(billOutStoreReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库单明细异常[{}]", JSONObject.toJSON(billOutStoreReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_BILL_OUT_STORE_DTL_BY_STL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBillOutStoreDtlsByStl(@RequestBody BillOutStoreReqDto billOutStoreReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreDtlService.addBillOutStoreDtlsByStl(billOutStoreReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增出库单明细异常[{}]", JSONObject.toJSON(billOutStoreReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库单明细异常[{}]", JSONObject.toJSON(billOutStoreReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBillOutStoreDtls(@RequestBody BillOutStoreReqDto billOutStoreReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreDtlService.updateBillOutStoreDtls(billOutStoreReqDto);
		} catch (BaseException e) {
			LOGGER.error("修改出库单明细异常[{}]", JSONObject.toJSON(billOutStoreReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改出库单明细异常[{}]", JSONObject.toJSON(billOutStoreReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BATCH_DELETE_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBillOutStoreDtlsByIds(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreDtlService.deleteBillOutStoreDtlsByIds(billOutStoreDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("删除出库单明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除出库单明细信息异常[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.AVAILABLE_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreDtlCustomsResDto> queryAvailableResultByCon(
			BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		PageResult<BillOutStoreDtlCustomsResDto> result = new PageResult<BillOutStoreDtlCustomsResDto>();
		try {
			result = billOutStoreDtlService.queryAvailableResultByCon(billOutStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询可报关的出库明细 异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询可报关的出库明细 异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 出库单打印
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.BATCH_PRINT_BILL_OUT_STORE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreDtlResDto> queryBillOutDtlPrint(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();
		try {
			return billOutStoreDtlService.queryBillOutDtlPrint(billOutStoreDtlSearchReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询出库单明细列表异常,入参：[{}],{},{}", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询出库单明细打印列表异常：[{}]", JSONObject.toJSON(billOutStoreDtlSearchReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}
}
