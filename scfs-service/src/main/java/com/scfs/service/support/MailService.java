package com.scfs.service.support;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.scfs.common.consts.BaseConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scfs.domain.common.MailAuthenticator;
import com.scfs.domain.common.MailSenderInfo;

/**
 * Created by Administrator on 2016/11/29.
 */
@Service
public class MailService {
	// 登陆邮件发送服务器的用户名和密码
	@Value("${mail.username}")
	private String userName;
	@Value("${mail.pwd}")
	private String password;
	@Value("${mail.smtp.host}")
	private String mailServerHost;
	@Value("${mail.smtp.port}")
	private String mailServerPort = "25";
	@Value("${mail.open.ssl}")
	private String mailOpenSsl;

	private Properties properties;

	@PostConstruct
	public void initProperties() {
		properties = new Properties();
		properties.put("mail.smtp.host", this.mailServerHost);
		properties.put("mail.smtp.port", this.mailServerPort);
		properties.put("mail.smtp.auth", "true");
		if ("true".equals(this.mailOpenSsl)) {
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.socketFactory.port", this.mailServerPort);
		}
	}

	/**
	 * 单个以HTML格式发送邮件
	 *
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	@SuppressWarnings("static-access")
	public boolean sendMail(MailSenderInfo mailInfo) throws Exception {
		MailAuthenticator authenticator = new MailAuthenticator(this.userName, this.password);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress(userName);
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);

		// 设置多个收件人地址
		InternetAddress[] internetAddressTo = new InternetAddress().parse(mailInfo.getToAddress());
		mailMessage.setRecipients(Message.RecipientType.TO, internetAddressTo);
		// 设置多个抄送地址
		if (StringUtils.isNotBlank(mailInfo.getCcAddress())) {
			InternetAddress[] internetAddressCC = new InternetAddress().parse(mailInfo.getCcAddress());
			mailMessage.setRecipients(Message.RecipientType.CC, internetAddressCC);
		}
		// 设置邮件消息的主题
		mailMessage.setSubject(mailInfo.getSubject());
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();
		// 设置HTML内容
		html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
		// mbp_text.setContent(data.get(Constants.EMAIL_TEXT),
		// "text/html;charset=gbk");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容
		mailMessage.setContent(mainPart);

		// MimeBodyPart mimeBodyPart;
		// 多个附件
		// for (int i = 0; i < email.getEmail_file().size(); i++) {
		// Map<String, String> map = email.getEmail_file().get(i);
		// String fileSource="";
		// String fileName="";
		// for (String key : map.keySet()) {
		// fileSource = key;
		// fileName = map.get(key);
		// }
		// mimeBodyPart = new MimeBodyPart();
		// DataSource source = new FileDataSource(fileSource);
		// mimeBodyPart.setDataHandler(new DataHandler(source));
		// mimeBodyPart.setFileName(MimeUtility.encodeText(fileName));
		// multipart.addBodyPart(mimeBodyPart);// Put parts in
		// }
		// 发送邮件
		Transport.send(mailMessage);
		return true;
	}

}
