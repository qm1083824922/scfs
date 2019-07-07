package com.scfs.service.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.report.InvoicingReportDao;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.report.entity.InvoicingDTlResult;
import com.scfs.domain.report.entity.InvoicingListReport;
import com.scfs.domain.report.entity.InvoicingReport;
import com.scfs.domain.report.entity.NumAndAmount;
import com.scfs.domain.report.req.InvoicingReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * 
 * @author Administrator
 *
 */

@Service
public class InvoicingReportService {

	@Autowired
	private InvoicingReportDao invoicingReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ReportProjectService reportProjectService;

	public PageResult<InvoicingReport> queryResultsByCon(InvoicingReportReqDto invoicingReportReqDto) {
		invoicingReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.THREE));

		invoicingReportReqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<InvoicingReport> result = new PageResult<InvoicingReport>();
		int offSet = PageUtil.getOffSet(invoicingReportReqDto.getPage(), invoicingReportReqDto.getPer_page());
		List<InvoicingReport> allList = new ArrayList<InvoicingReport>();
		// 查询条件
		invoicingReportReqDto.setFirst(offSet);
		invoicingReportReqDto.setLast(invoicingReportReqDto.getPer_page());
		List<InvoicingReportReqDto> conditionList = invoicingReportDao.queryGoodsIdByCon(invoicingReportReqDto);
		for (InvoicingReportReqDto condition : conditionList) {
			InvoicingReport invoiceReport = new InvoicingReport();
			// 期初入库数量\期初出库数量
			condition.setUserId(ServiceSupport.getUser().getId());
			condition.setStartBusinessDate(invoicingReportReqDto.getStartBusinessDate());
			NumAndAmount invoiceInStart = invoicingReportDao.queryStartInByCon(condition);
			NumAndAmount invoiceOutStart = invoicingReportDao.queryStartOutByCon(condition);
			condition.setEndBusinessDate(invoicingReportReqDto.getEndBusinessDate());
			NumAndAmount invoiceOut = invoicingReportDao.queryOutByCon(condition);
			NumAndAmount invoiceIn = invoicingReportDao.queryInByCon(condition);
			condition.setStartBusinessDate("");
			NumAndAmount invoiceInEnd = invoicingReportDao.queryStartInByCon(condition);
			NumAndAmount invoiceOutEnd = invoicingReportDao.queryStartOutByCon(condition);
			BeanUtils.copyProperties(condition, invoiceReport);
			if (invoicingReportReqDto.getStatisticsDimensionType() == 1) {
				invoiceReport.setProjectName(cacheService.showProjectNameById(condition.getProjectId()));
				invoiceReport.setBussinessUserName(cacheService.getUserChineseNameByid(condition.getBussinessUserId()));
				invoiceReport.setDepartName(cacheService.getBaseDepartmentById(condition.getDepartmentId()).getName());
				if (condition.getCustomerId() != null) {
					invoiceReport.setCustomerName(cacheService.showSubjectNameByIdAndKey(invoiceReport.getCustomerId(),
							CacheKeyConsts.CUSTOMER));
				}
			} else if (invoicingReportReqDto.getStatisticsDimensionType() == 2) {
				invoiceReport
						.setWareHouseName(cacheService.getWarehouseById(condition.getWareHouseId()).getChineseName());
			} else {
				invoiceReport.setProjectName(cacheService.showProjectNameById(condition.getProjectId()));
				invoiceReport.setBussinessUserName(cacheService.getUserChineseNameByid(condition.getBussinessUserId()));
				invoiceReport.setDepartName(cacheService.getBaseDepartmentById(condition.getDepartmentId()).getName());
				invoiceReport
						.setWareHouseName(cacheService.getWarehouseById(condition.getWareHouseId()).getChineseName());
				if (condition.getCustomerId() != null) {
					invoiceReport.setCustomerName(cacheService.showSubjectNameByIdAndKey(invoiceReport.getCustomerId(),
							CacheKeyConsts.CUSTOMER));
				}
			}
			invoiceReport.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					condition.getCurrencyType() + ""));
			invoiceReport.setName(cacheService.getGoodsById(condition.getGoodsId()).getName());
			invoiceReport.setNumber(cacheService.getGoodsById(condition.getGoodsId()).getNumber());
			invoiceReport.setStartNumber(invoiceInStart.getNum().subtract(invoiceOutStart.getNum()));
			invoiceReport.setStartAmount(
					DecimalUtil.formatScale2(invoiceInStart.getAmount().subtract(invoiceOutStart.getAmount())));
			invoiceReport.setEndNumber(invoiceInEnd.getNum().subtract(invoiceOutEnd.getNum()));
			invoiceReport.setEndAmount(
					DecimalUtil.formatScale2(invoiceInEnd.getAmount().subtract(invoiceOutEnd.getAmount())));
			invoiceReport.setCurrInNumber(invoiceIn.getNum());
			invoiceReport.setCurrInAmount(DecimalUtil.formatScale2(invoiceIn.getAmount()));
			invoiceReport.setCurrOutNumber(invoiceOut.getNum());
			invoiceReport.setCurrOutAmount(DecimalUtil.formatScale2(invoiceOut.getAmount()));
			BigDecimal taxRate = cacheService.getGoodsById(condition.getGoodsId()).getTaxRate();

			AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(condition.getBusiUnit());
			if (accountBook == null || accountBook.getIsHome() != BaseConsts.ZERO) {
				invoiceReport.setStartExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getStartAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
				invoiceReport.setEndExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getEndAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
				invoiceReport.setCurrInExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getCurrInAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
				invoiceReport.setCurrOutExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getCurrOutAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
			}
			invoiceReport.setBusiUnit(condition.getBusiUnit());
			if (condition.getBusiUnit() != null) {
				invoiceReport.setBusiUnitName(
						cacheService.showSubjectNameByIdAndKey(condition.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
			}
			allList.add(invoiceReport);
		}
		if (null != invoicingReportReqDto.getNeedSum() && invoicingReportReqDto.getNeedSum() == 1) {
			String totalStr = sumAllCondition(invoicingReportReqDto);
			result.setTotalStr(totalStr);
		}
		result.setItems(allList);
		Integer total = invoicingReportDao.querySumNumber(invoicingReportReqDto);
		result.setTotal(total);
		return result;
	}

	public Result<InvoicingListReport> queryResultsDtlByCon(InvoicingReportReqDto invoicingReportReqDto)
			throws Exception {
		invoicingReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.THREE));

		invoicingReportReqDto.setUserId(ServiceSupport.getUser().getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Result<InvoicingListReport> result = new Result<InvoicingListReport>();
		InvoicingReport invoiceReport = new InvoicingReport();
		InvoicingListReport invoiceList = new InvoicingListReport();
		List<InvoicingDTlResult> conditionList = invoicingReportDao.queryInfoDtl(invoicingReportReqDto);
		List<InvoicingDTlResult> resultList = new ArrayList<InvoicingDTlResult>();
		InvoicingDTlResult startNum = new InvoicingDTlResult();
		NumAndAmount invoiceInStart = invoicingReportDao.queryStartInByCon(invoicingReportReqDto);
		NumAndAmount invoiceOutStart = invoicingReportDao.queryStartOutByCon(invoicingReportReqDto);
		startNum.setAmount(invoiceInStart.getAmount().subtract(invoiceOutStart.getAmount()));
		startNum.setNumber(invoiceInStart.getNum().subtract(invoiceOutStart.getNum()));
		startNum.setNumberSum(invoiceInStart.getNum().subtract(invoiceOutStart.getNum()));
		startNum.setAmountSum(invoiceInStart.getAmount().subtract(invoiceOutStart.getAmount()));
		startNum.setBillDate(sdf.parse(invoicingReportReqDto.getStartBusinessDate()));
		startNum.setBillTypeName("期初");
		startNum.setBillNo("期初");
		BigDecimal numSum = startNum.getNumber();
		BigDecimal amountSum = startNum.getAmount();
		resultList.add(startNum);
		for (InvoicingDTlResult condition : conditionList) {
			if (condition.getBillNo().contains("IN")) {
				numSum = numSum.add(condition.getNumber());
				amountSum = amountSum.add(condition.getAmount());
				condition.setBillTypeName("入库");
				condition.setAmountSum(amountSum);
				condition.setNumberSum(numSum);
			} else {
				numSum = numSum.subtract(condition.getNumber());
				amountSum = amountSum.subtract(condition.getAmount());
				condition.setAmountSum(amountSum);
				condition.setNumberSum(numSum);
				condition.setBillTypeName("出库");
			}
			resultList.add(condition);
		}
		conditionList.add(startNum);
		BeanUtils.copyProperties(invoicingReportReqDto, invoiceReport);
		if (invoicingReportReqDto.getStatisticsDimensionType() == 1) {
			invoiceReport.setProjectName(cacheService.showProjectNameById(invoicingReportReqDto.getProjectId()));
			invoiceReport.setBussinessUserName(
					cacheService.getUserChineseNameByid(invoicingReportReqDto.getBussinessUserId()));
			invoiceReport.setDepartName(
					cacheService.getBaseDepartmentById(invoicingReportReqDto.getDepartmentId()).getName());
			if (invoicingReportReqDto.getCustomerId() != null) {
				invoiceReport.setCustomerName(
						cacheService.showSubjectNameByIdAndKey(invoiceReport.getCustomerId(), CacheKeyConsts.CUSTOMER));
			}
		} else if (invoicingReportReqDto.getStatisticsDimensionType() == 2) {
			invoiceReport.setWareHouseName(
					cacheService.getWarehouseById(invoicingReportReqDto.getWareHouseId()).getChineseName());
		} else {
			invoiceReport.setProjectName(cacheService.showProjectNameById(invoicingReportReqDto.getProjectId()));
			invoiceReport.setBussinessUserName(
					cacheService.getUserChineseNameByid(invoicingReportReqDto.getBussinessUserId()));
			invoiceReport.setDepartName(
					cacheService.getBaseDepartmentById(invoicingReportReqDto.getDepartmentId()).getName());
			invoiceReport.setWareHouseName(
					cacheService.getWarehouseById(invoicingReportReqDto.getWareHouseId()).getChineseName());
			if (invoicingReportReqDto.getCustomerId() != null) {
				invoiceReport.setCustomerName(
						cacheService.showSubjectNameByIdAndKey(invoiceReport.getCustomerId(), CacheKeyConsts.CUSTOMER));
			}
		}

		invoiceReport.setName(cacheService.getGoodsById(invoicingReportReqDto.getGoodsId()).getName());
		invoiceReport.setNumber(cacheService.getGoodsById(invoicingReportReqDto.getGoodsId()).getNumber());
		invoiceList.setInvoicingList(resultList);
		invoiceList.setInvoicingReport(invoiceReport);
		result.setItems(invoiceList);
		return result;
	}

	private String sumAllCondition(InvoicingReportReqDto invoicingReportReqDto) {
		// 期初总数
		BigDecimal startNumSum = BigDecimal.ZERO;
		BigDecimal startAmountSum = BigDecimal.ZERO;
		BigDecimal InNumSum = BigDecimal.ZERO;
		BigDecimal InAmountSum = BigDecimal.ZERO;
		BigDecimal OutAmountSum = BigDecimal.ZERO;
		BigDecimal OutNumSum = BigDecimal.ZERO;
		BigDecimal EndAmountSum = BigDecimal.ZERO;
		BigDecimal EndNumSum = BigDecimal.ZERO;
		invoicingReportReqDto.setFirst(-1);
		List<InvoicingReportReqDto> sumList = invoicingReportDao.queryGoodsIdByCon(invoicingReportReqDto);
		for (InvoicingReportReqDto sumSingle : sumList) {
			sumSingle.setUserId(ServiceSupport.getUser().getId());
			sumSingle.setStartBusinessDate(invoicingReportReqDto.getStartBusinessDate());
			NumAndAmount sumInStart = invoicingReportDao.queryStartInByCon(sumSingle);
			NumAndAmount sumOutStart = invoicingReportDao.queryStartOutByCon(sumSingle);
			sumSingle.setEndBusinessDate(invoicingReportReqDto.getEndBusinessDate());
			NumAndAmount sumOut = invoicingReportDao.queryOutByCon(sumSingle);
			NumAndAmount sumIn = invoicingReportDao.queryInByCon(sumSingle);
			sumSingle.setStartBusinessDate("");
			NumAndAmount sumInEnd = invoicingReportDao.queryStartInByCon(sumSingle);
			NumAndAmount sumOutEnd = invoicingReportDao.queryStartOutByCon(sumSingle);
			startNumSum = DecimalUtil.add(sumInStart.getNum().subtract(sumOutStart.getNum()), startNumSum);
			startAmountSum = DecimalUtil.add(startAmountSum, ServiceSupport.amountNewToRMB(
					sumInStart.getAmount().subtract(sumOutStart.getAmount()), sumSingle.getCurrencyType(), null));
			InNumSum = DecimalUtil.add(InNumSum, sumIn.getNum());
			InAmountSum = DecimalUtil.add(InAmountSum,
					ServiceSupport.amountNewToRMB(sumIn.getAmount(), sumSingle.getCurrencyType(), null));
			OutNumSum = DecimalUtil.add(OutNumSum, sumOut.getNum());
			OutAmountSum = DecimalUtil.add(OutAmountSum,
					ServiceSupport.amountNewToRMB(sumOut.getAmount(), sumSingle.getCurrencyType(), null));
			EndNumSum = DecimalUtil.add(sumInEnd.getNum().subtract(sumOutEnd.getNum()), EndNumSum);
			EndAmountSum = DecimalUtil.add(EndAmountSum, ServiceSupport.amountNewToRMB(
					sumInEnd.getAmount().subtract(sumOutEnd.getAmount()), sumSingle.getCurrencyType(), null));
		}
		StringBuilder totalStr = new StringBuilder();
		totalStr.append(DecimalUtil.formatScale2(startNumSum) + "," + DecimalUtil.formatScale2(InNumSum) + ","
				+ DecimalUtil.formatScale2(OutNumSum) + "," + DecimalUtil.formatScale2(EndNumSum) + ","
				+ DecimalUtil.formatScale2(startAmountSum) + "," + DecimalUtil.formatScale2(InAmountSum) + ","
				+ DecimalUtil.formatScale2(OutAmountSum) + "," + DecimalUtil.formatScale2(EndAmountSum));
		return totalStr.toString();
	}

	public List<InvoicingReport> queryExportResultsByCon(InvoicingReportReqDto invoicingReportReqDto) {
		invoicingReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.THREE));

		invoicingReportReqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoicingReport> allList = new ArrayList<InvoicingReport>();
		// 查询条件
		invoicingReportReqDto.setFirst(-1);
		invoicingReportReqDto.setLast(invoicingReportReqDto.getPer_page());
		List<InvoicingReportReqDto> conditionList = invoicingReportDao.queryGoodsIdByCon(invoicingReportReqDto);
		for (InvoicingReportReqDto condition : conditionList) {
			InvoicingReport invoiceReport = new InvoicingReport();
			// 期初入库数量\期初出库数量
			condition.setUserId(ServiceSupport.getUser().getId());
			condition.setStartBusinessDate(invoicingReportReqDto.getStartBusinessDate());
			NumAndAmount invoiceInStart = invoicingReportDao.queryStartInByCon(condition);
			NumAndAmount invoiceOutStart = invoicingReportDao.queryStartOutByCon(condition);
			condition.setEndBusinessDate(invoicingReportReqDto.getEndBusinessDate());
			NumAndAmount invoiceOut = invoicingReportDao.queryOutByCon(condition);
			NumAndAmount invoiceIn = invoicingReportDao.queryInByCon(condition);
			condition.setStartBusinessDate("");
			NumAndAmount invoiceInEnd = invoicingReportDao.queryStartInByCon(condition);
			NumAndAmount invoiceOutEnd = invoicingReportDao.queryStartOutByCon(condition);
			BeanUtils.copyProperties(condition, invoiceReport);
			if (invoicingReportReqDto.getStatisticsDimensionType() == 1) {
				invoiceReport.setProjectName(cacheService.showProjectNameById(condition.getProjectId()));
				invoiceReport.setBussinessUserName(cacheService.getUserChineseNameByid(condition.getBussinessUserId()));
				invoiceReport.setDepartName(cacheService.getBaseDepartmentById(condition.getDepartmentId()).getName());
				if (condition.getCustomerId() != null) {
					invoiceReport.setCustomerName(cacheService.showSubjectNameByIdAndKey(invoiceReport.getCustomerId(),
							CacheKeyConsts.CUSTOMER));
				}
			} else if (invoicingReportReqDto.getStatisticsDimensionType() == 2) {
				invoiceReport
						.setWareHouseName(cacheService.getWarehouseById(condition.getWareHouseId()).getChineseName());
			} else {
				invoiceReport.setProjectName(cacheService.showProjectNameById(condition.getProjectId()));
				invoiceReport.setBussinessUserName(cacheService.getUserChineseNameByid(condition.getBussinessUserId()));
				invoiceReport.setDepartName(cacheService.getBaseDepartmentById(condition.getDepartmentId()).getName());
				invoiceReport
						.setWareHouseName(cacheService.getWarehouseById(condition.getWareHouseId()).getChineseName());
				if (condition.getCustomerId() != null) {
					invoiceReport.setCustomerName(cacheService.showSubjectNameByIdAndKey(invoiceReport.getCustomerId(),
							CacheKeyConsts.CUSTOMER));
				}
			}
			invoiceReport.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					condition.getCurrencyType() + ""));
			invoiceReport.setName(cacheService.getGoodsById(condition.getGoodsId()).getName());
			invoiceReport.setNumber(cacheService.getGoodsById(condition.getGoodsId()).getNumber());
			invoiceReport.setStartNumber(invoiceInStart.getNum().subtract(invoiceOutStart.getNum()));
			invoiceReport.setStartAmount(
					DecimalUtil.formatScale2(invoiceInStart.getAmount().subtract(invoiceOutStart.getAmount())));
			invoiceReport.setEndNumber(invoiceInEnd.getNum().subtract(invoiceOutEnd.getNum()));
			invoiceReport.setEndAmount(
					DecimalUtil.formatScale2(invoiceInEnd.getAmount().subtract(invoiceOutEnd.getAmount())));
			invoiceReport.setCurrInNumber(invoiceIn.getNum());
			invoiceReport.setCurrInAmount(DecimalUtil.formatScale2(invoiceIn.getAmount()));
			invoiceReport.setCurrOutNumber(invoiceOut.getNum());
			invoiceReport.setCurrOutAmount(DecimalUtil.formatScale2(invoiceOut.getAmount()));
			BigDecimal taxRate = cacheService.getGoodsById(condition.getGoodsId()).getTaxRate();
			AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(condition.getBusiUnit());
			if (accountBook == null || accountBook.getIsHome() != BaseConsts.ZERO) {
				invoiceReport.setStartExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getStartAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
				invoiceReport.setEndExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getEndAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
				invoiceReport.setCurrInExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getCurrInAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
				invoiceReport.setCurrOutExRateAmount(DecimalUtil
						.formatScale2(DecimalUtil.divide(invoiceReport.getCurrOutAmount(), BigDecimal.ONE.add(taxRate))
								.setScale(2, BigDecimal.ROUND_HALF_DOWN)));
			}
			allList.add(invoiceReport);
		}
		return allList;
	}

}
