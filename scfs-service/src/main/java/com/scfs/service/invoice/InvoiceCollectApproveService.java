package com.scfs.service.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectApproveDao;
import com.scfs.domain.invoice.dto.req.InvoiceCollectApproveSearchReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectApproveResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectApprove;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2017年3月8日.
 */
@Service
public class InvoiceCollectApproveService {
	@Autowired
	private InvoiceCollectApproveDao invoiceCollectApproveDao;

	public void createInvoiceCollectApprove(InvoiceCollectApprove invoiceCollectApprove) {
		invoiceCollectApprove.setApprover(ServiceSupport.getUser().getChineseName());
		invoiceCollectApprove.setApproverId(ServiceSupport.getUser().getId());
		invoiceCollectApprove.setCreator(ServiceSupport.getUser().getChineseName());
		invoiceCollectApprove.setCreatorId(ServiceSupport.getUser().getId());
		invoiceCollectApprove.setIsDelete(BaseConsts.ZERO);
		invoiceCollectApproveDao.insert(invoiceCollectApprove);
	}

	public PageResult<InvoiceCollectApproveResDto> queryInvoiceCollectApproveResultsByCon(
			InvoiceCollectApproveSearchReqDto invoiceCollectApproveSearchReqDto) {
		PageResult<InvoiceCollectApproveResDto> pageResult = new PageResult<InvoiceCollectApproveResDto>();
		int offSet = PageUtil.getOffSet(invoiceCollectApproveSearchReqDto.getPage(),
				invoiceCollectApproveSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, invoiceCollectApproveSearchReqDto.getPer_page());
		invoiceCollectApproveSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceCollectApproveResDto> invoiceCollectApproveResDtoList = convertToInvoiceCollectApproveResDtos(
				invoiceCollectApproveDao.queryResultsByCon(invoiceCollectApproveSearchReqDto, rowBounds));

		pageResult.setItems(invoiceCollectApproveResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(),
				invoiceCollectApproveSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(invoiceCollectApproveSearchReqDto.getPage());
		pageResult.setPer_page(invoiceCollectApproveSearchReqDto.getPer_page());
		return pageResult;
	}

	public List<InvoiceCollectApproveResDto> convertToInvoiceCollectApproveResDtos(
			List<InvoiceCollectApprove> invoiceCollectApproveList) {
		List<InvoiceCollectApproveResDto> invoiceCollectApproveResDtoList = Lists.newArrayList();
		if (null == invoiceCollectApproveList) {
			return invoiceCollectApproveResDtoList;
		}
		for (InvoiceCollectApprove invoiceCollectApprove : invoiceCollectApproveList) {
			InvoiceCollectApproveResDto invoiceCollectApproveResDto = convertToInvoiceCollectApproveResDto(
					invoiceCollectApprove);
			invoiceCollectApproveResDtoList.add(invoiceCollectApproveResDto);
		}
		return invoiceCollectApproveResDtoList;
	}

	public InvoiceCollectApproveResDto convertToInvoiceCollectApproveResDto(
			InvoiceCollectApprove invoiceCollectApprove) {
		InvoiceCollectApproveResDto invoiceCollectApproveResDto = new InvoiceCollectApproveResDto();
		if (null != invoiceCollectApprove) {
			BeanUtils.copyProperties(invoiceCollectApprove, invoiceCollectApproveResDto);
		}
		return invoiceCollectApproveResDto;
	}
}
