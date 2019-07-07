package com.scfs.dao.api.pms;

import com.scfs.domain.api.pms.entity.PmsReturnPoRel;

public interface PmsReturnPoRelDao {
	int deleteByPrimaryKey(Integer id);

	int insert(PmsReturnPoRel record);

	PmsReturnPoRel selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(PmsReturnPoRel record);

	int updateByPrimaryKey(PmsReturnPoRel record);
}