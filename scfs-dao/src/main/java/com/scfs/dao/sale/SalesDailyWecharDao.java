package com.scfs.dao.sale;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.sale.dto.req.SalesDailyWecharReqDto;
import com.scfs.domain.sale.entity.SalesDailyWechar;

@Repository
public interface SalesDailyWecharDao {
	int insert(SalesDailyWechar salesDailyWechar);

	int update(SalesDailyWechar salesDailyWechar);

	List<SalesDailyWechar> queryResultsByCon(SalesDailyWecharReqDto reqDto, RowBounds rowBounds);

	List<SalesDailyWechar> queryResultsByCon(SalesDailyWecharReqDto reqDto);

	SalesDailyWechar queryEntityById(Integer id);

	/**
	 * 获取销售额
	 * 
	 * @param reqDto
	 * @return
	 */
	List<SalesDailyWechar> querySaleAmount(SalesDailyWecharReqDto reqDto);

	/**
	 * 获取付款金额
	 * 
	 * @param reqDto
	 * @return
	 */
	List<SalesDailyWechar> queryPayAmount(SalesDailyWecharReqDto reqDto);

	/**
	 * 获取回款金额
	 * 
	 * @param reqDto
	 * @return
	 */
	List<SalesDailyWechar> queryPayment(SalesDailyWecharReqDto reqDto);

	/**
	 * 获取库存数量
	 * 
	 * @param reqDto
	 * @return
	 */
	BigDecimal queryStlNum(SalesDailyWecharReqDto reqDto);

	/**
	 * 获取库存金额
	 * 
	 * @param reqDto
	 * @return
	 */
	List<SalesDailyWechar> queryStlAmount(SalesDailyWecharReqDto reqDto);

	List<Integer> queryUserByRole(Integer roleId);
}
