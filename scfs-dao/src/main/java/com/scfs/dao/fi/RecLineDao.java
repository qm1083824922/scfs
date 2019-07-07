package com.scfs.dao.fi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.entity.RecLine;

@Repository
public interface RecLineDao {

	int deleteById(Integer id);

	int insert(RecLine record);

	RecLine queryEntityById(Integer id);

	int updateById(RecLine recLine);

	List<RecLine> queryResultsByRecId(RecLineSearchReqDto req, RowBounds rowBounds);

	List<RecLine> queryListByRecId(RecLineSearchReqDto req);

	RecLine queryRecLineByCon(RecLineSearchReqDto req);

	List<RecLine> queryResultsRecLineByCon(RecLineSearchReqDto req);

	/**
	 * 1.费用单 2.入库单 3.出库单 4.付款单 5.收票单 6.开票单 7.水单单 TODO. 根据单据id查询应收总额
	 * 
	 * @param billType
	 * @param billId
	 * @return
	 */
	BigDecimal querySumByBillNo(@Param(value = "billType") Integer billType, @Param(value = "billNo") String billNo);

	/**
	 * 根据虚拟水单查询应收明细
	 * 
	 * @param receiptNo
	 * @return
	 */
	RecLine queryRecLineByVirtualReceiptNo(String receiptNo);

}