package com.scfs.web.controller.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.AuditFlowReqDto;
import com.scfs.domain.base.dto.resp.AuditFlowResDto;
import com.scfs.domain.base.entity.AuditFlow;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;

/**
 * Created by Administrator on 2017年7月22日.
 */
@Controller
public class AuditFlowController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuditFlowController.class);
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 获取信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowResDto> queryAuditFlowResult(AuditFlowReqDto reqDto) {
		PageResult<AuditFlowResDto> result = new PageResult<AuditFlowResDto>();
		try {
			result = auditFlowService.queryAuditFlowResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取审核流信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取审核流信息失败[{}]", null, e);
			result.setMsg("获取审核流信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createAuditFlow(AuditFlow auditFlow) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = auditFlowService.createAuditFlow(auditFlow);
			br.setItems(id);
		} catch (BaseException e) {
			LOGGER.error("添加审核流信息异常[{}]", JSONObject.toJSON(auditFlow), e);
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加审核流信息异常[{}]", JSONObject.toJSON(auditFlow), e);
			br.setMsg("添加审核流失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public Result<AuditFlowResDto> detailPayOrderById(AuditFlow auditFlow) {
		Result<AuditFlowResDto> result = new Result<AuditFlowResDto>();
		try {
			result = auditFlowService.detailAuditFlowById(auditFlow);
		} catch (BaseException e) {
			LOGGER.error("浏览审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg("浏览审核流信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public Result<AuditFlowResDto> detailAuditFlowById(AuditFlow auditFlow) {
		Result<AuditFlowResDto> result = new Result<AuditFlowResDto>();
		try {
			result = auditFlowService.detailAuditFlowById(auditFlow);
		} catch (BaseException e) {
			LOGGER.error("编辑审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg("编辑审核流信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 修改信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateAuditFlow(AuditFlow auditFlow) {
		BaseResult result = new BaseResult();
		try {
			result = auditFlowService.updateAuditFlow(auditFlow);
		} catch (BaseException e) {
			LOGGER.error("更新审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg("更新审核流信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAuditFlow(AuditFlow auditFlow) {
		BaseResult result = new BaseResult();
		try {
			result = auditFlowService.deleteAuditFlow(auditFlow);
		} catch (BaseException e) {
			LOGGER.error("删除审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
			result.setMsg("删除审核流信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询付款审核流
	 * 
	 * @param auditFlow
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.PAY_AUDIT_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public List<AuditFlow> queryAuditFlow4Pay(AuditFlow auditFlow) {
		List<AuditFlow> list = Lists.newArrayList();
		try {
			list = auditFlowService.queryAuditFlow4Pay();
		} catch (BaseException e) {
			LOGGER.error("获取付款单审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
		} catch (Exception e) {
			LOGGER.error("获取付款单审核流信息失败[{}]", JSONObject.toJSON(auditFlow), e);
		}
		return list;
	}

}
