package com.scfs.service.schedule;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
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
 * Created by Administrator on 2016/11/22.
 */
public class HsbcExchangeRateJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(HsbcExchangeRateJob.class);

	@Value("${hsbc.rate.url}")
	private String hsbcExchangeRateUrl;
	@Autowired
	private BaseExchangeRateService baseExchangeRateService;
	private XPath xPath = XPathFactory.newInstance().newXPath();
	private String usdPath = "//a[@title='View 美元 History']/../../td";// 美元
	private String cnyPath = "//a[@title='View 人民幣 History']/../../td";// 人民币
	private String ukPath = "//a[@title='View 歐羅 History']/../../td";// 欧元
	private String bankName = "汇丰银行";// 抓取银行

	@PostConstruct
	public void tryConnnect() {
		try {
			String contents = Jsoup.connect(hsbcExchangeRateUrl).get().html();
			LOGGER.info("启动尝试获取汇丰银行汇率页面内容.");
			//LOGGER.info("启动尝试获取汇丰银行汇率页面内容：" + contents);
		} catch (Exception e) {
			LOGGER.error("启动尝试获取汇丰银行汇率页面内容异常，忽略此异常：", e);
		}
	}

	public void execute() {
		long begin = System.currentTimeMillis();
		LOGGER.info("==========开始抓取汇丰银行汇率==========");
		try {
			String contents = Jsoup.connect(hsbcExchangeRateUrl).timeout(30000).get().html();
			//LOGGER.info("获取汇丰银行汇率页面内容：" + contents);
			HtmlCleaner hc = new HtmlCleaner();
			TagNode tn = hc.clean(contents);
			DomSerializer domSerializer = new DomSerializer(new CleanerProperties());
			Document dom = domSerializer.createDOM(tn);

			List<BaseExchangeRate> newExchangeRateList = Lists.newArrayList();
			BaseExchangeRate cnyRate = creatCnyRate(dom);
			newExchangeRateList.add(cnyRate);
			BaseExchangeRate usdRate = creatUSAdRate(dom);
			newExchangeRateList.add(usdRate);

			BaseExchangeRate ukRate = creatUkRate(dom);
			newExchangeRateList.add(ukRate);

			baseExchangeRateService.flushRateAndBackup(newExchangeRateList);
		} catch (BaseException baseException) {
			baseExchangeRateService.updateAllException(BaseConsts.ONE + "");
			baseExchangeRateService.sendSystemAlarm(bankName, hsbcExchangeRateUrl);
			baseExchangeRateService.sendWebcatAlarm("SCFS抓取汇率失败", hsbcExchangeRateUrl, "汇丰银行汇率异常");
			LOGGER.error("抓取汇丰银行汇率异常：", baseException.getMsg());
		} catch (Exception e) {
			LOGGER.error("抓取汇丰银行汇率异常：", e);
			baseExchangeRateService.updateAllException(BaseConsts.ONE + "");
			baseExchangeRateService.sendSystemAlarm(bankName, hsbcExchangeRateUrl);
			baseExchangeRateService.sendWebcatAlarm("SCFS抓取汇率失败", hsbcExchangeRateUrl, "汇丰银行汇率异常");
		}
		LOGGER.info("==========结束抓取汇丰银行汇率=========耗时:" + (System.currentTimeMillis() - begin) / 1000);
	}

	private BaseExchangeRate creatUSAdRate(Document dom) throws Exception {
		BaseExchangeRate rate = new BaseExchangeRate();
		rate.setBank("1");// 1 表示 香港汇丰银行 对应港币
		rate.setCurrency("3");// 3是港币
		rate.setForeignCurrency("2");// 2为美元
		fillRateByXpath(usdPath, rate, "美元", dom);
		return rate;
	}

	private BaseExchangeRate creatUkRate(Document dom) throws Exception {
		BaseExchangeRate rate = new BaseExchangeRate();
		rate.setBank("1");// 1 表示 香港汇丰银行 对应港币
		rate.setCurrency("3");// 3是港币
		rate.setForeignCurrency("4");// 2为欧元
		fillRateByXpath(ukPath, rate, "歐羅", dom);
		return rate;
	}

	private BaseExchangeRate creatCnyRate(Document dom) throws Exception {
		BaseExchangeRate rate = new BaseExchangeRate();
		rate.setBank("1");// 1 表示 香港汇丰银行 对应港币
		rate.setCurrency("3");// 3是港币
		rate.setForeignCurrency("1");// 1为人民币
		fillRateByXpath(cnyPath, rate, "人民幣", dom);
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
				LOGGER.error("抓取汇丰银行汇率异常,【{}】货币名称【{}】不一致", currucyName, cName);
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "货币名称不一致，抓取汇率异常" + cName);
			}
			BigDecimal draftBuyingPrice = new BigDecimal(nodeList.item(1).getTextContent());
			BigDecimal draftSellingPrice = new BigDecimal(nodeList.item(2).getTextContent());
			BigDecimal cashBuyingPrice = new BigDecimal(nodeList.item(3).getTextContent());
			BigDecimal cashSellingPrice = new BigDecimal(nodeList.item(4).getTextContent());
			String publishAtStr = nodeList.item(5).getTextContent().replace("於 香港時間 ", "");
			String parseFomtStr = "yyyy年MM月dd日 HH:mm";
			Date publishAt = DateFormatUtils.parse(parseFomtStr, publishAtStr);
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
			rate.setDraftSellingPrice(draftSellingPrice);
			rate.setCashBuyingPrice(cashBuyingPrice);
			rate.setCashSellingPrice(cashSellingPrice);
			boolean isRateExcp = baseExchangeRateService.isRateExcep(rate);
			if (isRateExcp) {
				rate.setIsError(2);
				LOGGER.error("汇率浮动超过10%" + JSONObject.toJSONString(rate));
				baseExchangeRateService.sendSystemAlarm(bankName, hsbcExchangeRateUrl);
			}
		}
	}

}
