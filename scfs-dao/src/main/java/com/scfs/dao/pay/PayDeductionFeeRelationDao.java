package com.scfs.dao.pay;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.pay.dto.req.PayDeductionFeeRelationReqDto;
import com.scfs.domain.pay.entity.PayDeductionFeeRelation;
import com.scfs.domain.pay.entity.PayDeductionFeeRelationModel;

public interface PayDeductionFeeRelationDao {
	int insert(PayDeductionFeeRelation payDeductionFeeRelation);

	PayDeductionFeeRelation queryEntityById(Integer id);

	PayDeductionFeeRelation queryLockEntityById(Integer id);

	int updateById(PayDeductionFeeRelation payDeductionFeeRelation);

	/**
	 * 获取列表所有信息
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	List<PayDeductionFeeRelationModel> queryResultsByCon(PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto);

	/**
	 * 获取分页数据
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @param rowBounds
	 * @return
	 */
	List<PayDeductionFeeRelationModel> queryResultsByCon(PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto,
			RowBounds rowBounds);

	List<PayDeductionFeeRelationModel> queryGroupDeductionFeeByPayOrderId(Integer payId);

	/**
	 * 根据费用Id查询
	 * 
	 * @param feeId
	 * @return
	 */
	List<PayDeductionFeeRelation> queryResultByFeeId(Integer feeId);
}