package com.scfs.service.fi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fi.VlReceiptRelDao;
import com.scfs.domain.fi.dto.req.VlReceiptRelSearchReqDto;
import com.scfs.domain.fi.entity.VlReceiptRel;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: VlReceiptRelService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator
 *
 * </pre>
 */

@Service
public class VlReceiptRelService {
	@Autowired
	VlReceiptRelDao vlReceiptRelDao;

	public void insert(VlReceiptRel vlReceiptRel) {
		if (!vlReceiptRel.getWriteOffAmount().equals(DecimalUtil.ZERO)) {
			vlReceiptRel.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
					: ServiceSupport.getUser().getChineseName());
			vlReceiptRel.setCreatorId(
					ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID : ServiceSupport.getUser().getId());
			vlReceiptRelDao.insert(vlReceiptRel);
		}
	}

	public VlReceiptRel queryEntityByReceiptAndLineId(VlReceiptRelSearchReqDto req) {
		return vlReceiptRelDao.queryEntityByReceiptAndLineId(req);
	}

	public void updateById(VlReceiptRel vlReceiptRel) {
		if (vlReceiptRel.getWriteOffAmount().equals(DecimalUtil.ZERO)) {
			vlReceiptRelDao.deleteById(vlReceiptRel.getId());
		} else {
			vlReceiptRelDao.updateById(vlReceiptRel);
		}
	}

}
