package com.scfs.service.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.common.entity.MsgContent;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.support.MsgSysAlarmService;
import com.scfs.service.wechat.WechatEventService;

/**
 * Created by Administrator on 2016/12/15.
 */
@Service
public class MsgContentSendJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(MsgContentSendJob.class);

	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private MsgSysAlarmService msgSysAlarmService;
	@Autowired
	private WechatEventService wechatEventService;

	@IgnoreTransactionalMark
	public void sendMsg() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[发送系统消息定时任务]开始时间：" + new Date());
		try {
			List<MsgContent> msgContentList = msgContentService.querySendMsgList();
			if (CollectionUtils.isNotEmpty(msgContentList)) {
				for (MsgContent msgContent : msgContentList) {
					if (StringUtils.isEmpty(msgContent.getToAccounts())) {
						continue;
					}
					switch (msgContent.getMsgType()) {
					case BaseConsts.ONE:
						sendRtx(msgContent);
						break;
					case BaseConsts.TWO:
						sendEmail(msgContent);
						break;
					case BaseConsts.FOUR:
						sendWechat(msgContent);
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("发送邮件或者rtx消息异常：", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("[发送系统消息定时任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}

	@SuppressWarnings({ "unchecked", "static-access" })
    public void sendWechat(MsgContent msgContent) {
		boolean isAllow = msgSysAlarmService.isAllowSend();
		if (isAllow) {
			String openIds = msgContent.getToAccounts();
			String[] openidArr = openIds.split(",");
			if (openidArr != null && openidArr.length > 0) {
				try {
					for (int i = 0; i < openidArr.length; i++) {
						String content = msgContent.getMsgContent();
						Map<String, String> map = (Map<String, String>) JSONObject.parse(content);
						map.put("touser", openidArr[i]);
						String json = msgContentService.createWechatTempMsg(map);
						String accessToken = wechatEventService.getAccessToken();
						String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
								+ accessToken;
						LOGGER.info(json);
						String result = HttpInvoker.post(url, json);
						JSONObject jsonObject = JSONObject.parseObject(result);
						if ("0".equalsIgnoreCase(jsonObject.getString("errcode"))) {
							msgContent.setIsSend(BaseConsts.TWO);
						} else {
							msgContent.setIsSend(BaseConsts.THREE);
						}
						msgContent.setRemark(result);
					}
				} catch (Exception e) {
					msgContent.setIsSend(BaseConsts.THREE);
					LOGGER.error("【{}】发送微信消息失败：", msgContent, e);
				}
			}
		}
		msgContent.setSendCount(msgContent.getSendCount() + 1);
		msgContentService.updateMsgById(msgContent);
	}

	private void sendEmail(MsgContent msgContent) {
		try {
			LOGGER.info("发送email:" + msgContent.getToAccounts());
			boolean isSuccess = msgSysAlarmService.sendSystemEmail(msgContent.getToAccounts(),
					msgContent.getCcAccounts(), msgContent.getMsgTitle(), msgContent.getMsgContent());
			if (isSuccess) {
				msgContent.setIsSend(BaseConsts.TWO);
			} else {
				msgContent.setIsSend(BaseConsts.THREE);
			}
		} catch (Exception e) {
			msgContent.setIsSend(BaseConsts.THREE);
			LOGGER.error("[{}]发送邮件系统异常:", JSONObject.toJSON(msgContent), e);
		}
		msgContent.setSendCount(msgContent.getSendCount() + 1);
		msgContentService.updateMsgById(msgContent);
	}

	private void sendRtx(MsgContent msgContent) {
	    //TODO 待实现，需对接rtx
	}
}
