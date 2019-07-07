package com.scfs.service.base.exchangeRate;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseExchangeRateDao;
import com.scfs.dao.common.CurrencyRateDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseExchangeRateReqDto;
import com.scfs.domain.base.dto.resp.BaseExchangeRateResDto;
import com.scfs.domain.base.entity.BaseExchangeRate;
import com.scfs.domain.common.entity.CurrencyRate;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.support.ServiceSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BaseExchangeRateService {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseExchangeRateService.class);

	@Autowired
	private BaseExchangeRateDao baseExchangeRateDao;
	@Autowired
	private CurrencyRateDao currencyRateDao;
	@Autowired
	private MsgContentService msgContentService;

	public PageResult<BaseExchangeRateResDto> getBaseExchangeRateList(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		PageResult<BaseExchangeRateResDto> result = new PageResult<BaseExchangeRateResDto>();
		int offSet = PageUtil.getOffSet(baseExchangeRateReqDto.getPage(), baseExchangeRateReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseExchangeRateReqDto.getPer_page());
		List<BaseExchangeRate> exchangeRateList = baseExchangeRateDao.getUseExchangeRateList(baseExchangeRateReqDto,
				rowBounds);
		List<BaseExchangeRateResDto> baseExchangeRateResList = convertToResult(exchangeRateList);
		result.setItems(baseExchangeRateResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseExchangeRateReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseExchangeRateReqDto.getPage());
		result.setPer_page(baseExchangeRateReqDto.getPer_page());
		return result;
	}

	public PageResult<BaseExchangeRateResDto> getBaseExchangeRateHisList(
			BaseExchangeRateReqDto baseExchangeRateReqDto) {
		PageResult<BaseExchangeRateResDto> result = new PageResult<BaseExchangeRateResDto>();
		int offSet = PageUtil.getOffSet(baseExchangeRateReqDto.getPage(), baseExchangeRateReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseExchangeRateReqDto.getPer_page());
		List<BaseExchangeRate> exchangeRateList = baseExchangeRateDao.getHisExchangeRateList(baseExchangeRateReqDto,
				rowBounds);
		List<BaseExchangeRateResDto> baseExchangeRateResList = convertToResult(exchangeRateList);
		result.setItems(baseExchangeRateResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseExchangeRateReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseExchangeRateReqDto.getPage());
		result.setPer_page(baseExchangeRateReqDto.getPer_page());

		return result;
	}

	private List<BaseExchangeRateResDto> convertToResult(List<BaseExchangeRate> exchangeRateList) {
		List<BaseExchangeRateResDto> baseExchangeRateList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(exchangeRateList)) {
			return baseExchangeRateList;
		}
		for (BaseExchangeRate exchangeRate : exchangeRateList) {
			BaseExchangeRateResDto exchangeRateRes = exchangeRateConvertToRes(exchangeRate);
			// 操作集合
			List<CodeValue> operList = getOperList(exchangeRate.getIsError());
			exchangeRateRes.setOpertaList(operList);
			baseExchangeRateList.add(exchangeRateRes);
		}
		return baseExchangeRateList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseExchangeRateResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.EDIT);
			break;

		}
		return opertaList;
	}

	private BaseExchangeRate convertToBaseExchangeRate(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		BaseExchangeRate baseExchangeRate = new BaseExchangeRate();
		baseExchangeRate.setId(baseExchangeRateReqDto.getId());
		baseExchangeRate.setBank(baseExchangeRateReqDto.getBank());
		baseExchangeRate.setCurrency(baseExchangeRateReqDto.getCurrency());
		baseExchangeRate.setForeignCurrency(baseExchangeRateReqDto.getForeignCurrency());
		baseExchangeRate.setCashSellingPrice(baseExchangeRateReqDto.getCashSellingPrice());
		baseExchangeRate.setCashBuyingPrice(baseExchangeRateReqDto.getCashBuyingPrice());
		baseExchangeRate.setDraftSellingPrice(baseExchangeRateReqDto.getDraftSellingPrice());
		baseExchangeRate.setDraftBuyingPrice(baseExchangeRateReqDto.getDraftBuyingPrice());
		baseExchangeRate.setPublishAt(baseExchangeRateReqDto.getPublishAt());
		baseExchangeRate.setPublishBegin(baseExchangeRateReqDto.getPublishBegin());
		baseExchangeRate.setPublishEnd(baseExchangeRateReqDto.getPublishEnd());
		baseExchangeRate.setBackupFrom(baseExchangeRateReqDto.getBackupFrom());
		baseExchangeRate.setBackupTo(baseExchangeRateReqDto.getBackupTo());

		return baseExchangeRate;
	}

	public Result<BaseExchangeRateResDto> queryBaseExchangeRateById(int id) {
		Result<BaseExchangeRateResDto> result = new Result<BaseExchangeRateResDto>();
		BaseExchangeRate baseExchangeRate = baseExchangeRateDao.queryBaseExchangeRateById(id);
		BaseExchangeRateResDto baseExchangeRateResDto = exchangeRateConvertToRes(baseExchangeRate);
		result.setItems(baseExchangeRateResDto);
		return result;
	}

	private BaseExchangeRateResDto exchangeRateConvertToRes(BaseExchangeRate exchangeRate) {
		BaseExchangeRateResDto exchangeRateRes = new BaseExchangeRateResDto();
		exchangeRateRes.setId(exchangeRate.getId());
		exchangeRateRes.setBankId(exchangeRate.getBank());
		exchangeRateRes.setBank(ServiceSupport.getValueByBizCode(BizCodeConsts.BANK_NAME, exchangeRate.getBank()));
		exchangeRateRes.setCurrencyId(exchangeRate.getCurrency());
		exchangeRateRes.setCurrency(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, exchangeRate.getCurrency()));
		exchangeRateRes.setForeignCurrencyId(exchangeRate.getForeignCurrency());
		exchangeRateRes.setForeignCurrency(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				exchangeRate.getForeignCurrency()));
		exchangeRateRes.setCashSellingPrice(exchangeRate.getCashSellingPrice());
		exchangeRateRes.setCashBuyingPrice(exchangeRate.getCashBuyingPrice());
		exchangeRateRes.setDraftSellingPrice(exchangeRate.getDraftSellingPrice());
		exchangeRateRes.setDraftBuyingPrice(exchangeRate.getDraftBuyingPrice());
		exchangeRateRes.setPublishAt(exchangeRate.getPublishAt());
		exchangeRateRes.setBackupAt(exchangeRate.getBackupAt());
		exchangeRateRes.setBackupPerson(exchangeRate.getBackupPerson());
		exchangeRateRes.setCreator(exchangeRate.getCreator());
		exchangeRateRes.setCreateAt(exchangeRate.getCreateAt());
		if (exchangeRate.getIsError() != null) {
			exchangeRateRes.setIsError(ServiceSupport.getValueByBizAndCode(BizCodeConsts.EXCHANGE_RATE_STATUS,
					Integer.toString(exchangeRate.getIsError())));
		}
		return exchangeRateRes;
	}

	public BaseResult addBaseExchangeRate(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		BaseResult result = new BaseResult();
		BaseExchangeRate baseExchangeRate = convertToBaseExchangeRate(baseExchangeRateReqDto);
		baseExchangeRate.setCreateAt(new Date());
		baseExchangeRate.setCreator(ServiceSupport.getUser().getChineseName());
		BaseExchangeRate existExchangeRate = baseExchangeRateDao.queryExchangeRateByExchangeRate(baseExchangeRate);
		if (existExchangeRate != null) {
			result.setMsg("此外币种已存在，不能插入");
			return result;
		}
		baseExchangeRate.setIsDelete(BaseConsts.ZERO);
		baseExchangeRate.setIsError(BaseConsts.ONE);
		baseExchangeRateDao.insert(baseExchangeRate);
		return result;
	}

	public BaseResult updateBaseExchangeRate(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		BaseResult result = new BaseResult();
		// 备份
		BaseExchangeRate backExchangeRate = baseExchangeRateDao
				.queryBaseExchangeRateById(baseExchangeRateReqDto.getId());
		backExchangeRate.setBackupAt(new Date());
		backExchangeRate.setBackupPerson(ServiceSupport.getUser().getChineseName());
		backExchangeRate.setIsDelete(BaseConsts.ONE);
		backExchangeRate.setIsError(BaseConsts.ONE);
		backExchangeRate.setId(null);
		baseExchangeRateDao.insert(backExchangeRate);
		// 后更新
		BaseExchangeRate baseExchangeRate = convertToBaseExchangeRate(baseExchangeRateReqDto);
		baseExchangeRate.setIsError(BaseConsts.ONE);
		baseExchangeRate.setPublishAt(new Date());
		baseExchangeRate.setCreateAt(new Date());
		baseExchangeRateDao.updateById(baseExchangeRate);
		return result;
	}

	/**
	 * 更新汇率并备份
	 *
	 * @param exchangeRateList
	 */
	public void flushRateAndBackup(List<BaseExchangeRate> exchangeRateList) {
		if (CollectionUtils.isNotEmpty(exchangeRateList)) {
			for (BaseExchangeRate exchangeRate : exchangeRateList) {
				exchangeRate.setBackupAt(new Date());
				exchangeRate.setBackupPerson("系统");
				baseExchangeRateDao.backUpExchangeRate(exchangeRate);
				exchangeRate.setBackupPerson(null);
				exchangeRate.setBackupAt(null);
				exchangeRate.setIsDelete(BaseConsts.ZERO);
				baseExchangeRateDao.insert(exchangeRate);
			}

		}

	}

	/**
	 * 抓取不到汇率，所有汇率标识异常
	 */
	public void updateAllException(String bankCode) {
		baseExchangeRateDao.updateAllExcption(bankCode);
	}

	/**
	 * 查询正在使用的汇率信息,根据bank, currency, foreign_currency 查询汇率信息
	 *
	 * @return
	 */
	public BaseExchangeRate queryExchangeRateByExchangeRate(BaseExchangeRate baseExchangeRate) {
		return baseExchangeRateDao.queryExchangeRateByExchangeRate(baseExchangeRate);
	}

	/**
	 * @param bankCode 银行代码
	 * @param currency 原币种
	 * @param aimCurrency 目标币种
	 * @param date 兑换日期 汇率价位:默认现钞买入价
	 * @return
	 */
	public BigDecimal convertCurrency(String bankCode, String currency, String aimCurrency, Date date) {
		return convertCurrency(bankCode, currency, aimCurrency, date, BaseConsts.TWO + "");
	}

	/***
	 * 获取财务汇率
	 * 
	 * @param curreny
	 * @param date
	 * @return
	 */
	public BigDecimal convertCurrency(int curreny, Date date) {
		CurrencyRate currencyRate = currencyRateDao.queryByTheMonthCd(BaseConsts.CURRENCY_UNIT_MAP.get(curreny),
				DateFormatUtils.format(DateFormatUtils.YYYYMM, date));
		if (currencyRate != null) {
			if (currencyRate.getCnyRate() != null) {
				BigDecimal rate = currencyRate.getCnyRate();
				return rate;
			} else {
				LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(date));
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
			}
		} else {
			LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(date));
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
		}
	}

	/**
	 * @param bankCode 银行代码
	 * @param currency 原币种
	 * @param aimCurrency 目标币种
	 * @param date 兑换日期
	 * @param price 汇率价位
	 * @return
	 */
	public BigDecimal convertCurrency(String bankCode, String currency, String aimCurrency, Date date, String price) {
		if (currency.equals(aimCurrency)) {
			return DecimalUtil.ONE;
		}

		String dateStr = formateDate(date);

		BaseExchangeRateReqDto rateReqDto = new BaseExchangeRateReqDto();
		rateReqDto.setBank(bankCode);
		rateReqDto.setCurrency(currency);
		rateReqDto.setForeignCurrency(aimCurrency);
		rateReqDto.setCreateAt(dateStr);
		BaseExchangeRate rate = baseExchangeRateDao.queryExchangeRateByRateReqDto(rateReqDto);
		if (rate != null) {// 正常兑换 人民币兑换美元
			return getRate(rate, price);
		} else {
			// 交叉兑换 港币兑换美元 或者美元兑换港币
			rateReqDto.setCurrency(null);
			BaseExchangeRate aimRate = baseExchangeRateDao.queryExchangeRateByRateReqDto(rateReqDto);
			rateReqDto.setForeignCurrency(currency);
			BaseExchangeRate currencyRate = baseExchangeRateDao.queryExchangeRateByRateReqDto(rateReqDto);
			if (aimRate == null || currencyRate == null) {
				// 逆向兑换 美元兑换人民币 或者港币兑换人民
				rateReqDto.setCurrency(aimCurrency);
				rateReqDto.setForeignCurrency(currency);
				rate = baseExchangeRateDao.queryExchangeRateByRateReqDto(rateReqDto);
				if (rate != null) {
					BigDecimal exchangeRate = getRate(rate, price);
					BigDecimal r = new BigDecimal(1);
					return r.divide(exchangeRate, 4, BigDecimal.ROUND_HALF_UP);
				} else {
					LOGGER.error("查不到转换汇率信息:{}", JSONObject.toJSONString(rateReqDto));
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查不到转换汇率信息,请联系管理员编辑汇率！");
				}
			} else {
				return getRate(transformRate(currencyRate, aimRate), price);
			}
		}
	}

	private static String formateDate(Date date) {
		try {
			String dStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, date);
			Date now = new Date();
			String nowStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, now);
			if (!nowStr.equalsIgnoreCase(dStr)) {// 查询之前指定某一天的汇率
				return dStr;
			} else {// 查询当天的汇率，小于10点取前一天的汇率，否则取当天的汇率
				Date dtmTime = DateUtils.truncate(date, Calendar.MINUTE);
				String str = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD + " 10:05:00", now);
				Date tenNow = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, str);
				if (dtmTime.compareTo(tenNow) < 0) {
					return DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, DateFormatUtils.beforeDay(date, 1));
				} else {
					return DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, date);
				}
			}
		} catch (ParseException e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查询指定某天汇率，日期转换异常");
		}

	}

	private BigDecimal getRate(BaseExchangeRate currencyRate, String price) {
		BigDecimal rateprice = null;
		if (price.equals(BaseConsts.ONE + "")) {
			rateprice = currencyRate.getCashSellingPrice();
		}
		if (price.equals(BaseConsts.TWO + "")) {
			rateprice = currencyRate.getCashBuyingPrice();
		}
		if (price.equals(BaseConsts.THREE + "")) {
			rateprice = currencyRate.getDraftSellingPrice();
		}
		if (price.equals(BaseConsts.FOUR + "")) {
			rateprice = currencyRate.getDraftBuyingPrice();
		}
		return rateprice;
	}

	private BaseExchangeRate transformRate(BaseExchangeRate currencyRate, BaseExchangeRate aimRate) {
		BaseExchangeRate result = new BaseExchangeRate();
		result.setCashBuyingPrice(DecimalUtil.divide(aimRate.getCashBuyingPrice(), currencyRate.getCashBuyingPrice()));
		result.setCashSellingPrice(
				DecimalUtil.divide(aimRate.getCashSellingPrice(), currencyRate.getCashSellingPrice()));
		result.setDraftBuyingPrice(
				DecimalUtil.divide(aimRate.getDraftBuyingPrice(), currencyRate.getDraftBuyingPrice()));
		result.setDraftSellingPrice(
				DecimalUtil.divide(aimRate.getDraftSellingPrice(), currencyRate.getDraftSellingPrice()));
		return result;
	}

	/**
	 * 看汇率是否有异常，和昨天的对比 上下浮动超过10% 异常
	 *
	 * @param rate
	 * @return
	 */
	public boolean isRateExcep(BaseExchangeRate rate) {
		// 查找最近一次的历史汇率
		BaseExchangeRate exchangeRate = queryExchangeRateByExchangeRate(rate);
		if (exchangeRate != null) {
			// 判断汇率上下变动10%
			BigDecimal b = new BigDecimal("0.1");
			BigDecimal subDrafB = DecimalUtil
					.divide(DecimalUtil.subtract(exchangeRate.getDraftBuyingPrice(), rate.getDraftBuyingPrice()),
							rate.getDraftBuyingPrice())
					.abs();
			boolean isExDraftBuyingPrice = DecimalUtil.ge(subDrafB, b);
			BigDecimal subDrafS = DecimalUtil
					.divide(DecimalUtil.subtract(exchangeRate.getDraftSellingPrice(), rate.getDraftSellingPrice()),
							rate.getDraftSellingPrice())
					.abs();
			boolean isExDraftSellingPrice = DecimalUtil.ge(subDrafS, b);
			BigDecimal subCashB = DecimalUtil
					.divide(DecimalUtil.subtract(exchangeRate.getCashBuyingPrice(), rate.getCashBuyingPrice()),
							rate.getCashBuyingPrice())
					.abs();
			boolean isExCashBuyingPrice = DecimalUtil.ge(subCashB, b);
			BigDecimal subCashS = DecimalUtil
					.divide(DecimalUtil.subtract(exchangeRate.getCashSellingPrice(), rate.getCashSellingPrice()),
							rate.getCashSellingPrice())
					.abs();
			boolean isCashSellingPrice = DecimalUtil.ge(subCashS, b);
			if (isExDraftBuyingPrice || isExDraftSellingPrice || isExCashBuyingPrice || isCashSellingPrice) {// 标记异常汇率
				return true;
			}
		}
		return false;
	}

	/**
	 * 发送系统报警
	 * 
	 * @param content
	 */
	public void sendSystemAlarm(final String... content) {
		String msg = MessageFormatter.arrayFormat(BaseConsts.RATE_EXCEPT_MSG, content).getMessage();
		msgContentService.addMsgContentByRoleName(BaseConsts.ATE_EXCEPT_ROLENAME, BaseConsts.RATE_EXCEPT_MSG_SUBJECT, msg, BaseConsts.ONE);
		msgContentService.addMsgContentByRoleName(BaseConsts.ATE_EXCEPT_ROLENAME, BaseConsts.RATE_EXCEPT_MSG_SUBJECT, msg, BaseConsts.TWO);
	}

	/**
	 * 发送微信报警
	 * 
	 * @param content
	 */
	public void sendWebcatAlarm(String title, String url, String content) {
		msgContentService.addWebcatMsgByRoleName(BaseConsts.ATE_EXCEPT_ROLENAME, title, url, content, "");
	}

}
