package com.scfs.dao.pay;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.pay.dto.req.AccountPoolReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.entity.AccountPool;
import com.scfs.domain.pay.entity.PayOrder;

@Repository
public interface AccountPoolDao {
	/**
	 * 获取资金池信息
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	public List<AccountPool> queryAccountPoolResults(AccountPoolReqDto reqDto, RowBounds rowBounds);

	public List<AccountPool> queryAccountPoolResults(AccountPoolReqDto reqDto);

	/**
	 * 添加数据
	 * 
	 * @param accountPool
	 * @return
	 */
	Integer insert(AccountPool accountPool);

	/**
	 * 修改数据
	 * 
	 * @param accountPool
	 * @return
	 */
	Integer update(AccountPool accountPool);

	Integer delete(Integer id);

	/**
	 * 获取资金池详情
	 * 
	 * @param account
	 * @return
	 */
	public AccountPool queryEntityById(Integer id);

	public AccountPool queryEntityByParam(AccountPoolReqDto reqDto);

	/**
	 * 获取原始数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public List<AccountPool> queryAccountPool(AccountPoolReqDto reqDto);

	/**
	 * 获取付款单（订单和费用类型）实际付款金额,付款手续费
	 * 
	 * @param requDto
	 * @return
	 */
	public PayOrder queryAmount(PayOrderSearchReqDto requDto);

	/**
	 * 获取类型水单金额
	 * 
	 * @return
	 */
	public BigDecimal queryReceiptAmount(AccountPoolReqDto reqDto);
}
