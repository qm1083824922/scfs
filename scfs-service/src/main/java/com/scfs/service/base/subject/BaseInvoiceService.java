package com.scfs.service.base.subject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseInvoiceDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.subject.dto.req.AddInvoiceDto;
import com.scfs.domain.base.subject.dto.req.QueryInvoiceReqDto;
import com.scfs.domain.base.subject.dto.resp.QueryInvoiceResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: BaseInvoiceServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Service
public class BaseInvoiceService {

	@Autowired
	private BaseInvoiceDao baseInvoiceDao;

	public BaseInvoice loadAndLockEntityById(int id) {
		BaseInvoice obj = baseInvoiceDao.loadAndLockEntityById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, baseInvoiceDao.getClass(), id);
		}
		return obj;
	}

	public void addBaseInvoice(AddInvoiceDto addInvoiceDto) {

		addInvoiceDto.setCreator(ServiceSupport.getUser().getChineseName());
		addInvoiceDto.setState(BaseConsts.ONE);
		int result = baseInvoiceDao.insertBaseInvoice(addInvoiceDto);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(addInvoiceDto));
		}
	}

	public BaseInvoice queryInvoiceById(Integer id) {
		BaseInvoice obj = baseInvoiceDao.queryInvoiceById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, baseInvoiceDao.getClass(), id);
		}
		return obj;
	}

	public PageResult<QueryInvoiceResDto> queryInvoiceBySubjectId(QueryInvoiceReqDto queryInvoiceReqDto) {
		PageResult<QueryInvoiceResDto> pageResult = new PageResult<QueryInvoiceResDto>();
		int offSet = PageUtil.getOffSet(queryInvoiceReqDto.getPage(), queryInvoiceReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryInvoiceReqDto.getPer_page());
		List<QueryInvoiceResDto> queryInvoiceResDtos = convertToResult(
				baseInvoiceDao.queryInvoiceBySubjectId(queryInvoiceReqDto, rowBounds),
				queryInvoiceReqDto.getSubjectType());
		pageResult.setItems(queryInvoiceResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryInvoiceReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryInvoiceReqDto.getPage());
		pageResult.setPer_page(queryInvoiceReqDto.getPer_page());
		return pageResult;
	}

	private List<QueryInvoiceResDto> convertToResult(List<BaseInvoice> baseInvoices, int subjectType) {
		List<QueryInvoiceResDto> queryInvoiceResDtos = new ArrayList<QueryInvoiceResDto>();
		if (CollectionUtils.isEmpty(baseInvoices)) {
			return queryInvoiceResDtos;
		}
		for (BaseInvoice baseInvoice : baseInvoices) {
			// 操作集合
			QueryInvoiceResDto queryInvoiceResDto = new QueryInvoiceResDto();
			queryInvoiceResDto.setAccountNo(baseInvoice.getAccountNo());
			queryInvoiceResDto.setAddress(baseInvoice.getAddress());
			queryInvoiceResDto.setBankName(baseInvoice.getBankName());
			queryInvoiceResDto.setCreatAt(baseInvoice.getCreateAt());
			queryInvoiceResDto.setCreator(baseInvoice.getCreator());
			queryInvoiceResDto.setPhoneNumber(baseInvoice.getPhoneNumber());
			queryInvoiceResDto.setSubjectId(baseInvoice.getSubjectId());
			queryInvoiceResDto.setTaxPayer(baseInvoice.getTaxPayer());
			queryInvoiceResDto.setId(baseInvoice.getId());

			queryInvoiceResDto.setState(baseInvoice.getState());
			queryInvoiceResDto.setStateLabel(
					ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_STATE_TYPE, baseInvoice.getState() + ""));

			queryInvoiceResDtos.add(queryInvoiceResDto);
		}
		return queryInvoiceResDtos;
	}

	public void updateBaseInvoice(BaseInvoice baseInvoice) {

		int result = baseInvoiceDao.updateBaseInvoiceById(baseInvoice);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(baseInvoice));
		}

	}

	public void invalidBaseInvoiceByIds(List<Integer> ids) {
		for (Integer id : ids) {
			invalidBaseInvoiceById(id);
		}
	}

	public void invalidBaseInvoiceById(Integer id) {
		BaseInvoice baseInvoice = loadAndLockEntityById(id);
		baseInvoice.setDeleteAt(new Date());
		baseInvoice.setDeleter(ServiceSupport.getUser().getChineseName());
		baseInvoice.setState(BaseConsts.TWO);
		int result = baseInvoiceDao.invalidInvoiceById(baseInvoice);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "作废失败:" + JSONObject.toJSON(baseInvoice));
		}
	}

}
