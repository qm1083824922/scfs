package com.scfs.dao.invoice;

import com.scfs.domain.invoice.entity.InvoiceInfoDtl;

public interface InvoiceInfoDtlDao {
	int deleteByPrimaryKey(Integer id);

	int insert(InvoiceInfoDtl record);

	int insertSelective(InvoiceInfoDtl record);

	InvoiceInfoDtl selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(InvoiceInfoDtl record);

	int updateByPrimaryKey(InvoiceInfoDtl record);
}