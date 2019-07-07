package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.subject.dto.req.AddInvoiceDto;
import com.scfs.domain.base.subject.dto.req.QueryInvoiceReqDto;
import com.scfs.domain.base.subject.dto.resp.QueryInvoiceResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.subject.BaseInvoiceService;
import com.scfs.common.exception.BaseException;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: BaseInvoiceController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Controller
public class BaseInvoiceController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseInvoiceController.class);
	@Autowired
	private BaseInvoiceService baseInvoiceService;

	@RequestMapping(value = BaseUrlConsts.ADDSUBJECTINVOICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBaseInvoice(AddInvoiceDto addInvoiceDto) {
		BaseResult br = new BaseResult();
		try {
			baseInvoiceService.addBaseInvoice(addInvoiceDto);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addInvoiceDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYINVOICEBYID, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseInvoice> queryInvoiceById(QueryInvoiceReqDto queryInvoiceDto) {
		Result<BaseInvoice> result = new Result<BaseInvoice>();
		BaseInvoice invoice = baseInvoiceService.queryInvoiceById(queryInvoiceDto.getId());
		result.setItems(invoice);
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYINVOICEBYSUBJECTID, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<QueryInvoiceResDto> queryInvoiceBySubjectId(QueryInvoiceReqDto queryInvoiceDto) {
		return baseInvoiceService.queryInvoiceBySubjectId(queryInvoiceDto);
	}

	@RequestMapping(value = BaseUrlConsts.BATCHINVALIDSUBJECTINVOICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult invalidSubjectInvoice(QueryInvoiceReqDto queryInvoiceDto) {
		BaseResult br = new BaseResult();
		try {
			baseInvoiceService.invalidBaseInvoiceByIds(queryInvoiceDto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("作废信息异常[{}]", JSONObject.toJSON(queryInvoiceDto), e);
			br.setSuccess(false);
			br.setMsg("作废失败，请重试");
		}
		return br;
	}

}
