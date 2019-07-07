package com.scfs.rpc.wechat;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.domain.wechat.entity.WechatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/6.
 */
@Service
public class WechatRpcService {
	private final static Logger LOGGER = LoggerFactory.getLogger(WechatRpcService.class);

	private static String API_WEIXIN_URL = "https://api.weixin.qq.com/";

	private String lang = "zh_CN";

	public WechatUser queryWechatUserByOpenId(String openId, String accessToken) {
		try {
			String userInfoUrl = API_WEIXIN_URL + "cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId
					+ "&lang=" + lang;
			String result = HttpInvoker.get(userInfoUrl);
			LOGGER.info("获取微信用户信息：{}", result);
			if (result != null && !result.contains("errcode")) {
				WechatUser wechatUser = JSONObject.parseObject(result, WechatUser.class);
				return wechatUser;
			}
		} catch (IOException e) {

		}
		return null;
	}

	/**
	 * 获取access_token
	 *
	 * @param appid
	 * @param secret
	 * @return
	 */
	public String getWechatAccToken(String appid, String secret) {
		String accTokenUrl = API_WEIXIN_URL + "cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret="
				+ secret;
		try {
			String result = HttpInvoker.get(accTokenUrl);
			JSONObject jsonObject = JSONObject.parseObject(result);
			return jsonObject.getString("access_token");
		} catch (Exception e) {

		}
		return null;
	}

}
