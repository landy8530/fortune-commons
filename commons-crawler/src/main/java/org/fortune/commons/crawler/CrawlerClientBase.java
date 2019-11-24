package org.fortune.commons.crawler;

import org.fortune.commons.core.http.HttpBinaryResponse;
import org.fortune.commons.core.http.HttpClient;
import org.fortune.commons.core.http.HttpRequest;
import org.fortune.commons.core.http.HttpResponse;

import java.net.URI;

/**
 * @author: landy
 * @date: 2019/11/24 12:48
 * @description:
 */
public abstract class CrawlerClientBase {

    static final int DEFAULT_RETRY_COUNT = 2;

    HttpClient httpClient;

    BrowserCompatibleRedirectHandler redirectHandler;

    HttpRequest httpMethod;

    HttpResponse response;

    HttpBinaryResponse binaryResponse;

    URI lastVisitURI;

    int retryCount = DEFAULT_RETRY_COUNT;

    String failureFlag; //used to flag some special text, when such text occur, it means that the request fail.

    public CrawlerClientBase(HttpClient httpClient, BrowserCompatibleRedirectHandler redirectHandler) {
        this.httpClient = httpClient;
        this.redirectHandler = redirectHandler;
    }

}
