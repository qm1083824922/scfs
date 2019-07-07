package com.scfs.service.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
@Service
public class WechatMsgDispatcher {
	@Autowired
	private WechatMsgService wechatMsgService;

	public String processMessage(Map<String, String> map) {
		if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
			String content = wechatMsgService.reciveText(map.get(""));
			return MessageUtil.textMessageToXml(content, map);
		}
		if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // 图片消息
			System.out.println("==============这是图片消息！");
		}

		if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
			System.out.println("==============这是链接消息！");
		}
		return null;
	}
}
