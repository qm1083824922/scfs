package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.ReceiptPoolFundDtl;

public interface ReceiptPoolFundDtlDao {
	int deleteByPrimaryKey(Integer id);

	int insert(ReceiptPoolFundDtl record);

	int insertPoolDtl(ReceiptPoolFundDtl record);

	ReceiptPoolFundDtl selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ReceiptPoolFundDtl record);

	int updateByPrimaryKey(ReceiptPoolFundDtl record);

	/**
	 * 根据资金池的币种和经营单位查询资金池明细
	 * 
	 * @param poolReqDto
	 * @param rowBounds
	 * @return
	 */
	List<ReceiptPoolFundDtl> selectPoolFundResultByCon(FundPoolReqDto poolReqDto, RowBounds rowBounds);

	/**
	 * 根据资金池的币种和经营单位查询资金池明细 不带分页的
	 * 
	 * @param poolReqDto
	 * @param rowBounds
	 * @return
	 */
	List<ReceiptPoolFundDtl> selectPoolFundResultByCon(FundPoolReqDto poolReqDto);

	/**
	 * 资金池刷新删除所有数据
	 * 
	 * @return
	 */
	int deleteAllPoolFund();

	/**
	 * 融资池资金数据的合计
	 * 
	 * @param poolReqDto
	 * @return
	 */
	int isOverasyncMaxLine(FundPoolReqDto poolReqDto);

}