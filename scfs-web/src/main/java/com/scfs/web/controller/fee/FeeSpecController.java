package com.scfs.web.controller.fee;

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
import com.scfs.domain.fee.dto.req.FeeSpecSearchReqDto;
import com.scfs.domain.fee.dto.resp.FeeSpecResDto;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.FeeSpecService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: FeeSpecController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator
 *
 * </pre>
 */
@Controller
public class FeeSpecController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeSpecController.class);
	@Autowired
	FeeSpecService feeSpecService;

	/**
	 * 新建费用科目
	 * 
	 * @param feeSpec
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADDFEESPEC, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createFeeSpec(FeeSpec feeSpec) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = feeSpecService.createFeeSpec(feeSpec);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加费用科目异常[{}]", JSONObject.toJSON(feeSpec), e);
			br.setSuccess(false);
			br.setMsg("添加费用科目失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览费用科目
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAILFEESPEC, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeSpecResDto> detailFeeSpecResDtoById(FeeSpec feeSpec) {
		Result<FeeSpecResDto> result = new Result<FeeSpecResDto>();
		try {
			result = feeSpecService.editFeeSpecById(feeSpec);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览费用科目失败[{}]", JSONObject.toJSON(feeSpec), e);
			result.setMsg("浏览费用科目异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑费用科目
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDITFEESPEC, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeSpecResDto> editFeeSpecResDtoById(FeeSpec feeSpec) {
		Result<FeeSpecResDto> result = new Result<FeeSpecResDto>();
		try {
			result = feeSpecService.editFeeSpecById(feeSpec);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑费用科目失败[{}]", JSONObject.toJSON(feeSpec), e);
			result.setMsg("编辑费用科目异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新费用科目
	 * 
	 * @param feeSpec
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATEFEESPEC, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateFeeSpecById(FeeSpec feeSpec) {
		BaseResult result = new BaseResult();
		try {
			result = feeSpecService.updateFeeSpecById(feeSpec);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新费用科目失败[{}]", JSONObject.toJSON(feeSpec), e);
			result.setMsg("更新费用科目异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除费用科目
	 * 
	 * @param feeSpec
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEFEESPEC, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFeeSpecById(FeeSpec feeSpec) {
		BaseResult result = new BaseResult();
		try {
			result = feeSpecService.deleteFeeSpecById(feeSpec);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除费用科目失败[{}]", JSONObject.toJSON(feeSpec), e);
			result.setMsg("删除费用科目异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询数据
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYFEESPEC, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeSpecResDto> queryAllFeeSpec(FeeSpecSearchReqDto req) {
		PageResult<FeeSpecResDto> pageResult = new PageResult<FeeSpecResDto>();
		try {
			pageResult = feeSpecService.queryFeeSpecResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

}
