package com.scfs.dao.export;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.export.dto.req.RefundApplyLineReqDto;
import com.scfs.domain.export.entity.RefundApplyLine;

@Repository
public interface RefundApplyLineDao {
	/**
	 * 添加数据
	 * 
	 * @param refundApplyLine
	 * @return
	 */
	int insert(RefundApplyLine refundApplyLine);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	RefundApplyLine queryEntityById(Integer id);

	/**
	 * 修改数据
	 * 
	 * @param refundApplyLine
	 * @return
	 */
	int updateById(RefundApplyLine refundApplyLine);

	/**
	 * 获取分页数据
	 * 
	 * @param refundApplyLineReqDto
	 * @param rowBounds
	 * @return
	 */
	List<RefundApplyLine> queryResultsByCon(RefundApplyLineReqDto refundApplyLineReqDto, RowBounds rowBounds);

	List<RefundApplyLine> queryResultsByCon(RefundApplyLineReqDto refundApplyLineReqDto);
}
