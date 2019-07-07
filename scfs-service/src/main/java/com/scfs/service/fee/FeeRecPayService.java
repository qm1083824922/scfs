package com.scfs.service.fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.fee.FeeRecPayDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.FeeRecPayReqDto;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.entity.FeeRecPay;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fee.impl.FeeServiceImpl;

/**
 * <pre>
 * 	应收应付费用
 *  File: FeeRecPayService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年11月29日				Administrator
 *
 * </pre>
 */
@Service
public class FeeRecPayService {
	@Autowired
	private FeeServiceImpl feeService;
	@Autowired
	private FeeRecPayDao feeRecPayDao;

	/**
	 * 获取未关联应付费用
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeePayByNotRecCond(QueryFeeReqDto queryFeeReqDto) {
		FeeQueryResDto resDto = feeService.detailEntityById(queryFeeReqDto.getId()).getItems();
		queryFeeReqDto.setProjectId(resDto.getProjectId());
		return feeService.queryFeePayByNotRecCond(queryFeeReqDto);
	}

	/**
	 * 获取关联应付费用
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeePayByRecCond(QueryFeeReqDto queryFeeReqDto) {
		FeeQueryResDto resDto = feeService.detailEntityById(queryFeeReqDto.getId()).getItems();
		queryFeeReqDto.setProjectId(resDto.getProjectId());
		return feeService.queryFeePayByRecCond(queryFeeReqDto);
	}

	/**
	 * 添加
	 * 
	 * @param queryFeeReqDto
	 */
	public BaseResult saveFeeRecPay(FeeRecPayReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : reqDto.getIds()) {
			FeeRecPay feeRecPay = new FeeRecPay();
			feeRecPay.setRecFeeId(reqDto.getRecFeeId());
			feeRecPay.setPayFeeId(id);
			feeRecPayDao.insert(feeRecPay);
		}
		return baseResult;
	}

	/**
	 * 修改
	 * 
	 * @param reqDto
	 * @return
	 */
	public BaseResult update(FeeRecPayReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : reqDto.getIds()) {
			FeeRecPay feeRecPay = new FeeRecPay();
			feeRecPay.setId(id);
			feeRecPay.setIsDelete(BaseConsts.ONE);
			feeRecPayDao.updateById(feeRecPay);
		}
		return baseResult;
	}
}
