package org.fortune.commons.core.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author: landy
 * @date: 2019/11/20 22:30
 * @description:
 */
public class XMLPost extends HttpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLPost.class);
    String xml;
    boolean chunked;

    @Override
    HttpRequestBase createRequest(String url) throws IOException {
        HttpPost post = new HttpPost(url);
        AbstractHttpEntity entity = new StringEntity(this.xml, "UTF-8");
        entity.setContentType("text/xml; charset=UTF-8");
        entity.setChunked(this.chunked);
        post.setEntity(entity);
        return post;
    }

    @Override
    protected void logRequestParams() {
        LOGGER.debug("[param] xml=" + this.xml);
    }

    public void setXMLContent(String xml) {
        this.xml = xml;
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }
}
