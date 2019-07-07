package com.scfs.service.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.GoodsReportDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.report.entity.GoodsInRepot;
import com.scfs.domain.report.entity.GoodsOutReport;
import com.scfs.domain.report.entity.GoodsPlReport;
import com.scfs.domain.report.entity.GoodsReport;
import com.scfs.domain.report.entity.GoodsRtReport;
import com.scfs.domain.report.entity.GoodsStlReport;
import com.scfs.domain.report.req.GoodsReportReqDto;
import com.scfs.domain.report.resp.GoodsReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: GoodsReportService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
  *  2017年08月31日				Administrator
 *
 * </pre>
 */
@Service
public class GoodsReportService {
	@Autowired
	private GoodsReportDto goodsReportDto;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BaseGoodsDao baseGoodsDao;

	/**
	 * 查询列表信息
	 * 
	 * @param reportReqDto
	 * @return
	 * @throws ParseException
	 */
	public PageResult<GoodsReportResDto> queryResultByCon(GoodsReportReqDto reportReqDto) throws ParseException {
		// 封装查询条件
		// reportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_16));
		// 获取查询的条件
		reportReqDto.setUserId(ServiceSupport.getUser().getId());
		// reportReqDto.setStatisticsDimension(this.returnStatisticsType(reportReqDto));
		reportReqDto.setStatisticsDimension(BaseConsts.SEVEN);
		PageResult<GoodsReportResDto> result = new PageResult<GoodsReportResDto>();
		int offSet = PageUtil.getOffSet(reportReqDto.getPage(), reportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reportReqDto.getPer_page());
		// 得到当前所有数据
		List<GoodsReport> goodsReList = goodsReportDto.queryResultsByCon(reportReqDto, rowBounds);
		if (reportReqDto.getNeedSum() != null && reportReqDto.getNeedSum().equals(BaseConsts.ONE)) {
			BigDecimal sumBeforeNum = BigDecimal.ZERO;
			BigDecimal sumInNum = BigDecimal.ZERO;
			BigDecimal sumSaleNum = BigDecimal.ZERO;
			BigDecimal sumReNum = BigDecimal.ZERO;
			BigDecimal sumAfterNum = BigDecimal.ZERO;
			BigDecimal sumPlNum = BigDecimal.ZERO;
			for (GoodsReport goodsReport : goodsReList) {
				sumBeforeNum = DecimalUtil.add(sumBeforeNum,
						goodsReport.getBeforeNumber() == null ? BigDecimal.ZERO : goodsReport.getBeforeNumber());
				sumInNum = DecimalUtil.add(sumInNum,
						goodsReport.getInNumber() == null ? BigDecimal.ZERO : goodsReport.getInNumber());
				sumSaleNum = DecimalUtil.add(sumSaleNum,
						goodsReport.getSaleNumber() == null ? BigDecimal.ZERO : goodsReport.getSaleNumber());
				sumReNum = DecimalUtil.add(sumReNum,
						goodsReport.getReturnNumber() == null ? BigDecimal.ZERO : goodsReport.getReturnNumber());
				sumAfterNum = DecimalUtil.add(sumAfterNum,
						goodsReport.getAfterNumber() == null ? BigDecimal.ZERO : goodsReport.getAfterNumber());
				sumPlNum = DecimalUtil.add(sumPlNum,
						goodsReport.getPleaseNumber() == null ? BigDecimal.ZERO : goodsReport.getPleaseNumber());
			}
			result.setTotalStr("合计：期初总数量：" + DecimalUtil.toQuantityString(sumBeforeNum) + "&nbsp;入库总数量："
					+ DecimalUtil.toQuantityString(sumInNum) + "&nbsp;" + "&nbsp;销售总数量："
					+ DecimalUtil.toQuantityString(sumSaleNum) + " &nbsp;" + "&nbsp;退货总数量："
					+ DecimalUtil.toQuantityString(sumReNum) + "&nbsp;期末总数量："
					+ DecimalUtil.toQuantityString(sumAfterNum) + "&nbsp;请款总数量"
					+ DecimalUtil.toQuantityString(sumPlNum));
		}
		List<GoodsReportResDto> resDtos = this.convertToResults(goodsReList);
		if (!CollectionUtils.isEmpty(resDtos)) {
			for (int i = 0; i < resDtos.size(); i++) {
				GoodsReportResDto goodsResDto = resDtos.get(i);
				boolean isWarn = false;
				reportReqDto.setGoodsCode(goodsResDto.getGoodsCode());
				reportReqDto.setProjectId(goodsResDto.getProjectId());
				reportReqDto.setSupplierId(goodsResDto.getSupplierId());
				reportReqDto.setCustomerId(goodsResDto.getCustomerId());
				List<GoodsInRepot> goodsInList = this.queryAllGoodsPmsInReportExport(reportReqDto);
				for (GoodsInRepot goodsInRepot : goodsInList) {
					if (goodsInRepot.getStlAge() >= BaseConsts.INT_60) {
						isWarn = true;
					}
				}
				goodsResDto.setWarn(isWarn);// 标红显示
				if (reportReqDto.getNeedOver() != null) {// 是否超期
					if (reportReqDto.getNeedOver().equals(BaseConsts.ONE)) {
						if (!isWarn) {
							resDtos.remove(i);
							i = i - 1;
						}
					} else {
						if (isWarn) {
							resDtos.remove(i);
							i = i - 1;
						}
					}
				}
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reportReqDto.getPer_page());
		result.setItems(resDtos);
		result.setTotal(CountHelper.getTotalRow());
		result.setLast_page(totalPage);
		result.setCurrent_page(reportReqDto.getPage());
		result.setPer_page(reportReqDto.getPer_page());
		return result;
	}

	/**
	 * 确定当前查询的条件
	 * 
	 * @param reportReqDto
	 * @return
	 */
	@SuppressWarnings("unused")
	private Integer returnStatisticsType(GoodsReportReqDto reportReqDto) {
		Integer statisType = BaseConsts.ZERO;
		if (reportReqDto.getProjectId() != null) {
			statisType = BaseConsts.ONE;
		}
		if (reportReqDto.getSupplierId() != null) {
			statisType = BaseConsts.TWO;
		}
		if (reportReqDto.getCustomerId() != null) {
			statisType = BaseConsts.THREE;
		}
		if (reportReqDto.getProjectId() != null && reportReqDto.getSupplierId() != null) {
			statisType = BaseConsts.FOUR;
		}
		if (reportReqDto.getProjectId() != null && reportReqDto.getCustomerId() != null) {
			statisType = BaseConsts.FIVE;
		}
		if (reportReqDto.getSupplierId() != null && reportReqDto.getCustomerId() != null) {
			statisType = BaseConsts.SIX;
		}
		if (reportReqDto.getSupplierId() != null && reportReqDto.getCustomerId() != null
				&& reportReqDto.getProjectId() != null) {
			statisType = BaseConsts.SEVEN;
		}
		return statisType;
	}

	/**
	 * 查询商品详情
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public PageResult<GoodsReportResDto> queryGoodsDetail(GoodsReportReqDto goodsReportReqDto) {

		PageResult<GoodsReportResDto> result = new PageResult<GoodsReportResDto>();
		// 数据校验
		if (goodsReportReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "传输数据为空");
		}
		// 前端将编号的数据赋值到ID上面
		if (goodsReportReqDto.getGoodsCode() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号为空");
		}
		// 根据商品编号查询
		BaseGoods goods = baseGoodsDao.queryGoodsByNumber(goodsReportReqDto.getGoodsCode());
		if (goods == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品详情数据为空");
		}
		List<GoodsReportResDto> goodsReportResDto = this.converToGoods(goodsReportReqDto, goods);
		result.setItems(goodsReportResDto);
		return result;
	}

	/**
	 * 根据当前条件查询入库明细数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 * @throws ParseException
	 */
	public PageResult<GoodsInRepot> queryPmsInDetailByCon(GoodsReportReqDto goodsReportReqDto) throws ParseException {
		PageResult<GoodsInRepot> result = new PageResult<GoodsInRepot>();
		goodsReportReqDto.setUserId(ServiceSupport.getUser().getId());
		// 校验当前数据
		this.checkData(goodsReportReqDto);
		List<GoodsInRepot> inDtoResults = goodsReportDto.queryPmsInBySku(goodsReportReqDto);
		result.setItems(inDtoResults);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), goodsReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(inDtoResults.size());
		result.setCurrent_page(goodsReportReqDto.getPage());
		result.setPer_page(goodsReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 根据当前条件查询出库单明细数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public PageResult<GoodsOutReport> queryPmsOutDetail(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsOutReport> result = new PageResult<GoodsOutReport>();
		int offSet = PageUtil.getOffSet(goodsReportReqDto.getPage(), goodsReportReqDto.getPer_page());
		goodsReportReqDto.setUserId(ServiceSupport.getUser().getId());
		RowBounds rowBounds = new RowBounds(offSet, goodsReportReqDto.getPer_page());
		// 校验当前数据
		this.checkData(goodsReportReqDto);
		List<GoodsOutReport> goodsOutReports = goodsReportDto.queryPmsOutReport(goodsReportReqDto, rowBounds);
		if (!CollectionUtils.isEmpty(goodsOutReports)) {
			for (GoodsOutReport goodsOutReport : goodsOutReports) {
				String currencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
						goodsOutReport.getCurrencyType());
				goodsOutReport.setCurrencyName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, currencyType));
			}
		}
		result.setItems(goodsOutReports);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), goodsReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(goodsReportReqDto.getPage());
		result.setPer_page(goodsReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 根据当前条件查询请款单单明细数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public PageResult<GoodsPlReport> queryPmsPlDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsPlReport> result = new PageResult<GoodsPlReport>();
		goodsReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(goodsReportReqDto.getPage(), goodsReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, goodsReportReqDto.getPer_page());
		// 校验当前数据
		this.checkData(goodsReportReqDto);
		List<GoodsPlReport> goodsPlReports = goodsReportDto.queryPmsPlReport(goodsReportReqDto, rowBounds);
		if (!CollectionUtils.isEmpty(goodsPlReports)) {
			for (GoodsPlReport goodsPlReport : goodsPlReports) {
				goodsPlReport.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						goodsPlReport.getCurrencyType() + ""));
				goodsPlReport.setRealCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, goodsPlReport.getRealCurrencyType() + ""));
			}
		}
		result.setItems(goodsPlReports);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), goodsReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(goodsReportReqDto.getPage());
		result.setPer_page(goodsReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 根据当前条件查询退货明细数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public PageResult<GoodsRtReport> queryPmsRTDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsRtReport> result = new PageResult<GoodsRtReport>();
		goodsReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(goodsReportReqDto.getPage(), goodsReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, goodsReportReqDto.getPer_page());
		// 校验当前数据
		this.checkData(goodsReportReqDto);
		List<GoodsRtReport> goodsRtReports = goodsReportDto.queryPmsRtReport(goodsReportReqDto, rowBounds);
		if (!CollectionUtils.isEmpty(goodsRtReports)) {
			for (GoodsRtReport rtReport : goodsRtReports) {
				rtReport.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						rtReport.getCurrencyId() + ""));
				rtReport.setOrderTypeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.PO_TYPE, rtReport.getOrderType() + ""));
			}
		}
		result.setItems(goodsRtReports);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), goodsReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(goodsReportReqDto.getPage());
		result.setPer_page(goodsReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 根据当前条件查询库存数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public PageResult<GoodsStlReport> queryStlDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsStlReport> result = new PageResult<GoodsStlReport>();
		goodsReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(goodsReportReqDto.getPage(), goodsReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, goodsReportReqDto.getPer_page());
		// 校验当前数据
		this.checkData(goodsReportReqDto);
		List<GoodsStlReport> goodsStlReports = goodsReportDto.queryStlReport(goodsReportReqDto, rowBounds);
		if (!CollectionUtils.isEmpty(goodsStlReports)) {
			for (GoodsStlReport stlReport : goodsStlReports) {
				stlReport.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						stlReport.getCurrencyId() + ""));
				stlReport.setProjectName(cacheService.getProjectNameById(goodsReportReqDto.getProjectId()));
				stlReport.setCustomerName(cacheService.getSubjectNcByIdAndKey(goodsReportReqDto.getCustomerId(),
						CacheKeyConsts.CUSTOMER));
				stlReport.setSupplierName(cacheService.getSubjectNcByIdAndKey(goodsReportReqDto.getSupplierId(),
						CacheKeyConsts.SUPPLIER));
			}
		}
		result.setItems(goodsStlReports);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), goodsReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(goodsReportReqDto.getPage());
		result.setPer_page(goodsReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 导出铺货进销存报表
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public List<GoodsReportResDto> queryAllGoodsReportExport(GoodsReportReqDto goodsReportReqDto) {
		// 封装查询条件
		// goodsReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_16));
		// 获取查询的条件
		goodsReportReqDto.setStatisticsDimension(BaseConsts.SEVEN);
		// 得到当前所有数据
		goodsReportReqDto.setUserId(ServiceSupport.getUser().getId());
		List<GoodsReport> goodsReList = goodsReportDto.queryResultsByCon(goodsReportReqDto);
		List<GoodsReportResDto> resDtos = this.convertToResults(goodsReList);
		return resDtos;
	}

	/**
	 * 导出所有入库信息
	 * 
	 * @param goodsReportReqDto
	 * @return
	 * @throws ParseException
	 */
	public List<GoodsInRepot> queryAllGoodsPmsInReportExport(GoodsReportReqDto goodsReportReqDto)
			throws ParseException {
		goodsReportReqDto = getGoodsCodes(queryAllGoodsReportExport(goodsReportReqDto), goodsReportReqDto);
		List<GoodsInRepot> resDtos = goodsReportDto.queryPmsInBySku(goodsReportReqDto);
		if (CollectionUtils.isNotEmpty(resDtos)) {
			for (GoodsInRepot model : resDtos) {
				if (model.getProjectId() != null) {
					model.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
				}
				if (model.getCustomerId() != null) {
					model.setCustomerName(
							cacheService.getSubjectNcByIdAndKey(model.getCustomerId(), CacheKeyConsts.CUSTOMER));
				}
				if (model.getSupplierId() != null) {
					model.setSupplierName(
							cacheService.getSubjectNcByIdAndKey(model.getSupplierId(), CacheKeyConsts.SUPPLIER));
				}
			}
		}
		return resDtos;
	}

	/**
	 * 导出所有出库信息
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public List<GoodsOutReport> queryAllGoodsPmsOutReportExport(GoodsReportReqDto goodsReportReqDto) {
		goodsReportReqDto = getGoodsCodes(queryAllGoodsReportExport(goodsReportReqDto), goodsReportReqDto);
		List<GoodsOutReport> resDtos = goodsReportDto.queryPmsOutReport(goodsReportReqDto);
		if (CollectionUtils.isNotEmpty(resDtos)) {
			for (GoodsOutReport model : resDtos) {
				String currencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
						model.getCurrencyType());
				model.setCurrencyName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, currencyType));
				if (model.getProjectId() != null) {
					model.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
				}
				if (model.getCustomerId() != null) {
					model.setCustomerName(
							cacheService.getSubjectNcByIdAndKey(model.getCustomerId(), CacheKeyConsts.CUSTOMER));
				}
				if (model.getSupplierId() != null) {
					model.setSupplierName(
							cacheService.getSubjectNcByIdAndKey(model.getSupplierId(), CacheKeyConsts.SUPPLIER));
				}
			}
		}
		return resDtos;
	}

	/**
	 * 导出所有请款信息
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public List<GoodsPlReport> queryAllGoodsPleaseReportExport(GoodsReportReqDto goodsReportReqDto) {
		goodsReportReqDto = getGoodsCodes(queryAllGoodsReportExport(goodsReportReqDto), goodsReportReqDto);
		List<GoodsPlReport> resDtos = goodsReportDto.queryPmsPlReport(goodsReportReqDto);
		if (CollectionUtils.isNotEmpty(resDtos)) {
			for (GoodsPlReport model : resDtos) {
				model.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						model.getCurrencyType() + ""));
				model.setRealCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						model.getRealCurrencyType() + ""));
				if (model.getProjectId() != null) {
					model.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
				}
				if (model.getCustomerId() != null) {
					model.setCustomerName(
							cacheService.getSubjectNcByIdAndKey(model.getCustomerId(), CacheKeyConsts.CUSTOMER));
				}
				if (model.getSupplierId() != null) {
					model.setSupplierName(
							cacheService.getSubjectNcByIdAndKey(model.getSupplierId(), CacheKeyConsts.SUPPLIER));
				}
			}
		}
		return resDtos;
	}

	/**
	 * 获取导出退货信息
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	public List<GoodsRtReport> queryAllGoodsReturnReportExport(GoodsReportReqDto goodsReportReqDto) {
		goodsReportReqDto = getGoodsCodes(queryAllGoodsReportExport(goodsReportReqDto), goodsReportReqDto);
		List<GoodsRtReport> resDtos = goodsReportDto.queryPmsRtReport(goodsReportReqDto);
		if (CollectionUtils.isNotEmpty(resDtos)) {
			for (GoodsRtReport model : resDtos) {
				model.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						model.getCurrencyId() + ""));
				model.setOrderTypeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.PO_TYPE, model.getOrderType() + ""));
				if (model.getProjectId() != null) {
					model.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
				}
				if (model.getCustomerId() != null) {
					model.setCustomerName(
							cacheService.getSubjectNcByIdAndKey(model.getCustomerId(), CacheKeyConsts.CUSTOMER));
				}
				if (model.getSupplierId() != null) {
					model.setSupplierName(
							cacheService.getSubjectNcByIdAndKey(model.getSupplierId(), CacheKeyConsts.SUPPLIER));
				}
			}
		}
		return resDtos;
	}

	/**
	 * 所有所有商品编号
	 * 
	 * @param resDtos
	 * @return
	 */
	private GoodsReportReqDto getGoodsCodes(List<GoodsReportResDto> resDtos, GoodsReportReqDto result) {
		List<String> goodsCodes = null;
		List<Integer> supplierIds = null;
		List<Integer> customerIds = null;
		List<Integer> projectIds = null;
		if (CollectionUtils.isNotEmpty(resDtos)) {
			goodsCodes = new ArrayList<String>();
			supplierIds = new ArrayList<Integer>();
			customerIds = new ArrayList<Integer>();
			projectIds = new ArrayList<Integer>();
			for (GoodsReportResDto model : resDtos) {
				goodsCodes.add(model.getGoodsCode());
				supplierIds.add(model.getSupplierId());
				customerIds.add(model.getCustomerId());
				projectIds.add(model.getProjectId());
			}
		}
		result.setGoodsCodeList(goodsCodes);
		result.setSupplierIdList(supplierIds);
		result.setCustomerIdList(customerIds);
		result.setProjectIdList(projectIds);
		result.setGoodsCode(null);
		result.setSupplierId(null);
		result.setCustomerId(null);
		result.setProjectId(null);
		return result;
	}

	/**
	 * 校验当前数据
	 * 
	 * @param goodsReportReqDto
	 */
	private void checkData(GoodsReportReqDto goodsReportReqDto) {
		if (goodsReportReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查询条件为空,请刷新页面");
		}
		if (StringUtils.isEmpty(goodsReportReqDto.getGoodsCode())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号为空,请重新操作");
		}
		if (goodsReportReqDto.getStartCheckDate() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "期初时间为空,请重新操作");
		}
		if (goodsReportReqDto.getEndCheckDate() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "期末时间为空,请重新操作");
		}
	}

	/**
	 * 封装当前商品详情的数据
	 * 
	 * @param goodsReportReqDto
	 * @param goods
	 * @return
	 */
	private List<GoodsReportResDto> converToGoods(GoodsReportReqDto goodsReportReqDto, BaseGoods goods) {
		List<GoodsReportResDto> list = new ArrayList<GoodsReportResDto>();
		if (goods != null) {
			GoodsReportResDto goodsReportResDto = new GoodsReportResDto();
			if (goodsReportReqDto.getProjectId() != null) {
				goodsReportResDto.setProjectName(cacheService.getProjectNameById(goodsReportReqDto.getProjectId()));
				goodsReportResDto.setProjectId(goodsReportReqDto.getProjectId());
			}
			if (goodsReportReqDto.getCustomerId() != null) {
				goodsReportResDto.setCustomerName(cacheService.getSubjectNcByIdAndKey(goodsReportReqDto.getCustomerId(),
						CacheKeyConsts.CUSTOMER));
				goodsReportResDto.setCustomerId(goodsReportReqDto.getCustomerId());
			}
			if (goodsReportReqDto.getSupplierId() != null) {
				goodsReportResDto.setSupplierName(cacheService.getSubjectNcByIdAndKey(goodsReportReqDto.getSupplierId(),
						CacheKeyConsts.SUPPLIER));
				goodsReportResDto.setSupplierId(goodsReportReqDto.getSupplierId());
			}
			goodsReportResDto.setGoodsCode(goods.getNumber());
			goodsReportResDto.setGoodsName(goods.getName());
			goodsReportResDto.setBarCode(goods.getBarCode());
			goodsReportResDto.setType(goods.getType());
			goodsReportResDto.setTaxRate(goods.getTaxRate() == null ? BigDecimal.ZERO : goods.getTaxRate());
			goodsReportResDto.setSpecification(goods.getSpecification());
			goodsReportResDto.setPledgeProportion(goods.getPledgeProportion());
			list.add(goodsReportResDto);
		}

		return list;
	}

	/**
	 * 封装当前显示的数据
	 * 
	 * @param goodsReList
	 * @return
	 */
	public List<GoodsReportResDto> convertToResults(List<GoodsReport> goodsReList) {
		List<GoodsReportResDto> goodsList = new ArrayList<GoodsReportResDto>();
		if (CollectionUtils.isEmpty(goodsReList)) {
			return goodsList;
		}
		for (GoodsReport goodsReport : goodsReList) {
			GoodsReportResDto reportResDto = new GoodsReportResDto();
			reportResDto = this.convertToResult(goodsReport);
			goodsList.add(reportResDto);
		}
		return goodsList;
	}

	/**
	 * 封装单笔数据
	 * 
	 * @param goodsReport
	 * @return
	 */
	public GoodsReportResDto convertToResult(GoodsReport goodsReport) {
		GoodsReportResDto reportResDto = new GoodsReportResDto();
		if (goodsReport == null) {
			return reportResDto;
		}
		if (goodsReport.getProjectId() != null) {
			reportResDto.setProjectName(cacheService.getProjectNameById(goodsReport.getProjectId()));// 项目名称
			reportResDto.setProjectId(goodsReport.getProjectId());
		}
		if (goodsReport.getSupplierId() != null) {
			reportResDto.setSupplierName(
					cacheService.showSubjectNameByIdAndKey(goodsReport.getSupplierId(), CacheKeyConsts.SUPPLIER));// 供应商
			reportResDto.setSupplierId(goodsReport.getSupplierId());
		}
		if (goodsReport.getCustomerId() != null) {
			reportResDto.setCustomerName(
					cacheService.getSubjectNcByIdAndKey(goodsReport.getCustomerId(), CacheKeyConsts.CUSTOMER));// 客户
			reportResDto.setCustomerId(goodsReport.getCustomerId());
		}
		reportResDto.setGoodsCode(goodsReport.getSku());// 商品编号
		reportResDto.setBeforeNumber(
				goodsReport.getBeforeNumber() == null ? BigDecimal.ZERO : goodsReport.getBeforeNumber());
		reportResDto.setInNumber(goodsReport.getInNumber() == null ? BigDecimal.ZERO : goodsReport.getInNumber());
		reportResDto.setSaleNumber(goodsReport.getSaleNumber() == null ? BigDecimal.ZERO : goodsReport.getSaleNumber());
		reportResDto.setReturnNumber(goodsReport.getReturnNumber());
		reportResDto.setAfterNumber(goodsReport.getAfterNumber());
		reportResDto.setPleaseNumber(goodsReport.getPleaseNumber());
		return reportResDto;
	}
}
