package com.scfs.dao.pay;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.pay.dto.req.AccountPoolFundReqDto;
import com.scfs.domain.pay.entity.AccountPoolFund;

@Repository
public interface AccountPoolFundDao {
	/**
	 * 获取资金池信息
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	public List<AccountPoolFund> queryAccountPoolResults(AccountPoolFundReqDto reqDto, RowBounds rowBounds);

	public List<AccountPoolFund> queryAccountPoolResults(AccountPoolFundReqDto reqDto);

	/**
	 * 添加数据
	 * 
	 * @param accountPool
	 * @return
	 */
	Integer insert(AccountPoolFund accountPoolFund);

	Integer delete(Integer id);

	Integer deleteByPoolId(Integer poolId);

}
