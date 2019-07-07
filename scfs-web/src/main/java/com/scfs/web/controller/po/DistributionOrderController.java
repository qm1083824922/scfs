package com.scfs.web.controller.po;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.entity.PurchaseReturnDtl;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.po.DistributionOrderService;
import com.scfs.service.po.PurchaseOrderService;

@Controller
public class DistributionOrderController {
	@Autowired
	private DistributionOrderService distributionOrderService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	private final static Logger LOGGER = LoggerFactory.getLogger(DistributionOrderController.class);

	@RequestMapping(value = BusUrlConsts.QUERY_DISTRIBUTION_ORDER_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoTitleRespDto> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		try {
			poTitleReqDto.setOrderType(2);
			result = distributionOrderService.queryPoTitlesResultsByCon(poTitleReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询铺货单信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg(e.getMsg());
			return result;
		} catch (Exception e) {
			LOGGER.error("查询信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_DISTRIBUTION_ORDER_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> detailPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return distributionOrderService.queryPurchaseOrderTitleById(purchaseOrderTitle.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("浏览铺货单详细信息异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("浏览铺货单详细信息异常[{}]", purchaseOrderTitle, e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_DISTRIBUTION_ORDER_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesByPoTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return distributionOrderService.queryPoLinesByPoTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询铺货单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询铺货单列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 铺货订单导出
	 * 
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_ORDER, method = RequestMethod.GET)
	public String exportDistriOrderTitle(ModelMap model, PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.TWO);
		List<PoTitleRespDto> result = purchaseOrderService.queryAllPoTitlesResultsByCon(poTitleReqDto);
		if (!CollectionUtils.isEmpty(result)) {
			model.addAttribute("disOrderList", result);
		} else {
			model.addAttribute("disOrderList", new ArrayList<PoTitleRespDto>());
		}
		return "export/po/dis_list";
	}

	/**
	 * 铺货订单明细导出
	 * 
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_ORDER_DTL, method = RequestMethod.GET)
	public String exportDistriOrderLine(ModelMap model, PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.TWO);
		List<PoLineModel> result = distributionOrderService.queryDistriOrderLine(poTitleReqDto);
		if (!CollectionUtils.isEmpty(result)) {
			model.addAttribute("disOrderDtlList", result);
		} else {
			model.addAttribute("disOrderDtlList", new ArrayList<PurchaseReturnDtl>());
		}
		return "export/po/dis_order_dtl_list";
	}

	/**
	 * 铺货订单导出统计
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_ORDER_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportDistriOrderCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = distributionOrderService.isOverDistriOrderMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	/**
	 * 铺货订单明细导出统计
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_ORDER_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportDistriOrderLineCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = distributionOrderService.isOverasyncDistriOrderDtlByTitleIdMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}
}
