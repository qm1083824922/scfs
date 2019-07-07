package com.scfs.dao.export;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.export.entity.CustomsApplyLine;
import com.scfs.domain.export.entity.CustomsApplyLineSum;

public interface CustomsApplyLineDao {
	int deleteById(Integer id);

	int insert(CustomsApplyLine customsApplyLine);

	CustomsApplyLine queryAndLockEntityById(Integer id);

	CustomsApplyLine queryEntityById(Integer id);

	int updateById(CustomsApplyLine customsApplyLine);

	int queryCountByCustomsApplyId(Integer customsApplyId);

	CustomsApplyLineSum querySumByCustomsApplyId(Integer customsApplyId);

	List<CustomsApplyLine> queryResultsByCustomsApplyId(Integer customsApplyId, RowBounds rowBounds);

	List<CustomsApplyLine> queryResultsByCustomsApplyId(Integer customsApplyId);

	BigDecimal queryCustomsDeclareNumByBillDtlId(Integer billDtlId);

}