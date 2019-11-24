package org.fortune.commons.core.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/19 23:05
 * @description:
 */
public class HttpHeaders {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHeaders.class);

    List<HttpHeader> headers;

    static HttpHeaders createResponseHeaders(HttpResponse response) {
        Header[] rawHeaders = response.getAllHeaders();
        List<HttpHeader> httpHeaders = new ArrayList(rawHeaders.length);
        Header[] headers = rawHeaders;
        int length = rawHeaders.length;

        for(int i = 0; i < length; ++i) {
            Header header = headers[i];
            httpHeaders.add(new HttpHeader(header.getName(), header.getValue()));
        }

        HttpHeaders headersCopy = new HttpHeaders();
        headersCopy.headers = Collections.unmodifiableList(httpHeaders);
        return headersCopy;
    }

    public void add(String name, String value) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        this.headers.add(new HttpHeader(name, value));
    }

    public List<HttpHeader> getValues() {
        return this.headers;
    }

    public String getFirstHeaderValue(String name) {
        if (this.headers != null) {
            Iterator iterator = this.headers.iterator();
            while(iterator.hasNext()) {
                HttpHeader header = (HttpHeader)iterator.next();
                if (header.getName().equals(name)) {
                    return header.getValue();
                }
            }
        }
        return null;
    }

    void addHeadersToRequest(HttpRequestBase request) {
        if (this.headers != null) {
            Iterator iterator = this.headers.iterator();
            while(iterator.hasNext()) {
                HttpHeader header = (HttpHeader)iterator.next();
                request.addHeader(header.getName(), header.getValue());
            }
        }
    }

    void log() {
        if (this.headers != null) {
            Iterator iterator = this.headers.iterator();
            while(iterator.hasNext()) {
                HttpHeader header = (HttpHeader)iterator.next();
                LOGGER.debug("[header] " + header.getName() + "=" + header.getValue());
            }
        }
    }
}
