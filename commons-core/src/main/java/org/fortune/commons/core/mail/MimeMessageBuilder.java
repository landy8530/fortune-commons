package org.fortune.commons.core.mail;

import org.apache.commons.lang3.RandomUtils;
import org.fortune.commons.core.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author landyl
 * @create 4:06 PM 03/24/2020
 */
class MimeMessageBuilder {
    static final String HEADER_MESSAGE_ID = "Message-Id";

    private static Logger LOGGER = LoggerFactory.getLogger(MimeMessageBuilder.class);

    final Mail mail;

    MimeMessageBuilder(Mail mail) {
        this.mail = mail;
    }

    MimeMessage createMimeMessage(Session session) throws MessagingException {
        MimeMessage message = this.createMimeMessageEnvelope(session);
        Multipart multipart = new MimeMultipart("alternative");
        this.setBody(multipart);
        if (this.mail.getAttachments().size() > 0) {
            Iterator attachmentIterator = this.mail.getAttachments().iterator();

            while(attachmentIterator.hasNext()) {
                Attachment attachment = (Attachment)attachmentIterator.next();
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(attachment.getFile());
                attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                String fileName = attachment.getName();
                try {
                    fileName = MimeUtility.encodeText(fileName); // 解决中文附件乱码
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("Occurring an unexpected exception", e);
                }
                attachmentPart.setFileName(fileName);
                multipart.addBodyPart(attachmentPart);
            }
        }

        message.setContent(multipart);
        return message;
    }

    void removeServerInfoFromMessageIdHeader(MimeMessage message) throws MessagingException {
        message.setHeader(HEADER_MESSAGE_ID, "<" + RandomUtils.nextInt(0, 2147483647) + "." + System.currentTimeMillis() + ".mail@qq.com>");
    }

    private void setBody(Multipart message) throws MessagingException {
        String htmlBody = this.mail.getHtmlBody();
        String textBody = this.mail.getTextBody();
        if (StringUtils.hasText(htmlBody)) {
            String contentType = this.mail.getContentType();
            MimeBodyPart htmlBodyPart = new MimeBodyPart();
            try {
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(htmlBody, StringUtils.hasText(contentType) ? contentType : "text/html;charset=UTF-8");
                DataHandler dataHandler = new DataHandler(byteArrayDataSource);
                htmlBodyPart.setDataHandler(dataHandler);
            } catch (IOException e) {
                LOGGER.error("Occurring an unexpected exception", e);
            }
            //直接发送content，html内容会乱码
            //htmlBodyPart.setContent(htmlBody, StringUtils.hasText(contentType) ? contentType : "text/html;charset=UTF-8");
            message.addBodyPart(htmlBodyPart);
        }

        if (StringUtils.hasText(textBody)) {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBody = convertLineBreakForTxt(textBody);
            textBodyPart.setText(textBody);
            message.addBodyPart(textBodyPart, 0);
        }

    }

    private static String convertLineBreakForTxt(String bodyContent) {
        if (StringUtils.hasText(bodyContent)) {
            bodyContent = bodyContent.replaceAll("<br/>", "\r\n");
        }

        return bodyContent;
    }

    MimeMessage createMimeMessageEnvelope(Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session) {
            protected void updateMessageID() throws MessagingException {
                MimeMessageBuilder.this.removeServerInfoFromMessageIdHeader(this);
            }
        };
        Iterator iterator = this.mail.getToAddresses().iterator();

        String replyTo;
        while(iterator.hasNext()) {
            replyTo = (String)iterator.next();
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(replyTo));
        }

        iterator = this.mail.getCCAddresses().iterator();

        while(iterator.hasNext()) {
            replyTo = (String)iterator.next();
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(replyTo));
        }

        iterator = this.mail.getBCCAddresses().iterator();

        while(iterator.hasNext()) {
            replyTo = (String)iterator.next();
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(replyTo));
        }

        String from = this.mail.getFrom();
        if (StringUtils.hasText(from)) {
            message.setFrom(new InternetAddress(from));
        }

        if(!StringUtils.hasText(this.mail.getSubject())) throw new MailException("mail.subject is required");
        message.setSubject(this.mail.getSubject());
        replyTo = this.mail.getReplyTo();
        if (StringUtils.hasText(replyTo)) {
            message.setReplyTo(new Address[]{new InternetAddress(replyTo)});
        }

        message.setSentDate(new Date());
        Iterator headerIterator = this.mail.headers.entrySet().iterator();

        while(headerIterator.hasNext()) {
            Map.Entry<String, String> header = (Map.Entry)headerIterator.next();
            message.setHeader(header.getKey(), header.getValue());
        }

        return message;
    }
}
