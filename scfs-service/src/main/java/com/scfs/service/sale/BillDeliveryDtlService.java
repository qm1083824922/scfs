package com.scfs.service.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.BillOutStorePickDtlDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.logistics.entity.StlSum;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliveryDtlSearchReqDto;
import com.scfs.domain.sale.dto.req.BillDeliveryReqDto;
import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlExtResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.domain.sale.entity.BillDeliveryDtlExcel;
import com.scfs.domain.sale.entity.BillDeliveryDtlExt;
import com.scfs.domain.sale.entity.BillDeliveryDtlSum;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.logistics.StlService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月27日.
 */
@Service
public class BillDeliveryDtlService {
	@Value("${billDeliveryDtl.import.xmlConfig}")
	private String billDeliveryDtlXmlConfig;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillOutStorePickDtlDao billOutStorePickDtlDao;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private StlService stlService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 根据销售单ID查询销售单明细
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillDeliveryDtlResDto> queryBillDeliveryDtlsByBillDeliveryId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		PageResult<BillDeliveryDtlResDto> result = new PageResult<BillDeliveryDtlResDto>();

		int offSet = PageUtil.getOffSet(billDeliveryDtlSearchReqDto.getPage(),
				billDeliveryDtlSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billDeliveryDtlSearchReqDto.getPer_page());
		List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryDtlDao
				.queryResultsByBillDeliveryId(billDeliveryDtlSearchReqDto.getBillDeliveryId(), rowBounds);
		BillDeliveryDtlResDto billDeliveryDtlResDtoSum = new BillDeliveryDtlResDto();
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryDtlSearchReqDto.getBillDeliveryId());
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList = convertToResDto(billDelivery, billDeliveryDtlList,
				billDeliveryDtlResDtoSum);
		result.setItems(billDeliveryDtlResDtoList);
		String totalStr = getTotalStr(billDelivery, billDeliveryDtlResDtoSum);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billDeliveryDtlSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billDeliveryDtlSearchReqDto.getPage());
		result.setPer_page(billDeliveryDtlSearchReqDto.getPer_page());

		return result;
	}

	private String getTotalStr(BillDelivery billDelivery, BillDeliveryDtlResDto billDeliveryDtlResDtoSum) {
		String totalStr = "";
		if (null != billDeliveryDtlResDtoSum && null != billDelivery) {
			if (billDelivery.getBillType().equals(BaseConsts.ONE)) { // 1-销售
				totalStr = "销售数量：" + DecimalUtil.toQuantityString(billDeliveryDtlResDtoSum.getRequiredSendNum())
						+ "；销售金额："
						+ (StringUtils.isBlank(billDeliveryDtlResDtoSum.getRequiredSendAmountStr()) == true ? "0.00"
								: billDeliveryDtlResDtoSum.getRequiredSendAmountStr())
						+ BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(billDelivery.getCurrencyType()) + "；资金占用金额："
						+ (StringUtils.isBlank(billDeliveryDtlResDtoSum.getPayAmountStr()) == true ? "0.00"
								: billDeliveryDtlResDtoSum.getPayAmountStr())
						+ BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(billDelivery.getCurrencyType()) + "；服务金额："
						+ (StringUtils.isBlank(billDeliveryDtlResDtoSum.getServiceAmountStr()) == true ? "0.00"
								: billDeliveryDtlResDtoSum.getServiceAmountStr())
						+ BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(billDelivery.getCurrencyType()) + "；其他费用："
						+ (StringUtils.isBlank(billDeliveryDtlResDtoSum.getOtherAmountStr()) == true ? "0.00"
								: billDeliveryDtlResDtoSum.getOtherAmountStr())
						+ BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(billDelivery.getCurrencyType());
			} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
				totalStr = "退货数量：" + DecimalUtil.toQuantityString(billDeliveryDtlResDtoSum.getRequiredSendNum())
						+ "；退货金额："
						+ (StringUtils.isBlank(billDeliveryDtlResDtoSum.getRequiredSendAmountStr()) == true ? "0.00"
								: billDeliveryDtlResDtoSum.getRequiredSendAmountStr())
						+ BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(billDelivery.getCurrencyType());
			}
		}
		return totalStr;
	}

	/**
	 * 根据销售单ID查询销售单明细(不分页)
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillDeliveryDtlResDto> queryAllBillDeliveryDtlsByBillDeliveryId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		return queryAllBillDeliveryDtlsByBillDeliveryId(billDeliveryDtlSearchReqDto, false, null);
	}

	/**
	 * 根据销售单ID查询销售单明细(不分页)
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillDeliveryDtlResDto> queryAllBillDeliveryDtlsByBillDeliveryId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto, boolean isReCalcPrice, Date returnTime) {
		PageResult<BillDeliveryDtlResDto> result = new PageResult<BillDeliveryDtlResDto>();
		List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryDtlDao
				.queryResultsByBillDeliveryId(billDeliveryDtlSearchReqDto.getBillDeliveryId());
		BillDeliveryDtlResDto billDeliveryDtlResDtoSum = new BillDeliveryDtlResDto();
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryDtlSearchReqDto.getBillDeliveryId());
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList = convertToResDto(billDelivery, billDeliveryDtlList,
				billDeliveryDtlResDtoSum, isReCalcPrice, returnTime);
		result.setItems(billDeliveryDtlResDtoList);

		return result;
	}

	/**
	 * 根据单据条件查询销售单明细
	 * 
	 * @param billDeliverySearchReqDto
	 * @return
	 */
	public List<BillDeliveryDtlExtResDto> queryAllBillDeliveryDtlExtResultsByBillDeliveryCon(
			BillDeliverySearchReqDto billDeliverySearchReqDto) {
		if (null == billDeliverySearchReqDto.getUserId()) {
			billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<BillDeliveryDtlExt> billDeliveryDtlExtList = billDeliveryDtlDao
				.queryResultsByBillDeliveryCon(billDeliverySearchReqDto);
		List<BillDeliveryDtlExtResDto> billDeliveryDtlExtResDtoList = convertToExtResDto(billDeliveryDtlExtList);

		return billDeliveryDtlExtResDtoList;
	}

	/**
	 * 根据ID查询销售单明细信息
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public Result<BillDeliveryDtlResDto> queryBillDeliveryDtlById(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		Result<BillDeliveryDtlResDto> result = new Result<BillDeliveryDtlResDto>();
		BillDeliveryDtl billDeliveryDtlRes = billDeliveryDtlDao.queryEntityById(billDeliveryDtlSearchReqDto.getId());
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryDtlRes.getBillDeliveryId());
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		BillDeliveryDtlResDto bllDeliveryDtlResDto = convertToResDto(billDelivery, billDeliveryDtlRes);
		result.setItems(bllDeliveryDtlResDto);
		return result;
	}

	public List<BillDeliveryDtlResDto> convertToResDto(BillDelivery billDelivery,
			List<BillDeliveryDtl> billDeliveryDtlList, BillDeliveryDtlResDto billDeliveryDtlResDtoSum) {
		return convertToResDto(billDelivery, billDeliveryDtlList, billDeliveryDtlResDtoSum, false, null);
	}

	private List<BillDeliveryDtlResDto> convertToResDto(BillDelivery billDelivery,
			List<BillDeliveryDtl> billDeliveryDtlList, BillDeliveryDtlResDto billDeliveryDtlResDtoSum,
			boolean isReCalcPrice, Date returnTime) {
		List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList = new ArrayList<BillDeliveryDtlResDto>(5);
		if (CollectionUtils.isEmpty(billDeliveryDtlList)) {
			return billDeliveryDtlResDtoList;
		}
		BigDecimal totalRequiredSendAmount = BigDecimal.ZERO;
		for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtlList) {
			BillDeliveryDtlResDto billDeliveryDtlResDto = convertToResDto(billDelivery, billDeliveryDtl, isReCalcPrice,
					returnTime);
			billDeliveryDtlResDtoSum.setBillDeliveryId(billDeliveryDtlResDto.getBillDeliveryId());
			billDeliveryDtlResDtoSum.setRequiredSendNum(DecimalUtil.add(
					null == billDeliveryDtlResDtoSum.getRequiredSendNum() ? BigDecimal.ZERO
							: billDeliveryDtlResDtoSum.getRequiredSendNum(),
					null == billDeliveryDtlResDto.getRequiredSendNum() ? BigDecimal.ZERO
							: billDeliveryDtlResDto.getRequiredSendNum()));
			billDeliveryDtlResDtoSum.setRequiredSendPrice(DecimalUtil.add(
					null == billDeliveryDtlResDtoSum.getRequiredSendPrice() ? BigDecimal.ZERO
							: billDeliveryDtlResDtoSum.getRequiredSendPrice(),
					null == billDeliveryDtlResDto.getRequiredSendPrice() ? BigDecimal.ZERO
							: billDeliveryDtlResDto.getRequiredSendPrice()));
			billDeliveryDtlResDtoSum.setPayAmount(DecimalUtil.add(
					null == billDeliveryDtlResDtoSum.getPayAmount() ? BigDecimal.ZERO
							: billDeliveryDtlResDtoSum.getPayAmount(),
					null == billDeliveryDtlResDto.getPayAmount() ? BigDecimal.ZERO
							: billDeliveryDtlResDto.getPayAmount()));
			billDeliveryDtlResDtoSum.setServiceAmount(DecimalUtil.add(
					null == billDeliveryDtlResDtoSum.getServiceAmount() ? BigDecimal.ZERO
							: billDeliveryDtlResDtoSum.getServiceAmount(),
					null == billDeliveryDtlResDto.getServiceAmount() ? BigDecimal.ZERO
							: billDeliveryDtlResDto.getServiceAmount()));
			billDeliveryDtlResDtoSum.setOtherAmount(DecimalUtil.add(
					null == billDeliveryDtlResDtoSum.getOtherAmount() ? BigDecimal.ZERO
							: billDeliveryDtlResDtoSum.getOtherAmount(),
					null == billDeliveryDtlResDto.getOtherAmount() ? BigDecimal.ZERO
							: billDeliveryDtlResDto.getOtherAmount()));
			totalRequiredSendAmount = DecimalUtil.add(totalRequiredSendAmount,
					DecimalUtil.multiply(
							null == billDeliveryDtlResDto.getRequiredSendNum() ? BigDecimal.ZERO
									: billDeliveryDtlResDto.getRequiredSendNum(),
							null == billDeliveryDtlResDto.getRequiredSendPrice() ? BigDecimal.ZERO
									: billDeliveryDtlResDto.getRequiredSendPrice()));
			billDeliveryDtlResDtoSum.setRequiredSendAmount(totalRequiredSendAmount);

			billDeliveryDtlResDtoSum.setRequiredSendAmountStr(DecimalUtil.toAmountString(totalRequiredSendAmount));
			billDeliveryDtlResDtoSum
					.setPayAmountStr(DecimalUtil.toAmountString(billDeliveryDtlResDtoSum.getPayAmount()));
			billDeliveryDtlResDtoSum
					.setServiceAmountStr(DecimalUtil.toAmountString(billDeliveryDtlResDtoSum.getServiceAmount()));
			billDeliveryDtlResDtoSum
					.setOtherAmountStr(DecimalUtil.toAmountString(billDeliveryDtlResDtoSum.getOtherAmount()));

			billDeliveryDtlResDto.setRemark(billDeliveryDtl.getRemark() == null ? "" : billDeliveryDtl.getRemark());
			billDeliveryDtlResDtoList.add(billDeliveryDtlResDto);
		}
		return billDeliveryDtlResDtoList;
	}

	private BillDeliveryDtlResDto convertToResDto(BillDelivery billDelivery, BillDeliveryDtl billDeliveryDtl) {
		return convertToResDto(billDelivery, billDeliveryDtl, false, null);
	}

	private BillDeliveryDtlResDto convertToResDto(BillDelivery billDelivery, BillDeliveryDtl billDeliveryDtl,
			boolean isReCalcPrice, Date returnTime) {
		BillDeliveryDtlResDto billDeliveryDtlResDto = new BillDeliveryDtlResDto();
		billDeliveryDtlResDto.setPayRealCurrency(billDeliveryDtl.getPayRealCurrency());
		billDeliveryDtlResDto.setPayRate(billDeliveryDtl.getPayRate());
		if (null != billDeliveryDtl) {
			BeanUtils.copyProperties(billDeliveryDtl, billDeliveryDtlResDto);
			if (billDeliveryDtl.getGoodsStatus() != null) {
				billDeliveryDtlResDto.setGoodsStatusName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_IN_STORE_GOODS_STATUS, Integer.toString(billDeliveryDtl.getGoodsStatus())));
			}
			if (billDeliveryDtl.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billDeliveryDtl.getGoodsId());
				if (null != baseGoods) {
					billDeliveryDtlResDto.setGoodsName(baseGoods.getName());
					billDeliveryDtlResDto.setGoodsNumber(baseGoods.getNumber());
					billDeliveryDtlResDto.setGoodsType(baseGoods.getType());
					billDeliveryDtlResDto.setGoodsUnit(baseGoods.getUnit());
					billDeliveryDtlResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}

			if (billDelivery.getBillType().equals(BaseConsts.ONE) || billDelivery.getBillType() == BaseConsts.THREE) { // 1-销售
				if (billDeliveryDtl.getAssignStlFlag() != null) {
					billDeliveryDtlResDto.setAssignStlFlagName(ServiceSupport.getValueByBizCode(
							BizCodeConsts.PROMPT_STATUS, Integer.toString(billDeliveryDtl.getAssignStlFlag())));
				}
				if (null != billDeliveryDtl.getStlId()) {
					Stl stl = stlService.queryEntityById(billDeliveryDtl.getStlId());
					if (null != stl) {
						billDeliveryDtlResDto.setAvailableNum(stl.getAvailableNum());
						billDeliveryDtlResDto.setCostPrice(stl.getCostPrice());
						billDeliveryDtlResDto.setOrderNo(stl.getOrderNo());
						if (isReCalcPrice == true) {
							BigDecimal salePrice = projectItemService.getSalePrice(billDeliveryDtl.getStlId(),
									returnTime);
							billDeliveryDtlResDto.setRequiredSendPrice(null == salePrice ? BigDecimal.ZERO : salePrice);
						}
					}
				}

				BigDecimal requiredSendPrice = (null == billDeliveryDtlResDto.getRequiredSendPrice() ? BigDecimal.ZERO
						: billDeliveryDtlResDto.getRequiredSendPrice());
				BigDecimal requiredSendAmount = (null == billDeliveryDtlResDto.getRequiredSendAmount() ? BigDecimal.ZERO
						: billDeliveryDtlResDto.getRequiredSendAmount());
				BigDecimal costPrice = (null == billDeliveryDtlResDto.getCostPrice() ? BigDecimal.ZERO
						: billDeliveryDtlResDto.getCostPrice());
				BigDecimal originSendPrice = (null == billDeliveryDtlResDto.getOriginSendPrice() ? BigDecimal.ZERO
						: billDeliveryDtlResDto.getOriginSendPrice());

				// 付款
				BigDecimal payAmount = DecimalUtil.multiply(
						null == billDeliveryDtlResDto.getPayPrice() ? BigDecimal.ZERO
								: billDeliveryDtlResDto.getPayPrice(),
						null == billDeliveryDtlResDto.getRequiredSendNum() ? BigDecimal.ZERO
								: billDeliveryDtlResDto.getRequiredSendNum());
				billDeliveryDtlResDto.setPayAmount(payAmount);
				// 服务费
				BigDecimal serviceAmount = BigDecimal.ZERO;
				if (isReCalcPrice == true) {
					if (null != billDeliveryDtl.getStlId()) {
						BigDecimal profitPrice = projectItemService
								.getProfitPriceByStl(billDeliveryDtlResDto.getStlId(), returnTime);
						serviceAmount = DecimalUtil.multiply(null == profitPrice ? BigDecimal.ZERO : profitPrice,
								null == billDeliveryDtlResDto.getRequiredSendNum() ? BigDecimal.ZERO
										: billDeliveryDtlResDto.getRequiredSendNum());
					}
				} else {
					serviceAmount = DecimalUtil.multiply(
							null == billDeliveryDtlResDto.getProfitPrice() ? BigDecimal.ZERO
									: billDeliveryDtlResDto.getProfitPrice(),
							null == billDeliveryDtlResDto.getRequiredSendNum() ? BigDecimal.ZERO
									: billDeliveryDtlResDto.getRequiredSendNum());
				}
				billDeliveryDtlResDto.setServiceAmount(serviceAmount);
				Integer projectId = billDeliveryDtlResDto.getProjectId();
				if (null == projectId) {
					projectId = billDelivery.getProjectId();
				}
				Integer payDays = 0;
				if (isReCalcPrice == true) {
					payDays = (int) projectItemService.getOccupyDays(projectId, billDeliveryDtlResDto.getPayTime(),
							returnTime);
				} else {
					payDays = (int) projectItemService.getOccupyDays(projectId, billDeliveryDtlResDto.getPayTime(),
							billDeliveryDtlResDto.getReturnTime());
				}
				billDeliveryDtlResDto.setPayDays(payDays); // 占用天数
				if (null != projectId) {
					ProjectItem projectItem = projectItemService.getProjectItem(projectId); // 条款
					billDeliveryDtlResDto.setFundMonthRate(projectItemService.getFundMonthRate(projectItem, payDays)); // 资金日服务费率
					billDeliveryDtlResDto.setDayPenalRate(projectItem.getDayPenalRate()); // 日违约金费率
				}

				// 其他费用
				BigDecimal otherAmount = DecimalUtil.subtract(billDeliveryDtlResDto.getRequiredSendAmount(),
						DecimalUtil.add(billDeliveryDtlResDto.getServiceAmount(),
								DecimalUtil.multiply(
										null == billDeliveryDtlResDto.getCostPrice() ? BigDecimal.ZERO
												: billDeliveryDtlResDto.getCostPrice(),
										billDeliveryDtlResDto.getRequiredSendNum())));
				billDeliveryDtlResDto.setOtherAmount(otherAmount);

				billDeliveryDtlResDto.setRequiredSendPriceStr(DecimalUtil.toPriceString(requiredSendPrice));
				billDeliveryDtlResDto.setRequiredSendPriceDouble(DecimalUtil.toAmountString(requiredSendPrice));
				billDeliveryDtlResDto.setRequiredSendAmountStr(DecimalUtil.toAmountString(requiredSendAmount));
				billDeliveryDtlResDto.setCostPriceStr(DecimalUtil.toPriceString(costPrice));
				billDeliveryDtlResDto.setPayAmountStr(DecimalUtil.toAmountString(payAmount));
				billDeliveryDtlResDto.setServiceAmountStr(DecimalUtil.toAmountString(serviceAmount));
				billDeliveryDtlResDto.setOtherAmountStr(DecimalUtil.toAmountString(otherAmount));
				billDeliveryDtlResDto.setOriginSendPriceStr(DecimalUtil.toPriceString(originSendPrice));
			} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
				if (null != billDeliveryDtlResDto.getBillOutStorePickDtlId()) {
					BillOutStorePickDtl billOutStorePickDtl = billOutStorePickDtlDao
							.queryEntityById(billDeliveryDtlResDto.getBillOutStorePickDtlId());
					if (null != billOutStorePickDtl) {
						billDeliveryDtlResDto.setAvailableReturnNum(DecimalUtil
								.subtract(billOutStorePickDtl.getPickupNum(), billOutStorePickDtl.getReturnNum()));
					}
				}
				billDeliveryDtlResDto.setRequiredSendPriceStr(
						DecimalUtil.toPriceString(billDeliveryDtlResDto.getRequiredSendPrice()));
				billDeliveryDtlResDto.setRequiredSendPriceDouble(
						DecimalUtil.toAmountString(billDeliveryDtlResDto.getRequiredSendPrice()));
				billDeliveryDtlResDto.setRequiredSendAmountStr(
						DecimalUtil.toAmountString(billDeliveryDtlResDto.getRequiredSendAmount()));
				billDeliveryDtlResDto.setCostPriceStr(DecimalUtil.toPriceString(billDeliveryDtlResDto.getCostPrice()));
				billDeliveryDtlResDto.setPayAmountStr(DecimalUtil.toAmountString(billDeliveryDtlResDto.getPayAmount()));
				billDeliveryDtlResDto
						.setServiceAmountStr(DecimalUtil.toAmountString(billDeliveryDtlResDto.getServiceAmount()));
				billDeliveryDtlResDto
						.setOtherAmountStr(DecimalUtil.toAmountString(billDeliveryDtlResDto.getOtherAmount()));
				billDeliveryDtlResDto
						.setOriginSendPriceStr(DecimalUtil.toPriceString(billDeliveryDtlResDto.getOriginSendPrice()));
			}
		}
		return billDeliveryDtlResDto;
	}

	/**
	 * 对象扩展转换
	 * 
	 * @param billDeliveryDtlExtList
	 * @return
	 */
	private List<BillDeliveryDtlExtResDto> convertToExtResDto(List<BillDeliveryDtlExt> billDeliveryDtlExtList) {
		List<BillDeliveryDtlExtResDto> billDeliveryDtlExtResDtoList = new ArrayList<BillDeliveryDtlExtResDto>(5);
		if (CollectionUtils.isEmpty(billDeliveryDtlExtList)) {
			return billDeliveryDtlExtResDtoList;
		}
		for (BillDeliveryDtlExt billDeliveryDtlExt : billDeliveryDtlExtList) {
			BillDeliveryDtlExtResDto billDeliveryDtlExtResDto = convertToExtResDto(billDeliveryDtlExt);
			billDeliveryDtlExtResDtoList.add(billDeliveryDtlExtResDto);
		}
		return billDeliveryDtlExtResDtoList;
	}

	private BillDeliveryDtlExtResDto convertToExtResDto(BillDeliveryDtlExt billDeliveryDtlExt) {
		BillDeliveryDtlExtResDto billDeliveryDtlExtResDto = new BillDeliveryDtlExtResDto();
		if (null != billDeliveryDtlExt) {
			BeanUtils.copyProperties(billDeliveryDtlExt, billDeliveryDtlExtResDto);
			if (billDeliveryDtlExt.getGoodsStatus() != null) {
				billDeliveryDtlExtResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billDeliveryDtlExt.getGoodsStatus())));
			}
			if (billDeliveryDtlExt.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billDeliveryDtlExt.getGoodsId());
				if (null != baseGoods) {
					billDeliveryDtlExtResDto.setGoodsName(baseGoods.getName());
					billDeliveryDtlExtResDto.setGoodsNumber(baseGoods.getNumber());
					billDeliveryDtlExtResDto.setGoodsType(baseGoods.getType());
					billDeliveryDtlExtResDto.setGoodsUnit(baseGoods.getUnit());
					billDeliveryDtlExtResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}
			if (billDeliveryDtlExt.getAssignStlFlag() != null) {
				billDeliveryDtlExtResDto.setAssignStlFlagName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.PROMPT_STATUS, Integer.toString(billDeliveryDtlExt.getAssignStlFlag())));
			}

			// 销售单信息
			if (billDeliveryDtlExt.gettStatus() != null) {
				if (billDeliveryDtlExt.gettBillType().equals(BaseConsts.ONE)) { // 1-销售
					billDeliveryDtlExtResDto.settStatusName(ServiceSupport.getValueByBizCode(
							BizCodeConsts.BILL_DELIVERY_STATUS, Integer.toString(billDeliveryDtlExt.gettStatus())));
				} else if (billDeliveryDtlExt.gettBillType().equals(BaseConsts.TWO)) { // 2-退货
					billDeliveryDtlExtResDto.settStatusName(ServiceSupport.getValueByBizCode(
							BizCodeConsts.BILL_RETURN_STATUS, Integer.toString(billDeliveryDtlExt.gettStatus())));
				}
			}
			if (billDeliveryDtlExt.gettBillType() != null) {
				billDeliveryDtlExtResDto.settBillTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_DELIVERY_TYPE_SELECT, Integer.toString(billDeliveryDtlExt.gettBillType())));
			}
			if (billDeliveryDtlExt.gettTransferMode() != null) {
				billDeliveryDtlExtResDto.settTransferModeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE,
								Integer.toString(billDeliveryDtlExt.gettTransferMode())));
			}
			if (billDeliveryDtlExt.gettCurrencyType() != null) {
				billDeliveryDtlExtResDto.settCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, Integer.toString(billDeliveryDtlExt.gettCurrencyType())));
			}

			billDeliveryDtlExtResDto
					.settProjectName(cacheService.showProjectNameById(billDeliveryDtlExt.gettProjectId()));
			billDeliveryDtlExtResDto.settWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billDeliveryDtlExt.gettWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billDeliveryDtlExtResDto.settCustomerName(cacheService
					.showSubjectNameByIdAndKey(billDeliveryDtlExt.gettCustomerId(), CacheKeyConsts.CUSTOMER));

			BaseAddress baseAddress = cacheService.getAddressById(billDeliveryDtlExt.gettCustomerAddressId());
			if (null != baseAddress) {
				billDeliveryDtlExtResDto.settCustomerAddress(baseAddress.getShowValue());
			}

			if (billDeliveryDtlExt.gettBillType().equals(BaseConsts.ONE)) { // 1-销售
				BigDecimal requiredSendPrice = (null == billDeliveryDtlExtResDto.getRequiredSendPrice()
						? BigDecimal.ZERO : billDeliveryDtlExtResDto.getRequiredSendPrice());
				BigDecimal requiredSendAmount = (null == billDeliveryDtlExtResDto.getRequiredSendAmount()
						? BigDecimal.ZERO : billDeliveryDtlExtResDto.getRequiredSendAmount());
				BigDecimal costPrice = (null == billDeliveryDtlExtResDto.getCostPrice() ? BigDecimal.ZERO
						: billDeliveryDtlExtResDto.getCostPrice());
				BigDecimal originSendPrice = (null == billDeliveryDtlExt.getOriginSendPrice() ? BigDecimal.ZERO
						: billDeliveryDtlExt.getOriginSendPrice());
				// 付款
				BigDecimal payAmount = DecimalUtil.multiply(
						null == billDeliveryDtlExtResDto.getPayPrice() ? BigDecimal.ZERO
								: billDeliveryDtlExtResDto.getPayPrice(),
						null == billDeliveryDtlExtResDto.getRequiredSendNum() ? BigDecimal.ZERO
								: billDeliveryDtlExtResDto.getRequiredSendNum());
				billDeliveryDtlExtResDto.setPayAmount(payAmount);
				// 服务费
				BigDecimal serviceAmount = DecimalUtil.multiply(
						null == billDeliveryDtlExtResDto.getProfitPrice() ? BigDecimal.ZERO
								: billDeliveryDtlExtResDto.getProfitPrice(),
						null == billDeliveryDtlExtResDto.getRequiredSendNum() ? BigDecimal.ZERO
								: billDeliveryDtlExtResDto.getRequiredSendNum());
				billDeliveryDtlExtResDto.setServiceAmount(serviceAmount);
				Integer payDays = (int) projectItemService.getOccupyDays(billDeliveryDtlExtResDto.gettProjectId(),
						billDeliveryDtlExtResDto.getPayTime(), billDeliveryDtlExtResDto.gettReturnTime());
				billDeliveryDtlExtResDto.setPayDays(payDays); // 占用天数
				ProjectItem projectItem = projectItemService.getProjectItem(billDeliveryDtlExtResDto.gettProjectId()); // 条款
				billDeliveryDtlExtResDto.setFundMonthRate(projectItemService.getFundMonthRate(projectItem, payDays)); // 资金日服务费率
				billDeliveryDtlExtResDto.setDayPenalRate(projectItem.getDayPenalRate()); // 日违约金费率
				// 其他费用
				BigDecimal otherAmount = DecimalUtil
						.subtract(billDeliveryDtlExtResDto.getRequiredSendAmount(),
								DecimalUtil
										.add(billDeliveryDtlExtResDto.getServiceAmount(),
												DecimalUtil
														.multiply(
																null == billDeliveryDtlExtResDto.getCostPrice()
																		? BigDecimal.ZERO
																		: billDeliveryDtlExtResDto.getCostPrice(),
																billDeliveryDtlExtResDto.getRequiredSendNum())));
				billDeliveryDtlExtResDto.setOtherAmount(otherAmount);

				billDeliveryDtlExtResDto.setRequiredSendPriceStr(DecimalUtil.toPriceString(requiredSendPrice));
				billDeliveryDtlExtResDto.setRequiredSendAmountStr(DecimalUtil.toAmountString(requiredSendAmount));
				billDeliveryDtlExtResDto.setCostPriceStr(DecimalUtil.toPriceString(costPrice));
				billDeliveryDtlExtResDto.setPayAmountStr(DecimalUtil.toAmountString(payAmount));
				billDeliveryDtlExtResDto.setServiceAmountStr(DecimalUtil.toAmountString(serviceAmount));
				billDeliveryDtlExtResDto.setOtherAmountStr(DecimalUtil.toAmountString(otherAmount));
				billDeliveryDtlExtResDto.setOriginSendPriceStr(DecimalUtil.toPriceString(originSendPrice));
			} else if (billDeliveryDtlExt.gettBillType().equals(BaseConsts.TWO)) { // 2-退货
				billDeliveryDtlExtResDto.setRequiredSendPriceStr(
						DecimalUtil.toPriceString(billDeliveryDtlExtResDto.getRequiredSendPrice()));
				billDeliveryDtlExtResDto.setRequiredSendAmountStr(
						DecimalUtil.toAmountString(billDeliveryDtlExtResDto.getRequiredSendAmount()));
				billDeliveryDtlExtResDto
						.setCostPriceStr(DecimalUtil.toPriceString(billDeliveryDtlExtResDto.getCostPrice()));
				billDeliveryDtlExtResDto
						.setPayAmountStr(DecimalUtil.toAmountString(billDeliveryDtlExtResDto.getPayAmount()));
				billDeliveryDtlExtResDto
						.setServiceAmountStr(DecimalUtil.toAmountString(billDeliveryDtlExtResDto.getServiceAmount()));
				billDeliveryDtlExtResDto
						.setOtherAmountStr(DecimalUtil.toAmountString(billDeliveryDtlExtResDto.getOtherAmount()));
				billDeliveryDtlExtResDto.setOriginSendPriceStr(
						DecimalUtil.toPriceString(billDeliveryDtlExtResDto.getOriginSendPrice()));
			}
		}
		return billDeliveryDtlExtResDto;
	}

	/**
	 * 新增销售单明细
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void addBillDeliveryDtls(BillDeliveryReqDto billDeliveryReqDto) {
		BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(billDeliveryReqDto.getId());
		if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryReqDto.getBillDeliveryDtlList();
			if (!CollectionUtils.isEmpty(billDeliveryDtlList)) {
				for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtlList) { // 无库存信息
					if (billDeliveryDtl.getGoodsId() == null
							|| cacheService.getGoodsById(billDeliveryDtl.getGoodsId()) == null) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
								"商品编号:" + billDeliveryDtl.getGoodsNo() + "不存在，不能添加");
					}
					billDeliveryDtl.setBillDeliveryId(billDeliveryReqDto.getId());
					billDeliveryDtl.setAssignStlFlag(BaseConsts.ZERO); // 0-不指定库存
					billDeliveryDtl.setCreator(ServiceSupport.getUser().getChineseName());
					billDeliveryDtl.setCreatorId(ServiceSupport.getUser().getId());
					billDeliveryDtlDao.insert(billDeliveryDtl);
				}
				Integer billDeliveryId = billDeliveryReqDto.getId();
				// 更新销售单金额和数量
				updateBillDeliveryInfo(billDeliveryId);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 根据库存新增销售单明细
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void addBillDeliveryDtlsByStl(BillDeliveryReqDto billDeliveryReqDto) {
		BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(billDeliveryReqDto.getId());
		if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			List<Stl> stlList = billDeliveryReqDto.getStlList();
			if (!CollectionUtils.isEmpty(stlList)) {
				for (Stl stl : stlList) {
					BillDeliveryDtl billDeliveryDtl = new BillDeliveryDtl();
					Integer stlId = stl.getId();
					Stl stlRes = stlService.queryEntityById(stlId);
					billDeliveryDtl.setBillDeliveryId(billDeliveryReqDto.getId());
					billDeliveryDtl.setStlId(stlId);
					billDeliveryDtl.setGoodsId(stlRes.getGoodsId());
					billDeliveryDtl.setBatchNo(stlRes.getBatchNo());
					billDeliveryDtl.setGoodsStatus(stlRes.getGoodsStatus());
					billDeliveryDtl.setRequiredSendNum(stl.getRequiredSendNum());
					billDeliveryDtl.setRequiredSendPrice(stl.getRequiredSendPrice());
					billDeliveryDtl.setPoPrice(stlRes.getPoPrice());
					billDeliveryDtl.setCostPrice(stlRes.getCostPrice());
					billDeliveryDtl.setAssignStlFlag(BaseConsts.ONE); // 1-指定库存
					billDeliveryDtl.setRemark(stlRes.getRemark());
					billDeliveryDtl.setCreator(ServiceSupport.getUser().getChineseName());
					billDeliveryDtl.setCreatorId(ServiceSupport.getUser().getId());
					billDeliveryDtl.setPayPrice(stlRes.getPayPrice());
					billDeliveryDtl.setPayRate(stlRes.getPayRate() == null ? BigDecimal.ZERO : stlRes.getPayRate());
					billDeliveryDtl.setPayTime(stlRes.getPayTime());
					billDeliveryDtl.setPayRealCurrency(stlRes.getPayRealCurrency());
					BigDecimal salePrice = projectItemService.getSalePrice(stlId, billDelivery.getReturnTime());
					billDeliveryDtl.setSaleGuidePrice(salePrice);
					BigDecimal profitPrice = projectItemService.getProfitPriceByStl(stlId,
							billDelivery.getReturnTime());
					billDeliveryDtl.setProfitPrice(profitPrice);
					billDeliveryDtlDao.insert(billDeliveryDtl);
					stlService.lockStlSaleNum(stlRes, billDeliveryDtl.getRequiredSendNum()); // 锁定库存的销售数量
				}
				Integer billDeliveryId = billDeliveryReqDto.getId();
				Integer isChangePrice = 0;
				int count = billDeliveryDao.queryChangePriceCountById(billDeliveryId);
				if (count > 0) {
					isChangePrice = 1;
				}
				// 更新销售单金额和数量
				if (null != billDeliveryId) {
					updateBillDeliveryInfo(billDeliveryId, isChangePrice, null);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 根据出库单明细新增退货单明细
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void addBillDeliveryDtlsByBillOutStore(BillDeliveryReqDto billDeliveryReqDto) {
		BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(billDeliveryReqDto.getId());
		if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			List<BillOutStoreDetailReqDto> billOutStoreDetailReqDtoList = billDeliveryReqDto
					.getBillOutStoreDetailReqDtoList();
			if (!CollectionUtils.isEmpty(billOutStoreDetailReqDtoList)) {
				for (BillOutStoreDetailReqDto billOutStoreDetailReqDto : billOutStoreDetailReqDtoList) {
					BillDeliveryDtl billDeliveryDtl = new BillDeliveryDtl();
					Integer billOutStorePickDtlId = billOutStoreDetailReqDto.getBillOutStorePickDtlId();

					BillOutStorePickDtl billOutStorePickDtl = billOutStorePickDtlDao
							.queryEntityById(billOutStorePickDtlId);
					billDeliveryDtl.setBillDeliveryId(billDeliveryReqDto.getId());
					billDeliveryDtl.setBillOutStoreId(billOutStorePickDtl.getBillOutStoreId());
					billDeliveryDtl.setBillOutStoreDtlId(billOutStorePickDtl.getBillOutStoreDtlId());
					billDeliveryDtl.setBillOutStorePickDtlId(billOutStorePickDtlId);
					billDeliveryDtl.setRequiredSendNum(
							DecimalUtil.multiply(new BigDecimal("-1"), billOutStoreDetailReqDto.getRequiredSendNum()));
					billDeliveryDtl.setRequiredSendPrice(billOutStoreDetailReqDto.getRequiredSendPrice());
					billDeliveryDtl.setGoodsId(billOutStorePickDtl.getGoodsId());
					billDeliveryDtl.setBatchNo(billOutStorePickDtl.getBatchNo());
					billDeliveryDtl.setGoodsStatus(billOutStorePickDtl.getGoodsStatus());
					billDeliveryDtl.setPoPrice(billOutStorePickDtl.getPoPrice());
					billDeliveryDtl.setCostPrice(billOutStorePickDtl.getCostPrice());
					billDeliveryDtl.setAssignStlFlag(BaseConsts.ONE); // 1-指定库存
					billDeliveryDtl.setRemark(billOutStorePickDtl.getRemark());
					billDeliveryDtl.setCreator(ServiceSupport.getUser().getChineseName());
					billDeliveryDtl.setCreatorId(ServiceSupport.getUser().getId());
					billDeliveryDtl.setPayPrice(billOutStorePickDtl.getPayPrice());
					billDeliveryDtl.setPayRate(billOutStorePickDtl.getPayRate() == null ? BigDecimal.ZERO
							: billOutStorePickDtl.getPayRate());
					billDeliveryDtl.setPayRealCurrency(billOutStorePickDtl.getPayRealCurrency());
					billDeliveryDtl.setPayTime(billOutStorePickDtl.getPayTime());
					billDeliveryDtl.setStlId(billOutStorePickDtl.getStlId());
					// TODO 待确定
					Integer billOutStoreDtlId = billOutStorePickDtl.getBillOutStoreDtlId();
					BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao.queryEntityById(billOutStoreDtlId);
					Integer billDeliveryDtlId = billOutStoreDtl.getBillDeliveryDtlId();
					BillDeliveryDtl oldBillDeliveryDtl = billDeliveryDtlDao.queryEntityById(billDeliveryDtlId);
					billDeliveryDtl.setSaleGuidePrice(oldBillDeliveryDtl.getSaleGuidePrice());
					billDeliveryDtl.setProfitPrice(oldBillDeliveryDtl.getProfitPrice());
					billDeliveryDtl.setOriginSendPrice(oldBillDeliveryDtl.getRequiredSendPrice());
					billDeliveryDtlDao.insert(billDeliveryDtl);
					// 更新退货数量
					updateReturnNum(billDeliveryDtl, billDeliveryDtl, billOutStorePickDtl, BaseConsts.ONE);
				}
				Integer billDeliveryId = billDeliveryReqDto.getId();
				Integer isChangePrice = 0;
				int count = billDeliveryDao.queryReturnChangePriceCountById(billDeliveryId);
				if (count > 0) {
					isChangePrice = 1;
				}
				// 更新销售单金额和数量
				if (null != billDeliveryId) {
					updateBillDeliveryInfo(billDeliveryId, isChangePrice, null);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 更新出库单拣货明细的退货数量
	 * 
	 * @param oldBillDeliveryDtl
	 *            旧的退货明细
	 * @param newBillDeliveryDtl
	 *            新退货明细
	 * @param billOutStorePickDtl
	 *            出库单拣货明细
	 * @param operateFlag
	 *            1-新增 2-修改 3-删除
	 */
	public void updateReturnNum(BillDeliveryDtl oldBillDeliveryDtl, BillDeliveryDtl newBillDeliveryDtl,
			BillOutStorePickDtl billOutStorePickDtl, Integer operateFlag) {
		if (null == billOutStorePickDtl) {
			billOutStorePickDtl = billOutStorePickDtlDao.queryEntityById(oldBillDeliveryDtl.getBillOutStorePickDtlId());
		}
		if (null != billOutStorePickDtl) {
			// 更新出库单拣货明细的退货数量
			BigDecimal returnNum = (null == billOutStorePickDtl.getReturnNum() ? BigDecimal.ZERO
					: billOutStorePickDtl.getReturnNum().abs());
			BigDecimal pickupNum = (null == billOutStorePickDtl.getPickupNum() ? BigDecimal.ZERO
					: billOutStorePickDtl.getPickupNum().abs());

			BillOutStorePickDtl billOutStorePickDtl2 = new BillOutStorePickDtl();
			billOutStorePickDtl2.setId(oldBillDeliveryDtl.getBillOutStorePickDtlId());
			BigDecimal oldRequiredSendNum = oldBillDeliveryDtl.getRequiredSendNum().abs();
			BigDecimal newRequiredSendNum = newBillDeliveryDtl.getRequiredSendNum().abs();
			BigDecimal newReturnNum = BigDecimal.ZERO;
			if (operateFlag.equals(BaseConsts.ONE)) { // 1-新增
				newReturnNum = DecimalUtil.add(returnNum, newRequiredSendNum);
				if (DecimalUtil.gt(newReturnNum, pickupNum)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "退货数量超过出库明细的可退货数量，无法操作！");
				}
			}
			if (operateFlag.equals(BaseConsts.TWO)) { // 2-修改
				newReturnNum = DecimalUtil.add(DecimalUtil.subtract(returnNum, oldRequiredSendNum), newRequiredSendNum);
				if (DecimalUtil.gt(newReturnNum, pickupNum)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "退货数量超过出库明细的可退货数量，无法操作！");
				}
			}
			if (operateFlag.equals(BaseConsts.THREE)) { // 3-删除
				newReturnNum = DecimalUtil.subtract(returnNum, newRequiredSendNum);
				if (DecimalUtil.lt(newReturnNum, BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "退货数量超过出库明细的可退货数量，无法操作！");
				}
			}
			billOutStorePickDtl2.setReturnNum(newReturnNum);
			billOutStorePickDtlDao.updateById(billOutStorePickDtl2);
		}
		// 更新出库单的退货数量
		billOutStoreDao.updateReturnNum(oldBillDeliveryDtl.getBillOutStoreId());
	}

	/**
	 * 根据excel导入生成销售单明细
	 * 
	 * @param billDelivery
	 */
	public void addBillDeliveryDtlsByImport(BillDelivery billDelivery,
			List<BillDeliveryDtlExcel> billDeliveryDtlExcelList) {
		for (BillDeliveryDtlExcel billDeliveryDtlExcel : billDeliveryDtlExcelList) {
			// 查找明细库存数量，若足够可以添加，若不足够，则导入不成功
			StlSearchReqDto stlSearchReqDto = new StlSearchReqDto();
			stlSearchReqDto.setProjectId(billDelivery.getProjectId());
			stlSearchReqDto.setWarehouseId(billDelivery.getWarehouseId());
			stlSearchReqDto.setCustomerId(billDelivery.getCustomerId());
			stlSearchReqDto.setGoodsId(billDeliveryDtlExcel.getGoodsId());
			stlSearchReqDto.setCurrencyType(billDelivery.getCurrencyType());
			stlSearchReqDto.setPreOrderNo(billDeliveryDtlExcel.getOrderNo());
			stlSearchReqDto.setBatchNo(billDeliveryDtlExcel.getBatchNo());
			stlSearchReqDto.setQuerySource(BaseConsts.FIVE);
			stlSearchReqDto.setIsSameCustomer(BaseConsts.ONE);
			stlSearchReqDto.setIsExistPay(BaseConsts.ONE);
			Result<StlSum> result = stlService.queryAvailableStlByGoodsId(stlSearchReqDto);
			StlSum stlSum = result.getItems();
			BigDecimal sendNum = new BigDecimal(billDeliveryDtlExcel.getRequiredSendNum());
			if (null != stlSum && stlSum.getTotalAvailableNum().compareTo(sendNum) > 0) {
				BigDecimal remainNum = sendNum; // 剩余数量

				List<Stl> stls = stlService.queryStl4FIFO(stlSearchReqDto); // 按条件查询库存
				if (DecimalUtil.gt(remainNum, BigDecimal.ZERO)) {// 剩余数量大于0
					for (Stl stl : stls) {
						BigDecimal outNum = BigDecimal.ZERO;
						BigDecimal availableNum = DecimalUtil.subtract(DecimalUtil.add(stl.getAvailableNum(), sendNum),
								BigDecimal.ZERO);
						if (DecimalUtil.ge(availableNum, remainNum)) { // 可用库存大于等于剩余数量
							outNum = remainNum;
						} else { // 可用库存小于剩余数量
							outNum = stl.getAvailableNum();
						}
						addBillDeliveryDtlByImport(billDelivery, billDeliveryDtlExcel, stl, outNum); // 新增销售明细

						remainNum = DecimalUtil.subtract(remainNum, outNum); // 更新剩余数量
						if (DecimalUtil.eq(remainNum, BigDecimal.ZERO)) { // 剩余数量为0
							break;
						}
					}
				}
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单【" + billDeliveryDtlExcel.getOrderNo() + "】，商品【 "
						+ billDeliveryDtlExcel.getGoodsNo() + "】库存不足");
			}
		}
		Integer billDeliveryId = billDelivery.getId();
		Integer isChangePrice = 0;
		int count = billDeliveryDao.queryChangePriceCountById(billDeliveryId);
		if (count > 0) {
			isChangePrice = 1;
		}
		// 更新销售单金额和数量
		if (null != billDeliveryId) {
			updateBillDeliveryInfo(billDeliveryId, isChangePrice, null);
		}
	}

	public void addBillDeliveryDtlByImport(BillDelivery billDelivery, BillDeliveryDtlExcel billDeliveryDtlExcel,
			Stl stl, BigDecimal outNum) {
		BillDeliveryDtl billDeliveryDtl = new BillDeliveryDtl();
		billDeliveryDtl.setId(null); // ID重置
		billDeliveryDtl.setBillDeliveryId(billDelivery.getId());
		billDeliveryDtl.setGoodsId(billDeliveryDtlExcel.getGoodsId());
		billDeliveryDtl.setRequiredSendNum(outNum);
		if (StringUtils.isBlank(billDeliveryDtlExcel.getRequiredSendPrice())) {
			billDeliveryDtl
					.setRequiredSendPrice(projectItemService.getSalePrice(stl.getId(), billDelivery.getReturnTime()));
		} else {
			billDeliveryDtl.setRequiredSendPrice(new BigDecimal(billDeliveryDtlExcel.getRequiredSendPrice()));
		}
		billDeliveryDtl.setStlId(stl.getId());
		billDeliveryDtl.setAssignStlFlag(BaseConsts.ONE); // 1-指定库存
		billDeliveryDtl.setBatchNo(stl.getBatchNo());
		billDeliveryDtl.setGoodsStatus(stl.getGoodsStatus());
		billDeliveryDtl.setCostPrice(null == stl.getCostPrice() ? BigDecimal.ZERO : stl.getCostPrice());
		billDeliveryDtl.setPoPrice(null == stl.getPoPrice() ? BigDecimal.ZERO : stl.getPoPrice());
		billDeliveryDtl.setCustomerId(stl.getCustomerId());
		billDeliveryDtl.setCreator(ServiceSupport.getUser().getChineseName());
		billDeliveryDtl.setCreatorId(ServiceSupport.getUser().getId());
		billDeliveryDtl.setPayPrice(stl.getPayPrice());
		billDeliveryDtl.setPayRate(stl.getPayRate() == null ? BigDecimal.ZERO : stl.getPayRate());
		billDeliveryDtl.setPayRealCurrency(stl.getPayRealCurrency());
		billDeliveryDtl.setPayTime(stl.getPayTime());
		BigDecimal salePrice = projectItemService.getSalePrice(stl.getId(), billDelivery.getReturnTime());
		billDeliveryDtl.setSaleGuidePrice(salePrice);
		BigDecimal profitPrice = projectItemService.getProfitPriceByStl(stl.getId(), billDelivery.getReturnTime());
		billDeliveryDtl.setProfitPrice(profitPrice);
		billDeliveryDtlDao.insert(billDeliveryDtl);
		stlService.lockStlSaleNum(stl, billDeliveryDtl.getRequiredSendNum()); // 锁定库存的销售数量
	}

	/**
	 * 更新销售单明细
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void updateBillDeliveryDtls(BillDeliveryReqDto billDeliveryReqDto) {
		BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(billDeliveryReqDto.getId());
		if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryReqDto.getBillDeliveryDtlList();
			if (!CollectionUtils.isEmpty(billDeliveryDtlList)) {
				for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtlList) {
					BillDeliveryDtl oldBillDeliveryDtl = billDeliveryDtlDao.queryEntityById(billDeliveryDtl.getId());
					billDeliveryDtlDao.updateById(billDeliveryDtl);
					if (billDelivery.getBillType().equals(BaseConsts.ONE)) { // 1-销售
						if (null != oldBillDeliveryDtl.getStlId()) {
							// 释放库存的销售数量
							stlService.releaseStlSaleNum(billDeliveryDtl.getStlId(),
									oldBillDeliveryDtl.getRequiredSendNum());
							// 锁定库存的销售数量
							stlService.lockStlSaleNum(billDeliveryDtl.getStlId(), billDeliveryDtl.getRequiredSendNum());
						}
					} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
						// 更新退货数量
						updateReturnNum(oldBillDeliveryDtl, billDeliveryDtl, null, BaseConsts.TWO);
					}
				}
				Integer billDeliveryId = billDeliveryReqDto.getId();
				Integer isChangePrice = 0;
				int count = 0;
				if (billDelivery.getBillType().equals(BaseConsts.ONE)) { // 1-销售
					count = billDeliveryDao.queryChangePriceCountById(billDeliveryId);
				} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
					count = billDeliveryDao.queryReturnChangePriceCountById(billDeliveryId);
				}
				if (count > 0) {
					isChangePrice = 1;
				}
				// 更新销售单金额和数量
				updateBillDeliveryInfo(billDeliveryId, isChangePrice, null);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_UPDATE_STATUS_ERROR);
		}
	}

	/**
	 * 删除销售明细
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public void deleteBillDeliveryDtlsByIds(BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		List<Integer> ids = billDeliveryDtlSearchReqDto.getIds();
		Integer billDeliveryId = billDeliveryDtlSearchReqDto.getBillDeliveryId();
		if (!CollectionUtils.isEmpty(ids) && null != billDeliveryId) {
			BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(billDeliveryId);
			if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
				for (Integer id : ids) {
					BillDeliveryDtl billDeliveryDtl = billDeliveryDtlDao.queryEntityById(id);
					billDeliveryDtlDao.deleteById(id);
					if (billDelivery.getBillType().equals(BaseConsts.ONE)
							|| billDelivery.getBillType() == BaseConsts.THREE) { // 1-销售
						if (null != billDeliveryDtl.getStlId()) {
							// 释放库存的销售数量
							stlService.releaseStlSaleNum(billDeliveryDtl.getStlId(),
									billDeliveryDtl.getRequiredSendNum());
						}
					} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
						// 更新退货数量
						updateReturnNum(billDeliveryDtl, billDeliveryDtl, null, BaseConsts.THREE);
					}
				}
				Integer isChangePrice = 0;
				int count = 0;
				if (billDelivery.getBillType().equals(BaseConsts.ONE)
						|| billDelivery.getBillType() == BaseConsts.THREE) { // 1-销售
					count = billDeliveryDao.queryChangePriceCountById(billDeliveryId);
				} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
					count = billDeliveryDao.queryReturnChangePriceCountById(billDeliveryId);
				}
				if (count > 0) {
					isChangePrice = 1;
				}
				// 更新销售单金额和数量
				updateBillDeliveryInfo(billDeliveryId, isChangePrice, null);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_DELIVERY_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 更新销售明细信息
	 * 
	 * @param billDeliveryId
	 */
	public void updateBillDeliveryInfo(Integer billDeliveryId) {
		updateBillDeliveryInfo(billDeliveryId, null, null);
	}

	/**
	 * 更新销售明细信息
	 * 
	 * @param billDeliveryId
	 */
	public void updateBillDeliveryInfo(Integer billDeliveryId, Date returnTime) {
		updateBillDeliveryInfo(billDeliveryId, null, returnTime);
	}

	/**
	 * 更新销售明细信息
	 * 
	 * @param billDeliveryId
	 * @param isChangePrice
	 *            是否改价
	 */
	public void updateBillDeliveryInfo(Integer billDeliveryId, Integer isChangePrice, Date returnTime) {
		BillDeliveryDtl billDeliveryDtlReq = new BillDeliveryDtl();
		billDeliveryDtlReq.setBillDeliveryId(billDeliveryId);
		BillDeliveryDtlSum billDeliveryDtlSum = billDeliveryDtlDao.querySumByBillDeliveryId(billDeliveryDtlReq);

		if (null != billDeliveryDtlSum) {
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billDeliveryId);
			billDelivery.setRequiredSendNum(null == billDeliveryDtlSum.getRequiredSendNum() ? BigDecimal.ZERO
					: billDeliveryDtlSum.getRequiredSendNum());
			billDelivery.setRequiredSendAmount(null == billDeliveryDtlSum.getRequiredSendAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billDeliveryDtlSum.getRequiredSendAmount()));
			billDelivery.setCostAmount(null == billDeliveryDtlSum.getCostAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billDeliveryDtlSum.getCostAmount()));
			billDelivery.setPoAmount(null == billDeliveryDtlSum.getPoAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billDeliveryDtlSum.getPoAmount()));
			billDelivery.setPayAmount(null == billDeliveryDtlSum.getPayAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billDeliveryDtlSum.getPayAmount()));
			if (null != isChangePrice) {
				billDelivery.setIsChangePrice(isChangePrice);
			}
			if (null != returnTime) {
				billDelivery.setReturnTime(returnTime);
			}
			billDeliveryDao.updateById(billDelivery);
		}
	}

	/**
	 * 导入销售单明细Excel
	 * 
	 * @param importFile
	 */
	public void importBillDeliveryDtlExcel(BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto,
			MultipartFile importFile) {
		List<BillDeliveryDtlExcel> billDeliveryDtlExcelList = Lists.newArrayList();
		Map<String, List<BillDeliveryDtlExcel>> beans = Maps.newHashMap();
		beans.put("billDeliveryDtlExcelList", billDeliveryDtlExcelList);
		ExcelService.resolverExcel(importFile, "/excel/sale/billDelivery/billDeliveryDtl.xml", beans);
		// 业务逻辑处理
		billDeliveryDtlExcelList = (List<BillDeliveryDtlExcel>) beans.get("billDeliveryDtlExcelList");
		if (!CollectionUtils.isEmpty(billDeliveryDtlExcelList)) {
			if (billDeliveryDtlExcelList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			Integer billDeliveryId = billDeliveryDtlSearchReqDto.getBillDeliveryId();
			BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(billDeliveryId);
			if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
				String projectName = cacheService.showProjectNameById(billDelivery.getProjectId());
				for (BillDeliveryDtlExcel billDeliveryDtlExcel : billDeliveryDtlExcelList) {
					String orderNo = (null == billDeliveryDtlExcel.getOrderNo() ? ""
							: billDeliveryDtlExcel.getOrderNo().trim());
					if (StringUtils.isBlank(orderNo)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单编号不能为空");
					}
					String goodsNo = (null == billDeliveryDtlExcel.getGoodsNo() ? ""
							: billDeliveryDtlExcel.getGoodsNo().trim());
					if (StringUtils.isBlank(goodsNo)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号不能为空");
					}
					String requiredSendNum = billDeliveryDtlExcel.getRequiredSendNum();
					if (StringUtils.isBlank(requiredSendNum)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售数量不能为空");
					}
					BigDecimal sendNum = BigDecimal.ZERO;
					try {
						sendNum = new BigDecimal(requiredSendNum);
					} catch (Exception e) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售数量格式错误");
					}
					if (sendNum.compareTo(BigDecimal.ZERO) <= 0) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售数量必须大于零");
					}
					if (StringUtils.isNotBlank(billDeliveryDtlExcel.getRequiredSendPrice())) {
						try {
							new BigDecimal(billDeliveryDtlExcel.getRequiredSendPrice());
						} catch (Exception e) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售价格式错误");
						}
					}
					BaseGoods baseGoods = cacheService.getGoodsByPidAndNo(billDelivery.getProjectId(),
							billDeliveryDtlExcel.getGoodsNo());
					if (null == baseGoods) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
								"项目【" + projectName + "】下商品【" + goodsNo + "】不存在");
					}
					billDeliveryDtlExcel.setGoodsId(baseGoods.getId());
				}
				addBillDeliveryDtlsByImport(billDelivery, billDeliveryDtlExcelList);
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单已提交，不能导入");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入销售单明细不能为空");
		}
	}

	public boolean isOverBillDeliveryDtlMaxLine(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billDeliveryDtlDao.queryCountByBillDeliveryCon(billDeliverySearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("销售单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillDeliveryDtlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_delivery_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_15);
			asyncExcelService.addAsyncExcel(billDeliverySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillDeliveryDtlExport(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillDeliveryDtlExtResDto> billDeliveryDtlExtResDtoList = queryAllBillDeliveryDtlExtResultsByBillDeliveryCon(
				billDeliverySearchReqDto);
		model.put("billDeliveryDtlList", billDeliveryDtlExtResDtoList);
		return model;
	}

	public boolean isOverBillReturnDtlMaxLine(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billDeliveryDtlDao.queryCountByBillDeliveryCon(billDeliverySearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("销售退货单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillReturnDtlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_return_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_25);
			asyncExcelService.addAsyncExcel(billDeliverySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillReturnDtlExport(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillDeliveryDtlExtResDto> billDeliveryDtlExtResDtoList = queryAllBillDeliveryDtlExtResultsByBillDeliveryCon(
				billDeliverySearchReqDto);
		model.put("billDeliveryDtlList", billDeliveryDtlExtResDtoList);
		return model;
	}

	/**
	 * 销售单合同打印明细查询
	 *
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillDeliveryDtlResDto> queryAllBillDeliveryDtlPrintByBillDeliveryId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		PageResult<BillDeliveryDtlResDto> result = new PageResult<BillDeliveryDtlResDto>();
		List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryDtlDao
				.queryResultsByBillDeliveryId(billDeliveryDtlSearchReqDto.getBillDeliveryId());
		BillDeliveryDtlResDto billDeliveryDtlResDtoSum = new BillDeliveryDtlResDto();
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryDtlSearchReqDto.getBillDeliveryId());
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList = convertToResDto(billDelivery, billDeliveryDtlList,
				billDeliveryDtlResDtoSum, false, null);
		if (!CollectionUtils.isEmpty(billDeliveryDtlResDtoList)) {
			for (BillDeliveryDtl deliveryDtl : billDeliveryDtlList) {
				BigDecimal countPayCurtAmount = BigDecimal.ZERO;
				for (BillDeliveryDtlResDto billDeliveryDtlResDto : billDeliveryDtlResDtoList) {
					if (billDelivery.getStatus() == BaseConsts.FIVE) {// 销售单状态为5
																		// 销售合同打印所需
						BigDecimal payRate = null == deliveryDtl.getPayRate() ? BigDecimal.ONE
								: deliveryDtl.getPayRate();
						billDeliveryDtlResDto.setPayRate(payRate);
						// billDeliveryDtlResDto.setPayCurtPrice(DecimalUtil.multiply(billDeliveryDtlResDto.getRequiredSendPrice(),
						// payRate));
						// 新规则，直接取价格，无需折算汇率
						billDeliveryDtlResDto.setPayCurtPrice(billDeliveryDtlResDto.getRequiredSendPrice());
						/*
						 * billDeliveryDtlResDto.setPayCurtAmount(DecimalUtil.
						 * multiply(DecimalUtil.multiply(billDeliveryDtlResDto.
						 * getRequiredSendPrice(), payRate),
						 * billDeliveryDtlResDto.getRequiredSendNum()));
						 */
						BigDecimal requiredSendAmount = (null == billDeliveryDtlResDto.getRequiredSendAmount()
								? BigDecimal.ZERO : billDeliveryDtlResDto.getRequiredSendAmount());
						billDeliveryDtlResDto.setPayCurtAmount(requiredSendAmount);

						billDeliveryDtlResDto.setCountPayCurtAmount(
								DecimalUtil.add(countPayCurtAmount, billDeliveryDtlResDto.getPayCurtAmount()));
						countPayCurtAmount = DecimalUtil.add(countPayCurtAmount,
								billDeliveryDtlResDto.getPayCurtAmount());
					}
				}
			}
		}
		result.setItems(billDeliveryDtlResDtoList);
		return result;
	}

	/**
	 * 销售单合同/发票打印明细查询（合并打印）
	 *
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillDeliveryDtlResDto> queryAllBillDeliveryDtListPrintByBDId(
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto) {
		PageResult<BillDeliveryDtlResDto> result = new PageResult<BillDeliveryDtlResDto>();
		List<Integer> ids = billDeliveryDtlSearchReqDto.getIds();
		BillDelivery billDelivery = new BillDelivery();
		List<BillDeliveryDtlResDto> billDeliveryDtlResDtoLists = new ArrayList<BillDeliveryDtlResDto>();
		for (Integer i : ids) {
			billDelivery.setId(i);

			List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryDtlDao.queryResultsByBillDeliveryId(i);
			BillDeliveryDtlResDto billDeliveryDtlResDtoSum = new BillDeliveryDtlResDto();

			billDelivery = billDeliveryDao.queryEntityById(billDelivery);
			List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList = convertToResDto(billDelivery, billDeliveryDtlList,
					billDeliveryDtlResDtoSum, false, null);
			if (!CollectionUtils.isEmpty(billDeliveryDtlResDtoList)) {
				for (BillDeliveryDtl deliveryDtl : billDeliveryDtlList) {
					BigDecimal countPayCurtAmount = BigDecimal.ZERO;
					for (BillDeliveryDtlResDto billDeliveryDtlResDto : billDeliveryDtlResDtoList) {
						if (billDelivery.getStatus() == BaseConsts.FIVE) {// 销售单状态为5
																			// 销售合同打印所需
							BigDecimal payRate = null == deliveryDtl.getPayRate() ? BigDecimal.ONE
									: deliveryDtl.getPayRate();
							billDeliveryDtlResDto.setPayRate(payRate);
							// billDeliveryDtlResDto.setPayCurtPrice(DecimalUtil.multiply(billDeliveryDtlResDto.getRequiredSendPrice(),
							// payRate));
							// 新规则，直接取价格，无需折算汇率
							billDeliveryDtlResDto.setPayCurtPrice(billDeliveryDtlResDto.getRequiredSendPrice());
							/*
							 * billDeliveryDtlResDto.setPayCurtAmount(
							 * DecimalUtil.multiply(DecimalUtil.multiply(
							 * billDeliveryDtlResDto.getRequiredSendPrice(),
							 * payRate),
							 * billDeliveryDtlResDto.getRequiredSendNum()));
							 */
							BigDecimal requiredSendAmount = (null == billDeliveryDtlResDto.getRequiredSendAmount()
									? BigDecimal.ZERO : billDeliveryDtlResDto.getRequiredSendAmount());
							billDeliveryDtlResDto.setPayCurtAmount(requiredSendAmount);

							billDeliveryDtlResDto.setCountPayCurtAmount(
									DecimalUtil.add(countPayCurtAmount, billDeliveryDtlResDto.getPayCurtAmount()));
							// 销售单号
							billDeliveryDtlResDto.setBillNo(billDelivery.getBillNo());
							countPayCurtAmount = DecimalUtil.add(countPayCurtAmount,
									billDeliveryDtlResDto.getPayCurtAmount());

						}
					}
				}
			}
			// 增加
			billDeliveryDtlResDtoLists.addAll(billDeliveryDtlResDtoList);
		}
		// 计算总金额
		BigDecimal countPayCurtAmount = BigDecimal.ZERO;
		for (BillDeliveryDtlResDto billDeliveryDtlResDto : billDeliveryDtlResDtoLists) {
			billDeliveryDtlResDto.setCountPayCurtAmount(
					DecimalUtil.add(countPayCurtAmount, billDeliveryDtlResDto.getPayCurtAmount()));
			countPayCurtAmount = DecimalUtil.add(countPayCurtAmount, billDeliveryDtlResDto.getPayCurtAmount());
		}
		result.setItems(billDeliveryDtlResDtoLists);
		return result;
	}

}
