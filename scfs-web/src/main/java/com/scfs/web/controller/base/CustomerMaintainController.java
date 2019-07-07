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
import com.scfs.domain.base.dto.resp.CustomerMaintainResDto;
import com.scfs.domain.base.entity.CustomerMaintain;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.customer.CustomerMaintainService;

/**
 * <pre>
 *   客户维护
 *  File: CustomerMaintainController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月26日				Administrator
 *
 * </pre>
 */
@Controller
public class CustomerMaintainController {
	@Autowired
	private CustomerMaintainService customerMaintainService;

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerMaintainController.class);

	/**
	 * 新建
	 * 
	 * @param customerMaintain
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_CUSTOMER_MAINTAIN, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createCustomerMaintain(CustomerMaintain customerMaintain) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = customerMaintainService.insertCustomerMaintain(customerMaintain);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建客户维护信息异常[{}]", JSONObject.toJSON(customerMaintain), e);
			br.setSuccess(false);
			br.setMsg("新建客户维护失败，请重试");
		}
		return br;
	}

	/**
	 * 修改信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_CUSTOMER_MAINTAIN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateCustomerMaintain(CustomerMaintain customerMaintain) {
		BaseResult result = new BaseResult();
		try {
			result = customerMaintainService.updateCustomerMaintain(customerMaintain);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改客户维护信息失败[{}]", JSONObject.toJSON(customerMaintain), e);
			result.setMsg("修改客户维护信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑客户维护信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_CUSTOMER_MAINTAIN, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomerMaintainResDto> editCustomerMaintain(CustomerMaintain customerMaintain) {
		Result<CustomerMaintainResDto> result = new Result<CustomerMaintainResDto>();
		try {
			result = customerMaintainService.queryCustomerMaintainById(customerMaintain);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑客户维护信息失败[{}]", JSONObject.toJSON(customerMaintain), e);
			result.setMsg("编辑客户维护信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 浏览客户维护信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_CUSTOMER_MAINTAIN, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomerMaintainResDto> detailCustomerMaintain(CustomerMaintain customerMaintain) {
		Result<CustomerMaintainResDto> result = new Result<CustomerMaintainResDto>();
		try {
			result = customerMaintainService.queryCustomerMaintainById(customerMaintain);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览客户维护信息失败[{}]", JSONObject.toJSON(customerMaintain), e);
			result.setMsg("浏览客户维护信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_CUSTOMER_MAINTAIN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteCustomerMaintain(CustomerMaintain customerMaintain) {
		BaseResult result = new BaseResult();
		try {
			result = customerMaintainService.deleteCustomerMaintain(customerMaintain);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除客户维护信息失败[{}]", JSONObject.toJSON(customerMaintain), e);
			result.setMsg("删除客户维护信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_CUSTOMER_MAINTAIN, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CustomerMaintainResDto> queryCustomerMaintain(CustomerMaintainReqDto reqDto) {
		PageResult<CustomerMaintainResDto> result = new PageResult<CustomerMaintainResDto>();
		try {
			result = customerMaintainService.queryCustomerMaintainResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询客户维护信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}
}
