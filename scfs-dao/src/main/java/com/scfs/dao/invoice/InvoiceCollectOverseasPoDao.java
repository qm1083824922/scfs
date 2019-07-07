package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasPoReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasPo;

public interface InvoiceCollectOverseasPoDao {
	int deleteByPrimaryKey(Integer id);

	int insert(InvoiceCollectOverseasPo record);

	InvoiceCollectOverseasPo queryEntityById(Integer id);

	int updateById(InvoiceCollectOverseasPo record);

	/**
	 * 根据境外收票id查询列表数据
	 * 
	 * @param overId
	 * @return
	 */
	List<InvoiceCollectOverseasPo> queryOverseasIdResult(Integer overId);

	/**
	 * 带分页查询境外收票关联采购的信息
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectOverseasPo> queryInvoicePoByInvoiceIdResult(InvoiceCollectOverseasPoReqDto reqDto,
			RowBounds rowBounds);

	/**
	 * 不带分页查询境外收票关联采购的信息
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectOverseasPo> queryInvoicePoByInvoiceIdResult(InvoiceCollectOverseasPoReqDto reqDto);

}