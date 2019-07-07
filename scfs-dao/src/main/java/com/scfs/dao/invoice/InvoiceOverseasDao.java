package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.invoice.dto.req.InvoiceOverseasSearchReqDto;
import com.scfs.domain.invoice.entity.InvoiceOverseas;

@Repository
public interface InvoiceOverseasDao {
	/**
	 * 添加数据
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	int insert(InvoiceOverseas invoiceOverseas);

	/**
	 * 修改
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	int updateById(InvoiceOverseas invoiceOverseas);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceOverseas queryEntityById(Integer id);

	/**
	 * 获取分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceOverseas> queryResultsByCon(InvoiceOverseasSearchReqDto reqDto, RowBounds rowBounds);

	List<InvoiceOverseas> queryResultsByCon(InvoiceOverseasSearchReqDto reqDto);

	/**
	 * 获取导出总行数
	 * 
	 * @param reqDto
	 * @return
	 */
	public int isOverasyncMaxLine(InvoiceOverseasSearchReqDto reqDto);

}
