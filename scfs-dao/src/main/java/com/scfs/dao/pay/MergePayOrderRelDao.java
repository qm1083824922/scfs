package com.scfs.dao.pay;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.pay.dto.req.MergePayOrderRelSearchReqDto;
import com.scfs.domain.pay.entity.MergePayOrderRel;

public interface MergePayOrderRelDao {

	int deleteById(Integer id);

	int insert(MergePayOrderRel record);

	MergePayOrderRel queryEntityById(Integer id);

	int updateById(MergePayOrderRel record);

	List<MergePayOrderRel> queryResultsByMergeId(MergePayOrderRelSearchReqDto req, RowBounds rowBounds);

	List<MergePayOrderRel> queryResultsByMergeId(MergePayOrderRelSearchReqDto req);

	List<MergePayOrderRel> queryResultsByMergeId(Integer mergePayId);

	/** 根据合并付款单的ID和付款单的ID进行删除 **/
	void deleteMergePayOrdrById(MergePayOrderRel payOrderRel);
}