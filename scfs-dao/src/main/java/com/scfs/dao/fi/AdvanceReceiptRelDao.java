package com.scfs.dao.fi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;

@Repository
public interface AdvanceReceiptRelDao {
	/**
	 * 添加数据
	 * 
	 * @param advanceSearchReqDto
	 * @return
	 */
	int insert(AdvanceReceiptRel advanceReceiptRel);

	/**
	 * 修改数据
	 * 
	 * @return
	 */
	int updateById(AdvanceReceiptRel advanceReceiptRel);

	/**
	 * 删除数据
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(int id);

	/**
	 * 获取详情数据
	 * 
	 * @return
	 */
	AdvanceReceiptRel queryEntityById(AdvanceReceiptRel advanceReceiptRel);

	/**
	 * 获取分页列表数据
	 * 
	 * @param advanceSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<AdvanceReceiptRel> queryResultsByCon(AdvanceSearchReqDto advanceSearchReqDto, RowBounds rowBounds);

	/**
	 * 获取总数据
	 * 
	 * @param advanceSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<AdvanceReceiptRel> queryResultsByCon(AdvanceSearchReqDto advanceSearchReqDto);

	/**
	 * 获取水单转预收总金额 TODO.
	 *
	 * @param receiptId
	 * @return
	 */
	BigDecimal querySumByReceiptId(Integer receiptId);

}
