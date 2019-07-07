package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.entity.Advance;

@Repository
public interface AdvanceDao {
	/**
	 * 添加操作
	 * 
	 * @param bankReceipt
	 * @return
	 */
	int insert(Advance advance);

	/**
	 * 修改操作
	 * 
	 * @param bankReceipt
	 * @return
	 */
	int updateById(Advance advance);

	/**
	 * 获取详情
	 * 
	 * @return
	 */
	Advance queryEntityById(Advance advance);

	/**
	 * 查询和锁定
	 * 
	 * @param id
	 * @return
	 */
	Advance queryAndLockEntityById(@Param("id") int id);

	/**
	 * 获取分页列表数据
	 * 
	 * @param advanceSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<Advance> queryResultsByCon(AdvanceSearchReqDto advanceSearchReqDto, RowBounds rowBounds);

	/**
	 * 采购单需要调用，校验
	 * 
	 * @param advance
	 * @return
	 */
	List<Advance> queryPerAdvanceByProCId(Advance advance);

	/**
	 * 统计
	 * 
	 * @param advanceSearchReqDto
	 * @return
	 */
	List<Advance> sumAdvance(AdvanceSearchReqDto advanceSearchReqDto);
}
