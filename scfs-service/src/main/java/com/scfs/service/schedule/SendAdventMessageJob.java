package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.base.send.SenderManageService;

@Service
public class SendAdventMessageJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(SendAdventMessageJob.class);
	@Autowired
	private SenderManageService senderManageService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("执行账期提醒定时任务]开始时间：" + new Date());
		try {
			senderManageService.adventMessageSchedule();
		} catch (BaseException e) {
			LOGGER.error("执行账期提醒定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("执行账期提醒定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[执行账期提醒定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
