package com.scfs.dao.pay;

import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.PayOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PayOrderDao {
	/**
	 * 添加数据
	 * 
	 * @param payOrder
	 * @return
	 */
	int insert(PayOrder payOrder);

	/**
	 * 查询详情
	 * 
	 * @param id
	 * @return
	 */
	PayOrder queryEntityById(Integer id);

	/**
	 * 根据付款编号查询付款单
	 * 
	 * @param payNo
	 * @return
	 */
	PayOrder queryEntityByPayNo(@Param("payNo") String payNo);

	/**
	 * 根据付款附属编号查询付款单
	 * 
	 * @param attachedNumbe
	 * @return
	 */
	PayOrder queryEntityByAttachedNumbe(@Param("attachedNumbe") String attachedNumbe);

	/***
	 * 修改
	 * 
	 * @param payOrder
	 * @return
	 */
	int updateById(PayOrder payOrder);

	/**
	 * 获取数据分页
	 * 
	 * @return
	 */
	List<PayOrder> queryResultsByCon(PayOrderSearchReqDto payOrderSearchReqDto, RowBounds rowBounds);

	/**
	 * 获取总数据
	 * 
	 * @param payOrderSearchReqDto
	 * @return
	 */
	List<PayOrder> queryResultsByCon(PayOrderSearchReqDto payOrderSearchReqDto);

	/**
	 * 获取到期的承兑汇票付款单
	 */
	List<PayOrder> queryExpireResults();

	/**
	 * 根据采购单ID获取付款ID
	 * 
	 * @param poId
	 * @return
	 */
	int queryPayIdByPoId(@Param("poId") Integer poId);

	/**
	 * 统计
	 * 
	 * @param payOrderSearchReqDto
	 * @return
	 */
	List<PayOrder> sumPayOrder(PayOrderSearchReqDto payOrderSearchReqDto);

	BigDecimal querySumByIds(@Param("ids") List<Integer> ids);

	List<PayOrder> queryListByIds(@Param("ids") List<Integer> ids);

	/**
	 * 获取总行数
	 * 
	 * @param payOrderSearchReqDto
	 * @return
	 */
	int isOverasyncMaxLine(PayOrderSearchReqDto payOrderSearchReqDto);

	Integer queryAccounIdByCon(PayOrderResDto payOrderResDto);

	/**
	 * 根据付款单id查询金额
	 * 
	 * @param id
	 * @return
	 */
	BigDecimal queryPayRecrAmount(Integer id);

	/**
	 * 根据PO单明细ID查询付款单
	 * 
	 * @param poLineId
	 * @return
	 */
	PayOrder queryPayOrderPolineID(@Param("poLineId") Integer poLineId);

	/**
	 * 根据PO单明细ID查询付款单支付类型为0 和1
	 * 
	 * @param poLineId
	 * @return
	 */
	List<PayOrder> queryPayOrderPolineIDAndType(@Param("poLineId") Integer poLineId);

	/**
	 * 根据费用ID查询资金占用金额
	 * 
	 * @param feeId
	 * @return
	 */
	BigDecimal queryFundUsedByFeeId(Integer feeId);

	/**
	 * 根据QK查询付款比例
	 * 
	 * @param qk
	 * @return
	 */
	BigDecimal queryPayRateByQk(String qk);

	/**
	 * 根据费用ID查询付款比例
	 * 
	 * @param feeId
	 * @return
	 */
	BigDecimal queryPayRateByFeeId(Integer feeId);

	/**
	 * 获取已完成付款总数量
	 * 
	 * @param projectId
	 * @return
	 */
	int queryFinishedBillCount(Integer projectId);

	/**
	 * 根据合并付款ID获取付款单
	 * 
	 * @param id
	 * @return
	 */
	List<PayOrder> queryPayOrderByMergePayId(Integer mergePayId);

	/**
	 * 刷新付款单资金池的数据
	 * 
	 * @return
	 */
	List<PayOrder> queryRefreshReceiptFund();
}