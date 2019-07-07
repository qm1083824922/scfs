package com.scfs.dao.invoice;

import java.util.List;

import com.scfs.domain.invoice.dto.resp.InvoiceSumTaxGroup;
import com.scfs.domain.invoice.entity.InvoiceDtlInfo;

public interface InvoiceDtlInfoDao {
	int deleteById(Integer id);

	int insert(InvoiceDtlInfo record);

	List<InvoiceDtlInfo> selectByApplyId(Integer id);

	int updateById(InvoiceDtlInfo record);

	List<InvoiceSumTaxGroup> querySumTaxGroupByInvoiceId(Integer invoiceId);
}