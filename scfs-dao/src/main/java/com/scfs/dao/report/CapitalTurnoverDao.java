package com.scfs.dao.report;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.CapitalTurnover;
import com.scfs.domain.report.req.CapitalTurnoverReqDto;

@Repository
public interface CapitalTurnoverDao {
	/**
	 * 添加数据
	 * 
	 * @param capitalTurnover
	 * @return
	 */
	int insert(CapitalTurnover capitalTurnover);

	/**
	 * 修改数据
	 * 
	 * @param capitalTurnover
	 * @return
	 */
	int updateById(CapitalTurnover capitalTurnover);

	/**
	 * 删除数据
	 * 
	 * @param capitalTurnover
	 * @return
	 */
	int deleteById(Integer id);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	CapitalTurnover queryEntityById(Integer id);

	List<CapitalTurnover> queryResultsByCon(CapitalTurnoverReqDto capitalTurnoverReqDto, RowBounds rowBounds);

	List<CapitalTurnover> queryResultsByCon(CapitalTurnoverReqDto capitalTurnoverReqDto);

	List<CapitalTurnover> queryAllResultsByCon(CapitalTurnoverReqDto capitalTurnoverReqDto);

	/**
	 * 获取资金周转率
	 * 
	 * @param capitalTurnoverReqDto
	 * @return
	 */
	BigDecimal queryRurnoverRate(CapitalTurnoverReqDto capitalTurnoverReqDto);
}
