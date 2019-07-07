package com.scfs.service.export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.export.RefundApplyLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.export.dto.req.CustomsApplySearchReqDto;
import com.scfs.domain.export.dto.req.RefundApplyLineReqDto;
import com.scfs.domain.export.dto.resp.CustomsApplyResDto;
import com.scfs.domain.export.dto.resp.RefundApplyLineResDto;
import com.scfs.domain.export.entity.CustomsApply;
import com.scfs.domain.export.entity.RefundApply;
import com.scfs.domain.export.entity.RefundApplyLine;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  
 *  File: RefundApplyLineService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日				Administrator
 *
 * </pre>
 */
@Service
public class RefundApplyLineService {
	@Autowired
	private RefundApplyLineDao refundApplyLineDao;

	@Autowired
	private RefundApplyService refundApplyService;// 退税申请

	@Autowired
	private CustomsApplyService customsApplyService;// 报关信息

	/***
	 * 获取报关申请信息
	 * 
	 * @param customsApplySearchReqDto
	 * @return
	 */
	public PageResult<CustomsApplyResDto> queryCustomsApplyResultsByCon(CustomsApplySearchReqDto customsApply) {
		RefundApply refundApply = refundApplyService.queryEntityById(customsApply.getId());
		customsApply.setUserId(ServiceSupport.getUser().getId());
		customsApply.setProjectId(refundApply.getProjectId());
		customsApply.setCustomerId(refundApply.getCustId());
		customsApply.setStatus(BaseConsts.TWO);
		customsApply.setIsReturnTax(BaseConsts.ZERO);
		return customsApplyService.queryCustomsApplyResultsByCon(customsApply);
	}

	/**
	 * 添加退税明细
	 * 
	 * @param refundApplyLine
	 * @return
	 */
	public BaseResult createRefundApplyLine(RefundApplyLineReqDto refundApplyLineReqDto) {
		BaseResult baseResult = new BaseResult();
		Integer refundApplyId = refundApplyLineReqDto.getRefundApplyId();// 退税id
		RefundApply refundApply = refundApplyService.queryEntityById(refundApplyId);
		BigDecimal sumNum = refundApply.getRefundApplyNum();
		BigDecimal sumAmount = refundApply.getRefundApplyAmount();
		BigDecimal sumTax = refundApply.getRefundApplyTax();

		List<RefundApplyLine> refundList = refundApplyLineReqDto.getRefundList();
		for (RefundApplyLine applyLine : refundList) {
			Integer customsApplyId = applyLine.getCustomsApplyId();// 报关id

			CustomsApplyResDto customsApplyRes = customsApplyService.queryCustomsApplyById(customsApplyId);
			BigDecimal customsNum = customsApplyRes.getCustomsNum();// 报关数量
			BigDecimal customsAmount = customsApplyRes.getCustomsAmount();// 报关含税金额
			BigDecimal customsTaxAmount = customsApplyRes.getCustomsTaxAmount();// 报关税额
			BigDecimal taxRate = customsApplyRes.getTaxRate();// 税率

			applyLine.setRefundApplyId(refundApplyId);
			applyLine.setApplyAmount(customsAmount);
			applyLine.setApplyNum(customsNum);
			applyLine.setApplyTax(customsTaxAmount);
			applyLine.setTaxRate(taxRate);
			applyLine.setCreateAt(new Date());
			applyLine.setCreator(ServiceSupport.getUser().getChineseName());
			applyLine.setCreatorId(ServiceSupport.getUser().getId());
			applyLine.setIsDelete(BaseConsts.ZERO);
			refundApplyLineDao.insert(applyLine);

			sumNum = DecimalUtil.add(sumNum, customsNum);
			sumAmount = DecimalUtil.add(sumAmount, customsAmount);
			sumTax = DecimalUtil.add(sumTax, customsTaxAmount);

			CustomsApply upCustomsApply = new CustomsApply();
			upCustomsApply.setId(customsApplyId);
			upCustomsApply.setIsReturnTax(BaseConsts.ONE);
			customsApplyService.updateCustomsApplyTaxById(upCustomsApply);
		}
		RefundApply upRefundApply = new RefundApply();
		upRefundApply.setId(refundApplyId);
		upRefundApply.setRefundApplyNum(sumNum);
		upRefundApply.setRefundApplyAmount(sumAmount);
		upRefundApply.setRefundApplyTax(sumTax);
		refundApplyService.updateRefundApplyById(upRefundApply);
		return baseResult;
	}

