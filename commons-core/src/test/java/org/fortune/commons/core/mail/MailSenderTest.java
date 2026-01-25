package org.fortune.commons.core.mail;

import org.fortune.commons.core.Junit4TestBase;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author: landy
 * @date: 2020/3/24 22:17
 * @description:
 */
public class MailSenderTest extends Junit4TestBase {

    @Resource
    MailSender mailSender;

    @Test
    public void send() {
        Mail mail = new Mail();
        mail.setSubject("Test File....");
        mail.setTextBody("Test File Body From Landy....");
        mail.setHtmlBody("<span>欣汇峰预估库存汇总表</span>");
        mail.addAttachment(
            "TestFile.txt",
            new File(getClass().getClassLoader().getResource("doc/Test.txt").getFile())
        );
        mailSender.send(mail);
    }
}
