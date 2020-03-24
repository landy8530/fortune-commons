package org.fortune.commons.core.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author landyl
 * @create 3:53 PM 03/24/2020
 */
public class Mail {
    private static final String CONTENT_TYPE_HTML = "text/html";

    private String contentType;
    private final List<String> toAddresses = new ArrayList();
    private final List<String> ccAddresses = new ArrayList();
    private final List<String> bccAddresses = new ArrayList();
    private String replyTo;
    private String subject;
    private String from;
    private String htmlBody;
    private String textBody;
    private final List<Attachment> attachments = new ArrayList();
    final Map<String, String> headers = new HashMap();

    public void addAttachment(String name, File file) {
        Attachment attachment = new Attachment();
        attachment.setName(name);
        attachment.setFile(file);
        this.attachments.add(attachment);
    }

    public void setHeader(String header, String value) {
        this.headers.put(header, value);
    }

    public void addTo(String toAddress) {
        this.toAddresses.add(toAddress);
    }

    public void addCC(String ccAddress) {
        this.ccAddresses.add(ccAddress);
    }

    public void setBCC(String bccAddress) {
        this.bccAddresses.add(bccAddress);
    }

    public void clearToAddresses() {
        this.toAddresses.clear();
    }

    public List<String> getCCAddresses() {
        return this.ccAddresses;
    }

    public void clearCCAddresses() {
        this.ccAddresses.clear();
    }

    public List<String> getBCCAddresses() {
        return this.bccAddresses;
    }

    public void clearBCCAddresses() {
        this.bccAddresses.clear();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public List<String> getCcAddresses() {
        return ccAddresses;
    }

    public List<String> getBccAddresses() {
        return bccAddresses;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
        this.contentType = CONTENT_TYPE_HTML;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
