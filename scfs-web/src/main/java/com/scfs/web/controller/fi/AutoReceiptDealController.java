package com.scfs.web.controller.fi;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.service.fi.AutoReceiptDealService;
import com.scfs.service.fi.BankReceiptService;

/**
 * <pre>
 * 
 *  File: AutoReceiptDealController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月1日				Administrator
 *
 * </pre>
 */
@Controller
public class AutoReceiptDealController {

	@Autowired
	AutoReceiptDealService autoReceiptDealService;
	@Autowired
	BankReceiptService bankReceiptService;

	private final static Logger LOGGER = LoggerFactory.getLogger(AutoReceiptDealController.class);

	@RequestMapping(value = "/api/autoReceiptDeal", method = RequestMethod.GET)
	@ResponseBody
	public String autoReceiptDeal(BankReceiptSearchReqDto bankReceiptSearchReqDto) {
		LOGGER.info("自动处理水单核销开始");
		try {
			autoReceiptDealService.autoReceiptDeal(bankReceiptSearchReqDto);
		} catch (BaseException e) {
			if (StringUtils.isEmpty(e.getMsg())) {
				LOGGER.error("自动处理水单核销失败,并未捕获到异常");
			}
			System.out.println(e.getMsg());
			LOGGER.error("自动处理水单核销失败：" + e.getMsg());
			return "自动处理水单核销失败：" + e.getMsg();
		} catch (Exception e) {
			LOGGER.error("自动处理水单核销异常[{}]", JSONObject.toJSON(bankReceiptSearchReqDto), e);
			return "自动处理水单核销异常";
		}
		LOGGER.info("自动处理水单核销结束");
		return "自动处理水单核销完成";
	}

	@RequestMapping(value = "/api/autoSend", method = RequestMethod.GET)
	@ResponseBody
	public String autoSend(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		LOGGER.info("自动送货开始");
		try {
			autoReceiptDealService.autoSend(billOutStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("自动送货失败：" + e.getMsg());
			return "自动送货失败：" + e.getMsg();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("自动送货异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			return "自动送货异常：";
		}
		LOGGER.info("自动送货结束");
		return "自动送货完成";
	}

	@RequestMapping(value = "/api/rollbackReceipt", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult rollbackReceipt(BankReceiptSearchReqDto bankReceiptSearchReqDto) {
		BaseResult baseResult = new BaseResult();
		LOGGER.info("回滚水单开始");
		try {
			autoReceiptDealService.rollbackSubmitOver(bankReceiptSearchReqDto);
		} catch (BaseException e) {
			baseResult.setMsg("回滚水单失败：" + e.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			baseResult.setMsg("回滚水单异常");
		}
		LOGGER.info("回滚水单结束");
		return baseResult;
	}

	@RequestMapping(value = "/api/autoGenaPoolFundDtl", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult autoGenaPoolFundDtl() {
		BaseResult baseResult = new BaseResult();
		long startTime = System.currentTimeMillis();
		LOGGER.info("清理资金明细开始时间：" + new Date());
		try {
			autoReceiptDealService.autoGenaPoolFundDtl();
		} catch (BaseException e) {
			baseResult.setMsg("清理资金明细失败：" + e.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			baseResult.setMsg("清理资金明细异常");
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("清理资金明细结束时间" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
		return baseResult;
	}

	@RequestMapping(value = "/api/rollbackFee", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult rollbackFee(QueryFeeReqDto queryFeeReqDto) {
		BaseResult baseResult = new BaseResult();
		long startTime = System.currentTimeMillis();
		LOGGER.info("回滚费用开始时间：" + new Date());
		try {
			autoReceiptDealService.rollbackFee(queryFeeReqDto);
		} catch (BaseException e) {
			baseResult.setMsg("回滚费用失败：" + e.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			baseResult.setMsg("回滚费用异常");
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("回滚费用结束时间" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
		return baseResult;
	}

}
