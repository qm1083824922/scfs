package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseas;

public interface InvoiceCollectOverseasDao {
	int deleteByPrimaryKey(Integer id);

	int insert(InvoiceCollectOverseas record);

	InvoiceCollectOverseas queryEntityById(Integer id);

	int updateById(InvoiceCollectOverseas record);

	/**
	 * 带分页查询境外收票
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectOverseas> queryInvoiceOverseasList(InvoiceCollectOverseasReqDto reqDto, RowBounds rowBounds);

	/**
	 * 不带分页查询境外收票
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectOverseas> queryInvoiceOverseasList(InvoiceCollectOverseasReqDto reqDto);

	/**
	 * invoice收票进行合计查询金额
	 * 
	 * @param reqDto
	 * @return
	 */
	List<InvoiceCollectOverseas> sumInvoiceCollectOversrea(InvoiceCollectOverseasReqDto reqDto);

	/**
	 * 获取导出总行数
	 * 
	 * @param reqDto
	 * @return
	 */
	public int isOverasyncMaxLine(InvoiceCollectOverseasReqDto reqDto);

	InvoiceCollectOverseas queryInvoiceByApplyNo(InvoiceCollectOverseas collectOverseas);

}