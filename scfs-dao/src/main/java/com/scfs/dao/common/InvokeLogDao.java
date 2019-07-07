package com.scfs.dao.common;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.common.dto.req.InvokeLogSearchReqDto;
import com.scfs.domain.common.entity.InvokeLog;

public interface InvokeLogDao {
	int deleteById(Integer id);

	int insert(InvokeLog invokeLog);

	InvokeLog queryEntityById(Integer id);

	InvokeLog queryAndLockEntityById(Integer id);

	int updateById(InvokeLog invokeLog);

	List<InvokeLog> queryResultsByCon(InvokeLogSearchReqDto invokeLogSearchReqDto, RowBounds rowBounds);

	List<InvokeLog> queryExcResults();

}