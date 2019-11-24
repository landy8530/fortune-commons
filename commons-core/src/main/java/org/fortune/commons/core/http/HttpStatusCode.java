package org.fortune.commons.core.http;

/**
 * @author: landy
 * @date: 2019/11/20 20:37
 * @description:
 */
public class HttpStatusCode {

    private final int statusCode;

    public HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isRedirect() {
        return this.statusCode == 302 || this.statusCode == 301 || this.statusCode == 307 || this.statusCode == 303;
    }

    public boolean isSuccess() {
        return this.statusCode >= 200 && this.statusCode <= 207;
    }

    public boolean isServerError() {
        return this.statusCode >= 500 && this.statusCode <= 507;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String toString() {
        return String.valueOf(this.statusCode);
    }
}
