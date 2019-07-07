package com.scfs.web.controller.fi;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineModelResDto;
import com.scfs.domain.fi.dto.resp.VoucherResDto;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.VoucherLineService;
import com.scfs.service.fi.VoucherService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: VoucherController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月26日			Administrator
 *
 * </pre>
 */
@Controller
public class VoucherController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(VoucherController.class);

	@Autowired
	VoucherService voucherService;
	@Autowired
	VoucherLineService voucherLineService;

	@RequestMapping(value = BusUrlConsts.ADD_VOUCHER_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createVoucherDetail(@RequestBody VoucherDetail voucherDetail) {
		BaseResult baseResult = new BaseResult();
		try {
			int id = voucherService.createVoucherDetailByCon(voucherDetail);
			if (id <= 0) {
				baseResult.setMsg("新增凭证异常，请稍后重试");
			}
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增凭证失败[{}]", JSONObject.toJSON(voucherDetail), e);
			baseResult.setMsg("新增凭证异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_VOUCHER_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateVoucherDetail(@RequestBody VoucherDetail voucherDetail) {
		BaseResult baseResult = new BaseResult();
		try {
			voucherService.updateVoucherDetail(voucherDetail);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改凭证失败[{}]", JSONObject.toJSON(voucherDetail), e);
			baseResult.setMsg("修改凭证异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_VOUCHER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<VoucherResDto> queryVoucherResultsByCon(VoucherSearchReqDto req) {
		PageResult<VoucherResDto> pageResult = new PageResult<VoucherResDto>();
		try {
			pageResult = voucherService.queryVoucherResultsByCon(req);
		} catch (Exception e) {
			LOGGER.error("查询凭证失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("修查询凭证异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_VOUCHER_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public Result<VoucherDetailResDto> detailVoucherResultsById(int voucherId) {
		Result<VoucherDetailResDto> result = new Result<VoucherDetailResDto>();
		try {
			VoucherDetailResDto res = voucherService.detailVoucherDetailById(voucherId);
			result.setItems(res);
		} catch (Exception e) {
			LOGGER.error("查询凭证失败[{}]", JSONObject.toJSON(voucherId), e);
			result.setMsg("查询凭证异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_VOUCHER_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public Result<VoucherDetailResDto> editVoucherResultsById(int voucherId) {
		Result<VoucherDetailResDto> result = new Result<VoucherDetailResDto>();
		try {
			VoucherDetailResDto res = voucherService.editVoucherDetailById(voucherId);
			result.setItems(res);
		} catch (Exception e) {
			LOGGER.error("查询凭证失败[{}]", JSONObject.toJSON(voucherId), e);
			result.setMsg("查询凭证异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_VOUCHER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteVoucherById(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			voucherService.deleteVoucherById(id);
		} catch (Exception e) {
			LOGGER.error("删除凭证失败[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("删除凭证异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_VOUCHER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitVoucherById(int id) {
		BaseResult baseResult = new BaseResult();
		try {
			voucherService.submitVoucherById(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交凭证失败[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("提交凭证异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 查询凭证明细数量
	 * 
	 * @param voucherLineSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_VOUCHER_LINE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportVoucherLineCount(VoucherSearchReqDto voucherSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = voucherLineService.isOverVoucherLineMaxLine(voucherSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(voucherSearchReqDto), e);
		}
		return result;
	}

	/**
	 * 导出凭证明细
	 * 
	 * @param model
	 * @param voucherLineSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_VOUCHER_LINE, method = RequestMethod.GET)
	public String exportVoucherLine(ModelMap model, VoucherSearchReqDto voucherSearchReqDto) {
		List<VoucherLineModelResDto> result = voucherLineService.queryVoucherLineModelList(voucherSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("voucherLineList", result);
		} else {
			model.addAttribute("voucherLineList", new ArrayList<VoucherLineModelResDto>());
		}
		return "export/fi/voucher_line_list";
	}

}
