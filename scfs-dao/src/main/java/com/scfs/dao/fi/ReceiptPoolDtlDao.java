package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.ReceiptPoolDtl;

public interface ReceiptPoolDtlDao {
	int deleteByPrimaryKey(Integer id);

	int insert(ReceiptPoolDtl record);

	int insertReceiptPool(ReceiptPoolDtl record);

	ReceiptPoolDtl selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ReceiptPoolDtl record);

	int updateByPrimaryKey(ReceiptPoolDtl record);

	/**
	 * 根据经营单位和币种查询资金明细
	 * 
	 * @param poolReqDto
	 * @return
	 */
	List<ReceiptPoolDtl> queryFundResults(FundPoolReqDto poolReqDto, RowBounds rowBounds);

	/**
	 * 根据经营单位和币种查询资金明细
	 * 
	 * @param poolReqDto
	 * @return
	 */
	List<ReceiptPoolDtl> queryFundResults(FundPoolReqDto poolReqDto);

	/**
	 * 资金池刷新删除数据
	 * 
	 * @return
	 */
	int deleteAllPoolDtl();

	int isOverasyncMaxLine(FundPoolReqDto poolReqDto);

}