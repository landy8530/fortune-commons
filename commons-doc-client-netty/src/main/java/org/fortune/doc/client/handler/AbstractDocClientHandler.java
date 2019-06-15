package org.fortune.doc.client.handler;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author: landy
 * @date: 2019/5/30 23:25
 * @description:
 */
public abstract class AbstractDocClientHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDocClientHandler.class);
    private String host;
    private URI uri;
    private String userName;
    private String pwd;
    private HttpRequest request;

    public AbstractDocClientHandler(String host, URI uri, String userName,
                                 String pwd) {
        this.host = host;
        this.uri = uri;
        this.userName = userName;
        this.pwd = pwd;
        this.request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.POST, uri.toASCIIString());
        setHeaderDatas();
    }

    private void setHeaderDatas() {
        this.request.setHeader("Host", this.host);
        this.request.setHeader("Connection", "close");
        this.request.setHeader("Accept-Encoding", "gzip,deflate");
        this.request.setHeader("Accept-Charset",
                "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        this.request.setHeader("Accept-Language", "fr");
        this.request.setHeader("Referer", this.uri.toString());
        this.request.setHeader("User-Agent", "Netty Simple Http Client side");
        this.request
                .setHeader("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    }

    public abstract HttpPostRequestEncoder wrapRequestData(
            HttpDataFactory paramHttpDataFactory);

    public HttpRequest getRequest() {
        return this.request;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
