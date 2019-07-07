package com.scfs.web.controller.fi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.resp.AdvanceResDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.AdvanceService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 *  转预收Controller
 *  File: AdvanceController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator
 *
 * </pre>
 */
@Controller
public class AdvanceController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AdvanceController.class);

	@Autowired
	AdvanceService advanceService;

	/**
	 * 新增转预收
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_ADVANCE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createAdvance(AdvanceReceiptRel advanceSearchReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			advanceService.createAdvanceRel(advanceSearchReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增转预收失败[{}]", JSONObject.toJSON(advanceSearchReqDto), e);
			baseResult.setMsg("新增转预收异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 批量删除转预存信息
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_ADVANCE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAllAdvance(AdvanceSearchReqDto advanceSearchReqDto) {
		BaseResult br = new BaseResult();
		try {
			for (Integer id : advanceSearchReqDto.getIds()) {
				AdvanceReceiptRel advanceReceiptRel = new AdvanceReceiptRel();
				advanceReceiptRel.setId(id);
				AdvanceResDto advanceRel = advanceService.detailAdvanceReceiptRelById(id);
				if (advanceRel.getDeletePrivFlag().equals(BaseConsts.ONE)) {
					br.setMsg("项目【" + advanceRel.getProjectName() + "】 客户【" + advanceRel.getCustName() + "】 预收金额【"
							+ advanceRel.getPreRecAmount() + "】 前台不能删除，请重新选择");
					return br;
				}
			}
			br = advanceService.deleteAdvanceRelById(advanceSearchReqDto);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除转预存信息异常[{}]", JSONObject.toJSON(advanceSearchReqDto), e);
			br.setMsg("批量删除转预存信息失败，请重试");
		}
		return br;
	}

	/**
	 * 转预存列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_ADVANCE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AdvanceResDto> queryAdvanceResultsByCon(AdvanceSearchReqDto advanceSearchReqDto) {
		PageResult<AdvanceResDto> pageResult = new PageResult<AdvanceResDto>();
		try {
			pageResult = advanceService.queryAdvanceRelResultsByCon(advanceSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("转预存失败[{}]", JSONObject.toJSON(advanceSearchReqDto), e);
			pageResult.setMsg("转预存异常，请稍后重试");
		}
		return pageResult;
	}
}
