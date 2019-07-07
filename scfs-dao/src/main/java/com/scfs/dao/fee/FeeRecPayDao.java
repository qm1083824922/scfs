package com.scfs.dao.fee;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.fee.entity.FeeRecPay;

@Repository
public interface FeeRecPayDao {
	/**
	 * 添加
	 * 
	 * @param feeRecPay
	 * @return
	 */
	int insert(FeeRecPay feeRecPay);

	/**
	 * 修改
	 * 
	 * @param feeRecPay
	 * @return
	 */
	int updateById(FeeRecPay feeRecPay);

	/**
	 * 根据应收费用Id进行查询
	 * 
	 * @param recFeeId
	 * @return
	 */
	List<FeeRecPay> queryByRecId(Integer recFeeId);

}
