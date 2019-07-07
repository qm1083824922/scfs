package com.scfs.web.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.domain.BaseResult;
import com.scfs.service.support.ServiceSupport;
import com.scfs.web.controller.wechat.WechatController;

/**
 * Created by Administrator on 2016/11/18.
 */
public class UrlFilter extends OncePerRequestFilter {
	private final static Logger LOGGER = LoggerFactory.getLogger(UrlFilter.class);
	/**
	 * 以api开头的不需要登录
	 */
	private String uri = "/api/";
	// 微信验证
	private String WECHAT_OAUTH_URL = null;
	// 验证回调
	private String WECHAT_OAUTH_REDIRECT_URL = null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String pathURI = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
		if (BaseUrlConsts.LOGIN.equalsIgnoreCase(pathURI) || BaseUrlConsts.LOGIN_INNER.equalsIgnoreCase(pathURI)
				|| BaseUrlConsts.LOGIN_INNER_REDIRECT.equals(pathURI)
				|| BaseUrlConsts.CAPTCHA.equalsIgnoreCase(pathURI)) {// 登陆请求，直接通过
			filterChain.doFilter(request, response);
			return;
		}
		if (pathURI.startsWith(uri)) {// 外部URL接口，不需要登录
			filterChain.doFilter(request, response);
			return;
		}
		if (pathURI.startsWith(BusUrlConsts.DETAIL_PO_PACK) || pathURI.startsWith(BusUrlConsts.QUERY_PO_PACK_LINE)) {
			filterChain.doFilter(request, response);
			return;
		}
		if (pathURI.startsWith("/wechat")) {// 微信请求，不需要登录
			filterChain.doFilter(request, response);
			return;
		}
		boolean isWxRequest = false;
		String source = request.getParameter("s");
		if (source != null && source.equalsIgnoreCase("1")) {
			isWxRequest = true;
		}
		// 先判断是否登陆，登陆是否失效，
		boolean isLogin = ServiceSupport.isLoginSuccess(request, isWxRequest);
		if (!isLogin) {// 跳转到登陆
			BaseResult result = new BaseResult();
			if (isWxRequest) {
				if (WECHAT_OAUTH_URL == null) {
					synchronized (this) {
						if (WECHAT_OAUTH_URL == null) {
							initProperties();
						}
					}
				}
				// 微信请求，跳转到微信
				String redUrl = request.getParameter("redirectURL");
				String url = WECHAT_OAUTH_URL
						+ URLEncoder.encode(WECHAT_OAUTH_REDIRECT_URL + "?redirectURL=" + redUrl, "utf-8")
						+ "&state=123#wechat_redirect";
				LOGGER.warn("微信没有登陆，需要登陆，请求URL：{}", url);
				result.setRedirectURL(url);
			}
			// PC请求，跳转到PC
			result.setLogin(true);
			response.getOutputStream().print(JSONObject.toJSONString(result));
			LOGGER.warn("没有登陆，需要登陆，请求URL：{}", pathURI);
			return;
		} else {// 登陆后 权限验证
			if (ServiceSupport.isAdminUser(null)) {// 如果是超级管理员，拥有所有权限
				filterChain.doFilter(request, response);
				return;
			}
			boolean isAuthor = hashAuth(request);
			if (!isAuthor) {
				LOGGER.warn("用户ID【{}】没有权限，请求URL：{}", ServiceSupport.getUser().getId(), pathURI);
				BaseResult result = new BaseResult();
				result.setPermission(false);
				response.getOutputStream().print(JSONObject.toJSONString(result));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private boolean hashAuth(HttpServletRequest request) {
		String pathURI = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
		/**
		if (ignorePathSuffix) {
			pathURI = removeSuffix(pathURI);
		}
		**/
		return ServiceSupport.isAllowPerm(pathURI);
	}

	/**
	 * 将url后缀remove.eg: /user/add.json --> /user/add
	 *
	 * @param requestURI
	 * @return
	 */
	public String removeSuffix(String requestURI) {
		int last = requestURI.lastIndexOf(".");
		last = last < 0 ? requestURI.length() : last;
		requestURI = requestURI.substring(0, last);
		return requestURI;
	}

	/**
	 * 初始化配置文件
	 */
	public void initProperties() {
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties("props/wechat.properties");
			WECHAT_OAUTH_URL = properties.getProperty("wechat.oauth.url");
			WECHAT_OAUTH_REDIRECT_URL = properties.getProperty("wechat.oauth.redirect.url");
			WechatController.WECHAT_MSG_URL = properties.getProperty("wechat.msg.url");
			System.out.println("============" + WECHAT_OAUTH_URL);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

}
