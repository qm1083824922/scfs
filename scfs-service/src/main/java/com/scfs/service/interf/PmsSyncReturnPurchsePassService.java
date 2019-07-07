package com.scfs.service.interf;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.audit.AuditDao;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.rpc.pms.entity.PmsSyncReturnPurchseReqDto;
import com.scfs.service.common.InvokeLogService;

/**
 * pms 退货订单回传接口
 * 
 * @author  2017:06:22
 *
 */
@Service
public class PmsSyncReturnPurchsePassService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncReturnPurchsePassService.class);
	@Autowired
	private InvokeLogService invokeLogService;

	@Autowired
	private AuditDao auditDao;

	/**
	 * 审核不通过时候进行的调用
	 * 
	 * @param audit
	 * @param purchaseOrderTitle
	 * @return
	 */
	public boolean unPassPurchase(Audit audit, PurchaseOrderTitle purchaseOrderTitle) {
		return this.sendPurchse(audit, purchaseOrderTitle, false);
	}

	/**
	 * 审核通过时候的调用
	 * 
	 * @param audit
	 * @param purchaseOrderTitle
	 * @return
	 */
	public boolean passPurchase(Audit audit, PurchaseOrderTitle purchaseOrderTitle) {
		return this.sendPurchse(audit, purchaseOrderTitle, true);
	}

	/**
	 * PMS 退货订单确认回传接口
	 * 
	 * @param purchseReqDto
	 * @param flag
	 * @return
	 */
	public boolean sendPurchse(Audit audit, PurchaseOrderTitle purchaseOrderTitle, boolean flag) {
		boolean isSuccess = false;
		// 封装当前请求PMS退货订单确认接口
		PmsSyncReturnPurchseReqDto purchseReqDto = this.createPurchse(audit, purchaseOrderTitle, flag);
		// 数据类型转换
		String data = JSON.toJSONString(purchseReqDto, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.WriteMapNullValue);
		try {
			save(purchaseOrderTitle, data);
		} catch (Exception e) {
			LOGGER.error("新增接口调用日志失败：", e);
			return isSuccess;
		}
		isSuccess = true;
		// if (null != invokeLog) {
		// try {
		// PmsSyncReturnPurchaseResDto payPmsHttpResDto =
		// purchaseRpcService.invokePurchase(data);
		// invokeLog.setReturnMsg(payPmsHttpResDto.getMsg());
		// if (payPmsHttpResDto.getFlag().equals(BaseConsts.FLAG_NO)) {
		// //返回结果N，调用失败
		// invokeLogService.invokeError(invokeLog);
		// } else {
		// invokeLogService.invokeSuccess(invokeLog);
		// }
		// isSuccess = true;
		// } catch (Exception e) {
		// LOGGER.error("调用pms上传付款信息接口失败：", e);
		// invokeLogService.invokeException(invokeLog, e);
		// }
		// }
		return isSuccess;
	}

	/**
	 * 封装当前调用PMS退货确认接口的数据
	 * 
	 * @param audit
	 * @param purchaseOrderTitle
	 * @param flag
	 * @return
	 */
	private PmsSyncReturnPurchseReqDto createPurchse(Audit audit, PurchaseOrderTitle purchaseOrderTitle, boolean flag) {
		PmsSyncReturnPurchseReqDto purchseReqDto = new PmsSyncReturnPurchseReqDto();
		purchseReqDto.setRefund_order_sn(purchaseOrderTitle.getAppendNo());// 附属编号
		// 数据校验
		audit = auditDao.queryAuditById(audit.getId());
		if (null == audit) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "审核数据为空", audit);
		}
		if (flag == true) { // 该类型为审核通过状态
			purchseReqDto.setAudit_state(BaseConsts.ONE);// 通过状态
			purchseReqDto.setAudit_suggestion(audit.getSuggestion());// 审核意见
		} else {
			purchseReqDto.setAudit_state(BaseConsts.ZERO);// 不通过状态
			purchseReqDto.setAudit_suggestion(audit.getSuggestion());// 审核意见
		}
		return purchseReqDto;
	}

	/**
	 * 接口日志数据的记录
	 * 
	 * @param purchaseOrderTitle
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private InvokeLog save(PurchaseOrderTitle purchaseOrderTitle, String data) throws Exception {
		InvokeLog invokeLog = new InvokeLog();
		if (StringUtils.isNotBlank(data)) {
			invokeLog.setContent(data);
		}
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PURCHASE_ORDER_CONFIRM.getType());
		invokeLog.setInvokeMode(BaseConsts.TWO); // 2-异步
		invokeLog.setModuleType(BaseConsts.SIX); // 6 采购
		invokeLog.setBillType(BaseConsts.EIGHT); // 8 采购单
		invokeLog.setBillId(purchaseOrderTitle.getId());
		invokeLog.setBillNo(purchaseOrderTitle.getOrderNo());
		invokeLog.setBillDate(purchaseOrderTitle.getOrderTime());
		invokeLog.setProvider(BaseConsts.THREE); // 2-pms
		invokeLog.setConsumer(BaseConsts.ONE); // 1-scfs
		invokeLog.setIsSuccess(BaseConsts.ZERO); // 0-未调用
		invokeLog.setTryAgainFlag(BaseConsts.ONE); // 1-可重新调用
		invokeLogService.add(invokeLog);
		return invokeLog;
	}
}
