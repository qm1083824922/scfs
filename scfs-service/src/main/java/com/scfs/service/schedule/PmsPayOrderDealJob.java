package com.scfs.service.schedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.service.interf.PmsPayOrderTitleService;

/**
 * <pre>
 * 
 *  File: PmsPayOrderDealJob.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年1月7日				Administrator
 *
 * </pre>
 */
public class PmsPayOrderDealJob {
	@Autowired
	private PmsPayOrderTitleDao pmsPayOrderTitleDao;

	@Autowired
	private PmsPayOrderTitleService pmsPayOrderTitleService;

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayOrderDealJob.class);

	public void execute() {
		try {
			PmsPoTitleSearchReqDto req = new PmsPoTitleSearchReqDto();
			// req.setSearchType(BaseConsts.ONE);
			req.setState(BaseConsts.ONE); // 抓取待处理
			List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByCon(req);
			if (!CollectionUtils.isEmpty(pmsPayOrderTitles)) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[pms请款单自动处理]开始时间：" + new Date());
				for (PmsPayOrderTitle pmsPayOrderTitle : pmsPayOrderTitles) {
					Integer id = pmsPayOrderTitle.getId();
					try {
						pmsPayOrderTitleService.dealPmsPayOrder(id);
					} catch (BaseException e) {
						pmsPayOrderTitleService.dealFail(id, e.getMsg());
					} catch (Exception e) {
						LOGGER.error("处理pms请款单失败[{}]: {}", JSONObject.toJSON(pmsPayOrderTitle), e);
						pmsPayOrderTitleService.dealFail(id, "系统异常，请稍后再试");
					}
				}
				long endTime = System.currentTimeMillis();
				LOGGER.info("[pms请款单自动处理]结束时间：" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
			}
		} catch (Exception e) {
			LOGGER.error("pms请款单异常：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		}
	}
}
