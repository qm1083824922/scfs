package com.scfs.dao.common;

import org.apache.ibatis.annotations.Param;

import com.scfs.domain.common.entity.SysParam;

public interface SysParamDao {
	int deleteById(Integer id);

	int insert(SysParam sysParam);

	SysParam queryEntityById(Integer id);

	int updateById(SysParam sysParam);

	SysParam queryEntityByParamKey(@Param("paramKey") String paramKey);
}