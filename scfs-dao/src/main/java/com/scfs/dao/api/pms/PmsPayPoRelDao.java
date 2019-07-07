package com.scfs.dao.api.pms;

import java.util.List;

import com.scfs.domain.api.pms.entity.PmsPayPoRel;

public interface PmsPayPoRelDao {
	int deleteByPrimaryKey(Integer id);

	int insert(PmsPayPoRel record);

	PmsPayPoRel selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(PmsPayPoRel record);

	int updateByPrimaryKey(PmsPayPoRel record);

	String queryOrderNoByLineId(Integer lineId);

	List<PmsPayPoRel> queryPmsPayPoRelListByPmsPaySn(String paySn);

	/**
	 * 根据采购明细Id查找铺货类型的ID
	 * 
	 * @param lineId
	 * @return
	 */
	Integer queryPmsPayPoLineIdByLineId(Integer pyPoLineId);
}