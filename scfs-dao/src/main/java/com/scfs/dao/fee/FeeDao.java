package com.scfs.dao.fee;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeSumModel;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;

@Repository
public interface FeeDao {
	int insert(Fee fee);

	int updateById(Fee fee);

	int deleteById(Fee fee);

	int submitById(Fee fee);

	List<Fee> queryFeeByCond(QueryFeeReqDto queryFeeReqDto, RowBounds rowBounds);

	List<Fee> queryFeeByCond(QueryFeeReqDto queryFeeReqDto);

	List<FeeSumModel> queryFeeSumByCond(QueryFeeReqDto queryFeeReqDto);

	Fee queryEntityById(Integer id);

	int updatePrintNum(Fee fee);

	List<Fee> selectNotByCon(InvoiceApplyManager manager);

	Integer queryCountByCond(QueryFeeReqDto queryFeeReqDto);

	List<BankReceipt> queryRefreshFeeInfo(@Param(value = "expireDate") String expireDate);

	/**
	 * 获取应收应付费用相关
	 * 
	 * @param queryFeeReqDto
	 * @param rowBounds
	 * @return
	 */
	List<Fee> queryFeePayByRecCond(QueryFeeReqDto queryFeeReqDto, RowBounds rowBounds);

	List<Fee> queryFeePayByNotRecCond(QueryFeeReqDto queryFeeReqDto, RowBounds rowBounds);
}
