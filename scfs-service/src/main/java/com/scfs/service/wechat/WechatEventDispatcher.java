package com.scfs.service.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
@Service
public class WechatEventDispatcher {

	@Autowired
	private WechatEventService wechatEventService;

	public static final String NAV_MENU = "欢迎关注，回复:\n1加用户名绑定用户,比如：1zhangsan\n2.查询订单信息\n";

	public String processEvent(Map<String, String> map) {
		if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { // 关注事件
			wechatEventService.subscribe(map);
			return MessageUtil.textMessageToXml(NAV_MENU, map);
		}
		if (map.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { // 取消关注事件
			System.out.println("==============这是取消关注事件！");
		}

		if (map.get("Event").equals(MessageUtil.EVENT_TYPE_CLICK)) { // 自定义菜单点击事件
			System.out.println("==============这是自定义菜单点击事件！");
		}

		return null;
	}
}
