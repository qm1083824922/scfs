package com.scfs.dao.api.pms;

import com.scfs.domain.api.pms.entity.PmsOutPoRel;

public interface PmsOutPoRelDao {
	int deleteByPrimaryKey(Integer id);

	int insertPmsOutPoRel(PmsOutPoRel record);

	PmsOutPoRel selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(PmsOutPoRel record);

	int updateByPrimaryKey(PmsOutPoRel record);
}