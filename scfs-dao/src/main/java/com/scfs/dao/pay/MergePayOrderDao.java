package com.scfs.dao.pay;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.pay.dto.req.MergePayOrderSearchReqDto;
import com.scfs.domain.pay.entity.MergePayOrder;

public interface MergePayOrderDao {
	int deleteById(Integer id);

	int insert(MergePayOrder record);

	MergePayOrder queryEntityById(Integer id);

	int updateById(MergePayOrder record);

	List<MergePayOrder> queryResultsByCon(MergePayOrderSearchReqDto mergePayOrderSearchReqDto, RowBounds rowBounds);

	List<MergePayOrder> sumMergePayOrder(MergePayOrderSearchReqDto mergePayOrderSearchReqDto);

	/**
	 * 查询总额 TODO.
	 *
	 * @param id
	 * @return
	 */
	MergePayOrder querySumById(Integer id);

	/**
	 * 查询销售总额 TODO.
	 *
	 * @param id
	 * @return
	 */
	BigDecimal querySaleAmountById(Integer id);

	/**
	 * 根据合并付款编号进行查询
	 * 
	 * @param mergePayNo
	 * @return
	 */
	MergePayOrder queryMergePayOrderByMerge(@Param("mergePayNo") String mergePayNo);

	/**
	 * 获取已完成合并付款总数量
	 * 
	 * @param projectId
	 * @return
	 */
	int queryFinishedBillCount(Integer projectId);

}