package org.fortune.commons.core.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.fortune.commons.core.util.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author: landy
 * @date: 2019/11/20 22:34
 * @description:
 */
public class ByteArrayPost extends HttpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayPost.class);

    byte[] bytes;
    boolean chunked;

    @Override
    HttpRequestBase createRequest(String url) throws IOException {
        HttpPost post = new HttpPost(url);
        ByteArrayEntity entity = new ByteArrayEntity(this.bytes);
        entity.setContentType("binary/octet-stream");
        entity.setChunked(this.chunked);
        post.setEntity(entity);
        return post;
    }

    @Override
    protected void logRequestParams() {
        LOGGER.debug("[param] bytes=" + DigestUtils.base64(this.bytes));
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }
}
