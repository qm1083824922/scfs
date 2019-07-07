package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.logistics.dto.req.VeriAdvanceSearchReqDto;
import com.scfs.domain.logistics.entity.VerificationAdvance;

@Repository
public interface VerificationAdvanceDao {

	int deleteById(Integer id);

	int insert(VerificationAdvance record);

	VerificationAdvance queryEntityById(Integer id);

	int updateById(VerificationAdvance record);

	List<VerificationAdvance> queryResultByBillDeliveryId(Integer billDeliveryId);

	List<VerificationAdvance> queryResultsByCon(VeriAdvanceSearchReqDto req);

	BigDecimal querySumByBillDeliveryId(Integer billDeliveryId);

}