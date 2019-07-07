package com.scfs.dao.api.pms;

import com.scfs.domain.api.pms.entity.PmsPledgePayConfirm;

public interface PmsPledgePayConfirmDao {
	int deleteById(Integer id);

	int insert(PmsPledgePayConfirm pmsPledgePayConfirm);

	PmsPledgePayConfirm queryEntityById(Integer id);

	int updateById(PmsPledgePayConfirm pmsPledgePayConfirm);
}