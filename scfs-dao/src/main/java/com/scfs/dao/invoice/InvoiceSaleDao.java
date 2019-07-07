package com.scfs.dao.invoice;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.entity.InvoiceSaleManager;

public interface InvoiceSaleDao {

	int deleteByCon(Integer id);

	int insert(InvoiceSaleManager record);

	/**
	 * 根据开票单查询出多条结果
	 * 
	 * @return
	 */
	int deleteByInvoiceAppId(Integer InvAppId);

	List<InvoiceSaleManager> selectByInvoiceId(Integer invoiceApplyId, RowBounds rowBounds);

	List<InvoiceSaleManager> selectByInvoiceId(Integer invoiceApplyId);

	int updateById(InvoiceSaleManager record);

	// 查询申请金额
	InvoiceSaleManager querySaleFee(Integer id);

	InvoiceSaleManager queryAndLockById(Integer id);

	BigDecimal querySaleAmountByAppId(Integer InvAppId);
}