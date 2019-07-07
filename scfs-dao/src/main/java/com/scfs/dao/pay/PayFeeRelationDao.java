package com.scfs.dao.pay;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.entity.PayFeeRelation;
import com.scfs.domain.pay.entity.PayFeeRelationModel;

@Repository
public interface PayFeeRelationDao {
	/**
	 * 添加数据
	 * 
	 * @param record
	 * @return
	 */
	int insert(PayFeeRelation record);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	PayFeeRelation queryPayFeeById(Integer id);

	/**
	 * 修改
	 * 
	 * @param record
	 * @return
	 */
	int updateById(PayFeeRelation record);

	/**
	 * 获取分页数据
	 * 
	 * @param payFeeRelationReqDto
	 * @param rowBounds
	 * @return
	 */
	List<PayFeeRelationModel> queryResultsByCon(PayFeeRelationReqDto payFeeRelationReqDto, RowBounds rowBounds);

	/**
	 * 获取列表所有信息
	 * 
	 * @param payFeeRelationReqDto
	 * @return
	 */
	List<PayFeeRelationModel> queryResultsByCon(PayFeeRelationReqDto payFeeRelationReqDto);

	List<PayFeeRelationModel> queryGroupFeeByPayOrderId(Integer payId);

	/**
	 * 根据付款单查询金额
	 * 
	 * @param payId
	 * @return
	 */
	List<PayFeeRelationModel> queryPayFeeByPayID(Integer payId);

}