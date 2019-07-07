package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.invoice.dto.req.InvoiceOverseasFeeReqDto;
import com.scfs.domain.invoice.entity.InvoiceOverseasFee;

@Repository
public interface InvoiceOverseasFeeDao {
	/**
	 * 添加数据
	 * 
	 * @param invoiceOverseasFee
	 * @return
	 */
	int insert(InvoiceOverseasFee invoiceOverseasFee);

	/**
	 * 修改
	 * 
	 * @param invoiceOverseasFee
	 * @return
	 */
	int updateById(InvoiceOverseasFee invoiceOverseasFee);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceOverseasFee queryEntityById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceOverseasFee> queryResultsByCon(InvoiceOverseasFeeReqDto reqDto, RowBounds rowBounds);

	List<InvoiceOverseasFee> queryResultsByCon(InvoiceOverseasFeeReqDto reqDto);
}
