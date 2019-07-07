package com.scfs.dao.pay;

import com.scfs.domain.pay.entity.PayPool;

public interface PayPoolDao {
	int deleteById(Integer id);

	int insert(PayPool record);

	PayPool selectById(Integer id);

	PayPool queryAndLockById(Integer id);

	int updateById(PayPool record);
}