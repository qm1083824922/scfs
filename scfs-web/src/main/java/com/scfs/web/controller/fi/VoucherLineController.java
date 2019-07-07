package com.scfs.web.controller.fi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.domain.fi.dto.req.VoucherLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.StandardCoinResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineModelResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.common.exception.BaseException;
import com.scfs.service.fi.VoucherLineService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: VoucherLineController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月4日				Administrator
 *
 * </pre>
 */
@RestController
public class VoucherLineController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(VoucherLineController.class);

	@Autowired
	VoucherLineService voucherLineService;

	@RequestMapping(value = BusUrlConsts.QUERY_VOUCHER_LINE_GROUP, method = RequestMethod.POST)
	public PageResult<VoucherLineResDto> queryGroupResultsByCon(VoucherLineSearchReqDto req) {
		PageResult<VoucherLineResDto> pageResult = new PageResult<VoucherLineResDto>();
		try {
			pageResult = voucherLineService.queryGroupResultsByCon(req);
		} catch (BaseException e) {
			LOGGER.error("查询分录汇总结果失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询分录汇总结果失败[{}]", JSONObject.toJSON(req), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_VOUCHER_LINE, method = RequestMethod.POST)
	public PageResult<VoucherLineModelResDto> queryVoucherLineByCon(VoucherLineSearchReqDto req) {
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		try {
			pageResult = voucherLineService.queryLineResultsByCon(req);
		} catch (BaseException e) {
			LOGGER.error("查询分录失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询分录失败[{}]", JSONObject.toJSON(req), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_VOUCHER_LINE_CHECK, method = RequestMethod.POST)
	public PageResult<VoucherLineModelResDto> queryVoucherLineCheckAllByCon(VoucherLineSearchReqDto req) {
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		try {
			List<VoucherLineModelResDto> list = voucherLineService.queryLineCheckResultsByCon(req);
			pageResult.setItems(list);
		} catch (BaseException e) {
			LOGGER.error("查询待对账分录失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询待对账分录失败[{}]", JSONObject.toJSON(req), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_VOUCHER_LINE_CHECK_PAGE, method = RequestMethod.POST)
	public PageResult<VoucherLineModelResDto> queryVoucherLineCheckByCon(VoucherLineSearchReqDto req) {
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		try {
			pageResult = voucherLineService.queryLineCheckPageByCon(req);
		} catch (BaseException e) {
			LOGGER.error("查询待对账分录失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询待对账分录失败[{}]", JSONObject.toJSON(req), e);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_VOUCHER_LINE_MERGE, method = RequestMethod.POST)
	public PageResult<VoucherLineModelResDto> queryVoucherLineMergeByIds(VoucherLineSearchReqDto req) {
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		try {
			List<VoucherLineModelResDto> voucherLineModelResDtos = voucherLineService.queryEntityByIds(req);
			pageResult.setItems(voucherLineModelResDtos);
		} catch (BaseException e) {
			LOGGER.error("查询待合并分录失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询待合并分录失败[{}]", JSONObject.toJSON(req), e);
		}
		return pageResult;
	}

	/**
	 * 浏览待对账分录明细 TODO.
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_CHECK_BILL_INFO, method = RequestMethod.POST)
	public PageResult<VoucherLineModelResDto> detailCheckBillInfo(VoucherLineSearchReqDto req) {
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		try {
			List<VoucherLineModelResDto> list = voucherLineService.queryLineCheckResultsByCon(req);
			pageResult.setItems(list);
		} catch (BaseException e) {
			LOGGER.error("查询待对账分录失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询待对账分录失败[{}]", JSONObject.toJSON(req), e);
		}
		return pageResult;
	}

	/**
	 * 获取本币金额、本币币种
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_STANDARD_COIN_INFO, method = RequestMethod.POST)
	public Result<StandardCoinResDto> queryStandardCoinInfo(VoucherLineSearchReqDto voucherLineSearchReqDto) {
		Result<StandardCoinResDto> result = new Result<StandardCoinResDto>();
		try {
			StandardCoinResDto res = voucherLineService.queryStandardCoinInfo(voucherLineSearchReqDto);
			result.setItems(res);
		} catch (BaseException e) {
			LOGGER.error("查询凭证本币金额、本币币种失败[{}]", JSONObject.toJSON(voucherLineSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询凭证本币金额、本币币种失败[{}]", JSONObject.toJSON(voucherLineSearchReqDto), e);
			result.setMsg("查询凭证本币金额、本币币种异常，请稍后重试");
		}
		return result;
	}

}
