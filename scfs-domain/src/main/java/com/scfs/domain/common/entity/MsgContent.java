package com.scfs.domain.common.entity;

import com.scfs.domain.base.entity.BaseEntity;

import java.util.Date;

public class MsgContent extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String toAccounts;

    private String ccAccounts;

    private String msgTitle;

    private String msgContent;

    private String remark;

    private Integer msgType;

    private Integer isSend;

    private Integer sendCount;

    private Date sendAt;


    public String getToAccounts() {
        return toAccounts;
    }

    public void setToAccounts(String toAccounts) {
        this.toAccounts = toAccounts;
    }

    public String getCcAccounts() {
        return ccAccounts;
    }

    public void setCcAccounts(String ccAccounts) {
        this.ccAccounts = ccAccounts;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}