package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.fi.dto.req.PrepaidReceiptRelReqDto;
import com.scfs.domain.fi.entity.PrepaidReceiptRel;

public interface PrepaidReceiptRelDao {
	int deleteById(Integer id);

	int insert(PrepaidReceiptRel record);

	PrepaidReceiptRel queryEntityById(Integer id);

	int updateById(PrepaidReceiptRel record);

	/**
	 * 分页查询预付水单明细的数据
	 * 
	 * @param receiptRelReqDto
	 * @param rowBounds
	 * @return
	 */
	List<PrepaidReceiptRel> queryResultsByCon(PrepaidReceiptRelReqDto receiptRelReqDto, RowBounds rowBounds);

	/**
	 * 查询预付水单明细的总数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	List<PrepaidReceiptRel> queryResultsByCon(PrepaidReceiptRelReqDto receiptRelReqDto);
}