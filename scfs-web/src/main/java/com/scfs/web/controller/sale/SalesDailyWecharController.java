package com.scfs.web.controller.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.SalesDailyWecharReqDto;
import com.scfs.domain.sale.dto.resp.SalesDailyWecharResDto;
import com.scfs.service.sale.SalesDailyWecharService;
import com.scfs.web.controller.BaseController;
import com.scfs.web.controller.report.InvoicingWecharController;

/**
 * <pre>
 * 	 
 *  File: SalesDailyWecharController.java
 *  Description:销售日报微信推送
 *  TODO
 *  Date,					Who,				
 *  2017年10月26日			Administrator
 *
 * </pre>
 */
@Controller
public class SalesDailyWecharController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoicingWecharController.class);
	@Autowired
	private SalesDailyWecharService salesDailyWecharService;

	/**
	 * 获取资金周转率明细信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_SALES_DAILY, method = RequestMethod.POST)
	@ResponseBody
	public Result<SalesDailyWecharResDto> querSalesDailyWechar(SalesDailyWecharReqDto reqDto) {
		Result<SalesDailyWecharResDto> result = new Result<SalesDailyWecharResDto>();
		try {
			result = salesDailyWecharService.querSalesDailyWechar(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取销售日报信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取销售日报信息失败[{}]", null, e);
			result.setMsg("获取销售日报信息异常，请稍后重试");
		}
		return result;
	}
}
