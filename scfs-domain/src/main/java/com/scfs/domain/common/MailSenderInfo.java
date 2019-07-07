package com.scfs.domain.common;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MailSenderInfo implements Serializable{

    private static final long serialVersionUID = 1209632788866312001L;

    /** 邮件接收者的地址*/
    private String toAddress;
    /**抄送地址*/
    private String ccAddress;
    /**邮件主题*/
    private String subject;
    /** 邮件的文本内容*/
    private String content;

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
