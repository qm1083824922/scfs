package com.scfs.service.schedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.api.pms.PmsStoreOutDao;
import com.scfs.domain.pay.entity.PmsStoreOut;
import com.scfs.service.api.pms.PmsSyncBillOutStoreService;
import com.scfs.service.common.CommonService;
import com.scfs.service.po.PurchaseOrderService;

@Service
public class PmsBillOutStoreJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsBillOutStoreJob.class);

	@Autowired
	private PmsStoreOutDao pmsStoreOutDao;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private PmsSyncBillOutStoreService billOutStoreService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private PmsSyncBillOutStoreService pmsSyncBillOutStoreService;

	// 执行当前PMS
	public void execute() {
		try {
			// 查询当前未处理的PMS同步出库数据
			List<PmsStoreOut> list = pmsStoreOutDao.queryPmsStoreOut();
			if (!CollectionUtils.isEmpty(list)) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[pms 出库单自动处理]开始时间：" + new Date());
				for (PmsStoreOut pmsStoreOut : list) {
					Integer id = pmsStoreOut.getId();
					try {
						purchaseOrderService.updatePurchaseOrderLineByCon(pmsStoreOutDao.queryEntityById(id));
						dealFail(id, "PMS出库单处理完成", BaseConsts.THREE);
					} catch (BaseException e) {
						dealFail(id, e.getMsg(), BaseConsts.TWO);
					} catch (Exception e) {
						LOGGER.error("处理pms出库单失败[{}]: {}", JSONObject.toJSON(pmsStoreOut), e);
						dealFail(id, "系统异常，请稍后再试", BaseConsts.TWO);
					}
				}
				long endTime = System.currentTimeMillis();
				LOGGER.info("[pms出库单自动处理]结束时间：" + new Date() + "，共执行" + (endTime - startTime) / 1000 + "秒");
			}
		} catch (BaseException e) {
			LOGGER.error("出库单同步定时任务定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("出库单同步定时任务定时任务失败：", e);
		}
	}

	/**
	 * 接口调用PMS出库接口修改状态
	 * 
	 * @param id
	 * @param errorMsg
	 */
	public void dealFail(Integer id, String errorMsg, Integer type) {
		PmsStoreOut pmsStoreOut = pmsStoreOutDao.queryEntityById(id);
		PmsStoreOut storeOut = new PmsStoreOut();
		storeOut.setId(pmsStoreOut.getId());
		storeOut.setDealFlag(type);
		storeOut.setDealMsg(commonService.getMsg(errorMsg));
		billOutStoreService.updateById(storeOut);
		if (type == BaseConsts.TWO) { // 2-处理失败
			pmsSyncBillOutStoreService.sendSystemAlarm(id, BaseConsts.ZERO, errorMsg);
		}
	}
}
