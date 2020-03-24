package org.fortune.commons.core.mail;

import org.fortune.commons.core.BaseJunit4Test;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author: landy
 * @date: 2020/3/24 22:17
 * @description:
 */
public class MailSenderTest extends BaseJunit4Test {

    @Resource
    MailSender mailSender;

    @Test
    public void send() {
        Mail mail = new Mail();
        mail.setSubject("Test File....");
        mail.setTextBody("Test File Body From Landy....");
        mail.addAttachment("TestFile.txt", new File("C:\\03_code\\idea_workspace\\fortune-commons\\commons-core\\src\\test\\resources\\doc\\Test.txt"));
        mailSender.send(mail);
    }
}
