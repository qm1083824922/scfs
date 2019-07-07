package com.scfs.web.mail;

import com.scfs.domain.common.MailSenderInfo;
import com.scfs.service.support.MailService;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MailServiceTest extends BaseJUnitTest {
    @Autowired
    private MailService mailService;

    @Test
    public void testSendMail(){
        try {
            MailSenderInfo mailSenderInfo = new MailSenderInfo();
            mailSenderInfo.setContent("你好，恭喜你，中奖了.....");
            mailSenderInfo.setSubject("测试邮件");
            mailSenderInfo.setToAddress("test@163.com");
            mailSenderInfo.setCcAddress("test@163.com");
            mailService.sendMail(mailSenderInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
