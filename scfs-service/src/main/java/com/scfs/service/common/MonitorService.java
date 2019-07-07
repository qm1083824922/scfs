package com.scfs.service.common;

import static com.scfs.service.support.ServiceSupport.getUser;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.dao.common.MonitorLogDao;
import com.scfs.domain.common.entity.MonitorLog;

/**
 * Created by Administrator on 2017/3/18.
 */
@Service
public class MonitorService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MonitorService.class);

	@Autowired
	private MonitorLogDao monitorLogDao;

	public void insert(MonitorLog monitorLog) {
		try {
			if (getUser() != null) {
				monitorLog.setCreator(getUser().getChineseName());
				monitorLog.setCreatorId(getUser().getId());
			}
			monitorLog.setCreateAt(new Date());
			monitorLogDao.insert(monitorLog);
		} catch (Exception e) {
			LOGGER.error("【{}】插入监控时间异常", JSONObject.toJSON(monitorLog), e);
		}
	}

}
