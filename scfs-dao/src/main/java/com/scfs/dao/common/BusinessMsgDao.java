package com.scfs.dao.common;

import org.apache.ibatis.annotations.Param;

import com.scfs.domain.common.entity.BusinessMsg;

public interface BusinessMsgDao {
	int insert(BusinessMsg businessMsg);

	BusinessMsg queryEntityById(Integer id);

	int updateById(BusinessMsg businessMsg);

	BusinessMsg queryEntityByCondition(@Param("billNo") String billNo, @Param("billType") Integer billType,
			@Param("invokeType") Integer invokeType);
}