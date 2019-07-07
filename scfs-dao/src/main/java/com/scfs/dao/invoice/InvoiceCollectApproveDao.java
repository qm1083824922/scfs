package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.dto.req.InvoiceCollectApproveSearchReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectApprove;

public interface InvoiceCollectApproveDao {
	int deleteById(Integer id);

	int insert(InvoiceCollectApprove record);

	InvoiceCollectApprove queryEntityById(Integer id);

	InvoiceCollectApprove queryAndLockEntityById(Integer id);

	int updateById(InvoiceCollectApprove record);

	List<InvoiceCollectApprove> queryResultsByCon(InvoiceCollectApproveSearchReqDto invoiceCollectApproveSearchReqDto,
			RowBounds rowBounds);
}