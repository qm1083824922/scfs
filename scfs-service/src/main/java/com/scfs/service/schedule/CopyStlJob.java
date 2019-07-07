package com.scfs.service.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.logistics.StlHistoryDao;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;

/**
 * Created by Administrator on 2016年10月26日.
 */
public class CopyStlJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(CopyStlJob.class);
	@Autowired
	private StlDao stlDao;
	@Autowired
	private StlHistoryDao stlHistoryDao;

	public void execute() throws Exception {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[历史库存备份定时任务]开始时间：" + new Date());
		try {
			Date currDate = new Date();
			Date copyDate = DateFormatUtils.beforeDay(currDate, 1);

			String currDateStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate);
			String copyDateStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, copyDate);
			currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, currDateStr);
			copyDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, copyDateStr);

			int count = stlHistoryDao.queryStlHistoryCountByCopyDate(copyDate);
			if (count > 0) {
				LOGGER.info(
						"[历史库存备份定时任务]历史库存已备份，备份数据的日期：" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, copyDate));
			} else {
				StlSearchReqDto stlSearchReqDto = new StlSearchReqDto();
				stlSearchReqDto.setCopyDate(copyDate);
				stlSearchReqDto.setCurrDate(currDate);
				int total = stlDao.queryStlCount4LastDay(stlSearchReqDto);
				LOGGER.info("[历史库存备份定时任务]备份记录总数：" + total);
				if (total > 0) {
					int pageSize = 500;
					int mod = total / pageSize;
					int leftSize = total % pageSize;
					int pageNo = 1;
					if (total <= 500) {
						int offSet = PageUtil.getPageOffSet(pageNo, pageSize);
						stlHistoryDao.copyInsert(offSet, leftSize, stlSearchReqDto);
					} else {
						for (int i = 0; i <= mod - 1; i++) {
							pageNo = i + 1;
							int offSet = PageUtil.getPageOffSet(pageNo, pageSize);
							stlHistoryDao.copyInsert(offSet, pageSize, stlSearchReqDto);
						}
						if (leftSize > 0) {
							pageNo = mod + 1;
							int offSet = PageUtil.getPageOffSet(pageNo, pageSize);
							stlHistoryDao.copyInsert(offSet, leftSize, stlSearchReqDto);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("历史库存备份异常：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		}

		long endTime = System.currentTimeMillis();
		LOGGER.info("[历史库存备份定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}
}
