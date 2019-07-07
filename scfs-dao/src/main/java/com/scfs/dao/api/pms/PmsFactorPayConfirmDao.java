package com.scfs.dao.api.pms;

import com.scfs.domain.api.pms.entity.PmsFactorPayConfirm;

public interface PmsFactorPayConfirmDao {
	int deleteById(Integer id);

	int insert(PmsFactorPayConfirm pmsFactorPayConfirm);

	PmsFactorPayConfirm queryEntityById(Integer id);

	int updateById(PmsFactorPayConfirm pmsFactorPayConfirm);
}