package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.report.ReportRecordService;

/**
 * Created by Administrator on 2017年4月13日.
 */
@Service
public class PerformanceReportJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceReportJob.class);
	@Autowired
	private ReportRecordService reportRecordService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[绩效报表统计定时任务]开始时间：" + new Date());
		try {
			reportRecordService.dealPerformanceReport(true);
		} catch (BaseException e) {
			LOGGER.error("绩效报表统计定时任务定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("绩效报表统计定时任务定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[绩效报表统计定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
