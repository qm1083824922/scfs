package com.scfs.dao.fi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.fi.dto.req.CopeReceiptRelReqDto;
import com.scfs.domain.fi.entity.CopeReceiptRel;

public interface CopeReceiptRelDao {
	int deleteById(Integer id);

	int insert(CopeReceiptRel record);

	CopeReceiptRel queryEntityById(Integer id);

	int updateById(CopeReceiptRel record);

	/**
	 * 分页查询列表数据
	 * 
	 * @param receiptRelReqDto
	 * @param rowBounds
	 * @return
	 */
	List<CopeReceiptRel> queryResultsByCon(CopeReceiptRelReqDto receiptRelReqDto, RowBounds rowBounds);

	/**
	 * 获取总数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	List<CopeReceiptRel> queryResultsByCon(CopeReceiptRelReqDto receiptRelReqDto);

	/**
	 * 根据应付ID查询付款单汇率
	 * 
	 * @param copeId
	 * @return
	 */
	BigDecimal queryPayRateBy(Integer copeId);

}