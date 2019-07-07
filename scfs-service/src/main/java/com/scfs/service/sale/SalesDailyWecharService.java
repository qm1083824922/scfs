package com.scfs.service.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.dao.common.CurrencyRateDao;
import com.scfs.dao.sale.SalesDailyWecharDao;
import com.scfs.domain.common.entity.CurrencyRate;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.SalesDailyWecharReqDto;
import com.scfs.domain.sale.dto.resp.SalesDailyWecharResDto;
import com.scfs.domain.sale.entity.SalesDailyWechar;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.service.base.user.UserWechatService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 	 
 *  File: SalesDailyWecharService.java
 *  Description:销售日报微信推送
 *  TODO
 *  Date,					Who,				
 *  2017年10月26日			Administrator
 *
 * </pre>
 */
@Service
public class SalesDailyWecharService {
	private final static Logger LOGGER = LoggerFactory.getLogger(SalesDailyWecharService.class);
	@Autowired
	private SalesDailyWecharDao salesDailyWecharDao;
	@Autowired
	private CurrencyRateDao currencyRateDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private UserWechatService userWechatService;
	@Value("${domain}")
	private String domain;

	/**
	 * 获取昨日发送信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public Result<SalesDailyWecharResDto> querSalesDailyWechar(SalesDailyWecharReqDto reqDto) {
		Result<SalesDailyWecharResDto> result = new Result<SalesDailyWecharResDto>();
		SalesDailyWecharResDto model = convertResDto(salesDailyWecharDao.queryEntityById(reqDto.getId()));
		result.setItems(model);
		return result;
	}

	/**
	 * 发送微信消息
	 */
	public void realSendWechatMsg() {
		List<Integer> departmengUser = salesDailyWecharDao.queryUserByRole(BaseConsts.NINE);// 获取所有部门负责人用户
		if (CollectionUtils.isNotEmpty(departmengUser)) {
			Date currDate = new Date();
			Date yesterday = DateFormatUtils.beforeDay(currDate, BaseConsts.ONE);// 获取昨日时间
			String dailyDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.beforeDay(currDate, BaseConsts.ONE));
			Date oneDay = DateFormatUtils.getNowDateFristDay(yesterday);// 获取1号
			String no = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, oneDay);
			// 获取当月
			SalesDailyWecharReqDto reqDtoMonth = new SalesDailyWecharReqDto();
			reqDtoMonth.setStartDate(no);
			reqDtoMonth.setEndDate(dailyDate);
			// 获取昨日
			SalesDailyWecharReqDto reqDtoDaily = new SalesDailyWecharReqDto();
			reqDtoDaily.setStartDate(dailyDate);
			reqDtoDaily.setEndDate(dailyDate);
			for (Integer departmentUserId : departmengUser) {
				reqDtoMonth.setDepartmentUserId(departmentUserId);
				reqDtoDaily.setDepartmentUserId(departmentUserId);
				SalesDailyWechar result = new SalesDailyWechar();
				result.setDepartmentUserId(departmentUserId);
				result.setDailyDate(dailyDate);
				List<SalesDailyWechar> dailySalesList = salesDailyWecharDao.querySaleAmount(reqDtoDaily);// 日销售额
				if (CollectionUtils.isNotEmpty(dailySalesList)) {
					BigDecimal dailySales = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : dailySalesList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							dailySales = DecimalUtil.add(dailySales,
									DecimalUtil.multiply(dailyWechar.getMonthSales(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setDailySales(dailySales);
				}
				List<SalesDailyWechar> monthSalesList = salesDailyWecharDao.querySaleAmount(reqDtoMonth);// 月销售额
				if (CollectionUtils.isNotEmpty(monthSalesList)) {
					BigDecimal monthSales = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : monthSalesList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							monthSales = DecimalUtil.add(monthSales,
									DecimalUtil.multiply(dailyWechar.getMonthSales(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setMonthSales(monthSales);
				}
				List<SalesDailyWechar> dailyPayList = salesDailyWecharDao.queryPayAmount(reqDtoDaily);// 日付款额
				if (CollectionUtils.isNotEmpty(dailyPayList)) {
					BigDecimal dailyPay = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : dailyPayList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							dailyPay = DecimalUtil.add(dailyPay,
									DecimalUtil.multiply(dailyWechar.getMonthPay(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setDailyPay(dailyPay);
				}
				List<SalesDailyWechar> monthPayList = salesDailyWecharDao.queryPayAmount(reqDtoMonth);// 月付款额
				if (CollectionUtils.isNotEmpty(monthPayList)) {
					BigDecimal monthPay = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : monthPayList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							monthPay = DecimalUtil.add(monthPay,
									DecimalUtil.multiply(dailyWechar.getMonthPay(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setMonthPay(monthPay);
				}
				List<SalesDailyWechar> dailyPaymentList = salesDailyWecharDao.queryPayment(reqDtoDaily);// 日回款额
				if (CollectionUtils.isNotEmpty(dailyPaymentList)) {
					BigDecimal dailyPayment = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : dailyPaymentList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							dailyPayment = DecimalUtil.add(dailyPayment,
									DecimalUtil.multiply(dailyWechar.getMonthPayment(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setDailyPayment(dailyPayment);
				}
				List<SalesDailyWechar> monthPaymentList = salesDailyWecharDao.queryPayment(reqDtoMonth);// 月回款额
				if (CollectionUtils.isNotEmpty(monthPaymentList)) {
					BigDecimal monthPayment = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : monthPaymentList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							monthPayment = DecimalUtil.add(monthPayment,
									DecimalUtil.multiply(dailyWechar.getMonthPayment(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setMonthPayment(monthPayment);
				}
				BigDecimal stlNum = salesDailyWecharDao.queryStlNum(reqDtoMonth);// 库存数量
				if (stlNum != null) {
					result.setStlNum(stlNum);
				}
				List<SalesDailyWechar> stlAmountList = salesDailyWecharDao.queryStlAmount(reqDtoMonth);// 库存金额
				if (CollectionUtils.isNotEmpty(stlAmountList)) {
					BigDecimal stlAmount = BigDecimal.ZERO;
					for (SalesDailyWechar dailyWechar : stlAmountList) {
						CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(
								BaseConsts.CURRENCY_UNIT_MAP.get(dailyWechar.getCurrnecyType()),
								DateFormatUtils.format(DateFormatUtils.YYYYMM, yesterday));// 获取当月汇率
						if (currencyRate != null) {
							BigDecimal cnyRate = currencyRate.getCnyRate();
							stlAmount = DecimalUtil.add(stlAmount,
									DecimalUtil.multiply(dailyWechar.getStlAmount(), cnyRate));
						} else {
							LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(result));
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
						}
					}
					result.setStlAmount(stlAmount);
				}
				if (result.getDailySales() != BigDecimal.ZERO || result.getDailyPay() != BigDecimal.ZERO
						|| result.getDailyPayment() != BigDecimal.ZERO || result.getMonthSales() != BigDecimal.ZERO
						|| result.getMonthPay() != BigDecimal.ZERO || result.getMonthPayment() != BigDecimal.ZERO
						|| result.getStlAmount() != BigDecimal.ZERO || result.getStlNum() != BigDecimal.ZERO) {// 为0数据则不添加
					result.setCreateAt(currDate);
					result.setSendDate(currDate);
					result.setSendState(BaseConsts.ZERO);
					result.setCurrnecyType(BaseConsts.ONE);// 人民币
					salesDailyWecharDao.insert(result);
					Integer sendResult = sendWecharByUserId(departmentUserId, result.getId());
					if (sendResult != null) {
						SalesDailyWechar upSales = new SalesDailyWechar();
						upSales.setSendState(BaseConsts.ONE);
						upSales.setSendDate(new Date());
						salesDailyWecharDao.update(upSales);
					}
				}
			}
		}
	}

	public Integer sendWecharByUserId(Integer userId, Integer dailyId) {
		Integer result = null;
		List<WechatUser> wechatUsers = userWechatService.queryBindWechatsByUserId(userId);// 发送微信消息
		if (CollectionUtils.isNotEmpty(wechatUsers)) {
			StringBuilder acc = new StringBuilder();
			for (WechatUser wechatUser : wechatUsers) {
				acc.append(wechatUser.getOpenid()).append(",");
			}
			Map<String, String> map = Maps.newHashMap();
			map.put("template_id", "UHt3WWjHBpfEtoEK2UJja23Uv_vCdoMtkr59p_MGqG0");
			map.put("first", "你好！你昨日的销售日报信息请查看：");
			String url = domain + "wechat/html/service/show_sales_daily.html?dailyId=" + dailyId;
			map.put("url", url);
			map.put("keyword1", "销售日报信息");
			map.put("keyword2", "正常");
			map.put("keyword3", "推送昨日销售日报信息");
			map.put("remark", "点击跳转销售日报信息页面查看。");
			String content = JSONObject.toJSONString(map);
			msgContentService.addMsgContent(acc.toString(), null, content, BaseConsts.FOUR);
			result = BaseConsts.ONE;
		}
		return result;
	}

	public List<SalesDailyWecharResDto> convertToResDtos(List<SalesDailyWechar> result) {
		List<SalesDailyWecharResDto> resDtos = new ArrayList<SalesDailyWecharResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtos;
		}
		for (SalesDailyWechar model : result) {
			SalesDailyWecharResDto resDto = convertResDto(model);
			resDtos.add(resDto);
		}
		return resDtos;
	}

	public SalesDailyWecharResDto convertResDto(SalesDailyWechar model) {
		SalesDailyWecharResDto result = new SalesDailyWecharResDto();
		result.setId(model.getId());
		result.setDepartmentUserId(model.getDepartmentUserId());
		result.setDailyDate(model.getDailyDate());
		result.setDailySales(model.getDailySales());
		result.setMonthSales(model.getMonthSales());
		result.setDailyPay(model.getDailyPay());
		result.setMonthPay(model.getMonthPay());
		result.setDailyPayment(model.getDailyPayment());
		result.setMonthPayment(model.getMonthPayment());
		result.setStlAmount(model.getStlAmount());
		result.setStlNum(model.getStlNum());
		result.setCurrnecyType(model.getCurrnecyType());
		result.setCurrnecyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrnecyType() + ""));
		result.setSendState(model.getSendState());
		result.setSendDate(model.getSendDate());
		return result;
	}
}
