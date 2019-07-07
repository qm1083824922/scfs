package com.scfs.web.controller.pay;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.model.MergePayOrderAuditInfo;
import com.scfs.domain.pay.dto.req.MergePayOrderSearchReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.MergePayOrder;
import com.scfs.domain.pay.entity.MergePayOrderDetail;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.MergePayAuditService;
import com.scfs.service.pay.MergePayOrderService;
import com.scfs.service.pay.PayService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: MergePayOrderController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */

@Controller
public class MergePayOrderController {
	@Autowired
	MergePayOrderService mergePayOrderService;
	@Autowired
	PayService payService;
	@Autowired
	MergePayAuditService mergePayAuditService;

	private final static Logger LOGGER = LoggerFactory.getLogger(MergePayOrderController.class);

	/**
	 * 新建
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createPayOrder(@RequestBody MergePayOrderDetail mergePayOrderDetail) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = mergePayOrderService.createMergePayOrder(mergePayOrderDetail);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(mergePayOrderDetail), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览付款信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<MergePayOrderResDto> detailPayOrderById(Integer id) {
		Result<MergePayOrderResDto> result = new Result<MergePayOrderResDto>();
		try {
			MergePayOrderResDto mergePayOrderResDto = mergePayOrderService.detailPayOrderById(id);
			result.setItems(mergePayOrderResDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览付款失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑付款信息
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<MergePayOrderResDto> editPayOrderById(Integer id) {
		Result<MergePayOrderResDto> result = new Result<MergePayOrderResDto>();
		try {
			MergePayOrderResDto mergePayOrderResDto = mergePayOrderService.editPayOrderById(id);
			result.setItems(mergePayOrderResDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付款失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新付款信息
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayOrderById(MergePayOrder payOrder) {
		BaseResult result = new BaseResult();
		try {
			mergePayOrderService.updatePayOrderById(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新付款失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("更新付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除付款
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayOrderById(Integer id) {
		BaseResult result = new BaseResult();
		try {
			mergePayOrderService.deleteById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付款失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("删除付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPayOrderById(Integer id) {
		BaseResult result = new BaseResult();
		try {
			mergePayOrderService.submitById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(id), e);
			result.setSuccess(false);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MergePayOrderResDto> queryPayOrderResultsByCon(MergePayOrderSearchReqDto req) {
		PageResult<MergePayOrderResDto> pageResult = new PageResult<MergePayOrderResDto>();
		try {
			pageResult = mergePayOrderService.queryResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询待合并的付款列表
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVIDE_MERGE_PAY_ORDER_ALL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderResDto> queryAllResultsByCon(PayOrderSearchReqDto req) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		try {
			req.setState(BaseConsts.ZERO); // 待提交
			req.setCanMerge(BaseConsts.ONE); // 可以合并
			List<PayOrderResDto> payOrderResDtos = payService.queryPayOrderResultsExcel(req);
			pageResult.setItems(payOrderResDtos);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 根据合并付款id查询可合并的付款列表
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVIDE_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderResDto> dividPayOrderByMergeId(MergePayOrderSearchReqDto mergePayOrderSearchReqDto) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		try {
			pageResult = mergePayOrderService.dividPayOrderByMergeId(mergePayOrderSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(mergePayOrderSearchReqDto), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询合并付款打印信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MERGE_PAY_ORDER_PRINT, method = RequestMethod.POST)
	@ResponseBody
	public Result<MergePayOrderAuditInfo> queryMergePayOrderPrint(Integer id) {
		Result<MergePayOrderAuditInfo> result = new Result<MergePayOrderAuditInfo>();
		try {
			MergePayOrderAuditInfo mergePayOrderAuditInfo = mergePayAuditService.queryPayAuditInfo(id);
			result.setItems(mergePayOrderAuditInfo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款打印信息失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("查询付款打印信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 合并付款单预处理信息
	 */
	@RequestMapping(value = BusUrlConsts.PRINT_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> prePrint(Integer id) {
		Result<String> result = new Result<String>();
		try {
			String unionPrintIdentifier = mergePayOrderService.prePrint(id);
			result.setItems(unionPrintIdentifier);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量打印预处理失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("批量打印预处理失败异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 供应商批量提交
	 * 
	 * @param payOrderBatchConfirmReq
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.BATCH_SUBMIT_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult batchMergePaySubmitById(MergePayOrderSearchReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			mergePayOrderService.batchMergePaySubmitById(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量确认失败[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 供应商提交
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.CONFIG_SUBMIT_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult configSubmitById(Integer id) {
		BaseResult result = new BaseResult();
		try {
			if (!ServiceSupport.isAllowPerm(BusUrlConsts.CONFIG_SUBMIT_MERGE_PAY_ORDER)) {// 判断用户是否拥有权限
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "用户无权限!");
			}
			mergePayOrderService.submitById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(id), e);
			result.setSuccess(false);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 供应商确认驳回
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.REJECT_UPDATE_MERGE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult rejectPayOrderById(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			if (!ServiceSupport.isAllowPerm(BusUrlConsts.REJECT_UPDATE_MERGE_PAY_ORDER)) {// 判断用户是否拥有权限
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "用户无权限!");
			}
			mergePayOrderService.rejectPayOrderById(poAuditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新付款失败[{}]", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("更新付款异常，请稍后重试");
		}
		return result;
	}
}
