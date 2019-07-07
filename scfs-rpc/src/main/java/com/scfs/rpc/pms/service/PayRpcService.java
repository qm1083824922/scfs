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
import com.scfs.rpc.pms.dto.PayPmsHttpResDto;
import com.scfs.rpc.util.InvokeConfig;

/**
 * Created by Administrator on 2016年12月19日.
 */
@Service
public class PayRpcService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayRpcService.class);
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
	public PayPmsHttpResDto invokePay(String data) throws IOException {
		Map<String, String> parameter = new HashMap<String, String>();
		String key = MD5Util.getMD5String(invokeConfig.PMS_KEY + data);
		parameter.put("key", key);
		parameter.put("data", data);
		LOGGER.info("[pms]pms上传付款信息接口请求参数：key=" + key + "; data=" + data);
		String res = HttpInvoker.post(invokeConfig.SYNCPAYORDERINFO_URL, parameter);
		PayPmsHttpResDto payPmsHttpResDto = JSON.parseObject(res, PayPmsHttpResDto.class);
		LOGGER.info("[pms]pms上传付款信息接口返回结果：" + (null == payPmsHttpResDto ? "" : payPmsHttpResDto.toString()));
		return payPmsHttpResDto;
	}
}
