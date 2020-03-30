package org.fortune.commons.core.mail;

import org.fortune.commons.core.setting.Settings;
import org.fortune.commons.core.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author landyl
 * @create 3:57 PM 03/24/2020
 */
@Component
public class MailSender {
    static final String PROPERTY_KEY_HOST = "mailSender.host";
    static final String PROPERTY_KEY_PORT = "mailSender.port";
    static final String PROPERTY_KEY_USER = "mailSender.user";
    static final String PROPERTY_KEY_PASSWORD = "mailSender.password";
    static final String PROPERTY_KEY_DEFAULT_TO = "mailSender.defaultTo";
    static final String PROPERTY_KEY_DEFAULT_CC = "mailSender.defaultCC";
    static final String PROPERTY_KEY_DEFAULT_FROM = "mailSender.defaultFrom";

    private static Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    @Autowired
    private Settings settings;

    private String host;
    private int port;
    private String user;
    private String password;
    private String[] defaultToAddress;
    private String[] defaultCCAddress;
    private String defaultFromAddress;


    public MailSender() {

    }

    @PostConstruct
    public void postConstructor() {
        this.host = this.getHost();
        this.port = this.getPort() == null ? -1 : this.getPort();
        this.user = this.getUser();
        this.password = this.getPassword();
        this.defaultCCAddress = this.getDefaultCCAddress();
        this.defaultToAddress = this.getDefaultToAddress();
        this.defaultFromAddress = this.getDefaultFromAddress();
    }

    public void send(Mail mail) {
        this.send(Arrays.asList(mail));
    }

    public void send(List<Mail> mails) {
        Transport transport = null;

        try {
            Session session = Session.getInstance(new Properties());
            transport = session.getTransport("smtp");
            LOGGER.info("connecting to smtp server, host={}",this.host);
            transport.connect(this.host, this.port, this.user, this.password);
            StopWatch watch = new StopWatch();
            Iterator mailIterator = mails.iterator();

            while(mailIterator.hasNext()) {
                Mail mail = (Mail)mailIterator.next();
                MimeMessage message = (new MimeMessageBuilder(mail)).createMimeMessage(session);
                this.setDefaultToAddressesIfNotPresents(message);
                this.setDefaultFromAddressIfNotPresents(message);
                this.setDefaultCCAddressesIfNotPresents(message);
                transport.sendMessage(message, message.getAllRecipients());
            }
            LOGGER.info("sending email finished, elapsedTime={}",watch.elapsedTime());
        } catch (NoSuchProviderException ex) {
            LOGGER.error("sending email failed",ex);
            throw new MailException(ex);
        } catch (MessagingException ex) {
            LOGGER.error("sending email failed",ex);
            throw new MailException(ex);
        } finally {
            this.closeTransport(transport);
        }
    }

    private void closeTransport(Transport transport) {
        if (transport != null) {
            try {
                transport.close();
            } catch (MessagingException ex) {
                LOGGER.error("failed to close transport",ex);
            }
        }

    }

    private void setDefaultFromAddressIfNotPresents(MimeMessage message) throws MessagingException {
        if (message.getFrom() == null && StringUtils.hasText(this.defaultFromAddress)) {
            message.setFrom(new InternetAddress(this.defaultFromAddress));
            LOGGER.info("default sending email address list(from):{}",defaultFromAddress);
        }
    }

    private void setDefaultToAddressesIfNotPresents(MimeMessage message) throws MessagingException {
        if (message.getRecipients(Message.RecipientType.TO) == null && this.defaultToAddress != null) {
            String[] defaultToAddress = this.defaultToAddress;
            int length = defaultToAddress.length;

            for(int i = 0; i < length; ++i) {
                String toAddress = defaultToAddress[i];
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            }
            LOGGER.info("default sending email address list(TO):{}",defaultToAddress);
        }

    }

    private void setDefaultCCAddressesIfNotPresents(MimeMessage message) throws MessagingException {
        if (message.getRecipients(Message.RecipientType.CC) == null && this.defaultCCAddress != null) {
            int length =  this.defaultCCAddress.length;

            for(int i = 0; i < length; ++i) {
                String ccAddress = defaultCCAddress[i];
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
            }
            LOGGER.info("default sending email address list(CC):{}",defaultCCAddress);
        }

    }

    public String getHost() {
        return settings.get(PROPERTY_KEY_HOST);
    }

    public Integer getPort() {
        String port = settings.get(PROPERTY_KEY_PORT);
        return StringUtils.hasText(port) ? Integer.valueOf(port) : -1;
    }

    public String getUser() {
        return settings.get(PROPERTY_KEY_USER);
    }

    public String getPassword() {
        return settings.get(PROPERTY_KEY_PASSWORD);
    }

    public String[] getDefaultToAddress() {
        String defaultToAddressValue = settings.get(PROPERTY_KEY_DEFAULT_TO);
        if (StringUtils.hasText(defaultToAddressValue)) {
            return defaultToAddressValue.split(";");
        }
        return null;
    }

    public String[] getDefaultCCAddress() {
        String defaultCCAddressValue = settings.get(PROPERTY_KEY_DEFAULT_CC);
        if (StringUtils.hasText(defaultCCAddressValue)) {
            return defaultCCAddressValue.split(";");
        }
        return null;
    }

    public String getDefaultFromAddress() {
        String defaultFromAddress = settings.get(PROPERTY_KEY_DEFAULT_FROM);
        if (StringUtils.hasText(defaultFromAddress)) {
            return defaultFromAddress;
        }
        return null;
    }
}
