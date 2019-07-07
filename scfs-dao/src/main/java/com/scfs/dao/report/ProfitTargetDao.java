package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.report.entity.ProfitTarget;
import com.scfs.domain.report.req.ProfitTargetReqDto;

public interface ProfitTargetDao {
	int insert(ProfitTarget profitTarget);

	int updateById(ProfitTarget profitTarget);

	int deleteById(Integer id);

	ProfitTarget queryEntityById(Integer id);

	List<ProfitTarget> queryResultsByCon(ProfitTargetReqDto reqDto, RowBounds rowBounds);

	List<ProfitTarget> queryResultsByCon(ProfitTargetReqDto reqDto);

	/**
	 * 获取月份间总和信息
	 * 
	 * @param reqDto
	 * @return
	 */
	ProfitTarget querySumByCont(ProfitTargetReqDto reqDto);
}
