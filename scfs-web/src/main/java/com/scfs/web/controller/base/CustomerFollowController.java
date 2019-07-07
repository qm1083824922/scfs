package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.CustomerMaintainReqDto;
import com.scfs.domain.base.dto.resp.CustomerFollowResDto;
import com.scfs.domain.base.entity.CustomerFollow;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.customer.CustomerFollowService;

/**
 * <pre>
 *   客户跟进
 *  File: CustomerFollowController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月27日				Administrator
 *
 * </pre>
 */
@Controller
public class CustomerFollowController {
	@Autowired
	private CustomerFollowService customerFollowService;

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerFollowController.class);

	@RequestMapping(value = BusUrlConsts.ADD_CUSTOMER_FOLLOW, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createCustomerFollow(CustomerFollow customerFollow) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = customerFollowService.insertCustomerFollow(customerFollow);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建客户跟进信息异常[{}]", JSONObject.toJSON(customerFollow), e);
			br.setSuccess(false);
			br.setMsg("新建客户跟进失败，请重试");
		}
		return br;
	}

	/**
	 * 修改信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_CUSTOMER_FOLLOW, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateCustomerFollow(CustomerFollow customerFollow) {
		BaseResult result = new BaseResult();
		try {
			result = customerFollowService.updateCustomerFollow(customerFollow);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改客户跟进信息失败[{}]", JSONObject.toJSON(customerFollow), e);
			result.setMsg("修改客户跟进信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑客户跟进信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_CUSTOMER_FOLLOW, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomerFollowResDto> editCustomerFollow(CustomerFollow customerFollow) {
		Result<CustomerFollowResDto> result = new Result<CustomerFollowResDto>();
		try {
			result = customerFollowService.queryCustomerFollowById(customerFollow);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑客户跟进信息失败[{}]", JSONObject.toJSON(customerFollow), e);
			result.setMsg("编辑客户跟进信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_CUSTOMER_FOLLOW, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAllCustomerFollow(CustomerFollow customerFollow) {
		BaseResult result = new BaseResult();
		try {
			result = customerFollowService.deleteCustomerFollow(customerFollow);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除客户跟进信息失败[{}]", JSONObject.toJSON(customerFollow), e);
			result.setMsg("删除客户跟进信息异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_ALL_CUSTOMER_FOLLOW, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteCustomerFollow(CustomerMaintainReqDto customerReq) {
		BaseResult result = new BaseResult();
		try {
			result = customerFollowService.deleteAllCustomerFollow(customerReq);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除客户跟进信息失败[{}]", JSONObject.toJSON(customerReq), e);
			result.setMsg("批量删除客户跟进信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_CUSTOMER_FOLLOW, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CustomerFollowResDto> queryCustomerFollow(CustomerFollow reqDto) {
		PageResult<CustomerFollowResDto> result = new PageResult<CustomerFollowResDto>();
		try {
			result = customerFollowService.queryCustomerFollowResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询铺货商品信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}
}
