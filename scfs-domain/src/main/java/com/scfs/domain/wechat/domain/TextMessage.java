package com.scfs.domain.wechat.domain;

import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
@XmlRootElement(name = "xml")
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public TextMessage(Map<String, String> map) {
		super(map);
		this.setMsgType("text");
	}

	public String getContent() {
		return Content;
	}

	@XmlElement(name = "Content")
	public void setContent(String content) {
		Content = content;
	}
}