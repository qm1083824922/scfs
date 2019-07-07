package com.scfs.domain.common;

/**
 * Created by Administrator on 2016/12/10.
 */
public class MsgData {
    private String content;//消息内容
    private String title;//	消息标题
    private String to;//接收人
    private String channel;//渠道简码

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
