package org.fortune.commons.core.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author: landy
 * @date: 2019/11/20 22:05
 * @description:
 */
public class FormPost extends HttpRequest implements FormHttpRequest {
    final List<NameValuePair> parameters = new ArrayList();
    boolean chunked;

    @Override
    public void addParameter(String key, String value) {
        this.parameters.add(new BasicNameValuePair(key, value));
    }

    public void setParameter(String key, String value) {
        ListIterator iterator = this.parameters.listIterator();

        while(iterator.hasNext()) {
            NameValuePair param = (NameValuePair)iterator.next();
            if (param.getName().equals(key)) {
                iterator.remove();
            }
        }

        this.addParameter(key, value);
    }

    @Override
    HttpRequestBase createRequest(String url) throws IOException {
        HttpPost post = new HttpPost(url);
        AbstractHttpEntity entity = new UrlEncodedFormEntity(this.parameters, "UTF-8");
        entity.setChunked(this.chunked);
        post.setEntity(entity);
        return post;
    }

    @Override
    protected void logRequestParams() {
        logRequestParams(this.parameters);
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }
}
