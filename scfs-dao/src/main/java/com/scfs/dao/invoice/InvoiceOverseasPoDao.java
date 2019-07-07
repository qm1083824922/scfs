package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.invoice.dto.req.InvoiceOverseasPoReqDto;
import com.scfs.domain.invoice.entity.InvoiceOverseasPo;

@Repository
public interface InvoiceOverseasPoDao {
	/**
	 * 添加数据
	 * 
	 * @param InvoiceOverseasPo
	 * @return
	 */
	int insert(InvoiceOverseasPo invoiceOverseasPo);

	/**
	 * 修改
	 * 
	 * @param InvoiceOverseasPo
	 * @return
	 */
	int updateById(InvoiceOverseasPo invoiceOverseasPo);

	/**
	 * 通过id获取数据
	 * 
	 * @param id
	 * @return
	 */
	InvoiceOverseasPo queryEntityById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<InvoiceOverseasPo> queryResultsByCon(InvoiceOverseasPoReqDto reqDto, RowBounds rowBounds);

	List<InvoiceOverseasPo> queryResultsByCon(InvoiceOverseasPoReqDto reqDto);

	/**
	 * 获取所有合并数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<InvoiceOverseasPo> queryAllResultsByCon(InvoiceOverseasPoReqDto reqDto);

	List<InvoiceOverseasPo> queryAllResultsGroupByBillNo(InvoiceOverseasPoReqDto reqDto);
}
