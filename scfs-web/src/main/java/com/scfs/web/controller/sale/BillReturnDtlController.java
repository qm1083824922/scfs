package com.scfs.web.controller.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliveryDtlSearchReqDto;
import com.scfs.domain.sale.dto.req.BillDeliveryReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.service.sale.BillDeliveryDtlService;

/**
 * Created by Administrator on 2017年3月22日.
 */
@Controller
public class BillReturnDtlController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillReturnDtlController.class);
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_RETURN_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillDeliveryDtlResDto> queryBillDeliveryDtlsByBillDeliveryId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		PageResult<BillDeliveryDtlResDto> result = new PageResult<BillDeliveryDtlResDto>();
		try {
			result = billDeliveryDtlService.queryBillDeliveryDtlsByBillDeliveryId(billDeliveryDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_BILL_RETURN_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillDeliveryDtlResDto> queryAllBillDeliveryDtlsByBillDeliveryId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		PageResult<BillDeliveryDtlResDto> result = new PageResult<BillDeliveryDtlResDto>();
		try {
			result = billDeliveryDtlService.queryAllBillDeliveryDtlsByBillDeliveryId(billDeliveryDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_BILL_RETURN_DTL, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDeliveryDtlResDto> queryBillDeliveryDtlById(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		Result<BillDeliveryDtlResDto> result = new Result<BillDeliveryDtlResDto>();
		try {
			result = billDeliveryDtlService.queryBillDeliveryDtlById(billDeliveryDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询指定ID销售退货单明细信息异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询指定ID销售退货单明细信息异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}

		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_BILL_RETURN_DTL_BY_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBillDeliveryDtlsByBillOutStore(@RequestBody BillDeliveryReqDto billDeliveryReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryDtlService.addBillDeliveryDtlsByBillOutStore(billDeliveryReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_BILL_RETURN_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBillDeliveryDtls(@RequestBody BillDeliveryReqDto billDeliveryReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryDtlService.updateBillDeliveryDtls(billDeliveryReqDto);
		} catch (BaseException e) {
			LOGGER.error("更新销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_BILL_RETURN_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBillDeliveryDtlsByIds(BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		BaseResult result = new Result<BillDeliveryResDto>();
		try {
			billDeliveryDtlService.deleteBillDeliveryDtlsByIds(billDeliveryDtlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("批量删除销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除销售退货单明细异常[{}]", JSONObject.toJSON(billDeliveryDtlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 下载销售退货单明细excel模板(TODO 待定)
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_BILL_RETURN_DTL_TEMPLATE, method = RequestMethod.GET)
	public String downloadBillDeliveryDtlTemplate() {
		return "template/sale/billDelivery/billDeliveryDtl_template";
	}

	/**
	 * 导入销售退货单明细excel(TODO 待定)
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_BILL_RETURN_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importBillDeliveryDtlExcel(BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto,
			MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryDtlService.importBillDeliveryDtlExcel(billDeliveryDtlSearchReqDto, file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}
}
