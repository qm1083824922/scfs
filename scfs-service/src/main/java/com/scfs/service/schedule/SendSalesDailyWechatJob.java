package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.sale.SalesDailyWecharService;

/**
 * <pre>
 * 	 
 *  File: SendSalesDailyWechatJob.java
 *  Description:销售日报微信推送
 *  TODO
 *  Date,					Who,				
 *  2017年10月26日			Administrator
 *
 * </pre>
 */
@Service
public class SendSalesDailyWechatJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(SendSalesDailyWechatJob.class);
	@Autowired
	private SalesDailyWecharService salesDailyWecharService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("执行销售日报发送微信定时任务]开始时间：" + new Date());
		try {
			salesDailyWecharService.realSendWechatMsg();
		} catch (BaseException e) {
			LOGGER.error("执行销售日报发送微信定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("执行销售日报发送微信定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[执行销售日报发送微信定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
