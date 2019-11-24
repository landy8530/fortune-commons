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
 * @date: 2019/11/20 22:26
 * @description:
 */
public class TextPost extends HttpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextPost.class);

    String body;
    String contentType = "text/plain";
    boolean chunked;

    @Override
    HttpRequestBase createRequest(String url) throws IOException {
        HttpPost post = new HttpPost(url);
        AbstractHttpEntity entity = new StringEntity(this.body, "UTF-8");
        entity.setContentType(this.contentType + "; charset=" + "UTF-8");
        entity.setChunked(this.chunked);
        post.setEntity(entity);
        return post;
    }

    @Override
    protected void logRequestParams() {
        LOGGER.debug("contentType=" + this.contentType);
        LOGGER.debug("[param] body=" + this.body);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }
}
