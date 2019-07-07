package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.BaseExchangeRateReqDto;
import com.scfs.domain.base.entity.BaseExchangeRate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BaseExchangeRateDao {

	/**
	 * 查询汇率汇总列表
	 * 
	 * @return
	 */
	List<BaseExchangeRate> getUseExchangeRateList(BaseExchangeRateReqDto baseExchangeRateReqDto, RowBounds rowBounds);

	/**
	 * 查询历史汇率列表
	 * 
	 * @return
	 */
	List<BaseExchangeRate> getHisExchangeRateList(BaseExchangeRateReqDto baseExchangeRateReqDto, RowBounds rowBounds);

	/**
	 * 根据id查找汇率
	 * 
	 * @param id
	 * @return
	 */
	BaseExchangeRate queryBaseExchangeRateById(int id);

	/**
	 * 根据 bank, currency, foreign_currency 查询汇率信息
	 * 
	 * @param baseExchangeRate
	 * @return
	 */
	BaseExchangeRate queryExchangeRateByExchangeRate(BaseExchangeRate baseExchangeRate);

	BaseExchangeRate queryExchangeRateByRateReqDto(BaseExchangeRateReqDto baseExchangeRateReqDto);

	/**
	 * 新增汇率
	 * 
	 * @param baseExchangeRate
	 * @return
	 */
	int insert(BaseExchangeRate baseExchangeRate);

	/**
	 * 更新汇率
	 * 
	 * @param baseExchangeRate
	 * @return
	 */
	int updateById(BaseExchangeRate baseExchangeRate);

	/**
	 * 标记所有汇率异常
	 * 
	 * @return
	 */
	int updateAllExcption(@Param("bank") String bank);

	/**
	 * 备份汇率
	 * 
	 * @param baseExchangeRate
	 * @return
	 */
	int backUpExchangeRate(BaseExchangeRate baseExchangeRate);

}
