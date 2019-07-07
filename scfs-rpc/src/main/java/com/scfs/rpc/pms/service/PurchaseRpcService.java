package com.scfs.rpc.pms.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.scfs.common.utils.MD5Util;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.rpc.pms.dto.PmsSyncReturnPurchaseResDto;
import com.scfs.rpc.util.InvokeConfig;

/**
 * 
 * @author  2017年06月23日
 *
 */
@Service
public class PurchaseRpcService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PurchaseRpcService.class);

	@Autowired
	public InvokeConfig invokeConfig;

	/**
	 * 调用pms上传付款信息接口
	 * 
	 * @param data
	 *            传输数据
	 * @return
	 * @throws IOException
	 */
	public PmsSyncReturnPurchaseResDto invokePurchase(String data) throws IOException {
		Map<String, String> parameter = new HashMap<String, String>();
		String key = MD5Util.getMD5String(invokeConfig.PMS_KEY + data);
		parameter.put("key", key);
		parameter.put("data", data);
		LOGGER.info("[pms]pms上传退货订单确认回传接口请求参数：key=" + key + "; data=" + data);
		String res = HttpInvoker.post(invokeConfig.SYNCPURCHASEPASS_URL, parameter);
		PmsSyncReturnPurchaseResDto purchaseResDto = JSON.parseObject(res, PmsSyncReturnPurchaseResDto.class);
		LOGGER.info("[pms]pms上传退货订单确认回传接口返回结果：" + (null == purchaseResDto ? "" : purchaseResDto.toString()));
		return purchaseResDto;
	}
}
