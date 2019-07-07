package com.scfs.web.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.common.dto.req.AsyncExcelReqDto;
import com.scfs.domain.common.dto.resp.AsyncExcelResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 
 *  File: BaseAccountController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Controller
public class AsyncExcelController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AsyncExcelController.class);

	@Autowired
	private AsyncExcelService asyncExcelService;

	@RequestMapping(value = BaseUrlConsts.QUERYEXCELLIST, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AsyncExcelResDto> query(AsyncExcelReqDto asyncExcelReqDto) {
		PageResult<AsyncExcelResDto> result = new PageResult<AsyncExcelResDto>();
		try {
			result = asyncExcelService.queryAsyncExcelList(asyncExcelReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询信息异常[{}]", JSONObject.toJSON(asyncExcelReqDto), e);
			result.setSuccess(false);
			result.setMsg("查询失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DOWNLOADEXCELLIST, method = RequestMethod.GET)
	@ResponseBody
	public void downloadExcel(Integer fileId, HttpServletResponse response) {
		try {
			asyncExcelService.downExcelFileById(fileId, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

}
