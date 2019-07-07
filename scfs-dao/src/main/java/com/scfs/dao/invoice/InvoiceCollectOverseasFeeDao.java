package com.scfs.dao.invoice;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasFeeReqDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasFee;

public interface InvoiceCollectOverseasFeeDao {
	int insert(InvoiceCollectOverseasFee record);

	InvoiceCollectOverseasFee queryEntityById(Integer id);

	int updateById(InvoiceCollectOverseasFee record);

	/**
	 * 根据境外收票ID查询列表数据
	 * 
	 * @param overId
	 * @return
	 */
	List<InvoiceCollectOverseasFee> queryOverseasIdResult(Integer overId);

	/**
	 * 带分页的查询费用收票的数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectOverseasFee> queryInvoiceFeeResult(InvoiceCollectOverseasFeeReqDto reqDto, RowBounds rowBounds);

	/**
	 * 不带分页的查询费用收票的数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectOverseasFee> queryInvoiceFeeResult(InvoiceCollectOverseasFeeReqDto reqDto);
}