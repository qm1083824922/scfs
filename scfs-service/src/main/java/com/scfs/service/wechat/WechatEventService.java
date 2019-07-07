package com.scfs.service.wechat;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.PwdUtils;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.dao.wechat.WechatUserDao;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.rpc.wechat.WechatRpcService;
import com.scfs.service.support.CacheService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/12/6.
 */
@Service
public class WechatEventService {

	private final static Logger LOGGER = LoggerFactory.getLogger(WechatEventService.class);

	@Value("${wechat.AppId}")
	private String appId;
	@Value("${wechat.AppSecret}")
	private String appSecret;
	@Autowired
	private WechatUserDao wechatUserDao;
	@Autowired
	private WechatRpcService wechatRpcService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 关注事件
	 *
	 * @param msgMap
	 */
	public void subscribe(Map<String, String> msgMap) {
		try {
			String openId = msgMap.get("FromUserName");
			String accessToken = getAccessToken();// 获取得到accessToken 调用微信
			WechatUser wechatUser = wechatRpcService.queryWechatUserByOpenId(openId, accessToken);
			if (wechatUser != null) {
				if (StringUtils.isNotBlank(wechatUser.getNickname())) {
					wechatUser.setNickname(Base64.encodeBase64String(wechatUser.getNickname().getBytes("utf-8")));
				}
				wechatUser.setSubscribe_time(new Date());
				wechatUserDao.insert(wechatUser);
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	/**
	 * 获取接口accessToken
	 *
	 * @return
	 */
	public String getAccessToken() {
		String accessToken = stringRedisTemplate.opsForValue().get("accessToken");
		if (accessToken != null) {
			return accessToken;
		} else {
			accessToken = wechatRpcService.getWechatAccToken(appId, appSecret);
			if (accessToken != null) {
				stringRedisTemplate.opsForValue().set("accessToken", accessToken, 5000, TimeUnit.SECONDS);
			}
			return accessToken;
		}
	}

	/**
	 * web授权根据code获取用户
	 *
	 * @param code
	 */
	@IgnoreTransactionalMark
	public String getBaseUserByCode(String code, HttpServletResponse response) throws Exception {
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret
				+ "&code=" + code + "&grant_type=authorization_code";
		String jsonStr = HttpInvoker.get(URL);
		LOGGER.info("微信登陆返回结果：【{}】", jsonStr);
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		String openid = jsonObj.getString("openid");
		WechatUser wechatUser = wechatUserDao.queryWechatUserByOpenId(openid);
		// 是否绑定用户
		if (wechatUser == null) {
			wechatUser = wechatRpcService.queryWechatUserByOpenId(openid, getAccessToken());
			if (wechatUser != null) {
				wechatUser.setSubscribe_time(new Date());
				if (StringUtils.isNotBlank(wechatUser.getNickname())) {
					wechatUser.setNickname(Base64.encodeBase64String(wechatUser.getNickname().getBytes("utf-8")));
				}
				wechatUserDao.insert(wechatUser);
			}
			return openid;
		} else {
			if (wechatUser.getUserId() != null) {
				BaseUser baseUser = cacheService.getUserByid(wechatUser.getUserId());
				String userName = baseUser.getUserName();
				String token = PwdUtils.encryptPassword(BaseConsts.WEI_XIN + userName + System.currentTimeMillis());
				stringRedisTemplate.opsForValue().set(BaseConsts.WEI_XIN + userName, token);
				Cookie nameCookie = createCookie(BaseConsts.LOGOIN_USER_NAME,
						URLEncoder.encode(Base64.encodeBase64String(userName.getBytes("utf-8")), "utf-8"));
				response.addCookie(nameCookie);
				String chineseName = baseUser.getChineseName();
				String base = Base64.encodeBase64String(chineseName.getBytes("utf-8"));
				Cookie chineseNameCookie = createCookie(BaseConsts.CHINESE_NAME, URLEncoder.encode(base, "utf-8"));
				response.addCookie(chineseNameCookie);
				Cookie tokenCookie = createCookie(BaseConsts.LOGOIN_USER_TOKEN, token);
				response.addCookie(tokenCookie);
				return null;
			}
		}
		return openid;
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		return cookie;
	}

	public void bindName(String openId, String realName) {
		WechatUser wechatUser = new WechatUser();
		wechatUser.setOpenid(openId);
		wechatUser.setRealName(realName);
		wechatUserDao.update(wechatUser);
	}
}
