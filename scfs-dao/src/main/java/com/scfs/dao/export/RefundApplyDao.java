package com.scfs.dao.export;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.export.dto.req.RefundApplySearchReqDto;
import com.scfs.domain.export.entity.RefundApply;

@Repository
public interface RefundApplyDao {
	/**
	 * 添加数据
	 * 
	 * @param refundApply
	 * @return
	 */
	int insert(RefundApply refundApply);

	int updatePrintNum(RefundApply refundApply);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	RefundApply queryEntityById(Integer id);

	/**
	 * 修改数据
	 * 
	 * @param refundApply
	 * @return
	 */
	int updateById(RefundApply refundApply);

	/**
	 * 获取分页数据
	 * 
	 * @param refundApplyReqDto
	 * @param rowBounds
	 * @return
	 */
	List<RefundApply> queryResultsByCon(RefundApplySearchReqDto refundApplyReqDto, RowBounds rowBounds);

	List<RefundApply> queryResultsByCon(RefundApplySearchReqDto refundApplyReqDto);

	List<RefundApply> sumRefundApply(RefundApplySearchReqDto refundApplyReqDto);

	/**
	 * 获取总数量
	 * 
	 * @param refundApplyReqDto
	 * @return
	 */
	int isOverasyncMaxLine(RefundApplySearchReqDto refundApplyReqDto);
}
