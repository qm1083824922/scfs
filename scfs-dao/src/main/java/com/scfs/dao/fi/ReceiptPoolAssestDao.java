package com.scfs.dao.fi;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.ReceiptPoolAssest;

public interface ReceiptPoolAssestDao {
	int deleteByPrimaryKey(Integer id);

	int insert(ReceiptPoolAssest record);

	int insertSelective(ReceiptPoolAssest record);

	ReceiptPoolAssest selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ReceiptPoolAssest record);

	int updateByPrimaryKey(ReceiptPoolAssest record);

	/**
	 * 根据经营单位和币种查询资产明细
	 * 
	 * @param poolReqDto
	 * @return
	 */
	List<ReceiptPoolAssest> queryFundPoolAssestResults(FundPoolReqDto poolReqDto, RowBounds rowBounds);

	/**
	 * 根据经营单位和币种查询资产明细
	 * 
	 * @param poolReqDto
	 * @return
	 */
	List<ReceiptPoolAssest> queryFundPoolAssestResults(FundPoolReqDto poolReqDto);

	/**
	 * 资金池刷新删除数据
	 * 
	 * @return
	 */
	int deleteAllPoolAssest();

	/**
	 * 查询融资池资产信息的总计
	 * 
	 * @param poolReqDto
	 * @return
	 */
	int isOverasyncMaxLine(FundPoolReqDto poolReqDto);
}