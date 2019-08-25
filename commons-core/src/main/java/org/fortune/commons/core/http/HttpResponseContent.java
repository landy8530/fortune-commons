package org.fortune.commons.core.http;

/**
 * @author landyl
 * @create 10:29 08/23/2019
 */
public class HttpResponseContent {
    private String encoding;
    private byte[] contentBytes;
    private int statusCode;
    private String contentType;
    private String contentTypeString;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTypeString() {
        return contentTypeString;
    }

    public void setContentTypeString(String contentTypeString) {
        this.contentTypeString = contentTypeString;
    }

    public String getContent() {
        return this.getContent(this.encoding);
    }

    public String getContent(String encoding) {
        if (encoding == null) {
            return new String(this.contentBytes);
        } else {
            try {
                return new String(this.contentBytes, encoding);
            } catch (Exception ex) {
                return "";
            }
        }
    }
}