	/**
	 * 删除数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public BaseResult deleteRefundApplyLineById(RefundApplyLineReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : reqDto.getIds()) {
			RefundApplyLine refundApplyLine = refundApplyLineDao.queryEntityById(id);
			Integer refundApplyId = refundApplyLine.getRefundApplyId();
			BigDecimal applyNum = refundApplyLine.getApplyNum();// 报关数量
			BigDecimal amount = refundApplyLine.getApplyAmount();// 报关含税金额
			BigDecimal taxAmount = refundApplyLine.getApplyTax();// 报关税额

			RefundApply refundApply = refundApplyService.queryEntityById(refundApplyId);
			RefundApply upRefundApply = new RefundApply();
			upRefundApply.setId(refundApplyId);
			upRefundApply.setRefundApplyNum(DecimalUtil.subtract(refundApply.getRefundApplyNum(), applyNum));
			upRefundApply.setRefundApplyAmount(DecimalUtil.subtract(refundApplyLine.getApplyAmount(), amount));
			upRefundApply.setRefundApplyTax(DecimalUtil.subtract(refundApplyLine.getApplyTax(), taxAmount));
			refundApplyService.updateRefundApplyById(upRefundApply);

			customsApplyService.queryCustomsApplyById(refundApplyLine.getCustomsApplyId());
			CustomsApply upCustomsApply = new CustomsApply();
			upCustomsApply.setId(refundApplyLine.getCustomsApplyId());
			upCustomsApply.setIsReturnTax(BaseConsts.ZERO);
			customsApplyService.updateCustomsApplyTaxById(upCustomsApply);

			RefundApplyLine upRefundApplyLine = new RefundApplyLine();
			upRefundApplyLine.setId(id);
			upRefundApplyLine.setIsDelete(BaseConsts.ONE);
			refundApplyLineDao.updateById(upRefundApplyLine);

		}
		return baseResult;
	}

	/**
	 * 获取退税申请下明细信息
	 * 
	 * @param refundApplyId
	 * @return
	 */
	public List<RefundApplyLineResDto> queryResultsByRefundId(int refundApplyId) {
		RefundApplyLineReqDto reqDto = new RefundApplyLineReqDto();
		reqDto.setRefundApplyId(refundApplyId);
		return convertToRefundApplyLineResDtos(refundApplyLineDao.queryResultsByCon(reqDto));
	}

	/**
	 * 修改报关单进度
	 * 
	 * @param refundId
	 */
	public void updateCustomsApplyByRefundId(int refundId) {
		List<RefundApplyLineResDto> lineList = queryResultsByRefundId(refundId);
		for (RefundApplyLineResDto applyLine : lineList) {
			RefundApplyLine refundApplyLine = refundApplyLineDao.queryEntityById(applyLine.getId());
			customsApplyService.queryCustomsApplyById(refundApplyLine.getCustomsApplyId());

			CustomsApply upCustomsApply = new CustomsApply();
			upCustomsApply.setId(refundApplyLine.getCustomsApplyId());
			upCustomsApply.setIsReturnTax(BaseConsts.TWO);
			customsApplyService.updateCustomsApplyTaxById(upCustomsApply);
		}
	}

	/**
	 * 获取列表分页数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<RefundApplyLineResDto> queryRefundApplyLineResultsByCon(RefundApplyLineReqDto reqDto) {
		PageResult<RefundApplyLineResDto> pageResult = new PageResult<RefundApplyLineResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<RefundApplyLineResDto> refundApplyLineResDto = convertToRefundApplyLineResDtos(
				refundApplyLineDao.queryResultsByCon(reqDto, rowBounds));
		pageResult.setItems(refundApplyLineResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	public List<RefundApplyLineResDto> convertToRefundApplyLineResDtos(List<RefundApplyLine> result) {
		List<RefundApplyLineResDto> refundApplyLineResDto = new ArrayList<RefundApplyLineResDto>();
		if (ListUtil.isEmpty(result)) {
			return refundApplyLineResDto;
		}
		for (RefundApplyLine applyLine : result) {
			RefundApplyLineResDto refundesDto = convertToRefundApplyLineResDto(applyLine);
			refundApplyLineResDto.add(refundesDto);
		}
		return refundApplyLineResDto;
	}

	public RefundApplyLineResDto convertToRefundApplyLineResDto(RefundApplyLine model) {
		RefundApplyLineResDto result = new RefundApplyLineResDto();
		result.setId(model.getId());
		result.setRefundApplyId(model.getRefundApplyId());
		result.setCustomsApplyId(model.getCustomsApplyId());
		CustomsApply customsApply = new CustomsApply();
		customsApply.setId(model.getCustomsApplyId());
		result.setApplyNo(model.getApplyNo()); // 编号
		result.setAffiliateNo(model.getAffiliateNo());// 附属编号
		result.setApplyNum(model.getApplyNum());// 数量
		result.setApplyAmount(model.getApplyAmount());// 金额
		result.setApplyTax(model.getApplyTax());// 税额
		result.setTaxTate(model.getTaxRate());// 税率
		BigDecimal exAlgorithm = DecimalUtil.add(model.getTaxRate(), new BigDecimal(BaseConsts.ONE));
		BigDecimal exRateAmount = DecimalUtil.divide(model.getApplyAmount(), exAlgorithm).setScale(BaseConsts.TWO,
				BigDecimal.ROUND_HALF_UP);// 未税金额=含税金额/(1+税率)
		result.setExRateAmount(exRateAmount);// 未税金额
		return result;
	}
}
