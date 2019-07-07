package com.scfs.web.controller.fi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.RecReceiptRelReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.req.ReceiptOutRelReqDto;
import com.scfs.domain.fi.dto.resp.RecReceiptRelResDto;
import com.scfs.domain.fi.dto.resp.ReceiptOutRelResDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.RecReceiptRelService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: RecReceiptRelController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日			Administrator
 *
 * </pre>
 */
@Controller
public class RecReceiptRelController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(RecReceiptRelController.class);

	@Autowired
	RecReceiptRelService recReceiptRelService;

	/**
	 * 创建应收水单关系
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_RECRECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createRecReceiptRel(@RequestBody RecReceiptRelReqDto recReceiptRelSearchReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = recReceiptRelService.createRecReceiptRel(recReceiptRelSearchReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增应收水单关系失败[{}]", JSONObject.toJSON(recReceiptRelSearchReqDto), e);
			baseResult.setMsg("新增应收水单关系异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 更新应收水单关系
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_RECRECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateRecReceiptRelById(@RequestBody RecReceiptRelReqDto recReceiptRelSearchReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = recReceiptRelService.updateRecReceiptRelById(recReceiptRelSearchReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新应收水单关系失败[{}]", JSONObject.toJSON(recReceiptRelSearchReqDto), e);
			baseResult.setMsg("更新应收水单关系异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_RECRECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public Result<RecReceiptRelResDto> queryRecReceiptRelById(RecReceiptRelReqDto recReceiptRelReqDto) {
		Result<RecReceiptRelResDto> result = new Result<RecReceiptRelResDto>();
		try {
			result = recReceiptRelService.editRecReceiptRelById(recReceiptRelReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑信息失败[{}]", JSONObject.toJSON(recReceiptRelReqDto), e);
			result.setMsg("编辑信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量删除应收水单关系
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_RECRECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRecReceiptRelById(RecReceiptRelSearchReqDto recReceiptRelSearchReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			recReceiptRelService.deleteRecReceiptRelById(recReceiptRelSearchReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除应收水单关系异常[{}]", JSONObject.toJSON(recReceiptRelSearchReqDto), e);
			baseResult.setMsg("批量删除应收水单关系失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 应收水单列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_RECRECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<RecReceiptRelResDto> queryRecReceiptRelResultsByCon(
			RecReceiptRelSearchReqDto recReceiptRelSearchReqDto) {
		PageResult<RecReceiptRelResDto> pageResult = new PageResult<RecReceiptRelResDto>();
		try {
			pageResult = recReceiptRelService.queryRecReceiptRelResultsByCon(recReceiptRelSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应收水单信息失败[{}]", JSONObject.toJSON(recReceiptRelSearchReqDto), e);
			pageResult.setMsg("查询应收水单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 应收列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_RECRECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveResDto> queryResultsByCon(RecReceiptRelReqDto recReceiptRelReqDto) {
		PageResult<ReceiveResDto> pageResult = new PageResult<ReceiveResDto>();
		try {
			pageResult = recReceiptRelService.queryResultsByCon(recReceiptRelReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应收水单信息失败[{}]", JSONObject.toJSON(recReceiptRelReqDto), e);
			pageResult.setMsg("查询应收水单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询水单类型为供应商退款类型的列表数据
	 * 
	 * @param relReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_RECRECEIPTOUTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiptOutRelResDto> queryRecOutResultByCon(ReceiptOutRelReqDto relReqDto) {
		PageResult<ReceiptOutRelResDto> pageResult = new PageResult<ReceiptOutRelResDto>();
		try {
			pageResult = recReceiptRelService.queryReceiptOutRelByCon(relReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库水单信息失败[{}]", JSONObject.toJSON(relReqDto), e);
			pageResult.setMsg("查询出库水单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询出库单的数据
	 * 
	 * @param relReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_RECRECEIPTOUTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiptOutRelResDto> queryRecOutResultsByCon(ReceiptOutRelReqDto relReqDto) {
		PageResult<ReceiptOutRelResDto> pageResult = new PageResult<ReceiptOutRelResDto>();
		try {
			pageResult = recReceiptRelService.queryRecOutResultsByCon(relReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库水单信息失败[{}]", JSONObject.toJSON(relReqDto), e);
			pageResult.setMsg("查询出库水单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 创建出库单和水单的关系
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_RECRECEIPTOUTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createRecOutRel(@RequestBody ReceiptOutRelReqDto outRelReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			recReceiptRelService.createRecOutRelByCon(outRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库水单关系失败[{}]", JSONObject.toJSON(outRelReqDto), e);
			baseResult.setMsg("新增出库水单关系异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 批量删除水单出库单关联数据
	 * 
	 * @param outRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_RECRECEIPTOUTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRecOutRelByCon(ReceiptOutRelReqDto outRelReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			recReceiptRelService.deleteRecOutRelByCon(outRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除出库水单关系失败[{}]", JSONObject.toJSON(outRelReqDto), e);
			baseResult.setMsg("批量删除出库水单关系异常，请稍后重试");
		}
		return baseResult;
	}

}
