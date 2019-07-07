package com.scfs.service.wechat;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/12/19.
 */
@Service
public class WechatMsgService {

	public String reciveText(String content) {
		switch (content) {
		case "采购单":
		case "销售单":
		default:
			return "关键词不存在";
		}
	}
}
