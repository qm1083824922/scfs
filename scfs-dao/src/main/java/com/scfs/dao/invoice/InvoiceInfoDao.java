package com.scfs.dao.invoice;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.entity.InvoiceInfo;

public interface InvoiceInfoDao {
	int deleteById(Integer id);

	int insert(InvoiceInfo record);

	List<InvoiceInfo> selectByApplyId(Integer id);

	int updateById(InvoiceInfo record);

	InvoiceInfo queryEntityById(Integer id);

	InvoiceInfo queryEntityByInvoiceNo(String invoiceNo);

	Integer queryByInvoiceCode(String invoiceCode);

	List<InvoiceInfo> queryResult(InvoiceApplyManagerReqDto invoiceApplyManagerReqDto);

	List<InvoiceInfo> queryResult(InvoiceApplyManagerReqDto invoiceApplyManagerReqDto, RowBounds rowBounds);

	int isOverasyncMaxLine(InvoiceApplyManagerReqDto invoiceApplyManagerReqDto);

	BigDecimal sumPoTitle(InvoiceApplyManagerReqDto invoiceApplyManagerReqDto);
}