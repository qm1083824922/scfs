package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.common.InvokeLogService;

/**
 * Created by Administrator on 2017年1月5日.
 */
@Service
public class InvokeLogJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvokeLogJob.class);
	@Autowired
	private InvokeLogService invokeLogService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[处理接口日志定时任务]开始时间：" + new Date());
		try {
			invokeLogService.excThirdParty();
		} catch (BaseException e) {
			LOGGER.error("处理接口日志定时任务定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("处理接口日志定时任务定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[处理接口日志定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
