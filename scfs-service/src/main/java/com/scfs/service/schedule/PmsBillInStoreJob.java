package com.scfs.service.schedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.api.pms.PmsStoreInDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.entity.PmsStoreIn;
import com.scfs.service.api.pms.PmsSyncBillInStoreService;
import com.scfs.service.interf.PmsBillStoreInService;

@Service
public class PmsBillInStoreJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsBillInStoreJob.class);
	@Autowired
	private PmsStoreInDao pmsStoreInDao;
	@Autowired
	private PmsSyncBillInStoreService pmsSyncBillInStoreService;
	@Autowired
	private PmsBillStoreInService billStoreInService;

	@IgnoreTransactionalMark
	public void execute() {
		try {
			List<PmsStoreIn> list = pmsStoreInDao.queryPmsStoreIn();
			if (!CollectionUtils.isEmpty(list)) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[pms入库单自动处理]开始时间：" + new Date());
				for (PmsStoreIn pmsStoreIn : list) {
					Integer id = pmsStoreIn.getId();
					try {
						// 插入头信息和明细信息
						billStoreInService.dealPmsStoreIn(id, true);
					} catch (BaseException e) {
						// 更新处理状态
						billStoreInService.dealFail(pmsStoreIn.getId(), e.getMsg());
						pmsSyncBillInStoreService.sendSystemAlarm(id, BaseConsts.ZERO, e.getMessage());
						LOGGER.error("处理pms入库单失败[{}]: {}", JSONObject.toJSON(pmsStoreIn), e);
					} catch (Exception e) {
						// 更新处理状态
						billStoreInService.dealFail(pmsStoreIn.getId(), e.getMessage());
						pmsSyncBillInStoreService.sendSystemAlarm(id, BaseConsts.ZERO, e.getMessage());
						LOGGER.error("处理pms入库单失败[{}]: {}", JSONObject.toJSON(pmsStoreIn), e);
					}
				}
				long endTime = System.currentTimeMillis();
				LOGGER.info("[pms入库单自动处理]结束时间：" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
			}
		} catch (BaseException e) {
			LOGGER.error("入库单同步定时任务定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("入库单同步定时任务定时任务失败：", e);
		}
	}
}
