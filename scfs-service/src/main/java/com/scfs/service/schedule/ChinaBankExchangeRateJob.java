package com.scfs.service.schedule;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.base.entity.BaseExchangeRate;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */
public class ChinaBankExchangeRateJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(ChinaBankExchangeRateJob.class);

	@Value("${china.bank.rate.url}")
	private String chinaBankRateUrl;
	@Autowired
	private BaseExchangeRateService baseExchangeRateService;
	private XPath xPath = XPathFactory.newInstance().newXPath();
	private String usaPath = "//tr[27]/td";// 美元
	private String hkPath = "//tr[10]/td";// 港币
	private String ukPath = "//tr[8]/td";// 欧元
	private String bankName = "中国银行";// 抓取银行

	@PostConstruct
	public void tryConnnect() {
		try {
			String contents = Jsoup.connect(chinaBankRateUrl).get().html();
			LOGGER.info("启动尝试获取中国银行汇率页面内容.");
			//LOGGER.info("启动尝试获取中国银行汇率页面内容：" + contents);
		} catch (Exception e) {
			LOGGER.error("启动尝试获取中国银行汇率页面内容异常，忽略此异常：", e);
		}
	}

	public void execute() {
		long begin = System.currentTimeMillis();
		LOGGER.info("==========开始中国银行抓取汇率==========");
		try {

			String contents = Jsoup.connect(chinaBankRateUrl).timeout(30000).get().html();
			//LOGGER.info("获取中国银行汇率页面内容：" + contents);
			HtmlCleaner hc = new HtmlCleaner();
			TagNode tn = hc.clean(contents);
			DomSerializer domSerializer = new DomSerializer(new CleanerProperties());
			Document dom = domSerializer.createDOM(tn);

			List<BaseExchangeRate> newExchangeRateList = Lists.newArrayList();
			BaseExchangeRate cnyRate = creatHKRate(dom);
			newExchangeRateList.add(cnyRate);
			BaseExchangeRate usdRate = creatUsaRate(dom);
			newExchangeRateList.add(usdRate);

			BaseExchangeRate ukdRate = creatUkRate(dom);
			newExchangeRateList.add(ukdRate);

			baseExchangeRateService.flushRateAndBackup(newExchangeRateList);
		} catch (BaseException baseException) {
			baseExchangeRateService.updateAllException(BaseConsts.TWO + "");
			baseExchangeRateService.sendSystemAlarm(bankName, chinaBankRateUrl);
			baseExchangeRateService.sendWebcatAlarm("SCFS抓取汇率失败", chinaBankRateUrl, "中国银行汇率异常");
			LOGGER.error("抓取中国银行汇率异常：", baseException.getMsg());
		} catch (Exception e) {
			baseExchangeRateService.updateAllException(BaseConsts.TWO + "");
			baseExchangeRateService.sendSystemAlarm(bankName, chinaBankRateUrl);
			baseExchangeRateService.sendWebcatAlarm("SCFS抓取汇率失败", chinaBankRateUrl, "中国银行汇率异常");
			LOGGER.error("抓取中国银行汇率异常：", e);
		}
		LOGGER.info("==========结束抓取中国银行汇率=========耗时:" + (System.currentTimeMillis() - begin) / 1000);
	}

	private BaseExchangeRate creatUsaRate(Document dom) throws Exception {
		BaseExchangeRate rate = new BaseExchangeRate();
		rate.setBank("2");// 2 表示 中国银行 对应人民币
		rate.setCurrency("1");// 1为人民币
		rate.setForeignCurrency("2");// 2为美元
		fillRateByXpath(usaPath, rate, "美元", dom);
		return rate;
	}

	private BaseExchangeRate creatHKRate(Document dom) throws Exception {
		BaseExchangeRate rate = new BaseExchangeRate();
		rate.setBank("2");// 2 表示 中国银行 对应人民币
		rate.setCurrency("1");// 1为人民币
		rate.setForeignCurrency("3");// 3是港币
		fillRateByXpath(hkPath, rate, "港币", dom);
		return rate;
	}

	private BaseExchangeRate creatUkRate(Document dom) throws Exception {
		BaseExchangeRate rate = new BaseExchangeRate();
		rate.setBank("2");// 2 表示 中国银行 对应人民币
		rate.setCurrency("1");// 1为人民币
		rate.setForeignCurrency("4");// 3是港币
		fillRateByXpath(ukPath, rate, "欧元", dom);
		return rate;
	}

	private void fillRateByXpath(String htmlPath, BaseExchangeRate rate, String currucyName, Document dom)
			throws Exception {
		rate.setCreator("系统");
		rate.setCreateAt(new Date());
		rate.setIsError(1);
		rate.setIsDelete(BaseConsts.ZERO);
		Object result = xPath.evaluate(htmlPath, dom, XPathConstants.NODESET);
		if (result instanceof NodeList) {
			NodeList nodeList = (NodeList) result;
			String cName = nodeList.item(0).getTextContent().trim();
			if (!currucyName.equalsIgnoreCase(cName)) {
				LOGGER.error("抓取中国银行汇率异常,【{}】货币名称【{}】不一致", currucyName, cName);
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "货币名称不一致，抓取中国银行汇率异常，" + cName);
			}
			BigDecimal basePrice = new BigDecimal(100);
			BigDecimal draftBuyingPrice = DecimalUtil.divide(new BigDecimal(nodeList.item(1).getTextContent()),
					basePrice);
			BigDecimal draftSellingPrice = DecimalUtil.divide(new BigDecimal(nodeList.item(2).getTextContent()),
					basePrice);
			BigDecimal cashBuyingPrice = DecimalUtil.divide(new BigDecimal(nodeList.item(3).getTextContent()),
					basePrice);
			BigDecimal cashSellingPrice = DecimalUtil.divide(new BigDecimal(nodeList.item(4).getTextContent()),
					basePrice);
			String publishAtStr = nodeList.item(6).getTextContent();
			Date publishAt = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, publishAtStr);
			// 判断当天是否有发布汇率，修改发布时间
			String dStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, publishAt);
			Date now = new Date();
			String nowStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, now);
			if (!nowStr.equalsIgnoreCase(dStr)) {
				rate.setPublishAt(now);
			} else {
				rate.setPublishAt(publishAt);
			}
			rate.setDraftBuyingPrice(draftBuyingPrice);
			rate.setCashBuyingPrice(draftSellingPrice);
			rate.setDraftSellingPrice(cashBuyingPrice);
			rate.setCashSellingPrice(cashSellingPrice);
			boolean isRateExcp = baseExchangeRateService.isRateExcep(rate);
			if (isRateExcp) {
				LOGGER.error("汇率浮动超过10%" + JSONObject.toJSONString(rate));
				rate.setIsError(2);
				baseExchangeRateService.sendSystemAlarm(bankName, chinaBankRateUrl);
			}
		}
	}

}
