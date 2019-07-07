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
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.subject.dto.req.AddAddressDto;
import com.scfs.domain.base.subject.dto.req.QueryAddressReqDto;
import com.scfs.domain.base.subject.dto.resp.QueryAddressResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.subject.BaseAddressService;
import com.scfs.common.exception.BaseException;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: BaseAddressController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Controller
public class BaseAddressController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseAddressController.class);

	@Autowired
	private BaseAddressService baseAddressService;

	@RequestMapping(value = BaseUrlConsts.ADDSUBJECTADDRESS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBaseAddress(AddAddressDto addAddressDto) {
		BaseResult br = new BaseResult();
		try {
			baseAddressService.addBaseAddress(addAddressDto);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addAddressDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYADDRESSBYSUBJECTID, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<QueryAddressResDto> queryAddressBySubjectId(QueryAddressReqDto queryAddressReqDto) {
		return baseAddressService.queryAddressBySubjectId(queryAddressReqDto);
	}

	@RequestMapping(value = BaseUrlConsts.BATCHINVALIDSUBJECTADDRESS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult invalidSubjectAddress(QueryAddressReqDto queryAddressReqDto) {
		BaseResult br = new BaseResult();
		try {
			baseAddressService.invalidBaseAddressByIds(queryAddressReqDto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("作废信息异常[{}]", JSONObject.toJSON(queryAddressReqDto), e);
			br.setSuccess(false);
			br.setMsg("作废失败，请重试");
		}
		return br;
	}

}
