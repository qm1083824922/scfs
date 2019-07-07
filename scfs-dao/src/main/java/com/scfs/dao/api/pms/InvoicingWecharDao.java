package com.scfs.dao.api.pms;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.api.pms.model.InvoicingWechar;
import com.scfs.domain.pay.dto.req.InvoicingWecharReqDto;

@Repository
public interface InvoicingWecharDao {

	int insert(InvoicingWechar invoicingWechar);

	int updateById(InvoicingWechar invoicingWechar);

	List<InvoicingWechar> queryResultsByCon(InvoicingWecharReqDto reqDto);

	/** 获取期间所有供应商 **/
	List<Integer> querySupplierIdByDate(InvoicingWecharReqDto reqDto);

	/** 通过sku获取信息 **/
	List<InvoicingWechar> queryStoreInBySkuDate(InvoicingWecharReqDto reqDto);

	/** 获取sku入库数量 **/
	BigDecimal queryStockInNum(InvoicingWecharReqDto reqDto);

	/** 获取sku昨日入库数量 **/
	BigDecimal queryYesterdayStockInNum(InvoicingWecharReqDto reqDto);

	/** 入库之外所有供应商 **/
	List<Integer> querySupplierIdOutByDate(InvoicingWecharReqDto reqDto);

	/** 入库不存在获取信息 **/
	List<InvoicingWechar> queryStoreOutBySkuDate(InvoicingWecharReqDto reqDto);

	/** 获取sku销售数量 **/
	BigDecimal queryStockOutNum(InvoicingWecharReqDto reqDto);

	/** 获取sku昨日销售数量 **/
	BigDecimal queryStoreOutByDate(InvoicingWecharReqDto reqDto);
}
