package org.fortune.commons.core.http;


import org.apache.http.client.CookieStore;

/**
 * @author: landy
 * @date: 2019/11/20 22:22
 * @description:
 */
public class HttpBinaryResponse {

    final HttpStatusCode statusCode;
    final HttpHeaders headers;
    final byte[] responseContent;
    final Cookies cookies;

    public HttpBinaryResponse(HttpStatusCode statusCode, HttpHeaders headers, byte[] responseContent, CookieStore cookieStore) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.responseContent = responseContent;
        this.cookies = new Cookies(cookieStore);
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getResponseContent() {
        return responseContent;
    }

    public Cookies getCookies() {
        return cookies;
    }
}
