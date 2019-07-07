package com.scfs.dao.common;

import com.scfs.domain.common.entity.SyncDataTimestamp;

public interface SyncDataTimestampDao {
	int insert(SyncDataTimestamp syncDataTimestamp);

	SyncDataTimestamp queryEntityById(Integer id);

	int updateById(SyncDataTimestamp syncDataTimestamp);

	SyncDataTimestamp queryEntityByBusinessType(Integer businessType);

}