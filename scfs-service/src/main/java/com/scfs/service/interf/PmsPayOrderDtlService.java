package com.scfs.service.interf;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.interf.PmsPayOrderDtlDao;
import com.scfs.domain.api.pms.entity.PmsPayOrderDtl;
import com.scfs.domain.interf.dto.PmsPoDtlSearchReqDto;
import com.scfs.domain.result.PageResult;

/**
 * <pre>
 * 
 *  File: PmsPayOrderDtlService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */

@Service
public class PmsPayOrderDtlService {
	@Autowired
	PmsPayOrderDtlDao pmsPayOrderDtlDao;

	public PageResult<PmsPayOrderDtl> queryResultsByTitleId(PmsPoDtlSearchReqDto req) {
		PageResult<PmsPayOrderDtl> pageResult = new PageResult<PmsPayOrderDtl>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<PmsPayOrderDtl> list = pmsPayOrderDtlDao.queryResultsByTitleId(req.getTitleId(), rowBounds);
		pageResult.setItems(list);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

}
