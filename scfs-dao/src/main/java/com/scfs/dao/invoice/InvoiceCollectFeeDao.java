package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.invoice.dto.req.InvoiceCollectFeeReqDto;
import com.scfs.domain.invoice.dto.resp.TaxRateSum;
import com.scfs.domain.invoice.entity.InvoiceCollectFee;

@Repository
public interface InvoiceCollectFeeDao {
	/***
	 * 添加数据
	 * 
	 * @param invoiceCollectFee
	 * @return
	 */
	int insert(InvoiceCollectFee invoiceCollectFee);

	/**
	 * 修改数据
	 * 
	 * @param invoiceCollectFee
	 * @return
	 */
	int updateById(InvoiceCollectFee invoiceCollectFee);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceCollectFee queryEntityById(int id);

	/**
	 * 获取所有数据
	 */
	InvoiceCollectFee queryInvoiceCollectFeeById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectFee> queryResultsByCon(InvoiceCollectFeeReqDto reqDto, RowBounds rowBounds);

	/**
	 * 获取所有数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<InvoiceCollectFee> queryResultsByCon(InvoiceCollectFeeReqDto reqDto);

	/**
	 * 根据税率分组，查询费用税金总额
	 *
	 * @param invoice_collect_id
	 * @return
	 */
	List<TaxRateSum> queryRateSumByInvoiceCollectId(Integer invoice_collect_id);

}
