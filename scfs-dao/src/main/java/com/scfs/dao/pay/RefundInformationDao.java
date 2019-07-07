package com.scfs.dao.pay;

import com.scfs.domain.pay.entity.RefundInformation;

public interface RefundInformationDao {
	int deleteByPrimaryKey(Integer id);

	int insert(RefundInformation record);

	int insertSelective(RefundInformation record);

	RefundInformation selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RefundInformation record);

	int updateByPrimaryKey(RefundInformation record);

	int insertRefundInformation(RefundInformation record);
}