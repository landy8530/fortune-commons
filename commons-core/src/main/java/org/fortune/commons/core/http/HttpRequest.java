package org.fortune.commons.core.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/19 23:04
 * @description:
 */
public abstract class HttpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpHeaders headers = new HttpHeaders();

    //非线程方式
    private String url;

    public void addHeader(String name, String value) {
        this.headers.add(name, value);
    }

    HttpRequestBase createHttpRequest(String defaultURL) throws IOException {
        String url = this.getUrl(defaultURL);
        HttpRequestBase request = this.createRequest(url);
        this.headers.addHeadersToRequest(request);
        return request;
    }

    String getUrl(String defaultURL) {
         return getUrl() != null ? getUrl() : defaultURL;
    }

    public String getUrl() {
         return url;
    }

    public void setUrl(String url) {
         this.url = url;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    void logRequest() {
        LOGGER.debug("====== http request begin ======");
        this.headers.log();
        this.logRequestParams();
        LOGGER.debug("====== http request end ======");
    }

    protected void logRequestParams(List<NameValuePair> parameters) {
        NameValuePair parameter;
        String parameterValue;
        for(Iterator iterator = parameters.iterator(); iterator.hasNext();
            LOGGER.debug("[param] " + parameter.getName() + "=" + parameterValue)) {
            parameter = (NameValuePair)iterator.next();
            parameterValue = parameter.getValue();
            if (null != parameterValue && parameterValue.length() > 100) {
                parameterValue = parameterValue.substring(0, 100);
            }
        }
    }

    abstract HttpRequestBase createRequest(String url) throws IOException;

    protected abstract void logRequestParams();
}
