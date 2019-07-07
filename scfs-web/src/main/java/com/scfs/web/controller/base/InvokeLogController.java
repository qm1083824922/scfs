package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.InvokeLogSearchReqDto;
import com.scfs.domain.common.dto.resp.InvokeLogResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.InvokeLogService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年11月29日.
 */
@Controller
public class InvokeLogController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvokeLogController.class);
	@Autowired
	private InvokeLogService invokeLogService;

	@RequestMapping(value = BaseUrlConsts.QUERYINVOKELOG, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvokeLogResDto> queryInvokeLogResultsByCon(InvokeLogSearchReqDto invokeLogSearchReqDto) {
		PageResult<InvokeLogResDto> result = new PageResult<InvokeLogResDto>();
		try {
			result = invokeLogService.queryInvokeLogResultsByCon(invokeLogSearchReqDto);
		} catch (Exception e) {
			LOGGER.error("查询接口日志列表异常[{}]", JSONObject.toJSON(invokeLogSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.REINVOKEINVOKELOG, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult reInvoke(InvokeLogSearchReqDto invokeLogSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			invokeLogService.reInvoke(invokeLogSearchReqDto, false);
		} catch (BaseException e) {
			LOGGER.error("重新调用接口异常[{}]", JSONObject.toJSON(invokeLogSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("重新调用接口异常[{}]", JSONObject.toJSON(invokeLogSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.REDEALINVOKELOG, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult reDeal(InvokeLogSearchReqDto invokeLogSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			invokeLogService.reDeal(invokeLogSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("重新处理异常[{}]", JSONObject.toJSON(invokeLogSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("重新处理异常[{}]", JSONObject.toJSON(invokeLogSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

}
