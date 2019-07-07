package org.fortune.doc.client.result;

/**
 * @author: landy
 * @date: 2019/7/6 10:49
 * @description:
 */
public class DocResponseContent {

    private String encoding;
    private byte[] contentBytes;
    private int statusCode;
    private String contentType;
    private String contentTypeString;

    public DocResponseContent() {
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTypeString() {
        return this.contentTypeString;
    }

    public void setContentTypeString(String contenttypeString) {
        this.contentTypeString = contenttypeString;
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
            } catch (Exception var3) {
                return "";
            }
        }
    }

    public String getUTFContent() {
        try {
            return this.getContent("UTF-8");
        } catch (Exception var2) {
            return "";
        }
    }

    public byte[] getContentBytes() {
        return this.contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
