package com.scfs.dao.fi;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.ReceiptPool;

public interface ReceiptPoolDao {
	int deleteByPrimaryKey(Integer id);

	int insertSelective(ReceiptPool record);

	ReceiptPool selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ReceiptPool record);

	/**
	 * 根据条件分页查询出多条结果
	 * 
	 * @return
	 */
	List<ReceiptPool> quertReceiptPoolResultByCon(FundPoolReqDto poolReqDto, RowBounds rowBounds);

	/**
	 * 根据经营单位和币种查询资金池
	 * 
	 * @param poolReqDto
	 * @return
	 */
	ReceiptPool quertReceiptPoolResultByCon(FundPoolReqDto poolReqDto);

	/**
	 * 获取经营主体资金额度
	 * 
	 * @return
	 */
	List<ReceiptPool> queryReceiptPoolCount(FundPoolReqDto poolReqDto);

	/**
	 * 获取所有资金额度（获取实时汇率转换）
	 * 
	 * @return
	 */
	BigDecimal sumCountFundAmount(FundPoolReqDto poolReqDto);

	/**
	 * 获取统计信息
	 * 
	 * @return
	 */
	ReceiptPool sumReceiptPool(FundPoolReqDto poolReqDto);

	/**
	 * 数据刷新的删除所有数据
	 */
	int deleteAllReceiptPool();
}