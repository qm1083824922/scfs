package com.scfs.service.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.finance.CopeManageDao;
import com.scfs.dao.finance.CopeManageDtlDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.finance.cope.dto.req.CopeManageReqDto;
import com.scfs.domain.finance.cope.dto.resq.CopeManageDtlResDto;
import com.scfs.domain.finance.cope.entity.CopeManage;
import com.scfs.domain.finance.cope.entity.CopeManageDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  应付管理明细
 *  File: CopeManageDtlService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年10月31日         Administrator
 *
 * </pre>
 */
@Service
public class CopeManageDtlService {
	@Autowired
	private CopeManageDtlDao copeManageDtlDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private CopeManageDao copeManageDao;

	/**
	 * 获取列表数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<CopeManageDtlResDto> queryCopeManageDtlResults(CopeManageReqDto reqDto) {
		PageResult<CopeManageDtlResDto> result = new PageResult<CopeManageDtlResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		reqDto.setUserId(ServiceSupport.getUser().getId());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<CopeManageDtlResDto> CopeManageDtlResDtos = convertToResDtos(
				copeManageDtlDao.queryResultsByCon(reqDto, rowBounds));
		result.setItems(CopeManageDtlResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	public List<CopeManageDtlResDto> convertToResDtos(List<CopeManageDtl> result) {
		List<CopeManageDtlResDto> resDtos = new ArrayList<CopeManageDtlResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtos;
		}
		for (CopeManageDtl model : result) {
			CopeManageDtlResDto resDto = convertResDto(model);
			resDtos.add(resDto);
		}
		return resDtos;
	}

	public CopeManageDtlResDto convertResDto(CopeManageDtl model) {
		CopeManageDtlResDto result = new CopeManageDtlResDto();
		result.setId(model.getId());
		result.setCopeId(model.getCopeId());
		result.setBillId(model.getBillId());
		result.setVoucherLineId(model.getVoucherLineId());
		result.setProjectId(model.getProjectId());
		result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
		result.setCustomerId(model.getCustomerId());
		result.setCustomerName(cacheService.showSubjectNameByIdAndKey(result.getCustomerId(), CacheKeyConsts.CUSTOMER));
		result.setBusiUnitId(model.getBusiUnitId());
		result.setBusiUnitName(cacheService.showSubjectNameByIdAndKey(model.getBusiUnitId(), CacheKeyConsts.BUSI_UNIT));
		result.setCurrnecyType(model.getCurrnecyType());
		result.setCurrnecyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrnecyType() + ""));
		result.setBillDate(model.getBillDate());
		result.setBillAmount(model.getBillAmount());
		result.setCopeAmount(model.getCopeAmount());
		result.setPaidAmount(model.getPaidAmount());
		result.setUnpaidAmount(model.getUnpaidAmount());
		result.setBillNumber(model.getBillNumber());
		result.setCreateAt(model.getCreateAt());
		result.setCreatorId(model.getCreatorId());
		result.setUpdateAt(model.getUpdateAt());
		return result;
	}

	/**
	 * 根据项目等条件查询核销金额大于0的数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<CopeManageDtlResDto> queryCopeManageDtlByCon(CopeManageReqDto reqDto) {
		PageResult<CopeManageDtlResDto> result = new PageResult<CopeManageDtlResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<CopeManageDtlResDto> copeManageDtlResDtos = new ArrayList<CopeManageDtlResDto>();
		List<CopeManageDtl> list = copeManageDtlDao.queryResultByProject(reqDto, rowBounds);
		if (!CollectionUtils.isEmpty(list)) {
			copeManageDtlResDtos = this.convertToResDtos(list);
		}
		result.setItems(copeManageDtlResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 根据ID查询应付明细数据
	 * 
	 * @param id
	 * @return
	 */
	public CopeManageDtlResDto queryCopeManageById(Integer id) {
		CopeManageDtlResDto copeManageDtlResDto = this.convertResDto(copeManageDtlDao.queryEntityById(id));
		return copeManageDtlResDto;
	}

	/**
	 * 回写明细的核销金额和头信息的已付金额
	 * 
	 * @param id
	 *            明细ID
	 * @param writeOffAmount
	 *            回写金额
	 * @param type
	 *            1 相减 2 相加
	 * @param copeManageDtl
	 */
	public void updateCopeMange(Integer id, BigDecimal writeOffAmount, int type) {
		CopeManageDtl copeManageDtl = new CopeManageDtl();
		CopeManageDtl copeReceiptRel = copeManageDtlDao.queryEntityById(id);
		copeManageDtl.setId(copeReceiptRel.getId());
		if (type == BaseConsts.ONE) {
			copeManageDtl.setUnpaidAmount(DecimalUtil.subtract(copeReceiptRel.getUnpaidAmount(), writeOffAmount));// 未付金额减少
			copeManageDtl.setPaidAmount(DecimalUtil.add(copeReceiptRel.getPaidAmount(), writeOffAmount));
		} else {
			copeManageDtl.setUnpaidAmount(DecimalUtil.add(copeReceiptRel.getUnpaidAmount(), writeOffAmount));// 未付金额减少
			copeManageDtl.setPaidAmount(DecimalUtil.subtract(copeReceiptRel.getPaidAmount(), writeOffAmount));
		}
		copeManageDtlDao.updateById(copeManageDtl);
		CopeManage copeManage = copeManageDao.queryEntityById(copeReceiptRel.getCopeId());
		CopeManage manage = new CopeManage();
		if (type == BaseConsts.ONE) {
			manage.setPaidAmount(DecimalUtil.add(copeManage.getPaidAmount(), writeOffAmount));
			manage.setUnpaidAmount(DecimalUtil.subtract(copeManage.getUnpaidAmount(), writeOffAmount));
		} else {
			manage.setPaidAmount(DecimalUtil.subtract(copeManage.getPaidAmount(), writeOffAmount));
			manage.setUnpaidAmount(DecimalUtil.add(copeManage.getUnpaidAmount(), writeOffAmount));
		}
		manage.setId(copeManage.getId());
		copeManageDao.updateById(manage);
	}

}
