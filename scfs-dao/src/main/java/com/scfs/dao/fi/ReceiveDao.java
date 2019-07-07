package com.scfs.dao.fi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.RecSumModel;
import com.scfs.domain.fi.entity.Receive;

@Repository
public interface ReceiveDao {

	int deleteById(Integer id);

	int insert(Receive record);

	Receive queryEntityById(Integer id);

	int updateById(Receive record);

	List<Receive> queryResultsByCon(ReceiveSearchReqDto reqDto, RowBounds rowBounds);

	List<Receive> queryResultsByCon(ReceiveSearchReqDto reqDto);

	List<RecSumModel> querySumResultsByCon(ReceiveSearchReqDto reqDto);

	List<Receive> queryListByBillNo(@Param(value = "billType") Integer billType,
			@Param(value = "billNo") String billNo);

	List<Receive> queryResultsByConNoUser(ReceiveSearchReqDto reqDto);

	/**
	 * 获取超期未收金额，币种分组
	 * 
	 * @param reqDto
	 * @return
	 */
	List<Receive> queryResultsGroupBy(ReceiveSearchReqDto reqDto);

	/**
	 * 获取超期数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<Receive> queryResultsByOverDay(ReceiveSearchReqDto reqDto, RowBounds rowBounds);

	List<Receive> queryResultsByOverDay(ReceiveSearchReqDto reqDto);

	/**
	 * 获取总金额
	 * 
	 * @param reqDto
	 * @return
	 */
	BigDecimal queryResultsSum(ReceiveSearchReqDto reqDto);
}