package org.fortune.commons.core.exception;

/**
 * @author: landy
 * @date: 2019/11/20 21:50
 * @description:
 */
public class HttpException extends RuntimeException {

    public HttpException(String message) {
        super(message);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }
}
