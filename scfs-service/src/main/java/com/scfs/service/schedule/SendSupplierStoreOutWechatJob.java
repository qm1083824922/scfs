package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.api.pms.InvoicingWecharService;

/**
 * <pre>
 *  供应商销售发送微信消息
 *  File: SendSupplierStoreOutWechatJob.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年09月20日			Administrator
 *
 * </pre>
 */
@Service
public class SendSupplierStoreOutWechatJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(SendSupplierStoreOutWechatJob.class);
	@Autowired
	private InvoicingWecharService invoicingWecharService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("执行供应商销售发送微信定时任务]开始时间：" + new Date());
		try {
			invoicingWecharService.realSendWechatMsg();
		} catch (BaseException e) {
			LOGGER.error("执行供应商销售发送微信定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("执行供应商销售发送微信定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[执行供应商销售发送微信定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
