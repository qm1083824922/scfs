package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.report.ReportRecordService;

/**
 * <pre>
 *
 *  File: EntryInfoJob.java
 *  Description: 业务指标定时任务
 *  TODO
 *  Date,                   Who,
 *  2017年07月06日         Administrator
 *
 * </pre>
 */
@Service
public class EntryInfoJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(EntryInfoJob.class);
	@Autowired
	private ReportRecordService reportRecordService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[业务指标资金周转率定时任务]开始时间：" + new Date());
		try {
			reportRecordService.dealCapitalTurnover(true);
		} catch (BaseException e) {
			LOGGER.error("业务指标资金周转率定时任务定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("业务指标资金周转率定时任务定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[业务指标资金周转率定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
