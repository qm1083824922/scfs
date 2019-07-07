package com.scfs.service.schedule;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.exception.BaseException;
import com.scfs.service.project.ProjectPoolAdjustService;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustUpdateJob.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月24日				Administrator
 *
 * </pre>
 */

@Service
public class ProjectPoolAdjustUpdateJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectItemUpdateJob.class);
	@Autowired
	private ProjectPoolAdjustService projectPoolAdjustService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[临时额度更新定时任务]开始时间：" + new Date());
		try {
			projectPoolAdjustService.updateScheduleJob();
		} catch (BaseException e) {
			LOGGER.error("执行临时额度更新定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("执行临时额度更新定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[临时额度更新定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}

}
