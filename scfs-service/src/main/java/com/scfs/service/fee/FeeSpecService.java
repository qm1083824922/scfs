package com.scfs.service.fee;

import java.util.List;

import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.FeeSpecSearchReqDto;
import com.scfs.domain.fee.dto.resp.FeeSpecResDto;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;

/**
 * <pre>
 * 
 *  File: FeeSpecService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日				Administrator
 *
 * </pre>
 */
public interface FeeSpecService {
	List<FeeSpec> queryAllFeeSpec(FeeSpecSearchReqDto req);

	public PageResult<FeeSpecResDto> queryFeeSpecResultsByCon(FeeSpecSearchReqDto req);

	public int createFeeSpec(FeeSpec feeSpec);

	public BaseResult updateFeeSpecById(FeeSpec feeSpec);

	public Result<FeeSpecResDto> editFeeSpecById(FeeSpec feeSpec);

	public BaseResult deleteFeeSpecById(FeeSpec feeSpec);
}
