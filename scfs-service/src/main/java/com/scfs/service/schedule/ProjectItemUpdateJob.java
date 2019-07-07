package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.exception.BaseException;
import com.scfs.service.project.ProjectItemService;

/**
 * Created by Administrator on 2016年12月20日.
 */
@Service
public class ProjectItemUpdateJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectItemUpdateJob.class);
	@Autowired
	private ProjectItemService projectItemService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[项目条款更新定时任务]开始时间：" + new Date());
		try {
			projectItemService.updateStatusBySchedule();
		} catch (BaseException e) {
			LOGGER.error("执行项目条款更新定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("执行项目条款更新定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[项目条款更新定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
