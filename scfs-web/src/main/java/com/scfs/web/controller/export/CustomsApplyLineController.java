package com.scfs.web.controller.export;

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
import com.scfs.domain.export.dto.req.CustomsApplyLineSearchReqDto;
import com.scfs.domain.export.dto.req.CustomsApplyReqDto;
import com.scfs.domain.export.dto.resp.CustomsApplyLineResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.service.export.CustomsApplyLineService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年12月6日.
 */
@Controller
public class CustomsApplyLineController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(CustomsApplyLineController.class);

	@Autowired
	private CustomsApplyLineService customsApplyLineService;

	@RequestMapping(value = BusUrlConsts.QUERY_CUSTOMS_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CustomsApplyLineResDto> queryCustomsApplyLinesByBillDeliveryId(
			CustomsApplyLineSearchReqDto customsApplyLineSearchReqDto) {
		PageResult<CustomsApplyLineResDto> result = new PageResult<CustomsApplyLineResDto>();
		try {
			result = customsApplyLineService.queryCustomsApplyLinesByCustomsApplyId(customsApplyLineSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增报关明细异常[{}]", JSONObject.toJSON(customsApplyLineSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增报关明细异常[{}]", JSONObject.toJSON(customsApplyLineSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_CUSTOMS_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomsApplyLineResDto> queryCustomsApplyLineById(
			CustomsApplyLineSearchReqDto customsApplyLineSearchReqDto) {
		Result<CustomsApplyLineResDto> result = new Result<CustomsApplyLineResDto>();
		try {
			result = customsApplyLineService.queryCustomsApplyLineById(customsApplyLineSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询指定ID报关明细信息异常[{}]", JSONObject.toJSON(customsApplyLineSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询指定ID报关明细信息异常[{}]", JSONObject.toJSON(customsApplyLineSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_CUSTOMS_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addCustomsApplyLines(@RequestBody CustomsApplyReqDto customsApplyReqDto) {
		BaseResult result = new BaseResult();
		try {
			customsApplyLineService.addCustomsApplyLines(customsApplyReqDto);
		} catch (BaseException e) {
			LOGGER.error("新增报关明细异常[{}]", JSONObject.toJSON(customsApplyReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增报关明细异常[{}]", JSONObject.toJSON(customsApplyReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_CUSTOMS_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateCustomsApplyLines(@RequestBody CustomsApplyReqDto customsApplyReqDto) {
		BaseResult result = new BaseResult();
		try {
			customsApplyLineService.updateCustomsApplyLines(customsApplyReqDto);
		} catch (BaseException e) {
			LOGGER.error("更新报关明细异常[{}]", JSONObject.toJSON(customsApplyReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新报关明细异常[{}]", JSONObject.toJSON(customsApplyReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_CUSTOMS_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteCustomsApplyLinesByIds(CustomsApplyLineSearchReqDto customsApplyLineSearchReqDto) {
		BaseResult result = new Result<BillDeliveryResDto>();
		try {
			customsApplyLineService.deleteCustomsApplyLinesByIds(customsApplyLineSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("批量删除报关明细异常[{}]", JSONObject.toJSON(customsApplyLineSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除报关明细异常[{}]", JSONObject.toJSON(customsApplyLineSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
