package com.scfs.dao.pay;

import com.scfs.domain.pay.dto.req.PayAdvanceRelationReqDto;
import com.scfs.domain.pay.entity.PayAdvanceRelation;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface PayAdvanceRelationDao {
	/**
	 * 添加
	 * 
	 * @param record
	 * @return
	 */
	int insert(PayAdvanceRelation record);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	PayAdvanceRelation queryPayAdvanceById(Integer id);

	/**
	 * 修改
	 * 
	 * @param record
	 * @return
	 */
	int updateById(PayAdvanceRelation record);

	/**
	 * 获取数据分页
	 * 
	 * @return
	 */
	List<PayAdvanceRelation> queryResultsByCon(PayAdvanceRelationReqDto payAdRelationReqDto, RowBounds rowBounds);

	/**
	 * 获取所有信息
	 * 
	 * @param payPoRelationReqDto
	 * @return
	 */
	List<PayAdvanceRelation> queryResultsByCon(PayAdvanceRelationReqDto payAdRelationReqDto);

	/**
	 * 获取付款下面预收单总额
	 * 
	 * @return
	 */
	public BigDecimal sumPayAmount(Integer payId);

	/**
	 * 根据付款ID查询所有预付款
	 * 
	 * @param payId
	 * @return
	 */
	List<PayAdvanceRelation> queryPayAdvByPayId(@Param("payId") Integer payId);
}