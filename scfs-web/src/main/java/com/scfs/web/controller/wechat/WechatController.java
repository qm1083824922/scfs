package com.scfs.web.controller.wechat;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.domain.BaseResult;
import com.scfs.service.wechat.MessageUtil;
import com.scfs.service.wechat.MsgEnDeCryptService;
import com.scfs.service.wechat.WechatEventDispatcher;
import com.scfs.service.wechat.WechatEventService;
import com.scfs.service.wechat.WechatMsgDispatcher;

/**
 * Created by Administrator on 2016/12/5.
 */
@Controller
@RequestMapping(value = "/wechat")
public class WechatController {

	private final static Logger LOGGER = LoggerFactory.getLogger(WechatController.class);

	public static String WECHAT_MSG_URL;

	@Autowired
	private MsgEnDeCryptService msgEnDeCryptService;
	@Autowired
	private WechatEventService wechatEventService;
	@Autowired
	private WechatEventDispatcher wechatEventDispatcher;
	@Autowired
	private WechatMsgDispatcher wechatMsgDispatcher;

	@RequestMapping(value = "/security", method = RequestMethod.GET)
	public void doGet(HttpServletResponse response, String signature, String timestamp, String nonce, String echostr) {
		try {
			LOGGER.info("收到验证token信息：【{}】【{}】【{}】【{}】", signature, timestamp, nonce, echostr);
			if (msgEnDeCryptService.checkSignature(signature, timestamp, nonce)) {
				PrintWriter out = response.getWriter();
				out.print(echostr);
				out.close();
			}
		} catch (Exception e) {
			LOGGER.error("微信验证异常：", e);
		}
	}

	@RequestMapping(value = "/security", method = RequestMethod.POST)
	public void reciveMsg(String msg_signature, String timestamp, String nonce, @RequestBody String postdata,
			HttpServletResponse response) {
		try {
			response.setHeader("Content-Type", "text/html; charset=utf-8");
			Map<String, String> encrpMap = MessageUtil.parseXml(postdata);
			String deMsg = msgEnDeCryptService.decryptMsg(msg_signature, timestamp, nonce, encrpMap.get("Encrypt"));
			LOGGER.info(deMsg);
			Map<String, String> map = MessageUtil.parseXml(deMsg);
			String msgtype = map.get("MsgType");
			String responseMessage = null;
			if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)) {
				responseMessage = wechatEventDispatcher.processEvent(map); // 进入事件处理
			} else {
				responseMessage = wechatMsgDispatcher.processMessage(map); // 进入消息处理
			}
			PrintWriter out = response.getWriter();
			out.print(responseMessage);
		} catch (Exception e) {
			LOGGER.error("微信接收消息异常：", e);
		}

	}

	@RequestMapping(value = "/auth/code", method = RequestMethod.GET)
	public String auth(String code, HttpServletRequest request, HttpServletResponse response) {
		// 根据CODE获取openId,授权后重定向到列表页
		try {
			String openId = wechatEventService.getBaseUserByCode(code, response);
			if (openId != null) {
				return "redirect:" + WECHAT_MSG_URL + "?openId=" + openId;// 没有绑定后台用户，跳转到提示页面
			} else {
				String url = request.getParameter("redirectURL");
				String deUrl = new String(Base64.decodeBase64(url), "utf-8");
				LOGGER.info("解码URL:" + deUrl);
				return "redirect:" + deUrl;
			}
		} catch (Exception e) {
			LOGGER.error("获取code异常：", e);
		}
		return null;
	}

	@RequestMapping(value = "/realName", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult bindName(String openId, String realName) {
		BaseResult result = new BaseResult();
		try {
			wechatEventService.bindName(openId, realName);
		} catch (Exception e) {
			LOGGER.error("绑定真实姓名错误：【{}】【{}】", openId, realName, e);
			result.setMsg("系统错误，请稍后重试");
		}
		return result;
	}

}
