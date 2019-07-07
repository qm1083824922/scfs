package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.service.bookkeeping.DistributeBookkeepingService;
import com.scfs.service.common.MsgContentService;

/**
 * 根据铺货订单自动生成未请款商品凭证(一正一负) Created by Administrator on 2017年5月12日.
 */
@Service
public class AutoDistributeAccountJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(AutoDistributeAccountJob.class);

	@Autowired
	private DistributeBookkeepingService distributeBookkeepingService;
	@Autowired
	private MsgContentService msgContentService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[自动生成未请款凭证定时任务]开始时间：" + new Date());
		try {
			distributeBookkeepingService.distributeBookkeeping();
		} catch (BaseException e) {
			sendSystemAlarm(e.getMsg());
			LOGGER.error("自动生成未请款凭证定时任务失败：", e.getMsg());
		} catch (Exception e) {
			sendSystemAlarm(e.getMessage());
			LOGGER.error("自动生成未请款凭证定时任务失败：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[自动生成未请款凭证定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}

	public void sendSystemAlarm(String errorMsg) {
		String currDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date());
		String title = "【" + currDate + "】自动生成未请款凭证定时任务失败，失败原因：" + errorMsg;
		String msg = "【" + currDate + "】自动生成未请款凭证定时任务失败，失败原因：" + errorMsg;
		msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, msg, BaseConsts.ONE);
		msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, msg, BaseConsts.TWO);
	}
}
