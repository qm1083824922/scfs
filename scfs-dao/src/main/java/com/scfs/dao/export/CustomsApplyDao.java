package com.scfs.dao.export;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.export.dto.req.CustomsApplySearchReqDto;
import com.scfs.domain.export.entity.CustomsApply;

public interface CustomsApplyDao {
	int insert(CustomsApply customsApply);

	CustomsApply queryAndLockEntityById(Integer id);

	CustomsApply queryEntityById(Integer id);

	List<CustomsApply> queryResultsByCon(CustomsApplySearchReqDto customsApplySearchReqDto, RowBounds rowBounds);

	int updateById(CustomsApply customsApply);

	int updateCustomsApplyInfo(CustomsApply customsApply);

	CustomsApply sumAmountAndNumber(CustomsApplySearchReqDto customsApplySearchReqDto);

	int updatePrintNum(CustomsApply customsApply);
}