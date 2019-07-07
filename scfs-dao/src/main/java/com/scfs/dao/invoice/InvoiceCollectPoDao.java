package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.invoice.dto.req.InvoiceCollectPoReqDto;
import com.scfs.domain.invoice.dto.resp.TaxRateSum;
import com.scfs.domain.invoice.entity.InvoiceCollectPo;

@Repository
public interface InvoiceCollectPoDao {
	/**
	 * 添加数据
	 * 
	 * @param invoiceCollectPo
	 * @return
	 */
	int insert(InvoiceCollectPo invoiceCollectPo);

	/**
	 * 修改数据
	 * 
	 * @param invoiceCollectPo
	 * @return
	 */
	int updateById(InvoiceCollectPo invoiceCollectPo);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceCollectPo queryEntityById(int id);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceCollectPo queryInvoiceCollectPoById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceCollectPo> queryResultsByCon(InvoiceCollectPoReqDto reqDto, RowBounds rowBounds);

	List<InvoiceCollectPo> queryResultsByCon(InvoiceCollectPoReqDto reqDto);

	/**
	 * 根据税率分组，查询费用税金总额
	 *
	 * @param invoice_collect_id
	 * @return
	 */
	List<TaxRateSum> queryRateSumByInvoiceCollectId(Integer invoice_collect_id);

	/**
	 * 查询采购订单商品税率个数
	 */
	int countTaxRateByInvoiceCollectId(Integer invoiceCollectId);
}
