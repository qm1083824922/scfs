package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.invoice.dto.req.InvoiceCollectSearchReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollect;

@Repository
public interface InvoiceCollectDao {
	/**
	 * 添加数据
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	int insert(InvoiceCollect invoiceCollect);

	/**
	 * 修改
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	int updateById(InvoiceCollect invoiceCollect);

	int updatePrintNum(InvoiceCollect invoiceCollect);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceCollect queryEntityById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollect> queryResultsByCon(InvoiceCollectSearchReqDto reqDto, RowBounds rowBounds);

	List<InvoiceCollect> queryResultsByCon(InvoiceCollectSearchReqDto reqDto);

	/**
	 * 统计
	 * 
	 * @param reqDto
	 * @return
	 */
	List<InvoiceCollect> sumInvoiceCollect(InvoiceCollectSearchReqDto reqDto);

	/**
	 * 获取导出总行数
	 * 
	 * @param reqDto
	 * @return
	 */
	public int isOverasyncMaxLine(InvoiceCollectSearchReqDto reqDto);

	/**
	 * 通过申请编号获取信息
	 * 
	 * @param ApplyNo
	 * @return
	 */
	InvoiceCollect queryEntityByParam(InvoiceCollect invoiceCollect);
}
