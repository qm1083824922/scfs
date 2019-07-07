package com.scfs.dao.invoice;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.entity.InvoiceFeeManager;

public interface InvoiceFeeDao {

	int deleteByCon(InvoiceFeeManager record);

	int insert(InvoiceFeeManager record);

	int updateById(InvoiceFeeManager record);

	// 查询申请金额
	BigDecimal queryApplyFee(Integer id);

	BigDecimal queryFeeAmountByAppId(Integer id);

	InvoiceFeeManager selectInvoiceFeeById(Integer id);

	InvoiceFeeManager quertAndLockById(Integer id);

	List<InvoiceFeeManager> selectByInvoiceId(Integer invoiceApplyId);

	List<InvoiceFeeManager> selectByInvoiceId(Integer invoiceApplyId, RowBounds rowBounds);

}