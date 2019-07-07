package com.scfs.dao.pay;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.pay.dto.req.PayRefundRelationReqDto;
import com.scfs.domain.pay.entity.PayRefundRelation;

public interface PayRefundRelationDao {
	int deleteByPrimaryKey(Integer id);

	int insert(PayRefundRelation record);

	int insertSelective(PayRefundRelation record);

	PayRefundRelation selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(PayRefundRelation record);

	int updateByPrimaryKey(PayRefundRelation record);

	/** 查询当前付款单对应的退款的数据 **/
	List<PayRefundRelation> queryPayRefundLaResultsByCon(PayRefundRelationReqDto reqDto, RowBounds rowBounds);

	/**
	 * 获取当前付款单的所有信息
	 * 
	 * @param dto
	 * @return
	 */
	List<PayRefundRelation> queryPayRefundLaResultsByCon(PayRefundRelationReqDto dto);

	/**
	 * 根据退款的id查询
	 * 
	 * @param refundId
	 * @return
	 */
	PayRefundRelation queryPayRefundByfundId(Integer refundImId);

}
