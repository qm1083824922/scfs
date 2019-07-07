package com.scfs.service.finance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.CopeReceiptRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.fi.dto.req.CopeReceiptRelReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.CopeReceiptRel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  应付管理付款水单
 *  File: CopeManageMemoService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年10月31日         Administrator
 *
 * </pre>
 */
@Service
public class CopeManageMemoService {
	@Autowired
	private BankReceiptService bankReceiptService; // 水单
	@Autowired
	private CopeReceiptRelDao copeReceiptRelDao;// 关系

	public PageResult<BankReceiptResDto> queryCopeReceiptRelByCon(CopeReceiptRelReqDto reqDto) {
		PageResult<BankReceiptResDto> result = new PageResult<BankReceiptResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<BankReceiptResDto> receiptList = new ArrayList<BankReceiptResDto>();
		List<CopeReceiptRel> relResDtos = copeReceiptRelDao.queryResultsByCon(reqDto, rowBounds);
		if (!CollectionUtils.isEmpty(relResDtos)) {// 不存在，添加应付管理
			for (CopeReceiptRel copeReceiptRel : relResDtos) {
				BankReceiptResDto bankResDto = bankReceiptService.detailBankReceiptById(copeReceiptRel.getReceiptId())
						.getItems();
				bankResDto.setWriteOffAmount(copeReceiptRel.getWriteOffAmount());
				bankResDto.setCurrencyType(copeReceiptRel.getCurrnecyType());
				bankResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						copeReceiptRel.getCurrnecyType() + ""));
				receiptList.add(bankResDto);
			}
		}
		result.setItems(receiptList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}
}
