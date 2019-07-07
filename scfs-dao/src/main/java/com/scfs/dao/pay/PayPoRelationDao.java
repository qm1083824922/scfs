package com.scfs.dao.pay;

import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.pay.entity.PayPoRelationModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PayPoRelationDao {
	/**
	 * 添加
	 * 
	 * @param record
	 * @return
	 */
	int insert(PayPoRelation record);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	PayPoRelation queryPayPoById(Integer id);

	/**
	 * 根据付款ID查询付款与采购单列表
	 * 
	 * @param payId
	 * @return
	 */
	List<PayPoRelation> queryPayPoListByPayId(@Param("payId") Integer payId);

	/**
	 * 修改
	 * 
	 * @param record
	 * @return
	 */
	int updateById(PayPoRelation record);

	/**
	 * 获取数据分页
	 * 
	 * @return
	 */
	List<PayPoRelationModel> queryResultsByCon(PayPoRelationReqDto payPoRelationReqDto, RowBounds rowBounds);

	/**
	 * 获取所有信息
	 * 
	 * @param payPoRelationReqDto
	 * @return
	 */
	List<PayPoRelationModel> queryResultsByCon(PayPoRelationReqDto payPoRelationReqDto);

	/**
	 * 
	 * TODO. 获取该付款订单关联订单明细的商品总数
	 * 
	 * @param payOrderId
	 * @return
	 */
	BigDecimal queryTotalOrderNum(Integer payId);

	/**
	 * 物理删除 TODO.
	 *
	 * @param id
	 * @return
	 */
	Integer deleteById(Integer id);

	/**
	 * 根据POID查询付款采购关联数据
	 * 
	 * @param poId
	 * @return
	 */
	List<PayPoRelation> queryPayPoListByPoId(@Param("poId") Integer poId);

	/**
	 * 根据采购单明细id查询
	 * 
	 * @param poLineId
	 * @return
	 */
	PayPoRelation queryPayPoByPoLineId(@Param("poLineId") Integer poLineId);
	
	/**
	 * 根据采购单id查询已付款采购明细数量
	 * 
	 * @param poLineId
	 * @return
	 */
	int queryFinishedCountPayPoByPoId(@Param("poId") Integer poId);

	/**
	 * 查询订单付款总金额
	 * 
	 * @param payId
	 * @return
	 */
	BigDecimal queryTotalPayAmount(Integer payId);

	/**
	 * 通过采购单明细id获取付款总金额
	 * 
	 * @param payId
	 * @return
	 */
	BigDecimal queryTotalPayAmountByPoLineId(Integer poLineId);

	/**
	 * 根据采购单明细ID查询付款单支付类型
	 * 
	 * @param poLineId
	 * @return
	 */
	Integer queryPayWayTypeByPoLineId(Integer poLineId);
}