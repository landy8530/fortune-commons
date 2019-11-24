package org.fortune.commons.core.http;

import org.apache.http.client.CookieStore;

/**
 * @author: landy
 * @date: 2019/11/20 20:38
 * @description:
 */
public class HttpResponse {

    final HttpStatusCode statusCode;
    final Cookies cookies;
    final String responseText;
    final HttpHeaders headers;

    public HttpResponse(HttpStatusCode statusCode, String responseText, CookieStore cookieStore, HttpHeaders headers) {
        this.statusCode = statusCode;
        this.responseText = responseText;
        this.cookies = new Cookies(cookieStore);
        this.headers = headers;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public Cookies getCookies() {
        return cookies;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
}
