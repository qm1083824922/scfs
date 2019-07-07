package com.scfs.rpc.cms.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.scfs.common.utils.MD5Util;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.rpc.cms.dto.PayCmsHttpResDto;
import com.scfs.rpc.util.InvokeConfig;

@Service
public class CmsPayRpcService {
	private final static Logger LOGGER = LoggerFactory.getLogger(CmsPayRpcService.class);
	@Autowired
	public InvokeConfig invokeConfig;

	/**
	 * 调用cms上传付款信息接口
	 * 
	 * @param data
	 *            传输数据
	 * @return
	 * @throws IOException
	 */
	public PayCmsHttpResDto invokePay(String data) throws IOException {
		Map<String, String> parameter = new HashMap<String, String>();
		String key = MD5Util.getMD5String(invokeConfig.CMS_KEY + data);
		parameter.put("key", key);
		parameter.put("data", data);
		parameter.put("sn", invokeConfig.CMS_SN);
		parameter.put("token", invokeConfig.CMS_TOKEN);
		LOGGER.info("[cms]cms上传付款信息接口请求参数：key=" + key + "; data=" + data + "; sn=" + invokeConfig.CMS_SN + "; token="
				+ invokeConfig.CMS_TOKEN);
		String res = HttpInvoker.post(invokeConfig.SYNCCMSPAYORDER_URL, parameter);
		LOGGER.info("[cms]cms上传付款信息接口返回结果：" + res);
		PayCmsHttpResDto payCmsHttpResDto = null;
		if (StringUtils.isNotBlank(res)) {
			payCmsHttpResDto = JSON.parseObject(res, PayCmsHttpResDto.class);
		}
		return payCmsHttpResDto;
	}
}
