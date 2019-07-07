package com.scfs.web.controller.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.api.pms.dto.res.InvoicingWecharResDto;
import com.scfs.domain.pay.dto.req.InvoicingWecharReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.api.pms.InvoicingWecharService;

/**
 * <pre>
 * 
 *  File: PmsStoreOutController.java
 *  Description: 供应商销售信息相关业务处理
 *  TODO
 *  Date,					Who,				
 *  2017年09月20日				Administrator
 *
 * </pre>
 */
@Controller
public class InvoicingWecharController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoicingWecharController.class);

	@Autowired
	private InvoicingWecharService invoicingWecharService;

	/**
	 * 获取资金周转率明细信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PMSSTORE_OUT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoicingWecharResDto> queryCapitalTurnover(InvoicingWecharReqDto reqDto) {
		PageResult<InvoicingWecharResDto> result = new PageResult<InvoicingWecharResDto>();
		try {
			result = invoicingWecharService.querySupplierStoreOutBySupplierId(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取供应商销售明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取供应商销售明细信息失败[{}]", null, e);
			result.setMsg("获取供应商销售明细信息异常，请稍后重试");
		}
		return result;
	}
}
