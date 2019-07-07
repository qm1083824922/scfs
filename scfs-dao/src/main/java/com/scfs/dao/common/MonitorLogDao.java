package com.scfs.dao.common;

import com.scfs.domain.common.entity.MonitorLog;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorLogDao {
	int insert(MonitorLog monitorLog);
}
