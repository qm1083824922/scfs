package com.scfs.service.support;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scfs.domain.common.MailSenderInfo;

/**
 * Created by Administrator on 2016/12/13.
 */
@Service
public class MsgSysAlarmService {
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgSysAlarmService.class);
	@Value("${msg.ip}")
	private String msgIp;

	@Autowired
	private MailService mailService;

	@PostConstruct
	public void init() {

	}

	/**
	 * 发送系统邮件报警
	 *
	 * @param emails  多个邮件需要以逗号隔开
	 * @param ccAddrs 多个邮件需要以逗号隔开
	 * @param title 标题
	 * @param content 内容
	 * @return
	 * @throws Exception
	 */
	public boolean sendSystemEmail(String emails, String ccAddrs, String title, String content) throws Exception {
		if (isAllowSend()) {
			if (emails != null) {
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setCcAddress(ccAddrs);
				mailInfo.setToAddress(emails);
				mailInfo.setSubject(title);
				mailInfo.setContent(content);
				return mailService.sendMail(mailInfo);
			} else {
				return false;
			}
		} else {
			LOGGER.error("IP限制或者IP没配置，不能发送邮件");
			return true;
		}

	}

	/**
	 * 本服务器是否允许发送消息
	 * 
	 * @return
	 */
	public boolean isAllowSend() {
		String localIp = ServiceSupport.localIp;
		if (localIp != null && localIp.equalsIgnoreCase(msgIp)) {
			return true;
		} else {
			return false;
		}
	}

}
